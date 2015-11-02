app.controller('AdminCtrl', function ($scope, gridOptionsLookupService, userService, $location) {
    'use strict';
    var setData = function (data) {
        $scope.usersGrid.data = data;
    };
    $scope.usersGrid = gridOptionsLookupService.getGridOptions('admin');
    $scope.usersGrid.columnDefs = gridOptionsLookupService.getLookupColumnDefs('admin');
    $scope.usersGrid.onRegisterApi = function (gridApi) { $scope.gridApi = gridApi; };
    userService.getAllUsers().then(setData);
    $scope.createUser = function () { $location.path('/user/new'); };
});
