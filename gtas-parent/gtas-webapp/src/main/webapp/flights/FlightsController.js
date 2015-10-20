(function () {
    'use strict';
    app.controller('FlightsController', function ($scope, $state, $interval, $stateParams, flightService, gridService, uiGridConstants, executeQueryService, flights, flightsModel) {
        $scope.model = flightsModel;
        
        var stateName = $state ? $state.$current.self.name : 'flights',
            setFlightsGrid = function (grid, response) {
                //NEEDED because java services responses not standardize should have Lola change and Amit revert to what he had;
                var data = stateName === 'queryFlights' ? response.data.result : response.data;
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

        $scope.selectedFlight = $stateParams.flight;
        $scope.flightDirections = flightDirections;
        $scope.stateName = stateName;
        $scope.flightsGrid = {
            paginationPageSizes: [10, 15, 25],
            paginationPageSize: $scope.model.pageSize,
            paginationCurrentPage: $scope.model.pageNumber,
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
                name: 'passengerCount',
                field: 'passengerCount',
                displayName: 'P',
                width: 100,
                enableFiltering: false,
                cellTemplate: '<a ui-sref="flightpax({id: row.entity.id, flightNumber: row.entity.fullFlightNumber, origin: row.entity.origin, destination: row.entity.destination, direction: row.entity.direction, eta: row.entity.eta.substring(0, 10), etd: row.entity.etd.substring(0, 10)})" href="#/flights/{{row.entity.id}}/{{row.entity.fullFlightNumber}}/{{row.entity.origin}}/{{row.entity.destination}}/{{row.entity.direction}}/{{row.entity.eta.substring(0, 10)}}/{{row.entity.etd.substring(0, 10);}}" class="md-primary md-button md-default-theme" >{{COL_FIELD}}</a>'
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

        $scope.queryPassengersOnSelectedFlight = function (row_entity) {
            $state.go('passengers', {
                flightNumber: row_entity.flightNumber,
                origin: row_entity.origin,
                dest: row_entity.dest
            });
        };

        $scope.filter = function () {
            resolvePage();
        };
        $scope.reset = function () {
            $scope.model.reset();
            resolvePage();
        };
        $scope.getTableHeight = function () {
            return gridService.calculateGridHeight($scope.flightsGrid.data.length);
        };
        resolvePage();
    });
}());
