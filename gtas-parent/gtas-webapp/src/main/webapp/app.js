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
            'ui.grid.importer',
            'ui.grid.exporter',
            'ui.grid.expandable',
            'ngMaterial',
            'ngMessages',
            'ngAria',
            'ngAnimate',
            'angularSpinners',
            'ngFileUpload',
            'spring-security-csrf-token-interceptor',
            'swxSessionStorage',
            'ngCookies',
            'pascalprecht.translate'
        ],
        language = function ($translateProvider) {
      
    		$translateProvider.useUrlLoader('/gtas/messageBundle/');
    		$translateProvider.useCookieStorage();
    		$translateProvider.preferredLanguage('en');
    		$translateProvider.fallbackLanguage('en');
    		
        	
		},
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

                function (event, toState) {

                    var currentUser = $sessionStorage.get(APP_CONSTANTS.CURRENT_USER);
                    if (currentUser === undefined) {
                        $rootScope.$broadcast('unauthorizedEvent');
                    }
                    var roleCheck = checkUserRoleFactory.checkRoles(currentUser);
                    if (toState.authenticate && !roleCheck.hasRoles(toState.roles)) {
                        // User isn?t authenticated or authorized
                        window.location = APP_CONSTANTS.LOGIN_PAGE;
                        event.preventDefault();
                    }
                });

        },
        router = function ($stateProvider, $urlRouterProvider, $httpProvider, USER_ROLES) {

            $stateProvider
                .state('login', {
                    url: '/login',
                    controller: 'LoginController',
                    templateUrl: 'login.html',
                    authenticate: false

                })
                .state('dashboard', {
                    url: '/dashboard',
                    roles: [USER_ROLES.ADMIN, USER_ROLES.VIEW_FLIGHT_PASSENGERS, USER_ROLES.MANAGE_QUERIES, USER_ROLES.MANAGE_RULES, USER_ROLES.MANAGE_WATCHLIST],
                    authenticate: true,
                    views: {
                        '@': {
                            controller: 'DashboardController',
                            templateUrl: 'dashboard/dashboard.html'
                        }
                    }
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
                        //TODO research why this resolve doesn't stick...
                        // I remember reading why on cbp machine
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
                            controllerAs: 'build',
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
                })
                .state('userSettings', {
                    url: '/userSettings',
                    authenticate: true,
                    roles: [USER_ROLES.ADMIN, USER_ROLES.VIEW_FLIGHT_PASSENGERS, USER_ROLES.MANAGE_QUERIES, USER_ROLES.MANAGE_RULES, USER_ROLES.MANAGE_WATCHLIST],
                    views: {
                        '@': {
                            controller: 'UserSettingsController',
                            templateUrl: 'userSettings/userSettings.html'
                        }
                    },
                    resolve: {
                        user: function (userService) {
                            return userService.getUserData();
                        }
                    }
                });
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
                userSettings: {name: ['userSettings', 'setFilter']},
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
        .config(language)
        .constant('USER_ROLES', {
            ADMIN: 'Admin',
            VIEW_FLIGHT_PASSENGERS: 'View Flight And Passenger',
            MANAGE_QUERIES: 'Manage Queries',
            MANAGE_RULES: 'Manage Rules',
            MANAGE_WATCHLIST: 'Manage Watch List'
        })
        .constant('APP_CONSTANTS', {
            LOGIN_PAGE: '/gtas/login.html',
            HOME_PAGE: '/gtas/main.html',
            MAIN_PAGE: 'main.html#/dashboard',
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
        .directive('linearChart', function ($window) {
                return {
                    restrict: 'EA',
                    template: "<div class=\"container\"><div class=\"row\"><abc><lin></lin></abc></div></div>",
                    link: function (scope, elem, attrs) {

                        (function () {


                            d3.bullet = function () {
                                var orient = "left", // TODO top & bottom
                                    reverse = false,
                                    duration = 0,
                                    ranges = bulletRanges,
                                    markers = bulletMarkers,
                                    measures = bulletMeasures,
                                    width = 380,
                                    height = 30,
                                    tickFormat = null;

                                // For each small multiple…
                                function bullet(g) {
                                    g.each(function (d, i) {
                                        var rangez = ranges.call(this, d, i).slice().sort(d3.descending),
                                            markerz = markers.call(this, d, i).slice().sort(d3.descending),
                                            measurez = measures.call(this, d, i).slice().sort(d3.descending),
                                            g = d3.select(this);

                                        // Compute the new x-scale.
                                        var x1 = d3.scale.linear()
                                            .domain([0, Math.max(rangez[0], markerz[0], measurez[0])])
                                            .range(reverse ? [width, 0] : [0, width]);

                                        // Retrieve the old x-scale, if this is an update.
                                        var x0 = this.__chart__ || d3.scale.linear()
                                                .domain([0, Infinity])
                                                .range(x1.range());

                                        // Stash the new scale.
                                        this.__chart__ = x1;

                                        // Derive width-scales from the x-scales.
                                        var w0 = bulletWidth(x0),
                                            w1 = bulletWidth(x1);

                                        // Update the range rects.
                                        var range = g.selectAll("rect.range")
                                            .data(rangez);

                                        range.enter().append("rect")
                                            .attr("class", function (d, i) {
                                                return "range s" + i;
                                            })
                                            .attr("width", w0)
                                            .attr("height", height)
                                            .transition()
                                            .duration(2000)
                                            .ease("linear")
                                            .attr("x", reverse ? x0 : 0)
                                            .transition()
                                            .duration(duration)
                                            .transition()
                                            .duration(2000)
                                            .ease("linear")

                                            .attr("width", w1)
                                            .transition()
                                            .duration(2000)
                                            .ease("linear")

                                            .attr("x", reverse ? x1 : 0);

                                        range.transition()
                                            .duration(duration)
                                            .attr("x", reverse ? x1 : 0)
                                            //.transition()
                                            //.duration(2000)
                                            //.ease("linear")
                                            .attr("width", w1)

                                            .attr("height", height);

                                        // Update the measure rects.
                                        var measure = g.selectAll("rect.measure")
                                            .data(measurez);

                                        measure.enter().append("rect")
                                            .attr("class", function (d, i) {
                                                return "measure s" + i;
                                            })
                                            .attr("width", w0)
                                            .attr("height", height / 3)
                                            .attr("x", reverse ? x0 : 0)
                                            .attr("y", height / 3)
                                            .transition()
                                            .duration(duration)
                                            .attr("width", w1)
                                            .attr("x", reverse ? x1 : 0);

                                        measure.transition()
                                            .duration(duration)

                                            //.transition()
                                            //.duration(2000)
                                            //.ease("linear")

                                            .attr("width", w1)
                                            .attr("height", height / 3)
                                            .attr("x", reverse ? x1 : 0)
                                            .attr("y", height / 3);

                                        // Update the marker lines.
                                        var marker = g.selectAll("line.marker")
                                            .data(markerz);

                                        marker.enter().append("line")
                                            .attr("class", "marker")

                                            //.transition()
                                            //.duration(2000)
                                            //.ease("linear")

                                            .attr("x1", x0)
                                            .attr("x2", x0)
                                            .attr("y1", height / 6)
                                            .attr("y2", height * 5 / 6)
                                            .transition()
                                            .duration(duration)

                                            .transition()
                                            .duration(2000)
                                            .ease("linear")

                                            .attr("x1", x1)
                                            .attr("x2", x1);

                                        marker.transition()
                                            .duration(duration)
                                            //.transition()
                                            //.duration(2000)
                                            //.ease("linear")

                                            .attr("x1", x1)
                                            .attr("x2", x1)
                                            .attr("y1", height / 6)
                                            .attr("y2", height * 5 / 6);

                                        // Compute the tick format.
                                        var format = tickFormat || x1.tickFormat(8);

                                        // Update the tick groups.
                                        var tick = g.selectAll("g.tick")
                                            .data(x1.ticks(8), function (d) {
                                                return this.textContent || format(d);
                                            });

                                        // Initialize the ticks with the old scale, x0.
                                        var tickEnter = tick.enter().append("g")
                                            .attr("class", "tick")
                                            .attr("transform", bulletTranslate(x0))
                                            .style("opacity", 1e-6);

                                        tickEnter.append("line")
                                            .attr("y1", height)
                                            .attr("y2", height * 7 / 6);

                                        tickEnter.append("text")
                                            .attr("text-anchor", "middle")
                                            .attr("dy", "1em")
                                            .attr("y", height * 7 / 6)
                                            .text(format);

                                        // Transition the entering ticks to the new scale, x1.
                                        tickEnter.transition()
                                            .duration(duration)
                                            .attr("transform", bulletTranslate(x1))
                                            .style("opacity", 1);

                                        // Transition the updating ticks to the new scale, x1.
                                        var tickUpdate = tick.transition()
                                            .duration(duration)
                                            .attr("transform", bulletTranslate(x1))
                                            .style("opacity", 1);

                                        tickUpdate.select("line")
                                            .attr("y1", height)
                                            .attr("y2", height * 7 / 6);

                                        tickUpdate.select("text")
                                            .attr("y", height * 7 / 6);

                                        // Transition the exiting ticks to the new scale, x1.
                                        tick.exit().transition()
                                            .duration(duration)
                                            .attr("transform", bulletTranslate(x1))
                                            .style("opacity", 1e-6)
                                            .remove();
                                    });
                                    d3.timer.flush();
                                }

                                // left, right, top, bottom
                                bullet.orient = function (x) {
                                    if (!arguments.length) return orient;
                                    orient = x;
                                    reverse = orient == "right" || orient == "bottom";
                                    return bullet;
                                };

                                // ranges (bad, satisfactory, good)
                                bullet.ranges = function (x) {
                                    if (!arguments.length) return ranges;
                                    ranges = x;
                                    return bullet;
                                };

                                // markers (previous, goal)
                                bullet.markers = function (x) {
                                    if (!arguments.length) return markers;
                                    markers = x;
                                    return bullet;
                                };

                                // measures (actual, forecast)
                                bullet.measures = function (x) {
                                    if (!arguments.length) return measures;
                                    measures = x;
                                    return bullet;
                                };

                                bullet.width = function (x) {
                                    if (!arguments.length) return width;
                                    width = x;
                                    return bullet;
                                };

                                bullet.height = function (x) {
                                    if (!arguments.length) return height;
                                    height = x;
                                    return bullet;
                                };

                                bullet.tickFormat = function (x) {
                                    if (!arguments.length) return tickFormat;
                                    tickFormat = x;
                                    return bullet;
                                };

                                bullet.duration = function (x) {
                                    if (!arguments.length) return duration;
                                    duration = x;
                                    return bullet;
                                };

                                return bullet;
                            };

                            function bulletRanges(d) {
                                return d.ranges;
                            }

                            function bulletMarkers(d) {
                                return d.markers;
                            }

                            function bulletMeasures(d) {
                                return d.measures;
                            }

                            function bulletTranslate(x) {
                                return function (d) {
                                    return "translate(" + x(d) + ",0)";
                                };
                            }

                            function bulletWidth(x) {
                                var x0 = x(0);
                                return function (d) {
                                    return Math.abs(x(d) - x0);
                                };
                            }

                        })();


                        var margin = {top: 5, right: 40, bottom: 20, left: 120},
                            width = 960 - margin.left - margin.right,
                            height = 55 - margin.top - margin.bottom;

                        var chart = d3.bullet()
                            .width(width)
                            .height(height);

                        d3.json("data/bullets.json", function (error, data) {
                            if (error) throw error;

                            var svg = d3.select("abc").selectAll("lin")
                                .data(data)
                                .enter().append("svg")
                                .attr("class", "bullet")
                                .attr("width", width + margin.left + margin.right)
                                .attr("height", height + margin.top + margin.bottom)
                                .append("g")
                                .attr("transform", "translate(" + margin.left + "," + margin.top + ")")
                                .call(chart);

                            var title = svg.append("g")
                                .style("text-anchor", "end")
                                .attr("transform", "translate(-6," + height / 2 + ")");

                            title.append("text")
                                .attr("class", "title")
                                .text(function (d) {
                                    return d.title;
                                });

                            title.append("text")
                                .attr("class", "subtitle")
                                .attr("dy", "1em")
                                .text(function (d) {
                                    return d.subtitle;
                                });


                        });

                    }
                };
            }
        ) // End of Linear Chart
        .directive('barChart', function ($window) {
            return {
                restrict: 'EA',
                template: "<svg1></svg1>",
                link: function (scope, elem, attrs) {

                    var margin = {top: 10, right: 20, bottom: 20, left: 20},
                        width = 1600 - margin.left - margin.right,
                        height = 450 - margin.top - margin.bottom;

                    var x0 = d3.scale.ordinal()
                        .rangeRoundBands([0, width], .6);

                    var x1 = d3.scale.ordinal();

                    var yaxistext = '# of Messages Loaded';

                    var y = d3.scale
                        .linear()
                        //        .transition()
                        //        .duration(2000)
                        //        .ease("circle")
                        .range([height, 0]);

                    var color = d3.scale.ordinal()
                        .range(["#3A9DC6", "#376180"]);

                    var xAxis = d3.svg.axis()
                        .scale(x0)
                        .orient("bottom");

                    var yAxis = d3.svg.axis()
                        .scale(y)
                        .orient("left")
                        .tickFormat(d3.format("3d"));


                    var svg = d3.select("svg1").append("svg").attr("class", 'col-sm-offset-0')
                        .attr("width", width + margin.left + margin.right)
                        .attr("height", height + margin.top + margin.bottom)
                        .append("g")
                        .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

                    //var fvg = d3.select("svg1").append("svg");

                    d3.csv("data/data.csv", function (error, data) {
                        if (error) throw error;

                        var ageNames = d3.keys(data[0]).filter(function (key) {
                            return key !== "State";
                        });

                        data.forEach(function (d) {
                            d.ages = ageNames.map(function (name) {
                                return {name: name, value: +d[name]};
                            });
                        });

                        x0.domain(data.map(function (d) {
                            return d.State;
                        }));
                        x1.domain(ageNames).rangeRoundBands([0, x0.rangeBand()]);
                        y.domain([0, d3.max(data, function (d) {
                            return d3.max(d.ages, function (d) {
                                return d.value;
                            });
                        })]);

                        svg.append("g")
                            .attr("class", "x axis")
                            .attr("transform", "translate(0," + height + ")")
                            .call(xAxis);

                        svg.append("g")

                            .attr("class", "y axis")
                            .call(yAxis)
                            .append("text")

                            .attr("transform", "rotate(-90)")

                            .attr("y", 6)

                            .attr("dy", ".71em")
                            .style("text-anchor", "end")
                            .text(yaxistext);


                        var state = svg.selectAll(".state")
                            .data(data)
                            .enter().append("g")

                            .attr("class", "state")

                            .attr("transform", function (d) {
                                return "translate(" + x0(d.State) + ",0)";
                            })

                            ;

                        state.transition()
                            .duration(2000)
                            .ease("circle")
                        ;

                        state.selectAll("rect")

                            .data(function (d) {
                                return d.ages;
                            })
                            .enter()

                            .append("rect")
                            .attr("width", x1.rangeBand())
                            .attr("x", function (d) {
                                return x1(d.name);
                            })
                            .attr("y", height)
                            .attr("height", 0)
                            .transition()
                            .duration(2000)
                            .ease("linear")
                            .attr("y", function (d) {
                                return y(d.value);
                            })
                            //        .transition()
                            //        .duration(2000)
                            //        .ease("linear")
                            .attr("height", function (d) {
                                return height - y(d.value);
                            })
                            //        .transition()
                            //        .duration(2000)
                            //        .ease("circle")
                            .style("fill", function (d) {
                                return color(d.name);
                            })
                        ;

                        var legend = svg.selectAll(".legend")
                            .data(ageNames.slice().reverse())
                            .enter().append("g")
                            .attr("class", "legend")
                            .attr("transform", function (d, i) {
                                return "translate(0," + i * 20 + ")";
                            });

                        legend.append("rect")
                            .attr("x", width - 18)
                            .attr("width", 18)
                            .attr("height", 18)
                            .style("fill", color);

                        legend.append("text")
                            .attr("x", width - 29)
                            .attr("y", 9)
                            .attr("dy", ".35em")
                            .style("text-anchor", "end")
                            .text(function (d) {
                                return d;
                            });

                    });

                }
            }
        })// END of Bar Chart Directive

        .directive('ytdChart', function ($window) {
                return {
                    restrict: 'EA',
                    template: "<html> <head> <link rel=\"shortcut icon\" href=\"data:image/x-icon;,\" type=\"image/x-icon\"> <style> path.line { fill: none; stroke: #000; stroke-width: 3px;}.axis { shape-rendering: crispEdges;}.x.axis line, .x.axis path,.y.axis line, .y.axis path { fill: none; stroke: #000;}th, td { font-family: \"Open Sans\"; font-size:1em; padding-left:7px; padding-right:7px; padding-top:4px; padding-bottom:4px; vertical-align:middle; text-align:center;}th { color:white; background-color:#4682B4;}td { border:1px solid steelblue;}td.rowkey { text-align:left;}tr:hover { color:#000000; background-color:#E0E0E0;}tr[chosen=true] { background-color:lightgray;} </style> </head> <body> <div class=\"container\"><div class=\"row\"> <div style='padding-left: 40px;' id=\"table\"></div> <div id=\"plot\"></div></div></div> </body></html>",
                    link: function (scope, elem, attrs) {

                        (function () {

                            var xAxisGroup = null;
                            var yAxisGroup = null;


                                // This function builds a line plot on the g.#lineplot built by setupPlot.js
                                // It creates a line for each row selected in the associated MultiTable.

                                var drawLinePlot = function (stats, height, width, margin,
                                                             transDur, reportStats) {

                                    // Make a list of the selected statistics
                                    var statsToPlot = [];

                                    d3.selectAll("tr[chosen='true']")
                                        .each(function (d) {
                                            statsToPlot.push(d.key);
                                        });

                                    // If the function is run without any stats chosen, remove any
                                    // existing lines and their g elements
                                    if (statsToPlot.length < 1) {
                                        d3.selectAll("path.line")
                                            .transition().duration(transDur)
                                            .style("opacity", 1e-6)
                                            .remove();

                                        d3.selectAll("g.line").transition().duration(transDur).remove();

                                        return;
                                    }
                                    ;

                                    // Subset to the chosen data
                                    var plotdata = stats.filter(function (d) {
                                        return (statsToPlot.indexOf(d.stat_name) > -1)
                                    });


                                    // Nest each statistic's data in its own object
                                    var nested = d3.nest()
                                        .key(function (d) {
                                            return d.stat_name;
                                        })
                                        .entries(plotdata);


                                    // Set up the x-scale
                                    var xScale = d3.scale.linear()
                                        .domain([0,
                                            d3.nest()
                                                .key(function (d) {
                                                    return d.datestring;
                                                })
                                                .entries(plotdata)
                                                .length])
                                        .range([0, width - margin.left - margin.right])


                                    // Set up the y-scale
                                    var yScale = d3.scale.linear()
                                        .domain([0, d3.max(plotdata, function (d) {
                                            return d.qtr_result;
                                        })])
                                        .range([height - margin.top - margin.bottom, 0])


                                    // Set up the x-axis
                                    var xAxis = d3.svg.axis()
                                        .scale(xScale)
                                        .orient("bottom")
                                        .tickPadding(8);

                                    // Set up the y-axis
                                    var yAxis = d3.svg.axis()
                                        .scale(yScale)
                                        .orient("right")
                                        .tickPadding(8);


                                    // Set up a scale function for coloring the paths
                                    var colorScale = d3.scale.category10();

                                    var colorStat = {};
                                    var i=0;
                                    for (i = 0; i < reportStats.length; i++) {
                                        colorStat[reportStats[i]] = colorScale(i);
                                    }


                                    // Create a line generator
                                    var generateLine = d3.svg.line()
                                        .interpolate("step-after")
                                        .x(function (d, i) {
                                            return xScale(i);
                                        })
                                        .y(function (d) {
                                            return yScale(d.qtr_result);
                                        })

                                    // Create a second line generator for transitioning lines -
                                    // this gives them a starting and ending place on the axis
                                    var generateNullLine = d3.svg.line()
                                        .interpolate("step-after")
                                        .x(function (d, i) {
                                            return xScale(i);
                                        })
                                        .y(function () {
                                            return yScale(0);
                                        })


                                    // Find the g.#lineplot element - build the plot on this
                                    var lineplot = d3.select("#lineplot");

                                    // Set up a transition function to keep things DRY
                                    var transit = lineplot.transition().duration(transDur);

                                    // Each statistic should have a group for its plot elements
                                    // function(d)... lets d3 key the groups based on which statistic
                                    // they contain, so that the correct statistics and entered and exited
                                    // (as opposed to unkeyed joins, which only use the data's array index
                                    var lineGroups = lineplot.selectAll("g.line")
                                        .data(nested, function (d) {
                                            return d.key;
                                        });


                                    // Add the paths
                                    lineGroups.enter()
                                        .append("g")
                                        .attr("class", "line")
                                        .append("svg:path")
                                        .style("opacity", 1e-6)
                                        .attr("class", "line")
                                        .attr("d", function (d) {
                                            return generateNullLine(d.values);
                                        })
                                        .style("stroke", function (d) {
                                            return colorStat[d.key];
                                        })
                                        .transition().duration(transDur)
                                        .style("opacity", 1)
                                        .attr("d", function (d) {
                                            return generateLine(d.values);
                                        })

                                    //              .style("stroke", function(d) { return colorStat[d.key]; })


                                    // Transition deselected paths out
                                    lineGroups.exit().selectAll("path")
                                        .transition().duration(transDur)
                                        .attr("d", function (d) {
                                            return generateNullLine(d.values);
                                        })
                                        .remove();

                                    // Remove deselected g elements, too
                                    lineGroups.exit().transition().duration(transDur).remove();


                                    // Transition the remaining paths
                                    transit.selectAll("path.line")
                                        .attr("d", function (d) {
                                            return generateLine(d.values);
                                        });



                                    // Add or transition the x-axis
                                    if (!xAxisGroup) {
                                        xAxisGroup = lineplot.append("g")
                                            .attr("class", "x axis")
                                            .attr("transform", "translate(0, " + yScale.range()[0] + ")")
                                            .call(xAxis)
                                    }
                                    else {
                                        transit.select("g.x.axis").call(xAxis)
                                    }


                                    // Add or transition the y-axis
                                    if (!yAxisGroup) {
                                        yAxisGroup = lineplot.append("g")
                                            .attr("class", "y axis")
                                            .attr("transform", "translate(" + xScale.range()[1] + ", 0)")
                                            .call(yAxis)
                                    }
                                    else {
                                        transit.select("g.y.axis").call(yAxis)
                                    }


                                };

                            var toggleStat = function (stat_name) {

                                // Toggle the statistic's row
                                // Get the current value
                                var current = d3.select("tr[rowstat='" + stat_name + "']")
                                    .attr("chosen")

                                // Invert it. When the current toggle status is "true", the comparison
                                // below returns "false"; when the current status is "false", it returns
                                // "true". A bit opaque, but I can't store proper booleans in HTML attr.
                                d3.select("tr[rowstat='" + stat_name + "']")
                                    .attr("chosen", current == "false")


                                // Toggle the statistic in the plot
                                drawLinePlot(stats, height, width, margin, transDur, reportStats);


                            };
                            window.toggleStat = toggleStat;

                                // Define dimensions of the plot
                                var margin = {top: 120, right: 60, bottom: 60, left: 80};
                                var height = 500;
                                var width = 900;

                                 // Define the transition duration
                                var transDur = 700;

                                // Set up a global variable for the names of the stats reported here
                                // (in hopes of making it easier to keep line colors consistent
                                var reportStats = [];

                                var stats;


                                // Load in the CRD quarterly stats table
                                d3.csv("data/qtr_stats.csv", function (crd) {

                                    // Format the variables as neeeded
                                    crd.forEach(function (d) {
                                //        d.stat_year = +d.stat_year;
                                        d.stat_qtr = +d.stat_qtr;
                                        d.datestring = d.stat_qtr
                                //            + " Q" + d.stat_qtr
                                        ;

                                        d.qtr_result = +d.qtr_result;
                                    });

                                    // Subset to two sets of stats:
                                    // 1. Active Cases Reported for all metro residents and, separately,
                                    // just Denver residents.
                                    // 2. Active and latent tx starts and visits, for everyone
                                    var other_stats = ["ORD", "ATL", "DFW", "LAX", "DEN", "ATL"];

                                    var qtrly = crd.filter(function (d) {
                                        return (d.stat_name == "ORD" &&
                                            d.pt_group == "All Patients") ||

                                            ((other_stats.indexOf(d.stat_name) > -1) &&
                                            d.pt_group == "All Patients")

                                            ;
                                    });


                                    // Assign the data outside of the function for later use
                                    stats = qtrly;


                                    // Load the initial stats table
                                    makeMultiTable(stats);

                                    // Make a vector for all of the stats, so that plot attributes can be
                                    // kept consistent - probably a better way to do this.
                                    d3.selectAll("tbody tr")
                                        .each(function (d) {
                                            reportStats.push(d.key);
                                        });


                                    // Setup the line plot
                                    setupPlot(height, width, margin);


                                });

                                // This function creates a table with a row for each statistic in a flat data
                                // object and a column for each time period in the data object.


                                var makeMultiTable = function (stats) {

                                    // Set up the column names
                                    // One set for the year supercolumns
                                    var yrCols = d3.nest()
                                        .key(function (d) {
                                            return d.stat_year;
                                        })
                                        .rollup(function (d) {
                                            return d.length;
                                        })
                                        .entries(stats.filter(function (d) {
                                            return d.stat_name == "ATL";
                                        }));


                                    // And one for the quarter columns
                                //    var qtrCols = d3.keys(
                                //        d3.nest()
                                //            .key(function(d) { return d.datestring; })
                                //            .map(stats)
                                //    );

                                    // Add an empty column for the statistic name
                                    yrCols.unshift("");
                                //    qtrCols.unshift("");


                                    // Nest data within each statistic
                                    var aggstats = d3.nest()
                                        .key(function (d) {
                                            return d.stat_name;
                                        })

                                        .entries(stats);

                                    // Create the table
                                    var table = d3.select("#table").append("table");
                                    var thead = table.append("thead");
                                    var tbody = table.append("tbody");

                                    var yearCols = {"year": "2015"};
                                    var quarterCols = ["Airport", "Total Flights",
                                        "Rule Hits", "Watchlist Hits"];

                                    // Append the year headers
                                    thead.append("tr")
                                        .selectAll("th")
                                        .data(yrCols)
                                        //.data(yearCols)
                                        .enter()
                                        .append("th")
                                        .text(function (d) {
                                            return d.key;
                                        })
                                        .attr("colspan", function (d) {
                                            return d.values;
                                        })

                                    // Append the quarter headers
                                    thead.append("tr")
                                        .selectAll("th")
                                        //.data(qtrCols)
                                        .data(quarterCols)
                                        .enter()
                                        .append("th")
                                        .text(function (column) {
                                            return column.substr(0, 15);
                                        })




                                    // Bind each statistic to a line of the table
                                    var rows = tbody.selectAll("tr")
                                        .data(aggstats)
                                        .enter()
                                        .append("tr")
                                        .attr("rowstat", function (d) {
                                            return d.key;
                                        })
                                        .attr("chosen", false)
                                        .attr("onclick", function (d) {
                                            return "toggleStat('" + d.key + "')";
                                        })


                                    // Add statistic names to each row
                                    var stat_cells = rows.append("td")
                                        .text(function (d) {
                                            return d.key;
                                        })
                                        .attr("class", "rowkey")


                                    // Fill in the cells.  The data -> d.values pulls the value arrays from
                                    // the data assigned above to each row.
                                    // The unshift crap bumps the data cells over one - otherwise, the first
                                    // result value falls under the statistic labels.
                                    var res_cells = rows.selectAll("td")
                                        .data(function (d) {
                                            var x = d.values;
                                            x.unshift({qtr_result: ""});
                                            return x;
                                        })
                                        .enter()
                                        .append("td")
                                        .text(function (d) {
                                            return d3.format(",d")(d.qtr_result);
                                        })


                                };


                        var toggleStat = function (stat_name) {

                            // Toggle the statistic's row
                            // Get the current value
                            var current = d3.select("tr[rowstat='" + stat_name + "']")
                                .attr("chosen")

                            // Invert it. When the current toggle status is "true", the comparison
                            // below returns "false"; when the current status is "false", it returns
                            // "true". A bit opaque, but I can't store proper booleans in HTML attr.
                            d3.select("tr[rowstat='" + stat_name + "']")
                                .attr("chosen", current == "false")


                            // Toggle the statistic in the plot
                            drawLinePlot(stats, height, width, margin, transDur, reportStats);


                        };

                                // This function does the one-time setup for the plot - which is basically
                                // just setting up this g.#lineplot

                                var setupPlot = function (height, width, margin) {

                                    // Create an svg element for the plot
                                    d3.select("#plot").append("svg:svg")
                                        .attr("width", width)
                                        .attr("height", height)
                                        .append("g")
                                        .attr("transform",
                                            "translate(" + margin.left + "," + margin.top + ")")
                                        .attr("id", "lineplot");

                                    // Create global variables for the axes - no need to populate them just yet
                                     xAxisGroup = null;
                                     yAxisGroup = null;

                                };





                    })();

                    }
                };
            }
        ) // End of YTD Chart

        .controller('NavCtrl', NavCtrl)

        .config(function ($provide, $httpProvider) {
            $httpProvider.interceptors.push('httpSecurityInterceptor');
        })
        .factory('httpSecurityInterceptor', function ($q, $rootScope) {
            return {
                responseError: function (response) {
                    if ([401, 403].indexOf(response.status) >= 0) {
                        $rootScope.$broadcast('operationNotAllowedEvent');
                    }

                    return $q.reject(response);
                }
            };
        });
}());
