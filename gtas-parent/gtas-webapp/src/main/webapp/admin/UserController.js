
app.controller('UserCtrl', ['$state','$scope','$q','$stateParams', 'UserService','Base64',
    function($state,$scope,$q,$stateParams,userService,Base64) {

	$scope.user = $stateParams.user;
    $scope.action=$stateParams.action;
    $scope.userPasswordChanged = false;
    $scope.persistUser = {
            userId: '',
            firstName: '',
            lastName: '',
            active:1,
            roles:[]
        };

    $scope.IsCreateUser=function()
    {
        return $stateParams.action == 'create';
    };
    $scope.rolesLookup=[];

    $scope.selectedRoles = [];

        $scope.radioData = [ { label: 'Enable', value: 1 },
            { label: 'Disable', value: 0  }   ];
        $scope.roles=[];

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
                $scope.setRoles();
            }
        );
    };

   //$scope.roles = ['VIEW_FLIGHT_PASSENGERS','MANAGE_QUERIES','MANAGE_RULES','MANAGE_WATCHLIST', 'ADMIN'];
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

    $scope.setRoles=function () {
        var i = 0, len = $scope.rolesLookup.length;
            for (i = 0; i < len; i++) {  $scope.roles.push($scope.rolesLookup[i].roleDescription)   ;  }
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

    $scope.encodePassword = function(){
        $scope.userPasswordChanged=true;
    };

    $scope.saveUser=function()
    {
        if($scope.userPasswordChanged){
            //$scope.persistUser.password = btoa($scope.persistUser.password);
            //$scope.userPasswordChanged=false;
        }

    	$scope.populateSelectedRoles();
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
