(function (){
    'use strict';
    var ruleGridColumns = [{
            name: 'ruleId',
            "width": 60,
            displayName: 'Id',
            cellTemplate: ' <button id="editBtn" type="button" class="btn-small" ng-click="grid.appScope.ruleIdClick(row)">{{COL_FIELD}}</button>'
        }, {
            name: 'ruleTitle',
            displayName: 'Title'
        }, {
            name: 'ruleConditions',
            displayName: 'Conditions',
            field: 'hitsDetailsList[0]',
            cellFilter: 'hitsConditionDisplayFilter'
        }],
        setSubGridOptions = function (data, appScopeProvider) {
            data.passengers.forEach(function (rowScope) {
                rowScope.subGridOptions = {
                    appScopeProvider: appScopeProvider,
                    columnDefs: ruleGridColumns,
                    data: []
                };
            });
        },
        flightDirections = [
            {label: 'Inbound', value: 'I'},
            {label: 'Outbound', value: 'O'},
            {label: 'Any', value: ''}
        ],
        setPassengersGrid = function(grid, data) {
            grid.totalItems = data.totalPassengers;
            grid.data = data.passengers;
        }
    app
        .controller('PassengerDetailCtrl', function ($scope, passenger) {
            $scope.passenger = passenger.data;
        })
        .controller('PaxController', function ($scope, $injector, $stateParams, $state,
                                               paxService, sharedPaxData, uiGridConstants, gridService,
                                               queryBuilderFactory, jqueryQueryBuilderService, jqueryQueryBuilderWidget) {
            var getPage = function () {
                if ($stateParams.parent === 'flights') {
                    paxService.getPax($stateParams.flight.id, $scope.model).then(function (data) {
                        setSubGridOptions(data, $scope);
                        setPassengersGrid($scope.passengerGrid, data);
                    });
                } else {
                    paxService.getAllPax($scope.model).then(function (data) {
                        setSubGridOptions(data, $scope);
                        setPassengersGrid($scope.passengerGrid, data);
                    });
                }
            };

            $scope.flightDirections = flightDirections;

            $scope.model = paxService.model;

            $injector.invoke(jqueryQueryBuilderWidget, this, {$scope: $scope});
            $injector.invoke(queryBuilderFactory, this, {$scope: $scope});

            $scope.selectedFlight = $stateParams.flight;
            $scope.parent = $stateParams.parent;

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
                        getPage();
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
                        getPage();
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
                    cellClass: gridService.colorHits,
                    cellTemplate: '<div></div>'
                },
                {
                    name: 'onWatchList', displayName: 'L', width: 50,
                    cellClass: gridService.colorHits,
                    cellTemplate: '<div></div>'
                },
                {name: 'passengerType', displayName: 'Type', width: 50},
                {
                    name: 'lastName', displayName: 'Last Name',
                    sort: {
                        direction: uiGridConstants.DESC,
                        priority: 1
                    },
                    cellTemplate: '<div class="ngCellText"><a ui-sref="detail" target="pax.detail" href="#/paxdetail/{{row.entity.id}}/{{row.entity.flightId}}">{{COL_FIELD}}</a></div>'
                },
                {name: 'firstName', displayName: 'First Name'},
                {name: 'middleName', displayName: 'Middle'},
                {name: 'fullFlightNumber', displayName: 'Flight', visible: ($scope.parent !== 'flights')},
                {name: 'eta', displayName: 'ETA', visible: ($scope.parent !== 'flights')},
                {name: 'etd', displayName: 'ETD', visible: ($scope.parent !== 'flights')},
                {name: 'gender', displayName: 'G', width: 50},
                {name: 'dob', displayName: 'DOB', cellFilter: 'date'},
                {name: 'citizenshipCountry', displayName: 'CTZ', width: 75}
            ];

            $scope.filter = function () {
                getPage();
            };

            $scope.reset = function () {
                $scope.model = paxService.initialModel();
                getPage();
            };

            $scope.getTableHeight = function () {
                return gridService.calculateGridHeight($scope.passengerGrid.data.length);
            };

            getPage();
        })
        .controller('QueryPaxController', function ($scope, $injector, $stateParams, $state,
                                                    paxService, sharedPaxData, uiGridConstants, gridService,
                                                    queryBuilderFactory, jqueryQueryBuilderService, jqueryQueryBuilderWidget, queryResults) {
            var getPage = function () {
                var data = $scope.queryResults ? $scope.queryResults.result : { passengers: [], totalPassengers: 0 };
                //, qbTitle = localStorage['qbTitle'] || 'query results';
                setSubGridOptions(data, $scope);
                setPassengersGrid($scope.passengerGrid, data);
                //$scope.passengerGrid.exporterPdfHeader.text = qbTitle;
                //$scope.passengerGrid.exporterCsvFilename = 'queryResults.csv';
                //$scope.passengerGrid.exporterPdfHeader = { text: qbTitle, style: 'headerStyle' };
            };

            $scope.queryResults = queryResults.data;

            $scope.flightDirections = flightDirections;

            $scope.model = paxService.model;

            $injector.invoke(jqueryQueryBuilderWidget, this, {$scope: $scope});
            $injector.invoke(queryBuilderFactory, this, {$scope: $scope});

            $scope.selectedFlight = $stateParams.flight;
            $scope.parent = $stateParams.parent;

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
                        getPage();
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
                        getPage();
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
                    cellClass: gridService.colorHits,
                    cellTemplate: '<div></div>'
                },
                {
                    name: 'onWatchList', displayName: 'L', width: 50,
                    cellClass: gridService.colorHits,
                    cellTemplate: '<div></div>'
                },
                {name: 'passengerType', displayName: 'Type', width: 50},
                {
                    name: 'lastName', displayName: 'Last Name',
                    sort: {
                        direction: uiGridConstants.DESC,
                        priority: 1
                    },
                    cellTemplate: '<div class="ngCellText"><a ui-sref="detail" target="pax.detail" href="#/paxdetail/{{row.entity.id}}/{{row.entity.flightId}}">{{COL_FIELD}}</a></div>'
                },
                {name: 'firstName', displayName: 'First Name'},
                {name: 'middleName', displayName: 'Middle'},
                {name: 'fullFlightNumber', displayName: 'Flight', visible: ($scope.parent !== 'flights')},
                {name: 'eta', displayName: 'ETA', visible: ($scope.parent !== 'flights')},
                {name: 'etd', displayName: 'ETD', visible: ($scope.parent !== 'flights')},
                {name: 'gender', displayName: 'G', width: 50},
                {name: 'dob', displayName: 'DOB', cellFilter: 'date'},
                {name: 'citizenshipCountry', displayName: 'CTZ', width: 75}
            ];

            $scope.filter = function () {
                getPage();
            };

            $scope.reset = function () {
                $scope.model = paxService.initialModel();
                getPage();
            };

            $scope.getTableHeight = function () {
                return gridService.calculateGridHeight($scope.passengerGrid.data.length);
            };

            getPage();
        });
}());

