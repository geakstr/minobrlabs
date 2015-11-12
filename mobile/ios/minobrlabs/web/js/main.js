var charts, xyzAxises;

xyzAxises = ['x', 'y', 'z'];

// About states
// state = 0 - disabled
// state = 1 - gauge
// state = 2 - label
// state = 3 - horizontal bar
// state = 4 - vertical bar
// state = 5 - 3D axises

charts = {
  'microphone': {
    dom : document.getElementById('microphone'),
    units : 'дб',
    opts : {
      formatFunction: function(value, ratio) {
        return value + ' дб';
      },
      min : 40,
      max : 90,
      color : {
        pattern : ['#0000FF'],
        threshold : []
      }
    }, 
    state : {
      curIndex : 1,
      states : [0, 1, 2]
    }
  },
  'accel': {
    dom : document.getElementById('accel'),
    units : 'g',
    state : {
      curIndex : 1,
      states : [0, 5]
    }
  },
  'gyro': {
    dom : document.getElementById('gyro'),
    units : 'рад/с',
    state : {
      curIndex : 1,
      states : [0, 5]
    }
  },
  'airTemperature': {
    dom : document.getElementById('airTemperature'),
    units : '°C',
    opts : {
      formatFunction: function(value, ratio) {
        return (value == 0 ? '' : (value > 0 ? '+' : '-')) + value + ' °C';
      },
      min : -70,
      max : 70,
      color : {
        pattern: ['#0000FF', '#00FF00', '#FFFF00', '#FFA500', '#FF0000'],
        threshold: [1, 20, 30, 40, 50]
      }
    }, 
    state : {
      curIndex : 1,
      states : [0, 1, 2, 3, 4]
    }
  },
  'humidity': {
    dom : document.getElementById('humidity'),
    units : '%',
    opts : {
      formatFunction: function(value, ratio) {
        return value + ' %';
      },
      min: 0,
      max: 100,
      color : {
        pattern: ['#0000FF'],
        threshold: []
      }
    }, 
    state : {
      curIndex : 1,
      states : [0, 1, 2, 3, 4]
    }
  },    
  'atmoPressure': {
    dom : document.getElementById('atmoPressure'),
    units : 'кПа',
    opts : {
      formatFunction: function(value, ratio) {
        return value + ' кПа';
      },
      min: 0,
      max: 300,
      color : {
        pattern: ['#0000FF'],
        threshold: []
      }
    }, 
    state : {
      curIndex : 1,
      states : [0, 1, 2, 3, 4]
    }
  },
  'light': {
    dom : document.getElementById('light'),
    units : 'лк',
    opts : {
      formatFunction: function(value, ratio) {
        return value + ' лк';
      },
      min: 0,
      max: 1000,
      color : {
        pattern: ['#FFC107'],
        threshold: []
      }
    }, 
    state : {
      curIndex : 1,
      states : [0, 1, 2, 3, 4]
    }
  },
  'soluteTemperature': {
    dom : document.getElementById('soluteTemperature'),
    units : '°C',
    opts : {
      formatFunction: function(value, ratio) {
        return (value == 0 ? '' : (value > 0 ? '+' : '-')) + value + ' °C';
      },
      min: -50,
      max: 1500,
      color : {
        pattern: ['#0000FF', '#00FF00', '#FFFF00', '#FFA500', '#FF0000'],
        threshold: [1, 300, 600, 900, 1200]
      }
    }, 
    state : {
      curIndex : 1,
      states : [0, 1, 2, 3, 4]
    }
  },
  'voltage': {
    dom : document.getElementById('voltage'),
    units : 'В',
    opts : {
      formatFunction: function(value, ratio) {
        return (value == 0 ? '' : (value > 0 ? '+' : '-')) + value + ' В';
      },
      min: -30,
      max: 30,
      color : {
        pattern: ['#0000FF'],
        threshold: []
      }
    }, 
    state : {
      curIndex : 1,
      states : [0, 1, 2, 3, 4]
    }
  },
  'amperage': {
    dom : document.getElementById('amperage'),
    units : 'А',
    opts : {
      formatFunction: function(value, ratio) {
        return (value == 0 ? '' : (value > 0 ? '+' : '-')) + value + ' А';
      },
      min: -1,
      max: 1,
      color : {
        pattern: ['#0000FF'],
        threshold: []
      }
    }, 
    state : {
      curIndex : 1,
      states : [0, 1, 2, 3, 4]
    }
  },
  'ph': {
    dom : document.getElementById('ph'),
    units : 'pH',
    opts : {
      formatFunction: function(value, ratio) {
        return value + ' pH';
      },
      min: 0,
      max: 14,
      color : {
        pattern: ['#0000FF'],
        threshold: []
      }
    }, 
    state : {
      curIndex : 1,
      states : [0, 1, 2, 3, 4]
    }
  }
};









charts.microphone.container = createGaugeContainer(charts.microphone);

charts.accel.container = createAxisesContainer(charts.accel);
charts.gyro.container = createAxisesContainer(charts.gyro);

