'use strict';

var express = require('express');
var path = require('path');
var fs = require('fs');

var app = express();

var assets = path.join(__dirname, '/../common/web');

app.use('/', express.static(assets));

app.get('/', function(req, res) {
  fs.readFile(path.join(assets, '/index.html'), 'utf8', function(err, text) {
    res.send(text);
  });
});

var server = app.listen(8000, function() {
  console.log('Test web server started on port %s', server.address().port);
});
