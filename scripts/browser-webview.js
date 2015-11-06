'use strict';

var express = require('express');
var path = require('path');
var fs = require('fs');

var app = express();

var assets = path.join(__dirname, '/../common/web');

app.use('/', express.static(assets));

function loadPage(file, res) {
  fs.readFile(path.join(assets, file), 'utf8', function(err, text) {
    res.send(text);
  });
}

app.get('/', function(req, res) {
  loadPage('/index.html', res);
});

app.get('/stats', function(req, res) {
  loadPage('/stats.html', res);
});

var server = app.listen(8000, function() {
  console.log('Test web server started on port %s', server.address().port);
});