charts.airTemperature.container = createGaugeContainer(charts.airTemperature);
charts.humidity.container = createGaugeContainer(charts.humidity);
charts.atmoPressure.container = createGaugeContainer(charts.atmoPressure);
charts.light.container = createGaugeContainer(charts.light);
charts.soluteTemperature.container = createGaugeContainer(charts.soluteTemperature);
charts.voltage.container = createGaugeContainer(charts.voltage);
charts.amperage.container = createGaugeContainer(charts.amperage);
charts.ph.container = createGaugeContainer(charts.ph);


var clicks = 0;
(function() {
  var onclick, chart;

  onclick = function(e) {
    var node, id;

    node = e.target;
    while (node && node.parentNode && node.classList && !node.classList.contains('chart')) {
      node = node.parentNode;
    }

    id = node.id;
  };
  
  for (chart in charts) {
    if (charts.hasOwnProperty(chart)) {
      charts[chart].dom.onclick = onclick;
    }
  }
})();











function createLabelContainer(chart) {
  var container;

  container = document.createElement('div');
  container.className = 'chart-container';

  chart.dom.innerHTML = '';
  chart.dom.appendChild(container);

  return container;
}

function createAxisesContainer(chart) {
  var bar, container, signs, selector;

  signs = ['positive', 'negative'];

  container = {};

  xyzAxises.forEach(function(axis) {
    container[axis] = {};
    signs.forEach(function(sign) {      
      container[axis][sign] = {};

      bar = chart.dom.querySelector('.chart-axises-' + sign + ' .chart-' + axis + '-axis');

      container[axis][sign].bar = bar;
      container[axis][sign].val = bar.querySelector('.chart-axis-val');
    });
  });

  return container;
}

function createGaugeContainer(chart) {
  var parent, container, label;

  container = document.createElementNS('http://www.w3.org/2000/svg', 'svg');
  container.setAttribute('class', 'chart-container');

  label = document.createElement('div');
  label.className = 'chart-gauge-label';

  chart.dom.innerHTML = '';
  chart.dom.appendChild(container);
  chart.dom.appendChild(label);

  return c3.generate({
    bindto: container,
    data: {
      type: 'gauge',
      columns: [['data', 0]],   
    },
    gauge: {
      label: {
        format: chart.opts.formatFunction,
      },
      min: chart.opts.min,
      max: chart.opts.max,
      width: 15
    },
    color: {
      pattern: chart.opts.color.pattern,
      threshold: {
        values: chart.opts.color.threshold
      }
    },
    interaction: {
      enabled: false
    },
    transition: {
      duration: 300
    }
  });
}

function loadAxisesChart(chart, vals, widthFunction) {
  var eps, axis, val, sign, oppositeSign, i;

  eps = 0.005;
  vals = vals.map(function(val) {
    if (Math.abs(val) - eps < 0.0 + eps) {
      val = 0.0;
    }
    return val;
  });
  
  for (i = 0; i < 3; i++) {
    axis = xyzAxises[i];
    val = vals[i];

    sign = val >= 0.0 ? 'positive' : 'negative';
    oppositeSign = val >= 0.0 ? 'negative' : 'positive';

    chart.container[axis][sign].val.style.display = 'inline';
    chart.container[axis][sign].val.textContent = val.toFixed(2);  
    chart.container[axis][oppositeSign].val.style.display = 'none';

    chart.container[axis][sign].bar.classList.remove('hidden');
    chart.container[axis][sign].bar.style.width = widthFunction(val) + '%';
    chart.container[axis][oppositeSign].bar.style.width = 0;
    chart.container[axis][oppositeSign].bar.classList.add('hidden');
  }
}

function loadGaugeChart(chart, val) {
  chart.container.load({
    columns: [['data', val]]
  });
}

function loadLabelChart(chart, val) {
  chart.container.textContent = val + ' ' + chart.units;
}

function nextChartState(chart) {
  chart.state.curIndex++;

  if (chart.state.curIndex === chart.state.states.length) {
    chart.state.curIndex = 0;    
  }
}

// Microphone chart
function microphone(v) {
  loadGaugeChart(charts.microphone, v[0])
}

// Accelerometer chart
function accelBarWidthFunction(val) {
  return Math.min(Math.abs(val * 100.0 / 2.0), 100.0);
}

function accelNormalizeValFunction(val) {
  return val / 9.8;
}

function accel(vals) {
  loadAxisesChart(charts.accel, vals.map(accelNormalizeValFunction), accelBarWidthFunction);
}

// Gyroscope chart
function gyroBarWidthFunction(val) {
  return Math.min(Math.abs(val * 100.0), 100.0);
}

function gyro(vals) {
  loadAxisesChart(charts.gyro, vals, gyroBarWidthFunction);
}

// Air temperature chart
function airTemperature(v) {
  loadGaugeChart(charts.airTemperature, v[0]);
}

// Humidity chart
function humidity(v) {
  loadGaugeChart(charts.humidity, v[0]);
}

// Light chart
function light(v) {
  loadGaugeChart(charts.light, v[0]);
}
