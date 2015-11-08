var data = [];

var i = 0;
data.push([++i, 35.0]);
data.push([++i, 25.0]);
data.push([++i, 66.0]);

var g = new Dygraph(
    document.getElementById("chart-linear-stats"),
    data,
    {
      drawPoints: true,
      ylabel: 'Температура воздуха (°C)',
      labels: ['Время', 'Температура']
    }
);

function airTemperature(val) {
  data.push([++i, val]);
  g.updateOptions({
    'file': data
  });
} 