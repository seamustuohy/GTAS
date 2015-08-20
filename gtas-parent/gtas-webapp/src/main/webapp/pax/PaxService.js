app.service("paxService", function( $rootScope, $http, $q ) {	
	// Return public API.
	return({
	    getPax: getPax,
        broadcast: broadcast,
        getRuleHits: getRuleHits,
        broadcastRuleID: broadcastRuleID,
        getAllPax: getAllPax
	});
	
	function getPax(flightId) {
	    var request = $http({
	        method: "get",
	        url: "/gtas/passengers" + (flightId ? "?flightId=" + flightId : ""),
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
    
    
    function getRuleHits(passengerId) {
	    var request = $http({
	        method: "get",
	        url: "/gtas/hit/passenger" + (passengerId ? "?passengerId=" + passengerId : ""),
	        params: {
	            action: "get"
	        }
	    });
	    return( request.then( handleSuccess, handleError ) );
    	
    }
    
    function broadcastRuleID(ruleID) {
        $rootScope.$broadcast('ruleIDBroadcast', ruleID); 
    }
    
    
	function getAllPax() {
	    var request = $http({
	        method: "get",
	        url: "/gtas/passengers/all",
	        params: {
	            action: "get"
	        }
	    });
	    return( request.then( handleSuccess, handleError ) );
	}
    
});


