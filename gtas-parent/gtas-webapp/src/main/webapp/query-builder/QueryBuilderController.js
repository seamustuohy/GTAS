app.controller('QueryBuilderController', function ($scope, $injector, QueryBuilderCtrl, $filter, $q, queryBuilderService, queryService, $timeout, $interval) {
    'use strict';
    $injector.invoke(QueryBuilderCtrl, this, {$scope: $scope });
    var paginationPageSize = 10,
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
            PASSENGER: [
                { "name": "ruleHit", "displayName": "H", width: 50 },
                { "name": "onWatchList", "displayName": "W", width: 50 },
                { "name": "lastName", "displayName": "LastName", width: "*" },
                { "name": "firstName", "displayName": "FirstName", width: "*" },
                { "name": "passengerType", "displayName": "Type", width: 25 },
                { "name": "gender", "displayName": "GEN", width: 25 },
                { "name": "dob", "displayName": "DOB", width: 125 },
                { "name": "citizenship", "displayName": "CIT", width: 50 },
                { "name": "documentNumber", "displayName": "Doc #", width: 125 },
                { "name": "documentType", "displayName": "T", width: 25 },
                { "name": "documentIssuanceCountry", "displayName": "Issuer", width: 75 },
                { "name": "carrierCode", "displayName": "Carrier", width: "40" },
                { "name": "flightNumber", "displayName": "Flight #", width: 80 },
                { "name": "origin", "displayName": "Origin", width: "50" },
                { "name": "destination", "displayName": "Dest", width: "50" },
                { "name": "departureDt", "displayName": "ETD", width: 170 },
                { "name": "arrivalDt", "displayName": "ETA", width: 170 },
                { "name": "seat", "displayName": "Seat", width: 50 }
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
            ],
            QUERIES: [{
                name: "title",
                enableCellEdit: false,
                enableColumnMenu: false
            }, {
                name: "description",
                enableCellEdit: false,
                enableColumnMenu: false
            }]
        },
        pageOfPages = function (currentPage, pageCount) {
            return moment().format('YYYY-MM-DD') + (pageCount === 1 ? '' : '\t' + currentPage.toString() + ' of ' + pageCount.toString());
        };

    $scope.gridOpts = {
        columnDefs: columns.QUERIES,
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

    queryBuilderService.getList($scope.authorId).then(function (myData) {
        var data = [];
        if (myData.result === undefined || !Array.isArray(myData.result)) {
            $scope.saving = false;
            return;
        }

        myData.result.forEach(function (obj) {
            data.push(obj);
        });
        $scope.gridOpts.data = data;
    });

    $scope.hideGrid = true;

    $scope.loadRule = function () {
        var obj = $scope.gridOpts.data[$scope.selectedIndex];
        $scope.hideGrid = true;
        $scope.ruleId = obj.id;
        $scope.loadSummary({title: obj.title, description: obj.description });
        $scope.$builder.queryBuilder('loadRules', obj.query);
    };

    $scope.buildAfterEntitiesLoaded();

    $scope.gridOpts.onRegisterApi = function (gridApi) {
        $scope.gridApi = gridApi;
        gridApi.selection.on.rowSelectionChanged($scope, function (row) {
            if (row.isSelected) {
                $scope.selectedIndex = $scope.gridOpts.data.indexOf(row.entity);
                $scope.loadRule();
            } else {
                $scope.newRule();
                $scope.gridApi.selection.clearSelectedRows();
            }
        });
    };

    $scope.delete = function () {
        if (!$scope.ruleId) {
            $scope.alertError('No rule loaded to delete');
            return;
        }

        if (!$scope.authorId) {
            $scope.alertError('No user authenticated');
            return;
        }

        var selectedRowEntities = $scope.gridApi.selection.getSelectedRows();

        angular.forEach(selectedRowEntities, function (rowEntity) {
            var rowIndexToDelete = $scope.gridOpts.data.indexOf(rowEntity);

            // create a promise to reject errors from server side.
            var rowDeferred = $q.defer();

            console.log('Selected row: ' + rowIndexToDelete + ' to delete.');
            var deferred = queryBuilderService.deleteQuery($scope.ruleId, $scope.authorId);

            deferred.$promise.then(function (response) {
                    // success callback
                    var newLength = $scope.gridOpts.data.splice(rowIndexToDelete, 1);
                    rowDeferred.resolve(newLength);
                },
                function (error) {
                    // will fail because URL is not real, but will resolve anyway for purposes of this test.

                    // this is what I expect will remove the row from the ui-grid in the UI.
                    var newLength = $scope.gridOpts.data.splice(rowIndexToDelete, 1);
                    rowDeferred.resolve(newLength);
                    //rowDeferred.reject(error);
                });
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
            queryBuilderService.getList($scope.authorId).then(function (myData) {
                var data = [];
                if (myData.result === undefined || !Array.isArray(myData.result)) {
                    $scope.saving = false;
                    return;
                }

                myData.result.forEach(function (obj) {
                    data.push(obj);
                });
                $scope.gridOpts.data = data;
                $interval(function () {
                    var page;
                    if (!$scope.selectedIndex) {
                        page = $scope.gridApi.pagination.getTotalPages();
                        $scope.selectedIndex = $scope.gridOpts.data.length - 1;
                        $scope.gridApi.pagination.seek(page);
                    }
                    $scope.gridApi.selection.clearSelectedRows();
                    $scope.gridApi.selection.selectRow($scope.gridOpts.data[$scope.selectedIndex]);
                    $scope.saving = false;
                }, 0, 1);
            });
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
        exporterCsvFilename: 'queryResults.csv',
        exporterPdfDefaultStyle: {fontSize: 8},
        exporterPdfTableStyle: {margin: [10, 10, 10, 10]},
        exporterPdfTableHeaderStyle: {
            fontSize: 8,
            bold: true,
            italics: true
        },
        exporterPdfHeader: { text: "Query [NAME]", style: 'headerStyle' },
        exporterPdfFooter: function (currentPage, pageCount) {
            return { text: pageOfPages(currentPage, pageCount), style: 'footerStyle' };
        },
        exporterPdfCustomFormatter: pdfFormatter,
        exporterPdfOrientation: 'landscape',
        exporterPdfPageSize: 'LETTER',
        exporterPdfMaxGridWidth: 650,
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
            $scope.resultsGrid.exporterPdfHeader.text = $scope.title;
            $scope.resultsGrid.data = myData.result;
        });

        $timeout(function () {
            $scope.hideGrid = false;
        }, 1000);
    };
});
