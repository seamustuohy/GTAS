app.controller('FlightsIIController', function ($scope, $filter, $q, flightService, paxService, $rootScope, $injector, jQueryBuilderFactory, riskCriteriaService, $interval, $timeout) {
    'use strict';
    var self = this,
        data = [],
        paginationPageSize = 10,
        pdfFormatter = function (docDefinition) {
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

    $scope.hitDetailDisplay = '';
    $scope.ruleHitsRendered = false; // flag to render rule hits only once

    $injector.invoke(jQueryBuilderFactory, self, {$scope: $scope});
    $scope.loading = true;

    $scope.gridOpts = {
        columnDefs: columns.FLIGHTS,
        paginationPageSize: paginationPageSize,
        paginationPageSizes: [],
        enableFiltering: true,
        enableCellEditOnFocus: false,
        showGridFooter: true,
        multiSelect: false,
        enableGridMenu: true,
        enableSelectAll: false,
        exporterCsvFilename: 'MySavedQueries.csv',
        exporterPdfDefaultStyle: {fontSize: 9},
        exporterPdfTableStyle: {margin: [10, 10, 10, 10]},
        exporterPdfTableHeaderStyle: {
            fontSize: 10,
            bold: true,
            italics: true
        },
        exporterPdfHeader: { text: "My Saved Queries", style: 'headerStyle' },
        exporterPdfFooter: function (currentPage, pageCount) {
            return { text: pageOfPages(currentPage, pageCount), style: 'footerStyle' };
        },
        exporterPdfCustomFormatter: pdfFormatter,
        exporterPdfOrientation: 'landscape',
        exporterPdfPageSize: 'LETTER',
        exporterPdfMaxGridWidth: 600,
        exporterCsvLinkElement: angular.element(document.querySelectorAll(".custom-csv-link-location"))
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
