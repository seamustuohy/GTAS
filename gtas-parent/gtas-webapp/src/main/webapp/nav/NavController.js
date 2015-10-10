app.controller('NavCtrl', function ($scope) {
    'use strict';
    var lookup = {
        admin: ['admin', 'admin.users', 'admin.addUser'],
        dashboard: ['dashboard'],
        flights: ['flights'],
        passengers: ['paxAll', 'flightsPassengers'],
        queries: ['query-builder'],
        risks: ['risk-criteria'],
        watchlists: ['watchlists']
    };

    $scope.onRoute = function (stateName) {
        return lookup[stateName].indexOf($scope.stateName) >= 0;
    };

    $scope.showNav = function () {
        return ['queryFlights', 'queryPassengers'].indexOf($scope.stateName) === -1;
    };

    $scope.$on('stateChanged', function (e, stateName) {
        $scope.stateName = stateName;
    });
});
