app.controller('FlightsIIController', function ($scope, $rootScope, $injector, Modal, GridControl, uiGridConstants, $filter, $q, flightService, paxService, jQueryBuilderFactory, riskCriteriaService, $interval, $timeout) {
    'use strict';
    $injector.invoke(GridControl, this, {$scope: $scope });
    $injector.invoke(Modal, this, {$scope: $scope });

    // UI ToDos:
    // Add flight information in header of Modal
    // Fix Flight Hit count can only be one per passenger that has either one or multiple watchlistHit or ruleHit
    // Selection on Row [no check mark]
    // change new Rule to new Query on Query Screen
    // Require All Fields on watch list to save [add/update]
    // Follow up on Pull Request to fix column rendering on ui-grid
    // Submit Pull Request to allow disable/hide expandable on a row by row basis
    // Fix Sort Estimated Inbound, Time of Arrival, Outbound, ETD
    // Color code hits greenlight red light on flights' passengers
    // Color code hits in red text

    var myModal = new Modal(),
        self = this,
        columns = {
            "FLIGHTS": [{
                "name": "totalPax",
                "displayName": "P",
                "type": 'number'
            }, {
                "name": "ruleHits",
                "displayName": "H",
                "type": 'number'
            }, {
                "name": "watchlistHits",
                "displayName": "L",
                "type": 'number'
            }, {
                "name": "carrier",
                "displayName": "Carrier",
                "type": "string"
            }, {
                "name": "flightNumber",
                "displayName": "Flight #",
                "type": "string"
            }, {
                "name": "direction",
                "displayName": "Direction",
                "type": "string",
                "sort": {
                    "direction": uiGridConstants.ASC,
                    "priority": 0
                }
            }, {
                "name": "origin",
                "displayName": "Origin",
                "type": "string"
            }, {
                "name": "originCountry",
                "displayName": "Country",
                "type": "string"
            }, {
                "name": "departureDt",
                "displayName": "ETD",
                "type": "date",
                "sort": {
                    "direction": uiGridConstants.DESC,
                    "priority": 2
                }
            }, {
                "name": "destination",
                "displayName": "Destination",
                "type": "string"
            }, {
                "name": "destinationCountry",
                "displayName": "Country",
                "type": "string"
            }, {
                "name": "arrivalDt",
                "displayName": "ETA",
                "type": "date",
                "sort": {
                    "direction": uiGridConstants.DESC,
                    "priority": 1
                }
            }],
            "PASSENGERS": [{
                "name": "ruleHits",
                "displayName": "R",
                "width": 50,
                "type": "number",
                "sort": {
                    "direction": uiGridConstants.DESC,
                    "priority": 0
                }
            }, {
                "name": "listHits",
                "displayName": "L",
                "width": 50,
                "type": "string",
                "sort": {
                    "direction": uiGridConstants.DESC,
                    "priority": 1
                }
            }, {
                "name": "lastName",
                "displayName": "P",
                "width": 150,
                "type": "string"
            }, {
                "name": "firstName",
                "displayName": "First Name",
                "width": 150,
                "type": "string"
            }, {
                "name": "middleName",
                "displayName": "Middle Name",
                "type": "string"
            }, {
                "name": "passengerType",
                "displayName": "Type",
                "width": 50,
                "type": "string"
            }, {
                "name": "gender",
                "displayName": "Gender",
                "type": "string"
            }, {
                "name": "dob",
                "displayName": "DOB",
                "width": 120,
                "type": "date"
            }, {
                "name": "citizenshipCountry",
                "displayName": "CIT",
                "type": "string"
            }, {
                "name": "documents[0].documentNumber",
                "displayName": "Doc #",
                "type": "string"
            }, {
                "name": "documents[0].documentType",
                "displayName": "T",
                "type": "string"
            }, {
                "name": "documents[0].issuanceCountry",
                "displayName": "Issuer",
                "type": "string"
            }]
        };

    $scope.hideGrid = true;

    var subGridHeaders = [{
        name: 'ruleId',
        displayName: 'Id'
    }, {
        name: 'ruleTitle',
        displayName: 'Title'
    }, {
        name: 'ruleConditions',
        displayName: 'Conditions'
    }];

    var subGridRowSelected = function (subGridApi) {
        $scope.subGridApi = subGridApi;
        subGridApi.selection.on.rowSelectionChanged($scope, function (row) {
            if (row.isSelected) {
                riskCriteriaService.loadRuleById(row.entity.ruleId).then(function (myData) {
                    $scope.$builder.queryBuilder('readOnlyRules', myData.details);
                    $scope.hitDetailDisplay = myData.summary.title;
                    document.getElementById("QBModal").style.display = "block";

                    $scope.closeDialog = function () {
                        document.getElementById("QBModal").style.display = "none";
                    };

                });
            }
        });
    };

    $rootScope.gridOptions = $.extend({
        columnDefs: columns.PASSENGERS,
        expandableRowTemplate: '<div ui-grid="row.entity.subGridOptions" ui-grid-selection class="rule-details-grid"></div>',
        expandableRowHeight: 150,
        //subGridVariable will be available in subGrid scope
        expandableRowScope: {
            subGridVariable: 'subGridScopeVariable'
        },
        onRegisterApi: function (gridApi) {
            $scope.gridApi = gridApi;
            gridApi.expandable.on.rowExpandedStateChanged($scope, function (row) {
                if (row.isExpanded && (!!row.entity.ruleHits || !!row.entity.watchlistHits)) {
                    paxService.getRuleHits(row.entity.paxId).then(function (myData) {
                        var j, obj = {}, data = [];
                        for (j = 0; j < myData.length; j++) {
                            obj = {
                                ruleId: myData[j].ruleId,
                                ruleTitle: myData[j].ruleTitle
                            }

                            if (myData[j].hitsDetailsList !== undefined && Array.isArray(myData[j].hitsDetailsList) && myData[j].hitsDetailsList.length) {
                                obj.ruleConditions = myData[j].hitsDetailsList[0].ruleConditions.replace('$$$', '');
                            }

                            data.push(obj);
                        }
                        if (data.length) {
                            row.entity.subGridOptions.data = data;
                        } else {
//                            row.setExpanded(false);
//                            row.setSelected(false);
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
                    $rootScope.selectedFlightNumber = row.entity.flightNumber;
                    title = 'Passengers on Flight ' + $rootScope.selectedFlightNumber;
                    $rootScope.gridOptions.exporterCsvFilename = title  + '.csv';
                    $rootScope.gridOptions.exporterPdfHeader = { text: title, style: 'headerStyle' };

                    myData.forEach(function (d){
                        if (!!d.ruleHits || !!d.watchlistHits) {
                            d.subGridOptions = {
                                columnDefs: subGridHeaders,
                                onRegisterApi: subGridRowSelected
                            };
                        } else {
                        }
                    });

                    $rootScope.gridOptions.data = myData;
                    $interval(function () {
                        myModal.open();
                    }, 200, 1);
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
