app.service("queryBuilderService", function ($http, $q) {
    'use strict';
    var baseUrl = '/gtas/query/',
        handleError = function (response) {
            if (!angular.isObject(response.data) || !response.data.message) {
                return ($q.reject("An unknown error occurred."));
            }
            return ($q.reject(response.data.message));
        },
        handleSuccess = function (response) {
            return (response.data);
        },
        deleteQuery = function (ruleId, userId) {
            var request;

            if (userId === undefined || userId === null || ruleId === undefined || ruleId === null) {
                return;
            }

            // uniform would be: url: '/gtas/query/deleteQuery/'+ userId + '/' + ruleId
            request = $http({
                method: 'delete',
                url: baseUrl + 'deleteQuery',
                params: {
                    userId: userId,
                    id: ruleId
                }
            });

            return (request.then(handleSuccess, handleError));
        },
        saveQuery = function (data) {
            var request;

            if (data.id === null) {
                request = $http({
                    method: 'post',
                    url: baseUrl + 'saveQuery',
                    data: data
                });
            } else {
                request = $http({
                    method: 'put',
                    url: baseUrl + 'editQuery',
                    data: data
                });
            }

            return (request.then(handleSuccess, handleError));
        },
        getListByAuthor = function (userId) {
            var request;

            if (userId === undefined || userId === null) {
                //console.log('not valid user');
                return;
            }

            request = $http({
                method: "get",
                url: baseUrl + "listQuery",
                params: {
                    userId: userId
                }
            });

            return (request.then(handleSuccess, handleError));
        };

    // Return public API.
    return ({
        getList: getListByAuthor,
        deleteQuery: deleteQuery,
        saveQuery: saveQuery
    });
});
