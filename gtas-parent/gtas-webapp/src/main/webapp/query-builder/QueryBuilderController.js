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
        $scope.queriesGrid.data = data;
    };

    $injector.invoke(jqueryQueryBuilderWidget, this, {$scope: $scope });
    $injector.invoke(queryBuilderFactory, this, {$scope: $scope });

    jqueryQueryBuilderService.init('querybuilder');

//    $scope.resultsGrid = $.extend({}, $scope.queriesGrid);
//    $scope.resultsGrid.enableColumnResizing = true;

    $scope.queriesGrid = gridOptionsLookupService.getGridOptions('queries');
    $scope.queriesGrid.columnDefs = gridOptionsLookupService.getLookupColumnDefs('queries');
    $scope.queriesGrid.exporterCsvFilename = 'MySavedQueries.csv';
    $scope.queriesGrid.exporterPdfHeader = { text: "My Saved Queries", style: 'headerStyle' };


    jqueryQueryBuilderService.getList().then(function (myData) {
        $scope.setData(myData.result);
    });

    $scope.hideGrid = true;

    $scope.loadRule = function () {
        var obj = $scope.queriesGrid.data[$scope.selectedIndex];
        $scope.hideGrid = true;
        $scope.ruleId = obj.id;
        $scope.loadSummary({title: obj.title, description: obj.description });
        $scope.$builder.queryBuilder('loadRules', obj.query);
    };

    $scope.buildAfterEntitiesLoaded();

    $scope.loadRuleOnSelection = function (row) {
        $scope.selectedIndex = $scope.queriesGrid.data.indexOf(row.entity);
        $scope.loadRule();
    };

    $scope.queriesGrid.onRegisterApi = $scope.rowSelection;

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
        var qbData = $scope.$builder.queryBuilder('getDrools');

        if (qbData === false) {
            $scope.alertError('Can not execute / invalid query');
            return;
        }
        // because new window using local storage vs state params
        localStorage['qbData'] = JSON.stringify(qbData);
        localStorage['qbTitle'] = $scope.title.length ? $scope.title.trim() : '';
    };
});
