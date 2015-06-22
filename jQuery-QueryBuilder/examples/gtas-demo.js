var $builder = $('#builder');
var $result = $('#result');

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


// get rules Lola says she doesn't need SQL now
//$('.parse-sql').on('click', function() {
//  var res = $builder.queryBuilder('getSQL', $(this).data('stmt'), false);
//  $result.removeClass('hide')
//    .find('pre').html(
//      res.sql + (res.params ? '\n\n' + JSON.stringify(res.params, undefined, 2) : '')
//    );
//});

// expects array of strings
var getOptions = function (strings, selectedValue) {
  return '<option value="-1">-</option>' + $.map(strings, function( val ) {
    return '<option value="'+ val +'" '+(selectedValue === val ? 'selected' : '')+'>' + val + '</option>';
  }).join('');
};

Array.prototype.addUnique = function (name) {
  if (this.indexOf(name) < 0) { this.push(name); }
  return this;
};

Array.prototype.remove = function() {
  var what, a = arguments, L = a.length, ax;
  while (L && this.length) {
    what = a[--L];
    while ((ax = this.indexOf(what)) !== -1) {
      this.splice(ax, 1);
    }
  }
  return this;
};


var data = {
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
};

localStorage.setItem('sample', JSON.stringify(data));

savedQueryNamesList = document.querySelector('#saved-query-names');
queryNameInput = document.querySelector('#query-name');

var queryName;
var savedQueryNames = localStorage.getItem('savedQueryNames');
savedQueryNames = savedQueryNames ? savedQueryNames.split(',') : ['sample'];

var updateSavedQueryNamesList = function () {
  savedQueryNamesList.innerHTML = getOptions(savedQueryNames, queryName);
  $(savedQueryNamesList).selectpicker('refresh');
  localStorage.setItem('savedQueryNames', savedQueryNames);
  queryName = null;
  queryNameInput.value = '';
};

updateSavedQueryNamesList();
var resetQueryBuilder = function() {
  $builder.queryBuilder('reset');
  $result.addClass('hide').find('pre').empty();
};

$(document)
    // PREP name to save as
  .on('change', '#query-name', function (e){
    queryName = e.currentTarget.value;
  })
    // LOAD query selected from saved queries
  .on('change', '#saved-query-names', function (e) {
    queryName = $(e.currentTarget).val();
    queryNameInput.value = queryName;
    $builder.queryBuilder('loadRules', queryName);
  })
    // RESET rules on UI
  .on('click', '.reset', resetQueryBuilder)
    // SAVE AS rules on UI
  .on('click', '.save-as', function () {
      savedQueryNames.addUnique(queryName);
      $builder.queryBuilder('saveRules', queryName, true);
      updateSavedQueryNamesList();
    })
  // Delete rules on UI
  .on('click', '.delete', function () {
    $builder.queryBuilder('deleteRules', queryName);
    savedQueryNames.remove(queryName);
    updateSavedQueryNamesList();
    resetQueryBuilder();
  });