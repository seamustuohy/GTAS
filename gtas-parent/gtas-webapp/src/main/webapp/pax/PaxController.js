app.controller('PaxController', function ($scope, $rootScope, $injector, GridControl, jqueryQueryBuilderWidget, $filter,
                                          $q, paxService, sharedPaxData, $stateParams, $state,
                                          $timeout, $interval, uiGridConstants) {

    $scope.selectedFlight=$stateParams.flight;
    $scope.parent=$stateParams.parent;

    var self = this;
    $injector.invoke(jqueryQueryBuilderWidget, this, {$scope: $scope});
    $injector.invoke(GridControl, this, {$scope: $scope});

    $scope.isExpanded = true;
    $scope.paxHitList = [];
    $scope.list = sharedPaxData.list;
    $scope.add = sharedPaxData.add;
    $scope.getAll = sharedPaxData.getAll;

    $scope.getPaxSpecificList = function (index) {
        return $scope.list(index);
    };

    //--------Date functions-----------------------
    $scope.startDate = '';
    $scope.endDate = '';
    $scope.loading = true;

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

    $scope.passengerGrid = {
        saveFocus: true,
        saveScroll: true,
        saveGroupingExpandedStates: true,
        minRowsToShow: 12,
        enableFiltering: true,
        useExternalSorting: false,
        paginationPageSizes: [],
        paginationPageSize: 25,
        useExternalPagination: true,
        enableHorizontalScrollbar: true,

        keepLastSelected: true,
        //useExternalSorting: true,
        //pagingOptions: $scope.pagingOptions,
        filterOptions: $scope.filterOptions,
        sortInfo: $scope.sortOptions,

        columnDefs: [{
            "name": "ruleHits", "displayName": "H", width: 50, enableFiltering: false,
            "sort": {
                direction: uiGridConstants.DESC,
                priority: 0
            }
        },
            // {"name": "onWatchList", "displayName": "L", width: 50, enableFiltering: false},
            {
                "name": "lastName", "displayName": "Last Name", width: 175/*, enableFiltering: false*/,

                "sort": {
                    direction: uiGridConstants.DESC,
                    priority: 1
                }
            },
            {"name": "firstName", "displayName": "First Name", width: 150/*, enableFiltering: false*/},
            {"name": "middleName", "displayName": "Middle", width: 100/*, enableFiltering: false*/},
            {"name": "flightNumber", "displayName": "Flight", width: 90/*, enableFiltering: false*/},
            {"name": "flightETA", "displayName": "ETA", width: 175, enableFiltering: false},
            {"name": "flightETD", "displayName": "ETD", width: 175, enableFiltering: false},
            {"name": "passengerType", "displayName": "Type", width: 50, enableFiltering: false},
            {"name": "gender", "displayName": "G", width: 50, enableFiltering: false},
            {"name": "dob", "displayName": "DOB", field: 'dob', cellFilter: 'date', width: 175, enableFiltering: false},
            {"name": "citizenshipCountry", "displayName": "CTZ", width: 75/*, enableFiltering: false*/},
            {"name": "passengerType", "displayName": "T", width: 100, enableFiltering: false},
            {"name": "documentType", "displayName": "T", width: 50, enableFiltering: false},
            {"name": "carrier", "displayName": "Carrier", width: 50/*, enableFiltering: false*/},
            {"name": "seat", "displayName": "Seat", width: 75, enableFiltering: false}],

        enableGridMenu: true,
        enableSelectAll: false,
        exporterPdfDefaultStyle: {fontSize: 9},
        exporterPdfTableStyle: {margin: [10, 10, 10, 10]},
        exporterPdfTableHeaderStyle: {
            fontSize: 10,
            bold: true,
            italics: true
        },
        exporterPdfFooter: function (currentPage, pageCount) {
            return {
                text: pageOfPages(currentPage, pageCount),
                style: 'footerStyle'
            };
        },
        exporterPdfCustomFormatter: function (docDefinition) {
            docDefinition.pageMargins = [0, 40, 0, 40];
            docDefinition.styles.headerStyle = {
                fontSize: 22,
                bold: true,
                alignment: 'center',
                lineHeight: 1.5
            };
            docDefinition.styles.footerStyle = {
                fontSize: 10,
                italic: true,
                alignment: 'center'
            };
            return docDefinition;
        },
        exporterPdfOrientation: 'landscape',
        exporterPdfPageSize: 'LETTER',
        exporterPdfMaxGridWidth: 600,
        exporterCsvLinkElement: angular.element(document.querySelectorAll(".custom-csv-link-location")),

        exporterCsvFilename: 'Passengers.csv',
        exporterPdfHeader: {text: "Passengers", style: 'headerStyle'},

        onRegisterApi: function (gridApi) {
            $scope.gridApi = gridApi;

            gridApi.selection.on.rowSelectionChanged($scope, function (row) {
                var title;
                if (row.isSelected) {
                    $state.go('pax.detail', {id: row.entity.paxId, flightId: row.entity.flightId});
                }
            });


        }


    }; // END of Passenger Grid

    $scope.refreshListing = function () {
        $scope.loading = true;
        $scope.passengerGrid.data = [];

        var pax = {
            startDate: $filter('date')($scope.startDate, 'MM/dd/yyyy'),
            endDate: $filter('date')($scope.endDate, 'MM/dd/yyyy')
        };

        paxService.getAllPax(pax).then(function (myData) {
            $scope.loading = false;
            $scope.passengerGrid.totalItems = myData[0];
            $scope.passengerGrid.data = myData[1];
        });
    };

    $scope.refreshListing();

    //------- Pre-Refactor-------------------
    //Function to get Rule Hits per Passenger
    $scope.getRuleHits = function (passengerId) {
        var j, i;
        $scope.isExpanded = !$scope.isExpanded;
        if (!$scope.isExpanded) {
            paxService.getRuleHits(passengerId).then(function (myData) { // Begin

                $scope.paxHitList = [];
                $scope.tempPaxHitDetail = [];
                $scope.tempPaxHitList = [];
                var tempObj = [];

                for (j = 0; j < myData.length; j++) {
                    $scope.tempPaxHitList = myData[j].hitsDetailsList;
                    for (i = 0; i < $scope.tempPaxHitList.length; i++) {
                        tempObj = $scope.tempPaxHitList[i];
                        tempObj.ruleTitle = myData[j].ruleTitle;
                        tempObj.ruleConditions = tempObj.ruleConditions.substring(0, (tempObj.ruleConditions.length - 3));
                        $scope.tempPaxHitDetail[i] = tempObj;
                    }

                    $scope.paxHitList.push($scope.tempPaxHitDetail.pop());
                    $scope.tempPaxHitDetail = [];
                }
            }); // END of paxService getRuleHits
        }
    };
}); // END of PaxController

