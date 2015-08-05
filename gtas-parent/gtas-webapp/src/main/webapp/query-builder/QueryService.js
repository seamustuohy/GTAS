app.service("queryService", function ($rootScope, $http, $q) {
    'use strict';
    var handleError = function (response) {
            if (!angular.isObject(response.data) || !response.data.message) {
                return ($q.reject("An unknown error occurred."));
            }
            return ($q.reject(response.data.message));
        },
        handleSuccess = function (response) {
            return (response.data);
        },
        executeQuery = function (baseUrl, data) {
            var request = $http({
                method: 'post',
                url: baseUrl,
                data: data
            });
            return (request.then(handleSuccess, handleError));
        };
    // Return public API.
    return ({
        executeQuery: executeQuery
    });
});
