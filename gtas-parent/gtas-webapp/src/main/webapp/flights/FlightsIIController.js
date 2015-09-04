app.controller('FlightsIIController', function ($scope, $rootScope, $injector, Modal, GridControl, uiGridConstants, $filter, $q, flightService, paxService, jQueryBuilderFactory, riskCriteriaService, $interval, $timeout) {
    'use strict';
    $injector.invoke(GridControl, this, {$scope: $scope });
    $injector.invoke(Modal, this, {$scope: $scope });

    var myModal = new Modal(),
        self = this,
        columns = {
            FLIGHTS: [{
                "name": "totalPax",
                "displayName": "P",
                "sort": {
                    direction: uiGridConstants.DESC,
                    priority: 2
                }
            }, {
                "name": "ruleHits",
                "displayName": "H",
                "sort": {
                    direction: uiGridConstants.DESC,
                    priority: 0
                }
            }, {
                "name": "watchlistHits",
                "displayName": "L",
                "sort": {
                    direction: uiGridConstants.DESC,
                    priority: 1
                }
            }, {
                "name": "carrier",
                "displayName": "Carrier"
            }, {
                "name": "flightNumber",
                "displayName": "Flight #"
            }, {
                "name": "origin",
                "displayName": "Origin"
            }, {
                "name": "originCountry",
                "displayName": "Country"
            }, {
                "name": "departureDt",
                "displayName": "ETD"
            }, {
                "name": "destination",
                "displayName": "Destination"
            }, {
                "name": "destinationCountry",
                "displayName": "Country"
            }, {
                "name": "arrivalDt",
                "displayName": "ETA"
            }],
            PASSENGERS: [{
                "name": "ruleHits",
                "displayName": "R",
                width: 50,
                "sort": {
                    direction: uiGridConstants.DESC,
                    priority: 0
                }
            }, {
                "name": "listHits",
                "displayName": "L",
                width: 50,
                "sort": {
                    direction: uiGridConstants.DESC,
                    priority: 1
                }
            }, {
                "name": "lastName",
                "displayName": "P",
                width: 150
            }, {
                "name": "firstName",
                "displayName": "First Name",
                width: 150
            }, {
                "name": "middleName",
                "displayName": "Middle Name"
            }, {
                "name": "passengerType",
                "displayName": "Type",
                width: 50
            }, {
                "name": "gender",
                "displayName": "Gender"
            }, {
                "name": "dob",
                "displayName": "DOB",
                width: 120
            }, {
                "name": "citizenshipCountry",
                "displayName": "CIT"
            }, {
                "name": "documents[0].documentNumber",
                "displayName": "Doc #"
            }, {
                "name": "documents[0].documentType",
                "displayName": "T"
            }, {
                "name": "documents[0].issuanceCountry",
                "displayName": "Issuer"
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
        data: [{
            "title": null,
            "firstName": null,
            "middleName": null,
            "lastName": null,
            "suffix": null,
            "gender": null,
            "citizenshipCountry": null,
            "residencyCountry": null,
            "passengerType": null,
            "dob": null,
            "embarkation": "IWA",
            "debarkation": "SEA",
            "embarkCountry": "RUS",
            "debarkCountry": "USA",
            "paxId": "5151",
            "seat": null,
            "flightId": null,
            "flightNumber": null,
            "carrier": null,
            "flightOrigin": null,
            "flightDestination": null,
            "flightETD": null,
            "flightETA": null,
            "ruleHits": 0,
            "listHits": 0,
            "passengers": null,
            "pnrVo": null,
            "documents": [{
                "documentType": "P",
                "documentNumber": "610252034",
                "expirationDate": "2018-04-09 04:00 AM",
                "issuanceDate": null,
                "issuanceCountry": "USA"
            }]
        }],
        expandableRowTemplate: '<div ui-grid="row.entity.subGridOptions" ui-grid-selection class="rule-details-grid"></div>',
        expandableRowHeight: 150,
        //subGridVariable will be available in subGrid scope
        expandableRowScope: {
            subGridVariable: 'subGridScopeVariable'
        },
        onRegisterApi: function (gridApi) {
            $scope.gridApi = gridApi;
            gridApi.expandable.on.rowExpandedStateChanged($scope, function (row) {
                if (row.isExpanded && !!row.entity.ruleHits) {
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
                            row.entity.subGridOptions = {
                                columnDefs: subGridHeaders,
                                data: data,
                                onRegisterApi: subGridRowSelected
                            };
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
