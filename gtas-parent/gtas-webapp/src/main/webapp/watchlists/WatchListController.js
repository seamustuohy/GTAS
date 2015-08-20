app.controller('WatchListController', function ($scope, $filter, $q, watchListService, $window) {
    'use strict';
    var watchlist = localStorage["watchlist"] === undefined ? {} : JSON.parse(localStorage["watchlist"]);

    if (watchlist.types === undefined) {
        watchlist.types = [{ id: 1, name: "document", columns: {"type": "string", "number": "string"}}, { id: 2, name: "passenger", columns: {"first name": "string", "last name": "string", "DOB": "date"}}];
    }

    localStorage["watchlist"] = JSON.stringify(watchlist);

    $scope.tabs = watchListService.getListTypes();
    $scope.alertMe = function () {
        return $scope.tabs.filter(function (tab) {
            return tab.active;
        })[0];
    };
    $scope.tabs[0].active = true;
});
