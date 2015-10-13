(function () {
    'use strict';
    app.controller('PassengerDetailCtrl', function ($scope, passenger) {
        $scope.passenger = passenger.data;
    });
    app.controller('PaxController', function ($scope, $injector, $stateParams, $state, paxService, sharedPaxData, uiGridConstants, gridService,
                                              queryBuilderFactory, jqueryQueryBuilderService, jqueryQueryBuilderWidget, executeQueryService, passengers, $timeout) {
        var stateName = $state.$current.self.name,
            ruleGridColumns = [{
                name: 'ruleTitle',
                displayName: 'Title',
                cellTemplate: '<md-button class="md-primary md-button md-default-theme" ng-click="grid.appScope.ruleIdClick(row)">{{COL_FIELD}}</md-button>'
            }, {
                name: 'ruleConditions',
                displayName: 'Conditions',
                field: 'hitsDetailsList[0]',
                cellFilter: 'hitsConditionDisplayFilter'
            }],
            setSubGridOptions = function (data, appScopeProvider) {
                data.passengers.forEach(function (entity_row) {
                    if (!entity_row.flightId) {
                        entity_row.flightId = $stateParams.id;
                    }
                    entity_row.subGridOptions = {
                        appScopeProvider: appScopeProvider,
                        columnDefs: ruleGridColumns,
                        data: []
                    };
                });
            },
            setPassengersGrid = function (grid, response) {
                //NEEDED because java services responses not standardize should have Lola change and Amit revert to what he had;
                var data = stateName === 'queryPassengers' ? response.data.result : response.data;
                setSubGridOptions(data, $scope);
                grid.totalItems = data.totalPassengers === -1 ? 0 : data.totalPassengers;
                grid.data = data.passengers;
            },
            getPage = function () {
                setPassengersGrid($scope.passengerGrid, passengers);
            },
            update = function (data) {
                passengers = data;
                getPage();
            },
            fetchMethods = {
                'queryPassengers': function () {
                    var postData, query = JSON.parse(localStorage['query']);
                    postData = {
                        pageNumber: $scope.model.pageNumber,
                        pageSize: $scope.model.pageSize,
                        query: query
                    };
                    executeQueryService.queryPassengers(postData).then(update);
                },
                'flightpax': function () {
                    paxService.getPax($stateParams.id, $scope.model).then(update);
                },
                'paxAll': function () {
                    paxService.getAllPax($scope.model).then(update);
                }
            },
            resolvePage = function () {
                fetchMethods[stateName]();
            },
            flightDirections = [
                {label: 'Inbound', value: 'I'},
                {label: 'Outbound', value: 'O'},
                {label: 'Any', value: ''}
            ];
        $scope.flightDirections = flightDirections;

        $scope.model = stateName === 'flightpax' ? paxService.initialModel($stateParams) : paxService.model;

        $injector.invoke(jqueryQueryBuilderWidget, this, {$scope: $scope});
        $injector.invoke(queryBuilderFactory, this, {$scope: $scope});
        $scope.stateName = $state.$current.self.name;

        jqueryQueryBuilderService.init('riskcriteria');

        $scope.ruleIdClick = function (row) {
            $scope.getRuleObject(row.entity.ruleId);
        };

        $scope.getRuleObject = function (ruleID) {
            jqueryQueryBuilderService.loadRuleById(ruleID).then(function (myData) {
                $scope.$builder.queryBuilder('readOnlyRules', myData.result.details);
                $scope.hitDetailDisplay = myData.result.summary.title;
                document.getElementById("QBModal").style.display = "block";

                $scope.closeDialog = function () {
                    document.getElementById("QBModal").style.display = "none";
                };
            });
        };

        $scope.isExpanded = true;
        $scope.paxHitList = [];
        $scope.list = sharedPaxData.list;
        $scope.add = sharedPaxData.add;
        $scope.getAll = sharedPaxData.getAll;

        $scope.getPaxSpecificList = function (index) {
            return $scope.list(index);
        };

        $scope.buildAfterEntitiesLoaded();

        $scope.passengerGrid = {
            paginationPageSizes: [10, 15, 25],
            paginationPageSize: $scope.model.pageSize,
            useExternalPagination: true,
            useExternalSorting: true,
            useExternalFiltering: true,
            enableHorizontalScrollbar: 1,
            enableVerticalScrollbar: 0,
            enableColumnMenus: false,
            multiSelect: false,

            expandableRowTemplate: '<div ui-grid="row.entity.subGridOptions"></div>',

            onRegisterApi: function (gridApi) {
                $scope.gridApi = gridApi;

                gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                    $scope.model.pageNumber = newPage;
                    $scope.model.pageSize = pageSize;
                    resolvePage();
                });

                gridApi.core.on.sortChanged($scope, function (grid, sortColumns) {
                    if (sortColumns.length === 0) {
                        $scope.model.sort = null;
                    } else {
                        $scope.model.sort = [];
                        for (var i = 0; i < sortColumns.length; i++) {
                            $scope.model.sort.push({column: sortColumns[i].name, dir: sortColumns[i].sort.direction});
                        }
                    }
                    resolvePage();
                });

                gridApi.expandable.on.rowExpandedStateChanged($scope, function (row) {
                    if (row.isExpanded) {
                        paxService.getRuleHits(row.entity.id).then(function (data) {
                            row.entity.subGridOptions.data = data;
                        });
                    }
                });
            }
        };

        $scope.passengerGrid.columnDefs = [
            {
                name: 'onRuleHitList', displayName: 'H', width: 50,
                cellClass: gridService.ruleHit,
                sort: {
                    direction: uiGridConstants.DESC,
                    priority: 0
                },
                cellTemplate: '<md-button ng-click="grid.api.expandable.toggleRowExpansion(row.entity)"><i class="glyphicon glyphicon-flag"></i></md-button>'
            },
            {
                name: 'onWatchList', displayName: 'L', width: 70,
                cellClass: gridService.anyWatchlistHit,
                sort: {
                    direction: uiGridConstants.DESC,
                    priority: 1
                },
                cellTemplate: '<div><i class="{{row.entity.onWatchList|watchListHit}}"></i> <i class="{{row.entity.onWatchListDoc|watchListDocHit}}"></i></div>'
            },
            {name: 'passengerType', displayName: 'Type', width: 50},
            {
                name: 'lastName', displayName: 'Last Name',
                cellTemplate: '<md-button href="#/paxdetail/{{row.entity.id}}/{{row.entity.flightId}}" title="Launch Flight Passengers in new window" target="pax.detail" class="md-primary md-button md-default-theme" >{{COL_FIELD}}</md-button>'
            },
            {name: 'firstName', displayName: 'First Name'},
            {name: 'middleName', displayName: 'Middle'},
            {name: 'fullFlightNumber', displayName: 'Flight' },
            {
                name: 'eta',
                sort: {
                    direction: uiGridConstants.DESC,
                    priority: 2
                },
                displayName: 'ETA',
                visible: (stateName === 'paxAll')
            },
            {name: 'etd', displayName: 'ETD', visible: (stateName === 'paxAll')},
            {name: 'gender', displayName: 'G', width: 50},
            {name: 'dob', displayName: 'DOB', cellFilter: 'date'},
            {name: 'citizenshipCountry', displayName: 'CTZ', width: 75}
        ];

        $scope.filter = function () {
            resolvePage();
        };

        $scope.reset = function () {
            $scope.model = paxService.initialModel();
            resolvePage();
        };

        $scope.getTableHeight = function () {
            return gridService.calculateGridHeight($scope.passengerGrid.data.length);
        };

        getPage();
    });
}());
