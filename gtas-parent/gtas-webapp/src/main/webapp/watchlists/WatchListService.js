app.service("watchListService", function ($http, $q) {
    'use strict';
    var baseUrl = '/gtas/watchlist/',
        handleError = function (response) {
            if (!angular.isObject(response.data) || !response.data.message) {
                return ($q.reject("An unknown error occurred."));
            }
            return ($q.reject(response.data.message));
        },
        handleSuccess = function (response) {
            return (response.data);
        },
    /* utility functions */
        getNewId = function (items) {
            var highest = Number.NEGATIVE_INFINITY, tmp, i;
            for (i = items.length - 1; i >= 0; i--) {
                tmp = items[i].id;
                if (tmp > highest) { highest = tmp; }
            }
            return highest + 1;
        },
        updatedItems = function (items, valuesObj) {
            var i = items.length - 1;
            for (i; i >= 0; i--) {
                if (items[i].id === valuesObj.id) {
                    items[i] = valuesObj;
                    return items;
                }
            }
        },
    // returns list items for given listType
        getItemList = function (listTypeId) {
            //var request;
            var watchlist = JSON.parse(localStorage['watchlist']);

            if (!listTypeId || listTypeId === null) {
                console.log('no listTypeId');
                return "failure";
            }

            return watchlist[listTypeId];
            //request = $http({
            //    method: "get",
            //    url: baseUrl + "get/" + ruleId
            //});
            //
            //return (request.then(handleSuccess, handleError));
        },
        addItem = function (listTypeId, valuesObj) {
            var watchlist = JSON.parse(localStorage["watchlist"]);
            if (!listTypeId) {
                console.log('no listTypeId');
                return "failure";
            }
            delete valuesObj.$$hashKey;
            console.log(valuesObj);
            valuesObj.id = getNewId(watchlist.types[listTypeId].data);
            console.log(valuesObj.id);
            console.log(valuesObj);
            watchlist.types[listTypeId].data.unshift(valuesObj);
            localStorage["watchlist"] = JSON.stringify(watchlist);
            return true;
        },
        removeItem = function (itemId, listTypeId) {
            var watchlist = JSON.parse(localStorage["watchlist"]),
                items = watchlist.types[listTypeId];

            if (itemId === undefined || itemId === null) {
                return false;
            }

            function filterByID(obj) {
                return obj.id !== undefined && typeof obj.id === 'number' && !isNaN(obj.id);
            }

            items = items.filter(filterByID);
            localStorage["watchlist"] = JSON.stringify(watchlist);
            return items;
        },
        updateItem = function (listTypeId, valuesObj) {
            var watchlist = JSON.parse(localStorage["watchlist"]);
            if (listTypeId === undefined ||listTypeId === null) {
                return false;
            }
            watchlist.types[listTypeId].data = updatedItems(watchlist.types[listTypeId].data, valuesObj);
            localStorage["watchlist"] = JSON.stringify(watchlist);
            return watchlist[listTypeId];
        },
    // creates id and adds name and jsonObj of fieldNames/types to listTypeTable
        createListType = function (listName, columns) {
            var watchlist = JSON.parse(localStorage["watchlist"]);

            watchlist.types[listName] = {columns: columns, data: []};

            localStorage["watchlist"] = JSON.stringify(watchlist);

            return watchlist.types;
        },
        getListTypes = function () {
            var watchlist = JSON.parse(localStorage["watchlist"]),
                types = [];

            Object.keys(watchlist.types).forEach(function (key) {
                types.push({title: key});
            });
            return types;
        },
        getItemFieldTypes = function (listTypeId) {
            var watchlist = JSON.parse(localStorage["watchlist"]),
                columnNames = [];
            if (!listTypeId || !watchlist.types) {
                return false;
            }
            return watchlist.types[listTypeId](function (obj) {
                if (obj.id === listTypeId) {
                    Object.keys(obj.columns).forEach(function (column) {
                        columnNames.push(column);
                    });
                    return columnNames;
                }
            });
        };
    //getItemFieldTypes(typeId) returns fieldsObj;

    // Return public API.
    return ({
        getItemList: getItemList,
        addItem: addItem,
        removeItem: removeItem,
        updateItem: updateItem,
        createListType: createListType,
        getListTypes: getListTypes,
        getItemFieldTypes: getItemFieldTypes
    });
});
