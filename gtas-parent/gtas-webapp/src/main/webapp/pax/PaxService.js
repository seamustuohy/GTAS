app.service("paxService", function ($rootScope, $http, $q) {
    'use strict';
    function handleSuccess(response) {
        return (response.data);
    }

    function handleError(response) {
        if (!angular.isObject(response.data) || !response.data.message) {
            return ($q.reject("An unknown error occurred."));
        }
        return ($q.reject(response.data.message) );
    }

    function getPax(flightId) {
        console.log('paxService->getPax');
        console.log(flightId);
        var request = $http({
            method: "get",
            url: "/gtas/flights/flight/" + flightId + "/passengers",
            params: {
                action: "get"
            }
        });
        return (request.then(handleSuccess, handleError));
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

// temporary hack
    function getAllPax(pax) {
        var request = $http({
            method: "get",
            url: "/gtas/passengers?pageNumber=1&pageSize=10",
            params: pax
        });
        return (request.then(handleSuccess, handleError));
    }

    // Return public API.
    return ({
        getPax: getPax,
        broadcast: broadcast,
        getRuleHits: getRuleHits,
        broadcastRuleID: broadcastRuleID,
        getAllPax: getAllPax,
    });
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
