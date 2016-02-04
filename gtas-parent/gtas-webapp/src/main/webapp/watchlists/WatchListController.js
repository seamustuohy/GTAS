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
                    this.dob = entity ? entity.dob : undefined;
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
        $scope.watchlistGrid.importerDataAddCallback = function ( grid, newObjects ) {
            spinnerService.show('html5spinner');
            var listName = $scope.activeTab;
            watchListService.deleteListItems(watchlist.types[listName].entity, listName).then(function (response) {
                var objectType = $scope.activeTab,
                    watchlistType = watchlist.types[objectType],
                    columnTypeDict = {},
                    entity = watchlistType.entity,
                    watchlistItems = [],
                    columnType,
                    value,
                    terms,
                    ready = true;

                watchlistType.columns.forEach(function (column) {
                    columnTypeDict[column.name] = column.type;
                });

                newObjects.forEach(function (obj){
                    terms = [];
                    Object.keys(obj).forEach(function (key) {
                        if (['$$hashKey', 'id'].indexOf(key) === -1) {
                            columnType = columnTypeDict[key];
                            value = obj[key];
                            if (!value) {
                                ready = false;
                            }
                            terms.push({entity: entity, field: key, type: columnType, value: value});
                        }
                    });
                    watchlistItems.push({id: null, action: 'Create', terms: terms});

                });
                if (ready) {
                    watchListService.addItems(objectType, entity, watchlistItems).then(function (response) {
                        $scope.getListItemsFor(objectType);
                        spinnerService.hide('html5spinner');
                    });
                }
            });
        };
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
                    setTerm = function (term) { obj[term.field] = term.value; };
//                    setTerm = function (term) { obj[term.field] = term.type === 'date' ?  moment(term.value).toDate() : term.value; };
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
            if (mode === "Passenger" && $scope[mode].dob !== undefined) {
                $scope[mode].dob = moment($scope[mode].dob).toDate();
            }
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
                        value = moment(value).format('YYYY-MM-DD');
                    }
                    terms.push({entity: entity, field: key, type: columnType, value: value});
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
                    $scope.getListItemsFor($scope.activeTab);
//                    $scope.updateGrid($scope.activeTab);
                    $mdSidenav('save').close();
                });
            }
        };

        $scope.editRecord = function (row) {
            $scope.gridApi.selection.clearSelectedRows();
            $scope.gridApi.selection.selectRow(row);
            $scope[$scope.activeTab] = $.extend({}, row);
            if ($scope.activeTab === "Passenger" && $scope[$scope.activeTab].dob !== undefined) {
                $scope[$scope.activeTab].dob = moment($scope[$scope.activeTab].dob).toDate();
            }
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
                $scope.disableTrash = true;
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
