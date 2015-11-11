var charts = {};
var xyzAxises = ['x', 'y', 'z'];

function composeAxisesChart(opts) {
  var chart, signs, selector;

  signs = ['positive', 'negative'];

  chart = {};

  opts.axises.forEach(function(axis) {
    chart[axis] = {};
    signs.forEach(function(sign) {      
      chart[axis][sign] = {};

      selector = '.chart-' + opts.name + ' .chart-axises-' + sign + ' .chart-' + axis + '-axis';
      chart[axis][sign].bar = document.querySelector(selector);
      chart[axis][sign].val = document.querySelector(selector + ' .chart-axis-val');
    });
  });

  return chart;
}

function composeGaugeChart(opts) {
  return c3.generate({
    bindto: '.chart-' + opts.name,
    data: {
      type: 'gauge',
      columns: [['data', 0]],   
    },
    gauge: {
      label: {
        format: opts.formatFunction,
      },
      min: opts.min,
      max: opts.max,
      width: 15
    },
    color: {
      pattern: opts.color.pattern,
      threshold: {
        values: opts.color.threshold
      }
    },
    interaction: {
      enabled: false
    },
    transition: {
      duration: null
    }
  });
}

var labels = {
  microphone : document.querySelector('.label-microphone')
}

charts.gyro = composeAxisesChart({
  name : 'gyro',
  axises : xyzAxises
});

charts.accel = composeAxisesChart({
  name : 'accel',
  axises : xyzAxises
});

charts.airTemperature = composeGaugeChart({
  name : 'air-temperature',
  formatFunction: function(value, ratio) {
    return (value == 0 ? '' : (value > 0 ? '+' : '-')) + value + ' °C';
  },
  min: -70,
  max: 70,
  color : {
    pattern: ['#0000FF', '#00FF00', '#FFFF00', '#FFA500', '#FF0000'],
    threshold: [1, 20, 30, 40, 50]
  }
});

charts.humidity = composeGaugeChart({
  name : 'humidity',
  formatFunction: function(value, ratio) {
    return value + ' %';
  },
  min: 0,
  max: 100,
  color : {
    pattern: ['#0000FF'],
    threshold: []
  }
});

charts.atmoPressure = composeGaugeChart({
  name : 'atmo-pressure',
  formatFunction: function(value, ratio) {
    return value + ' кПа';
  },
  min: 0,
  max: 300,
  color : {
    pattern: ['#0000FF'],
    threshold: []
  }
});

charts.light = composeGaugeChart({
  name : 'light',
  formatFunction: function(value, ratio) {
    return value + ' лк';
  },
  min: 0,
  max: 1000,
  color : {
    pattern: ['#FFC107'],
    threshold: []
  }
});

charts.soluteTemperature = composeGaugeChart({
  name : 'solute-temperature',
  formatFunction: function(value, ratio) {
    return (value == 0 ? '' : (value > 0 ? '+' : '-')) + value + ' °C';
  },
  min: -50,
  max: 1500,
  color : {
    pattern: ['#0000FF', '#00FF00', '#FFFF00', '#FFA500', '#FF0000'],
    threshold: [1, 300, 600, 900, 1200]
  }
});

charts.voltage = composeGaugeChart({
  name : 'voltage',
  formatFunction: function(value, ratio) {
    return value + ' В';
  },
  min: -30,
  max: 30,
  color : {
    pattern: ['#0000FF'],
    threshold: []
  }
});

charts.amperage = composeGaugeChart({
  name : 'amperage',
  formatFunction: function(value, ratio) {
    return value + ' А';
  },
  min: -1,
  max: 1,
  color : {
    pattern: ['#0000FF'],
    threshold: []
  }
});

charts.ph = composeGaugeChart({
  name : 'ph',
  formatFunction: function(value, ratio) {
    return value + ' pH';
  },
  min: 0,
  max: 14,
  color : {
    pattern: ['#0000FF'],
    threshold: []
  }
});

function airTemperature(v) {
  charts.airTemperature.load({
    columns: [['data', v[0]]]
  });
}

function humidity(v) {
  charts.humidity.load({
    columns: [['data', v[0]]]
  });
}

function light(v) {
  charts.light.load({
    columns: [['data', v[0]]]
  });
}

function microphone(v) {
  labels.microphone.textContent = v[0] + ' дб';
}

function gyro(vals) {
  axisesChart(charts.gyro, xyzAxises, vals, function(val) {
    return Math.min(Math.abs(val * 100.0), 100.0);
  });
}

function accel(vals) {
  vals = vals.map(function(val) {
    return val / 9.8;
  });

  axisesChart(charts.accel, xyzAxises, vals, function(val) {
    return Math.min(Math.abs(val * 100.0 / 2.0), 100.0);
  });
}

function axisesChart(chart, axises, vals, widthFunction) {
  var eps, axis, val, sign, oppositeSign, l, i;

  eps = 0.005;
  vals = vals.map(function(val) {
    if (Math.abs(val) - eps < 0.0 + eps) {
      val = 0.0;
    }
    return val;
  });
  
  l = axises.length;
  for (i = 0; i < l; i++) {
    axis = axises[i];
    val = vals[i];

    sign = val >= 0.0 ? 'positive' : 'negative';
    oppositeSign = val >= 0.0 ? 'negative' : 'positive';

    chart[axis][sign].val.style.display = 'inline';
    chart[axis][sign].val.textContent = val.toFixed(2);  
    chart[axis][oppositeSign].val.style.display = 'none';

    chart[axis][sign].bar.style.width = widthFunction(val) + '%';
    chart[axis][oppositeSign].bar.style.width = 0;
  }
}





