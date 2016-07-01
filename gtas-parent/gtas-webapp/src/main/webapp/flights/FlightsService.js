/*
 * All GTAS code is Copyright 2016, Unisys Corporation.
 * 
 * Please see LICENSE.txt for details.
 */
app.service("flightService", function ($http, $q) {
    'use strict';

    function getFlights(pageRequest) {
        var dfd = $q.defer();
        dfd.resolve($http({
            method: 'post',
            url: "/gtas/flights/",
            data: pageRequest
        }));
        return dfd.promise;
    }

    // I transform the error response, unwrapping the application dta from
    // the API response payload.
    function handleError (response) {
        // The API response from the server should be returned in a
        // nomralized format. However, if the request was not handled by the
        // server (or what not handles properly - ex. server error), then we
        // may have to normalize it on our end, as best we can.
        if (!angular.isObject(response.data) || !response.data.message) {
            return ( $q.reject("An unknown error occurred.") );
        }
        // Otherwise, use expected error message.
        return ( $q.reject(response.data.message) );
    }

    // I transform the successful response, unwrapping the application data
    // from the API response payload.
    function handleSuccess(response) {
        return ( response.data );
    }

    // Return public API.
    return ({
        getFlights: getFlights
    });
});
