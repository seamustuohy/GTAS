
app.controller('UserCtrl', ['$scope','$stateParams', function($scope,$stateParams) {

	
	$scope.user = $stateParams.user;
    $scope.action=$stateParams.action;
    
    $scope.selected = [];
    $scope.Init=function () {
        
    	angular.forEach($scope.user.roles, function(value, key) {
    		$scope.selected.push(value.roleDescription);    		}
    	
    	)
    	$scope.userStatus=$scope.user.active;
    };

    $scope.radioData = [
                        { label: 'Enable', value: 1 },
                        { label: 'Disable', value: 0  },
                      ];
    
    $scope.userStatus = {
    	      group1 : 1,
    	      group2 : 0,
    	    };    
    
  // $scope.items = [1,2,3,4,5];
   $scope.roles = [{1:'ADMIN'},{2:'ROLE_CUST'},{3:'VIEW_FLIGHT_PASSENGERS'},{4:'MANAGE_QUERIES'},{5:'MANAGE_RULES'},{6:'MANAGE_WATCHLIST'},{7:'ROLE_ADMIN'}];
   $scope.items = ['ROLE_CUST','VIEW_FLIGHT_PASSENGERS','MANAGE_QUERIES','MANAGE_RULES','MANAGE_WATCHLIST','ROLE_ADMIN', 'ADMIN'];
    $scope.selected = [];
    $scope.toggle = function (item, list) {
      var idx = list.indexOf(item);
      if (idx > -1) list.splice(idx, 1);
      else list.push(item);
    };
    $scope.exists = function (item, list) {
      return list.indexOf(item) > -1;
    };
    
    $scope.Init();
  }]);
  
 
 