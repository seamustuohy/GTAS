(function () {
    'use strict';
    app
        .service('paxDetailService', function ($http, $q) {
            function getPaxDetail(paxId, flightId) {
                var dfd = $q.defer();
                dfd.resolve($http.get("/gtas/passengers/passenger/" + paxId + "/details?flightId=" + flightId));
                return dfd.promise;
            }
            return ({getPaxDetail: getPaxDetail});
        })
        .controller('PassengerDetailCtrl', function ($scope, passenger) {
            $scope.passenger = passenger.data;
            /* to ask Manessh about this paxTableEnabled prop, I went ahead and removed the three new date statements he had since he had date filters that got ignored in template" */
            $scope.paxTableEnabled = false;
        });
}());
