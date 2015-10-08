app.service("executeQueryService", function ($rootScope, $http, $q) {
    'use strict';
    var serviceURLs = {
            flights: '/gtas/query/queryFlights/',
            passengers: '/gtas/query/queryPassengers/'
        },
        executeQuery = function (baseUrl, qbData) {
            var dfd = $q.defer(),
                request = $http({
                    method: 'post',
                    url: baseUrl,
                    data: qbData
                });
            dfd.resolve(request);
            return dfd.promise;
        },
        queryFlights = function (qbData) {
            executeQuery(serviceURLs.flights, qbData);
        },
        queryPassengers = function (qbData) {
            executeQuery(serviceURLs.passengers, qbData);
        };
    // Return public API.
    return ({
        queryFlights: queryFlights,
        queryPassengers: queryPassengers
    });
});
