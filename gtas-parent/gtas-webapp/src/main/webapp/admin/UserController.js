
app.directive('formAutofillFix', function() {
    return function(scope, elem, attrs, ngModel) {
        // Fixes Chrome bug: https://groups.google.com/forum/#!topic/angular/6NlucSskQjY
        elem.prop('method', 'POST');
        // Fix autofill issues where Angular doesn't know about autofilled inputs
            setTimeout(function() {
            }, 0);

    };
});
app.directive('autoFillSync', function($timeout) {
    return {
        require: 'ngModel',
        link: function(scope, elem, attrs, ngModel) {
            var origVal = elem.val();
            $timeout(function () {
                var newVal = elem.val();
            }, 500);
        }
    }
});

app.controller('UserCtrl', ['$state','$scope','$q','$stateParams', 'userService','Base64','$mdToast',
    function($state,$scope,$q,$stateParams,userService,Base64, $mdToast) {

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

    $scope.roles=[];

    $scope.IsCreateUser=function()
    {
        return $stateParams.action == 'create';
    };
    $scope.rolesLookup=[];

    $scope.selectedRoles = [];

    function selectedRole(role, selected, disabled) {
            this.role = role,
            this.selected = selected,
            this.disabled= disabled
        };

        $scope.userStatus = {
            enabled: true
        }

        $scope.radioData = [ { label: 'Enable', value: 1 },
            { label: 'Disable', value: 0  }   ];

        $scope.enableDisableUser = function(value){
            if(value === true){
                $scope.persistUser.active = 1;
            }else{
                $scope.persistUser.active = 0;
            }
        }

    $scope.Init=function () {
    	if($scope.user!=null)
    	{
            $scope.getRoles();

            $scope.IsCreateUser=true;

            $scope.persistUser=$scope.user;

            if($scope.user.active === 1) {
                $scope.userStatus.enabled = true;
            }else {
                $scope.userStatus.enabled = false;
            }

    	}else {
            $scope.getRoles();
        }

    };

        $scope.refreshSelectedRoles = function(sourceRoles, targetRoles){
            angular.forEach(sourceRoles, function(item) {
                var roleExists = false;
                var j = 0, len = targetRoles.length;
                for (j = 0; j < len; j++) {
                        if(targetRoles[j].role === item) {
                            roleExists = true;
                        }
                }
                if(!roleExists){
                    $scope.selectedRoles.push(new selectedRole(item, true, false));
                }
            })
        }

        $scope.sortRoles = function(items, field, reverse) {
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

        // SERVICE CALL
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

   $scope.enableRoles = function(){

       angular.forEach($scope.user.roles, function (value) {

           if(value.roleDescription != 'Admin') {
               $scope.roles[value.roleDescription] = {role: value.roleDescription, selected: true, disabled: false};
           }else if(value.roleDescription === 'Admin'){
               $scope.roles[value.roleDescription] = {role: value.roleDescription, selected: true, disabled: false};
               var i = 0, len = $scope.rolesLookup.length;
               for (i = 0; i < len; i++) { // Disable the rest of the options
                   if ($scope.rolesLookup[i].roleDescription != value.roleDescription) {
                       $scope.roles[$scope.rolesLookup[i].roleDescription] = {role:$scope.rolesLookup[i].roleDescription, selected:false, disabled:false};
                   }
               }
           }
       });
   };

   $scope.toggle = function (item, list) {
      var idx = list.indexOf(item);
      if (idx > -1) list.splice(idx, 1);
      else list.push(item);
    };


    $scope.toggleRole = function (item) {

            if (item === 'Admin') {

                if ($scope.roles[item].selected === true) {

                    var i = 0, len = $scope.rolesLookup.length;
                    for (i = 0; i < len; i++) { // Disable the rest of the options
                        if ($scope.rolesLookup[i].roleDescription != item) {
                            $scope.roles[$scope.rolesLookup[i].roleDescription] = {role:$scope.rolesLookup[i].roleDescription, selected:false, disabled:true};
                        }
                    }
                } // End Admin False
                else if ($scope.roles[item].selected === false) {

                    $scope.roles[item] = {role:'Admin', selected:false, disabled:false};
                    $scope.setRoles();
                } // End Admin True
            }
            if (item != 'Admin') {
                //$scope.roles[item].selected = !$scope.roles[item].selected;
                $scope.roles['Admin'] = {role:'Admin', selected:false, disabled:false};
            }
        };

    $scope.exists = function (item, list) {
      return list.indexOf(item) > -1;
    };

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
            for (i = 0; i < len; i++) {
                //$scope.roles.push($scope.rolesLookup[i].roleDescription);
                $scope.roles[$scope.rolesLookup[i].roleDescription] = {role:$scope.rolesLookup[i].roleDescription, selected:false, disabled:false};
                //$scope.selectedRoles.push(new selectedRole($scope.rolesLookup[i].roleDescription, false, false));
            }
        if($scope.user!=null) {
            $scope.enableRoles();
        }
    };

    $scope.populateSelectedRoles=function()
    {
        $scope.persistUser.roles=[];
        var len=$scope.rolesLookup.length;
        for (i = 0; i < len; i++) {
            if($scope.roles[$scope.rolesLookup[i].roleDescription].selected) {
                var role = $scope.getRoleFromLookup($scope.roles[$scope.rolesLookup[i].roleDescription].role);
                $scope.persistUser.roles.push(role);
            }
        }
    };

    $scope.encodePassword = function(){
        $scope.userPasswordChanged=true;
    };

    /* GET ROLES AND INITIALIZE*/
    //$scope.getRoles();

    $scope.Init();

        $scope.showRoleToast = function() {
            $mdToast.show(
                $mdToast.simple()
                    .content('One or More User Roles Have To Be Selected')
                    .position("top right")
                    .hideDelay(3000)
            );
        };

        $scope.showFieldToast = function() {
            $mdToast.show(
                $mdToast.simple()
                    .content('All Input Fields are Required')
                    .position("top right")
                    .hideDelay(3000)
            );
        };


        $scope.saveUser=function() {
            if ($scope.userPasswordChanged) {
                //$scope.persistUser.password = btoa($scope.persistUser.password);
                //$scope.userPasswordChanged=false;
            }

            $scope.populateSelectedRoles();
            if ($scope.persistUser.roles.length === 0) {
                $scope.showRoleToast();
                event.preventDefault();
            }
            else if($scope.persistUser.userId.length < 1
                 || $scope.persistUser.password.length < 1
                 || $scope.persistUser.firstName.length < 1
                 || $scope.persistUser.lastName.length < 1){
                $scope.showFieldToast();
                event.preventDefault();
            }
            else{
            if ($scope.action == 'modify') {
                userService.updateUser($scope.persistUser)
                    .then(
                    function (user) {
                        $state.go('admin.users', {action: 'created', user: user});
                    });

            } else {

                userService.createUser($scope.persistUser)
                    .then(
                    function (user) {
                        $state.go('admin.users', {action: 'created', user: user});
                    });
            }
        }
    };

    $scope.back=function()    {
        $state.go('admin.users');
    };

        if($scope.action != 'modify') {
            $scope.$broadcast("autofill:update");
        }
  }]);
