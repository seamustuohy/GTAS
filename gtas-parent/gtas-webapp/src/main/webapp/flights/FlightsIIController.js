app.controller('FlightsIIController', function ($scope, $rootScope, $injector, GridControl, $filter, $q, flightService, paxService, jQueryBuilderFactory, riskCriteriaService, $interval, $timeout) {
    'use strict';
    var self = this,
        data = [],
        columns = {
            FLIGHTS: [
                {"name": "totalPax", "displayName": "P"},
                {"name": "ruleHits", "displayName": "H"},
                {"name": "watchlistHits", "displayName": "L"},
                {"name": "carrier", "displayName": "Carrier"}, //carrier or carrierCode...?
                {"name": "flightNumber", "displayName": "Flight #"},
                {"name": "origin", "displayName": "Origin"},
                {"name": "originCountry", "displayName": "Country"},
                {"name": "departureDt", "displayName": "ETD"},
                {"name": "destination", "displayName": "Destination"},
                {"name": "destinationCountry", "displayName": "Country"},
                {"name": "arrivalDt", "displayName": "ETA"}
            ]
        };

    $injector.invoke(GridControl, this, {$scope: $scope });

    $scope.hitDetailDisplay = '';
    $scope.ruleHitsRendered = false; // flag to render rule hits only once

    $injector.invoke(jQueryBuilderFactory, self, {$scope: $scope});
    $scope.loading = true;

    $scope.gridOpts.columnDefs = columns.FLIGHTS;
    $scope.gridOpts.exporterCsvFilename = 'MySavedQueries.csv';
    $scope.gridOpts.exporterPdfHeader = { text: "Saved Queries", style: 'headerStyle' };

    flightService.getFlights().then(function (myData) {
        $scope.gridOpts.data = myData;
        $scope.loading = false;
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
