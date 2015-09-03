app.controller('WatchListController', function ($scope, $rootScope, $injector, GridControl, $filter, $q, watchListService, $interval) {
    'use strict';
    var watchlist = {};
    $injector.invoke(GridControl, this, {$scope: $scope});

    watchlist.types = {
        "Document": {
            entity: "DOCUMENT",
            columns: [{
                name: "documentType",
                displayName: "Type",
                enableCellEdit: true,
                "type": "string"
            }, {
                name: "documentNumber",
                displayName: "Number",
                enableCellEdit: true,
                "type": "string"
            }]
        },
        "Passenger": {
            entity: "PASSENGER",
            columns: [{
                name: "firstName",
                displayName: "First Name",
                enableCellEdit: true,
                "type": "string"
            }, {
                name: "lastName",
                displayName: "Last Name",
                enableCellEdit: true,
                "type": "string"
            }, {
                name: "dob",
                displayName:"DOB",
                enableCellEdit: true,
                "type": "date"
            }]
        }
    };

    $scope.gridOpts.enableCellEditOnFocus = true;
    $scope.gridOpts.columnDefs = watchlist.types.Document.columns;

    $scope.updateGrid = function (listName) {
        watchListService.getListItems(watchlist.types[listName].entity, listName).then(function (response) {
            var obj, data = [], items;
            $scope.activeTab = listName;
            $scope.gridOpts.columnDefs = watchlist.types[listName].columns;
            $scope.gridOpts.exporterCsvFilename = 'watchlist-' + listName + '.csv';
            if (response.data.watchlistItems === undefined) {
                $scope.gridOpts.data = data;
                return false;
            }
            items = response.data.watchlistItems;
            items.forEach(function (item) {
                obj = {id: item.id };
                item.terms.forEach(function (term) {
                    obj[term.field] = term.type === 'date' ? moment(term.value).format('YYYY-MM-DD') : term.value;
                });
                data.push(obj);
            });
            $scope.gridOpts.data = data;
        });
    };

    //watchListService.getTabs().then(function (myData) {
    $scope.activeTab = 'Document';
    $scope.tabs = [];
    Object.keys(watchlist.types).forEach(function (key) {
        $scope.tabs.push({title: key});
    });
    //});

    $scope.Add = function () {
        var starterData = {};
        $scope.gridOpts.columnDefs.forEach(function (field) {
            starterData[field.name] = null;
        });
        $scope.gridOpts.data.unshift(starterData);
    };

    $scope.saveRow = function (rowEntity) {
        var watchlistType = watchlist.types[$scope.activeTab],
            columnTypeDict = {},
            entity = watchlistType.entity,
            method = !rowEntity.id ? 'addItem' : 'updateItem',
            terms = [],
            promise = $q.defer(),
            columnType,
            value;

        watchlistType.columns.forEach(function (column) {
            columnTypeDict[column.name] = column.type;
        });
        $scope.gridApi.rowEdit.setSavePromise(rowEntity, promise.promise);

        Object.keys(rowEntity).forEach(function (key) {
            if (['$$hashKey', 'id'].indexOf(key) === -1) {
                columnType = columnTypeDict[key];
                value = rowEntity[key];
                if (columnType === 'date') {
                    value = moment(value).format('YYYY-MM-DD');
                }
                terms.push({entity: entity, field: key, type: columnType, value: value});
            }
        });

        watchListService[method]($scope.activeTab, entity, rowEntity.id, terms).then(function (response) {
            $interval(function () {
                promise.resolve();
            }, 300, 1);
        });
    };

    $scope.gridOpts.onRegisterApi = function (gridApi) {
        $scope.gridApi = gridApi;
        gridApi.rowEdit.on.saveRow($scope, $scope.saveRow);
    };

    $scope.removeRow = function () {
        var entity = watchlist.types[$scope.activeTab].entity,
            selectedRowEntities = $scope.gridApi.selection.getSelectedRows();

        angular.forEach(selectedRowEntities, function (rowEntity) {
            var rowIndexToDelete = $scope.gridOpts.data.indexOf(rowEntity);
            watchListService.deleteItem($scope.activeTab, entity, rowEntity.id).then(function () {
                $scope.gridOpts.data.splice(rowIndexToDelete, 1);
            });
        });
    };

    $scope.$scope = $scope;
});
