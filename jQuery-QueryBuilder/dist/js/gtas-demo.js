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
var TABLE_COLUMNS_CONTAINER = document.createElement('div');

TABLE_COLUMNS_CONTAINER.innerHTML= ' \
  <label>Table: \
    <select class="table-name form-control"> \
    ' + getOptions(TABLE_NAMES) +' \
    </select></label> \
  <label>Column: <select class="table-corresponding-fields form-control"></select></label>';

// adds Table and Column dropdowns to DOM
var addTablesBeforeFields = function () {
  var currentRule = document.querySelector('.rule-container:last-child');
  var filters = currentRule.querySelector('.rule-filter-container');
  var container = TABLE_COLUMNS_CONTAINER.cloneNode(true);
  currentRule.insertBefore(container, filters);  
};
addTablesBeforeFields();

$('body')
  .on('change', '.table-name', function(e) {
      var $this = $(e.currentTarget);
      var fields = dbSchema[$this.val()];
      // find corresponding fields, both populate fields assign the table via data()
      $this.parents('.rule-container').find('.table-corresponding-fields').html(populateFieldOptions(fields)).data('table', $this.val());
  })
  .on('change', '.table-corresponding-fields', function(e) {
    var $this = $(e.currentTarget);
    var $rule = $this.parents('.rule-container');
      // automate selecting hidden jquery-queryBuilder filter list via TABLE.Column 
      $rule.find('.btn.dropdown-toggle').eq(0).click();
      $('.bootstrap-select.open').find('.dropdown-menu.inner').find('a').filter(function(){
        return this.childNodes[0].innerHTML === $this.data('table')+'.'+$this.find("option:selected").text();
      }).click();
  })
  .on('click', '[data-add="rule"], [data-add="group"]', function(){
    // add rule driving dropdowns (table, column) anytime rule or group added 
    setTimeout(addTablesBeforeFields, 50); 
  });
