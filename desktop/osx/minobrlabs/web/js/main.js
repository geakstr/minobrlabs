'use strict';

// About states
// state = 0 - disabled
// state = 1 - gauge
// state = 2 - label
// state = 3 - 3D axises
// state = 4 - horizontal bar
// state = 5 - vertical bar

var charts, xyzAxises, utils;

xyzAxises = ['x', 'y', 'z'];

utils = {
  normalizeVal : function(val) {
    if (Math.abs(val) - 0.005 < 0.0 + 0.005) {
      val = 0.0;
    }
    return val;
  }
};

// About states
// state = 0 - disabled
// state = 1 - gauge
// state = 2 - label
// state = 3 - 3D axises
// state = 4 - horizontal bar
// state = 5 - vertical bar

charts = {
  'microphone': {
    title: 'Звуковое давление',
    units : 'дб',
    val: 0,
    dom : document.getElementById('microphone'),    
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
    title: 'Перегрузка (g)',
    units : 'g',
    val: [0, 0, 0],
    dom : document.getElementById('accel'),    
    opts : {
      width: function(val) {
        return Math.min(Math.abs(val * 100.0 / charts.accel.opts.max), 100.0);
      },
      normalize: function(val) {
        return val / 9.8;
      },
      min: -2,
      max: 2
    },
    state : {
      curIndex : 1,
      states : [0, 3]
    }
  },
  'gyro': {
    title: 'Частота вращения (рад/с)',
    units : 'рад/с',
    val: [0, 0, 0],    
    dom : document.getElementById('gyro'),    
    opts : {
      width: function(val) {
        return Math.min(Math.abs(val * 100.0), 100.0);
      }      
    },
    state : {
      curIndex : 1,
      states : [0, 3]
    }
  },
  'airTemperature': {
    title: 'Температура воздуха',
    units : '°C',
    val: 0,
    dom : document.getElementById('airTemperature'),    
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
      states : [0, 1, 2]
    }
  },
  'humidity': {
    title: 'Относительная влажность',
    units : '%',
    val: 0,
    dom : document.getElementById('humidity'),    
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
      states : [0, 1, 2]
    }
  },    
  'atmoPressure': {
    title: 'Атмосферное давление',
    units : 'кПа',
    val: 0,
    dom : document.getElementById('atmoPressure'),    
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
      states : [0, 1, 2]
    }
  },
  'light': {
    title: 'Освещенность',
    units : 'лк',
    val: 0,
    dom : document.getElementById('light'),    
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
      states : [0, 1, 2]
    }
  },
  'soluteTemperature': {
    title: 'Температура раствора',
    units : '°C',
    val: 0,
    dom : document.getElementById('soluteTemperature'),    
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
      states : [0, 1, 2]
    }
  },
  'voltage': {
    title: 'Напряжение',
    units : 'В',
    val: 0,
    dom : document.getElementById('voltage'),    
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
      states : [0, 1, 2]
    }
  },
  'amperage': {
    title: 'Сила тока',
    units : 'А',
    val: 0,
    dom : document.getElementById('amperage'),    
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
      states : [0, 1, 2]
    }
  },
  'ph': {
    title: 'Водородный показатель',
    units : 'pH',
    val: 0,
    dom : document.getElementById('ph'),    
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
      states : [0, 1, 2]
    }
  }
};

function createElement(tag, className) {
  var node = document.createElement(tag);
  node.className = typeof className === 'undefined' ? '' : className;
  return node;
}

