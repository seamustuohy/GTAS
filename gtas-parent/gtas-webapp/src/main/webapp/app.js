var app;
(function () {
    'use strict';
    var pageDefaults = {
            pageNumber: 1,
            pageSize: 10
        },
        appDependencies = [
            'ui.router',
            'ui.grid',
            'ui.grid.resizeColumns',
            'ui.grid.moveColumns',
            'ui.grid.pagination',
            'ui.grid.autoResize',
            'ui.grid.edit',
            'ui.grid.rowEdit',
            'ui.grid.cellNav',
            'ui.grid.selection',
            'ui.grid.exporter',
            'ui.grid.expandable',
            'ngMaterial',
            'ngMessages',
            'ngAria',
            'ngAnimate',
            'angularSpinners',
            'ngFileUpload',
            'spring-security-csrf-token-interceptor',
            'swxSessionStorage'
        ],
        localDateMomentFormat = function ($mdDateLocaleProvider) {
            // Example of a French localization.
            //$mdDateLocaleProvider.months = ['janvier', 'février', 'mars', ...];
            //$mdDateLocaleProvider.shortMonths = ['janv', 'févr', 'mars', ...];
            //$mdDateLocaleProvider.days = ['dimanche', 'lundi', 'mardi', ...];
            //$mdDateLocaleProvider.shortDays = ['Di', 'Lu', 'Ma', ...];
            // Can change week display to start on Monday.
            //$mdDateLocaleProvider.firstDayOfWeek = 1;
            // Optional.
            //$mdDateLocaleProvider.dates = [1, 2, 3, 4, 5, 6, ...];
            // Example uses moment.js to parse and format dates.
            $mdDateLocaleProvider.parseDate = function (dateString) {
                var manipulated, year, m;

                manipulated = dateString.split('-');
                year = manipulated.shift();
                manipulated.push(year);
                m = moment(manipulated.join('/'), 'L', true);

                return m.isValid() ? m.toDate() : new Date(NaN);
            };
            $mdDateLocaleProvider.formatDate = function (date) {
                return moment(date).format('YYYY-MM-DD');
            };
            //$mdDateLocaleProvider.monthHeaderFormatter = function(date) {
            //    return myShortMonths[date.getMonth()] + ' ' + date.getFullYear();
            //};
            // In addition to date display, date components also need localized messages
            // for aria-labels for screen-reader users.
            //$mdDateLocaleProvider.weekNumberFormatter = function(weekNumber) {
            //    return 'Semaine ' + weekNumber;
            //};
            //$mdDateLocaleProvider.msgCalendar = 'Calendrier';
            //$mdDateLocaleProvider.msgOpenCalendar = 'Ouvrir le calendrier';
        },
        initialize = function ($rootScope, AuthService, USER_ROLES, $state, APP_CONSTANTS, $sessionStorage, checkUserRoleFactory) {
            $rootScope.ROLES = USER_ROLES;
            $rootScope.$on('$stateChangeStart',

                function (event, toState, toParams, fromState, fromParams) {

                    var currentUser = $sessionStorage.get(APP_CONSTANTS.CURRENT_USER);
                    if (currentUser === undefined) {
                        $rootScope.$broadcast('unauthorizedEvent');
                    }
                    ;

                    var roleCheck = checkUserRoleFactory.checkRoles(currentUser);
                    if (toState.authenticate && !roleCheck.hasRoles(toState.roles)) {
                        // User isn?t authenticated or authorized
                        window.location = APP_CONSTANTS.LOGIN_PAGE;
                        event.preventDefault();
                    }
                });

        },
        router = function ($stateProvider, $urlRouterProvider, $httpProvider, USER_ROLES, $locationProvider) {

            //$locationProvider.html5Mode(true);

            $stateProvider
                .state('login', {
                    url: '/login',
                    controller: 'LoginController',
                    templateUrl: 'login.html',
                    authenticate: false

                })
                .state('dashboard', {
                    url: '/dashboard',
                    authenticate: true,
                    views: {
                        '@': {
                            controller: 'DashboardController',
                            templateUrl: 'dashboard/dashboard.html'
                        }
                    }
                })
                .state('home', {
                    url: '/home',
                    controller: 'DashboardController',
                    templateUrl: 'main.html',
                    roles: [USER_ROLES.ADMIN, USER_ROLES.VIEW_FLIGHT_PASSENGERS, USER_ROLES.MANAGE_QUERIES, USER_ROLES.MANAGE_RULES, USER_ROLES.MANAGE_WATCHLIST],
                    authenticate: true
                })
                .state('admin', {
                    url: '/admin',
                    authenticate: true,
                    roles: [USER_ROLES.ADMIN],
                    views: {
                        "@": {
                            controller: 'AdminCtrl',
                            templateUrl: 'admin/admin.html'
                        }
                    }
                })
                .state('modifyUser', {
                    url: '/user/:userId',
                    authenticate: true,
                    roles: ['Admin'],
                    views: {
                        '@': {
                            controller: 'UserCtrl',
                            templateUrl: 'admin/user.html'
                        }
                    }
                })
                .state('upload', {
                    url: '/upload',
                    authenticate: true,
                    roles: [USER_ROLES.ADMIN],
                    views: {
                        '@': {
                            controller: 'UploadCtrl',
                            templateUrl: 'admin/upload.html'
                        }
                    }
                })
                .state('flights', {
                    url: '/flights',
                    roles: [USER_ROLES.ADMIN, USER_ROLES.VIEW_FLIGHT_PASSENGERS],
                    authenticate: true,
                    views: {
                        '@': {
                            controller: 'FlightsController as flights',
                            templateUrl: 'flights/flights.html'
                        }
                    },
                    resolve: {
                        flights: function (passengersBasedOnUserFilter, flightsModel) {
                            return passengersBasedOnUserFilter.load();
                        }
                    }
                })
                .state('queryFlights', {
                    url: '/query/flights',
                    authenticate: true,
                    roles: [USER_ROLES.ADMIN, USER_ROLES.VIEW_FLIGHT_PASSENGERS],
                    views: {
                        '@': {
                            controller: 'FlightsController',
                            templateUrl: 'flights/query-flights.html'
                        }
                    },
                    resolve: {
                        flights: function (executeQueryService) {
                            return executeQueryService.queryFlights();
                        }
                    }
                })
                .state('paxAll', {
                    url: '/passengers',
                    authenticate: true,
                    roles: [USER_ROLES.ADMIN, USER_ROLES.VIEW_FLIGHT_PASSENGERS],
                    views: {
                        '@': {
                            controller: 'PaxController',
                            templateUrl: 'pax/pax.table.html'
                        }
                    },
                    resolve: {
                        passengers: function (paxService, paxModel) {
                            return paxService.getAllPax(paxModel.model);
                        }
                    }
                })
                .state('flightpax', {
                    url: '/flightpax/:id/:flightNumber/:origin/:destination/:direction/:eta/:etd',
                    authenticate: true,
                    roles: [USER_ROLES.ADMIN, USER_ROLES.VIEW_FLIGHT_PASSENGERS],
                    views: {
                        '@': {
                            controller: 'PaxController',
                            templateUrl: 'pax/pax.table.html'
                        }
                    },
                    resolve: {
                        paxModel: function ($stateParams, paxModel) {
                            return {
                                model: paxModel.initial($stateParams),
                                reset: function () {
                                    this.model.lastName = '';
                                }
                            };
                        },
                        passengers: function (paxService, $stateParams, paxModel) {
                            //because of field/model not standard
                            $stateParams.dest = $stateParams.destination;
                            $stateParams.etaStart = $stateParams.eta;
                            $stateParams.etaEnd = $stateParams.etd;
                            return paxService.getPax($stateParams.id, paxModel.model);
                        }
                    }
                })
                .state('queryPassengers', {
                    url: '/query/passengers',
                    authenticate: true,
                    roles: [USER_ROLES.ADMIN, USER_ROLES.VIEW_FLIGHT_PASSENGERS],
                    views: {
                        '@': {
                            controller: 'PaxController',
                            templateUrl: 'pax/pax.table.html'
                        }
                    },
                    resolve: {
                        passengers: function (executeQueryService, $stateParams) {
                            var postData, query = JSON.parse(localStorage['query']);
                            postData = {
                                pageNumber: $stateParams.pageNumber || pageDefaults.pageNumber,
                                pageSize: $stateParams.pageSize || pageDefaults.pageSize,
                                query: query
                            };
                            return executeQueryService.queryPassengers(postData);
                        }
                    }
                })
                .state('detail', {
                    url: '/paxdetail/{paxId}/{flightId}',
                    authenticate: true,
                    roles: [USER_ROLES.ADMIN, USER_ROLES.VIEW_FLIGHT_PASSENGERS],
                    views: {
                        '@': {
                            controller: 'PassengerDetailCtrl',
                            templateUrl: 'pax/pax.detail.html'
                        }
                    },
                    resolve: {
                        passenger: function (paxDetailService, $stateParams) {
                            return paxDetailService.getPaxDetail($stateParams.paxId, $stateParams.flightId);
                        }
                    }
                })
                .state('build', {
                    url: '/build/:mode',
                    authenticate: true,
                    roles: [USER_ROLES.ADMIN, USER_ROLES.MANAGE_QUERIES, USER_ROLES.MANAGE_RULES],
                    views: {
                        '@': {
                            controller: 'BuildController',
                            templateUrl: 'build/build.html'
                        }
                    }
                })
                .state('watchlists', {
                    url: '/watchlists',
                    authenticate: true,
                    roles: [USER_ROLES.ADMIN, USER_ROLES.MANAGE_WATCHLIST],
                    views: {
                        '@': {
                            controller: 'WatchListController',
                            templateUrl: 'watchlists/watchlists.html'
                        }
                    }
                }).state('user-settings', {
                    url: '/user-settings',
                    authenticate: true,
                    roles: [USER_ROLES.ADMIN, USER_ROLES.VIEW_FLIGHT_PASSENGERS, USER_ROLES.MANAGE_QUERIES, USER_ROLES.MANAGE_RULES, USER_ROLES.MANAGE_WATCHLIST],
                    views: {
                        '@': {
                            controller: 'UserSettingsController',
                            templateUrl: 'user-settings/user-settings.html'
                        }
                    },
                    resolve: {
                        user: function (userService) {
                            return userService.getUserData();
                        }
                    }
                }).state('setFilter', {
                    url: '/set/filter',
                    views: {
                        '@': {
                            controller: 'FilterCtrl',
                            templateUrl: 'user-settings/filter.html'
                        }
                    }
                });

            //$urlRouterProvider.otherwise("/login");
            $httpProvider.defaults.headers.common["X-Requested-With"] = 'XMLHttpRequest';
        },

        NavCtrl = function ($scope, $http, APP_CONSTANTS, $sessionStorage, $rootScope) {
            $http.defaults.xsrfHeaderName = 'X-CSRF-TOKEN';
            $http.defaults.xsrfCookieName = 'CSRF-TOKEN';

            var lookup = {
                admin: {name: ['admin', 'addUser', 'modifyUser']},
                dashboard: {name: ['dashboard']},
                flights: {name: ['flights']},
                passengers: {name: ['paxAll', 'flightpax']},
                queries: {mode: ['query']},
                risks: {mode: ['rule']},
                watchlists: {name: ['watchlists']},
                usersettings: {name: ['user-settings', 'setFilter']},
                upload: {name: ['upload']}
            };
            $scope.onRoute = function (key) {
                return (lookup[key].name && lookup[key].name.indexOf($scope.stateName) >= 0) || (lookup[key].mode && lookup[key].mode.indexOf($scope.mode) >= 0);
            };
            $scope.showNav = function () {
                return ['queryFlights', 'queryPassengers', 'detail'].indexOf($scope.stateName) === -1;
            };
            $scope.$on('stateChanged', function (e, state, toParams) {
                $scope.stateName = state.name;
                $scope.mode = toParams.mode;
            });

            $rootScope.$on('unauthorizedEvent', function () {
                $sessionStorage.remove(APP_CONSTANTS.CURRENT_USER);
                window.location = APP_CONSTANTS.LOGIN_PAGE;
            });

            $rootScope.$on('operationNotAllowedEvent', function () {
                $scope.logout();
                window.location = APP_CONSTANTS.LOGIN_PAGE;
            });

            $scope.logout = function () {

                $http({
                    method: 'POST',
                    url: 'logout'
                }).then(function (response) {
                    if (response.status === 200 || response.status === 403) {
                        $sessionStorage.remove(APP_CONSTANTS.CURRENT_USER);
                        $rootScope.authenticated = false;
                        window.location = APP_CONSTANTS.LOGIN_PAGE;
                    }
                });
            };

        };

    app = angular
        .module('myApp', appDependencies)
        .config(router)
        .config(localDateMomentFormat)
        .constant('USER_ROLES', {
            ADMIN: 'Admin',
            VIEW_FLIGHT_PASSENGERS: 'View Flight And Passenger',
            MANAGE_QUERIES: 'Manage Queries',
            MANAGE_RULES: 'Manage Rules',
            MANAGE_WATCHLIST: 'Manage Watch List'
        })
        .constant('APP_CONSTANTS', {
            LOGIN_PAGE: '/gtas/login.html',
            // HOME_PAGE: '/gtas/home.action',
            HOME_PAGE: '/gtas/main.html',
            MAIN_PAGE: 'main.html',
            CURRENT_USER: 'CurrentUser',
            LOGIN_ERROR_MSG: ' Invalid User Name or Password. Please Try Again '
        })
        .run(initialize)
        .factory('sessionFactory', function () {
            var currentUser;

            return {
                setCurrentUser: function (user) {
                    currentUser = user;
                    currentUser.hasRole = function (requiredRole) {
                        var hasRole = false;

                        for (var i = 0; i < currentUser.roles.length; i++) {
                            if (currentUser.roles[i].toLowerCase() === requiredRole) {
                                hasRole = true;
                                break;
                            }
                        }
                        return hasRole;
                    };
                },
                getCurrentUser: function () {
                    return currentUser;
                }
            };
        })
        .factory('checkUserRoleFactory', function () {
            var currentUser;
            return {
                checkRole: function (user) {
                    currentUser = user;
                    currentUser.hasRole = function (requiredRole) {
                        var hasRole = false;

                        for (var i = 0; i < currentUser.roles.length; i++) {
                            if (String(currentUser.roles[i].roleDescription).toLowerCase() === requiredRole.toLowerCase()) {
                                hasRole = true;
                                break;
                            }
                        }
                        return hasRole;
                    };
                    return currentUser;
                },
                checkRoles: function (user) {
                    currentUser = user;
                    currentUser.hasRoles = function (requiredRoles) {
                        var hasRole = false;

                        for (var j = 0; j < requiredRoles.length; j++) {
                            for (var i = 0; i < currentUser.roles.length; i++) {
                                if (String(currentUser.roles[i].roleDescription).toLowerCase() === requiredRoles[j].toLowerCase()) {
                                    hasRole = true;
                                    break;
                                }
                            } // end of inner for loop
                            if (hasRole) {
                                break;
                            }
                        } // end of outer for loop
                        return hasRole;
                    };
                    return currentUser;
                }
            }
        })
        .directive('hasRole', function (sessionFactory, $sessionStorage, checkUserRoleFactory, APP_CONSTANTS) {
            return {
                restrict: 'A',
                link: function (scope, element, attributes) {
                    var currentUser = $sessionStorage.get(APP_CONSTANTS.CURRENT_USER);
                    if (currentUser != undefined || currentUser != null) {
                        var roleCheck = checkUserRoleFactory.checkRole(currentUser);
                        var hasRole = false;
                        var attr = String(attributes.hasRole);
                        attr = attr.split(',');
                        for (var i = 0; i < attr.length; i++) {
                            hasRole = false;
                            //console.log(attr[i].replace(/[^\w\s]/gi, '').trim());
                            if (roleCheck.hasRole(attr[i].replace(/[^\w\s]/gi, '').trim())) {
                                //console.log(attr[i].replace(/[^\w\s]/gi, '').trim() + ' role exists');
                                hasRole = true;
                                break;
                            } else {
                                hasRole = false;
                            }

                        } // end of for loop

                        if (!hasRole) {
                            element.remove();
                        }

                    }

                } // End of Function
            };
        })
        .controller('NavCtrl', NavCtrl)

        .config(function ($provide, $httpProvider) {
            $httpProvider.interceptors.push('httpSecurityInterceptor');
        })
        .factory('httpSecurityInterceptor', function ($q, $rootScope, $sessionStorage, APP_CONSTANTS) {
            return {
                responseError: function (response) {
                    if (response.status === 401) {
                        $rootScope.$broadcast('operationNotAllowedEvent');
                    }
                    if (response.status === 403) {
                        $rootScope.$broadcast('operationNotAllowedEvent');
                    }

                    return $q.reject(response);
                }
            };
        })
}());
