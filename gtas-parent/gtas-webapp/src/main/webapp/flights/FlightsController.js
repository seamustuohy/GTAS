app.controller('FlightsController', function ($scope, $state, $interval, $stateParams, flightService, gridService, uiGridConstants) {
  $scope.flight = flightService;
  
  $scope.selectedFlight = $stateParams.flight;

  $scope.flightsGrid = {
    enableSorting: true,
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
        console.log('sort changed');
        if (sortColumns.length === 0) {
          $scope.flight.model.sort = null; 
        } else {
          $scope.flight.model.sort = [];
          for (i = 0; i<sortColumns.length; i++) {
            $scope.flight.model.sort.push({ column: sortColumns[i].name, dir: sortColumns[i].sort.direction });
          }
        }
        getPage();
      });

      gridApi.core.on.filterChanged( $scope, function() {
        var grid = this.grid;
      });

      gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
        console.log('page changed');
        $scope.flight.model.pageNumber = newPage;
        $scope.flight.model.pageSize = pageSize;
        getPage();
      });
    }
  };

  $scope.flightsGrid.columnDefs = [
    { name: 'passengerCount', displayName: 'P', width: 50, enableFiltering: false,
        cellTemplate: '<button id="editBtn" type="button" class="btn-small" ng-click="grid.appScope.passengerNav(row)">{{COL_FIELD}}</button> ' ,
    },
    { name: 'ruleHitCount', displayName: 'H', width: 50, enableFiltering: false, cellClass: gridService.colorHits,
      sort: {
        direction: uiGridConstants.DESC,
        priority: 0
      }
    },
    { name: 'listHitCount', displayName: 'L', width: 50, enableFiltering: false, cellClass: gridService.colorHits,
      sort: {
        direction: uiGridConstants.DESC,
        priority: 1
      }    
    },
    { name: 'carrier', width: 75 },
    { name: 'flightNumber', displayName: 'Flight', width: 75 },
    { name: 'eta', displayName: 'ETA',
      sort: {
        direction: uiGridConstants.DESC,
        priority: 2
      }    
    },
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
    console.log(JSON.stringify($scope.flight.model));
    flightService.getFlights($scope.flight.model).then(function (page) {
      $scope.flightsGrid.totalItems = page.totalFlights;
      $scope.flightsGrid.data = page.flights;
    });
  };
  
  $scope.refresh = function() {
    getPage();
  }
  
  $scope.getTableHeight = function() {
     return gridService.calculateGridHeight($scope.flightsGrid.data.length);
  };  
  
  $state.go('flights.all');
  getPage();
});
