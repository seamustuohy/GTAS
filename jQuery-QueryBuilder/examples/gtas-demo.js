var $builder = $('#builder');
var $result = $('#result');

var dbSchema = {
  'PASSENGER': [
    { id : 'cob', label: 'COB'}
  ],
  'FLIGHT': [
    { id : 'in_stock', label: 'HasHits' }     
  ]
};

// define filters
var options = {
  allow_empty: true,
  plugins: {
    'bt-tooltip-errors': { delay: 100},
    'sortable': null,
    'filter-description': { mode: 'bootbox' },
    'bt-selectpicker': null,
    'unique-filter': null,
    'bt-checkbox': { color: 'primary' }
  },
  tables: {
    'FLIGHT': {
      columns: [
        { id : 'in_stock', label: 'HasHits' }
      ]
    },
    'PASSENGER': {
      columns: [
        { id : 'cob', label: 'COB'}
      ]
    }
  },
  filters: [{
    id: 'PASSENGER.cob',
    label: 'PASSENGER.COB',
    'type': 'string',
    input: 'select',
    multiple: true,
    plugin: 'selectize',
    plugin_config: {
      valueField: 'id',
      labelField: 'name',
      searchField: 'name',
      sortField: 'name',
      options: [
        { id: "US", name: "UNITED STATES" },
        { id: "CH", name: "CHINA" },
        { id: "SA", name: "SAUDI ARABIA" }
      ]
    },
    valueSetter: function(rule, value) {
      rule.$el.find('.rule-value-container select')[0].selectize.setValue(value);
    }
  },{
    id: 'FLIGHT.in_stock',
    label: 'FLIGHT.HasHits',
    'type': 'integer',
    input: 'radio',
    values: {
      1: 'Yes',
      0: 'No'
    },
    operators: ['equal']
  }]
};

// init
$builder.queryBuilder(options);

$builder.on('afterCreateRuleInput.queryBuilder', function(e, rule) {
    if (rule.filter.plugin == 'selectize') {
        rule.$el.find('.rule-value-container').css('min-width', '200px')
          .find('.selectize-control').removeClass('form-control');
    }
});


// reset builder
$('.reset').on('click', function() {
  $builder.queryBuilder('reset');
  $result.addClass('hide').find('pre').empty();
});

// get rules
$('.parse-sql').on('click', function() {
  var res = $builder.queryBuilder('getSQL', $(this).data('stmt'), false);
  $result.removeClass('hide')
    .find('pre').html(
      res.sql + (res.params ? '\n\n' + JSON.stringify(res.params, undefined, 2) : '')
    );
});

// expects array of strings
var getOptions = function (strings) {
  return '<option value="-1">-</option>' + $.map(strings, function( val ) {
    return '<option value="'+ val +'">' + val + '</option>';
  }).join('');
};

// expects array of objects {id: '', label: ''}
var populateFieldOptions = function (objects) {
  return '<option value="-1">-</option>' + $.map(objects, function( val ) {
    return '<option value="'+ val.id +'">' + val.label + '</option>';
  }).join('');
};

// CONSTS
var TABLE_NAMES = Object.keys(dbSchema);


// $('.set').on('click', function() {
//   $builder.queryBuilder('setRules', {
//     condition: 'AND',
//     rules: [{
//       id: 'price',
//       operator: 'between',
//       value: [10.25, 15.52],
//       flags: {
//         no_delete: true,
//         filter_readonly: true
//       },
//       data: {
//         unit: '€'
//       }
//     }, {
//       id: 'state',
//       operator: 'equal',
//       value: 'AK',
//     }, {
//       condition: 'OR',
//       rules: [{
//         id: 'category',
//         operator: 'equal',
//         value: 2
//       }, {
//         id: 'coord',
//         operator: 'equal',
//         value: 'B.3'
//       }]
//     }]
//   });
// });

$('.set').on('click', function() {
  $builder.queryBuilder('setRules', {
    condition: 'OR',
    rules: [{
      id: 'PASSENGER.cob',
      operator: 'equal',
      value: 'CH',
      options: [
        { id: "US", name: "UNITED STATES" },
        { id: "CH", name: "CHINA" },
        { id: "SA", name: "SAUDI ARABIA" }
      ]
    }, {
      id: 'PASSENGER.cob',
      operator: 'equal',
      value: 'US',
      options: [
        { id: "US", name: "UNITED STATES" },
        { id: "CH", name: "CHINA" },
        { id: "SA", name: "SAUDI ARABIA" }
      ]
    }]
  });
});