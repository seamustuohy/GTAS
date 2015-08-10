app.controller('ExecutedQueryResultsController', function ($rootScope, DTOptionsBuilder, DTColumnBuilder) {
    'use strict';
    var callback, reloadData, vm = this;

    callback = function (json) {
        console.log(json);
    };
    reloadData = function () {
        vm.dtInstance.reloadData(callback, false);
    };

    vm.dtOptions = DTOptionsBuilder.fromSource('data.json')
        .withPaginationType('full_numbers');

    vm.dtColumns = [
        DTColumnBuilder.newColumn('carrierCode').withTitle('Carrier'),
        DTColumnBuilder.newColumn('flightNumber').withTitle('Flight #'),
        DTColumnBuilder.newColumn('origin').withTitle('Origin Airport'),
        DTColumnBuilder.newColumn('originCountry').withTitle('Country'),
        DTColumnBuilder.newColumn('departureDt').withTitle('ETD'),
        DTColumnBuilder.newColumn('destination').withTitle('Destination'),
        DTColumnBuilder.newColumn('destinationCountry').withTitle('Country'),
        DTColumnBuilder.newColumn('arrivalDt').withTitle('ETA')
    ];

    vm.reloadData = reloadData;
    vm.dtInstance = {};

    $rootScope.$on('aFactory:keyChanged', function (e, value) {
        vm.newSource = value;
        vm.reloadData();
        console.log(e);
    });

});
