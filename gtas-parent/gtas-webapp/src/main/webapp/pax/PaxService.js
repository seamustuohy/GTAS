app.service("paxService", function( $rootScope, $http, $q ) {	
	// Return public API.
	return({
	    getPax: getPax,
        broadcast: broadcast,
        getRuleHits: getRuleHits
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
    
    
    function getRuleHits(travelerId) {
	    var request = $http({
	        method: "get",
	        url: "/gtas/hit/traveler" + (travelerId ? "?travelerId=" + travelerId : ""),
	      //  url: "/hit/traveler/"+(),
	        params: {
	            action: "get"
	        }
	    });
	    return( request.then( handleSuccess, handleError ) );
    	
    }
    
    
});


