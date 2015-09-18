app.factory("sharedPaxData", function () {
    'use strict';
    var items = [], itemsService = {};

    itemsService.add = function (item, index) {
        items[index] = item;
    };

    itemsService.list = function (index) {
        return items[index];
    };

    itemsService.getAll = function () {
        return items;
    };

    return itemsService;
});