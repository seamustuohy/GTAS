app.service("paxService", function( $rootScope, $http, $q ) {	
	// Return public API.
	return({
	    getPax: getPax,
        broadcast: broadcast,
        getRuleHits: getRuleHits,
        broadcastRuleID: broadcastRuleID,
        getAllPax: getAllPax,
        getPaxByPage: getPaxByPage
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
    
	function getAllPax(pax) {
	    var request = $http({
	        method: "get",
	        url: "/gtas/passengers/all",
	        params: pax
	    });
	    return( request.then( handleSuccess, handleError ) );
	}
	
	function getPaxByPage(pax) {
	    var request = $http({
	        method: "get",
	        url: "/gtas/passengers/page/"+pax.pageNumber,
	        params: pax
//	        {
//	            action: "get",
//	            pageNumber: pageNumber,
//	            name: ""
//	        }
	    });
	    return( request.then( handleSuccess, handleError ) );
	}
});

app.service("paxDetailService",['$http','$stateParams', function($http, $stateParams){
return({getPaxDetail: getPaxDetail})

	function getPaxDetail($http, $stateParams){
        var url = "/gtas/passengers/" + $stateParams.id+"?flightId=" + $stateParams.flightId;
        return $http.get(url);
            //.then(function(res){ return res.data; });
    }

}]);


