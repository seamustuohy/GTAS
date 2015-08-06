app.controller('PaxController', function($scope, $filter, $q, ngTableParams, paxService, sharedPaxData) {
	var paxData = [];
    var flightId = 1;
    var self = this;
    var paxArrayIndex = 0;
    
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
    	
    	
    	
 /*   	
        total: paxData.length, // length of data
        getData: function($defer, params) {
            paxService.getPax(flightId).then(
           function (myData) {
        	   paxData = myData;
			    //vm.tableParams.total(result.total);
                // use build-in angular filter
                var filteredData = params.filter() ?
                        $filter('filter')(paxData, params.filter()) :
                        	paxData;
                var orderedData = params.sorting() ?
                        $filter('orderBy')(filteredData, params.orderBy()) :
                        	paxData;

                params.total(orderedData.length); // set total for recalc pagination
                $defer.resolve(orderedData.slice((params.page() - 1) * params.count(), params.page() * params.count()));
		    }
            
            );            
        }*/
    
    
    });
    
    
    $scope.getPaxInfo = function(flightId, index)
    
    {    	
    	self.flightId = flightId;
    	
    	paxService.getPax(flightId).then(function (myData) {
    	//paxData = [];	
    	self.paxArrayIndex = index;
    	$scope.add(myData, index);
    	paxData = $scope.getPaxSpecificList(index);
    	$scope.$emit('paxDataResponse', paxData);
       	$scope.tableParams.settings().$scope = $scope;
       	$scope.tableParams.reload();
     	 });
    	
    }; 	
    
    
    $scope.getRuleHits = function(travelerId){
    	
    	paxService.getRuleHits(travelerId).then(function (myData) {
        	
    		//paxData = [];	
        	//self.paxArrayIndex = index;
        //	$scope.add(myData);
        	//paxData = $scope.getPaxSpecificList(index);
        	//$scope.$emit('paxDataResponse', paxData);
           	//$scope.tableParams.settings().$scope = $scope;
           	//$scope.tableParams.reload();
    		
         	 });
    };
    
    
    //	 paxService.getPax(flightId).then(function (myData) { 
    //	 paxData = myData;
			    //vm.tableParams.total(result.total);
             // use build-in angular filter
//             var filteredData = params.filter() ?
//                     $filter('filter')(data, params.filter()) :
//                     data;
//             var orderedData = params.sorting() ?
//                     $filter('orderBy')(filteredData, params.orderBy()) :
//                     data;

//             params.total(orderedData.length); // set total for recalc pagination
//             $defer.resolve(orderedData.slice((params.page() - 1) * params.count(), params.page() * params.count()));
    	 
    	/* }
    	 
    	 );*/
    	
 //   };
    
	
   
/*    $scope.$on('handleBroadcast', function(event, id) {
        flightId = id;
        $scope.tableParams.reload();
        // $scope.$apply();
    });     
    */
});
