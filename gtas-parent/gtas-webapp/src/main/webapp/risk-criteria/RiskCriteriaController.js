app.controller('RiskCriteriaController', function ($scope, $rootScope, $injector, jqueryQueryBuilderWidget, queryBuilderFactory, GridControl, $filter, $q, jqueryQueryBuilderService, $timeout, $interval) {
    'use strict';
    jqueryQueryBuilderService.init('riskcriteria');

    $scope.setData = function (myData) {
        var temp, data = [];
        myData.forEach(function (obj) {
            temp = $.extend({}, obj.summary, {id: obj.id});
            data.push(temp);
        });
        $scope.gridOpts.data = data;
    };

    $injector.invoke(jqueryQueryBuilderWidget, this, {$scope: $scope });
    $injector.invoke(queryBuilderFactory, this, {$scope: $scope });
    $injector.invoke(GridControl, this, {$scope: $scope});

    $scope.gridOpts.columnDefs = $rootScope.columns.RISK_CRITERIA;
    $scope.gridOpts.exporterCsvFilename = 'riskCriteria.csv';
    $scope.gridOpts.exporterPdfHeader = {text: "Risk Criteria", style: 'headerStyle'};

    jqueryQueryBuilderService.getList().then(function (myData) {
        $scope.setData(myData.result);
    });

    $scope.loadRuleOnSelection = function (row) {
        $scope.selectedIndex = $scope.gridOpts.data.indexOf(row.entity);
        jqueryQueryBuilderService.loadRuleById(row.entity.id).then(function (myData) {
            var result = myData.result;
            $scope.ruleId = result.id;
            $scope.loadSummary(result.summary);
            $scope.$builder.queryBuilder('loadRules', result.details);
        });
    };

    $scope.gridOpts.onRegisterApi = $scope.rowSelection;

    $scope.buildAfterEntitiesLoaded({deleteEntity: 'HITS'});

    $scope.summaryDefaults = {title: '', description: null, enabled: true};

//    $scope.newRule();
    $scope.saving = false;
    $scope.save = function () {
        var ruleObject, startDate, endDate, details;

        if ($scope.saving) {
            return;
        }

        $scope.saving = true;
        startDate = moment($scope.startDate, $scope.formats, true);
        endDate = moment($scope.endDate, $scope.formats, true);

        $scope.title = $scope.title.trim();
        if (!$scope.title.length) {
            $scope.alertError('Title summary can not be blank!');
            $scope.saving = false;
            return;
        }

        /* was told startDate ignored on updates so only matters on new rules */
        if ($scope.ruleId === null) {
            //if (!startDate.isValid()) {
            //    $scope.alertError('Dates must be in this format: ' + $scope.formats.toString());
            //    $scope.saving = false;
            //    return;
            //}
            //if (startDate < $scope.today) {
            //    $scope.alertError('Start date must be today or later when created new.');
            //    $scope.saving = false;
            //    return;
            //}
        }

        if ($scope.endDate) {
            //if (!endDate.isValid()) {
            //    $scope.alertError('End Date must be empty/open or in this format: ' + $scope.formats.toString());
            //    $scope.saving = false;
            //    return;
            //}
            //if (endDate < startDate) {
            //    $scope.alertError('End Date must be empty/open or be >= startDate: ' + $scope.formats.toString());
            //    $scope.saving = false;
            //    return;
            //}
        }

        details = $scope.$builder.queryBuilder('getDrools');

        if (details === false) {
            $scope.saving = false;
            return;
        }
        ruleObject = {
            id: $scope.ruleId,
            details: details,
            summary: {
                title: $scope.title,
                description: $scope.description || null,
                startDate: $scope.startDate || $scope.today,
                endDate: $scope.endDate || null,
                enabled: $scope.enabled
            }
        };

        jqueryQueryBuilderService.save(ruleObject).then($scope.updateQueryBuilderOnSave);
    };

    $scope.$scope = $scope;
}).config(function ($mdDateLocaleProvider) {
    'use strict';
    $mdDateLocaleProvider.formatDate = function (date) {
        return moment(date).format('YYYY-MM-DD');
    };
});
