var charts = {};

charts.airTemperature = c3.generate({
  bindto: '.chart-air-temperature',
  data: {
    type: 'gauge',
    columns: [['data', 30]],   
  },
  gauge: {
    label: {
      format: function(value, ratio) {
        return (value > 0 ? '+' : '-') + value + ' °C';
      },
    },
    min: -70,
    max: 70,
    width: 25
  },
  color: {
    pattern: ['#00FF00', '#FFFF00', '#FFA500', '#FF0000'],
    threshold: {
      values: [35, 42, 49, 56]
    }
  },
  interaction: {
    enabled: false
  },
  transition: {
    duration: null
  }
});

charts.humidity = c3.generate({
  bindto: '.chart-humidity',
  data: {
    type: 'gauge',
    columns: [['data', 91.4]],   
  },
  gauge: {
    label: {
      format: function(value, ratio) {
        return value + ' %';
      },
    },
    min: 0,
    max: 100,
    width: 25
  },
  color: {
    pattern: ['#0000FF']
  },
  interaction: {
    enabled: false
  },
  transition: {
    duration: null
  }
});

charts.atmoPressure = c3.generate({
  bindto: '.chart-atmo-pressure',
  data: {
    type: 'gauge',
    columns: [['data', 91.4]],   
  },
  gauge: {
    label: {
      format: function(value, ratio) {
        return value + ' кПа';
      },
    },
    min: 0,
    max: 300,
    width: 25
  },
  color: {
    pattern: ['#0000FF']
  },
  interaction: {
    enabled: false
  },
  transition: {
    duration: null
  }
});

charts.light = c3.generate({
  bindto: '.chart-light',
  data: {
    type: 'gauge',
    columns: [['data', 91.4]],   
  },
  gauge: {
    label: {
      format: function(value, ratio) {
        return value + ' лк';
      },
    },
    min: 0,
    max: 10000,
    width: 25
  },
  color: {
    pattern: ['#FFC107']
  },
  interaction: {
    enabled: false
  },
  transition: {
    duration: null
  }
});

charts.soluteTemperature = c3.generate({
  bindto: '.chart-solute-temperature',
  data: {
    type: 'gauge',
    columns: [['data', 91.4]],   
  },
  gauge: {
    label: {
      format: function(value, ratio) {
        return (value > 0 ? '+' : '-') + value + ' °C';
      },
    },
    min: -50,
    max: 1500,
    width: 25
  },
  color: {
    pattern: ['#0000FF']
  },
  interaction: {
    enabled: false
  },
  transition: {
    duration: null
  }
});

charts.voltage = c3.generate({
  bindto: '.chart-voltage',
  data: {
    type: 'gauge',
    columns: [['data', 91.4]],   
  },
  gauge: {
    label: {
      format: function(value, ratio) {
        return value + '  В';
      },
    },
    min: -30,
    max: 30,
    width: 25
  },
  color: {
    pattern: ['#0000FF']
  },
  interaction: {
    enabled: false
  },
  transition: {
    duration: null
  }
});

charts.amperage = c3.generate({
  bindto: '.chart-amperage',
  data: {
    type: 'gauge',
    columns: [['data', 91.4]],   
  },
  gauge: {
    label: {
      format: function(value, ratio) {
        return value + ' А';
      },
    },
    min: -1,
    max: 1,
    width: 25
  },
  color: {
    pattern: ['#0000FF']
  },
  interaction: {
    enabled: false
  },
  transition: {
    duration: null
  }
});

charts.ph = c3.generate({
  bindto: '.chart-ph',
  data: {
    type: 'gauge',
    columns: [['data', 91.4]],   
  },
  gauge: {
    label: {
      format: function(value, ratio) {
        return value + ' pH';
      },
    },
    min: 0,
    max: 14,
    width: 25
  },
  color: {
    pattern: ['#0000FF']
  },
  interaction: {
    enabled: false
  },
  transition: {
    duration: null
  }
});

