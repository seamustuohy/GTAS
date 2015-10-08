app.service("executeQueryService", function ($rootScope, $http, $q) {
    'use strict';
    var serviceURLs = {
            flights: '/gtas/query/queryFlights/',
            passengers: '/gtas/query/queryPassengers/'
        },
        handleError = function (response) {
            if (!angular.isObject(response.data) || !response.data.message) {
                return ($q.reject("An unknown error occurred."));
            }
            return ($q.reject(response.data.message));
        },
        handleSuccess = function (response) {
            $rootScope.$broadcast('aFactory:keyChanged', response.data);
            return (response.data);
        },
        executeQuery = function (baseUrl, qbData) {
            var request = $http({
                method: 'post',
                url: baseUrl,
                data: qbData
            });
            return (request.then(handleSuccess, handleError));
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
