app.controller('WatchListController', function ($scope, $filter, $q, watchListService, $window) {
    'use strict';
    var watchlist = localStorage["watchlist"] === undefined ? {} : JSON.parse(localStorage["watchlist"]);

    if (watchlist.types === undefined) {
        watchlist.types = {
            "document": {
                columns: [{
                    name: "type",
                    enableCellEdit: true,
                    "type": "string"
                }, {
                    name: "number",
                    enableCellEdit: true,
                    "type": "string"
                }],
                data: [
                    {"type": "P", "number": "76283AJLG"},
                    {"type": "V", "number": "111123AJLV"}
                ]
            },
            "passenger": {
                columns: [{
                    name: "first name",
                    enableCellEdit: true,
                    "type": "string"
                }, {
                    name: "last name",
                    enableCellEdit: true,
                    "type": "string"
                }, {
                    name: "DOB",
                    enableCellEdit: true,
                    "type": "date"
                }],
                data: [
                    {"first name": "John", "last name": "Johnnson", "DOB": "1977-01-01"},
                    {"first name": "Jack", "last name": "Johnson", "DOB": "1978-01-01"}
                ]
            }
        };
    }
    localStorage["watchlist"] = JSON.stringify(watchlist);

    $scope.createNew = function () {
        $scope.activeTab = 'create-new';
    };

    $scope.tabs = watchListService.getListTypes();

    $scope.alertMe = function (listName) {
        $scope.activeTab = listName;
        $scope.gridOpts.columnDefs = watchlist.types[listName].columns;
        $scope.gridOpts.data = watchlist.types[listName].data;
    };

    $scope.gridOpts = {
        paginationPageSize: 10,
        paginationPageSizes: [],
        enableFiltering: true,
        enableCellEditOnFocus: true,
        showGridFooter: true,
        columnDefs: watchlist.types.document.columns,
        data: watchlist.types.document.data
    };
    $scope.saveRow = function (rowEntity) {
        // create a fake promise - normally you'd use the promise returned by $http or $resource
        var promise = $q.defer();
        $scope.gridApi.rowEdit.setSavePromise(rowEntity, promise.promise);

        // fake a delay of 3 seconds whilst the save occurs, return error if gender is "male"
        $interval(function () {
            if (rowEntity.gender === 'male' ){
                promise.reject();
            } else {
                promise.resolve();
            }
        }, 3000, 1);
    };

    $scope.gridOpts.onRegisterApi = function (gridApi) {
        //set gridApi on scope
        $scope.gridApi = gridApi;
        gridApi.rowEdit.on.saveRow($scope, $scope.saveRow);
    };
});
