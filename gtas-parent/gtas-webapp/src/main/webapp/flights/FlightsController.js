
app.controller('FlightsController', function($scope, $filter, $q, ngTableParams, flightService) {
	var data = [];
    $scope.tableParams = new ngTableParams(
    {
        page: 1,            // show first page
        count: 10,          // count per page
        filter: {},
        sorting: {
            hits: 'desc',
            destinationDateTimeSort: 'asc' //, 'number': 'asc'     // initial sorting
        }
    }, {
        total: data.length, // length of data
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
		    });            
        }
	});
});
