app.controller('NavCtrl', function ($scope, $location, $state) {
    'use strict';
    var routes = ['/flights', '/passengers/', '/query-builder', '/risk-criteria', '/watchlists', '/admin'],
        route = window.location.hash.split('?')[0].replace('#', '');

    if (!route.length) {
        route = '/flights';
    }

    $scope.selectedIndex = routes.indexOf(route) >= 0 ? routes.indexOf(route) : null;

    if (route.indexOf('/paxdetail') >= 0) {
        $state.go('detail');
        route = window.location.hash.replace('#', '');
        $('nav').remove();
    }

    $location.url(route);

    if ($scope.selectedIndex !== null) {
        $scope.$watch('selectedIndex', function (current) {
            $location.url(routes[current]);
        });
    }
});
