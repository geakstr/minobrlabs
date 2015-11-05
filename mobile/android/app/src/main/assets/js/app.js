var charts = {};

charts.temperature = c3.generate({
  bindto: '.temperature',
  data: {
    type: 'gauge',
    columns: [['data', 30]],   
  },
  gauge: {
    label: {
      format: function(value, ratio) {
        return value + ' °C';
      },
    },
    min: -50,
    max: 60
  },
  color: {
    pattern: ['#0000FF', '#00FF00', '#FFFF00', '#FFA500', '#FF0000'],
    threshold: {
      values: [1, 19, 30, 40]
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
  bindto: '.humidity',
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
    max: 100
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
  charts.temperature.load({
    columns: [['data', parseFloat(v)]]
  });
}

function setHumidity(v) {
  charts.humidity.load({
    columns: [['data', parseFloat(v)]]
  });
}