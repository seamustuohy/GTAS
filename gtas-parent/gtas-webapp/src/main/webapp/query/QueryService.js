app.service("paxService", function( $rootScope, $http, $q ) {	
	// Return public API.
	return({
	    getPax: getPax,
        broadcast: broadcast
	});
	
	function getPax(flightId) {
	    var request = $http({
	        method: "get",
	        url: "/gtas/travelers" + (flightId ? "?flightId=" + flightId : ""),
	        params: {
	            action: "get"
	        }
	    });
	    return( request.then( handleSuccess, handleError ) );
	}
	
    function handleError( response ) {
        if (! angular.isObject( response.data ) || ! response.data.message) {
            return( $q.reject( "An unknown error occurred." ) );
        }
        return( $q.reject( response.data.message ) );
    }

    function handleSuccess( response ) {
        return( response.data );
    }
    
    function broadcast(flightId) {
        $rootScope.$broadcast('handleBroadcast', flightId); 
    }    
});