function createDisabledContainer(chart) {
  var container, title;

  container = createElement('div', 'chart-container');

  title = createElement('div', 'chart-title');
  title.innerHTML = chart.title + '<p class="chart-on">показать</p>';

  chart.dom.innerHTML = '';
  chart.dom.className = 'chart chart-disabled cf';
  chart.dom.appendChild(container);
  chart.dom.appendChild(title);

  return container;
}
function createLabelContainer(chart) {
  var container, title;

  container = createElement('div', 'chart-container');

  title = createElement('div', 'chart-title');
  title.textContent = chart.title;

  chart.dom.innerHTML = '';
  chart.dom.className = 'chart chart-label cf';
  chart.dom.appendChild(container);
  chart.dom.appendChild(title);

  return container;
}
function createAxisesContainer(chart) {
  var bar, container, signs, selector;

  signs = ['positive', 'negative'];

  container = {};

  chart.dom.innerHTML = '';
  chart.dom.className = 'chart chart-axises cf';
  signs.forEach(function(sign) {     
    container[sign] = {}; 

    var chartAxisesSign = createElement('div', 'chart-axises-' + sign);
    chart.dom.appendChild(chartAxisesSign);

    xyzAxises.forEach(function(axis) {
      container[sign][axis] = {};

      var chartAxisWrapper = createElement('div', 'chart-axis-wrapper cf');
      var chartAxis = createElement('div', 'chart-axis chart-' + axis + '-axis');
      var chartVal = createElement('span', 'chart-axis-val');

      chartAxis.appendChild(chartVal);
      chartAxisWrapper.appendChild(chartAxis);
      chartAxisesSign.appendChild(chartAxisWrapper);

      container[sign][axis].axis = chartAxis;
      container[sign][axis].val = chartVal;
    });
  });

  var chartLegend = createElement('ul', 'chart-legend');

  var chartLegendX = createElement('li');
  chartLegendX.textContent = 'x';

  var chartLegendY = createElement('li');
  chartLegendY.textContent = 'y';

  var chartLegendZ = createElement('li');
  chartLegendZ.textContent = 'z';

  chartLegend.appendChild(chartLegendX);
  chartLegend.appendChild(chartLegendY);
  chartLegend.appendChild(chartLegendZ);

  chart.dom.appendChild(chartLegend);

  var chartTitle = createElement('div', 'chart-title cf');
  chartTitle.textContent = chart.title;

  chart.dom.appendChild(chartTitle);

  return container;
}
function createGaugeContainer(chart) {
  var container, label, title;

  container = document.createElementNS('http://www.w3.org/2000/svg', 'svg');
  container.setAttribute('class', 'chart-container');

  label = createElement('div', 'chart-gauge-label');
  
  title = createElement('div', 'chart-title');
  title.textContent = chart.title;

  chart.dom.innerHTML = '';
  chart.dom.className = 'chart chart-gauge cf';
  chart.dom.appendChild(container);
  chart.dom.appendChild(label);
  chart.dom.appendChild(title);

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

function loadAxisesChart(chart) {
  var axis, val, sign, oppositeSign, i;

  for (i = 0; i < 3; i++) {
    axis = xyzAxises[i];
    val = chart.val[i];

    sign = val >= 0.0 ? 'positive' : 'negative';
    oppositeSign = val >= 0.0 ? 'negative' : 'positive';

    chart.container[sign][axis].val.style.display = 'inline';
    chart.container[sign][axis].val.textContent = val.toFixed(2);  
    chart.container[oppositeSign][axis].val.style.display = 'none';

    chart.container[sign][axis].axis.classList.remove('hidden');
    chart.container[sign][axis].axis.style.width = chart.opts.width(val) + '%';
    chart.container[oppositeSign][axis].axis.style.width = 0;
    chart.container[oppositeSign][axis].axis.classList.add('hidden');
  }
}
function loadGaugeChart(chart) {
  chart.container.load({
    columns: [['data', chart.val]]
  });
}
function loadLabelChart(chart) {
  chart.container.textContent = chart.opts.formatFunction(chart.val);
}

function loadCurrentChartState(chart) {
  switch (chart.state.states[chart.state.curIndex]) {
    case 1:
      loadGaugeChart(chart);
      break;
    case 2:
      loadLabelChart(chart);
      break;
    case 3:
      loadAxisesChart(chart);
    default:
      break;
  }
}
function nextChartState(chart) {
  chart.state.curIndex++;

  if (chart.state.curIndex === chart.state.states.length) {
    chart.state.curIndex = 0;    
  }

  switch (chart.state.states[chart.state.curIndex]) {
    case 0:
      chart.container = createDisabledContainer(chart);
      break;
    case 1:
      chart.container = createGaugeContainer(chart);
      break;
    case 2:
      chart.container = createLabelContainer(chart);
      break;
    case 3:
      chart.container = createAxisesContainer(chart);
      break;
    default:
      break;
  }

  loadCurrentChartState(chart);
}



// Microphone chart
function microphone(v) {
  charts.microphone.val = v[0];
  loadCurrentChartState(charts.microphone);
}

// Accelerometer chart
function accel(v) {
  charts.accel.val = v.map(charts.accel.opts.normalize).map(utils.normalizeVal);
  loadCurrentChartState(charts.accel);
}

// Gyroscope chart
function gyro(v) {
  charts.gyro.val = v.map(utils.normalizeVal);
  loadCurrentChartState(charts.gyro);
}

// Air temperature chart
function airTemperature(v) {
  charts.airTemperature.val = v[0];
  loadCurrentChartState(charts.airTemperature);
}

// Humidity chart
function humidity(v) {
  charts.humidity.val = v[0];
  loadCurrentChartState(charts.humidity);
}

// Light chart
function light(v) {
  charts.light.val = v[0];
  loadCurrentChartState(charts.light);
}




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

    nextChartState(charts[id]);
  };
  
  for (chart in charts) {
    if (charts.hasOwnProperty(chart)) {
      charts[chart].dom.onclick = onclick;
    }
  }
})();



microphone([43]);
accel([-0.5, 1, 0.3]);