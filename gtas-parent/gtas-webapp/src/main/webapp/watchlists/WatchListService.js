app.service("watchListService", function ($http, $q) {
    'use strict';
    var baseUrl = '/gtas/wl/',
        handleError = function (response) {
            if (!angular.isObject(response.data) || !response.data.message) {
                return ($q.reject("An unknown error occurred."));
            }
            return ($q.reject(response.data.message));
        },
        handleSuccess = function (response) {
            return (response);
        },
        getRequest = function (method, listTypeName, entity, id, terms) {
            var action = !id ? 'Create' : !terms ? 'Delete' : 'Update';
            return $http({
                method: method,
                url: baseUrl + 'adelorie', //to get once angular security in place
                params: {
                    "@class": "gov.gtas.model.watchlist.json.WatchlistSpec",
                    "name": listTypeName,
                    "entity": entity,
                    "watchlistItems": [{
                        "id": id,
                        "action": action,
                        "terms": terms
                    }]
                }
            });
        },
        service = {
            getTabs: function () {
                // HAVE TO USE .json for now...
                var request = $http({
                    method: 'get',
                    url: baseUrl + 'list'
                });

                return (request.then(handleSuccess, handleError));
            },
            getListItems: function (listTypeName) {
                if (!listTypeName) {
                    return false;
                }
                var request = $http({
                    method: 'get',
                    url: baseUrl + listTypeName
                });

                return (request.then(handleSuccess, handleError));
            },
            deleteItem: function (listTypeName, entity, id) {
                var request;

                if (!listTypeName || !entity || !id) {
                    return false;
                }

                //Method put not delete until told otherwise
                request = getRequest('put', listTypeName, entity, id, null);

                return (request.then(handleSuccess, handleError));
            },
            addItem: function (listTypeName, entity, id, terms) {
                var request;

                if (!listTypeName || !entity || !terms) {
                    return false;
                }

                request = getRequest('post', listTypeName, entity, id, terms);

                return (request.then(handleSuccess, handleError));
            },
            updateItem: function (listTypeName, entity, id, terms) {
                var request;

                if (!listTypeName || !entity || !terms) {
                    return false;
                }

                request = getRequest('put', listTypeName, entity, id, terms);

                return (request.then(handleSuccess, handleError));
            },
            // creates id and adds name and jsonObj of fieldNames/types to listTypeTable
            createListType: function (listName, columns) {
                //watchlist.types[listName] = {columns: columns, data: []};
                //return watchlist.types;
            }
        };

    // Return public API.
    return service;
});
