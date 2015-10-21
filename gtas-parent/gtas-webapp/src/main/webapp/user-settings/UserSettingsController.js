app.controller('UserSettingsController', function ($scope, $state, $interval, $stateParams) {

    $scope.userCredentials = {
        firstName: '',
        lastName: '',
        newPassword:'',
        confirmPassword:''
    };

    $scope.updateCredentials=function()
    {

    };
});
