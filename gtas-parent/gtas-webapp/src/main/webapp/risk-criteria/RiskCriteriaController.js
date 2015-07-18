app.controller('QueryController', function($scope, $filter, $q, ngTableParams, queryService) {
    var datepickerOptions = {
        format: 'yyyy-mm-dd',
        todayBtn: 'linked',
        todayHighlight: true,
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
                    "label": "DOCUMENT",
                    "columns": [{
                        "id": "citizenship",
                        "label": "Citizenship OR Issuance Country",
                        "type": "string"
                    }, {
                        "id": "exp_date",
                        "label": "Expiration Date",
                        "type": "date"
                    }, {
                        "id": "issuance_country",
                        "label": "Issuance Country",
                        "type": "string"
                    }, {
                        "id": "doc_number",
                        "label": "Number",
                        "type": "string"
                    }, {
                        "id": "doc_type", "label": "Type", "type": "string"
                    }]
                },
                "Flight": {
                    "label": 'FLIGHT',
                    "columns": [{
                        "id": "airport_destination",
                        "label": "Airport - Destination",
                        "type": "string"
                    }, {
                        "id": "airport_origin",
                        "label": "Airport - Origin",
                        "type": "string"
                    }, {
                        "id": "carrier",
                        "label": "Carrier",
                        "type": "string"
                    }, {
                        "id": "dest_country",
                        "label": "Country - Destination",
                        "type": "string"
                    }, {
                        "id": "origin_country",
                        "label": "Country - Origin",
                        "type": "string"
                    }, {
                        "id": "direction",
                        "label": "Direction",
                        "type": "string"
                    }, {
                        "id": "eta",
                        "label": "ETA",
                        "type": "datetime"
                    }, {
                        "id": "etd",
                        "label": "ETD",
                        "type": "datetime"
                    }, {
                        "id": "flightNumber",
                        "label": "Number",
                        "type": "string"
                    }, {
                        "id": "thru",
                        "label": "Thru",
                        "type": "string"
                    }]
                },
                "Pax": {
                    "label": "PASSENGER",
                    "columns": [{
                        "id": "age", "label": "Age", "type": "integer"
                    }, {
                        "id": "citizenship_country", "label": "Citizenship Country", "type": "string"
                    }, {
                        "id": "debarkation", "label": "Debarkation", "type": "string"
                    }, {
                        "id": "debarkation_country", "label": "Debarkation Country", "type": "string"
                    }, {
                        "id": "dob", "label": "DOB", "type": "date"
                    }, {
                        "id": "embarkation", "label": "Embarkation", "type": "string"
                    }, {
                        "id": "embarkationDate", "label": "Embarkation Date", "type": "date"
                    }, {
                        "id": "embarkation_country", "label": "Embarkation Country", "type": "string"
                    }, {
                        "id": "gender", "label": "Gender", "type": "string"
                    }, {
                        "id": "first_name", "label": "Name - First", "type": "string"
                    }, {
                        "id": "last_name", "label": "Name - Last", "type": "string"
                    }, {
                        "id": "middle_name", "label": "Name - Middle", "type": "string"
                    }, {
                        "id": "residency_country", "label": "Residency Country", "type": "string"
                    }, {
                        "id": "seat", "label": "Seat", "type": "string"
                    }, {
                        "id": "type", "label": "Type", "type": "string"
                    }]
                }
            },
            filters: [
                {
                    "id": "Document.citizenship",
                    "label": "Document.citizenship",
                    "type": "string",
                     operators: ['EQUAL', 'NOT_EQUAL', 'IN', 'NOT_IN']
                }, {
                    "id": "Document.exp_date",
                    "label": "Document.exp_date",
                    type: 'date',
                    validation: {
                        format: 'YYYY-MM-DD'
                    },
                    plugin: 'datepicker',
                    plugin_config: datepickerOptions
                }, {
                    "id": "Document.issuance_country",
                    "label": "Document.issuance_country",
                    "type": "string"
                }, {
                    "id": "Document.doc_number",
                    "label": "Document.doc_number",
                    "type": "string"
                }, {
                    "id": "Document.doc_type",
                    "label": "Document.doc_type",
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
                    "id": "Flight.airport_destination",
                    "label": "Flight.airport_destination",
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
                    "id": "Flight.airport_origin",
                    "label": "Flight.airport_origin",
                    "type": "string"
                }, {
                    "id": "Flight.carrier",
                    "label": "Flight.carrier",
                    "type": "string"
                }, {
                    "id": "Flight.dest_country",
                    "label": "Flight.dest_country",
                    "type": "string"
                }, {
                    "id": "Flight.origin_country",
                    "label": "Flight.origin_country",
                    "type": "string"
                }, {
                    "id": "Flight.direction",
                    "label": "Flight.direction",
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
                    "id": "Flight.eta",
                    "label": "Flight.eta",
                    "type": "datetime",
                    operators: ['BETWEEN', 'EQUAL', 'GREATER_OR_EQUAL', 'LESSER_OR_EQUAL'],
                    plugin: 'datetimepicker',
                    plugin_config: datetimepickerOptions
                }, {
                    "id": "Flight.etd",
                    "label": "Flight.etd",
                    "type": "datetime",
                    operators: ['BETWEEN', 'EQUAL', 'GREATER_OR_EQUAL', 'LESSER_OR_EQUAL'],
                    plugin: 'datetimepicker',
                    plugin_config: datetimepickerOptions
                }, {
                    "id": "Flight.flightNumber",
                    "label": "Flight.flightNumber",
                    "type": "string",
                    operators: ['EQUAL', 'IN']
                }, {
                    "id": "Flight.thru",
                    "label": "Flight.thru",
                    "type": "string"
                }, {`
                    "id": "Pax.age",
                    "label": "Pax.age",
                    "type": "integer",
                    operators: ['BETWEEN', 'EQUAL', 'GREATER_OR_EQUAL', 'LESSER_OR_EQUAL']
                }, {
                    "id": "Pax.citizenship_country",
                    "label": "Pax.citizenship_country",
                    "type": "string"
                }, {
                    "id": "Pax.debarkation",
                    "label": "Pax.debarkation",
                    "type": "string"
                }, {
                    "id": "Pax.debarkation_country",
                    "label": "Pax.debarkation_country",
                    "type": "string"
                }, {
                    "id": "Pax.dob",
                    "label": "Pax.dob",
                    "type": 'date',
                    validation: {
                        format: 'YYYY-MM-DD'
                    },
                    plugin: 'datepicker',
                    plugin_config: datepickerOptions
                }, {
                    "id": "Pax.embarkation",
                    "label": "Pax.embarkation",
                    "type": "string"
                }, {
                    "id": "Pax.embarkationDate",
                    "label": "Pax.embarkationDate",
                    type: 'date',
                    validation: {
                        format: 'YYYY-MM-DD'
                    },
                    plugin: 'datepicker',
                    plugin_config: datepickerOptions
                }, {
                    "id": "Pax.embarkation_country",
                    "label": "Pax.embarkation_country",
                    "type": "string"
                }, {
                    "id": "Pax.gender",
                    "label": "Pax.gender",
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
                    "id": "Pax.first_name",
                    "label": "Pax.first_name",
                    "type": "string"
                }, {
                    "id": "Pax.last_name",
                    "label": "Pax.last_name",
                    "type": "string"
                }, {
                    "id": "Pax.middle_name",
                    "label": "Pax.middle_name",
                    "type": "string"
                }, {
                    "id": "Pax.residency_country",
                    "label": "Pax.residency_country",
                    "type": "string"
                }, {
                    "id": "Pax.seat",
                    "label": "Pax.seat",
                    "type": "string"
                }, {
                    "id": "Pax.type",
                    "label": "Pax.type",
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
        // expects array of strings
        var getOptions = function (strings, selectedValue) {
            return '<option value="Empty / New">Empty / New</option>' + $.map(strings, function (val) {
                    return '<option value="' + val + '" ' + (selectedValue === val ? 'selected' : '') + '>' + val.split(' | ')[0] + '</option>';
                }).join('');
        };

        Array.prototype.addUnique = function (name) {
            if (this.indexOf(name) < 0) {
                this.push(name);
                return true;
            }
            return false;
        };

        Array.prototype.remove = function () {
            var what, a = arguments, L = a.length, ax;
            while (L && this.length) {
                what = a[--L];
                while ((ax = this.indexOf(what)) !== -1) {
                    this.splice(ax, 1);
                }
            }
            return this;
        };

        $savedQueryNamesList = $(document.querySelector('#saved-query-names'));
        queryNameInput = document.querySelector('#query-name');

        var queryName;
        var convertToArray = function (string) {
            return string === null || string === "[]" ? [] : string.split(',');
        };

        var savedQueryNames = convertToArray(localStorage.getItem('savedQueryNames'));
        var updateSavedQueryNamesList = function () {
            if (savedQueryNames.length) {
                $savedQueryNamesList.html(getOptions(savedQueryNames, queryName)).selectpicker('refresh');
                localStorage.setItem('savedQueryNames', savedQueryNames);
                $savedQueryNamesList.parent().removeClass('hide');
            } else {
                $savedQueryNamesList.parent().addClass('hide');
            }
            queryName = null;
            queryNameInput.value = '';
        };

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
        queryService.loadRuleById(this.summary.id).then(function (myData) {
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
            queryService.getList($scope.authorId).then(function (myData) {
                data = [];
                myData.forEach(function (obj) {
                    // add id to summary obj
                    obj.summary.id = obj.id;
                    // add summary obj to data array
                    data.push(obj.summary);
                });
                //vm.tableParams.total(result.total);
                // use build-in angular filter
                var filteredData = params.filter() ?
                    $filter('filter')(data, params.filter()) :
                    data;
                var orderedData = params.sorting() ?
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

    $scope.deleteRule = function () {
        var msg = $scope.title + ' | #' + $scope.ruleId;
        queryService.ruleDelete($scope.ruleId, $scope.authorId).then(function (myData) {
            $scope.ruleId = null;
            $scope.resetQueryBuilder();
            $scope.resetSummary();

            //TODO: SHOULD REMOVE FROM DATA AND REFRESH ng-table
            alert('successfully deleted: ' + msg);
            //window.location.reload();
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
        $scope.title = '';
        $scope.description = null;
        $scope.startDate = $scope.today;
        $scope.endDate = null;
        $scope.enabled = true;
    };
    $scope.resetSummary();
    $scope.enabled = true;
    $scope.ruleId = null;
    $scope.formats =["YYYY-MM-DD"];

    $scope.submit = function() {
        var summary;
        var startDate = moment($scope.startDate, $scope.formats, true);
        var endDate = $scope.endDate || moment($scope.endDate, $scope.formats, true);

        var ruleObject = {
            id: $scope.ruleId,
            details: $scope.$builder.queryBuilder('saveRules')
        };
        $scope.title = $scope.title.trim();
        if (!$scope.title.length ) {
            alert('Risk Criteria title summary can not be blank!');
            return;
        }

        /* was told startDate ignored on updates so only matters on new rules */
        if ($scope.ruleId === null) {
            if (!startDate.isValid())
            {
                alert('Dates must be in this format: ' + $scope.formats.toString());
                return;
            }
            if (startDate < $scope.today ) {
                alert('Risk Criteria start date must be today or later when created new.');
                return;
            }
        }

        if ($scope.endDate !== null) {
            if (!endDate.isValid() ) {
                alert('End Date must be empty/open or in this format: ' + $scope.formats.toString());
                return;
            }
            if (endDate < startDate ) {
                alert('End Date must be empty/open or be >= startDate: ' + $scope.formats.toString());
                return;
            }
        }

        summary = {
            title: $scope.title,
            description: $scope.description || null,
            startDate: $scope.startDate,
            endDate: $scope.endDate || null,
            enabled: $scope.enabled
        };
        data.push(summary);
        ruleObject.summary = summary;
        $scope.tableData = data;

        $scope.tableParams.total($scope.tableData.length);
        $scope.tableParams.reload();

        queryService.ruleSave(ruleObject, $scope.authorId).then(function (myData) {
            if (typeof myData.errorCode !== "undefined")
            {
                alert(myData.errorMessage);
                return;
            }
            $scope.tableParams.reload();
        });
    };
});