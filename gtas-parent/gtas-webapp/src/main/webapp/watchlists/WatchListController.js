app.controller('WatchListController', function ($scope, $filter, $q, watchListService, $interval) {
    'use strict';
    var watchlist = localStorage["watchlist"] === undefined ? {} : JSON.parse(localStorage["watchlist"]);
    var removeTemplate = '<input type="button" value="remove" ng-click="getExternalScopes().removeRow(row)" />';

    if (watchlist.types === undefined) {
        watchlist.types = {
            "document": {
                columns: [{
                    name: "id",
                    enableCellEdit: false,
                    enableFiltering: false,
                    enableSorting: false,
                    "type": "number"
                }, {
                    name: "type",
                    enableCellEdit: true,
                    "type": "string"
                }, {
                    name: "number",
                    enableCellEdit: true,
                    "type": "string"
                }],
                data: [
                    {id: 1, "type": "P", "number": "76283AJLG"},
                    {id: 2, "type": "V", "number": "111123AJLV"}
                ]
            },
            "passenger": {
                columns: [{
                    name: "id",
                    enableFiltering: false,
                    enableSorting: false,
                    enableCellEdit: false,
                    "type": "number"
                }, {
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
                    {id: 1, "first name": "John", "last name": "Johnnson", "DOB": "1977-01-01"},
                    {id: 2, "first name": "Jack", "last name": "Johnson", "DOB": "1978-01-01"}
                ]
            }
        };
    }
    localStorage["watchlist"] = JSON.stringify(watchlist);

    $scope.createNew = function () {
        $scope.activeTab = 'create-new';
    };

    $scope.tabs = watchListService.getListTypes();

    $scope.tabfields = watchlist.types;

    $scope.alertMe = function (listName) {
        var columnDefClone = watchlist.types[listName].columns.slice(0);
        columnDefClone.push({
            field: 'remove',
            displayName: '',
            enableFiltering: false,
            enableSorting: false,
            cellTemplate: removeTemplate
        });
        $scope.activeTab = listName;
        $scope.gridOpts.columnDefs = columnDefClone;
        $scope.gridOpts.data = watchlist.types[listName].data;
    };
    $scope.gridOpts = {
        paginationPageSize: 10,
        paginationPageSizes: [],
        enableFiltering: true,
        enableCellEditOnFocus: true,
        showGridFooter: true
    };
    $scope.alertMe('document');

    $scope.Add = function () {
        var starterData = {};
        $scope.gridOpts.columnDefs.forEach(function (field) {
            starterData[field.name] = null;
        });
        $scope.gridOpts.data.unshift(starterData);
    };

    $scope.saveRow = function (rowEntity) {
        // create a fake promise - normally you'd use the promise returned by $http or $resource
        var promise = $q.defer();
        console.log(rowEntity);

//        $scope.gridApi.rowEdit.setSavePromise(rowEntity, promise.promise);
        if (!rowEntity.id) {
            watchListService.addItem($scope.activeTab, rowEntity);
        } else {
            watchListService.updateItem($scope.activeTab, rowEntity);
        }
        $scope.gridApi.rowEdit.setSavePromise(rowEntity, promise.promise);
        $interval(function () {
            promise.resolve();
        }, 300, 1);
    };

    $scope.gridOpts.onRegisterApi = function (gridApi) {
        //set gridApi on scope
        $scope.gridApi = gridApi;
        gridApi.rowEdit.on.saveRow($scope, $scope.saveRow);
    };

    $scope.gridScope = {
        removeRow: function (row) {
            var index = $scope.gridOpts.data.indexOf(row.entity);
            $scope.gridOpts.data.splice(index, 1);
        }
    };
});
