app.service("paxService", function ($rootScope, $http, $q) {
    'use strict';
    
    // Return public API.
    return ({
        getPax: getPax,
        getAllPax: getAllPax,
        broadcast: broadcast,
        getRuleHits: getRuleHits,
        broadcastRuleID: broadcastRuleID
    });
    
    function getPax(flightId, paginationOptions) {
        console.log('paxService->getPax');
        console.log('flight id: ' + flightId);
        var request = $http({
            method: "get",
            url: "/gtas/flights/flight/" + flightId + "/passengers",
            params: { pageNumber: paginationOptions.pageNumber, pageSize: paginationOptions.pageSize }
        });
        return (request.then(handleSuccess, handleError));
    }

    function getAllPax(paginationOptions) {
        var request = $http({
            method: "get",
            url: "/gtas/passengers",
            params: { pageNumber: paginationOptions.pageNumber, pageSize: paginationOptions.pageSize }
        });
        return (request.then(handleSuccess, handleError));
    }

    function handleSuccess(response) {
        return (response.data);
    }

    function handleError(response) {
        if (!angular.isObject(response.data) || !response.data.message) {
            return ($q.reject("An unknown error occurred."));
        }
        return ($q.reject(response.data.message) );
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
        return (request.then(handleSuccess, handleError));

    }

    function broadcastRuleID(ruleID) {
        $rootScope.$broadcast('ruleIDBroadcast', ruleID);
    }
});

app.service("paxDetailService", ['$http', '$stateParams', function ($http, $stateParams) {
    'use strict';
    var getPaxDetail = function ($http, $stateParams) {
        var url = "/gtas/passengers/passenger/" + $stateParams.id + "/details?flightId=" + $stateParams.flightId;
        return $http.get(url);
        //.then(function(res){ return res.data; });
    };
    return ({getPaxDetail: getPaxDetail});
}]);
