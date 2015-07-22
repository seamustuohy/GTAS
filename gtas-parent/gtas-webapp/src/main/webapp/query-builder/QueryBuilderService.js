app.service("queryBuilderService", function ($rootScope, $http, $q) {
    'use strict';
    var baseUrl = '/gtas/query/';

    // LOLA DID NOT PROVIDE
    function loadRuleById(ruleId, userId) {
        var request;

        if (ruleId === undefined || ruleId === null) {
            //console.log('not valid queryId');
            return;
        }

        request = $http({
            method: "get",
            url: baseUrl + "listQuery",
            params: {
                userId: userId,
                id: ruleId
            }
        });

        return (request.then(handleSuccess, handleError));
    }

    function deleteQuery(ruleId, userId) {
        var request;

        if (userId === undefined || userId === null) {
//            console.log('not valid user');
            return;
        }

        if (ruleId === undefined || ruleId === null) {
//            console.log('not valid queryId');
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
    }

    function saveQuery(data) {
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
    }

    function getListByAuthor(userId) {
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
    }

    function handleError(response) {
        if (!angular.isObject(response.data) || !response.data.message) {
            return ($q.reject("An unknown error occurred."));
        }
        return ($q.reject(response.data.message));
    }

    function handleSuccess(response) {
        return (response.data);
    }

    // Return public API.
    return ({
        getList: getListByAuthor,
        loadRuleById: loadRuleById,
        deleteQuery: deleteQuery,
        saveQuery: saveQuery
    });
});
