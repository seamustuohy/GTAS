app.controller('QueryBuilderController', function ($scope, $rootScope, $injector, QueryBuilderCtrl, GridControl, $filter, $q, queryBuilderService, queryService, $timeout, $interval) {
    'use strict';
    var setData = function (myData) {
        var data = [];
        if (myData.result === undefined || !Array.isArray(myData.result)) {
            $scope.saving = false;
            return;
        }

        myData.result.forEach(function (obj) {
            data.push(obj);
        });
        $scope.gridOpts.data = data;
    };

    $injector.invoke(QueryBuilderCtrl, this, {$scope: $scope });
    $injector.invoke(GridControl, this, {$scope: $scope });

    $scope.resultsGrid = $.extend({}, $scope.gridOpts);

    $scope.gridOpts.columnDefs = $rootScope.columns.QUERIES;
    $scope.gridOpts.exporterCsvFilename = 'MySavedQueries.csv';
    $scope.gridOpts.exporterPdfHeader = { text: "My Saved Queries", style: 'headerStyle' };

    queryBuilderService.getList($scope.authorId).then(function (myData) {
        setData(myData);
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
                setData(myData);
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

    $scope.resultsGrid.exporterCsvFilename = 'queryResults.csv';
    $scope.resultsGrid.exporterPdfHeader = { text: "Query [NAME]", style: 'headerStyle' };

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
            $scope.resultsGrid.columnDefs = $rootScope.columns[$scope.viewType];
            $scope.resultsGrid.exporterPdfHeader.text = $scope.title;
            $scope.resultsGrid.data = myData.result;
        });

        $timeout(function () {
            $scope.hideGrid = false;
        }, 1000);
    };
});
