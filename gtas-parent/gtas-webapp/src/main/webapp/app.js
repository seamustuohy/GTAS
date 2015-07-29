var app = angular.module('myApp', [
	'ui.router', 
	'ui.bootstrap', 
	'ngTable',
	'spring-security-csrf-token-interceptor'
]);

app.factory('QueryBuilderCtrl',function(){
    return function ($scope) {
        var valueSetter = function (rule, value) {
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
            if (localStorage[property] === undefined) {
                $.getJSON('./data/' + property + '.json', function (data) {
                    localStorage[property] = JSON.stringify(data);
                    try {
                        data.forEach(function (item) {
                            that.addOption(item);
                        });
                    }
                    catch (exception) {
                        throw exception;
                    }
                });
            } else {
                try {
                    JSON.parse(localStorage[property]).forEach(function (item) {
                        that.addOption(item);
                    });
                } catch (exception) {
                    throw exception;
                }
            }
        };
        $scope.today = moment().format('YYYY-MM-DD').toString();

        $scope.authorId = 'adelorie';
        $scope.$result = $('#result');
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
            filters: [{
                "id": "DOCUMENT.issuanceCountry.iso2",
                "label": "Citizenship OR Issuance Country",
                "type": "string",
                "operators": ["EQUAL", "NOT_EQUAL", "IN", "NOT_IN"],
                "input": "select",
                "multiple": true,
                "plugin": "selectize",
                "plugin_config": {
                    "valueField": "id",
                    "labelField": "name",
                    "searchField": "name",
                    "sortField": "name",
                    "create": true,
                    "plugins": ["remove_button"],
                    "onInitialize": function () {
                        getOptionsFromJSONArray(this, "countries");
                    }
                },
                "valueSetter": valueSetter
            }, {
                "id": "DOCUMENT.expirationDate",
                "label": "Expiration Date",
                "type": "date",
                "validation": {
                    "format": "YYYY-MM-DD"
                },
                "plugin": "datepicker",
                "plugin_config": {
                    "format": "yyyy-mm-dd",
                    "autoClose": true
                }
            }, {
                "id": "DOCUMENT.documentNumber",
                "label": "Number",
                "type": "string"
            }, {
                "id": "DOCUMENT.documentType",
                "label": "Type",
                "type": "string",
                "operators": ["EQUAL", "NOT_EQUAL", "IN", "NOT_IN"],
                "input": "select",
                "plugin": "selectize",
                "plugin_config": {
                    "valueField": "id",
                    "labelField": "name",
                    "searchField": "name",
                    "sortField": "name",
                    "create": true,
                    "plugins": ["remove_button"],
                    "onInitialize": function () {
                        getOptionsFromJSONArray(this, "doc_types");
                    }
                },
                "valueSetter": valueSetter
            }, {
                "id": "FLIGHT.destination.iata",
                "label": "Airport Destination",
                "type": "string",
                "operators": ["EQUAL", "NOT_EQUAL", "IN", "NOT_IN"],
                "input": "select",
                "plugin": "selectize",
                "plugin_config": {
                    "valueField": "id",
                    "labelField": "name",
                    "searchField": "name",
                    "sortField": "name",
                    "create": true,
                    "plugins": ["remove_button"],
                    "onInitialize": function () {
                        getOptionsFromJSONArray(this, "airports");
                    }
                },
                "valueSetter": valueSetter
            }, {
                "id": "FLIGHT.origin.iata",
                "label": "Airport Origin",
                "type": "string",
                "operators": ["EQUAL", "NOT_EQUAL", "IN", "NOT_IN"],
                "input": "select",
                "plugin": "selectize",
                "plugin_config": {
                    "valueField": "id",
                    "labelField": "name",
                    "searchField": "name",
                    "sortField": "name",
                    "create": true,
                    "plugins": ["remove_button"],
                    "onInitialize": function () {
                        getOptionsFromJSONArray(this, "airports");
                    }
                },
                "valueSetter": valueSetter
            }, {
                "id": "FLIGHT.carrier.iata",
                "label": "Carrier",
                "type": "string",
                "operators": ["EQUAL", "NOT_EQUAL", "IN", "NOT_IN"],
                "input": "select",
                "multiple": true,
                "plugin": "selectize",
                "plugin_config": {
                    "valueField": "id",
                    "labelField": "name",
                    "searchField": "name",
                    "sortField": "name",
                    "create": true,
                    "plugins": ["remove_button"],
                    "onInitialize": function () {
                        getOptionsFromJSONArray(this, "carriers");
                    }
                },
                "valueSetter": valueSetter
            }, {
                "id": "FLIGHT.destinationCountry.iso3",
                "label": "Destination Country",
                "type": "string",
                "operators": ["EQUAL", "NOT_EQUAL", "IN", "NOT_IN"],
                "input": "select",
                "multiple": true,
                "plugin": "selectize",
                "plugin_config": {
                    "valueField": "id",
                    "labelField": "name",
                    "searchField": "name",
                    "sortField": "name",
                    "create": true,
                    "plugins": ["remove_button"],
                    "onInitialize": function () {
                        getOptionsFromJSONArray(this, "countries");
                    }
                },
                "valueSetter": valueSetter
            }, {
                "id": "FLIGHT.originCountry.iso3",
                "label": "Origin Country",
                "type": "string",
                "operators": ["EQUAL", "NOT_EQUAL", "IN", "NOT_IN"],
                "input": "select",
                "multiple": true,
                "plugin": "selectize",
                "plugin_config": {
                    "valueField": "id",
                    "labelField": "name",
                    "searchField": "name",
                    "sortField": "name",
                    "create": true,
                    "plugins": ["remove_button"],
                    "onInitialize": function () {
                        getOptionsFromJSONArray(this, "countries");
                    }
                },
                "valueSetter": valueSetter
            }, {
                "id": "FLIGHT.direction",
                "label": "Direction",
                "type": "string",
                "operators": ["EQUAL"],
                "input": "select",
                "plugin": "selectize",
                "plugin_config": {
                    "valueField": "id",
                    "labelField": "name",
                    "searchField": "name",
                    "sortField": "name",
                    "create": true,
                    "plugins": ["remove_button"],
                    "onInitialize": function () {
                        getOptionsFromJSONArray(this, "direction");
                    }
                },
                "valueSetter": valueSetter
            }, {
                "id": "FLIGHT.eta",
                "label": "ETA",
                "type": "datetime",
                "plugin": "datetimepicker",
                "plugin_config": {}
            }, {
                "id": "FLIGHT.etd",
                "label": "ETD",
                "type": "datetime",
                "plugin": "datetimepicker",
                "plugin_config": {}
            }, {
                "id": "FLIGHT.flightNumber",
                "label": "Number",
                "type": "string"
            }, {
                "id": "FLIGHT.thru",
                "label": "Thru",
                "type": "string"
            }, {
                "id": "PAX.age",
                "label": "PAX.age",
                "type": "integer"
            }, {
                "id": "PAX.citizenshipCountry.iso2",
                "label": "Citizenship Country",
                "type": "string",
                "operators": ["EQUAL", "NOT_EQUAL", "IN", "NOT_IN"],
                "input": "select",
                "multiple": true,
                "plugin": "selectize",
                "plugin_config": {
                    "valueField": "id",
                    "labelField": "name",
                    "searchField": "name",
                    "sortField": "name",
                    "create": true,
                    "plugins": ["remove_button"],
                    "onInitialize": function () {
                        getOptionsFromJSONArray(this, "countries");
                    }
                },
                "valueSetter": valueSetter
            }, {
                "id": "PAX.debarkation.iata",
                "label": "Debarkation",
                "type": "string"
            }, {
                "id": "PAX.debarkCountry.iso2",
                "label": "Debarkation Country",
                "type": "string",
                "operators": ["EQUAL", "NOT_EQUAL", "IN", "NOT_IN"],
                "input": "select",
                "multiple": true,
                "plugin": "selectize",
                "plugin_config": {
                    "valueField": "id",
                    "labelField": "name",
                    "searchField": "name",
                    "sortField": "name",
                    "create": true,
                    "plugins": ["remove_button"],
                    "onInitialize": function () {
                        getOptionsFromJSONArray(this, "countries");
                    }
                },
                "valueSetter": valueSetter
            }, {
                "id": "PAX.dob",
                "label": "DOB",
                "type": "date",
                "validation": {
                    "format": "YYYY-MM-DD"
                },
                "plugin": "datepicker",
                "plugin_config": {
                    "format": "yyyy-mm-dd",
                    "autoClose": true
                }
            }, {
                "id": "PAX.embarkation.iata",
                "label": "Embarkation",
                "type": "string"
            }, {
                "id": "PAX.embarkCountry.iso2",
                "label": "Embarkation Country",
                "type": "string",
                "operators": ["EQUAL", "NOT_EQUAL", "IN", "NOT_IN"],
                "input": "select",
                "multiple": true,
                "plugin": "selectize",
                "plugin_config": {
                    "valueField": "id",
                    "labelField": "name",
                    "searchField": "name",
                    "sortField": "name",
                    "create": true,
                    "plugins": ["remove_button"],
                    "onInitialize": function () {
                        getOptionsFromJSONArray(this, "countries");
                    }
                },
                "valueSetter": valueSetter
            }, {
                "id": "PAX.gender",
                "label": "PAX.gender",
                "type": "string",
                "operators": ["EQUAL", "NOT_EQUAL", "IN", "NOT_IN"],
                "input": "select",
                "multiple": true,
                "plugin": "selectize",
                "plugin_config": {
                    "valueField": "id",
                    "labelField": "name",
                    "searchField": "name",
                    "sortField": "name",
                    "create": true,
                    "plugins": ["remove_button"],
                    "onInitialize": function () {
                        getOptionsFromJSONArray(this, "genders");
                    }
                },
                "valueSetter": valueSetter
            }, {
                "id": "PAX.firstName",
                "label": "First Name",
                "type": "string"
            }, {
                "id": "PAX.lastName",
                "label": "Last Name",
                "type": "string"
            }, {
                "id": "PAX.middleName",
                "label": "Middle Name",
                "type": "string"
            }, {
                "id": "PAX.residencyCountry.iso2",
                "label": "Residency Country",
                "type": "string",
                "operators": ["EQUAL", "NOT_EQUAL", "IN", "NOT_IN"],
                "input": "select",
                "multiple": true,
                "plugin": "selectize",
                "plugin_config": {
                    "valueField": "id",
                    "labelField": "name",
                    "searchField": "name",
                    "sortField": "name",
                    "create": true,
                    "plugins": ["remove_button"],
                    "onInitialize": function () {
                        getOptionsFromJSONArray(this, "countries");
                    }
                },
                "valueSetter": valueSetter
            }, {
                "id": "PAX.seat",
                "label": "PAX.seat",
                "type": "string"
            }, {
                "id": "PAX.traveler_type",
                "label": "Traveler Type",
                "type": "string"
            }]
        };
        $scope.buildAfterEntitiesLoaded = function () {
            var property = 'entities',
                $builder = $('#builder');

            // init
            $builder
                .on('afterCreateRuleInput.queryBuilder', function (e, rule) {
                    if (rule.filter.plugin == 'selectize') {
                        rule.$el.find('.rule-value-container').css('min-width', '200px')
                            .find('.selectize-control').removeClass('form-control');
                    }
                });
            try {
                if (localStorage[property] === undefined) {
                    $.getJSON('./data/' + property + '.json', function (data) {
                        localStorage[property] = JSON.stringify(data);
                        $scope.options.entities = data;
                        $builder.queryBuilder($scope.options);
                        $scope.$builder = $builder;
                    });
                } else {
                    $scope.options.entities = JSON.parse(localStorage[property]);
                    $builder.queryBuilder($scope.options);
                    $scope.$builder = $builder;
                }
            } catch (exception) {
                throw exception;
            }
        };
        $scope.isBeingEdited = function (ruleId) {
            return $scope.ruleId === ruleId;
        };
        $scope.resetQueryBuilder = function () {
            $scope.$builder.queryBuilder('reset');
            $scope.$result.addClass('hide').find('pre').empty();
        };

        $scope.loadSummary = function (summary) {
            Object.keys(summary).forEach( function (key) {
                $scope[key] = summary[key];
            });
        };

        $scope.formats =["YYYY-MM-DD"];

        $scope.newRule = function () {
            $scope.ruleId = null;
            $scope.resetQueryBuilder();
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
})
