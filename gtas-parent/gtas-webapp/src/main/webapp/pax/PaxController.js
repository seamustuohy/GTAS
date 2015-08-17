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
    
    $scope.tableParams = new ngTableParams({  
        page: 1,            // show first page
        count: 10,          // count per page
        filter: {},
        sorting: {
    //        hits: 'desc',
    //        destinationDateTimeSort: 'asc' //, 'number': 'asc'     // initial sorting
        }
    }, {
    	
    	
    	 total: (paxData).length, // length of data
         getData: function($defer, params) {
        	 
        	 /*paxService.getPax(flightId).then(function (myData) {
          	   paxData = myData;
          	   
        	 });*/
        	 
        	 	 params.total(paxData.length);
                 $defer.resolve(paxData.slice((params.page() - 1) * params.count(), params.page() * params.count()));
             //$defer.resolve($scope.dataset.slice((params.page() - 1) * params.count(), params.page() * params.count()));
         }
    
    });
    
    
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
    
    
    $scope.getRuleHits = function(passengerId){
    	
    	$scope.isExpanded = !$scope.isExpanded;
    	
    	if(!$scope.isExpanded){
    	
    	paxService.getRuleHits(passengerId).then(function (myData) { // Begin
    		
    		$scope.paxHitList = [];
        	$scope.tempPaxHitList = [];
        	var tempObj = [];
    		
    		for (j = 0; j < myData.length; j++){
    		$scope.tempPaxHitList[j] = myData[j].hitsDetailsList;
    		
    		
    	//	for (i = 0; i < $scope.tempPaxHitList.length; i++){
    		var tempRuleID = $scope.tempPaxHitList[j][0].ruleId;
    		tempObj = $scope.tempPaxHitList[j][0];
    		
    	    riskCriteriaService.loadRuleById(tempRuleID).then(function (myData) {
                //$scope.$builder.queryBuilder('readOnlyRules', myData.details);
    	    	if(myData.summary != undefined){
    	    	tempObj.ruleTitle = myData.summary.title;
    	    	tempObj.ruleDesc = myData.summary.description;
    	    	}
          	  	console.log(myData);
          	  
          	  
            });
    	    
    	    $scope.paxHitList[j] = tempObj;
    	    
    	//	} // END of paxHitList object loop
    		}
    		}); // END of paxService getRuleHits

    }
    	
    };
    
});
