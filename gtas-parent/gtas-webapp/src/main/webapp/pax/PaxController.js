app.controller('PaxController', function ($scope, $rootScope, $injector, jqueryQueryBuilderWidget,
                                          paxService, sharedPaxData, $stateParams, $state,
                                          gridOptionsLookupService, $mdDialog, passengers) {
    'use strict';
    var paginationOptions = gridOptionsLookupService.paginationOptions,
        selectedPassenger,
        PassengerDetailsDialogController = function ($scope) {
            $scope.passenger = selectedPassenger;
        },
        getPage = function () {
            console.log('requesting pax page #' + paginationOptions.pageNumber);
            if ($scope.parent === 'flights') {
                //$scope.passengerGrid.data = passengers;
                paxService.getPax($stateParams.flight.id, paginationOptions).then(function (myData) {
                    $scope.passengerGrid.totalItems = myData.totalPassengers;
                    $scope.passengerGrid.data = myData.passengers;
                });

            } else {
                paxService.getAllPax(paginationOptions).then(function (myData) {
                    $scope.passengerGrid.totalItems = myData.totalPassengers;
                    $scope.passengerGrid.data = myData.passengers;
                });
            }
        };

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

    $scope.isExpanded = true;
    $scope.paxHitList = [];
    $scope.list = sharedPaxData.list;
    $scope.add = sharedPaxData.add;
    $scope.getAll = sharedPaxData.getAll;

    $scope.getPaxSpecificList = function (index) {
        return $scope.list(index);
    };

    $scope.passengerGrid = gridOptionsLookupService.getGridOptions('passengers');
    $scope.passengerGrid.columnDefs = gridOptionsLookupService.getLookupColumnDefs('passengers');
    $scope.passengerGrid.onRegisterApi = function (gridApi) {
        $scope.gridApi = gridApi;
        $scope.loading = false;
        $scope.passengerGrid.data = passengers;

        gridApi.selection.on.rowSelectionChanged($scope, function (row) {
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

    getPage();

    //------- Pre-Refactor-------------------
    //Function to get Rule Hits per Passenger
    $scope.getRuleHits = function (passengerId) {
        var j, i;
        $scope.isExpanded = !$scope.isExpanded;
        if (!$scope.isExpanded) {
            paxService.getRuleHits(passengerId).then(function (myData) { // Begin

                $scope.paxHitList = [];
                $scope.tempPaxHitDetail = [];
                $scope.tempPaxHitList = [];
                var tempObj = [];

                for (j = 0; j < myData.length; j++) {
                    $scope.tempPaxHitList = myData[j].hitsDetailsList;
                    for (i = 0; i < $scope.tempPaxHitList.length; i++) {
                        tempObj = $scope.tempPaxHitList[i];
                        tempObj.ruleTitle = myData[j].ruleTitle;
                        tempObj.ruleConditions = tempObj.ruleConditions.substring(0, (tempObj.ruleConditions.length - 3));
                        $scope.tempPaxHitDetail[i] = tempObj;
                    }

                    $scope.paxHitList.push($scope.tempPaxHitDetail.pop());
                    $scope.tempPaxHitDetail = [];
                }
            }); // END of paxService getRuleHits
        }
    };
});

// Customs Filters
app.filter('capitalize', function () {
    return function (input, all) {
        return (!!input) ? input.replace(/([^\W_]+[^\s-]*) */g, function (txt) {
            return txt.charAt(0).toUpperCase() + txt.substr(1).toLowerCase();
        }) : '';
    };
});

app.filter('orderObjectBy', function () {
    return function (items, field, reverse) {
        var filtered = [];
        items.forEach(function (item) {
            filtered.push(item);
        });
        filtered.sort(function (a, b) {
            return (a[field] > b[field] ? 1 : -1);
        });
        if (reverse) { filtered.reverse(); }
        return filtered;
    };
});
