'use strict';

// About states
// state = 0 - disabled
// state = 1 - gauge
// state = 2 - label
// state = 3 - 3D axises
// state = 4 - horizontal bar
// state = 5 - vertical bar

var pages, charts, stats, xyzAxises, utils, os;

var i = 0;

xyzAxises = ['x', 'y', 'z'];

utils = {
  normalizeVal : function(val) {
    if (Math.abs(val) - 0.005 < 0.0 + 0.005) {
      val = 0.0;
    }
    return val;
  },
  width: function(name, val) {
    return Math.min(Math.abs((val - charts[name].opts.min) * 100.0 / (charts[name].opts.max - charts[name].opts.min)), 100.0);
  }
};

pages = {
  active: 'mainPage',
  mainPage: document.getElementById('main-page'),
  statsPage: document.getElementById('stats-page')
};

charts = {
  'microphone': {
    name: 'microphone',
    title: 'Звуковое давление',
    units : 'дб',
    disabled: false,
    val: 0,
    dom : document.getElementById('microphone'),    
    opts : {
      width: function(val) {
        return utils.width('microphone', val);
      },
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
      states : [0, 1, 2, 4, 5]
    }
  },
  'accel': {
    name: 'accel',
    title: 'Перегрузка',
    units : 'g',
    disabled: false,
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
    name: 'gyro',
    title: 'Частота вращения',
    units : 'рад/с',
    disabled: false,
    val: [0, 0, 0],    
    dom : document.getElementById('gyro'),    
    opts : {
      width: function(val) {
        return Math.min(Math.abs(val * 100.0 / charts.gyro.opts.max), 100.0);
      },
      min: -1,
      max: 1      
    },
    state : {
      curIndex : 1,
      states : [0, 3]
    }
  },
  'airTemperature': {
    name: 'airTemperature',
    title: 'Температура воздуха',
    units : '°C',
    disabled: false,
    val: 0,
    dom : document.getElementById('airTemperature'),    
    opts : {
      width: function(val) {
        return utils.width('airTemperature', val);
      },
      formatFunction: function(value, ratio) {
        return (value == 0 ? '' : (value > 0 ? '+' : '-')) + value + ' °C';
      },
      min : -70,
      max : 70,
      color : {
        pattern: ['#0000FF', '#00FF00', '#FFFF00', '#FFA500', '#FF0000'],
        threshold: [1, 20, 30, 40]
      }
    }, 
    state : {
      curIndex : 1,
      states : [0, 1, 2, 4, 5]
    }
  },
  'humidity': {
    name: 'humidity',
    title: 'Относительная влажность',
    units : '%',
    disabled: false,
    val: 0,
    dom : document.getElementById('humidity'),    
    opts : {
      width: function(val) {
        return utils.width('humidity', val);
      },
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
      states : [0, 1, 2, 4, 5]
    }
  },    
  'atmoPressure': {
    name: 'atmoPressure',
    title: 'Атмосферное давление',
    units : 'кПа',
    disabled: false,
    val: 0,
    dom : document.getElementById('atmoPressure'),    
    opts : {
      width: function(val) {
        return utils.width('atmoPressure', val);
      },
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
      states : [0, 1, 2, 4, 5]
    }
  },
  'light': {
    name: 'light',
    title: 'Освещенность',
    units : 'лк',
    disabled: false,
    val: 0,
    dom : document.getElementById('light'),    
    opts : {
      width: function(val) {
        return utils.width('light', val);
      },
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
      states : [0, 1, 2, 4, 5]
    }
  },
  'soluteTemperature': {
    name: 'soluteTemperature',
    title: 'Температура раствора',
    units : '°C',
    disabled: false,
    val: 0,
    dom : document.getElementById('soluteTemperature'),    
    opts : {
      width: function(val) {
        return utils.width('soluteTemperature', val);
      },
      formatFunction: function(value, ratio) {
        return (value == 0 ? '' : (value > 0 ? '+' : '-')) + value + ' °C';
      },
      min: -50,
      max: 1500,
      color : {
        pattern: ['#0000FF', '#00FF00', '#FFFF00', '#FFA500', '#FF0000'],
        threshold: [1, 400, 800, 1200]
      }
    }, 
    state : {
      curIndex : 1,
      states : [0, 1, 2, 4, 5]
    }
  },
  'voltage': {
    name: 'voltage',
    title: 'Напряжение',
    units : 'В',
    disabled: false,
    val: 0,
    dom : document.getElementById('voltage'),    
    opts : {
      width: function(val) {
        return utils.width('voltage', val);
      },
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
      states : [0, 1, 2, 4, 5]
    }
  },
  'amperage': {
    name: 'amperage',
    title: 'Сила тока',
    units : 'А',
    disabled: false,
    val: 0,
    dom : document.getElementById('amperage'),    
    opts : {
      width: function(val) {
        return utils.width('amperage', val);
      },
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
      states : [0, 1, 2, 4, 5]
    }
  },
  'ph': {
    name: 'ph',
    title: 'Водородный показатель',
    units : 'pH',
    disabled: false,
    val: 0,
    dom : document.getElementById('ph'),    
    opts : {
      width: function(val) {
        return utils.width('ph', val);
      },
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
      states : [0, 1, 2, 4, 5]
    }
  }
};


