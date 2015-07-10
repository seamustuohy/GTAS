var $builder = $('#builder');
var $result = $('#result');

var Service = {
  RULE_BUILDER: "DROOLS",
  QUERY_BUILDER: "QueryBuilder"
};

// define filters
var options = {
  allow_empty: true,
  service: Service.RULE_BUILDER,
  /*
  plugins: {
    'bt-tooltip-errors': { delay: 100},
    'sortable': null,
    'filter-description': { mode: 'bootbox' },
    'bt-selectpicker': null,
    'unique-filter': null,
    'bt-checkbox': { color: 'primary' }
  },
  */
   plugins: [
   'sortable',
   'filter-description',
   'unique-filter',
   'bt-tooltip-errors',
   'bt-selectpicker',
   'bt-checkbox'
   ],

   tables: {
    /*
    "ADDRESS":{
      "columns":[{
          "id":"city",
          "label":"City",
          "type":"string"
        }, {
          "id":"country",
          "label":"Country",
          "type":"string"
        },{
          "id":"line1",
          "label":"Line 1",
          "type":"string"
        },{
          "id":"line2",
          "label":"Line 2",
          "type":"string"
        },{
          "id":"line3",
          "label":"Line 3",
          "type":"string"
        },{
          "id":"postal_code",
          "label":"Postal Code",
          "type":"string"
        },{
          "id":"state",
          "label":"State/Province",
          "type":"string"
        }]
    },
    "API":{
      "columns":[{
          "id":"flight_direction",
          "label":"Flight Direction",
          "type":"string"
        }]
    },
    "CREDIT CARD":{
      "columns":[{
          "id":"cc_number",
          "label":"Number",
          "type":"string"
        }]
    },*/
    "DOCUMENT":{
      "columns":[{
          "id":"citizenship",
          "label":"Citizenship OR Issuance Country",
          "type":"string"
        },{
          "id":"exp_date",
          "label":"Expiration Date",
          "type":"string"
        },{
          "id":"issuance_country",
          "label":"Issuance Country",
          "type":"string"
        },{
          "id":"doc_number",
          "label":"Number",
          "type":"string"
        },{
          "id":"doc_type","label":"Type","type":"string"
        }]
    },
    /*
    "EMAIL":{
      "columns":[{
          "id":"email_address",
          "label":"Address",
          "type":"string"
        },{
          "id":"domain",
          "label":"Domain",
          "type":"string"
        }]
    },*/
    "FLIGHT":{
      "columns":[{
          "id":"airport_destination",
          "label":"Airport - Destination",
          "type":"string"
        },{
          "id":"airport_origin",
          "label":"Airport - Origin",
          "type":"string"
        },{
          "id":"carrier",
          "label":"Carrier",
          "type":"string"
        },{
          "id":"dest_country",
          "label":"Country - Destination",
          "type":"string"
        },{
          "id":"origin_country",
          "label":"Country - Origin",
          "type":"string"
        },{
          "id":"inbound",
          "label":"Direction - Inbound",
          "type":"string"
        },{
          "id":"outbound",
          "label":"Direction - Outbound",
          "type":"string"
        },{
          "id":"eta",
          "label":"ETA",
          "type":"datetime"
        },{
          "id":"etd",
          "label":"ETD",
          "type":"datetime"
        },{
          "id":"flightNumber",
          "label":"Number",
          "type":"string"
        },{
          "id":"thru",
          "label":"Thru",
          "type":"string"
        }]
    },
    /*
    "FREQUENT FLYER":{
      "columns":[{
          "id":"ff_airline",
          "label":"Airline",
          "type":"string"
        },{
          "id":"ff_number",
          "label":"Number",
          "type":"string"
        }]
    },
    "HITS":{
      "columns":[{
          "id":"has_hits",
          "label":"Has Hits",
          "type":"string"
       },{
          "id":"has_list_rule_hit",
          "label":"Has List Rule Hit",
          "type":"string"
        },{
          "id":"has_rule_hit",
          "label":"Has Rule Hit",
          "type":"string"
        },{
          "id":"master_list_id",
          "label":"List Rules - Master List Id",
          "type":"string"
        },{
          "id":"sub_list_id",
          "label":"Sub List Id",
          "type":"string"
        },{
          "id":"rule_id",
          "label":"Rules - Rule Id",
          "type":"string"
        }]
    },
    "NAME ORIGIN":{
      "columns":[{
          "id":"first_name",
          "label":"First Name",
          "type":"string"
        },{
          "id":"first_or_last_name",
          "label":"First or Last Name",
          "type":"string"
        },{
          "id":"last_name",
          "label":"Last Name",
          "type":"string"
        }]
    },*/
    "PASSENGER":{
      "columns":[{
          "id":"age","label":"Age","type":"integer"
        },{
          "id":"citizenship_country","label":"Citizenship Country","type":"string"
        },{
          "id":"debarkation","label":"Debarkation","type":"string"
        },{
          "id":"debarkation_country","label":"Debarkation Country","type":"string"
        },{
          "id":"dob","label":"DOB","type":"string"
        },{
          "id":"embarkation","label":"Embarkation","type":"string"
        },{
          "id":"embarkation_country","label":"Embarkation Country","type":"string"
        },{
          "id":"gender","label":"Gender","type":"string"
        },{
          "id":"first_name","label":"Name - First","type":"string"
        },{
          "id":"last_name","label":"Name - Last","type":"string"
        },{
          "id":"middle_name","label":"Name - Middle","type":"string"
        },{
          "id":"residency_country","label":"Residency Country","type":"string"
        },{
          "id":"seat","label":"Seat","type":"string"
        },{
          "id":"type","label":"Type","type":"string"
      }]
    }
    /*,

    "PHONE":{
      "columns":[{
          "id":"phone_number","label":"Number","type":"string"
      }]
    },
    "PNR":{
      "columns":[{
          "id":"bag_count","label":"Bag - Count","type":"integer"
        },{
          "id":"booking_date","label":"Booking Date","type":"string"
        },{
          "id":"carrier_code","label":"Carrier Code","type":"string"
        },{
          "id":"days_booked_before_trvl","label":"Days Booked Before Travel","type":"integer"
        },{
          "id":"dwell_airport","label":"Dwell - Airport","type":"string"
        },{
          "id":"dwell_country","label":"Dwell - Country","type":"string"
        },{
          "id":"dwell_duration","label":"Dwell - Duration","type":"integer"
        },{
          "id":"dwell_total_duration","label":"Dwell - Total Duration","type":"integer"
        },{
          "id":"form_of_payment","label":"Form of Payment","type":"string"
        },{
          "id":"first_name","label":"Name - First","type":"string"
        },{
          "id":"last_name","label":"Name - Last","type":"string"
        },{
          "id":"middle_name","label":"Name - Middle","type":"string"
        },{
          "id":"origin_airport","label":"Origin - Airport","type":"string"
        },{
          "id":"origin_country","label":"Origin - Country","type":"string"
        },{
          "id":"passenger_count","label":"Passenger Count","type":"integer"
        },{
          "id":"record_locator","label":"Record Locator","type":"string"
        },{
          "id":"route","label":"Route","type":"string"
        }]
    },
    "TRAVEL AGENCY":{
      "columns":[{
          "id":"city",
          "label":"City",
          "type":"string"},{"id":"name","label":"Name","type":"string"
        },{
          "id":"phone","label":"Phone","type":"string"
        }]
    }
    */
    },
  filters: [
/*
    {"id":"ADDRESS.city","label":"ADDRESS.city","type":"string"},{"id":"ADDRESS.country","label":"ADDRESS.country","type":"string"},{"id":"ADDRESS.line1","label":"ADDRESS.line1","type":"string"},{"id":"ADDRESS.line2","label":"ADDRESS.line2","type":"string"},{"id":"ADDRESS.line3","label":"ADDRESS.line3","type":"string"},{"id":"ADDRESS.postal_code","label":"ADDRESS.postal_code","type":"string"},{"id":"ADDRESS.state","label":"ADDRESS.state","type":"string"},
    {"id":"API.flight_direction","label":"API.flight_direction","type":"string"},
    {"id":"CREDIT CARD.cc_number","label":"CREDIT CARD.cc_number","type":"string"},
*/
    {"id":"DOCUMENT.citizenship","label":"DOCUMENT.citizenship","type":"string"},{"id":"DOCUMENT.exp_date","label":"DOCUMENT.exp_date","type":"string"},{"id":"DOCUMENT.issuance_country","label":"DOCUMENT.issuance_country","type":"string"},{"id":"DOCUMENT.doc_number","label":"DOCUMENT.doc_number","type":"string"},{"id":"DOCUMENT.doc_type","label":"DOCUMENT.doc_type","type":"string"},
/*
    {"id":"EMAIL.email_address","label":"EMAIL.email_address","type":"string"},{"id":"EMAIL.domain","label":"EMAIL.domain","type":"string"},
*/
    {"id":"FLIGHT.airport_destination","label":"FLIGHT.airport_destination","type":"string", input: 'select', multiple: true, plugin: 'selectize',
        plugin_config: {
            valueField: 'id',
            labelField: 'name',
            searchField: 'name',
            sortField: 'name',
            options: [
                { id: "IAD", name: "IAD" },
                { id: "JFK", name: "JFK" },
                { id: "SFO", name: "SFO" }
            ]
        },
        valueSetter: function(rule, value) {
            rule.$el.find('.rule-value-container select')[0].selectize.setValue(value);
        }
    },{"id":"FLIGHT.airport_origin","label":"FLIGHT.airport_origin","type":"string"},{"id":"FLIGHT.carrier","label":"FLIGHT.carrier","type":"string"},{"id":"FLIGHT.dest_country","label":"FLIGHT.dest_country","type":"string"},{"id":"FLIGHT.origin_country","label":"FLIGHT.origin_country","type":"string"},{"id":"FLIGHT.inbound","label":"FLIGHT.inbound","type":"string"},{"id":"FLIGHT.outbound","label":"FLIGHT.outbound","type":"string"},{"id":"FLIGHT.eta","label":"FLIGHT.eta","type":"datetime"},{"id":"FLIGHT.etd","label":"FLIGHT.etd","type":"datetime"},{"id":"FLIGHT.flightNumber","label":"FLIGHT.flightNumber","type":"string"},{"id":"FLIGHT.thru","label":"FLIGHT.thru","type":"string"},
/*
    {"id":"FREQUENT FLYER.ff_airline","label":"FREQUENT FLYER.ff_airline","type":"string"},{"id":"FREQUENT FLYER.ff_number","label":"FREQUENT FLYER.ff_number","type":"string"},
    {"id":"HITS.has_hits","label":"HITS.has_hits","type":"string"},{"id":"HITS.has_list_rule_hit","label":"HITS.has_list_rule_hit","type":"string"},{"id":"HITS.has_rule_hit","label":"HITS.has_rule_hit","type":"string"},{"id":"HITS.master_list_id","label":"HITS.master_list_id","type":"string"},{"id":"HITS.sub_list_id","label":"HITS.sub_list_id","type":"string"},{"id":"HITS.rule_id","label":"HITS.rule_id","type":"string"},
    {"id":"NAME ORIGIN.first_name","label":"NAME ORIGIN.first_name","type":"string"},{"id":"NAME ORIGIN.first_or_last_name","label":"NAME ORIGIN.first_or_last_name","type":"string"},{"id":"NAME ORIGIN.last_name","label":"NAME ORIGIN.last_name","type":"string"},
*/
    {"id":"PASSENGER.age","label":"PASSENGER.age","type":"integer"},{"id":"PASSENGER.citizenship_country","label":"PASSENGER.citizenship_country","type":"string"},{"id":"PASSENGER.debarkation","label":"PASSENGER.debarkation","type":"string"},{"id":"PASSENGER.debarkation_country","label":"PASSENGER.debarkation_country","type":"string"},{"id":"PASSENGER.dob","label":"PASSENGER.dob","type":"string"},{"id":"PASSENGER.embarkation","label":"PASSENGER.embarkation","type":"string"},{"id":"PASSENGER.embarkation_country","label":"PASSENGER.embarkation_country","type":"string"},{"id":"PASSENGER.gender","label":"PASSENGER.gender","type":"string"},{"id":"PASSENGER.first_name","label":"PASSENGER.first_name","type":"string"},{"id":"PASSENGER.last_name","label":"PASSENGER.last_name","type":"string"},{"id":"PASSENGER.middle_name","label":"PASSENGER.middle_name","type":"string"},{"id":"PASSENGER.residency_country","label":"PASSENGER.residency_country","type":"string"},{"id":"PASSENGER.seat","label":"PASSENGER.seat","type":"string"},{"id":"PASSENGER.type","label":"PASSENGER.type","type":"string"},
/*
    {"id":"PHONE.phone_number","label":"PHONE.phone_number","type":"string"},
    {"id":"PNR.bag_count","label":"PNR.bag_count","type":"integer"},{"id":"PNR.booking_date","label":"PNR.booking_date","type":"string"},{"id":"PNR.carrier_code","label":"PNR.carrier_code","type":"string"},{"id":"PNR.days_booked_before_trvl","label":"PNR.days_booked_before_trvl","type":"integer"},{"id":"PNR.dwell_airport","label":"PNR.dwell_airport","type":"string"},{"id":"PNR.dwell_country","label":"PNR.dwell_country","type":"string"},{"id":"PNR.dwell_duration","label":"PNR.dwell_duration","type":"integer"},{"id":"PNR.dwell_total_duration","label":"PNR.dwell_total_duration","type":"integer"},{"id":"PNR.form_of_payment","label":"PNR.form_of_payment","type":"string"},{"id":"PNR.first_name","label":"PNR.first_name","type":"string"},{"id":"PNR.last_name","label":"PNR.last_name","type":"string"},{"id":"PNR.middle_name","label":"PNR.middle_name","type":"string"},{"id":"PNR.origin_airport","label":"PNR.origin_airport","type":"string"},{"id":"PNR.origin_country","label":"PNR.origin_country","type":"string"},{"id":"PNR.passenger_count","label":"PNR.passenger_count","type":"integer"},{"id":"PNR.record_locator","label":"PNR.record_locator","type":"string"},{"id":"PNR.route","label":"PNR.route","type":"string"},
    {"id":"TRAVEL AGENCY.city","label":"TRAVEL AGENCY.city","type":"string"},{"id":"TRAVEL AGENCY.name","label":"TRAVEL AGENCY.name","type":"string"},{"id":"TRAVEL AGENCY.phone","label":"TRAVEL AGENCY.phone","type":"string"}
*/
  ]
};

