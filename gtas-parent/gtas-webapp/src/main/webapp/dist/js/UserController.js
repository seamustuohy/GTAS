
app.controller('UserCtrl', ['$state','$scope','$q','$stateParams', 'UserService',function($state,$scope,$q,$stateParams,userService) {
	
	$scope.user = $stateParams.user;
	
    $scope.action=$stateParams.action;  
    $scope.persistUser = {
            userId: '',
            firstName: '',
            lastName: '',
            active:1,
            roles:[]
        };
    /**
     * @return {boolean}
     * @return {boolean}
     */
    $scope.IsCreateUser=function()
    {
        return $stateParams.action == 'create';
    };
    $scope.rolesLookup=[];

    
    $scope.selectedRoles = [];
    $scope.Init=function () {
    	if($scope.user!=null)
    	{
            $scope.IsCreateUser=true;
    		 $scope.persistUser=$scope.user;
            angular.forEach($scope.user.roles, function (value) {
                    $scope.selectedRoles.push(value.roleDescription);    		}

            )
    	}
        $scope.getRoles();
    };

    $scope.getRoles = function () {

        userService.getRoles()
            .then(
            function (roles) {
                // responses are redirected to confirmation page nothing do here..
                $scope.rolesLookup=roles;
            }
        );
    };

    $scope.radioData = [
                        { label: 'Enable', value: 1 },
                        { label: 'Disable', value: 0  }
    ];

   $scope.roles = ['ROLE_CUST','VIEW_FLIGHT_PASSENGERS','MANAGE_QUERIES','MANAGE_RULES','MANAGE_WATCHLIST','ROLE_ADMIN', 'ADMIN'];
    $scope.selectedRoles = [];
    $scope.toggle = function (item, list) {
      var idx = list.indexOf(item);
      if (idx > -1) list.splice(idx, 1);
      else list.push(item);
    };
    $scope.exists = function (item, list) {
      return list.indexOf(item) > -1;
    };
    
    $scope.Init();
    
    $scope.getRoleFromLookup=function (roleDescription) {
        var i = 0, len = $scope.rolesLookup.length;

        if (roleDescription.length <= 0) {
        } else {
            for (i = 0; i < len; i++) {
                if ($scope.rolesLookup[i].roleDescription === roleDescription) {
                    {
                        //alert("Found match");
                        return $scope.rolesLookup[i];
                    }
                }
            }
        }
        return null;
    };

    $scope.populateSelectedRoles=function()
    {
        $scope.persistUser.roles=[];
        var len=$scope.selectedRoles.length;
        for (i = 0; i < len; i++) {
        	
        	var role=$scope.getRoleFromLookup($scope.selectedRoles[i] );
        	
        	$scope.persistUser.roles.push(role);
        }
       
    };

    $scope.saveUser=function()
    {
    	$scope.populateSelectedRoles();
        console.log($scope.persistUser);
        if ($scope.action == 'modify') {
            userService.updateUser($scope.persistUser)
                .then(
                function (user) {
                    $state.go('admin.users',{ action: 'created', user: user });

                });

        } else {
            userService.createUser($scope.persistUser)
                .then(
                function (user) {
                    $state.go('admin.users',{ action: 'created', user: user });

                });
        }
        	

    };

    $scope.back=function()    {

        $state.go('admin.users');
    };
  }]);
  
 
 