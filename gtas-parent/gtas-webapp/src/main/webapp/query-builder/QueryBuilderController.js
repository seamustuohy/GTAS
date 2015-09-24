app.controller('QueryBuilderController', function ($scope, $injector, jqueryQueryBuilderWidget, queryBuilderFactory, $location, gridOptionsLookupService, jqueryQueryBuilderService) {
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
        $scope.gridOpts.data = data;
    };

    $injector.invoke(jqueryQueryBuilderWidget, this, {$scope: $scope });
    $injector.invoke(queryBuilderFactory, this, {$scope: $scope });

    jqueryQueryBuilderService.init('querybuilder');

//    $scope.resultsGrid = $.extend({}, $scope.gridOpts);
//    $scope.resultsGrid.enableColumnResizing = true;

    $scope.gridOpts = gridOptionsLookupService.defaultGridOptions();
    $scope.gridOpts.columnDefs = gridOptionsLookupService.getLookupColumnDefs('QUERIES');
    $scope.gridOpts.exporterCsvFilename = 'MySavedQueries.csv';
    $scope.gridOpts.exporterPdfHeader = { text: "My Saved Queries", style: 'headerStyle' };

    jqueryQueryBuilderService.getList().then(function (myData) {
        $scope.setData(myData.result);
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

    $scope.loadRuleOnSelection = function (row) {
        $scope.selectedIndex = $scope.gridOpts.data.indexOf(row.entity);
        $scope.loadRule();
    };

    $scope.gridOpts.onRegisterApi = $scope.rowSelection;

    //TODO it's SPRINT 10 adelorie needs to go away but QueryBuilder REST requires even though it gets from spring... Ugh...
    $scope.summaryDefaults = {description: null, userId: 'adelorie', title: ''};
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

        //TODO userId: $scope.userId, SHOULD go away it's sprint 10!
        queryObject = {
            id: $scope.ruleId,
            title: $scope.title,
            description: $scope.description || null,
            userId: $scope.userId,
            query: query
        };

        jqueryQueryBuilderService.save(queryObject).then($scope.updateQueryBuilderOnSave);
    };

    $scope.viewType = 'FLIGHT';
    $scope.executeQuery = function () {
        var qbData = $scope.$builder.queryBuilder('getDrools');

        if (qbData === false) {
            $scope.alertError('Can not execute / invalid query');
            return;
        }
        localStorage['qbData'] = JSON.stringify(qbData);
        localStorage['qbTitle'] = $scope.title.trim();
        localStorage['qbType'] = $scope.viewType;
        $location.url(['/query/', $scope.viewType.toLocaleLowerCase(), 's'].join(''));
    };
});
