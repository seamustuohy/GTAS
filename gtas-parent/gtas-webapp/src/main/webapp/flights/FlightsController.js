(function () {
    'use strict';
    app.controller('FlightsController', function ($scope, $state, $interval, $stateParams, flightService, gridService, uiGridConstants, executeQueryService, flights) {
        var stateName = $state.$current.self.name,
            setFlightsGrid = function (grid, flights) {
                //NEEDED because java services responses not standardize should have Lola change and Amit revert to what he had;
                var data = stateName === 'queryFlights' ? flights.data.result : flights.data;
                grid.totalItems = data.totalFlights === -1 ? 0 : data.totalFlights;
                grid.data = data.flights;
            },
            flightDirections = [
                {label: 'Inbound', value: 'I'},
                {label: 'Outbound', value: 'O'},
                {label: 'Any', value: ''}
            ],
            getPage = function () {
                setFlightsGrid($scope.flightsGrid, flights || {flights: [], totalFlights: 0 });
            },
            update = function (data) {
                flights = data;
                getPage();
            },
            fetchMethods = {
                queryFlights: function () {
                    var postData, query = JSON.parse(localStorage['query']);
                    postData = {
                        pageNumber: $scope.model.pageNumber,
                        pageSize: $scope.model.pageSize,
                        query: query
                    };
                    executeQueryService.queryFlights(postData).then(update);
                },
                flights: function () {
                    flightService.getFlights($scope.model).then(update);
                }
            },
            resolvePage = function () {
                fetchMethods[stateName]();
            };

        $scope.model = flightService.model;
        $scope.selectedFlight = $stateParams.flight;
        $scope.flightDirections = flightDirections;

        $scope.flightsGrid = {
            paginationPageSizes: [10, 15, 25],
            paginationPageSize: $scope.model.pageSize,
            useExternalPagination: true,
            useExternalSorting: true,
            useExternalFiltering: true,
            enableHorizontalScrollbar: 0,
            enableVerticalScrollbar: 0,
            enableColumnMenus: false,

            onRegisterApi: function (gridApi) {
                $scope.gridApi = gridApi;

                gridApi.core.on.sortChanged($scope, function (grid, sortColumns) {
                    if (sortColumns.length === 0) {
                        $scope.model.sort = null;
                    } else {
                        $scope.model.sort = [];
                        for (var i = 0; i < sortColumns.length; i++) {
                            $scope.model.sort.push({
                                column: sortColumns[i].name,
                                dir: sortColumns[i].sort.direction
                            });
                        }
                    }
                    resolvePage();
                });

                gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                    if ($scope.model.pageNumber !== newPage || $scope.model.pageSize !== pageSize) {
                        $scope.model.pageNumber = newPage;
                        $scope.model.pageSize = pageSize;
                        resolvePage();
                    }
                });
            }
        };

        $scope.flightsGrid.columnDefs = [
            {
                name: 'passengerCount', displayName: 'P', width: 50, enableFiltering: false,
                cellTemplate: '<button id="editBtn" type="button" class="btn-small" ng-click="grid.appScope.passengerNav(row)">{{COL_FIELD}}</button> ',
            },
            {
                name: 'ruleHitCount',
                displayName: 'H',
                width: 50,
                enableFiltering: false,
                cellClass: gridService.colorHits,
                sort: {
                    direction: uiGridConstants.DESC,
                    priority: 0
                }
            },
            {
                name: 'listHitCount',
                displayName: 'L',
                width: 50,
                enableFiltering: false,
                cellClass: gridService.colorHits,
                sort: {
                    direction: uiGridConstants.DESC,
                    priority: 1
                }
            },
            {name: 'fullFlightNumber', displayName: 'Flight', width: 75},
            {
                name: 'eta', displayName: 'ETA',
                sort: {
                    direction: uiGridConstants.DESC,
                    priority: 2
                }
            },
            {name: 'etd', displayName: 'ETD'},
            {name: 'origin'},
            {name: 'originCountry', displayName: 'Country'},
            {name: 'destination'},
            {name: 'destinationCountry', displayName: 'Country'}
        ];

        $scope.passengerNav = function (row) {
            $scope.selectedFlight = row.entity;
            $state.go('flights.passengers', {parent: 'flights', flight: $scope.selectedFlight});
        };

        $scope.filter = function () {
            resolvePage();

        };
        $scope.reset = function () {
            $scope.model = flightService.initialModel();
            getPage();
        };
        $scope.getTableHeight = function () {
            return gridService.calculateGridHeight($scope.flightsGrid.data.length);
        };
        getPage();
    });
}());
