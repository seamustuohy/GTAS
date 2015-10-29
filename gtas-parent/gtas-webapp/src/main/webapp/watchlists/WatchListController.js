(function () {
    'use strict';
    app.controller('WatchListController', function ($scope, gridOptionsLookupService, $q, watchListService, $mdSidenav, $interval, spinnerService, $timeout) {
        var watchlist = {},
            tabs = [],
            model = {
                Document: function (entity) {
                    this.id = entity ? entity.id : null;
                    this.documentType = entity ? entity.documentType : null;
                    this.documentNumber = entity ? entity.documentNumber : null;
                },
                Passenger: function (entity) {
                    this.id = entity ? entity.id : null;
                    this.firstName = entity ? entity.firstName : null;
                    this.lastName = entity ? entity.lastName : null;
                    this.dob = entity ? entity.dob : null;
                }
            },
            resetModels = function (m) {
                //resets all models
                m.Document = new model.Document();
                m.Passenger = new model.Passenger();
            },
            isItTrashTime = function (rows) {
                console.log(rows);
//                $scope.disableTrash = $scope.gridApi.selection.selectedCount === 0;
                $scope.disableTrash = $scope.gridApi.selection.getSelectedRows($scope.watchlistGrid).length === 0;
            };

        $scope.watchlistGrid = gridOptionsLookupService.getGridOptions('watchlist');

        $scope.watchlistGrid.onRegisterApi = function (gridApi) {
            $scope.gridApi = gridApi;
            gridApi.selection.on.rowSelectionChanged($scope, isItTrashTime);
            gridApi.selection.on.rowSelectionChangedBatch($scope, isItTrashTime);
//            $scope.$watch($scope.gridApi.selection.selectedCount, isItTrashTime);
        };

        $scope.documentTypes = [
            {id: "P", label: "PASSPORT"},
            {id: "V", label: "VISA"}
        ];

        watchlist.types = {
            "Document": {
                entity: "DOCUMENT",
                icon: "file",
                columns: [{
                    field: "documentType",
                    name: "documentType",
                    displayName: "Type",
                    cellTemplate: "<md-button class=\"md-primary\"  ng-click=\"grid.appScope.editRecord(row.entity)\" style=\"min-width: 0; margin: 0 auto; width: 100%;\" >{{COL_FIELD}}</md-button>",
                    "type": "string"
                }, {
                    field: "documentNumber",
                    name: "documentNumber",
                    displayName: "Number",
                    cellTemplate: "<md-button class=\"md-primary\"  ng-click=\"grid.appScope.editRecord(row.entity)\" style=\"min-width: 0; margin: 0 auto; width: 100%;\" >{{COL_FIELD}}</md-button>",
                    "type": "string"
                }]
            },
            "Passenger": {
                entity: "PASSENGER",
                icon: "user",
                columns: [{
                    field: "firstName",
                    name: "firstName",
                    displayName: "First Name",
                    cellTemplate: "<md-button class=\"md-primary\"  ng-click=\"grid.appScope.editRecord(row.entity)\" style=\"min-width: 0; margin: 0 auto; width: 100%;\" >{{COL_FIELD}}</md-button>",
                    "type": "string"
                }, {
                    field: "lastName",
                    name: "lastName",
                    displayName: "Last Name",
                    cellTemplate: "<md-button class=\"md-primary\"  ng-click=\"grid.appScope.editRecord(row.entity)\" style=\"min-width: 0; margin: 0 auto; width: 100%;\" >{{COL_FIELD}}</md-button>",
                    "type": "string"
                }, {
                    field: "dob",
                    name: "dob",
                    displayName: "DOB",
                    cellTemplate: "<md-button class=\"md-primary\"  ng-click=\"grid.appScope.editRecord(row.entity)\" style=\"min-width: 0; margin: 0 auto; width: 100%;\" >{{COL_FIELD | date:'yyyy-MM-dd'}}</md-button>",
                    "type": "date"
                }]
            }
        };
        $scope.data = {};
        $scope.watchlistGrid.enableRowHeaderSelection = true;
        $scope.watchlistGrid.enableSelectAll = true;
        $scope.watchlistGrid.multiSelect = true;
        $scope.watchlistGrid.columnDefs = watchlist.types.Document.columns;

        $scope.updateGridIfData = function (listName) {
            $scope.gridApi.selection.clearSelectedRows();
            $scope.allSelected = false;
            isItTrashTime();
            $scope.icon = watchlist.types[listName].icon;
            $scope.activeTab = listName;
            $scope.watchlistGrid.columnDefs = watchlist.types[listName].columns;
            $scope.watchlistGrid.exporterCsvFilename = 'watchlist-' + listName + '.csv';
            $scope.watchlistGrid.data = $scope.data[listName];
        };

        $scope.getListItemsFor = function (listName) {
            watchListService.getListItems(watchlist.types[listName].entity, listName).then(function (response) {
                var obj, data = [], items = response.data.result.watchlistItems;
                if (items === undefined) {
                    $scope.watchlistGrid.data = [];
                    return false;
                }
                items.forEach(function (item) {
                    obj = {id: item.id};
                    item.terms.forEach(function (term) {
                        if (term.type === 'date') {
                            obj[term.field] = new Date(term.value);
                        } else {
                            obj[term.field] = term.value;
                        }
                    });
                    data.push(obj);
                });
                $scope.data[listName] = data;
                $scope.updateGridIfData(listName);
            });
        };

        $scope.getSaveStateText = function (activeTab) {
            return 'Save ';
            // todo listen to broadcast, and return save or update
//            return $scope[activeTab].id === null ? 'Save ' : 'Update ';
        };

        $scope.updateGrid = function (listName) {
            if ($scope.data[listName]) {
                $scope.updateGridIfData(listName);
                return;
            }
            $scope.getListItemsFor(listName);
        };

        Object.keys(watchlist.types).forEach(function (key) {
            tabs.push({title: key});
        });

        $scope.tabs = tabs;
        $scope.activeTab = tabs[0].title;
        $scope.icon = tabs[0].icon;
        $scope.rowSelected = null;

        $scope.Add = function () {
            var mode = $scope.activeTab;
            resetModels($scope);
            $scope[mode] = new model[mode]();
            $mdSidenav('save').open();
        };

        $scope.saveRow = function () {
            var objectType = $scope.activeTab,
                watchlistType = watchlist.types[objectType],
                columnTypeDict = {},
                entity = watchlistType.entity,
                method = !$scope[objectType].id ? 'addItem' : 'updateItem',
                terms = [],
                columnType,
                value,
                ready = true;

            watchlistType.columns.forEach(function (column) {
                columnTypeDict[column.name] = column.type;
            });

            Object.keys($scope[objectType]).forEach(function (key) {
                if (['$$hashKey', 'id'].indexOf(key) === -1) {
                    columnType = columnTypeDict[key];
                    value = $scope[objectType][key];
                    if (!value) {
                        ready = false;
                    }
                    if (columnType === 'date') {
                        value = moment(value).format('YYYY-MM-DD').toString();
//                        value = moment(value).toString();
                    }
                    terms.push({entity: entity, field: key, type: columnType, value: value});
                }
            });
            if (ready) {
                watchListService[method](objectType, entity, $scope[objectType].id, terms).then(function (response) {

                    if ($scope[$scope.activeTab].id === null) {
                        $scope[$scope.activeTab].id = response.data.result;
                        $scope.watchlistGrid.data.unshift($scope[$scope.activeTab]);
                    }
                    $scope.gridApi.selection.clearSelectedRows();
                    $scope.rowSelected = null;
                    $mdSidenav('save').close();
                });
            }
        };

        $scope.editRecord = function (row) {
            $scope.gridApi.selection.clearSelectedRows();
            $scope.gridApi.selection.selectRow(row);
            $scope[$scope.activeTab] = row;
            //broadcast save or update
            $scope.rowSelected = row;
            $mdSidenav('save').open();
        };

        $scope.removeRow = function () {
            var rowIndexToDelete,
                watchlistItems = [{id: $scope.rowSelected.id, action: 'Delete', terms: null}];

            watchListService.deleteItems($scope.activeTab, $scope.activeTab, watchlistItems).then(function () {
                rowIndexToDelete = $scope.watchlistGrid.data.indexOf($scope.rowSelected);
                $scope.watchlistGrid.data.splice(rowIndexToDelete, 1);
                $scope.rowSelected = null;
                $mdSidenav('save').close();
            });
        };

        $scope.removeRows = function () {
            var selectedRowEntities = $scope.gridApi.selection.getSelectedRows(),
                constructItem = function (rowEntity) {
                    return {id: rowEntity.id, action: 'Delete', terms: null};
                },
                watchlistItems = selectedRowEntities.map(constructItem);

            watchListService.deleteItems($scope.activeTab, $scope.activeTab, watchlistItems).then(function () {
                var rowIndexToDelete;
                selectedRowEntities.forEach(function (rowEntity) {
                    rowIndexToDelete = $scope.watchlistGrid.data.indexOf(rowEntity);
                    $scope.watchlistGrid.data.splice(rowIndexToDelete, 1);
                });
            });
        };

        $scope.updateWatchlistService = function () {
            if ($scope.updating) {
                return false;
            }
            $scope.updating = true;
            watchListService.compile().then(function () {
                spinnerService.show('html5spinner');
                $timeout(function () {
                    spinnerService.hide('html5spinner');
                    $scope.updating = false;
                }, 2500);
            });
        };

        $scope.$scope = $scope;
    });
}());
