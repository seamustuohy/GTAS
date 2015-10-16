app.controller('QueryBuilderController', function ($scope, $rootScope, $injector, jqueryQueryBuilderWidget, queryBuilderFactory, $location, gridOptionsLookupService, jqueryQueryBuilderService) {
    'use strict';

    $scope.setData = function (myData) {
        var data = [];
        if (myData === undefined || !Array.isArray(myData)) {
            $scope.saving = false;
            return;
        }

        myData.forEach(function (obj) {
            data.push(obj);
        });
        $scope.qbGrid.data = data;
    };

    $injector.invoke(jqueryQueryBuilderWidget, this, {$scope: $scope });
    $injector.invoke(queryBuilderFactory, this, {$scope: $scope });

    jqueryQueryBuilderService.init('querybuilder');

//    $scope.resultsGrid = $.extend({}, $scope.qbGrid);
//    $scope.resultsGrid.enableColumnResizing = true;

    $scope.qbGrid = gridOptionsLookupService.getGridOptions('queries');
    $scope.qbGrid.columnDefs = gridOptionsLookupService.getLookupColumnDefs('queries');
    $scope.qbGrid.exporterCsvFilename = 'MySavedQueries.csv';
    $scope.qbGrid.exporterPdfHeader = { text: "My Saved Queries", style: 'headerStyle' };


    jqueryQueryBuilderService.getList().then(function (myData) {
        $scope.setData(myData.result);
    });

    $scope.hideGrid = true;

    $scope.loadRule = function () {
        var obj = $scope.qbGrid.data[$scope.selectedIndex];
        $scope.hideGrid = true;
        $scope.ruleId = obj.id;
        $scope.loadSummary({title: obj.title, description: obj.description });
        $scope.$builder.queryBuilder('loadRules', obj.query);
    };

    $scope.buildAfterEntitiesLoaded();

    $scope.loadRuleOnSelection = function (row) {
        $scope.selectedIndex = $scope.qbGrid.data.indexOf(row.entity);
        $scope.loadRule();
    };

    $scope.qbGrid.onRegisterApi = $scope.rowSelection;

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
            query: query
        };

        jqueryQueryBuilderService.save(queryObject).then($scope.updateQueryBuilderOnSave);
    };

    $scope.executeQuery = function (viewType) {
        var query = $scope.$builder.queryBuilder('getDrools');
        if (query === false) {
            $scope.alertError('Can not execute / invalid query');
            return;
        }
        // because new window using local storage vs state params
        localStorage['query'] = JSON.stringify(query);
        localStorage['qbTitle'] = $scope.title.length ? $scope.title.trim() : '';
    };
});
