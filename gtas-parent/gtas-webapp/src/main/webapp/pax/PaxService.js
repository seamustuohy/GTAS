(function () {
    'use strict';
    app
        .service('paxDetailService', function ($http, $q) {
            function getPaxDetail(paxId, flightId) {
                var dfd = $q.defer();
                dfd.resolve($http.get("/gtas/passengers/passenger/" + paxId + "/details?flightId=" + flightId));
                return dfd.promise;
            }

            return ({getPaxDetail: getPaxDetail});
        })
        .service("paxService", function ($rootScope, $http, $q) {
            function getPax(flightId, pageRequest) {
                var dfd = $q.defer();
                dfd.resolve($http({
                    method: 'post',
                    url: '/gtas/flights/flight/' + flightId + '/passengers',
                    data: pageRequest
                }));
                return dfd.promise;
            }

            function getAllPax(pageRequest) {
                var dfd = $q.defer();
                dfd.resolve($http({
                    method: 'post',
                    url: '/gtas/passengers/',
                    data: pageRequest
                }));
                return dfd.promise;
            }

            function getPaxDetail(passengerId, flightId) {
                var url = "/gtas/passengers/passenger/" + passengerId + "/details?flightId=" + flightId;
                return $http.get(url).then(function (res) {
                    return res.data;
                });
            }

            function handleSuccess(response) {
                return (response.data);
            }

            function handleError(response) {
                if (!angular.isObject(response.data) || !response.data.message) {
                    return ($q.reject("An unknown error occurred."));
                }
                return ($q.reject(response.data.message));
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

            // Return public API.
            return ({
                getPax: getPax,
                getAllPax: getAllPax,
                broadcast: broadcast,
                getRuleHits: getRuleHits,
                getPaxDetail: getPaxDetail,
                broadcastRuleID: broadcastRuleID
            });
        });
}());