charts.gyro = {
  x : {
    positive : {
      bar : document.querySelector('.chart-gyro .chart-axises-positive .chart-x-axis'),
      val : document.querySelector('.chart-gyro .chart-axises-positive .chart-x-axis .chart-axis-val')  
    },
    negative : {
      bar : document.querySelector('.chart-gyro .chart-axises-negative .chart-x-axis'),
      val : document.querySelector('.chart-gyro .chart-axises-negative .chart-x-axis .chart-axis-val')  
    }
  },
  y : {
    positive : {
      bar : document.querySelector('.chart-gyro .chart-axises-positive .chart-y-axis'),
      val : document.querySelector('.chart-gyro .chart-axises-positive .chart-y-axis .chart-axis-val')  
    },
    negative : {
      bar : document.querySelector('.chart-gyro .chart-axises-negative .chart-y-axis'),
      val : document.querySelector('.chart-gyro .chart-axises-negative .chart-y-axis .chart-axis-val')  
    }
  },
  z : {
    positive : {
      bar : document.querySelector('.chart-gyro .chart-axises-positive .chart-z-axis'),
      val : document.querySelector('.chart-gyro .chart-axises-positive .chart-z-axis .chart-axis-val')  
    },
    negative : {
      bar : document.querySelector('.chart-gyro .chart-axises-negative .chart-z-axis'),
      val : document.querySelector('.chart-gyro .chart-axises-negative .chart-z-axis .chart-axis-val')  
    }
  }
};

charts.accel = {
  x : {
    positive : {
      bar : document.querySelector('.chart-accel .chart-axises-positive .chart-x-axis'),
      val : document.querySelector('.chart-accel .chart-axises-positive .chart-x-axis .chart-axis-val')  
    },
    negative : {
      bar : document.querySelector('.chart-accel .chart-axises-negative .chart-x-axis'),
      val : document.querySelector('.chart-accel .chart-axises-negative .chart-x-axis .chart-axis-val')  
    }
  },
  y : {
    positive : {
      bar : document.querySelector('.chart-accel .chart-axises-positive .chart-y-axis'),
      val : document.querySelector('.chart-accel .chart-axises-positive .chart-y-axis .chart-axis-val')  
    },
    negative : {
      bar : document.querySelector('.chart-accel .chart-axises-negative .chart-y-axis'),
      val : document.querySelector('.chart-accel .chart-axises-negative .chart-y-axis .chart-axis-val')  
    }
  },
  z : {
    positive : {
      bar : document.querySelector('.chart-accel .chart-axises-positive .chart-z-axis'),
      val : document.querySelector('.chart-accel .chart-axises-positive .chart-z-axis .chart-axis-val')  
    },
    negative : {
      bar : document.querySelector('.chart-accel .chart-axises-negative .chart-z-axis'),
      val : document.querySelector('.chart-accel .chart-axises-negative .chart-z-axis .chart-axis-val')  
    }
  }
};


var labels = {
  gyro : document.querySelector('.label-gyro'),
  accel : document.querySelector('.label-accel'),
  microphone : document.querySelector('.label-microphone')
}

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
  labels.microphone.textContent = v[0] + ' db';
}

function gyro(vals) {
  axisesChart('gyro', ['x', 'y', 'z'], vals, function(val) {
    return Math.min(Math.abs(val * 100.0), 100.0);
  });
}

function accel(vals) {
  vals = vals.map(function(val) {
    return val / 9.8;
  });

  axisesChart('accel', ['x', 'y', 'z'], vals, function(val) {
    return Math.min(Math.abs(val * 100.0 / 2.0), 100.0);
  });
}

function axisesChart(name, axises, vals, widthFunction) {
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

    charts[name][axis][sign].val.style.display = 'inline';
    charts[name][axis][sign].val.textContent = val.toFixed(2);  
    charts[name][axis][oppositeSign].val.style.display = 'none';

    charts[name][axis][sign].bar.style.width = widthFunction(val) + '%';
    charts[name][axis][oppositeSign].bar.style.width = 0;
  }
}





