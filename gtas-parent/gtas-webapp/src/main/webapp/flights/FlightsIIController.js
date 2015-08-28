app.controller('FlightsIIController', function ($scope, $rootScope, $injector, modal, GridControl, $filter, $q, flightService, paxService, jQueryBuilderFactory, riskCriteriaService, $interval, $timeout) {
    'use strict';
    $injector.invoke(GridControl, this, {$scope: $scope });
    $injector.invoke(modal, this, {$scope: $scope });

    var myModal = new modal(),
        self = this,
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
            ],
            PASSENGERS: [
//                { "name": "title" },
                { "name": "ruleHits", "displayName": "R" },
                { "name": "listHits", "displayName": "L" },
                { "name": "lastName", "displayName": "P" },
                { "name": "firstName", "displayName": "First Name" },
                { "name": "middleName", "displayName": "Middle Name" },
//                { "name": "suffix", "displayName": "Suffix" },
                { "name": "passengerType", "displayName": "Type" },
                { "name": "gender", "displayName": "Gender" },
                { "name": "dob", "displayName": "DOB" },
                { "name": "citizenshipCountry", "displayName": "CIT" },
                { "name": "documents[0].documentNumber", "displayName": "Doc #" },
                { "name": "documents[0].documentType", "displayName": "T" },
                { "name": "documents[0].issuanceCountry", "displayName": "Issuer" }//,
//                { "name": "residencyCountry" },
//                { "name": "embarkation" },
//                { "name": "debarkation" },
//                { "name": "embarkCountry" },
//                { "name": "debarkCountry" },
//                { "name": "paxId" },
//                { "name": "passengers" }
            ]
        };

    $scope.hideGrid = true;

    $rootScope.gridOptions = $.extend({
        columnDefs: columns.PASSENGERS,
        onRegisterApi: function (gridApi) {
            $scope.gridApi = gridApi;

            // call resize every 200 ms for 2 s after modal finishes opening - usually only necessary on a bootstrap modal
            $interval( function() {
                $scope.gridApi.core.handleWindowResize();
            }, 10, 500);
        }
    }, $scope.gridOpts);

    $scope.hitDetailDisplay = '';
    $scope.ruleHitsRendered = false; // flag to render rule hits only once

    $injector.invoke(jQueryBuilderFactory, self, {$scope: $scope});
    $scope.loading = true;

    $scope.gridOpts.columnDefs = columns.FLIGHTS;
    $scope.gridOpts.exporterCsvFilename = 'MySavedQueries.csv';
    $scope.gridOpts.exporterPdfHeader = { text: "Saved Queries", style: 'headerStyle' };

    $scope.gridOpts.onRegisterApi = function (gridApi) {
        $scope.gridApi = gridApi;
        gridApi.selection.on.rowSelectionChanged($scope, function (row) {
            var title;
            if (row.isSelected) {
                $scope.selectedIndex = $scope.gridOpts.data.indexOf(row.entity);
                paxService.getPax(row.entity.flightId).then(function (myData) {
                    $rootScope.selectedFlightNumber = row.entity.flightNumber;
                    title = 'Passengers on Flight ' + $rootScope.selectedFlightNumber;
                    $rootScope.gridOptions.exporterCsvFilename = title  + '.csv';
                    $rootScope.gridOptions.exporterPdfHeader = { text: title, style: 'headerStyle' };
                    $rootScope.gridOptions.data = myData;
                    myModal.open();
                });
            }
        });
    };

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
