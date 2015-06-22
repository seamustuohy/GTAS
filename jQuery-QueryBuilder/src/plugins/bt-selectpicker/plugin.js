/*!
 * jQuery QueryBuilder Bootstrap Selectpicker
 * Applies Bootstrap Select on filters and operators combo-boxes.
 * Copyright 2014-2015 Damien "Mistic" Sorel (http://www.strangeplanet.fr)
 */

QueryBuilder.define('bt-selectpicker', function(options) {
    if (!$.fn.selectpicker || !$.fn.selectpicker.Constructor) {
        error('Bootstrap Select is required to use "bt-selectpicker" plugin. Get it here: http://silviomoreto.github.io/bootstrap-select');
    }

    // init selectpicker
    this.on('afterCreateTables', function(e, rule) {
        rule.$el.find('.rule-table-container select').removeClass('form-control').selectpicker(options);
    });

    this.on('afterCreateTableFields', function(e, rule) {
        rule.$el.find('.rule-field-container select').removeClass('form-control').selectpicker(options);
    });

    //this.on('afterUpdateQueryNamesList', function(e, queryName) {
    //    if (this.savedQueryNames === undefined) {
    //        this.savedQueryNames = $('#saved-query-names');
    //    }
    //    this.savedQueryNames.selectpicker('refresh');
    //    if (queryName) {
    //        this.savedQueryNames.selectpicker('val', queryName);
    //    }
    //});

    this.on('afterCreateRuleFilters', function(e, rule) {
        rule.$el.find('.rule-filter-container select').removeClass('form-control').selectpicker(options);
    });

    this.on('afterCreateRuleOperators', function(e, rule) {
        rule.$el.find('.rule-operator-container select').removeClass('form-control').selectpicker(options);
    });

    // update selectpicker on change
    this.on('afterUpdateRuleTable', function(e, rule) {
        rule.$el.find('.rule-table-container select').selectpicker('render');
    });

    this.on('afterUpdateRuleColumn', function(e, rule) {
        rule.$el.find('.rule-field-container select').selectpicker('render');
    });

    this.on('afterUpdateRuleFilter', function(e, rule) {
        rule.$el.find('.rule-filter-container select').selectpicker('render');
    });

    this.on('afterUpdateRuleOperator', function(e, rule) {
        rule.$el.find('.rule-operator-container select').selectpicker('render');
    });
}, {
    container: 'body',
    style: 'btn-inverse btn-xs',
    width: 'auto',
    showIcon: false
});