app.controller('FlightsController', function ($scope, $state, $interval, $stateParams, flightService, gridService) {

  $scope.selectedFlight=$stateParams.flight;

  var paginationOptions = {
    pageNumber: 1,
    pageSize: 15,
    sort: null
  };

  $scope.flightsGrid = {
    enableSorting: false,
    multiSelect: false,
    enableFiltering: false,
    enableRowSelection: false,
    enableSelectAll: false,
    enableRowHeaderSelection: false,
    enableGridMenu: false,
    paginationPageSizes: [15, 25, 50],
    paginationPageSize: 15,
    useExternalPagination: true,
    useExternalSorting: true,
    useExternalFiltering: true,
    
    onRegisterApi: function(gridApi) {
      $scope.gridApi = gridApi;
      
      gridApi.core.on.sortChanged($scope, function(grid, sortColumns) {
        if (sortColumns.length === 0) {
          paginationOptions.sort = null;
        } else {
          paginationOptions.sort = sortColumns[0].sort.direction;
        }
        getPage();
      });

      gridApi.core.on.filterChanged( $scope, function() {
        var grid = this.grid;
      });

      gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
        paginationOptions.pageNumber = newPage;
        paginationOptions.pageSize = pageSize;
        getPage();
      });
    }
  };

  $scope.flightsGrid.columnDefs = [
    { name: 'passengerCount', displayName: 'P', width: 50, enableFiltering: false,
        cellTemplate: '<button id="editBtn" type="button" class="btn-small" ng-click="grid.appScope.passengerNav(row)">{{COL_FIELD}}</button> ' ,
    },
    { name: 'ruleHitCount', displayName: 'H', width: 50, enableFiltering: false, cellClass: gridService.colorHits },
    { name: 'listHitCount', displayName: 'L', width: 50, enableFiltering: false, cellClass: gridService.colorHits },
    { name: 'carrier', width: 75 },
    { name: 'flightNumber', displayName: 'Flight', width: 75 },
    { name: 'eta', displayName: 'ETA' },
    { name: 'etd', displayName: 'ETD' },    
    { name: 'origin', displayName: 'Origin' },
    { name: 'originCountry', displayName: 'Country' },
    { name: 'destination', displayName: 'Dest' },
    { name: 'destinationCountry', displayName: 'Country' }
  ];

  $scope.passengerNav = function(row) {
    $scope.selectedFlight=row.entity;
    $state.go('flights.passengers', { parent: 'flights', flight: $scope.selectedFlight });
  };

  var getPage = function() {
    flightService.getFlights(paginationOptions).then(function (page) {
      $scope.flightsGrid.totalItems = page.totalFlights;
      $scope.flightsGrid.data = page.flights;
    });
  };
  
  $scope.getTableHeight = function() {
     return gridService.calculateGridHeight($scope.flightsGrid.data.length);
  };  
  
  getPage();

  $state.go('flights.all');
});
