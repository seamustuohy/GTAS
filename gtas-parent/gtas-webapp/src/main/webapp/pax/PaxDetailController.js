app.controller('PaxDetailController', function($scope, $filter, $q, ngTableParams, paxService, sharedPaxData, passengers, $location) {

	$scope.passenger = [];
	$scope.passenger = passengers.data;
	$scope.paxTableEnabled = false;
	
	//date modifications
	$scope.passenger.flightETA = new Date($scope.passenger.flightETA);
	$scope.passenger.flightETD = new Date($scope.passenger.flightETD);
	$scope.passenger.dob = new Date($scope.passenger.dob);
	
	$location.path("/passengers/");
	
});
