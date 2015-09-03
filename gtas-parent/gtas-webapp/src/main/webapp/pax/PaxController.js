app.controller('PaxController', function ($scope, $rootScope, $injector, GridControl, jQueryBuilderFactory, $filter, 
											$q, paxService, sharedPaxData, riskCriteriaService, $stateParams, $state) {
    var self = this;
    $injector.invoke(jQueryBuilderFactory, this, {$scope: $scope});
    $injector.invoke(GridControl, this, {$scope: $scope});

    $scope.isExpanded = true;
    $scope.paxHitList = [];
    $scope.list = sharedPaxData.list;
    $scope.add = sharedPaxData.add;
    $scope.getAll = sharedPaxData.getAll;

    $scope.getPaxSpecificList = function (index) {
        return $scope.list(index);
    };

      //--------Date functions-----------------------

    
	  $scope.today = function() {
		    $scope.dt = $filter('date')(new Date(), 'MM/dd/yyyy');
		    $scope.dt3 = new Date();
		    $scope.dt3.setDate((new Date()).getDate()+3);
//		    $scope.dt3 = $filter('date')(((new Date())+3), 'MM/dd/yyyy');
		  };
		  $scope.today();

		  $scope.clear = function () {
		    $scope.dt = null;
		  };

		  // Disable weekend selection
		  $scope.disabled = function(date, mode) {
		    return ( mode === 'day' && ( date.getDay() === 0 || date.getDay() === 6 ) );
		  };

		  $scope.toggleMin = function() {
		    $scope.minDate = $scope.minDate ? null : new Date();
		  };
		  $scope.toggleMin();

		  $scope.opendt = function($event) {
			    $scope.status.openeddt = true;
			    $('.ng-valid.dropdown-menu').eq(0).css({display: 'block'});
			  };
			  
		  $scope.opendt3 = function($event) {
		    $scope.status.openeddt3 = true;
		    $('.ng-valid.dropdown-menu').eq(1).css({display: 'block'});
		  };

		  $scope.dateOptions = {
		    formatYear: 'yy',
		    startingDay: 1
		  };

		  $scope.formats = ['MM/dd/yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate'];
		  $scope.format = $scope.formats[0];

		  $scope.status = {
		    openeddt: false,
		    openeddt3: false
		  };

		  var tomorrow = new Date();
		  tomorrow.setDate(tomorrow.getDate() + 1);
		  var afterTomorrow = new Date();
		  afterTomorrow.setDate(tomorrow.getDate() + 2);
		  $scope.events =
		    [
		      {
		        date: tomorrow,
		        status: 'full'
		      },
		      {
		        date: afterTomorrow,
		        status: 'partially'
		      }
		    ];

		  $scope.getDayClass = function(date, mode) {
		    if (mode === 'day') {
		      var dayToCheck = new Date(date).setHours(0,0,0,0);

		      for (var i=0;i<$scope.events.length;i++){
		        var currentDay = new Date($scope.events[i].date).setHours(0,0,0,0);

		        if (dayToCheck === currentDay) {
		          return $scope.events[i].status;
		        }
		      }
		    }

		    return '';
		  };
	//--------------------------------------------
    
    $scope.passengerGrid = {
    		minRowsToShow : 12,	
            enableFiltering: true,
            useExternalSorting: false,
            paginationPageSizes: [],
            paginationPageSize: 25,
            useExternalPagination: true,
            enableHorizontalScrollbar: true,
            
            keepLastSelected: true,
            //useExternalSorting: true,
            //pagingOptions: $scope.pagingOptions,        
            filterOptions: $scope.filterOptions,
            sortInfo: $scope.sortOptions,	
            
            columnDefs: [{"name": "ruleHit", "displayName": "H", width: 50, enableFiltering: false},
                         // {"name": "onWatchList", "displayName": "L", width: 50, enableFiltering: false},
                         {"name": "lastName", "displayName": "Last Name", width: 175/*, enableFiltering: false*/},
                         {"name": "firstName", "displayName": "First Name", width: 150/*, enableFiltering: false*/},
                         {"name": "middleName", "displayName": "Middle", width: 100/*, enableFiltering: false*/},
                         {"name": "flightNumber", "displayName": "Flight", width: 90/*, enableFiltering: false*/},
                         {"name": "flightETA", "displayName": "ETA", width: 175, enableFiltering: false},
                         {"name": "flightETD", "displayName": "ETD", width: 175, enableFiltering: false},
                         {"name": "passengerType", "displayName": "Type", width: 50, enableFiltering: false},
                         {"name": "gender", "displayName": "G", width: 50, enableFiltering: false},
                         {"name": "dob", "displayName": "DOB", field: 'dob', cellFilter: 'date', width: 175, enableFiltering: false},
                         {"name": "citizenshipCountry", "displayName": "CTZ", width: 75/*, enableFiltering: false*/},
                         {"name": "passengerType", "displayName": "T", width: 100, enableFiltering: false},
                         {"name": "documentType", "displayName": "T", width: 50, enableFiltering: false},
                         {"name": "carrier", "displayName": "Carrier", width: 50/*, enableFiltering: false*/},
                         {"name": "seat", "displayName": "Seat", width: 75, enableFiltering: false}] ,
    
                         onRegisterApi: function (gridApi) {
                             $scope.gridApi = gridApi;
                             
                             gridApi.selection.on.rowSelectionChanged($scope, function (row) {
                                 var title;
                                 if (row.isSelected) {
                                     $state.go('pax.detail', { id: row.entity.paxId, flightId: row.entity.flightId });
                                 }
                             });
                             
                             
                         }
    
    
    	} // END of Passenger Grid
    	
		//    	$.extend({
		//        columnDefs: $rootScope.columns.PASSENGER_UI,
		//        exporterCsvFilename: 'Passengers.csv',
		//        exporterPdfHeader: {text: "Passengers", style: 'headerStyle'}
		//    }, $scope.gridOpts);


	    var pax = {
	        	startDate: $filter('date')($scope.dt, 'MM/dd/yyyy'),
	    		endDate: $filter('date')($scope.dt3, 'MM/dd/yyyy')
	        	//endDate: $scope.dt3
	        };
	    
	    paxService.getAllPax(pax).then(function (myData) {
	    	$scope.passengerGrid.totalItems = myData[0];
	        $scope.passengerGrid.data = myData[1];
	    });

    
    $scope.refreshListing = function(){
    	
        var pax = {
	        	startDate: $filter('date')($scope.dt, 'MM/dd/yyyy'),
	    		endDate: $filter('date')($scope.dt3, 'MM/dd/yyyy')
	        	//endDate: $scope.dt3
	        };
	    
	    paxService.getAllPax(pax).then(function (myData) {
	    	$scope.passengerGrid.totalItems = myData[0];
	        $scope.passengerGrid.data = myData[1];
	    });
	    
    }
    
    
    
    
    
    
    
    
    
    //------- Pre-Refactor-------------------
    //Function to get Rule Hits per Passenger
    $scope.getRuleHits = function (passengerId) {
        var j, i;
        $scope.isExpanded = !$scope.isExpanded;
        if (!$scope.isExpanded) {
            paxService.getRuleHits(passengerId).then(function (myData) { // Begin

                $scope.paxHitList = [];
                $scope.tempPaxHitDetail = [];
                $scope.tempPaxHitList = [];
                var tempObj = [];

                for (j = 0; j < myData.length; j++) {
                    $scope.tempPaxHitList = myData[j].hitsDetailsList;
                    for (i = 0; i < $scope.tempPaxHitList.length; i++) {
                        tempObj = $scope.tempPaxHitList[i];
                        tempObj.ruleTitle = myData[j].ruleTitle;
                        tempObj.ruleConditions = tempObj.ruleConditions.substring(0, (tempObj.ruleConditions.length - 3));
                        $scope.tempPaxHitDetail[i] = tempObj;
                    }

                    $scope.paxHitList.push($scope.tempPaxHitDetail.pop());
                    $scope.tempPaxHitDetail = [];
                }
            }); // END of paxService getRuleHits
        }
    };
    
    
    $state.go('pax.all');
    
}); // END of PaxController

// Customs Filters

app.filter('capitalize', function() {
  return function(input, all) {
    return (!!input) ? input.replace(/([^\W_]+[^\s-]*) */g, function(txt){return txt.charAt(0).toUpperCase() + txt.substr(1).toLowerCase();}) : '';
  }
});

app.filter('DobDateFormat', function myDateFormat($filter){
	  return function(text){
	    var  tempdate= new Date(text.replace(/-/g,"/"));
	    return $filter('date')(tempdate, "MMM-dd-yyyy");
	  }
	});


app.filter('orderObjectBy', function() {
	  return function(items, field, reverse) {
	    var filtered = [];
	    angular.forEach(items, function(item) {
	      filtered.push(item);
	    });
	    filtered.sort(function (a, b) {
	      return (a[field] > b[field] ? 1 : -1);
	    });
	    if(reverse) filtered.reverse();
	    return filtered;
	  };
	});

