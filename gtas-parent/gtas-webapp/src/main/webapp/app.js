var app = angular.module('myApp', ['ui.router', 'ui.bootstrap', 'ngTable' ])

app.config(function($stateProvider){
$stateProvider
    .state('index', {
        url: '',
        views: {
            'main': {
                template: 'index.main'
            },
            'viewB': {
                template: 'index.viewB'
            }
        }
    })
    .state('flights', {
        url: '/flights',
        views: {
            'main': {
                templateUrl: 'views/flights.html',
                controller: 'FlightController'
            },
            'viewB': {
                template: 'flights.viewB'
            }
        }
    })
    .state('query', {
        url: '/query',
        views: {
            'main': {
                template: 'query.main'
            },
            'viewB': {
                template: 'query.viewB'
            }
        }
    })
})
