app.service("paxModel", [function() {
  var defaultSort = [
      {column: 'onRuleHitList', dir: 'desc'},
      {column: 'onWatchList', dir: 'desc'},
      {column: 'eta', dir: 'desc'}
    ],
    startDate = new Date(),
    endDate = new Date();
    endDate.setDate(endDate.getDate() + 3);

  this.reset = function(params) {
    this.pageNumber = 1;
    this.pageSize = 10;
    this.lastName = '';
    this.flightNumber = params && params.flightNumber ? params.flightNumber : '';
    this.origin = params && params.origin ? params.origin : '';
    this.dest = params && params.destination ? params.destination : '';
    this.direction = params && params.direction ? params.direction : 'I';
    this.etaStart = params && params.etaStart ? new Date(params.etaStart) : startDate;
    this.etaEnd = params && params.etaEnd ? new Date(params.etaEnd) : endDate;
    this.sort = defaultSort
  }
  
  this.reset();
}]);