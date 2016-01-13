(function () {
    'use strict';
    app.controller('PassengerDetailCtrl', function ($scope, passenger) {
        $scope.passenger = passenger.data;
    });
    app.controller('PaxController', function ($scope, $injector, $stateParams, $state, paxService, sharedPaxData, uiGridConstants, gridService,
                                              jqueryQueryBuilderService, jqueryQueryBuilderWidget, executeQueryService, passengers,
                                              $timeout, paxModel, $http) {
        function createFilterFor(query) {
            var lowercaseQuery = query.toLowerCase();
            return function filterFn(contact) {
                return (contact.lowerCasedName.indexOf(lowercaseQuery) >= 0);
            };
        }
        /* Search for airports. */
        function querySearch(query) {
            var results = query && query.length ? self.allAirports.filter(createFilterFor(query)) : [];
            return results;
        }

        $scope.searchSort = querySearch;
        $scope.model = paxModel.model;

        //var obj1 = {
        //    id: "JFK",
        //    lowerCasedName: "john f kennedy intl (jfk)",
        //    name: "John F Kennedy Intl (JFK)"
        //};

       // var array1 = new Array();
        //array1.push(obj1);
        //$scope.model.origin = array1;

        var self = this, airports,
            stateName = $state.$current.self.name,
            ruleGridColumns = [{
                name: 'ruleTitle',
                displayName: 'Title',
                cellTemplate: '<md-button aria-label="title" class="md-primary md-button md-default-theme" ng-click="grid.appScope.ruleIdClick(row)">{{COL_FIELD}}</md-button>'
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
                populateAirports();
                fetchMethods[stateName]();
            },
            flightDirections = [
                {label: 'Inbound', value: 'I'},
                {label: 'Outbound', value: 'O'},
                {label: 'Any', value: 'A'}
            ];

        self.querySearch = querySearch;
        $http.get('data/airports.json')
            .then(function (allAirports) {
                airports = allAirports.data;
                self.allAirports = allAirports.data.map(function (contact) {
                    contact.lowerCasedName = contact.name.toLowerCase();
                    return contact;
                });
                self.filterSelected = true;
                $scope.filterSelected = true;
            });
        $scope.flightDirections = flightDirections;

        $injector.invoke(jqueryQueryBuilderWidget, this, {$scope: $scope});
//        $injector.invoke(queryBuilderFactory, this, {$scope: $scope});
        $scope.stateName = $state.$current.self.name;
        $scope.ruleIdClick = function (row) {
            $scope.getRuleObject(row.entity.ruleId);
        };

        $scope.getRuleObject = function (ruleID) {
            jqueryQueryBuilderService.loadRuleById('rule', ruleID).then(function (myData) {
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
            paginationCurrentPage: $scope.model.pageNumber,
            useExternalPagination: true,
            useExternalSorting: true,
            useExternalFiltering: true,
            enableHorizontalScrollbar: 0,
            enableVerticalScrollbar: 0,
            enableColumnMenus: false,
            multiSelect: false,
            enableExpandableRowHeader: false,
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

        if (stateName === 'queryPassengers') {
            $scope.passengerGrid.columnDefs = [
                {
                    field: 'onRuleHitList',
                    name: 'onRuleHitList',
                    displayName: 'H',
                    width: 50,
                    cellClass: "rule-hit",
                    sort: {
                        direction: uiGridConstants.DESC,
                        priority: 0
                    },
                    cellTemplate: '<md-button aria-label="hits" ng-click="grid.api.expandable.toggleRowExpansion(row.entity)" disabled="{{row.entity.onRuleHitList|ruleHitButton}}"><i class="{{row.entity.onRuleHitList|ruleHitIcon}}"></i></md-button>'
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
                {
                    field: 'passengerType',
                    name: 'passengerType',
                    displayName: 'Type',
                    width: 50},
                {
                    field: 'lastName',
                    name: 'lastName',
                    displayName: 'Last Name',
                    cellTemplate: '<md-button aria-label="type" href="#/paxdetail/{{row.entity.id}}/{{row.entity.flightId}}" title="Launch Flight Passengers in new window" target="pax.detail.{{row.entity.id}}.{{row.entity.flightId}}" class="md-primary md-button md-default-theme" >{{COL_FIELD}}</md-button>'
                },
                {
                    field: 'firstName',
                    name: 'firstName',
                    displayName: 'First Name'},
                {
                    field: 'middleName',
                    name: 'middleName',
                    displayName: 'Middle'
                },
                {
                    field: 'flightNumber',
                    name: 'flightNumber',
                    displayName: 'Flight',
                    cellTemplate: '<div>{{row.entity.carrier}}{{COL_FIELD}}</div>'
                },
                {
                    field: 'flightOrigin',
                    name: 'flightOrigin',
                    displayName: 'Origin'
                },
                {
                    field: 'flightDestination',
                    name: 'flightDestination',
                    displayName: 'Destination'
                },
                {
                    field: 'etaLocalTZ',
                    name: 'etaLocalTZ',
                    sort: {
                        direction: uiGridConstants.DESC,
                        priority: 2
                    },
                    displayName: 'ETA'
                },
                {
                    field: 'etdLocalTZ',
                    name: 'etdLocalTZ',
                    displayName: 'ETD'
                },
                {
                    field: 'gender',
                    name: 'gender',
                    displayName: 'G',
                    width: 50},
                {
                    name: 'dob',
                    displayName: 'DOB',
                    cellFilter: 'date'
                },
                {
                    name: 'citizenshipCountry',
                    displayName: 'CTZ',
                    width: 75
                }
            ];
        } else {
            $scope.passengerGrid.columnDefs = [
                {
                    name: 'onRuleHitList', displayName: 'H', width: 50,
                    cellClass: "rule-hit",
                    sort: {
                        direction: uiGridConstants.DESC,
                        priority: 0
                    },
                    cellTemplate: '<md-button aria-label="hits" ng-click="grid.api.expandable.toggleRowExpansion(row.entity)" disabled="{{row.entity.onRuleHitList|ruleHitButton}}"><i class="{{row.entity.onRuleHitList|ruleHitIcon}}"></i></md-button>'
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
                    cellTemplate: '<md-button aria-label="type" href="#/paxdetail/{{row.entity.id}}/{{row.entity.flightId}}" title="Launch Flight Passengers in new window" target="pax.detail" class="md-primary md-button md-default-theme" >{{COL_FIELD}}</md-button>'
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
        }

        var populateAirports = function(){

            var originAirports = new Array();
            var destinationAirports = new Array();

            angular.forEach($scope.model.origin,function(value,index){
                originAirports.push(value.id);
            })

            angular.forEach($scope.model.dest,function(value,index){
                destinationAirports.push(value.id);
            })

            $scope.model.originAirports = originAirports;
            $scope.model.destinationAirports = destinationAirports;
        };

        var mapAirports = function(){

            var originAirports = new Array();
            var destinationAirports = new Array();
            var airport = { id: "" };


            if($scope.model.origin ) {
                if ($scope.model.origin instanceof Array ) {
                } else {
                originAirports.push({id: $scope.model.origin});
                $scope.model.origin = originAirports
            }
            };
            if($scope.model.dest ) {
                if ($scope.model.dest instanceof Array ) {
                } else {
                    destinationAirports.push({id: $scope.model.dest});
                    $scope.model.dest = destinationAirports
                }
            };
            
        };

        //var loadUserFilters = function(){
        //
        //    var userModel;
        //    var userPrefs =  userService
        //        .getUserData(  )                     // Request #1
        //        .then( function( user ) {
        //            if(user.data.filter!=null) {
        //                if (user.data.filter.flightDirection)
        //                    userModel.direction = user.data.filter.flightDirection;
        //                if (user.data.filter.etaStart) {
        //                    flightsModel.starteeDate = new Date();
        //                    flightsModel.etaStart.setDate(today.getDate() + user.data.filter.etaStart);
        //                }
        //                if (user.data.filter.etaEnd) {
        //                    flightsModel.endDate = new Date();
        //                    flightsModel.etaEnd.setDate(today.getDate() + user.data.filter.etaEnd);
        //                }// Response Handler #1
        //                if (user.data.filter.originAirports != null)
        //                    flightsModel.origins = user.data.filter.originAirports;
        //                if (user.data.filter.destinationAirports != null)
        //                    flightsModel.destinations = user.data.filter.destinationAirports;
        //            }
        //            return flightsModel;
        //        });
        //
        //};
        //var populateAirports = function(){
        //
        //    var originAirports = new Array();
        //    var destinationAirports = new Array();
        //    var airport = { id: "" };
        //
        //    angular.forEach($scope.model.origin,function(value,index){
        //        //originAirports.push(value.id);
        //        originAirports.push({ id: value });
        //    })
        //
        //    angular.forEach($scope.model.dest,function(value,index){
        //        //destinationAirports.push(value.id);
        //        destinationAirports.push({  id: value });
        //    })
        //
        //    $scope.model.originAirports = originAirports;
        //    $scope.model.destinationAirports = destinationAirports;
        //
        //
        //    //var originAirports = new Array();
        //    //var destinationAirports = new Array();
        //    //var airport = { id: "" };
        //    //
        //    //angular.forEach(flightsModel.origins,function(value,index){
        //    //    originAirports.push({ id: value });
        //    //});
        //    //
        //    //angular.forEach(flightsModel.destinations,function(value,index){
        //    //    destinationAirports.push({  id: value });
        //    //});
        //    //$scope.model.origin= originAirports;
        //    //$scope.model.dest = destinationAirports;
        //
        //
        //};


        $scope.filter = function () {

            resolvePage();
        };

        $scope.reset = function () {
            paxModel.reset();
            resolvePage();
        };

        $scope.getTableHeight = function () {
            return gridService.calculateGridHeight($scope.passengerGrid.data.length);
        };

        getPage();
        mapAirports();
    });
}());
