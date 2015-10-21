app.controller('UserSettingsController', function ($scope, $state, $interval, $stateParams,user,userService) {

    $scope.userCredentials = {
        firstName: '',
        lastName: '',
        newPassword:'',
        confirmPassword:''
    };
    $scope.updateCredentials=function()
    {
        user.data.firstName=$scope.userCredentials.firstName;
        user.data.lastName=$scope.userCredentials.lastName;
        user.data.password=$scope.userCredentials.newPassword;
        userService.updateUser(user.data).
            then(
            function () {
                $scope.successMessage="Your credentials were updated successfully." ;
                $scope.success=true;
            }
        );
    };
    init=function()
    {
        $scope.userCredentials.firstName=user.data.firstName;
        $scope.userCredentials.lastName=user.data.lastName;
        $scope.userCredentials.newPassword=user.data.password;
        $scope.userCredentials.confirmPassword=user.data.password;
        $scope.success=false;
        $scope.error=false;
    };
    init();
});
