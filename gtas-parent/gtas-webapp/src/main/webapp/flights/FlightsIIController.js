app.controller('FlightsIIController', function ($scope, $rootScope, $injector, Modal, GridControl, uiGridConstants,
                                                $filter, $q, flightService, paxService, jQueryBuilderFactory,
                                                crudService, $interval, $timeout) {
    'use strict';
    $injector.invoke(GridControl, this, {$scope: $scope});
    $injector.invoke(Modal, this, {$scope: $scope});

    // UI ToDos:
    // Add flight information in header of Modal
    // Fix Flight Hit count can only be one per passenger that has either one or multiple watchlistHit or ruleHit
    // Selection on Row [no check mark]
    // # change new Rule to new Query on Query Screen
    // Require All Fields on watch list to save [add/update]
    // Follow up on Pull Request to fix column rendering on ui-grid
    // Submit Pull Request to allow disable/hide expandable on a row by row basis
    // #PING MIKE +++++++++++Fix Sort Estimated Inbound, Time of Arrival, Outbound, ETD
    // Color code hits greenlight red light on flights' passengers
    // Color code hits in red text

    //--------Date functions-----------------------

    $scope.dt;
    $scope.dt3;
    $scope.startDate = '';
    $scope.endDate = '';

    $scope.today = function () {
        $scope.dt = $filter('date')(new Date(), 'MM/dd/yyyy');
        $scope.dt3 = new Date();
        $scope.dt3.setDate((new Date()).getDate() + 3);
        $scope.dt3 = $filter('date')(($scope.dt3), 'MM/dd/yyyy');
        $scope.startDate = $scope.dt;
        $scope.endDate = $scope.dt3;
    };
    $scope.today();

    $scope.clear = function () {
        $scope.dt = null;
    };

    // Disable weekend selection
    $scope.disabled = function (date, mode) {
        return ( mode === 'day' && ( date.getDay() === 0 || date.getDay() === 6 ) );
    };

    $scope.toggleMin = function () {
        $scope.minDate = $scope.minDate ? null : new Date();
    };
    $scope.toggleMin();

    $scope.opendt = function ($event) {
        if (!$scope.status.openeddt) {
            $interval(function () {
                $('.ng-valid.dropdown-menu').eq(0).css({display: 'block'});
            }, 0, 1);
            $scope.status.openeddt = true;
        } else {
            $interval(function () {
                $('.ng-valid.dropdown-menu').eq(0).css({display: 'none'});
            }, 0, 1);
            $scope.status.openeddt = false;
        }
    };

    $scope.opendt3 = function ($event) {
        if (!$scope.status.openeddt3) {
            $interval(function () {
                $('.ng-valid.dropdown-menu').eq(1).css({display: 'block'});
            }, 0, 1);
            $scope.status.openeddt3 = true;
        } else {
            $interval(function () {
                $('.ng-valid.dropdown-menu').eq(1).css({display: 'none'});
            }, 0, 1);
            $scope.status.openeddt3 = false;
        }

    };

    $scope.dateOptions = {
        formatYear: 'yy',
        startingDay: 1
    };

    $scope.formats = ['MM/dd/yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate'];
    $scope.format = $scope.formats[0];

    $scope.status = {
        openeddt: false,
        openeddt3: false
    };

    var tomorrow = new Date();
    tomorrow.setDate(tomorrow.getDate() + 1);
    var afterTomorrow = new Date();
    afterTomorrow.setDate(tomorrow.getDate() + 2);
    $scope.events =
        [
            {
                date: tomorrow,
                status: 'full'
            },
            {
                date: afterTomorrow,
                status: 'partially'
            }
        ];

    $scope.getDayClass = function (date, mode) {
        if (mode === 'day') {
            var dayToCheck = new Date(date).setHours(0, 0, 0, 0);

            for (var i = 0; i < $scope.events.length; i++) {
                var currentDay = new Date($scope.events[i].date).setHours(0, 0, 0, 0);

                if (dayToCheck === currentDay) {
                    return $scope.events[i].status;
                }
            }
        }

        return '';
    };


    $('#start-date').datepicker().on('changeDate', function (ev) {
        var element = angular.element($('#start-date'));
        var controller = element.controller();
        var scope = element.scope();

        scope.$apply(function () {
            $scope.startDate = $filter('date')(ev.date, 'MM/dd/yyyy');

        });
    });

    $('#end-date').datepicker().on('changeDate', function (ev) {
        var element = angular.element($('#end-date'));
        var controller = element.controller();
        var scope = element.scope();

        scope.$apply(function () {
            $scope.endDate = $filter('date')(ev.date, 'MM/dd/yyyy');
        });
    });

    $timeout(function () {
        var $startDate = $('#start-date');
        $startDate.datepicker({
//          minDate: "today",
            startDate: "today",
            format: 'mm/dd/yyyy',
            autoClose: true
        });

        var $endDate = $('#end-date');
        $endDate.datepicker({
            startDate: "today",
//          minDate: startDate,
//            endDate: '+3d',
            format: 'mm/dd/yyyy',
            autoClose: true
        });

        $startDate.datepicker('setDate', $scope.dt);
        $startDate.datepicker('update');
        $startDate.val($scope.dt.toString());
        //
        $endDate.datepicker('setDate', $scope.dt3);
        $endDate.datepicker('update');
        $endDate.val($scope.dt3.toString());

    }, 100);
    //--------------------------------------------


    var myModal = new Modal(),
        self = this,
        columns = {
            "FLIGHTS": [{
                "name": "totalPax",
                "displayName": "P",
                "width": 50,
                "type": 'number'
            }, {
                "name": "ruleHits",
                "displayName": "H",
                cellTemplate:'<div><span class=\"glyphicon glyphicon-flag glyphiconFlightPax\" style=\"{{row.entity.ruleHits | flagImageFilter}}\"></span></div>',
                "sort": {
                    "direction": uiGridConstants.DESC,
                    "priority": 0
                },
                "width": 40,
                "type": 'number',
                enableFiltering: false
            }, {
                "name": "watchlistHits",
                "displayName": "L",
                cellTemplate:'<div><span class=\"{{row.entity.listHits | watchListImageFilter}}\" style=\"{{row.entity.listHits | watchListImageColorFilter}} \"><span class=\"{{row.entity.listHits | watchListImageInsertFilter}}\" style=\"{{row.entity.listHits | watchListImageColorFilter}} \"></span></div>',
                "width": 70,
                "type": 'number',
                enableFiltering: false
            }, {
                "name": "flightNumber",
                "displayName": "Flight #",
                "width": 80,
                "type": "string"
            }, {
                "name": "direction",
                "displayName": "Direction",
                "type": "string",
                "width": 50,
                "sort": {
                    "direction": uiGridConstants.ASC,
                    "priority": 0
                }
            }, {
                "name": "eta",
                "displayName": "ETA",
                "type": "date",
                "width": 170,
                "sort": {
                    "direction": uiGridConstants.DESC,
                    "priority": 1
                }
            }, {
                "name": "etd",
                "displayName": "ETD",
                "type": "date",
                "width": 170,
                "sort": {
                    "direction": uiGridConstants.DESC,
                    "priority": 2
                }
            }
                , {
                    "name": "origin",
                    "displayName": "Origin",
                    "type": "string"
                }, {
                    "name": "originCountry",
                    "displayName": "Country",
                    "type": "string"
                }, {
                    "name": "destination",
                    "displayName": "Destination",
                    "type": "string"
                }, {
                    "name": "destinationCountry",
                    "displayName": "Country",
                    "type": "string"
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
            },
            //    {
            //    "name": "listHits",
            //    "displayName": "L",
            //    "width": 50,
            //    "type": "string",
            //    "sort": {
            //        "direction": uiGridConstants.DESC,
            //        "priority": 1
            //    }
            //},
                {
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
        "width": 60,
        displayName: 'Id'
    }, {
        name: 'ruleTitle',
        displayName: 'Title'
    }, {
        name: 'ruleConditions',
        displayName: 'Conditions'
    }];

    crudService.init('riskcriteria');

    var entityMapper = {
        'PASSENGER': 'Passenger',
        'ADDRESS': 'Address',
        'DOCUMENT': 'Document'
    };

    var subGridRowSelected = function (subGridApi) {
        $scope.subGridApi = subGridApi;
        subGridApi.selection.on.rowSelectionChanged($scope, function (row) {
            if (row.isSelected) {
                crudService.loadRuleById(row.entity.ruleId).then(function (myData) {
                    //var details = myData.result.details;
                    //details.rules.forEach(function(rule) {
                    //    rule.entity = entityMapper[rule.entity];
                    //});
                 //   $scope.$builder.queryBuilder('loadRules', details);
                    $scope.$builder.queryBuilder('loadRules', myData.result.details);
                    $scope.hitDetailDisplay = myData.result.summary.title;
                    document.getElementById("QBModal").style.display = "block";

                    $scope.closeDialog = function () {
                        document.getElementById("QBModal").style.display = "none";
                    };

                });
            }
        });
    };

    //var subGridRowSelected = function (subGridApi) {
    //    $scope.subGridApi = subGridApi;
    //    subGridApi.selection.on.rowSelectionChanged($scope, function (row) {
    //        if (row.isSelected) {
    //            crudService.loadRuleById(row.entity.ruleId).then(function (myData) {
    //                $scope.$builder.queryBuilder('readOnlyRules', myData.result.details);
    //                $scope.hitDetailDisplay = myData.result.summary.title;
    //                document.getElementById("QBModal").style.display = "block";
    //
    //                $scope.closeDialog = function () {
    //                    document.getElementById("QBModal").style.display = "none";
    //                };
    //
    //            });
    //        }
    //    });
    //};

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

                            if (myData[j].ruleType != 'R') { //<REMOVE THIS AFTER ADDING WATCHLIST SUPPORT>
                            }else if(myData[j].ruleType === 'R'){
                            obj = {
                                ruleId: myData[j].ruleId,
                                ruleTitle: myData[j].ruleTitle
                            }

                            if (myData[j].hitsDetailsList !== undefined && Array.isArray(myData[j].hitsDetailsList) && myData[j].hitsDetailsList.length) {
                                obj.ruleConditions = myData[j].hitsDetailsList[0].ruleConditions.replace('$$$', '');
                            }

                            data.push(obj);
                        }// end of Else <REMOVE THIS AFTER ADDING WATCHLIST SUPPORT>
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
    $scope.gridOpts.exporterPdfHeader = {text: "Saved Queries", style: 'headerStyle'};

    $scope.gridOpts.onRegisterApi = function (gridApi) {
        $scope.gridApi = gridApi;
        gridApi.selection.on.rowSelectionChanged($scope, function (row) {
            if (row.isSelected) {
                $scope.selectedIndex = $scope.gridOpts.data.indexOf(row.entity);
                paxService.getPax(row.entity.flightId).then(function (myData) {
                    $rootScope.modalTitle = "Passengers List";
                    $rootScope.selectedRow = row.entity;
                    $rootScope.gridOptions.exporterCsvFilename = $rootScope.modalTitle + '.csv';
                    $rootScope.gridOptions.exporterPdfHeader = {text: $rootScope.modalTitle, style: 'headerStyle'};

                    myData.forEach(function (d) {
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

    var objParams = {
        startDate: $scope.startDate,
        endDate: $scope.endDate
    };

    flightService.getFlights(objParams).then(function (myData) {
        $scope.loading = false;
        $scope.gridOpts.totalItems = myData[0];
        $scope.gridOpts.data = myData[1];
    });


    $scope.refreshListing = function () {

        $scope.loading = true;
        //var startDate = moment($scope.startDate, $scope.formats, true);
        //var endDate = moment($scope.dt3, $scope.formats, true);

        var objParams = {
            startDate: $scope.startDate,
            endDate: $scope.endDate
        };

        flightService.getFlights(objParams).then(function (myData) {
            $scope.gridOpts.data = [];
            $scope.loading = false;
            $scope.gridOpts.totalItems = myData[0];
            $scope.gridOpts.data = myData[1];
        });

    }


    // Rule UI Modal Section

    $scope.viewRuleByID = function (ruleID) {
        paxService.broadcastRuleID(ruleID);
    }; // END OF viewRuleByID Function

    $rootScope.$on('ruleIDBroadcast', function (event, data) {
        $scope.getRuleObject(data);
    });

    $scope.buildAfterEntitiesLoaded({deleteEntity: 'HITS'});

    $scope.getRuleObject = function (ruleID) {
        crudService.loadRuleById(ruleID).then(function (myData) {
            $scope.$builder.queryBuilder('readOnlyRules', myData.result.details);
            $scope.hitDetailDisplay = myData.result.summary.title;
            document.getElementById("QBModal").style.display = "block";

            $scope.closeDialog = function () {
                document.getElementById("QBModal").style.display = "none";
            };
        });
    };


    $scope.$scope = $scope;

});
