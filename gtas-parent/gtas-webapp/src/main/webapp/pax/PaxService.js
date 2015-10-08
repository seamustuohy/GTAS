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
            var defaultSort = [
                {column: 'ruleHitCount', dir: 'desc'},
                {column: 'listHitCount', dir: 'desc'},
                {column: 'eta', dir: 'desc'}
            ];

            var startDate = new Date();
            var endDate = new Date();
            endDate.setDate(endDate.getDate() + 3);

            function initialModel() {
                return {
                    pageNumber: 1,
                    pageSize: 10,
                    lastName: '',
                    flightNumber: '',
                    origin: '',
                    dest: '',
                    direction: 'I',
                    etaStart: startDate,
                    etaEnd: endDate,
                    sort: defaultSort
                };
            }

            var model = initialModel();

            function getPax(flightId, pageRequest) {
                var request = $http({
                    method: 'post',
                    url: '/gtas/flights/flight/' + flightId + '/passengers',
                    data: pageRequest
                });
                return (request.then(handleSuccess, handleError));
            }

            function getAllPax(pageRequest) {
                var request = $http({
                    method: 'post',
                    url: '/gtas/passengers/',
                    data: pageRequest
                });
                return (request.then(handleSuccess, handleError));
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

            // Return public API.
            return ({
                model: model,
                initialModel: initialModel,
                getPax: getPax,
                getAllPax: getAllPax,
                broadcast: broadcast,
                getRuleHits: getRuleHits,
                getPaxDetail: getPaxDetail,
                broadcastRuleID: broadcastRuleID
            });
        });
}())

