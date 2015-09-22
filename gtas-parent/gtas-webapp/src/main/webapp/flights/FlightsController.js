app.controller('FlightsController', function ($scope, $http, flightService) {

  var paginationOptions = {
    pageNumber: 1,
    pageSize: 10,
    sort: null
  };
  
  $scope.gridOptions = { 
  	enableRowSelection: true, 
  	paginationPageSizes: [10, 25, 50],
    paginationPageSize: 10,
    useExternalPagination: true,
    useExternalSorting: true,
    onRegisterApi: function(gridApi) {
      $scope.gridApi = gridApi;
      gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
        paginationOptions.pageNumber = newPage;
        paginationOptions.pageSize = pageSize;
        getPage();
      });
    }    
  };

  $scope.gridOptions.columnDefs = [
    { name: 'P', field: 'totalPax', width: 50 },
    { name: 'H', field: 'ruleHits', width: 50 },
    { name: 'L', field: 'listHits', width: 50 },
    { name: 'Flight', field: 'flightNumber' },
    { name: 'Dir', field: 'direction' },    
    { name: 'ETA', displayName: 'ETA', field: 'eta' },
    { name: 'ETD', displayName: 'ETD', field: 'etd' },    
    { name: 'Origin', field: 'origin' },
    { name: 'Country', field: 'originCountry' },
    { name: 'Dest', field: 'destination' },
    { name: 'Country', field: 'destinationCountry' }
    
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
