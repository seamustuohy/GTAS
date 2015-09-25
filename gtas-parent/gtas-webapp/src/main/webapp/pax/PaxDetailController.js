app.controller('PaxDetailController', function ($scope, $filter, $q, paxService, sharedPaxData, passengers, $location, $stateParams) {
    $scope.passenger = [];
    $scope.passenger = passengers.data;
    $scope.paxTableEnabled = false;
    //date modifications
    $scope.passenger.flightETA = new Date($scope.passenger.flightETA);
    $scope.passenger.flightETD = new Date($scope.passenger.flightETD);
    $scope.passenger.dob = new Date($scope.passenger.dob);
    $scope.parent=$stateParams.parent;

  //$location.path("/passengers/");
});
