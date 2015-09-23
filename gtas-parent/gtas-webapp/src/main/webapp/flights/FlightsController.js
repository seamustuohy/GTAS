app.controller('FlightsController', function ($scope, $http, flightService) {

  var paginationOptions = {
    pageNumber: 1,
    pageSize: 10,
    sort: null
  };
  
  $scope.gridOptions = { 
  	enableRowSelection: true, 
  	multiSelect: false,
    enableFiltering: true,  	
  	paginationPageSizes: [10, 25, 50],
    paginationPageSize: 10,
    useExternalPagination: true,
    useExternalSorting: true,
    useExternalFiltering: true,
    
    onRegisterApi: function(gridApi) {
      $scope.gridApi = gridApi;
      
      gridApi.selection.on.rowSelectionChanged($scope,function(row) {
      });
      
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
        //alert(grid.columns[4].filters[0].term);
      });
      
      gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
        paginationOptions.pageNumber = newPage;
        paginationOptions.pageSize = pageSize;
        getPage();
      });
    }    
  };


  $scope.gridOptions.columnDefs = [
    { name: 'P', field: 'passengerCount', width: 50, enableFiltering: false },
    { name: 'H', field: 'ruleHits', width: 50, enableFiltering: false },
    { name: 'L', field: 'listHits', width: 50, enableFiltering: false },
    { name: 'Flight', field: 'flightNumber' },
    { name: 'Dir', field: 'direction', width: 50 },    
    { name: 'ETA', displayName: 'ETA', field: 'eta' },
    { name: 'ETD', displayName: 'ETD', field: 'etd' },    
    { name: 'Origin', field: 'origin' },
    { name: 'OriginCountry', displayName: "Country", field: 'originCountry' },
    { name: 'Dest', field: 'destination' },
    { name: 'DestCountry', displayName: "Country", field: 'destinationCountry' }
  ];
  
  var getPage = function() {
    console.log('requesting page #' + paginationOptions.pageNumber);
    flightService.getFlights(paginationOptions).then(function (page) {
      $scope.gridOptions.totalItems = page.totalFlights;
      $scope.gridOptions.data = page.flights;
    });
  };
  
  getPage();
});