// init
$builder.queryBuilder(options);

$builder.on('afterCreateRuleInput.queryBuilder', function(e, rule) {
    if (rule.filter.plugin == 'selectize') {
        rule.$el.find('.rule-value-container').css('min-width', '200px')
          .find('.selectize-control').removeClass('form-control');
    }
});

// expects array of strings
var getOptions = function (strings, selectedValue) {
  return '<option value="Empty / New">Empty / New</option>' + $.map(strings, function( val ) {
    return '<option value="'+ val +'" '+(selectedValue === val ? 'selected' : '')+'>' + val.split(' | ' )[0] + '</option>';
  }).join('');
};

Array.prototype.addUnique = function (name) {
  if (this.indexOf(name) < 0) {
    this.push(name);
    return true;
  }
  return false;
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

$savedQueryNamesList = $(document.querySelector('#saved-query-names'));
queryNameInput = document.querySelector('#query-name');

var queryName;
var convertToArray = function (string) {
  return string === null || string === "[]" ? [] : string.split(',');
};

var author = "Anthony";
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

updateSavedQueryNamesList();
var resetQueryBuilder = function() {
  $builder.queryBuilder('reset');
  $result.addClass('hide').find('pre').empty();
};

$(document)
    // PREP name to save as
  .on('change', '#query-name', function (e){
      queryName = e.currentTarget.value;
//      queryName = [e.currentTarget.value, author].join(' | ');
      console.log('queryName changed');
  })
    // LOAD query selected from saved queries
  .on('change', '#saved-query-names', function (e) {
      queryNameInput.value = $(e.currentTarget).val().split(' | ')[0];
      queryName = queryNameInput.value;
      $builder.queryBuilder('loadRules', queryName);
  })
    // RESET rules on UI
  .on('click', '.reset', resetQueryBuilder)
    // SAVE AS rules on UI
  .on('click', '.save-as', function () {
//      queryName = [queryName, author].join(' | ');
      if ($builder.queryBuilder('saveRules', queryName, true) ) {
        savedQueryNames.addUnique(queryName);
        updateSavedQueryNamesList();
      }
    })
  // Delete rules on UI
  .on('click', '.delete', function () {
//    queryName = [queryName, author].join(' | ');
    savedQueryNames.remove(queryName);
    $builder.queryBuilder('deleteRules', queryName, savedQueryNames);
    updateSavedQueryNamesList();
    resetQueryBuilder();
  });