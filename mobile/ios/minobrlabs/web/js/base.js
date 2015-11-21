(function() {
'use strict';

var dom = {
  mainPage: document.getElementById('main-page'),
  statsPage: document.getElementById('stats-page')
}

function showMainPage() {
  dom.mainPage.style.display = 'block';
  dom.statsPage.style.display = 'none';
}

function showStatsPage() {
  dom.mainPage.style.display = 'none';
  dom.statsPage.style.display = 'block';
}

})();