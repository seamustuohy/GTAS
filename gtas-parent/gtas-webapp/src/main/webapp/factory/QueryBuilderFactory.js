app.factory('queryBuilderFactory', function () {
    'use strict';
    return function ($scope, $timeout, jqueryQueryBuilderService, $interval) {
        $scope.ruleId = null;

        $scope.alerts = [];
        $scope.alert = function (type, text) {
            $scope.alerts.push({type: type, msg: text});
            $timeout(function () {
                $scope.alerts[$scope.alerts.length - 1].expired = true;
            }, 4000);
            $timeout(function () {
                $scope.alerts.splice($scope.alerts.length - 1, 1);
            }, 5000);
        };

        $scope.alertSuccess = function (text) {
            $scope.alert('success', text);
        };

        $scope.alertError = function (text) {
            $scope.alert('danger', text);
        };

        $scope.alertInfo = function (text) {
            $scope.alert('info', text);
        };

        $scope.alertWarn = function (text) {
            $scope.alert('warning', text);
        };

        $scope.closeAlert = function (index) {
            $scope.alerts.splice(index, 1);
        };

        $scope.updateQueryBuilderOnSave = function (myData) {
            if (myData.status === 'FAILURE') {
                $scope.alertError(myData.message);
                $scope.saving = false;
                return;
            }
            if (typeof myData.errorCode !== "undefined") {
                $scope.alertError(myData.errorMessage);
                return;
            }

            jqueryQueryBuilderService.getList().then(function (myData) {
                $scope.setData(myData.result);
                $interval(function () {
                    var page;
                    if (!$scope.selectedIndex) {
                        page = $scope.gridApi.pagination.getTotalPages();
                        $scope.selectedIndex = $scope.qbGrid.data.length - 1;
                        $scope.gridApi.pagination.seek(page);
                    }
                    $scope.gridApi.selection.clearSelectedRows();
                    $scope.gridApi.selection.selectRow($scope.qbGrid.data[$scope.selectedIndex]);
                    $scope.saving = false;
                }, 0, 1);
            });
        };

        $scope.rowSelection = function (gridApi) {
            $scope.gridApi = gridApi;
            gridApi.selection.on.rowSelectionChanged($scope, function (row) {
                if (row.isSelected) {
                    $scope.loadRuleOnSelection(row);
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

            var selectedRowEntities = $scope.gridApi.selection.getSelectedRows();

            selectedRowEntities.forEach(function (rowEntity) {
                var rowIndexToDelete = $scope.qbGrid.data.indexOf(rowEntity);

                jqueryQueryBuilderService.delete($scope.ruleId).then(function (response) {
                    $scope.qbGrid.data.splice(rowIndexToDelete, 1);
                    $scope.newRule();
                });
            });
        };

        $scope.today = moment().format('YYYY-MM-DD').toString();
        $scope.calendarOptions = {
            format: 'yyyy-mm-dd',
            autoClose: true
        };
        $scope.options = {
            allow_empty: true,
            service: "DROOLS",
            plugins: {
                'bt-tooltip-errors': {delay: 100},
                'sortable': null,
                'filter-description': {mode: 'bootbox'},
                'bt-selectpicker': null,
                'unique-filter': null,
                'bt-checkbox': {color: 'primary'}
            },
            filters: []
        };

        $scope.loadSummary = function (summary) {
            Object.keys(summary).forEach(function (key) {
                $scope[key] = summary[key];
            });
        };

        $scope.formats = ["YYYY-MM-DD"];

        $scope.newRule = function () {
            $scope.showSummaryClass = 'show';
            $scope.ruleId = null;
            $scope.$builder.queryBuilder('reset');
            $scope.loadSummary($scope.summaryDefaults);
            //TODO temp had trouble setting md-datepicker default value to null, default value today
            if (document.querySelector('[ng-model="endDate"] input')) {
                document.querySelector('[ng-model="endDate"] input').value = null;
            }

            //TODO find angular way to set focus
            document.querySelector('[autofocus]').focus();
            if ($scope.gridApi !== undefined) {
                $scope.gridApi.selection.clearSelectedRows();
                $scope.selectedIndex = null;
            }
        };
    };
});
