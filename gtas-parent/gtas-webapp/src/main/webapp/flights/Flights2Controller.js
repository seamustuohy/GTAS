app.controller('FlightsController', function ($scope, $filter, $q, ngTableParams, flightService, paxService, $rootScope, $injector, jQueryBuilderFactory, riskCriteriaService, $timeout) {
    'use strict';
    var self = this,
        data = [];

    $scope.hitDetailDisplay = '';
    $scope.ruleHitsRendered = false; // flag to render rule hits only once

    $injector.invoke(jQueryBuilderFactory, self, {$scope: $scope});

    $scope.loading = true;
    $scope.tableParams = new ngTableParams(
        {
            noPager: true,
            page: 1,            // show first page
            count: 10,          // count per page
            filter: {},
            sorting: {
                ruleHits: 'desc'
                //,
                //destinationDateTimeSort: 'asc' //, 'number': 'asc'     // initial sorting
            }
        }, {
            //  total: data.length, // length of data
            getData: function ($defer, params) {
                flightService.getFlights().then(function (myData) {
                    data = myData;
                    //vm.tableParams.total(result.total);
                    // use build-in angular filter
                    var filteredData = params.filter() ?
                        $filter('filter')(data, params.filter()) :
                        data;
                    var orderedData = params.sorting() ?
                        $filter('orderBy')(filteredData, params.orderBy()) :
                        data;

                    params.total(orderedData.length); // set total for recalc pagination
                    $defer.resolve(orderedData.slice((params.page() - 1) * params.count(), params.page() * params.count()));


                    $scope.loading = false;

                });
            }
        });

    $scope.updatePax = function (flightId) {
        paxService.getPax(flightId);
    };

    // Rule UI Modal Section

    $scope.viewRuleByID = function (ruleID) {
        paxService.broadcastRuleID(ruleID);
    }; // END OF viewRuleByID Function

    $rootScope.$on('ruleIDBroadcast', function (event, data) {
        $scope.getRuleObject(data);
    });


    $scope.buildAfterEntitiesLoaded({deleteEntity: 'HITS'});

    // GET RULE OBJECT FUNCTION    
    $scope.getRuleObject = function (ruleID) {
        riskCriteriaService.loadRuleById(ruleID).then(function (myData) {
            $scope.$builder.queryBuilder('readOnlyRules', myData.details);
            //console.log(myData);

            $scope.hitDetailDisplay = myData.summary.title;
            document.getElementById("QBModal").style.display = "block";

            $scope.closeDialog = function () {
                document.getElementById("QBModal").style.display = "none";
            };
        });
    };
});
