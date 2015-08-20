app.controller('PaxController', function($scope, $filter, $q, ngTableParams, paxService, sharedPaxData, riskCriteriaService) {
	var paxData = [];
    var flightId = 1;
    var self = this;
    var paxArrayIndex = 0;
    $scope.isExpanded = true;
    
    $scope.paxHitList = [];
    $scope.list = sharedPaxData.list; 
    $scope.add = sharedPaxData.add;
    $scope.getAll = sharedPaxData.getAll;
    
    $scope.getPaxSpecificList = function(index){
        return $scope.list(index);        
    };
    
    var data = [];
  
    // NG Table for pax under passengers UI
    $scope.paxTableParams = new ngTableParams(
    	    {
    	    	noPager: true,
    	    	page: 1,            // show first page
    	        count: 10,          // count per page
    	        filter: {},
    	        sorting: {
    	        	ruleHits: 'desc'
    	            //,
    	            //destinationDateTimeSort: 'asc' //, 'number': 'asc'     // initial sorting
    	        }
    	    }, {
    	    	counts: [],
    	      //  total: data.length, // length of data
    	        getData: function($defer, params) {
    	            paxService.getAllPax().then(function (myData) {
    	            	data = myData;
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
    
    //NG TABLE for pax under Flights UI
    $scope.tableParams = new ngTableParams({  
    	
    	noPager: true,
        page: 1,            // show first page
      //  count: 10,          // count per page
        filter: {},
        sorting: {
        	ruleHits: 'desc'
        		//,
    //        destinationDateTimeSort: 'asc' //, 'number': 'asc'     // initial sorting
        }
    }, {
    	 counts: [] ,
    	 total: (paxData).length, // length of data
         getData: function($defer, params) {
        	 
         var orderedData = $filter('orderBy')(paxData, params.orderBy());	 
        	 
   	 //	 params.total(paxData.length);
         $defer.resolve(orderedData.slice((params.page() - 1) * params.count(), params.page() * params.count()));
         //$defer.resolve($scope.dataset.slice((params.page() - 1) * params.count(), params.page() * params.count()));
         }
    
    });
    
    
    
    // Function to get Passenger Information
    $scope.getPaxInfo = function(flightId, index)
    {    	
    	self.flightId = flightId;
    	paxService.getPax(flightId).then(function (myData) {
    	//paxData = [];	
    	self.paxArrayIndex = index;
    	$scope.add(myData, index);
    	paxData = $scope.getPaxSpecificList(index);
    //	$scope.$emit('paxDataResponse', paxData);
       	$scope.tableParams.settings().$scope = $scope;
       	$scope.tableParams.reload();
     	 });
    }; 	
    

    //Function to get Rule Hits per Passenger
    $scope.getRuleHits = function(passengerId){
    	
    	$scope.isExpanded = !$scope.isExpanded;
    	
    	if(!$scope.isExpanded){
    	
    	paxService.getRuleHits(passengerId).then(function (myData) { // Begin
    		
    		$scope.paxHitList = [];
    		$scope.tempPaxHitDetail = [];
        	$scope.tempPaxHitList = [];
        	var tempObj = [];
    		
    		for (j = 0; j < myData.length; j++){

    			$scope.tempPaxHitList = myData[j].hitsDetailsList;

    			for (i = 0; i < $scope.tempPaxHitList.length; i++){
    				tempObj = $scope.tempPaxHitList[i];
    				tempObj.ruleTitle = myData[j].ruleTitle;
    				tempObj.ruleConditions = tempObj.ruleConditions.substring(0,(tempObj.ruleConditions.length - 3));
    				$scope.tempPaxHitDetail[i] = tempObj;
    			}
    			
    			$scope.paxHitList.push($scope.tempPaxHitDetail.pop());
    			$scope.tempPaxHitDetail = [];
    			
    			
//    		$scope.tempPaxHitList[j] = myData[j].hitsDetailsList;
//    		
//    		
//    	//	for (i = 0; i < $scope.tempPaxHitList.length; i++){
//    		var tempRuleID = $scope.tempPaxHitList[j][0].ruleId;
//    		tempObj = $scope.tempPaxHitList[j][0];
//    		
//    	    riskCriteriaService.loadRuleById(tempRuleID).then(function (myData) {
//                //$scope.$builder.queryBuilder('readOnlyRules', myData.details);
//    	    	if(myData.summary != undefined){
//    	    	tempObj.ruleTitle = myData.summary.title;
//    	    	tempObj.ruleDesc = myData.summary.description;
//    	    	}
//          	  	console.log(myData);
//            });
//    	    
//    	    $scope.paxHitList[j] = tempObj;
    	    
    	//	} // END of paxHitList object loop
    		}
    		
    		
    		
    		}); // END of paxService getRuleHits

    }
    	
    };
    
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

