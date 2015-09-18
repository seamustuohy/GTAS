// to be replaced by crudService.js next sprint

app.service("riskCriteriaService", function ($http, $q) {
    'use strict';
    var baseUrl = '/gtas/udr/',
        handleError = function (response) {
            if (!angular.isObject(response.data) || !response.data.message) {
                return ($q.reject("An unknown error occurred."));
            }
            return ($q.reject(response.data.message));
        },
        handleSuccess = function (response) {
            return (response.data);
        },
        loadRuleById = function (ruleId) {
            var request;

            if (ruleId === undefined || ruleId === null) {
                return;
            }

            request = $http({
                method: "get",
                url: baseUrl
            });

            return (request.then(handleSuccess, handleError));
        },
        ruleDelete = function (ruleId, userId) {
            var request;

            if (userId === undefined || userId === null || ruleId === undefined || ruleId === null) {
                return;
            }

            request = $http({
                method: 'delete',
                url: baseUrl + userId + '/' + ruleId
            });

            return (request.then(handleSuccess, handleError));
        },
        ruleSave = function (ruleObj, userId) {
            var method, request;

            if (userId === undefined || userId === null) {
                return;
            }

            method = ruleObj.id === null ? 'post' : 'put';
            request = $http({
                method: method,
                url: baseUrl,
                data: ruleObj
            });

            return (request.then(handleSuccess, handleError));
        },
        getListByAuthor = function (userId) {
            var request;

            if (userId === undefined || userId === null) {
                return;
            }

            request = $http({
                method: "get",
                url: baseUrl
            });
            return (request.then(handleSuccess, handleError));
        };

    // Return public API.
    return ({
        getList: getListByAuthor,
        loadRuleById: loadRuleById,
        ruleDelete: ruleDelete,
        ruleSave: ruleSave
    });
});
