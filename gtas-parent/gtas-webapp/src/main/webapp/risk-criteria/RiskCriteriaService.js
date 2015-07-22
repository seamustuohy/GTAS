app.service("riskCriteriaService", function($rootScope, $http, $q) {
    'use strict';
    var baseUrl = '/gtas/udr/';

    function loadRuleById(ruleId) {
        var request;

        if (ruleId === undefined || ruleId === null) {
            //console.log('not valid ruleId');
        } else {
            request = $http({
                method: "get",
                url: baseUrl + "get/" + ruleId
            });

            return (request.then(handleSuccess, handleError));
        }
    }

    function ruleDelete(ruleId, userId) {
        var request;

        if (userId === undefined || userId === null) {
            //console.log('not valid user');
            return;
        }

        if (ruleId === undefined || ruleId === null) {
            //console.log('not valid ruleId');
            return;
        }

        request = $http({
            method: 'delete',
            url: baseUrl + userId + '/' + ruleId
        });

        return (request.then(handleSuccess, handleError));
    }

    function ruleSave(ruleObj, userId) {
        var method, request;

        if (userId === undefined || userId === null) {
            //console.log('not valid user');
            return;
        }

        method = ruleObj.id === null ? 'post' : 'put';
        request = $http({
            method: method,
            url: baseUrl + userId,
            data: ruleObj
        });

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
            url: baseUrl + "list/" + userId,
            params: {
                action: "get"
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
        ruleDelete: ruleDelete,
        ruleSave: ruleSave
    });
});
