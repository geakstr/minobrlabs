CustomDygraphsInteractionModel = {
  startTouch : function(event, g, context) {
    this.wasInteract = true;
    
    event.preventDefault();  // touch browsers are all nice.
    if (event.touches.length > 1) {
      // If the user ever puts two fingers down, it's not a double tap.
      context.startTimeForDoubleTapMs = null;
    }

    var touches = [];
    for (var i = 0; i < event.touches.length; i++) {
      var t = event.touches[i];
      // we dispense with 'dragGetX_' because all touchBrowsers support pageX
      touches.push({
        pageX: t.pageX,
        pageY: t.pageY,
        dataX: g.toDataXCoord(t.pageX),
        dataY: g.toDataYCoord(t.pageY)
        // identifier: t.identifier
      });
    }
    context.initialTouches = touches;

    if (touches.length == 1) {
      // This is just a swipe.
      context.initialPinchCenter = touches[0];
      context.touchDirections = { x: true, y: true };

      var width = document.querySelector("#stats-page #params").offsetWidth;
      var closestTouchP = g.findClosestPoint(touches[0].pageX - width, touches[0].pageY); 
      if (closestTouchP) { 
        var selectionChanged = g.setSelection(closestTouchP.row, closestTouchP.seriesName); 
      } 
    } else if (touches.length >= 2) {
      // It's become a pinch!
      // In case there are 3+ touches, we ignore all but the "first" two.

      // only screen coordinates can be averaged (data coords could be log scale).
      context.initialPinchCenter = {
        pageX: 0.5 * (touches[0].pageX + touches[1].pageX),
        pageY: 0.5 * (touches[0].pageY + touches[1].pageY),

        // TODO(danvk): remove
        dataX: 0.5 * (touches[0].dataX + touches[1].dataX),
        dataY: 0.5 * (touches[0].dataY + touches[1].dataY)
      };

      // Make pinches in a 45-degree swath around either axis 1-dimensional zooms.
      var initialAngle = 180 / Math.PI * Math.atan2(
          context.initialPinchCenter.pageY - touches[0].pageY,
          touches[0].pageX - context.initialPinchCenter.pageX);

      // use symmetry to get it into the first quadrant.
      initialAngle = Math.abs(initialAngle);
      if (initialAngle > 90) initialAngle = 90 - initialAngle;

      context.touchDirections = {
        x: (initialAngle < (90 - 45/2)),
        y: (initialAngle > 45/2)
      };
    }

    // save the full x & y ranges.
    context.initialRange = {
      x: g.xAxisRange(),
      y: g.yAxisRange()
    };
  },
  endTouch : function(event, g, context) {
    if (event.touches.length !== 0) {
      // this is effectively a "reset"
      CustomDygraphsInteractionModel.startTouch(event, g, context);
    } else if (event.changedTouches.length == 1) {
      // Could be part of a "double tap"
      // The heuristic here is that it's a double-tap if the two touchend events
      // occur within 500ms and within a 50x50 pixel box.

      var now = new Date().getTime();
      var t = event.changedTouches[0];
      if (context.startTimeForDoubleTapMs &&
          now - context.startTimeForDoubleTapMs <= 250 &&
          context.doubleTapX && Math.abs(context.doubleTapX - t.screenX) < 50 &&
          context.doubleTapY && Math.abs(context.doubleTapY - t.screenY) < 50) {
        g.resetZoom();
        this.wasInteract = false;
        // Fix doubletap
        context.startTimeForDoubleTapMs = null;
      } else {
        context.startTimeForDoubleTapMs = now;
        context.doubleTapX = t.screenX;
        context.doubleTapY = t.screenY;
      }
    }
  },
  moveTouch : function(event, g, context) {
    // If the tap moves, then it's definitely not part of a double-tap.
    // Fix double tap
    //context.startTimeForDoubleTapMs = null;

    this.wasInteract = true;

    var i, touches = [];
    for (i = 0; i < event.touches.length; i++) {
      var t = event.touches[i];
      touches.push({
        pageX: t.pageX,
        pageY: t.pageY
      });
    }
    var initialTouches = context.initialTouches;

    var c_now;

    // old and new centers.
    var c_init = context.initialPinchCenter;
    if (touches.length == 1) {
      c_now = touches[0];
    } else {
      c_now = {
        pageX: 0.5 * (touches[0].pageX + touches[1].pageX),
        pageY: 0.5 * (touches[0].pageY + touches[1].pageY)
      };
    }

    // this is the "swipe" component
    // we toss it out for now, but could use it in the future.
    var swipe = {
      pageX: c_now.pageX - c_init.pageX,
      pageY: c_now.pageY - c_init.pageY
    };
    var dataWidth = context.initialRange.x[1] - context.initialRange.x[0];
    var dataHeight = context.initialRange.y[0] - context.initialRange.y[1];
    swipe.dataX = (swipe.pageX / g.plotter_.area.w) * dataWidth;
    swipe.dataY = (swipe.pageY / g.plotter_.area.h) * dataHeight;
    var xScale, yScale;

    // The residual bits are usually split into scale & rotate bits, but we split
    // them into x-scale and y-scale bits.
    if (touches.length == 1) {
      xScale = 1.0;
      yScale = 1.0;
    } else if (touches.length >= 2) {
      var initHalfWidth = (initialTouches[1].pageX - c_init.pageX);
      xScale = (touches[1].pageX - c_now.pageX) / initHalfWidth;

      var initHalfHeight = (initialTouches[1].pageY - c_init.pageY);
      yScale = (touches[1].pageY - c_now.pageY) / initHalfHeight;
    }

    // Clip scaling to [1/8, 8] to prevent too much blowup.
    xScale = Math.min(8, Math.max(0.125, xScale));
    yScale = Math.min(8, Math.max(0.125, yScale));

    var didZoom = false;
    if (context.touchDirections.x) {
      g.dateWindow_ = [
        c_init.dataX - swipe.dataX + (context.initialRange.x[0] - c_init.dataX) / xScale,
        c_init.dataX - swipe.dataX + (context.initialRange.x[1] - c_init.dataX) / xScale
      ];
      didZoom = true;
    }
    
    if (context.touchDirections.y) {
      for (i = 0; i < 1  /*g.axes_.length*/; i++) {
        var axis = g.axes_[i];
        var logscale = g.attributes_.getForAxis("logscale", i);
        if (logscale) {
          // TODO(danvk): implement
        } else {
          axis.valueWindow = [
            c_init.dataY - swipe.dataY + (context.initialRange.y[0] - c_init.dataY) / yScale,
            c_init.dataY - swipe.dataY + (context.initialRange.y[1] - c_init.dataY) / yScale
          ];
          didZoom = true;
        }
      }
    }

    g.drawGraph_(false);

    // We only call zoomCallback on zooms, not pans, to mirror desktop behavior.
    if (didZoom && touches.length > 1 && g.getFunctionOption('zoomCallback')) {
      var viewWindow = g.xAxisRange();
      g.getFunctionOption("zoomCallback").call(g, viewWindow[0], viewWindow[1], g.yAxisRanges());
    }
  }
};