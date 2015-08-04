var app = angular.module('myApp', [
    'ui.router',
    'ui.bootstrap',
    'ngTable',
    'spring-security-csrf-token-interceptor'
]);

app.factory('QueryBuilderCtrl', function () {
    return function ($scope, $timeout) {
        var selectizeValueSetter = function (rule, value) {
                var $selectize = rule.$el.find(".rule-value-container .selectized");
                if ($selectize.length) {
                    $timeout(function () {
                        $selectize[0].selectize.setValue(value);
                    }, 100);
                }
            },
            getOptionsFromJSONArray = function (that, property) {
                //if (localStorage[property] === undefined) {
                $.getJSON('./data/' + property + '.json', function (data) {
                    //localStorage[property] = JSON.stringify(data);
                    try {
                        data.forEach(function (item) {
                            that.addOption(item);
                        });
                    } catch (exception) {
                        throw exception;
                    }
                });
                //} else {
                //    try {
                //    JSON.parse(localStorage[property]).forEach(function (item) {
                //    that.addOption(item);
                //    });
                //    } catch (exception) {
                //       throw exception;
                //    }
                //}
            };

        $scope.today = moment().format('YYYY-MM-DD').toString();
        $scope.authorId = 'adelorie';
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
        $scope.buildAfterEntitiesLoaded = function () {
            var property = 'entities',
                $builder = $('#builder'),
                supplement = {
                    selectize: function (obj) {
                        obj.plugin_config = {
                            "valueField": "id",
                            "labelField": "name",
                            "searchField": "name",
                            "sortField": "name",
                            "create": false,
                            "plugins": ["remove_button"],
                            "onInitialize": function () {
                                getOptionsFromJSONArray(this, obj.dataSource);
                            }
                        };
                        obj.valueSetter = selectizeValueSetter;
                    },
                    datepicker: function (obj) {
                        obj.validation = { "format": "YYYY-MM-DD" };
                        obj.plugin_config = {
                            "format": "yyyy-mm-dd",
                            "autoClose": true
                        };
                    }
                };
            // init
            $builder
                .on('afterCreateRuleInput.queryBuilder', function (e, rule) {
                    if (rule.filter !== undefined && rule.filter.plugin === 'selectize') {
                        rule.$el.find('.rule-value-container').css('min-width', '200px')
                            .find('.selectize-control').removeClass('form-control');
                    }
                });

            try {
                //if (localStorage[property] === undefined) {
                $.getJSON('./data/' + property + '.json', function (data) {
                    //localStorage[property] = JSON.stringify(data);
                    $scope.options.entities = data;
                    $scope.options.filters = [];
                    Object.keys($scope.options.entities).forEach(function (key){
                        $scope.options.entities[key].columns.forEach(function (column){
                            switch (column.plugin) {
                                case 'selectize':
                                case 'datepicker':
                                    supplement[column.plugin](column);
                                    break;
                                default:
                                    break;
                            }
                            $scope.options.filters.push(column);
                        });
                    });

                    $builder.queryBuilder($scope.options);

                    $scope.$builder = $builder;
                });
                //} else {
                //    $scope.options.entities = JSON.parse(localStorage[property]);
                //    $builder.queryBuilder($scope.options);
                //    $scope.$builder = $builder;
                //}
            } catch (exception) {
                throw exception;
            }
        };

        $scope.isBeingEdited = function () {
            return $scope.ruleId === this.$data[this.$index].id;
        };

        $scope.loadSummary = function (summary) {
            Object.keys(summary).forEach(function (key) {
                $scope[key] = summary[key];
            });
        };

        $scope.formats = ["YYYY-MM-DD"];

        $scope.newRule = function () {
            $scope.ruleId = null;
            $scope.$builder.queryBuilder('reset');
            $scope.loadSummary($scope.summaryDefaults);
        };

        $scope.ruleId = null;
    };
});


app.config(function ($stateProvider){
    $stateProvider
        .state('flights', {
            url: '',
            templateUrl: 'flights/flights.html',
            controller: 'FlightsController'
        })
        .state('travelers', {
            url: '/travelers',
            templateUrl: 'pax/pax.html',
            controller: 'PaxController'
        })
        .state('pax', {
            url: '/pax',
            templateUrl: 'pax/pax.html',
            controller: 'PaxController'
        })
        .state('risk-criteria', {
            url: '/risk-criteria',
            templateUrl: 'risk-criteria/risk-criteria.html',
            controller: 'RiskCriteriaController'
        })
        .state('query-builder', {
            url: '/query-builder',
            templateUrl: 'query-builder/query.html',
            controller: 'QueryBuilderController'
        })
        .state('admin', {
            url: '/admin',
            templateUrl: 'admin/admin.html'
        });
});