stats = {
  chart : null,
  currentChart: null,
  recording: false,
  mode: 'realtime', // "realtime" or "experiment"
  data: {
    microphone: [],
    accel: [],
    gyro: [],
    airTemperature: [],
    humidity: [],
    atmoPressure: [],
    light: [],
    soluteTemperature: [],
    voltage: [],
    amperage: [],
    ph: []
  },
  opts: {
    microphone: {
      labels: ['Время', charts.microphone.title + ' (' + charts.microphone.units + ')']
    },
    accel: {
      labels: ['Время', 'X', 'Y', 'Z']
    },
    gyro: {
      labels: ['Время', 'X', 'Y', 'Z']
    },
    airTemperature: {
      labels: ['Время', charts.airTemperature.title + ' (' + charts.airTemperature.units + ')']
    },
    humidity: {
      labels: ['Время', charts.humidity.title + ' (' + charts.humidity.units + ')']
    },
  }
};

function showMainPage() {
  var chart;

  pages.active = 'mainPage';

  pages.mainPage.style.display = 'block';
  pages.statsPage.style.display = 'none';

  for (chart in charts) {
    if (charts.hasOwnProperty(chart)) {
      createCurrentChartState(charts[chart]);
    }
  }
}

function showStatsPage() {
  pages.active = 'statsPage';

  pages.mainPage.style.display = 'none';
  pages.statsPage.style.display = 'block';

  createDygraph();
}

function clear() {
  stats.data =  {
    microphone: [],
    accel: [],
    gyro: [],
    airTemperature: [],
    humidity: [],
    atmoPressure: [],
    light: [],
    soluteTemperature: [],
    voltage: [],
    amperage: [],
    ph: []
  };
}

function setRealtime() {
  stats.mode = "realtime";
  clear();
  createDygraph();  
}

function isRecording(flag) {
  stats.recording = flag;
}

function createElement(tag, className) {
  var node = document.createElement(tag);
  node.className = typeof className === 'undefined' ? '' : className;
  return node;
}

function createDygraph() {
  if (stats.data[stats.currentChart].length > 0) {
    stats.chart = new Dygraph(
        document.getElementById("chart-stats"),
        stats.data[stats.currentChart],
        {
          legend: "always",
          drawPoints: false,
          labels: stats.opts[stats.currentChart].labels,
          axes: {
            x: {
              axisLabelFormatter: function (d, gran) {
                var hours = Dygraph.zeropad(d.getHours());
                var mins = Dygraph.zeropad(d.getMinutes());
                var secs = Dygraph.zeropad(d.getSeconds());
                return hours + ":" + mins + ":" + secs;
              },
              valueFormatter: function (ms) {
                var d = new Date(ms);
                var date = Dygraph.zeropad(d.getDate());
                var month = Dygraph.zeropad(d.getMonth() + 1);
                var year = d.getFullYear();
                var hours = Dygraph.zeropad(d.getHours());
                var mins = Dygraph.zeropad(d.getMinutes());
                var secs = Dygraph.zeropad(d.getSeconds());
                return date + "." + month + "." + year + " " + hours + ":" + mins + ":" + secs;
              }
            }
          }
        }
    );
    stats.chart.updateOptions({
      interactionModel:{
        mousedown: Dygraph.defaultInteractionModel.mousedown, 
        mousemove: Dygraph.defaultInteractionModel.mousemove, 
        mouseup: Dygraph.defaultInteractionModel.mouseup, 
        touchstart: CustomDygraphsInteractionModel.startTouch, 
        touchend: CustomDygraphsInteractionModel.endTouch, 
        touchmove: CustomDygraphsInteractionModel.moveTouch
      } 
    });
  }
}

