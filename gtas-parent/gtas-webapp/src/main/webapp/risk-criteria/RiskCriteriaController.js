app.controller('RiskCriteriaController', function ($scope, $injector, jqueryQueryBuilderWidget, queryBuilderFactory, gridOptionsLookupService, jqueryQueryBuilderService, $timeout) {
    'use strict';
    var model = {
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

    jqueryQueryBuilderService.init('riskcriteria');
    $scope.mode = "rule";

    $injector.invoke(jqueryQueryBuilderWidget, this, {$scope: $scope });
    $injector.invoke(queryBuilderFactory, this, {$scope: $scope });

    $scope.qbGrid = gridOptionsLookupService.getGridOptions('riskCriteria');
    $scope.qbGrid.columnDefs = gridOptionsLookupService.getLookupColumnDefs('riskCriteria');
    $scope.qbGrid.exporterCsvFilename = 'riskCriteria.csv';
    $scope.qbGrid.exporterPdfHeader = {text: "Risk Criteria", style: 'headerStyle'};

    jqueryQueryBuilderService.getList().then(function (myData) {
        $scope.setData[$scope.mode](myData.result);
    });

    $scope.loadRuleOnSelection = function (row) {
        $scope.selectedIndex = $scope.qbGrid.data.indexOf(row.entity);
        jqueryQueryBuilderService.loadRuleById(row.entity.id).then(function (myData) {
            var result = myData.result;
            $scope.ruleId = result.id;
            $scope.loadSummary(result.summary);
            $scope.$builder.queryBuilder('loadRules', result.details);
        });
    };

    $scope.qbGrid.onRegisterApi = $scope.rowSelection;
    $scope.buildAfterEntitiesLoaded({deleteEntity: 'HITS'});


    $scope.enabled = true;
    $scope.$scope = $scope;

    //$scope.$watch("endDate", function (newValue) {
    //    var datepicker;
    //    if (newValue === null || newValue === undefined) {
    //        $timeout(function () {
    //            datepicker = document.querySelectorAll('.md-datepicker-input')[1];
    //            datepicker.value = '';
    //        }, 5);
    //    }
    //});
});
