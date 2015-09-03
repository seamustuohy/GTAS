app.controller('FlightsIIController', function ($scope, $rootScope, $injector, modal, GridControl, uiGridConstants, $filter, $q, flightService, paxService, jQueryBuilderFactory, riskCriteriaService, $interval, $timeout) {
    'use strict';
    $injector.invoke(GridControl, this, {$scope: $scope });
    $injector.invoke(modal, this, {$scope: $scope });

    var myModal = new modal(),
        self = this,
        columns = {
            FLIGHTS: [
                {"name": "totalPax", "displayName": "P"},
                {
                    "name": "ruleHits",
                    "displayName": "H",
                    "sort": {
                        direction: uiGridConstants.DESC,
                        priority: 0
                    }
                },
                {
                    "name": "watchlistHits",
                    "displayName": "L",
                    "sort": {
                        direction: uiGridConstants.DESC,
                        priority: 1
                    }
                },
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
        expandableRowTemplate: '<div ui-grid="row.entity.subGridOptions" style="height:150px;"></div>',
        expandableRowHeight: 150,
        //subGridVariable will be available in subGrid scope
        expandableRowScope: {
            subGridVariable: 'subGridScopeVariable'
        },
        onRegisterApi: function (gridApi) {
            $scope.gridApi = gridApi;
            gridApi.expandable.on.rowExpandedStateChanged($scope, function (row) {
                if (row.isExpanded) {
                    row.entity.subGridOptions = {
                        columnDefs: [
                            { name: 'ruleId'},
                            { name: 'ruleTitle'},
                            { name: 'ruleConditions'}
                        ]//,
//                        data: [{name: 'Tony', gender: 'M', company: 'UNISYS'}, {name: 'Claudia', gender: 'F', company: 'HOMEMAKER'}]
                    };
                    paxService.getRuleHits(row.entity.paxId).then(function (myData) {
                        var j, data = [], jeen;
                        for (j = 0; j < myData.length; j++) {
                            jeen = myData[j].ruleConditions;
                            data.push({
                                ruleId: myData[j].ruleId,
                                ruleTitle: myData[j].ruleTitle,
                                ruleConditions: jeen.substring(0, (jeen.length - 3))
                            });

                            row.entity.subGridOptions.data = data;
                        }
                    });
                }
            });
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
                    var i;

                    $rootScope.selectedFlightNumber = row.entity.flightNumber;
                    title = 'Passengers on Flight ' + $rootScope.selectedFlightNumber;
                    $rootScope.gridOptions.exporterCsvFilename = title  + '.csv';
                    $rootScope.gridOptions.exporterPdfHeader = { text: title, style: 'headerStyle' };
                    for (i = 0; i < myData.length; i++){
                        myData[i].subGridOptions = {
                            columnDefs: [ {name:"Id", field:"id"},{name:"Name", field:"name"} ],
                            data: [{id: 1, name: 'Tony'}, {id: 2, name: 'Tony'}, {id: 3, name: 'Tonio'}, {id: 4, name: 'Toni'}, {id: 5, name: 'Tone'}]
                        };
                    }
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

    // Rule UI Modal Section

    $scope.viewRuleByID = function (ruleID) {
        paxService.broadcastRuleID(ruleID);
    }; // END OF viewRuleByID Function

    $rootScope.$on('ruleIDBroadcast', function (event, data) {
        $scope.getRuleObject(data);
    });

    $scope.buildAfterEntitiesLoaded({deleteEntity: 'HITS'});

    $scope.getRuleObject = function (ruleID) {
        riskCriteriaService.loadRuleById(ruleID).then(function (myData) {
            $scope.$builder.queryBuilder('readOnlyRules', myData.details);
            $scope.hitDetailDisplay = myData.summary.title;
            document.getElementById("QBModal").style.display = "block";

            $scope.closeDialog = function () {
                document.getElementById("QBModal").style.display = "none";
            };
        });
    };
});
