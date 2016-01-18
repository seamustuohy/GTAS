(function () {
    'use strict';
    app.controller('WatchListController', function ($scope, gridOptionsLookupService, $q, watchListService, $mdSidenav, $interval, spinnerService) {
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
                    this.dobDateObject = entity ? moment(entity.dob).format('YYYY-MM-DD') : undefined;
                }
            },
            resetModels = function (m) {
                //resets all models
                m.Document = new model.Document();
                m.Passenger = new model.Passenger();
            },
            isItTrashTime = function (rows) {
                $scope.disableTrash = $scope.gridApi.selection.getSelectedRows(rows).length === 0;
            };

        $scope.watchlistGrid = gridOptionsLookupService.getGridOptions('watchlist');

        $scope.watchlistGrid.onRegisterApi = function (gridApi) {
            $scope.gridApi = gridApi;
            gridApi.selection.on.rowSelectionChanged($scope, isItTrashTime);
            gridApi.selection.on.rowSelectionChangedBatch($scope, isItTrashTime);
        };

        $scope.documentTypes = [
            {id: "P", label: "PASSPORT"},
            {id: "V", label: "VISA"}
        ];

        watchlist.types = {
            "Document": {
                entity: "DOCUMENT",
                icon: "file",
                columns: gridOptionsLookupService.getLookupColumnDefs('watchlist').DOCUMENT
            },
            "Passenger": {
                entity: "PASSENGER",
                icon: "user",
                columns: gridOptionsLookupService.getLookupColumnDefs('watchlist').PASSENGER
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
            $scope.disableTrash = true;
            $scope.icon = watchlist.types[listName].icon;
            $scope.activeTab = listName;
            $scope.watchlistGrid.columnDefs = watchlist.types[listName].columns;
            $scope.watchlistGrid.exporterCsvFilename = 'watchlist-' + listName + '.csv';
            $scope.watchlistGrid.data = $scope.data[listName];
        };

        $scope.getListItemsFor = function (listName) {
            spinnerService.show('html5spinner');
            watchListService.getListItems(watchlist.types[listName].entity, listName).then(function (response) {
                var obj, data = [], items = response.data.result.watchlistItems,
                    setTerm = function (term) { obj[term.field] = term.type === 'date' ? moment(term.value).toDate() : term.value; }; //moment(term.value).toDate()
                if (items === undefined) {
                    $scope.watchlistGrid.data = [];
                    return false;
                }
                items.forEach(function (item) {
                    obj = {id: item.id};
                    item.terms.forEach(setTerm);
                    data.push(obj);
                });
                $scope.data[listName] = data;
                $scope.updateGridIfData(listName);
                spinnerService.hide('html5spinner');
            });
        };

        $scope.getSaveStateText = function (activeTab) {
            //TODO listen to broadcast, and return save or update
            return 'Save ';
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
                    if (columnType === 'date') { //specific HACK to Passenger dob right now
                        value = moment($scope.Passenger.dobDateObject).format('YYYY-MM-DD');
                    }
                    if (key.indexOf('DateObject') < 0) { //specific Hack for DateObject workaround
                        terms.push({entity: entity, field: key, type: columnType, value: value});
                    }
                    if (!value) {
                        ready = false;
                    }
                }
            });
            if (ready) {
                watchListService[method](objectType, entity, $scope[objectType].id, terms).then(function (response) {
                    if ($scope[$scope.activeTab].id === null) {
                        $scope[$scope.activeTab].id = response.data.result[0];
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
            spinnerService.show('html5spinner');
            watchListService.deleteItems($scope.activeTab, $scope.activeTab, watchlistItems).then(function () {
                rowIndexToDelete = $scope.watchlistGrid.data.indexOf($scope.rowSelected);
                $scope.watchlistGrid.data.splice(rowIndexToDelete, 1);
                $scope.rowSelected = null;
                spinnerService.hide('html5spinner');
                $mdSidenav('save').close();
            });
        };

        $scope.removeRows = function () {
            var selectedRowEntities = $scope.gridApi.selection.getSelectedRows(),
                constructItem = function (rowEntity) {
                    return {id: rowEntity.id, action: 'Delete', terms: null};
                },
                watchlistItems = selectedRowEntities.map(constructItem);
            spinnerService.show('html5spinner');
            watchListService.deleteItems($scope.activeTab, $scope.activeTab, watchlistItems).then(function () {
                var rowIndexToDelete;
                selectedRowEntities.reverse();
                selectedRowEntities.forEach(function (rowEntity) {
                    rowIndexToDelete = $scope.watchlistGrid.data.indexOf(rowEntity);
                    $scope.watchlistGrid.data.splice(rowIndexToDelete, 1);
                });
                $scope.gridApi.selection.clearSelectedRows();
                spinnerService.hide('html5spinner');
            });
        };

        $scope.updateWatchlistService = function () {
            if ($scope.updating) {
                return false;
            }
            $scope.updating = true;
            spinnerService.show('html5spinner');
            watchListService.compile().then(function () {
                spinnerService.hide('html5spinner');
                $scope.updating = false;
            });
        };

        $scope.$scope = $scope;
    });
}());
