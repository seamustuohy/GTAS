app.service("crudService", function ($http, $q) {
    'use strict';
    var baseUrl,
        URLS = {
            querybuilder: '/gtas/query/',
            riskcriteria: '/gtas/udr/'
        },
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
            init: function (url) {
                baseUrl = URLS[url];
            },
            loadRuleById: function (ruleId) {
                var request;

                if (!ruleId) { return false; }

                request = $http({
                    method: "get",
                    url: [baseUrl, ruleId].join('')
                });

                return (request.then(handleSuccess, handleError));
            },
            delete: function (ruleId) {
                var request;

                if (!ruleId) { return false; }

                request = $http({
                    method: 'delete',
                    url: [baseUrl, ruleId].join('')
                });

                return (request.then(handleSuccess, handleError));
            },
            save: function (data) {
                var method, request, url;

                if (data.id === null) {
                    method = 'post';
                    url = baseUrl;
                } else {
                    method = 'put';
                    url = baseUrl + data.id;
                }

                request = $http({
                    method: method,
                    url: url,
                    data: data
                });

                return (request.then(handleSuccess, handleError));
            },
            getList: function () {
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
        init: services.init,
        getList: services.getList,
        loadRuleById: services.loadRuleById,
        delete: services.delete,
        save: services.save
    });
});