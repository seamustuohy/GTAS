app.controller('RiskCriteriaController', function($scope, $filter, $q, ngTableParams, riskCriteriaService) {
    var datepickerOptions = {
        format: 'yyyy-mm-dd',
        autoClose: true
    };

    $('.datepicker').datepicker(datepickerOptions);

    var valueSetter = function (rule, value) {
        var $input = rule.$el.find(".rule-value-container input");
        $input.focus();
        if (Array.isArray(value)) {
            value.forEach(function(v){
                $('[data-value="' + v + '"]').click();
            });
        } else {
            $('[data-value="' + value + '"]').click();
        }
        document.getElementById('title').focus();
        $input.blur();
    };

    function returnData (property) {
        try {
            if (localStorage[property] === undefined) {
                $.getJSON('./data/' + property + '.json', function (data) {
                    localStorage[property] = JSON.stringify(data);
                    return data;
                });
            } else {
                return JSON.parse(localStorage[property]);
            }
        } catch (exception) {
            throw exception;
        }
    }

    function getOptionsFromJSONArray (that, property) {
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
            entities: returnData('entities'),
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
        riskCriteriaService.ruleDelete($scope.ruleId, $scope.authorId).then(function (myData) {
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

    $scope.resetSummary = function () {
        $scope.ruleId = null;
        $scope.title = '';
        $scope.description = null;
        $scope.startDate = $scope.today;
        $scope.endDate = null;
        $scope.enabled = true;
    };
    $scope.resetSummary();
    $scope.formats =["YYYY-MM-DD"];

    $scope.submit = function() {
        var ruleObject;
        var startDate = moment($scope.startDate, $scope.formats, true);
        var endDate = $scope.endDate || moment($scope.endDate, $scope.formats, true);

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
            $scope.tableParams.reload();
        });
    };
});