app.controller('RiskCriteriaController', function($scope, $injector, QueryBuilderCtrl, $filter, $q, ngTableParams, riskCriteriaService) {
    $injector.invoke(QueryBuilderCtrl, this, {$scope: $scope });
    var data = [];

    $scope.loadRule = function () {
        //<i class="glyphicon glyphicon-pencil"></i>
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
        riskCriteriaService.ruleDelete($scope.ruleId, $scope.authorId).then(function (myData) {
            $scope.newRule();
            $scope.tableParams.reload();
        });
    };

    $scope.summaryDefaults = {title: '', description: null, startDate: $scope.today.toString(), endDate: null, enabled: true};

    $($scope.startDate).datepicker({
        minDate: "today",
        startDate: "today",
        format: 'yyyy-mm-dd',
        autoClose: true
    });

//    $scope.newRule();
    $scope.saving = false;
    $scope.save = function() {
        if ($scope.saving) return;
//        $scope.saving = true;
        var ruleObject;
        var startDate = moment($scope.startDate, $scope.formats, true);
        var endDate = moment($scope.endDate, $scope.formats, true);

        $scope.title = $scope.title.trim();
        if (!$scope.title.length ) {
            alert('Risk Criteria title summary can not be blank!');
            $scope.saving = false;
            return;
        }

        /* was told startDate ignored on updates so only matters on new rules */
        if ($scope.ruleId === null) {
            if (!startDate.isValid())
            {
                alert('Dates must be in this format: ' + $scope.formats.toString());
                $scope.saving = false;
                return;
            }
            if (startDate < $scope.today ) {
                alert('Risk Criteria start date must be today or later when created new.');
                $scope.saving = false;
                return;
            }
        }

        if ($scope.endDate !== null) {
            if (!endDate.isValid() ) {
                alert('End Date must be empty/open or in this format: ' + $scope.formats.toString());
                $scope.saving = false;
                return;
            }
            if (endDate < startDate ) {
                alert('End Date must be empty/open or be >= startDate: ' + $scope.formats.toString());
                $scope.saving = false;
                return;
            }
        }

        ruleObject = {
            id: $scope.ruleId,
            details: $scope.$builder.queryBuilder('saveRules'),
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
            if (typeof myData.errorCode !== "undefined")
            {
                alert(myData.errorMessage);
                return;
            }
            $scope.ruleId = $scope.ruleId = myData.responseDetails[0].attributeValue || null;
            $scope.tableParams.reload();
            $scope.saving = false;
        });
    };
});
