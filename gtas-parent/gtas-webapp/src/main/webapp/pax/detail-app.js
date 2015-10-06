(function () {
    'use strict';
    //function PassengerDetailCtrl($scope, passenger) {
    //    $scope.passenger = passenger;
    //}

    //function resolvePassenger($stateParams, paxDetailService) {
    //    paxDetailService.getPaxDetail($stateParams.paxId, $stateParams.flightId).then(function (response) {
    //        return response.data;
    //    });
    //}

    //function Router($stateProvider) {
    //    $stateProvider
    //        .state('default', {
    //            url: '',
    //            templateUrl: 'pax/pax.detail.html',
    //            controller: 'PassengerDetailCtrl',
    //            resolve: {
    //                passenger: resolvePassenger
    //            }
    //        });
    //}

    function PassengerDetailCtrl($scope, paxDetailService) {
        var regex = /[?&]([^=#]+)=([^&#]*)/g,
            url = window.location.href,
            params = {},
            match;
        while (match = regex.exec(url)) {
            params[match[1]] = match[2];
        }
        paxDetailService.getPaxDetail(params.paxId, params.flightId).then(function (response) {
            $scope.passenger = response.data;
        });
    }

    function paxDetailService($http) {
        function getPaxDetail(paxId, flightId) {
            return $http.get("/gtas/passengers/passenger/" + paxId + "/details?flightId=" + flightId);
        }
        return ({getPaxDetail: getPaxDetail});
    }

    angular.module('detailsApp', [
        'ui.router',
        'ngMaterial',
        'ngMessages',
        'ngAria',
        'ngAnimate',
        'angularSpinners'
    ])
        .controller('PassengerDetailCtrl', PassengerDetailCtrl)
        .service('paxDetailService', paxDetailService);
//        .config(Router);
})();
