app.controller('QueryBuilderController', function ($scope, $injector, QueryBuilderCtrl, $filter, $q, ngTableParams, queryBuilderService, queryService, $timeout, $window) {
    'use strict';
    $injector.invoke(QueryBuilderCtrl, this, {$scope: $scope });
    var data = [],
        paginationPageSize = 10,
        columns = {
            PASSENGER: [
                { "name": "ruleHit", "displayName": "H" },
                { "name": "onWatchList", "displayName": "W" },
                { "name": "lastName", "displayName": "LastName" },
                { "name": "firstName", "displayName": "FirstName" },
                { "name": "passengerType", "displayName": "Type" },
                { "name": "gender", "displayName": "Gender" },
                { "name": "dob", "displayName": "DOB" },
                { "name": "citizenship", "displayName": "CIT" },
                { "name": "documentNumber", "displayName": "Document #" },
                { "name": "documentType", "displayName": "T" },
                { "name": "documentIssuanceCountry", "displayName": "Issuance Country" },
                { "name": "carrierCode", "displayName": "Carrier" },
                { "name": "flightNumber", "displayName": "Flight #" },
                { "name": "origin", "displayName": "Origin" },
                { "name": "destination", "displayName": "Destination" },
                { "name": "departureDt", "displayName": "ETD" },
                { "name": "arrivalDt", "displayName": "ETA" },
                { "name": "seat", "displayName": "Seat" }
            ],
            FLIGHT: [
                { "name": "carrierCode", "displayName": "Carrier" },
                { "name": "flightNumber", "displayName": "Flight #" },
                { "name": "origin", "displayName": "Origin" },
                { "name": "originCountry", "displayName": "Country" },
                { "name": "departureDt", "displayName": "ETD" },
                { "name": "destination", "displayName": "Destination" },
                { "name": "destinationCountry", "displayName": "Country" },
                { "name": "arrivalDt", "displayName": "ETA" }
            ]
        };

    $scope.hideGrid = true;

    $scope.loadRule = function () {
        $scope.hideGrid = true;
        var obj = this.$data[this.$index];
        $scope.ruleId = obj.id;
        $scope.loadSummary({title: obj.title, description: obj.description });
        $scope.$builder.queryBuilder('loadRules', obj.query);
    };

    $scope.tableParams = new ngTableParams({
        page: 1,            // show first page
        count: 10,          // count per page
        filter: {},
        sorting: {
            hits: 'desc',
            destinationDateTimeSort: 'asc' //, 'number': 'asc'     // initial sorting
        }
    }, {
        counts: [],         // disable / hide page row count toggle
        total: data.length, // length of data
        getData: function ($defer, params) {
            queryBuilderService.getList($scope.authorId).then(function (myData) {
                var filteredData, orderedData;
                data = [];

                if (myData.result === undefined || !Array.isArray(myData.result)) {
                    return;
                }

                myData.result.forEach(function (obj) {
                    data.push(obj);
                });

                filteredData = params.filter() ? $filter('filter')(data, params.filter()) : data;
                orderedData = params.sorting() ? $filter('orderBy')(filteredData, params.orderBy()) : data;

                params.total(orderedData.length); // set total for recalc pagination
                $defer.resolve(orderedData.slice((params.page() - 1) * params.count(), params.page() * params.count()));
            });
        }
    });

    $scope.buildAfterEntitiesLoaded();

    $scope.delete = function () {
        if (!$scope.ruleId) {
            $scope.alertError('No rule loaded to delete');
            return;
        }

        if (!$scope.authorId) {
            $scope.alertError('No user authenticated');
            return;
        }
        queryBuilderService.deleteQuery($scope.ruleId, $scope.authorId).then(function (myData) {
            $scope.newRule();
            $scope.tableParams.reload();
        });
    };

    $scope.summaryDefaults = {description: null, title: ''};
    $scope.ruleId = null;
    $scope.saving = false;

    $scope.save = function () {
        var queryObject, query;
        if ($scope.saving) {
            return;
        }

        $scope.saving = true;
        $scope.title = $scope.title.trim();
        if (!$scope.title.length) {
            $scope.alertError('title can not be blank!');
            $scope.saving = false;
            return;
        }
        query = $scope.$builder.queryBuilder('getDrools');

        if (query === false) {
            $scope.saving = false;
            return;
        }

        queryObject = {
            id: $scope.ruleId,
            title: $scope.title,
            description: $scope.description || null,
            userId: $scope.authorId,
            query: query
        };

        queryBuilderService.saveQuery(queryObject).then(function (myData) {
            if (myData.status === 'FAILURE') {
                $scope.alertError(myData.message);
                $scope.saving = false;
                return;
            }
            $scope.tableParams.reload();
            $scope.showPencil(myData.result[0].id);
        });
    };

    $scope.serviceURLs = {
        FLIGHT: '/gtas/query/queryFlights/',
        PASSENGER: '/gtas/query/queryPassengers/'
    };

    $scope.viewType = 'FLIGHT';

    $scope.resultsGrid = {
        paginationPageSize: paginationPageSize,
        paginationPageSizes: [],
        enableFiltering: true,
        enableCellEditOnFocus: false,
        showGridFooter: true,
        multiSelect: false,
        enableGridMenu: true,
        enableSelectAll: false,
        exporterCsvFilename: 'myFile.csv',
        exporterPdfDefaultStyle: {fontSize: 9},
        exporterPdfTableStyle: {margin: [30, 30, 30, 30]},
        exporterPdfTableHeaderStyle: {fontSize: 10, bold: true, italics: true, color: 'red'},
        exporterPdfHeader: { text: "My Header", style: 'headerStyle' },
        exporterPdfFooter: function (currentPage, pageCount) {
            return { text: currentPage.toString() + ' of ' + pageCount.toString(), style: 'footerStyle' };
        },
        exporterPdfCustomFormatter: function (docDefinition) {
            docDefinition.styles.headerStyle = { fontSize: 22, bold: true };
            docDefinition.styles.footerStyle = { fontSize: 10, bold: true };
            return docDefinition;
        },
        exporterPdfOrientation: 'landscape',
        exporterPdfPageSize: 'LETTER',
        exporterPdfMaxGridWidth: 500,
        exporterCsvLinkElement: angular.element(document.querySelectorAll(".custom-csv-link-location"))
    };

    $scope.executeQuery = function () {
        var baseUrl = $scope.serviceURLs[$scope.viewType],
            qbData = $scope.$builder.queryBuilder('getDrools');

        if (qbData === false) {
            $scope.alertError('Can not execute / invalid query');
            return;
        }

        $scope.hideGrid = true;
        queryService.executeQuery(baseUrl, qbData).then(function (myData) {
            if (myData.result === undefined) {
                $scope.alertError('Error!');
                return;
            }
            $scope.resultsGrid.columnDefs = columns[$scope.viewType];
            $scope.resultsGrid.data = myData.result;
        });

        $timeout(function () {
            $scope.hideGrid = false;
        }, 1000);
    };
});
