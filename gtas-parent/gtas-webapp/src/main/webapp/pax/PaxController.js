app.controller('PaxController', function ($scope, $rootScope, $injector, jqueryQueryBuilderWidget,
                                          paxService, sharedPaxData, $stateParams, $state, uiGridSelectionService,
                                          gridOptionsLookupService, $mdDialog, passengers) {
    'use strict';
    var paginationOptions = gridOptionsLookupService.paginationOptions,
        selectedPassenger,
        PassengerDetailsDialogController = function ($scope) {
            $scope.passenger = selectedPassenger;
        },
        setSubGridOptions = function(data) {
            for (i = 0; i < data.passengers.length; i++) {
                var rowScope = data.passengers[i];
                rowScope.subGridOptions = {
                    appScopeProvider: $scope,
                    columnDefs: [
                        { name: 'ruleId', "width": 60, displayName: 'Id' },
                        { name: 'ruleTitle', displayName: 'Title' },
                        { name: 'ruleConditions', displayName: 'Conditions' }
                    ],
                    data: []
                }
            }
        },
        getPage = function () {
            console.log('requesting pax page #' + paginationOptions.pageNumber);
            if ($scope.parent === 'flights') {
                paxService.getPax($stateParams.flight.id, paginationOptions).then(function (data) {
                    setSubGridOptions(data);
                    $scope.passengerGrid.totalItems = data.totalPassengers;
                    $scope.passengerGrid.data = data.passengers;
                });
            } else {
                paxService.getAllPax(paginationOptions).then(function (data) {
                    setSubGridOptions(data);
                    $scope.passengerGrid.totalItems = data.totalPassengers;
                    $scope.passengerGrid.data = data.passengers;
                });
            }
        };

    $scope.selectedFlight = $stateParams.flight;
    $scope.parent = $stateParams.parent;
    $scope.isExpanded = true;
    $scope.paxHitList = [];
    $scope.list = sharedPaxData.list;
    $scope.add = sharedPaxData.add;
    $scope.getAll = sharedPaxData.getAll;

    $scope.getPaxSpecificList = function (index) {
        return $scope.list(index);
    };

    $scope.selectButtonClick = function (row, evt) {
        evt.stopPropagation();

        if (evt.shiftKey) {
            uiGridSelectionService.shiftSelect(self, row, evt, self.options.multiSelect);
        }
        else if (evt.ctrlKey || evt.metaKey) {
            uiGridSelectionService.toggleRowSelection(self, row, evt, self.options.multiSelect, self.options.noUnselect);
        }
        else {
            uiGridSelectionService.toggleRowSelection(self, row, evt, (self.options.multiSelect && !self.options.modifierKeysToMultiSelect), self.options.noUnselect);
        }
    };

    $scope.passengerGrid = gridOptionsLookupService.getGridOptions('passengers');
    $scope.passengerGrid.columnDefs = gridOptionsLookupService.getLookupColumnDefs('passengers');
    $scope.passengerGrid.onRegisterApi = function (gridApi) {
        $scope.gridApi = gridApi;

        gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
            paginationOptions.pageNumber = newPage;
            paginationOptions.pageSize = pageSize;
            getPage();
        });

        gridApi.selection.on.rowSelectionChanged($scope, function (row) {
            paxService.getRuleHits(row.entity.id).then(function (data) {
                console.log('get rule hits for pax ' + row.entity.id);
                row.entity.subGridOptions.data = data;
            });

            if (row.isSelected) {
                if ($scope.parent === 'flights') {
                    $stateParams.id = row.entity.id;
                    $stateParams.flightId = row.entity.flightId;
                    $state.go('flights.passengers.detail', {
                        id: row.entity.id,
                        flightId: row.entity.flightId,
                        parent: $scope.parent
                    });
                }
                $scope.showPaxDetailsModal(row.entity);
            }
        });
    };

    $scope.getTableHeight = function () {
        var rowHeight = 30,
            headerHeight = 30;
        return {
            height: ($scope.passengerGrid.data.length * rowHeight + 2 * headerHeight) + "px"
        };
    };

    getPage();

    $scope.showPaxDetailsModal = function (passenger) {
        selectedPassenger = passenger;
        $mdDialog.show({
            controller: PassengerDetailsDialogController,
            templateUrl: 'pax/pax.detail.html',
            parent: angular.element(document.body),
            clickOutsideToClose: true
        });
    };

    $scope.selectedFlight = $stateParams.flight;
    $scope.parent = $stateParams.parent;
    $injector.invoke(jqueryQueryBuilderWidget, this, {$scope: $scope});

    $scope.passengerGrid = gridOptionsLookupService.getGridOptions('passengers');
    $scope.passengerGrid.columnDefs = gridOptionsLookupService.getLookupColumnDefs('passengers');
    $scope.passengerGrid.onRegisterApi = function (gridApi) {
        $scope.gridApi = gridApi;
        $scope.loading = false;
        $scope.passengerGrid.data = passengers;

        gridApi.selection.on.rowSelectionChanged($scope, function (row) {
            if (row.isSelected) {
                //if ($scope.parent === 'flights') {
                //    $stateParams.id = row.entity.id;
                //    $stateParams.flightId = row.entity.flightId;
                //    $state.go('flights.passengers.detail', {
                //        id: row.entity.id,
                //        flightId: row.entity.flightId,
                //        parent: $scope.parent
                //    });
                //}
                $scope.showPaxDetailsModal(row.entity);
            }
        });
    };

    getPage();
});