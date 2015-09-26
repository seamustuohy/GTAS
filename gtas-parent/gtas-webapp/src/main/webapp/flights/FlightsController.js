app.controller('FlightsController', function ($scope, flightService, $state, $interval, $stateParams, gridOptionsLookupService) {
    'use strict';
    var paginationOptions = gridOptionsLookupService.paginationOptions,
        getPage = function () {
            console.log('requesting page #' + paginationOptions.pageNumber);
            flightService.getFlights(paginationOptions).then(function (page) {
                $scope.flightsGrid.totalItems = page.totalFlights;
                $scope.flightsGrid.data = page.flights;

                $interval(function () {
                    $scope.gridApi.selection.selectRow($scope.flightsGrid.data[0]);
                }, 0, 1);
            });
        };

    $scope.selectedFlight = $stateParams.flight;
    $scope.flightsGrid = gridOptionsLookupService.getGridOptions('flights');
    $scope.flightsGrid.columnDefs = gridOptionsLookupService.getLookupColumnDefs('flights');
    $scope.flightsGrid.onRegisterApi = function (gridApi) {
        $scope.gridApi = gridApi;

        gridApi.core.on.sortChanged($scope, function (grid, sortColumns) {
            if (sortColumns.length === 0) {
                paginationOptions.sort = null;
            } else {
                paginationOptions.sort = sortColumns[0].sort.direction;
            }
            getPage();
        });

        gridApi.core.on.filterChanged($scope, function () {
            var grid = this.grid;
        });

        gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
            paginationOptions.pageNumber = newPage;
            paginationOptions.pageSize = pageSize;
            getPage();
        });

        gridApi.selection.on.rowSelectionChanged($scope, function (row) {
            $scope.selectedFlight = row.entity;
            console.log($scope.selectedFlight);
        });
    };

    $scope.passengerNav = function (row) {
        $scope.selectedFlight = row.entity;
        $state.go('flights.passengers', {parent: 'flights', flight: $scope.selectedFlight});
    };

    getPage();

    $state.go('flights.all');
});
