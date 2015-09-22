app.factory('jQueryBuilderFactory', function () {
    'use strict';
    return function ($scope, $timeout) {
        var setSelectizeValue = function ($selectize, value) {
                $selectize[0].selectize.setValue(value);
                $timeout(function () {
                    if ($selectize[0].selectize.getValue().length === 0) setSelectizeValue($selectize, value);
                },50);
            },
            selectizeValueSetter = function (rule, value) {
                rule.$el.find(".rule-value-container select").val(value);
                var $selectize = rule.$el.find(".rule-value-container .selectized");

                if ($selectize.length) setSelectizeValue($selectize, value);
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

        $scope.buildAfterEntitiesLoaded = function (options) {
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
            if ($builder.length === 0) {
                alert('#builder not found in the DOM!');
            }
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
                    if (options && options.deleteEntity) {
                        data[options.deleteEntity] = null;
                        delete data[options.deleteEntity];
                    }
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
    };
});
