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
    min: 25,
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
    min: 1,
    max: 10000,
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

function setTemperature(v) {
  charts.airTemperature.load({
    columns: [['data', parseFloat(v)]]
  });
}

function setHumidity(v) {
  charts.humidity.load({
    columns: [['data', parseFloat(v)]]
  });
}

function setLight(v) {
  charts.light.load({
    columns: [['data', parseFloat(v)]]
  });
}