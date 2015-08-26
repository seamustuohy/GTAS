app.controller('RiskCriteriaController', function ($scope, $injector, QueryBuilderCtrl, $filter, $q, ngTableParams, riskCriteriaService, $timeout, $interval) {
    'use strict';
    $injector.invoke(QueryBuilderCtrl, this, {$scope: $scope });
//    var data = [];
    var paginationPageSize = 10;
    $scope.$scope = $scope;

    var pageOfPages = function (currentPage, pageCount) {
        return moment().format('YYYY-MM-DD') + (pageCount === 1 ? '' : '\t' + currentPage.toString() + ' of ' + pageCount.toString());
    };

    $scope.gridOpts = {
        paginationPageSize: paginationPageSize,
        paginationPageSizes: [],
        enableFiltering: true,
        enableCellEditOnFocus: false,
        showGridFooter: true,
        multiSelect: false,
        enableGridMenu: true,
        enableSelectAll: false,
        exporterCsvFilename: 'riskCriteria.csv',
        exporterPdfDefaultStyle: {fontSize: 9},
        exporterPdfTableStyle: {margin: [10, 10, 10, 10]},
        exporterPdfTableHeaderStyle: {
            fontSize: 10,
            bold: true,
            italics: true
        },
        exporterPdfHeader: { text: "Risk Criteria", style: 'headerStyle' },
        exporterPdfFooter: function (currentPage, pageCount) {
            return { text: pageOfPages(currentPage, pageCount), style: 'footerStyle' };
        },
        exporterPdfCustomFormatter: function (docDefinition) {
            docDefinition.styles.headerStyle = {
                fontSize: 22,
                bold: true,
                alignment: 'center',
                lineHeight: 1.5
            };
            docDefinition.styles.footerStyle = {
                fontSize: 10,
                italic: true,
                alignment: 'center'
            };
            return docDefinition;
        },
        exporterPdfOrientation: 'landscape',
        exporterPdfPageSize: 'LETTER',
        exporterPdfMaxGridWidth: 650,
        exporterCsvLinkElement: angular.element(document.querySelectorAll(".custom-csv-link-location"))
    };

    var riskCriteriaColumns = [{
        name: "title",
        enableCellEdit: false,
        enableColumnMenu: false
    }, {
        name: "description",
        enableCellEdit: false,
        enableColumnMenu: false
    }, {
        name: "startDate",
        enableCellEdit: false,
        enableColumnMenu: false
    }, {
        name: "endDate",
        enableCellEdit: false,
        enableColumnMenu: false
    }, {
        name: "enabled",
        enableCellEdit: false,
        enableColumnMenu: false
    }];
    $scope.gridOpts.columnDefs = riskCriteriaColumns;

    riskCriteriaService.getList($scope.authorId).then(function (myData) {
        var temp, data = [];
        myData.forEach(function (obj) {
            temp = $.extend({}, obj.summary, {id: obj.id});
            data.push(temp);
        });
        $scope.gridOpts.data = data;
    });

    $scope.gridOpts.onRegisterApi = function (gridApi) {
        //set gridApi on scope
        $scope.gridApi = gridApi;
        gridApi.selection.on.rowSelectionChanged($scope, function (row) {
            if (row.isSelected) {
                $scope.selectedIndex = $scope.gridOpts.data.indexOf(row.entity);
                riskCriteriaService.loadRuleById(row.entity.id).then(function (myData) {
                    $scope.ruleId = myData.id;
                    $scope.loadSummary(myData.summary);
                    $scope.$builder.queryBuilder('loadRules', myData.details);
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

            // create a promise to reject errors from server side.
            var rowDeferred = $q.defer();

            console.log('Selected row: ' + rowIndexToDelete + ' to delete.');
            var deferred = riskCriteriaService.ruleDelete($scope.ruleId, $scope.authorId);

            deferred.$promise.then(function (response) {
                    // success callback
                    var newLength = $scope.gridOpts.data.splice(rowIndexToDelete, 1);
                    rowDeferred.resolve(newLength);
                },
                function (error) {
                    // will fail because URL is not real, but will resolve anyway for purposes of this test.

                    // this is what I expect will remove the row from the ui-grid in the UI.
                    var newLength = $scope.gridOpts.data.splice(rowIndexToDelete, 1);
                    rowDeferred.resolve(newLength);
                    //rowDeferred.reject(error);
                });
        });
    };

    $scope.summaryDefaults = {title: '', description: null, startDate: $scope.today.toString(), endDate: null, enabled: true};


//    $scope.newRule();
    $scope.saving = false;
    $scope.save = function () {
        var ruleObject, startDate, endDate, details;

        if ($scope.saving) return;

        $scope.saving = true;
        startDate = moment($scope.startDate, $scope.formats, true);
        endDate = moment($scope.endDate, $scope.formats, true);

        $scope.title = $scope.title.trim();
        if (!$scope.title.length ) {
            $scope.alertError('Title summary can not be blank!');
            $scope.saving = false;
            return;
        }

        /* was told startDate ignored on updates so only matters on new rules */
        if ($scope.ruleId === null) {
            if (!startDate.isValid())
            {
                $scope.alertError('Dates must be in this format: ' + $scope.formats.toString());
                $scope.saving = false;
                return;
            }
            if (startDate < $scope.today ) {
                $scope.alertError('Start date must be today or later when created new.');
                $scope.saving = false;
                return;
            }
        }

        if ($scope.endDate) {
            if (!endDate.isValid() ) {
                $scope.alertError('End Date must be empty/open or in this format: ' + $scope.formats.toString());
                $scope.saving = false;
                return;
            }
            if (endDate < startDate ) {
                $scope.alertError('End Date must be empty/open or be >= startDate: ' + $scope.formats.toString());
                $scope.saving = false;
                return;
            }
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
                startDate: $scope.startDate,
                endDate: $scope.endDate || null,
                enabled: $scope.enabled
            }
        };

//        data.push(ruleObject.summary);
//        $scope.tableData = data;

//        $scope.tableParams.total($scope.tableData.length);
//        $scope.tableParams.reload();

        riskCriteriaService.ruleSave(ruleObject, $scope.authorId).then(function (myData) {
            var temp, data = [];
            if (typeof myData.errorCode !== "undefined") {
                $scope.alertError(myData.errorMessage);
                return;
            }

            riskCriteriaService.getList($scope.authorId).then(function (myData) {
                var temp, data = [];
                myData.forEach(function (obj) {
                    temp = $.extend({}, obj.summary, {id: obj.id});
                    data.push(temp);
                });

                $scope.gridOpts.data = data;
                $interval( function() {
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

    $timeout(function() {
        var $startDate = $('#start-date')
        $startDate.datepicker({
            minDate: "today",
            startDate: "today",
            format: 'yyyy-mm-dd',
            autoClose: true
        });

        $startDate.datepicker('setDate', new Date($scope.today));
        $startDate.datepicker('update');
        $startDate.val($scope.today.toString());
    }, 100)
});
