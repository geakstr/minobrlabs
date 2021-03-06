'use strict';

// About states
// state =-1 - unavailable
// state = 0 - disabled
// state = 1 - gauge
// state = 2 - label
// state = 3 - 3D axises
// state = 4 - horizontal bar
// state = 5 - vertical bar

var pages, charts, updates, charts_ids, chartsOrder, stats, xyzAxises, utils, os;

updates = {};

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

chartsOrder = ['microphone', 'accel', 'gyro', 'light', 'airTemperature', 'humidity', 'atmoPressure', 'soluteTemperature', 'voltage', 'amperage', 'ph'];

charts_ids = {
  15: "humidity",
  11: "airTemperature",
  5: "light",
  4: "gyro",
  1: "accel",
  14: "atmoPressure",
  12: "amperage",
  16: "ph",
  17: "soluteTemperature",
  13: "voltage",
  18: "microphone"
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
      min : 20,
      max : 90,
      valueRange : [0, 120],
      color : {
        pattern : ['#0000FF'],
        threshold : []
      }
    }, 
    state : {
      curIndex : 1,
      states : [-1, 1, 2, 4, 5, 0]
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
      min: -4,
      max: 4,
      valueRange : [-4, 4]
    },
    state : {
      curIndex : 1,
      states : [-1, 3, 0]
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
      min: -10,
      max: 10,
      valueRange : [-15, 15],
    },
    state : {
      curIndex : 1,
      states : [-1, 3, 0]
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
      valueRange : [-100, 100],
      color : {
        pattern: ['#0000FF', '#00FF00', '#FFFF00', '#FFA500', '#FF0000'],
        threshold: [1, 20, 30, 40]
      }
    }, 
    state : {
      curIndex : 1,
      states : [-1, 5, 4, 2, 1, 0]
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
      valueRange : [-10, 110],
      color : {
        pattern: ['#0000FF'],
        threshold: []
      }
    }, 
    state : {
      curIndex : 1,
      states : [-1, 4, 5, 2, 1, 0]
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
      valueRange : [-20, 320],
      color : {
        pattern: ['#0000FF'],
        threshold: []
      }
    }, 
    state : {
      curIndex : 1,
      states : [-1, 5, 4, 2, 1, 0]
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
      valueRange : [-50, 1050],
      color : {
        pattern: ['#FFC107'],
        threshold: []
      }
    }, 
    state : {
      curIndex : 1,
      states : [-1, 1, 2, 4, 5, 0]
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
      valueRange : [-100, 1600],
      color : {
        pattern: ['#0000FF', '#00FF00', '#FFFF00', '#FFA500', '#FF0000'],
        threshold: [1, 400, 800, 1200]
      }
    }, 
    state : {
      curIndex : 1,
      states : [-1, 5, 4, 2, 1, 0]
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
      valueRange : [-50, 50],
      color : {
        pattern: ['#0000FF'],
        threshold: []
      }
    }, 
    state : {
      curIndex : 1,
      states : [-1, 2, 4, 5, 1, 0]
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
      valueRange : [-3, 3],
      color : {
        pattern: ['#0000FF'],
        threshold: []
      }
    }, 
    state : {
      curIndex : 1,
      states : [-1, 2, 4, 5, 1, 0]
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
      valueRange : [-5, 20],
      color : {
        pattern: ['#0000FF'],
        threshold: []
      }
    }, 
    state : {
      curIndex : 1,
      states : [-1, 1, 2, 4, 5, 0]
    }
  }
};


stats = {
  chart : null,
  currentChart: null,
  recording: false,
  mode: 'realtime', // "realtime" or "experiment"
  experiment: -1,
  annotations: {},
  plotter : null,
  wasInteract: false,
  window: [20000, 4000],
  windowIdx: 0,
  dom: {
    params: document.getElementById("params"),
    chart: document.getElementById("chart-stats")
  },
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
    atmoPressure: {
      labels: ['Время', charts.atmoPressure.title + ' (' + charts.atmoPressure.units + ')']
    },
    light: {
      labels: ['Время', charts.light.title + ' (' + charts.light.units + ')']
    },
    soluteTemperature: {
      labels: ['Время', charts.soluteTemperature.title + ' (' + charts.soluteTemperature.units + ')']
    },
    voltage: {
      labels: ['Время', charts.voltage.title + ' (' + charts.voltage.units + ')']
    },
    amperage: {
      labels: ['Время', charts.amperage.title + ' (' + charts.amperage.units + ')']
    },
    ph: {
      labels: ['Время', charts.ph.title + ' (' + charts.ph.units + ')']
    }
  }
};