// Customs Filters

app.filter('capitalize', function () {
    return function (input, all) {
        return (!!input) ? input.replace(/([^\W_]+[^\s-]*) */g, function (txt) {
            return txt.charAt(0).toUpperCase() + txt.substr(1).toLowerCase();
        }) : '';
    };
});

app.filter('DobDateFormat', function myDateFormat($filter) {
    return function (text) {
        var tempdate = new Date(text.replace(/-/g, "/"));
        return $filter('date')(tempdate, "MMM-dd-yyyy");
    };
});


app.filter('orderObjectBy', function () {
    return function (items, field, reverse) {
        var filtered = [];
        angular.forEach(items, function (item) {
            filtered.push(item);
        });
        filtered.sort(function (a, b) {
            return (a[field] > b[field] ? 1 : -1);
        });
        if (reverse) filtered.reverse();
        return filtered;
    };
});

app.filter('flagImageFilter', function () {
    return function (hits) {
        if (hits === '0') {
            return 'padding: 1.5px; color: #007500;';
        }
        if (hits != '0') {
            return 'padding: 1.5px; color: #d9534f;';
        }
        return 'color: #007500;';
    };
});

//flagImageFilter

app.filter('watchListImageFilter', function () {
    return function (hits) {
        switch (hits) {
            case 1:
                return 'icon-user-check glyphiconWLPax col-sm-4';
            case 2:
                return 'icon-book glyphiconWLDocs col-sm-4';
            case 3:
                return 'icon-user-check glyphiconWLPaxDocs col-sm-4';
            default:
                return '';
        }
    };
});

app.filter('watchListImageInsertFilter', function () {
    return function (hits) {
        if (hits === 1) {
            return '';
        } // glyphiconWLPax
        if (hits === 2) {
            return '';
        } // glyphiconWLDocs
        if (hits === 3) {
            return 'icon-book';
        } // glyphiconWLPaxDocs
        return '';
    };
});

app.filter('watchListImageColorFilter', function () {
    return function (hits) {
        if (hits === 1) {
            return 'padding: 7.5px; color: #d9534f;';
        } // glyphiconWLPax
        if (hits === 2) {
            return 'padding: 7.5px; color: #d9534f;';
        } // glyphiconWLDocs
        if (hits === 3) {
            return 'padding: 7.5px; color: #d9534f;';
        } // glyphiconWLPaxDocs
        return '';
    };
});
