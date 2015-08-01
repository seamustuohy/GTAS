var app = angular.module('myApp', [
    'ui.router',
    'ui.bootstrap',
    'ngTable',
    'spring-security-csrf-token-interceptor'
]);

app.factory('QueryBuilderCtrl',function(){
    return function ($scope) {
        var valueSetter = function (rule, value) {
            debugger;
            var $input = rule.$el.find(".rule-value-container input");
            $input.focus();
            if (Array.isArray(value)) {
                value.forEach(function (v) {
                    $('[data-value="' + v + '"]').click();
                });
            } else {
                $('[data-value="' + value + '"]').click();
            }
            document.getElementById('title').focus();
            $input.blur();
        };
        var getOptionsFromJSONArray = function (that, property) {
            //if (localStorage[property] === undefined) {
            $.getJSON('./data/' + property + '.json', function (data) {
                //localStorage[property] = JSON.stringify(data);
                try {
                    data.forEach(function (item) {
                        that.addOption(item);
                    });
                }
                catch (exception) {
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
                $builder = $('#builder');

            var supplementSelectize = function(obj) {
                obj.multiple = true;
                obj.plugin = "selectize";
                obj.plugin_config = {
                    "valueField": "id",
                    "labelField": "name",
                    "searchField": "name",
                    "sortField": "name",
                    "create": true,
                    "plugins": ["remove_button"],
                    "onInitialize": function () {
                        getOptionsFromJSONArray(this, obj.data);
                    }
                };
                obj.valueSetter = valueSetter;
            };
            // init
            $builder
                .on('afterCreateRuleInput.queryBuilder', function (e, rule) {
                    if (rule.filter.plugin == 'selectize') {
                        rule.$el.find('.rule-value-container').css('min-width', '200px')
                            .find('.selectize-control').removeClass('form-control');
                    }
                })
                .on('onAfterSetValue.queryBuilder', function () {alert('test')});
            try {
                //if (localStorage[property] === undefined) {
                $.getJSON('./data/' + property + '.json', function (data) {
                    //localStorage[property] = JSON.stringify(data);
                    $scope.options.entities = data;
                    $scope.options.filters = [];
                    Object.keys($scope.options.entities).forEach(function(key){
                        $scope.options.entities[key].columns.forEach(function(column){
                            $scope.options.filters.push(column);
                        });
                    })

                    //TODO load options from entities
                    //DOCUMENT.documentType, doc_types
                    //FLIGHT.destination.iata airports
                    //FLIGHT.carrier.iata carriers
                    //FLIGHT.direction direction
                    //TRAVELER.gender genders


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
        $scope.isBeingEdited = function (ruleId) {
            return $scope.ruleId === ruleId;
        };


        $scope.loadSummary = function (summary) {
            Object.keys(summary).forEach( function (key) {
                $scope[key] = summary[key];
            });
        };

        $scope.formats =["YYYY-MM-DD"];

        $scope.newRule = function () {
            $scope.ruleId = null;
            $scope.$builder.queryBuilder('reset');
            $scope.loadSummary($scope.summaryDefaults);
        };
    };
});


app.config(function($stateProvider){
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
        })
});
