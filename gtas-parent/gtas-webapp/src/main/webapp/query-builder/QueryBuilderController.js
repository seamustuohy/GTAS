app.controller('QueryBuilderController', function ($scope, $injector, QueryBuilderCtrl, $filter, $q, ngTableParams, queryBuilderService, queryService, $timeout) {
    'use strict';
    $injector.invoke(QueryBuilderCtrl, this, {$scope: $scope });
    var data = [],
        dataSource = {
            FLIGHT: [],
            TRAVELER: []
        };

    $scope.loadRule = function () {
        var obj = this.$data[this.$index];
        $scope.ruleId = obj.id;
        $scope.loadSummary({title: obj.title, description: obj.description });
        $scope.$builder.queryBuilder('loadRules', obj.query);
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
            queryBuilderService.getList($scope.authorId).then(function (myData) {
                var filteredData, orderedData;
                data = [];

                if (myData.result === undefined || !Array.isArray(myData.result)) {
                    return;
                }

                myData.result.forEach(function (obj) {
                    data.push(obj);
                });

                filteredData = params.filter() ? $filter('filter')(data, params.filter()) : data;
                orderedData = params.sorting() ? $filter('orderBy')(filteredData, params.orderBy()) : data;

                params.total(orderedData.length); // set total for recalc pagination
                $defer.resolve(orderedData.slice((params.page() - 1) * params.count(), params.page() * params.count()));
            });
        }
    });

    $scope.buildAfterEntitiesLoaded();

    $scope.delete = function () {
        if ($scope.ruleId) {
            $scope.alertError('No rule loaded to delete');
            return;
        }

        if ($scope.authorId) {
            $scope.alertError('No user authenticated');
            return;
        }
        queryBuilderService.deleteQuery($scope.ruleId, $scope.authorId).then(function (myData) {
            $scope.newRule();
            $scope.tableParams.reload();
        });
    };

    $scope.summaryDefaults = {description: null, title: ''};
    $scope.ruleId = null;
    $scope.saving = false;

    $scope.save = function () {
        var queryObject, query;
        if ($scope.saving) {
            return;
        }

        $scope.saving = true;
        $scope.title = $scope.title.trim();
        if (!$scope.title.length) {
            $scope.alertError('title can not be blank!');
            $scope.saving = false;
            return;
        }
        query = $scope.$builder.queryBuilder('getDrools');

        if (query === false) {
            $scope.saving = false;
            return;
        }

        queryObject = {
            id: $scope.ruleId,
            title: $scope.title,
            description: $scope.description || null,
            userId: $scope.authorId,
            query: query
        };

        queryBuilderService.saveQuery(queryObject).then(function (myData) {
            if (myData.status === 'FAILURE') {
                $scope.alertError(myData.message);
                $scope.saving = false;
                return;
            }
            $scope.tableParams.reload();
            $scope.showPencil(myData.result[0].id);
        });
    };

    $scope.serviceURLs = {
        FLIGHT: '/gtas/query/queryFlights/',
        TRAVELER: '/gtas/query/queryPassengers/'
    };

    $scope.viewType = null;
    $scope.viewTypeChange = function () {
        var baseUrl = $scope.serviceURLs[$scope.viewType],
            qbData = $scope.$builder.queryBuilder('getDrools');

        if (qbData === false) {
            $scope.alertError('Can not execute / invalid query');
            return;
        }
        queryService.executeQuery(baseUrl, qbData).then(function (myData) {
            var results = $scope.viewType.toLowerCase() + 'Results';

            if (myData.result === undefined) {
                $scope.alertError('Error!');
                return;
            }

            dataSource[$scope.viewType] =  myData.result;
            $scope[results].settings().$scope = $scope;
            $scope[results].reload();
        });
    };

    $scope.flightResults = new ngTableParams({
        page: 1,            // show first page
        count: 10           // count per page
    }, {
        total: dataSource.FLIGHT.length, // length of data
        getData: function ($defer, params) {
            var orderedFlightsData = params.sorting() ? $filter('orderBy')(dataSource.FLIGHT, params.orderBy()) : dataSource.FLIGHT;
            orderedFlightsData = params.filter() ? $filter('filter')(orderedFlightsData, params.filter()) : orderedFlightsData;
            params.total(orderedFlightsData.length);
            $defer.resolve(orderedFlightsData.slice((params.page() - 1) * params.count(), params.page() * params.count()));
        }
    });

    $scope.travelerResults = new ngTableParams({
        page: 1,            // show first page
        count: 10           // count per page
    }, {
        total: dataSource.TRAVELER.length, // length of data
        getData: function ($defer, params) {
            var orderedTravelersData = params.sorting() ? $filter('orderBy')(dataSource.TRAVELER, params.orderBy()) : dataSource.TRAVELER;
            orderedTravelersData = params.filter() ? $filter('filter')(orderedTravelersData, params.filter()) : orderedTravelersData;
            params.total(orderedTravelersData.length);
            $defer.resolve(orderedTravelersData.slice((params.page() - 1) * params.count(), params.page() * params.count()));
        }
    });
});
