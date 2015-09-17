
app.controller('UserCtrl', ['$scope','$stateParams', function($scope,$stateParams) {
	
    /*$scope.user = {
      title: 'Developer',
      email: 'ipsum@lorem.com',
      firstName: '',
      lastName: '' ,
      company: 'Google' ,
      address: '1600 Amphitheatre Pkwy' ,
      city: 'Mountain View' ,
      state: 'CA' ,
      biography: 'Loves kittens, snowboarding, and can type at 130 WPM.\n\nAnd rumor has it she bouldered up Castle Craig!',
      postalCode : '94043'
    };*/
	
	$scope.user = $stateParams.user;
    $scope.action=$stateParams.action;
    
    $scope.selected = []
    $scope.Init=function () {
        
    	/*angular.forEach($scope.user.roles, function(value, key) {
    		$scope.selected.push(value.roleDescription);
    		})*/
    };
    

    $scope.radioData = [
                        { label: 'Enable', value: 1 },
                        { label: 'Disable', value: 0,isDisabled: true  },
                      ];
    
    $scope.data = {
    	      group1 : 1,
    	      group2 : 0,
    	    };    
    
  // $scope.items = [1,2,3,4,5];
   $scope.items = ['ROLE_CUST','VIEW_FLIGHT_PASSENGERS','MANAGE_QUERIES','MANAGE_WATCHLIST','ROLE_ADMIN'];
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
  
 
 