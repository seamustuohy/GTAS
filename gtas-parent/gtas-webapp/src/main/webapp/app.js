var app = angular.module('myApp', [
    'ui.router',
    'ui.bootstrap',
	'ngTable',
    'spring-security-csrf-token-interceptor',
    'ui.grid',
    'ui.grid.resizeColumns',
    'ui.grid.moveColumns',
    'ui.grid.pagination',
    'ui.grid.edit',
    'ui.grid.rowEdit',
    'ui.grid.cellNav',
    'ui.grid.selection',
    'ui.grid.exporter',
    'ui.grid.expandable',
    'ct.ui.router.extras'
]);

app.controller('NavCtrl', function ($scope, $location) {
    $scope.isActive = function (route) {
        $scope.path = $location.path();
        return $location.path() === route;
    };
});

app.run(function ($rootScope) {
    $rootScope.columns = {
        FLIGHT: [
            {"name": "carrierCode", "displayName": "Carrier"},
            {"name": "flightNumber", "displayName": "Flight #"},
            {"name": "origin", "displayName": "Origin"},
            {"name": "originCountry", "displayName": "Country"},
            {"name": "departureDt", "displayName": "ETD"},
            {"name": "destination", "displayName": "Destination"},
            {"name": "destinationCountry", "displayName": "Country"},
            {"name": "arrivalDt", "displayName": "ETA"}
        ],
        PASSENGER_UI: [
            {"name": "ruleHit", "displayName": "H", width: 50},
            {"name": "onWatchList", "displayName": "L", width: 50},
            {"name": "lastName", "displayName": "Last Name", width: "*"},
            {"name": "firstName", "displayName": "First Name", width: "*"},
            {"name": "middleName", "displayName": "Middle Name", width: "*"},
            {"name": "passengerType", "displayName": "Type", width: 25},
            {"name": "gender", "displayName": "GEN", width: 25},
            {"name": "dob", "displayName": "DOB", width: 125},
            {"name": "citizenshipCountry", "displayName": "CIT", width: 50},
            {"name": "documentNumber", "displayName": "Doc #", width: 125},
            {"name": "documentType", "displayName": "T", width: 25},
            {"name": "residencyCountry", "displayName": "Issuer", width: 50}
        ],
        PASSENGER: [
            {"name": "ruleHit", "displayName": "H", width: 50},
            {"name": "onWatchList", "displayName": "L", width: 50},
            {"name": "lastName", "displayName": "Last Name", width: "*"},
            {"name": "firstName", "displayName": "First Name", width: "*"},
            {"name": "middleName", "displayName": "Middle Name", width: "*"},
            {"name": "passengerType", "displayName": "Type", width: 25},
            {"name": "gender", "displayName": "GEN", width: 25},
            {"name": "dob", "displayName": "DOB", width: 125},
            {"name": "citizenship", "displayName": "CIT", width: 50},
            {"name": "documentNumber", "displayName": "Doc #", width: 125},
            {"name": "documentType", "displayName": "T", width: 25},
            {"name": "documentIssuanceCountry", "displayName": "Issuer", width: 75},
            {"name": "carrierCode", "displayName": "Carrier", width: "40"},
            {"name": "flightNumber", "displayName": "Flight #", width: 80},
            {"name": "origin", "displayName": "Origin", width: "50"},
            {"name": "destination", "displayName": "Dest", width: "50"},
            {"name": "departureDt", "displayName": "ETD", width: 170},
            {"name": "arrivalDt", "displayName": "ETA", width: 170},
            {"name": "seat", "displayName": "Seat", width: 50}
        ],
        QUERIES: [
            {
                name: "title",
                displayName: "Name",
                enableCellEdit: false,
                enableColumnMenu: false
            }, {
                name: "description",
                enableCellEdit: false,
                enableColumnMenu: false
            }
        ],
        RISK_CRITERIA: [
            {
                name: "title",
                displayName: "Name",
                enableCellEdit: false,
                enableColumnMenu: false
            }, {
                name: "description",
                enableCellEdit: false,
                enableColumnMenu: false
            }, {
                name: "startDate",
                enableCellEdit: false,
                enableColumnMenu: false
            }, {
                name: "endDate",
                enableCellEdit: false,
                enableColumnMenu: false
            }, {
                name: "enabled",
                enableCellEdit: false,
                enableColumnMenu: false
            }
        ]
    };
});

app.config(function ($stateProvider) {
    $stateProvider
        .state('admin', {
            url: '/admin',
            templateUrl: 'admin/admin.html'
        })
        .state('dashboard', {
            url: '',
            templateUrl: 'dashboard/dashboard.html',
            controller: 'DashboardController'
        })
        .state('flights', {
            url: '/flights',
            templateUrl: 'flights/flights2.html',
            controller: 'FlightsIIController'
        })
        .state('pax', {
            url: '/passengers',
            controller: 'PaxMainController',
            templateUrl: 'pax/pax.header.html'
        })
        .state('pax.all', {
            url: '/',
            sticky: true,
            dsr: true,
            views: {

                "content@pax": {
                    //controller: 'Paginate',
                    controller: 'PaxController',
                    templateUrl: 'pax/pax.table.html'
                }
            }
        })
        .state('pax.detail', {
            url: '/:id/:flightId',
            sticky: true,
            dsr: true,
            views: {

                "content@pax": {
                    controller: 'PaxDetailController',
                    templateUrl: 'pax/pax.detail.html',
                    resolve: {
                        passengers: function ($http, $stateParams, paxDetailService) {
                            return paxDetailService.getPaxDetail($http, $stateParams);
                        }
                    }
                }
            }
        })
        .state('query-builder', {
            url: '/query-builder',
            templateUrl: 'query-builder/query.html',
            controller: 'QueryBuilderController'
        })
        .state('risk-criteria', {
            url: '/risk-criteria',
            templateUrl: 'risk-criteria/risk-criteria.html',
            controller: 'RiskCriteriaController'
        })
        .state('watchlists', {
            url: '/watchlists',
            templateUrl: 'watchlists/watchlists.html',
            controller: 'WatchListController'
        });
});
