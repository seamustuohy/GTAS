
app.controller('FlightsController', function($scope, $filter, $q, ngTableParams, flightService, paxService) {
	var paxData = [];
	var data = [];
	
	 $scope.loading = true;
	
    $scope.tableParams = new ngTableParams(
    {
    	noPager: true,
    	page: 1,            // show first page
        count: 10,          // count per page
        filter: {},
        sorting: {
            hits: 'desc'
            //,
            //destinationDateTimeSort: 'asc' //, 'number': 'asc'     // initial sorting
        }
    }, {
      //  total: data.length, // length of data
        getData: function($defer, params) {
            flightService.getFlights().then(function (myData) {
            	data = myData;
			    //vm.tableParams.total(result.total);
                // use build-in angular filter
                var filteredData = params.filter() ?
                        $filter('filter')(data, params.filter()) :
                        data;
                var orderedData = params.sorting() ?
                        $filter('orderBy')(filteredData, params.orderBy()) :
                        data;

                params.total(orderedData.length); // set total for recalc pagination
                $defer.resolve(orderedData.slice((params.page() - 1) * params.count(), params.page() * params.count()));
                
                
                $scope.loading = false;
                
		    });            
        }
	});
    
  $scope.updatePax = function(flightId) {  
  	paxService.getPax(flightId);			
  //paxService.broadcast(flightId);
  };    
    
  $scope.$on('paxDataResponse', function (evnt, data) {
	    $scope.paxData = data;
	  });
  
});
