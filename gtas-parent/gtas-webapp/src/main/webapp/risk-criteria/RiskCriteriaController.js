app.controller('RiskCriteriaController', function ($scope, $injector, QueryBuilderCtrl, $filter, $q, ngTableParams, riskCriteriaService, $timeout) {
    'use strict';
    $injector.invoke(QueryBuilderCtrl, this, {$scope: $scope });
    var data = [];

    $scope.loadRule = function () {
        riskCriteriaService.loadRuleById(this.summary.id).then(function (myData) {
            $scope.ruleId = myData.id;
            $scope.loadSummary(myData.summary);
            $scope.$builder.queryBuilder('loadRules', myData.details);
        });
    };

    $scope.tableParams = new ngTableParams({
        page: 1,            // show first page
        count: 10,          // count per page
        filter: {},
        sorting: {
            hits: 'desc',
            destinationDateTimeSort: 'asc' //, 'number': 'asc'     // initial sorting
        }
    }, {
        counts: [],         // disable / hide page row count toggle
        total: data.length, // length of data
        getData: function ($defer, params) {
            riskCriteriaService.getList($scope.authorId).then(function (myData) {
                var filteredData, orderedData;
                data = [];

                if (!Array.isArray(myData)) return;

                myData.forEach(function (obj) {
                    // add id to summary obj
                    obj.summary.id = obj.id;
                    // add summary obj to data array
                    data.push(obj.summary);
                });

                filteredData = params.filter() ?
                    $filter('filter')(data, params.filter()) :
                    data;
                orderedData = params.sorting() ?
                    $filter('orderBy')(filteredData, params.orderBy()) :
                    data;

                params.total(orderedData.length); // set total for recalc pagination
                $defer.resolve(orderedData.slice((params.page() - 1) * params.count(), params.page() * params.count()));
            });
        }
    });

    $scope.buildAfterEntitiesLoaded({deleteEntity: 'HITS'});

    $scope.delete = function () {
        if ($scope.ruleId) {
            $scope.alertError('No rule loaded to delete');
            return;
        }

        if ($scope.authorId) {
            $scope.alertError('No user authenticated');
            return;
        }
        riskCriteriaService.ruleDelete($scope.ruleId, $scope.authorId).then(function (myData) {
            $scope.newRule();
            $scope.tableParams.reload();
        });
    };

    $scope.summaryDefaults = {title: '', description: null, startDate: $scope.today.toString(), endDate: null, enabled: true};


//    $scope.newRule();
    $scope.saving = false;
    $scope.save = function() {
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

        data.push(ruleObject.summary);
        $scope.tableData = data;

        $scope.tableParams.total($scope.tableData.length);
        $scope.tableParams.reload();

        riskCriteriaService.ruleSave(ruleObject, $scope.authorId).then(function (myData) {
            var $tableRows = $('table tbody').eq(0).find('tr');
            if (typeof myData.errorCode !== "undefined") {
                $scope.alertError(myData.errorMessage);
                return;
            }
            $scope.tableParams.reload();
            $scope.showPencil(myData.responseDetails[0].attributeValue);
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
