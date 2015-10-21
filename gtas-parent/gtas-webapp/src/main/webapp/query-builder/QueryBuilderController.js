app.controller('QueryBuilderController', function ($scope, $rootScope, $injector, jqueryQueryBuilderWidget, queryBuilderFactory, $location, gridOptionsLookupService, jqueryQueryBuilderService) {
    'use strict';
    var today = moment().format('YYYY-MM-DD').toString(),
        model = {
            summary: {
                query: function (obj) {
                    this.title = obj ? obj.title : '';
                    this.description = obj ? obj.description : null;
                },
                rule: function (obj) {
                    this.title = obj ? obj.title : '';
                    this.description = obj ? obj.description : null;
                    this.startDate = obj ? obj.startDate : today;
                    this.endDate = obj ? obj.endDate : null;
                    this.enabled = obj ? obj.enabled : true;
                }
            }
        };

    $scope.mode = "query";
    $injector.invoke(jqueryQueryBuilderWidget, this, {$scope: $scope });
    $injector.invoke(queryBuilderFactory, this, {$scope: $scope });

    jqueryQueryBuilderService.init('querybuilder');

//    $scope.resultsGrid.enableColumnResizing = true;

    $scope.qbGrid = gridOptionsLookupService.getGridOptions('queries');
    $scope.qbGrid.columnDefs = gridOptionsLookupService.getLookupColumnDefs('queries');
    $scope.qbGrid.exporterCsvFilename = 'MySavedQueries.csv';
    $scope.qbGrid.exporterPdfHeader = { text: "My Saved Queries", style: 'headerStyle' };


    jqueryQueryBuilderService.getList().then(function (myData) {
        $scope.setData.query(myData.result);
    });

    $scope.hideGrid = true;

    $scope.loadRule = function () {
        var obj = $scope.qbGrid.data[$scope.selectedIndex];
        $scope.hideGrid = true;
        $scope.ruleId = obj.id;
        $scope.loadSummary(new model[$scope.mode](obj));
        $scope.$builder.queryBuilder('loadRules', obj.query);
    };

    $scope.buildAfterEntitiesLoaded();

    $scope.loadRuleOnSelection = function (row) {
        $scope.selectedIndex = $scope.qbGrid.data.indexOf(row.entity);
        $scope.loadRule();
    };

    $scope.qbGrid.onRegisterApi = $scope.rowSelection;

});
