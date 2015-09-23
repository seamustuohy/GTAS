app.factory('queryBuilderFactory', function () {
    'use strict';
    return function ($scope, $timeout, jqueryQueryBuilderService, $interval) {
        $scope.ruleId = null;
        $scope.clearTables = function () {};

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
        //TODO remove when query service does not need it
        $scope.userId = 'adelorie';

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

            jqueryQueryBuilderService.getList().then(function (myData) {
                $scope.setData(myData.result);
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
                var rowIndexToDelete = $scope.gridOpts.data.indexOf(rowEntity);

                jqueryQueryBuilderService.delete($scope.ruleId).then(function (response) {
                    $scope.gridOpts.data.splice(rowIndexToDelete, 1);
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

        $scope.showPencil = function (id) {
            $timeout(function () {
                var $pageControls = $('.ng-table-pagination').children();
                if ($scope.ruleId === null && $pageControls.length >= 4) {
                    $pageControls.eq($pageControls.length - 2).find('a')[0].click();
                }
                $timeout(function () {
                    var $tableRows = $('table tbody').eq(0).find('tr');
                    if ($scope.ruleId === null) {
                        $tableRows.last().click();
                    }
                    $scope.ruleId = id;
                    $scope.saving = false;
                }, 200);
            }, 200);
        };

        $scope.isBeingEdited = function () {
            return $scope.ruleId === this.$data[this.$index].id;
        };

        $scope.loadSummary = function (summary) {
            Object.keys(summary).forEach(function (key) {
                $scope[key] = summary[key];
            });
            $scope.clearTables();
        };

        $scope.formats = ["YYYY-MM-DD"];

        $scope.newRule = function () {
            $scope.ruleId = null;
            $scope.$builder.queryBuilder('reset');
            $scope.loadSummary($scope.summaryDefaults);
            if (document.querySelector('[ng-model="endDate"] input')) {
                document.querySelector('[ng-model="endDate"] input').value = null;
            }
            document.getElementById('title').focus();
            if ($scope.gridApi !== undefined) {
                $scope.gridApi.selection.clearSelectedRows();
                $scope.selectedIndex = null;
            }
        };
    };
});
