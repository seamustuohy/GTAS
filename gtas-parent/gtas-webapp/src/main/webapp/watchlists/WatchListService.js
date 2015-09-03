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
                    method: "get",
                    url: baseUrl + 'list'
                });

                return (request.then(handleSuccess, handleError));
            },
            getListItems: function (entity, listTypeName) {
                if (!entity || !listTypeName) {
                    return false;
                }
                var request = $http({
                    method: "get",
                    url: baseUrl + entity + "/" + listTypeName
                });

                return (request.then(handleSuccess, handleError));
            },
            deleteItem: function (listTypeName, entity, id) {
                var request;
                var action = !id ? 'Create' : !terms ? 'Delete' : 'Update';

                if (!listTypeName || !entity || !id) {
                    return false;
                }

                //Method put not delete until told otherwise
//                request = getRequest('put', listTypeName, entity, id, null);
                request = $http.put({
                    url: baseUrl + 'adelorie', //to get once angular security in place
                    params: {
                        "@class": "gov.gtas.model.watchlist.json.WatchlistSpec",
                        "name": listTypeName,
                        "entity": entity,
                        "watchlistItems": [{
                            "id": id,
                            "action": action,
                            "terms": []
                        }]
                    }
                });

                return (request.then(handleSuccess, handleError));
            },
            addItem: function (listTypeName, entity, id, terms) {
                var request, action, url;

                if (!listTypeName || !entity || !terms) {
                    return false;
                }
                //remove hardcoded adelorie once angular security in place
                url = baseUrl + 'adelorie';
                action = !id ? 'Create' : !terms ? 'Delete' : 'Update';
//                request = getRequest('post', listTypeName, entity, id, terms);
                request = $http({
                    method: "post",
                    url: url,
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


                return (request.then(handleSuccess, handleError));
            },
            updateItem: function (listTypeName, entity, id, terms) {
                var request, action;

                if (!listTypeName || !entity || !terms) {
                    return false;
                }

                action = !id ? 'Create' : !terms ? 'Delete' : 'Update';
                request = $http.put({
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

                return (request.then(handleSuccess, handleError));
            },
            createListType: function (listName, columns) {
                //watchlist.types[listName] = {columns: columns, data: []};
                //return watchlist.types;
            }
        };

    // Return public API.
    return service;
});
