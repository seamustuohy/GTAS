app.controller('BuildController', function ($scope, $injector, jqueryQueryBuilderWidget, gridOptionsLookupService, jqueryQueryBuilderService, spinnerService, $mdSidenav, $stateParams, $interval, $timeout) {
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
        },
        resetModels = function (m) {
            //reset all models
            m.query = new model.summary.query();
            m.rule = new model.summary.rule();
        },
        setId = function () {
            return $scope.buttonMode === $scope.mode ? $scope.ruleId : null;
        },
        mode = $stateParams.mode,
        loadOnSelection = {
            query: function (row) {
                $scope.selectedIndex = $scope.qbGrid.data.indexOf(row.entity);
                $scope.loadQuery();
            },
            rule: function (row) {
                $scope.selectedIndex = $scope.qbGrid.data.indexOf(row.entity);
                jqueryQueryBuilderService.loadRuleById('rule', row.entity.id).then(function (myData) {
                    var result = myData.result;
                    $scope.ruleId = result.id;
                    $scope.loadSummary('rule', result.summary);
                    $scope.$builder.queryBuilder('loadRules', result.details);
                });
            }
        };

    $scope.$watch('selectedMode', function () {
        $scope.updateGrid();
    });

    $scope.copyRule = function () {
        spinnerService.show('html5spinner');
        var originalObj = $scope[$scope.mode], ruleId = $scope.ruleId;
        console.log(originalObj);
        $scope.addNew();
        $scope.gridApi.selection.clearSelectedRows();
        jqueryQueryBuilderService.copyRule(ruleId).then(function (response) {
            //this makes me cringe... hope result becomes object and this goes away.
            var partialCopyObj = {
                id: response.result,
                title: response.responseDetails[1]['attributeValue'],
                startDate: today,
                endDate: null,
                modifiedOn: today,
                modifiedBy: 'me'
            };
            $scope.qbGrid.data.unshift($.extend({}, originalObj, partialCopyObj));
            spinnerService.hide('html5spinner');
        });
    };

    $scope.mode = mode;
    $scope.selectedMode = mode;

    $scope.prompt = {
        open: function (mode) {
            $scope.buttonMode = mode;
            $mdSidenav(mode).open();
        },
        cancel: function () {
            $mdSidenav($scope.buttonMode).close();
        }
    };

    $scope.setData = {
        query: function (myData) {
            var data = [];
            if (myData === undefined || !Array.isArray(myData)) {
                $scope.saving = false;
                return;
            }

            myData.forEach(function (obj) {
                data.push(obj);
            });
            $scope.qbGrid.data = data;
        },
        rule: function (myData) {
            var temp, data = [];
            myData.forEach(function (obj) {
                temp = $.extend({}, obj.summary, {
                    id: obj.id,
                    hitCount: obj.hitCount,
                    modifiedOn: obj.modifiedOn,
                    modifiedBy: obj.modifiedBy
                });
                data.push(temp);
            });
            $scope.qbGrid.data = data;
        },
        all: function (myData) {
            var temp, data = [];
            myData.forEach(function (obj) {
                temp = $.extend({}, obj.summary, {
                    id: obj.id,
                    hitCount: obj.hitCount,
                    modifiedOn: obj.modifiedOn,
                    modifiedBy: obj.modifiedBy
                });
                data.push(temp);
            });
            $scope.qbGrid.data = data;
        }
    };

    $scope.getToolbarText = function () {
        switch ($scope.buttonMode) {
            case 'query':
                return $scope.mode === 'query' && $scope.ruleId !== null ? 'Update Query' : 'Save Query';
            case 'rule':
                return $scope.mode === 'rule' && $scope.ruleId !== null ? 'Update Rule' : 'Save Rule';
        }
    };

    //TODO move out to a service
    $scope.executeQuery = function () {
        var query = $scope.$builder.queryBuilder('getDrools');
        if (query === false) {
            $scope.alertError('Can not execute / invalid query');
            return;
        }
        localStorage['query'] = JSON.stringify(query);
        localStorage['qbTitle'] = $scope[$scope.mode].title.length ? $scope[$scope.mode].title.trim() : '';
    };

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

    $scope.updateGrid = function () {
        spinnerService.show('html5spinner');
        jqueryQueryBuilderService.getList($scope.selectedMode).then(function (myData) {
            $scope.setData[$scope.mode](myData.result);
            $scope.gridApi.selection.clearSelectedRows();
            //$scope.gridApi.selection.selectRow($scope.qbGrid.data[$scope.selectedIndex]);
            $scope.saving = false;
            spinnerService.hide('html5spinner');
        });
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

        jqueryQueryBuilderService.getList($scope.selectedMode).then(function (myData) {
            $scope.setData[$scope.selectedMode](myData.result);
            $mdSidenav('rule').close();
            $mdSidenav('query').close();
            $interval(function () {
                //var page;
                //if (!$scope.selectedIndex) {
                //    page = $scope.gridApi.pagination.getTotalPages();
                //    $scope.selectedIndex = $scope.qbGrid.data.length - 1;
                //    $scope.gridApi.pagination.seek(page);
                //}
                $scope.gridApi.selection.clearSelectedRows();
                //$scope.gridApi.selection.selectRow($scope.qbGrid.data[$scope.selectedIndex]);
                $scope.saving = false;
                spinnerService.hide('html5spinner');
            }, 0, 1);
        });
    };

    $scope.rowSelection = function (gridApi) {
        $scope.gridApi = gridApi;
        gridApi.selection.on.rowSelectionChanged($scope, function (row) {
            if (row.isSelected) {
                loadOnSelection[$scope.mode](row);
            } else {
                $scope.addNew();
                $scope.gridApi.selection.clearSelectedRows();
            }
        });
    };

    $scope.delete = function () {
        var selectedRowEntities = $scope.gridApi.selection.getSelectedRows().reverse(),
            rowIndexToDelete;

        if (!$scope.ruleId) {
            $scope.alertError('No rule loaded to delete');
            return;
        }
        $scope.addNew();
        spinnerService.show('html5spinner');
        selectedRowEntities.forEach(function (rowEntity) {
            rowIndexToDelete = $scope.qbGrid.data.indexOf(rowEntity);

            jqueryQueryBuilderService.delete(mode, rowEntity.id).then(function () {
                $scope.qbGrid.data.splice(rowIndexToDelete, 1);
                spinnerService.hide('html5spinner');
            });
        });
    };

    $scope.today = today;
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

    $scope.loadSummary = function (obj, summary) {
        Object.keys(summary).forEach(function (key) {
            $scope[obj][key] = summary[key];
        });
    };

    $scope.formats = ["YYYY-MM-DD"];

    $scope.addNew = function () {
        $scope.ruleId = null;
        $scope.$builder.queryBuilder('reset');
        resetModels($scope);
        ////TODO temp had trouble setting md-datepicker default value to null, default value today
        //if (document.querySelector('[ng-model="endDate"] input')) {
        //    document.querySelector('[ng-model="endDate"] input').value = null;
        //}
        //
        ////TODO find angular way to set focus
        //document.querySelector('[autofocus]').focus();
        //if ($scope.gridApi !== undefined) {
        //    $scope.gridApi.selection.clearSelectedRows();
        //    $scope.selectedIndex = null;
        //}
    };
    $scope.ruleId = null;
    $scope.saving = false;
    $scope.save = {
        query: {
            prompt: function () {
                $scope.prompt.open('query');
            },
            confirm: function () {
                var queryObject, query;
                if ($scope.saving) {
                    return;
                }

//                    $scope.saving = true;

                if ($scope.query.title && $scope.query.title.length) {
                    $scope.query.title = $scope.query.title.trim();
                }

                if (!$scope.query.title.length) {
                    $scope.alertError('Title summary can not be blank!');
                    $scope.saving = false;
                    return;
                }
                query = $scope.$builder.queryBuilder('getDrools');

                if (query === false) {
                    $scope.saving = false;
                    return;
                }
                queryObject = {
                    id: setId(),
                    title: $scope.query.title,
                    description: $scope.query.description || null,
                    query: query
                };
                spinnerService.show('html5spinner');
                jqueryQueryBuilderService.save('query', queryObject).then($scope.updateQueryBuilderOnSave);
            }
        },
        rule: {
            prompt: function () {
                $scope.prompt.open('rule');
            },
            confirm: function () {
                var ruleObject, details;

                if ($scope.saving) {
                    return;
                }

//                    $scope.saving = true;

                if ($scope.rule.title && $scope.rule.title.length) {
                    $scope.rule.title = $scope.rule.title.trim();
                }

                if ($scope.rule.title.length === 0) {
                    $scope.alertError('Title summary can not be blank!');
                    $scope.saving = false;
                    return;
                }

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

                if ($scope.rule.endDate) {
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
                    id: setId(),
                    details: details,
                    summary: $scope.rule
                };
                spinnerService.show('html5spinner');
                jqueryQueryBuilderService.save('rule', ruleObject).then($scope.updateQueryBuilderOnSave);
            }
        }
    };

    $injector.invoke(jqueryQueryBuilderWidget, this, {$scope: $scope });

    $scope.qbGrid = gridOptionsLookupService.getGridOptions(mode);
    $scope.qbGrid.columnDefs = gridOptionsLookupService.getLookupColumnDefs(mode);
    $scope.qbGrid.enableRowHeaderSelection = true;
    $scope.qbGrid.enableSelectAll = false;
    $scope.qbGrid.multiSelect = false;
    $scope.qbGrid.exporterCsvFilename = mode + '.csv';
    $scope.qbGrid.exporterPdfHeader = {text: mode, style: 'headerStyle'};
    $scope.qbGrid.onRegisterApi = $scope.rowSelection;

    //spinnerService.show('html5spinner');
    jqueryQueryBuilderService.getList($scope.selectedMode).then(function (myData) {
        $scope.setData[$scope.mode](myData.result);
        //spinnerService.hide('html5spinner');
    });

    //query specific
    $scope.loadQuery = function () {
        var obj = $scope.qbGrid.data[$scope.selectedIndex];
        $scope.ruleId = obj.id;
        $scope.loadSummary('query', new model.summary.query(obj));
        $scope.$builder.queryBuilder('loadRules', obj.query);
    };

    resetModels($scope);

    //if mode query     $scope.buildAfterEntitiesLoaded();
    $scope.buildAfterEntitiesLoaded({deleteEntity: 'HITS'});
    $scope.$scope = $scope;

    $scope.$watch("rule.endDate", function (newValue) {
        var datepicker;
        if (newValue === null || newValue === undefined) {
            $timeout(function () {
                datepicker = document.querySelectorAll('.md-datepicker-input')[1];
                datepicker.value = '';
            }, 5);
        }
    });
});
