var chart = c3.generate({
  bindto: '#temperature',
  data: {
    type: 'gauge',
    columns: [['data', 91.4]],   
  },
  gauge: {
    label: {
      format: function(value, ratio) {
        return value + ' °C';
      },
    },
    min: 0,
    max: 3000
  },
  color: {
    pattern: ['#FF0000', '#F97600', '#F6C600', '#60B044'],
    threshold: {
      values: [30, 60, 90, 100]
    }
  },
  interaction: {
    enabled: false
  },
  transition: {
    duration: 0
  }
});

function setTemperature(v) {
  chart.load({
    columns: [['data', parseFloat(v)]]
  });
}