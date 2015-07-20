app.controller('QueryBuilderController', function($scope, $filter, $q, ngTableParams, queryBuilderService) {
    'use strict';
    var datepickerOptions = {
        format: 'yyyy-mm-dd',
//        todayBtn: 'linked',
//        todayHighlight: true,
        autoClose: true
    };

    var datetimepickerOptions = {};
    $('.datepicker').datepicker(datepickerOptions);
    function execute_param(func) {
        func();
    }
    function getData (that, property) {
        if (localStorage[property] === undefined) {
            $.getJSON('./data/'+property+'.json', function(data) {
                localStorage[property] = JSON.stringify(data);
                data.forEach(function(item) {
                    that.addOption(item);
                });
            });
        }
        else {
            JSON.parse(localStorage[property]).forEach(function(item) {
                that.addOption(item);
            });
        }
    }
    var data = [];
    $scope.authorId = 'adelorie';
    $scope.$result = $('#result');
    var loadQueryBuilder = function () {
        var $builder = $('#builder');
        var Service = {
            RULE_BUILDER: "DROOLS",
            QUERY_BUILDER: "QueryBuilder"
        };

        // define filters
        var options = {
            allow_empty: true,
            service: Service.RULE_BUILDER,
            plugins: {
                'bt-tooltip-errors': { delay: 100},
                'sortable': null,
                'filter-description': { mode: 'bootbox' },
                'bt-selectpicker': null,
                'unique-filter': null,
                'bt-checkbox': { color: 'primary' }
            },
            /*
            plugins: [
                'sortable',
                'filter-description',
                'unique-filter',
                'bt-tooltip-errors',
                'bt-selectpicker',
                'bt-checkbox'
            ],
            */
            entities: {
                "Document": {
                    label: "DOCUMENT",
                    "columns": [{
                        id: "citizenship",
                        label: "Citizenship OR Issuance Country",
                        "type": "string"
                    }, {
                        id: "exp_date",
                        label: "Expiration Date",
                        "type": "date"
                    }, {
                        id: "issuance_country",
                        label: "Issuance Country",
                        "type": "string"
                    }, {
                        id: "doc_number",
                        label: "Number",
                        "type": "string"
                    }, {
                        id: "doc_type", label: "Type", "type": "string"
                    }]
                },
                "Flight": {
                    label: 'FLIGHT',
                    "columns": [{
                        id: "airport_destination",
                        label: "Airport - Destination",
                        "type": "string"
                    }, {
                        id: "airport_origin",
                        label: "Airport - Origin",
                        "type": "string"
                    }, {
                        id: "carrier",
                        label: "Carrier",
                        "type": "string"
                    }, {
                        id: "dest_country",
                        label: "Country - Destination",
                        "type": "string"
                    }, {
                        id: "origin_country",
                        label: "Country - Origin",
                        "type": "string"
                    }, {
                        id: "direction",
                        label: "Direction",
                        "type": "string"
                    }, {
                        id: "eta",
                        label: "ETA",
                        "type": "datetime"
                    }, {
                        id: "etd",
                        label: "ETD",
                        "type": "datetime"
                    }, {
                        id: "flightNumber",
                        label: "Number",
                        "type": "string"
                    }, {
                        id: "thru",
                        label: "Thru",
                        "type": "string"
                    }]
                },
                "Pax": {
                    label: "PASSENGER",
                    "columns": [{
                        id: "age", label: "Age", "type": "integer"
                    }, {
                        id: "citizenship_country", label: "Citizenship Country", "type": "string"
                    }, {
                        id: "debarkation", label: "Debarkation", "type": "string"
                    }, {
                        id: "debarkation_country", label: "Debarkation Country", "type": "string"
                    }, {
                        id: "dob", label: "DOB", "type": "date"
                    }, {
                        id: "embarkation", label: "Embarkation", "type": "string"
                    }, {
                        id: "embarkationDate", label: "Embarkation Date", "type": "date"
                    }, {
                        id: "embarkation_country", label: "Embarkation Country", "type": "string"
                    }, {
                        id: "gender", label: "Gender", "type": "string"
                    }, {
                        id: "first_name", label: "Name - First", "type": "string"
                    }, {
                        id: "last_name", label: "Name - Last", "type": "string"
                    }, {
                        id: "middle_name", label: "Name - Middle", "type": "string"
                    }, {
                        id: "residency_country", label: "Residency Country", "type": "string"
                    }, {
                        id: "seat", label: "Seat", "type": "string"
                    }, {
                        id: "type", label: "Type", "type": "string"
                    }]
                }
            },
            filters: [
                {
                    id: "Document.citizenship",
                    label: "Document.citizenship",
                    "type": "string",
                     operators: ['EQUAL', 'NOT_EQUAL', 'IN', 'NOT_IN']
                }, {
                    id: "Document.exp_date",
                    label: "Document.exp_date",
                    type: 'date',
                    validation: {
                        format: 'YYYY-MM-DD'
                    },
                    plugin: 'datepicker',
                    plugin_config: datepickerOptions
                }, {
                    id: "Document.issuance_country",
                    label: "Document.issuance_country",
                    "type": "string"
                }, {
                    id: "Document.doc_number",
                    label: "Document.doc_number",
                    "type": "string"
                }, {
                    id: "Document.doc_type",
                    label: "Document.doc_type",
                    "type": "string",
                    operators: ['EQUAL', 'NOT_EQUAL', 'IN', 'NOT_IN'],
                    input: 'select',
                    plugin: 'selectize',
                    plugin_config: {
                        valueField: 'id',
                        labelField: 'name',
                        searchField: 'name',
                        sortField: 'name',
                        create: true,
                        plugins: ['remove_button'],
                        onInitialize: function() {
                            getData(this, 'doc_types');
                        }
                    },
                    valueSetter: function(rule, value) {
                        rule.$el.find('.rule-value-container input')[0].selectize.setValue(value);
                    }

                }, {
                    id: "Flight.airport_destination",
                    label: "Flight.airport_destination",
                    "type": "string",
                    operators: ['EQUAL', 'NOT_EQUAL', 'IN', 'NOT_IN'],
                    input: 'select',
                    plugin: 'selectize',
                    plugin_config: {
                        valueField: 'id',
                        labelField: 'name',
                        searchField: 'name',
                        sortField: 'name',
                        create: true,
                        plugins: ['remove_button'],
                        onInitialize: function() {
                            getData(this, 'airports');
                        }
                    },
                    valueSetter: function(rule, value) {
                        rule.$el.find('.rule-value-container input')[0].selectize.setValue(value);
                    }
                }, {
                    id: "Flight.airport_origin",
                    label: "Flight.airport_origin",
                    "type": "string"
                }, {
                    id: "Flight.carrier",
                    label: "Flight.carrier",
                    "type": "string"
                }, {
                    id: "Flight.dest_country",
                    label: "Flight.dest_country",
                    "type": "string"
                }, {
                    id: "Flight.origin_country",
                    label: "Flight.origin_country",
                    "type": "string"
                }, {
                    id: "Flight.direction",
                    label: "Flight.direction",
                    "type": "string",
                    operators: ['EQUAL'],
                    input: 'select',
                    plugin: 'selectize',
                    plugin_config: {
                        valueField: 'id',
                        labelField: 'name',
                        searchField: 'name',
                        sortField: 'name',
                        create: true,
                        plugins: ['remove_button'],
                        onInitialize: function() {
                            getData(this, 'direction');
                        }
                    },
                    valueSetter: function(rule, value) {
                        rule.$el.find('.rule-value-container input')[0].selectize.setValue(value);
                    }
                }, {
                    id: "Flight.eta",
                    label: "Flight.eta",
                    "type": "datetime",
                    plugin: 'datetimepicker',
                    plugin_config: datetimepickerOptions
                }, {
                    id: "Flight.etd",
                    label: "Flight.etd",
                    "type": "datetime",
                    plugin: 'datetimepicker',
                    plugin_config: datetimepickerOptions
                }, {
                    id: "Flight.flightNumber",
                    label: "Flight.flightNumber",
                    "type": "string"
                }, {
                    id: "Flight.thru",
                    label: "Flight.thru",
                    "type": "string"
                }, {
                    id: "Pax.age",
                    label: "Pax.age",
                    "type": "integer"
                }, {
                    id: "Pax.citizenship_country",
                    label: "Pax.citizenship_country",
                    "type": "string"
                }, {
                    id: "Pax.debarkation",
                    label: "Pax.debarkation",
                    "type": "string"
                }, {
                    id: "Pax.debarkation_country",
                    label: "Pax.debarkation_country",
                    "type": "string"
                }, {
                    id: "Pax.dob",
                    label: "Pax.dob",
                    "type": 'date',
                    validation: {
                        format: 'YYYY-MM-DD'
                    },
                    plugin: 'datepicker',
                    plugin_config: datepickerOptions
                }, {
                    id: "Pax.embarkation",
                    label: "Pax.embarkation",
                    "type": "string"
                }, {
                    id: "Pax.embarkationDate",
                    label: "Pax.embarkationDate",
                    type: 'date',
                    validation: {
                        format: 'YYYY-MM-DD'
                    },
                    plugin: 'datepicker',
                    plugin_config: datepickerOptions
                }, {
                    id: "Pax.embarkation_country",
                    label: "Pax.embarkation_country",
                    "type": "string"
                }, {
                    id: "Pax.gender",
                    label: "Pax.gender",
                    "type": "string",
                    operators: ['EQUAL', 'NOT_EQUAL', 'IN', 'NOT_IN'],
                    input: 'select',
                    multiple: true,
                    plugin: 'selectize',
                    plugin_config: {
                        valueField: 'id',
                        labelField: 'name',
                        searchField: 'name',
                        sortField: 'name',
                        create: true,
                        plugins: ['remove_button'],
                        onInitialize: function() {
                            getData(this, 'genders');
                        }
                    },
                    valueSetter: function(rule, value) {
                        rule.$el.find('.rule-value-container input')[0].selectize.setValue(value);
                    }
                }, {
                    id: "Pax.first_name",
                    label: "Pax.first_name",
                    "type": "string"
                }, {
                    id: "Pax.last_name",
                    label: "Pax.last_name",
                    "type": "string"
                }, {
                    id: "Pax.middle_name",
                    label: "Pax.middle_name",
                    "type": "string"
                }, {
                    id: "Pax.residency_country",
                    label: "Pax.residency_country",
                    "type": "string"
                }, {
                    id: "Pax.seat",
                    label: "Pax.seat",
                    "type": "string"
                }, {
                    id: "Pax.type",
                    label: "Pax.type",
                    "type": "string"
                }
            ]
        };

        // init
        $builder.queryBuilder(options);

        $builder
            .on('afterCreateRuleInput.queryBuilder', function (e, rule) {
                if (rule.filter.plugin == 'selectize') {
                    rule.$el.find('.rule-value-container').css('min-width', '200px')
                        .find('.selectize-control').removeClass('form-control');
                }
            });

        return $builder;
    };
    $scope.isBeingEdited = function (ruleId) {
        return $scope.ruleId === ruleId;
    };
    $scope.resetQueryBuilder = function () {
        $scope.$builder.queryBuilder('reset');
        $scope.$result.addClass('hide').find('pre').empty();
    };
    $scope.loadRule = function () {
        //<i class="glyphicon glyphicon-pencil"></i>
        queryBuilderService.loadRuleById(this.summary.id).then(function (myData) {
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
            queryBuilderService.getList($scope.authorId).then(function (myData) {
                var filteredData, orderedData;
                data = [];

                if (myData.result === undefined || !Array.isArray(myData.result)) {
                    return;
                }

                myData.result.forEach(function (obj) {
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

    $scope.$builder = loadQueryBuilder();

    $scope.$on('handleBroadcast', function (event, id) {
        $scope.authorId = id;
        $scope.tableParams.reload();
        // $scope.$apply();
    });
    $scope.newRule = function () {
        $scope.ruleId = null;
        $scope.resetQueryBuilder();
        $scope.resetSummary();
    };

    $scope.delete = function () {
        queryBuilderService.deleteQuery($scope.ruleId, $scope.authorId).then(function (myData) {
            $scope.ruleId = null;
            $scope.resetQueryBuilder();
            $scope.resetSummary();
            $scope.tableParams.reload();
        });
    };

    $scope.loadSummary = function (summary) {
        $scope.title = summary.title;
        $scope.description = summary.description;
        $scope.startDate = summary.startDate;
        $scope.endDate = summary.endDate;
        $scope.enabled = summary.enabled;
    };

    $scope.today = moment().format('YYYY-MM-DD').toString();
    $scope.startDate = $scope.today.toString();

    $scope.resetSummary = function () {
        $scope.description = null;
        $scope.ruleId = null;
        $scope.title = '';
    };
    $scope.resetSummary();
    $scope.ruleId = null;

    $scope.submit = function() {
        var queryObject;
        $scope.title = $scope.title.trim();
        if (!$scope.title.length ) {
            alert('Risk Criteria title summary can not be blank!');
            return;
        }
        queryObject = {
            id: $scope.ruleId,
            title: $scope.title,
            description: $scope.description || null,
            userId: $scope.authorId,
            query: $scope.$builder.queryBuilder('saveRules')
        };

        queryBuilderService.saveQuery(queryObject).then(function (myData) {
            if (typeof myData.errorCode !== "undefined")
            {
                alert(myData.errorMessage);
                return;
            }
            $scope.tableParams.reload();
        });
    };
});
