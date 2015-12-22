(function () {
    'use strict';
    app
        .service("dashboardService", function ($rootScope, $http, $q) {

            function getFlightAndHitsCount(startDate, endDate) {
                var dfd = $q.defer();
                dfd.resolve($http({
                    method: 'get',
                    url: '/gtas/getFlightsAndHitsCount',
                    params: {
                        startDate: startDate,
                        endDate: endDate
                    }
                }));
                return dfd.promise;
            }

            function getMessagesCount(startDate, endDate) {
                var dfd = $q.defer();
                dfd.resolve($http({
                    method: 'get',
                    url: '/gtas/getMessagesCount',
                    params: {
                        startDate: startDate,
                        endDate: endDate
                    }
                }));
                return dfd.promise;
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

            // Return public API.
            return ({
                getFlightAndHitsCount: getFlightAndHitsCount,
                getMessagesCount: getMessagesCount

            });
        });
}());