function createDisabledContainer(chart) {
  var container, title;

  container = createElement('div', 'chart-container');

  title = createElement('div', 'chart-title');
  title.innerHTML = chart.title + '<br><p class="chart-on">включить</p>';

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
function createAxisContainer(chart, axis) {
  var container = createElement('div', 'chart-container');

  var chartAxisWrapper = createElement('div', 'chart-axis-wrapper chart-axis-wrapper-' + axis);
  var chartAxis = createElement('div', 'chart-axis-axis');
  var chartVal = createElement('span', 'chart-axis-val');
  var chartTitle = createElement('div', 'chart-title');
  chartTitle.textContent = chart.title;

  var chartMinVal = createElement('span', 'chart-axis-min-val');
  chartMinVal.textContent = chart.opts.min;

  var chartMaxVal = createElement('span', 'chart-axis-max-val');
  chartMaxVal.textContent = chart.opts.max;

  chartAxisWrapper.appendChild(chartVal);
  chartAxisWrapper.appendChild(chartMinVal);
  chartAxisWrapper.appendChild(chartMaxVal);
  chartAxisWrapper.appendChild(chartAxis);
  container.appendChild(chartAxisWrapper);
  container.appendChild(chartTitle);

  chart.dom.innerHTML = '';
  chart.dom.className = 'chart chart-axis';
  chart.dom.appendChild(container);  

  return {
    axis : chartAxis,
    val : chartVal
  };
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
      var chartAxis = createElement('div', 'chart-axis-axis chart-' + axis + '-axis');
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
  chartTitle.textContent = chart.title + ' (' + chart.units + ')';

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
      duration: null
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
function loadAxisChart(chart, axis) {
  chart.container.val.textContent = chart.opts.formatFunction(chart.val);
  chart.container.axis.style[axis === 'x' ? 'width' : 'height'] = chart.opts.width(chart.val) + '%';

  chart.container.axis.style.backgroundColor = (function() {
      var val = chart.val;
      var pattern = chart.opts.color.pattern;
      var threshold = chart.opts.color.threshold;

      var patternLength = pattern.length;
      var thresholdLength = threshold.length;

      if (thresholdLength === 0 || (thresholdLength > 0 && val < threshold[0])) {
        return patternLength === 0 ? 'blue' : pattern[0];
      }

      var i = 0;
      for (i = 1; i < thresholdLength && i < patternLength; i++) {
        if (val >= threshold[i - 1] && val < threshold[i]) {
          return pattern[i];
        }
      }

      return patternLength === 0 ? 'blue' : pattern[patternLength - 1];
    })();
}
function loadGaugeChart(chart) {
  chart.container.load({
    columns: [['data', chart.val]]
  });
}
function loadLabelChart(chart) {
  chart.container.textContent = chart.opts.formatFunction(chart.val);
}

function loadCurrentChartState(chart, mills) {
  if (pages.active === 'mainPage') {
    switch (chart.state.states[chart.state.curIndex]) {
      case 1:
        loadGaugeChart(chart);
        break;
      case 2:
        loadLabelChart(chart);
        break;
      case 3:
        loadAxisesChart(chart);
        break;
      case 4:
        loadAxisChart(chart, 'x');
        break;
      case 5:
        loadAxisChart(chart, 'y');
        break;
      default:
        break;
    }
  }

  if (stats.mode === 'realtime') {
    if (typeof mills === 'undefined') {
      return;
    }

    stats.data[chart.name].push([new Date(mills)].concat(chart.val));
    if (pages.active === 'statsPage') {
      if (chart.name === stats.currentChart) {
        if (null === stats.chart) {
          createDygraph();
        } else {
          stats.chart.updateOptions({
            'file': stats.data[chart.name]
          });
        }
      }
    }
  }
}

function loadExperiment(name, data) {
  var stat;

  stats.mode = 'experiment';
  showStatsPage();

  for (stat in data) {
    if (data.hasOwnProperty(stat)) {
      stats.data[stat] = data[stat].map(function(a) {
        return [new Date(a.date)].concat(JSON.parse(a.vals));
      });
    }
  }

  stats.chart.updateOptions({
    'file': stats.data[stats.currentChart]
  });
} 

function createNextChartState(chart) {
  chart.state.curIndex++;

  if (chart.state.curIndex === chart.state.states.length) {
    chart.state.curIndex = 0;    
  }

  createCurrentChartState(chart);

  if (os === 'android') {
    Android.updateState(chart.name, chart.state.states[chart.state.curIndex]);
  }
}
function createCurrentChartState(chart) {
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
    case 4:
      chart.container = createAxisContainer(chart, 'x');
      break;
    case 5:
      chart.container = createAxisContainer(chart, 'y');
      break;
    default:
      break;
  }

  loadCurrentChartState(chart);
}

function microphone(v, mills) {
  charts.microphone.val = v[0];
  loadCurrentChartState(charts.microphone, mills);
}

function accel(v, mills) {
  charts.accel.val = v.map(charts.accel.opts.normalize).map(utils.normalizeVal);
  loadCurrentChartState(charts.accel, mills);
}

function gyro(v, mills) {
  charts.gyro.val = v.map(utils.normalizeVal);
  loadCurrentChartState(charts.gyro, mills);
}

function airTemperature(v, mills) {
  charts.airTemperature.val = v[0];
  loadCurrentChartState(charts.airTemperature, mills);
}

function humidity(v, mills) {
  charts.humidity.val = v[0];
  loadCurrentChartState(charts.humidity, mills);
}

function atmoPressure(v, mills) {
  charts.atmoPressure.val = v[0];
  loadCurrentChartState(charts.atmoPressure, mills);
}

function light(v, mills) {
  charts.light.val = v[0];
  loadCurrentChartState(charts.light, mills);
}

function soluteTemperature(v, mills) {
  charts.soluteTemperature.val = v[0];
  loadCurrentChartState(charts.soluteTemperature, mills);
}

function voltage(v, mills) {
  charts.voltage.val = v[0];
  loadCurrentChartState(charts.voltage, mills);
}

function amperage(v, mills) {
  charts.amperage.val = v[0];
  loadCurrentChartState(charts.amperage, mills);
}

function ph(v, mills) {
  charts.ph.val = v[0];
  loadCurrentChartState(charts.ph, mills);
}

function init(config) {
  var chartOnClick, filterOnClick, chart, idx, i, l;

  os = config.os;

  for (chart in config.charts) {
    if (config.charts.hasOwnProperty(chart)) {
      idx = charts[chart].state.states.indexOf(config.charts[chart]);
      charts[chart].state.curIndex = idx === -1 ? charts[chart].state.curIndex : idx;
      createCurrentChartState(charts[chart]);
    }
  }

  chartOnClick = function(e) {
    var node, id;

    node = e.target;
    while (node && node.parentNode && node.classList && !node.classList.contains('chart')) {
      node = node.parentNode;
    }

    id = node.id;

    createNextChartState(charts[id]);
  };
  
  for (chart in charts) {
    if (charts.hasOwnProperty(chart)) {
      charts[chart].dom.onclick = chartOnClick;
    }
  }

  stats.currentChart = 'microphone';  


  filterOnClick = function(e) {
    var param = e.target.value;
    stats.currentChart = param;
    createDygraph();
  };

  var filters = document.getElementsByName("filter");
  for (i = 0, l = filters.length; i < l; i++) {
    filters[i].onclick = filterOnClick;
  }
}

//init({"os":"browser","charts":{"microphone":2,"accel":3,"gyro":3,"airTemperature":4,"humidity":1,"atmoPressure":0,"light":2,"soluteTemperature":1,"voltage":5,"amperage":1,"ph":1}});

if (os === 'browser') {
  showStatsPage();
  microphone([40], 87400000);
  microphone([50], 87400100);
  microphone([60], 87400200);
  microphone([10], 87400300);
  // accel([1, 2, 3]);
  // accel([5, 1, 2]);
}
