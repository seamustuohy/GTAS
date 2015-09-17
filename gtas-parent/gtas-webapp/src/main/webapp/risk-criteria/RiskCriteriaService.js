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
        services = {
            loadRuleById: function (ruleId) {
                var request;

                if (!ruleId) { return false; }

                request = $http({
                    method: "get",
                    url: [baseUrl, ruleId].join(':')
                });

                return (request.then(handleSuccess, handleError));
            },
            delete: function (ruleId) {
                var request;

                if (!ruleId) { return false; }

                request = $http({
                    method: 'delete',
                    url: [baseUrl, ruleId].join(':')
                });

                return (request.then(handleSuccess, handleError));
            },
            save: function (data) {
                var method, request;

                method = data.id === null ? 'post' : 'put';
                request = $http({
                    method: method,
                    url: baseUrl,
                    data: data
                });

                return (request.then(handleSuccess, handleError));
            },
            getListByAuthor: function () {
                var request;

                request = $http({
                    method: "get",
                    url: baseUrl
                });

                return (request.then(handleSuccess, handleError));
            }
        };

    // Return public API.
    return ({
        getList: services.getListByAuthor,
        loadRuleById: services.loadRuleById,
        delete: services.delete,
        save: services.save
    });
});
