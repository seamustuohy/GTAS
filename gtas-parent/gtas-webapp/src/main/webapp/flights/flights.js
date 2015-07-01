app.service("flightService", function( $http, $q ) {	
	// Return public API.
	return({
	    getFlights: getFlights
	});
	
	function getFlights() {
	    var request = $http({
	        method: "get",
	        url: "/gtas/flights",
	        params: {
	            action: "get"
	        }
	    });
	    return( request.then( handleSuccess, handleError ) );
	}
	
    // I transform the error response, unwrapping the application dta from
    // the API response payload.
    function handleError( response ) {
        // The API response from the server should be returned in a
        // nomralized format. However, if the request was not handled by the
        // server (or what not handles properly - ex. server error), then we
        // may have to normalize it on our end, as best we can.
        if (! angular.isObject( response.data ) || ! response.data.message) {
            return( $q.reject( "An unknown error occurred." ) );
        }

        // Otherwise, use expected error message.
        return( $q.reject( response.data.message ) );
    }

    // I transform the successful response, unwrapping the application data
    // from the API response payload.
    function handleSuccess( response ) {
        return( response.data );
    }
});


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
