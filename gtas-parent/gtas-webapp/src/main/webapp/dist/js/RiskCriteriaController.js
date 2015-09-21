app.controller('RiskCriteriaController', function ($scope, $rootScope, $injector, QueryBuilderCtrl, GridControl, $filter, $q, crudService, $timeout, $interval) {
    'use strict';
    $injector.invoke(QueryBuilderCtrl, this, {$scope: $scope});
    $injector.invoke(GridControl, this, {$scope: $scope});

    crudService.init('riskcriteria');

    var setData = function (myData) {
        var temp, data = [];
        myData.forEach(function (obj) {
            temp = $.extend({}, obj.summary, {id: obj.id});
            data.push(temp);
        });
        $scope.gridOpts.data = data;
    };

    $scope.gridOpts.columnDefs = $rootScope.columns.RISK_CRITERIA;
    $scope.gridOpts.exporterCsvFilename = 'riskCriteria.csv';
    $scope.gridOpts.exporterPdfHeader = {text: "Risk Criteria", style: 'headerStyle'};

    crudService.getList().then(function (myData) {
        setData(myData.result);
    });

    $scope.gridOpts.onRegisterApi = function (gridApi) {
        //set gridApi on scope
        $scope.gridApi = gridApi;
        gridApi.selection.on.rowSelectionChanged($scope, function (row) {
            if (row.isSelected) {
                $scope.selectedIndex = $scope.gridOpts.data.indexOf(row.entity);
                crudService.loadRuleById(row.entity.id).then(function (myData) {
                    var result = myData.result;
                    $scope.ruleId = result.id;
                    $scope.loadSummary(result.summary);
                    $scope.$builder.queryBuilder('loadRules', result.details);
                });
            } else {
                $scope.newRule();
                $scope.gridApi.selection.clearSelectedRows();
            }
        });
        //gridApi.rowEdit.on.saveRow($scope, $scope.saveRow);
    };

    $scope.buildAfterEntitiesLoaded({deleteEntity: 'HITS'});

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

            crudService.delete($scope.ruleId).then(function (response) {
                $scope.gridOpts.data.splice(rowIndexToDelete, 1);
            });
        });
    };

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

        crudService.save(ruleObject).then(function (myData) {
            var temp, data = [];
            if (typeof myData.errorCode !== "undefined") {
                $scope.alertError(myData.errorMessage);
                return;
            }

            crudService.getList().then(function (myData) {

                setData(myData.result);
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

    $scope.$scope = $scope;
}).config(function ($mdDateLocaleProvider) {
    'use strict';
    $mdDateLocaleProvider.formatDate = function (date) {
        return moment(date).format('YYYY-MM-DD');
    };
});