function showMainPage() {
  var chart;

  pages.active = 'mainPage';

  pages.mainPage.style.visibility = 'visible';
  pages.statsPage.style.visibility = 'hidden';

  for (chart in charts) {
    if (charts.hasOwnProperty(chart)) {
      createCurrentChartState(charts[chart]);
    }
  }
}

function showStatsPage() {
  pages.active = 'statsPage';

  pages.mainPage.style.visibility = 'hidden';
  pages.statsPage.style.visibility = 'visible';

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

function zeropad(x) {
  if (x < 10) return "0" + x; else return "" + x;
}

function createDygraph() {
  if (stats.data[stats.currentChart].length > 0) {
    var opts = {
      legend: "always",
      drawPoints: false,
      labels: stats.opts[stats.currentChart].labels,
      includeZero: true,
      plotter: stats.plotter,
      valueRange: charts[stats.currentChart].opts.valueRange,
      axes: {
        x: {
          axisLabelFormatter: function (d, gran) {
            var hours = zeropad(d.getHours());
            var mins = zeropad(d.getMinutes());
            var secs = zeropad(d.getSeconds());
            return hours + ":" + mins + ":" + secs;
          },
          valueFormatter: function (ms) {
            var d = new Date(ms);
            var date = zeropad(d.getDate());
            var month = zeropad(d.getMonth() + 1);
            var year = d.getFullYear();
            var hours = zeropad(d.getHours());
            var mins = zeropad(d.getMinutes());
            var secs = zeropad(d.getSeconds());
            return date + "." + month + "." + year + " " + hours + ":" + mins + ":" + secs;
          }
        }
      }
    };

    if (stats.mode === 'experiment') {
      if (os === 'android') {
        opts.interactionModel = {
          touchstart: CustomDygraphsInteractionModel.startTouch.bind(stats), 
          touchend: CustomDygraphsInteractionModel.endTouch.bind(stats), 
          touchmove: CustomDygraphsInteractionModel.moveTouch.bind(stats)
        };
      }
    } else {
      opts.interactionModel = {};
      opts.dateWindow = getStatsChartDateWindow(charts[stats.currentChart]);
    }

    stats.chart = new Dygraph(
        stats.dom.chart,
        stats.data[stats.currentChart],
        opts
    );    

    stats.chart.updateOptions({
      annotationClickHandler: (function() {
        var latestAnn, latestTime, i, l, search;
        return function(ann, point, dg, event) {
          var now = new Date().getTime();
          if (latestAnn
            && ann.x === latestAnn.x
            && ann.series === latestAnn.series
            && now - latestTime <= 250) {
            latestAnn = null;
            latestTime = now;

            for (i = 0, l = stats.annotations[ann.chart].length; i < l; i++) {
              if (stats.annotations[ann.chart][i].x === ann.x) {                
                stats.annotations[ann.chart].splice(i, 1);
                if (os === "android") {
                  Android.removeAnnotation(stats.currentChart, stats.experiment, ann.x);
                }
                break;
              }
            }
            stats.chart.setAnnotations(stats.annotations[ann.chart]);
          } else {
            latestAnn = ann;
            latestTime = now;
          }
        };
      })()
    });

    if (stats.annotations[stats.currentChart]) {
      stats.chart.setAnnotations(stats.annotations[stats.currentChart]);
    }    
  } else {
    stats.chart = null;
    stats.dom.chart.innerHTML = '<div class="message">Нет данных</div>';
  }
}

function setAnnotation(chartName, time, text, series) {
  if (!stats.annotations[chartName]) {
    stats.annotations[chartName] = [];
  }

  stats.annotations[chartName].push({
    series: series,
    width: text.length * 9,
    height: 16,
    x: time,
    shortText: text,
    text: text,
    tickHeight: 33,
    attachAtBottom: false,
    cssClass: 'stats-annotation',
    chart: chartName
  });

  if (stats.currentChart === chartName) {
    stats.chart.setAnnotations(stats.annotations[chartName]);
  }  
}

function getStatsChartDateWindow(chart) {
  var data = stats.data[chart.name];
  var lastDate = data[data.length - 1][0].getTime();
  var startDate = new Date(lastDate - stats.window[stats.mode === 'realtime' ? stats.windowIdx : 0]);
  return [startDate, new Date(lastDate)];
}
function createParamsState() {
  stats.dom.params.innerHTML = '';
}
function addParamToParamsState(param) {
  var li = createElement("li");
  var label = createElement("label");

  var input = createElement("input");
  input.type = "radio";
  input.name = "filter";
  input.value = param;

  var span = createElement("span");
  span.textContent = charts[param].title;

  label.appendChild(input);
  label.appendChild(span);
  li.appendChild(label);

  stats.dom.params.appendChild(li);

  return input;
}
function barChartPlotter(e) {
  var ctx = e.drawingContext;
  var points = e.points;
  var y_bottom = e.dygraph.toDomYCoord(0);

  ctx.fillStyle = e.color;

  // Find the minimum separation between x-values.
  // This determines the bar width.
  var min_sep = Infinity;
  for (var i = 1; i < points.length; i++) {
    var sep = points[i].canvasx - points[i - 1].canvasx;
    if (sep < min_sep) min_sep = sep;
  }
  var bar_width = Math.floor(2.0 / 3 * min_sep);

  // Do the actual plotting.
  for (var i = 0; i < points.length; i++) {
    var p = points[i];
    var center_x = p.canvasx;

    ctx.fillRect(center_x - bar_width / 2, p.canvasy, bar_width, y_bottom - p.canvasy);

    ctx.strokeRect(center_x - bar_width / 2, p.canvasy, bar_width, y_bottom - p.canvasy);
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
function createUnavailableContainer(chart) {
  var container, title;

  container = createElement('div', 'chart-container');

  title = createElement('div', 'chart-title');
  title.innerHTML = chart.title + '<br><p class="chart-label">недоступно</p>';

  chart.dom.innerHTML = '';
  chart.dom.className = 'chart chart-unavailable cf';
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
  chart.container.val.textContent = chart.opts.formatFunction(chart.val[0].toFixed(2));
  chart.container.axis.style[axis === 'x' ? 'width' : 'height'] = chart.opts.width(chart.val) + '%';

  chart.container.axis.style.backgroundColor = (function() {
      var val = chart.val[0];
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
    columns: [['data', chart.val[0].toFixed(2)]]
  });
}
function loadLabelChart(chart) {
  chart.container.textContent = chart.opts.formatFunction(chart.val[0].toFixed(2));
}

function loadCurrentChartState(name, data) {
  var chart, i, l, n, mills;

  chart = charts[name];

  if (!chart || chart.state.curIndex <= 0) {
    return;
  }

  if (pages.active === 'mainPage') {    
    updates[name] = chart;
  }

  if (stats.mode === 'realtime') {
    for (i = 0, l = data.length; i < l; i++) {
      mills = data[i][0];
      if (typeof mills === 'undefined') {
        continue;
      }

      stats.data[name].push([new Date(mills)].concat(data[i][1]));
    }
    if (pages.active === 'statsPage') {
      if (chart.name === stats.currentChart) {
        if (null === stats.chart) {
          createDygraph();
        } else {
          var opts = {
            'file': stats.data[name],
            'dateWindow': getStatsChartDateWindow(chart)
          };

          stats.chart.updateOptions(opts);
        }
      }
    }
  }
}

function loadExperiment(id, data, annotations) {
  var stat, i, l, ann, name;

  stats.mode = 'experiment';
  stats.experiment = id;
  stats.annotations = {};
  showStatsPage();

  for (stat in data) {
    if (data.hasOwnProperty(stat)) {
      stats.data[stat] = data[stat].map(function(a) {
        return [new Date(a.date)].concat(JSON.parse(a.vals));
      });
    }
  }

  for (i = 0, l = annotations.length; i < l; i++) {
    ann = annotations[i];
    name = charts_ids[ann.param];
    setAnnotation(name, ann.time, ann.text, stats.opts[name].labels[1]);
  }

  stats.chart.updateOptions({
    'file': stats.data[stats.currentChart]
  });
} 

function createNextChartState(chart) {
  if (chart.state.curIndex === 0) {
    return;
  }

  chart.state.curIndex++;

  if (chart.state.curIndex === chart.state.states.length) {
    chart.state.curIndex = 1;    
  }

  createCurrentChartState(chart);

  if (os === 'android') {
    Android.updateState(chart.name, chart.state.states[chart.state.curIndex]);
  }
}
function createCurrentChartState(chart) {
  switch (chart.state.states[chart.state.curIndex]) {
    case -1:
      chart.container = createUnavailableContainer(chart);
      break;
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

function update(data) {
  var i, l, d, c, name, grouped;

  grouped = {};
  for (i = 0, l = data.length; i < l; i++) {
    d = data[i];
    c = charts[charts_ids[d[0]]];
    c.val = d[2];

    if (!grouped.hasOwnProperty(c.name)) {
      grouped[c.name] = [[d[1], c.val]];
    } else {
      grouped[c.name].push([d[1], c.val]);
    }    
  }

  for (name in grouped) {
    if (grouped.hasOwnProperty(name)) {
      loadCurrentChartState(name, grouped[name]);
    }
  }
}

function stringStartsWith(string, prefix) {
  return string.slice(0, prefix.length) == prefix;
}


var dataCleanerInterval, updateMainPage1, updateMainPage2;
function init(config) {
  var chartOnClick, paramOnClick, chartTypesRadioOnClick, chart, idx, i, l;

  os = config.os;
  stats.currentChart = stats.currentChart ? stats.currentChart : config.currentStatsChart;  
  if (os === "android") {
    Android.selectStatsChart(stats.currentChart);
  }

  chartOnClick = function(e) {
    var node, id;

    node = e.target;
    while (node && node.parentNode && node.className && !stringStartsWith(node.className.toString(), 'chart ')) {
      node = node.parentNode;
    }

    id = node.id;

    createNextChartState(charts[id]);
  };

  paramOnClick = function(e) {
    var param = e.target.value;
    stats.currentChart = param;

    if (os === 'android') {
      Android.selectStatsChart(param);
    }

    createDygraph();
  };

  createParamsState();
  for (i = 0, l = chartsOrder.length; i < l; i++) {
    chart = chartsOrder[i];
    if (config.charts.hasOwnProperty(chart)) {
      idx = charts[chart].state.states.indexOf(config.charts[chart]);
      charts[chart].state.curIndex = idx === -1 ? 0 : idx;
      createCurrentChartState(charts[chart]);

      charts[chart].dom.onclick = chartOnClick;

      var param = addParamToParamsState(chart);
      param.onclick = paramOnClick;
      if (config.charts[chart] < 0) {
        param.disabled = true;
      }
      if (chart === stats.currentChart) {
        param.checked = true;
      }
    }
  }

  chartTypesRadioOnClick = function(e) {
    var type = e.target.value;
    stats.plotter = type === 'line' ? null : barChartPlotter;

    stats.windowIdx  = type === 'line'? 0 : 1;

    createDygraph();
  };
  var chartTypesRadio = document.getElementsByName("chart-stats-type");
  for (i = 0, l = chartTypesRadio.length; i < l; i++) {
    chartTypesRadio[i].onclick = chartTypesRadioOnClick;
  }

  clearInterval(dataCleanerInterval);
  dataCleanerInterval = setInterval(function() {
    var i, l, stat, time;
    time = new Date().getTime();
    if (stats.mode === 'realtime') {
      for (stat in stats.data) {
        if (stats.data.hasOwnProperty(stat)) {          
          l = stats.data[stat].length;
          while (l-- > 0 && time - stats.data[stat][0][0].getTime() > stats.window[0]) {
            stats.data[stat].shift();
          }
        }
      }
    }    
  }, 5000);

  clearInterval(updateMainPage1);
  updateMainPage1 = setInterval(function() {
    if (pages.active !== 'mainPage') { 
      return;
    }

    var chartName, chart;

    for (chartName in updates) {
      if (updates.hasOwnProperty(chartName)) {
        chart = updates[chartName];

        switch (chart.state.states[chart.state.curIndex]) {
          case 1:
            loadGaugeChart(chart);
            break;
          case 2:
            loadLabelChart(chart);
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
    }
  }, 200);

  clearInterval(updateMainPage2);
  updateMainPage2 = setInterval(function() {
    if (pages.active !== 'mainPage') { 
      return;
    }

    var chartName, chart;

    for (chartName in updates) {
      if (updates.hasOwnProperty(chartName)) {
        chart = updates[chartName];

        switch (chart.state.states[chart.state.curIndex]) {
          case 3:
            loadAxisesChart(chart);
            break;
          default:
            break;
        }
      }
    }
  }, 100);
}

//init({"os":"browser","charts":{"microphone":1,"accel":-1,"gyro":3,"airTemperature":4,"humidity":1,"atmoPressure":0,"light":2,"soluteTemperature":1,"voltage":5,"amperage":1,"ph":1}});

if (os === 'browser') {
  // showStatsPage();
}
