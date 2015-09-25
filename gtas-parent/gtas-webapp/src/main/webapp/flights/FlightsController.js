app.controller('FlightsController', function ($scope, $http, flightService,$state,$interval,$stateParams) {

  $scope.selectedFlight=$stateParams.flight;

  var paginationOptions = {
    pageNumber: 1,
    pageSize: 10,
    sort: null
  };
  
  function rowTemplate() {
    return '<div ng-dblclick="grid.appScope.rowDblClick(row)" >' +
           '  <div ng-repeat="(colRenderIndex, col) in colContainer.renderedColumns track by col.colDef.name" class="ui-grid-cell" ng-class="{ \'ui-grid-row-header-cell\': col.isRowHeader }"  ui-grid-cell></div>' +
           '</div>';
  }

  $scope.rowDblClick = function( row) {
    alert(JSON.stringify(row.entity)); 
  };
  
  $scope.gridOptions = { 
    showFooter: true,
    enableSorting: true,
    multiSelect: false,
    enableFiltering: false,     
    enableRowSelection: true, 
    enableSelectAll: false,
    enableRowHeaderSelection: false,
    selectionRowHeaderWidth: 35,  
    noUnselect: true,
    enableGridMenu: false,  	
    paginationPageSizes: [10, 25, 50],
    paginationPageSize: 10,
    useExternalPagination: true,
    useExternalSorting: true,
    useExternalFiltering: true,
    //rowTemplate: rowTemplate(),
    
    onRegisterApi: function(gridApi) {
      $scope.gridApi = gridApi;
      
      gridApi.core.on.sortChanged($scope, function(grid, sortColumns) {
        if (sortColumns.length == 0) {
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

      gridApi.selection.on.rowSelectionChanged($scope,function(row){
        $scope.selectedFlight=row.entity;
        console.log($scope.selectedFlight);
      })
    }
  };

  $scope.gridOptions.columnDefs = [
    { name: 'P', field: 'passengerCount', width: 50, enableFiltering: false,
        cellTemplate: '<button id="editBtn" type="button" class="btn-small" ng-click="grid.appScope.passengerNav()">{{COL_FIELD}}</button> ' ,
    },
    { name: 'H', field: 'ruleHitCount', width: 50, enableFiltering: false },
    { name: 'L', field: 'listHitCount', width: 50, enableFiltering: false },
    { name: 'Carrier', field: 'carrier', width: 75 },
    { name: 'Flight', field: 'flightNumber', width: 75 },
    { name: 'Dir', field: 'direction', width: 50 },    
    { name: 'ETA', displayName: 'ETA', field: 'eta' },
    { name: 'ETD', displayName: 'ETD', field: 'etd' },    
    { name: 'Origin', field: 'origin' },
    { name: 'OriginCountry', displayName: "Country", field: 'originCountry' },
    { name: 'Dest', field: 'destination' },
    { name: 'DestCountry', displayName: "Country", field: 'destinationCountry' }
  ];

    $scope.passengerNav = function(){
        $state.go('flights.passengers',{ parent: 'flights', flight: $scope.selectedFlight });
    };

    var getPage = function() {
    console.log('requesting page #' + paginationOptions.pageNumber);
    flightService.getFlights(paginationOptions).then(function (page) {
      console.log(page);
      $scope.gridOptions.totalItems = page.totalFlights;
      $scope.gridOptions.data = page.flights;

      $interval( function() {
        if ($scope.gridApi) {
          $scope.gridApi.selection.selectRow($scope.gridOptions.data[0]);
        }
      }, 0, 1);
    });
  };
  
  getPage();

    $state.go('flights.all');
});
