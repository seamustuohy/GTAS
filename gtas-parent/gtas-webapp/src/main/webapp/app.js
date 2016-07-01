/*
 * All GTAS code is Copyright 2016, Unisys Corporation.
 * 
 * Please see LICENSE.txt for details.
 */
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
            'pascalprecht.translate',
            'ngIdle'
        ],
        language = function ($translateProvider) {
      
    		$translateProvider.useUrlLoader('/gtas/messageBundle/');
    		$translateProvider.useCookieStorage();
    		$translateProvider.preferredLanguage('en');
    		$translateProvider.fallbackLanguage('en');
    		$translateProvider.useSanitizeValueStrategy('escape');
    		
        	
		},
		idleWatchConfig = function(IdleProvider, KeepaliveProvider, TitleProvider){
			TitleProvider.enabled(false);
			IdleProvider.interrupt('notDefault');
			IdleProvider.idle(540);
			IdleProvider.timeout(60);
			IdleProvider.keepalive(false);
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
        },
        initialize = function ($rootScope, AuthService, userService, USER_ROLES, $state, APP_CONSTANTS, $sessionStorage, checkUserRoleFactory, Idle, $mdDialog) {
            $rootScope.ROLES = USER_ROLES;
            $rootScope.$on('$stateChangeStart',

                function (event, toState) {
            		Idle.watch();
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
           $rootScope.triggerIdle = function(){
        	   	//Prevent triggers pre-login
        	   	if(Idle.running()){
        	   		Idle.watch();
           		}
           };
           
           $rootScope.$on('IdleStart', function(){
        	   $rootScope.showConfirm();
           });
           
           $rootScope.$on('IdleEnd', function(){
        	   //Keep session alive via small request
        	  userService.getUserData().then(console.log('No longer Idle'));
           });
           
           $rootScope.$on('IdleTimeout', function(){
        	  $mdDialog.hide();
        	  $rootScope.userTimedout = true;
        	  $rootScope.$broadcast('unauthorizedEvent');
        	  window.location.href = APP_CONSTANTS.LOGIN_PAGE +"?userTimeout";
           });
           
           $rootScope.showConfirm = function() {
        	   var confirm = $mdDialog.confirm({
	        	   parent: angular.element(document.body),
	               template:'<md-dialog ng-cloak>'+
	            	   '<form>'+
	            	   		'<md-dialog-content>'+
	            	   			'<div class="md-dialog-content" style="padding-top:0px;padding-bottom:0px;">'+
	            	   				'<h5 class="md-title"><strong>Idle Session Timeout Warning</strong></h5>'+
	            	   					'<div class="_md-dialog-content-body ng-scope"><p class="ng-binding">'+
	            	   						'Your session has been idle for too long. If you wish to maintain your session please click the button below.</p></div>'+
	            	   					'<span idle-countdown="countdown"> You will be logged out automatically in <strong style="font-size:xx-large;">{{countdown}}</strong> seconds. </span>'+
	            	   			'</div>'+
	            	   		'</md-dialog-content>'+
	            	   	'<md-dialog-actions layout="row">'+
        	      '<md-dialog-actions layout="row" class="layout-row">'+      	  	  
        	      '<md-button ng-click="dialog.hide()">Continue Session</md-button>'+
        	    '</md-dialog-actions>'+
        	  '</form>'+
        	  '</md-dialog>'})
        	  
	           $mdDialog.show(confirm).then(function() {
	            	      Idle.watch();
	            	    }, function() {
	            	      return false;
	            	    
	           });
       	  };
       	  
       	  $rootScope.hide = function(){
       		  $mdDialog.hide();
       	  };

        },
        router = function ($stateProvider, $urlRouterProvider, $httpProvider, USER_ROLES) {

            $stateProvider
                .state('login', {
                    url: '/login',
                    controller: 'LoginController',
                    templateUrl: 'login.html',
                    authenticate: false

                })
                .state('dash', { // temporary mapping to show sample dashboard data
                    url: '/dash',
                    roles: [USER_ROLES.ADMIN, USER_ROLES.VIEW_FLIGHT_PASSENGERS, USER_ROLES.MANAGE_QUERIES, USER_ROLES.MANAGE_RULES, USER_ROLES.MANAGE_WATCHLIST],
                    authenticate: true,
                    views: {
                        '@': {
                            controller: 'DashboardController',
                            templateUrl: 'dashboard/dash.html'
                        }
                    }
                        ,
                        resolve: {
                            sampleData: function(){
                                return true;
                            },
                            ytdRuleHits: function(dashboardService){
                                return dashboardService.getYtdRulesCount();
                            },
                            ytdAirportStats: function (dashboardService) {
                                return dashboardService.getYtdAirportStats();
                            }
                        }

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
                    ,
                    resolve: {
                        sampleData: function(){
                            return false;
                        },
                        ytdRuleHits: function(dashboardService){
                            return dashboardService.getYtdRulesCount();
                        },
                        ytdAirportStats: function (dashboardService) {
                            return dashboardService.getYtdAirportStats();
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
                    roles: [USER_ROLES.ADMIN],
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
                        flights: function (passengersBasedOnUserFilter, flightsModel) {
                            return passengersBasedOnUserFilter.load();
                        }
                    }
                })
                .state('cases', {
                    url: '/cases',
                    authenticate: true,
                    roles: [USER_ROLES.ADMIN, USER_ROLES.VIEW_FLIGHT_PASSENGERS],
                    views: {
                        '@': {
                            controller: 'CasesCtrl',
                            templateUrl: 'cases/cases.html'
                        },
                    },
                    resolve: {
                        	newCases: function(caseService){
                        		return caseService.getAllCases();
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
                           //removed return due to it being an empty call to the service, returning an erroneous 400 Bad Request. 
                           //Kept resolve rather than restructuring flights.html to not use flights entity as it was.
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
                            return paxService.getPassengersBasedOnUser(paxModel);
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
                        	$stateParams.dest = $stateParams.destination;
                            $stateParams.etaStart = $stateParams.eta;
                            $stateParams.etaEnd = $stateParams.etd;
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
                        },
                        user: function (userService) {
                            return userService.getUserData();
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
            $httpProvider.defaults.withCredentials = false;
        },

        NavCtrl = function ($scope, $http, APP_CONSTANTS, $sessionStorage, $rootScope, $cookies) {
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
                upload: {name: ['upload']},
                cases: {name: ['cases']}                
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
                    if (response.status === 200 || response.status === 403 || response.status === 405) {
                        var cookies = $cookies.getAll();
                        angular.forEach(cookies, function (v, k) {
                            if(APP_CONSTANTS.LOCALE_COOKIE_KEY != k) {
                                $cookies.remove(k);
                            }
                        });
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
        .config(idleWatchConfig)
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
            LOCALE_COOKIE_KEY: 'myLocaleCookie',
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
                                            .attr("width", w1)
                                            .attr("height", height / 3)
                                            .attr("x", reverse ? x1 : 0)
                                            .attr("y", height / 3);

                                        // Update the marker lines.
                                        var marker = g.selectAll("line.marker")
                                            .data(markerz);

                                        marker.enter().append("line")
                                            .attr("class", "marker")
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
        .directive('barChart', function ($window, dashboardService, $http) {
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

                    var today = moment();
                    var credentials = {
                        beforeDate: '',
                        startDate: '',
                        endDate: ''
                    };
                    credentials.startDate = today.format('YYYY-MM-DD');
                    credentials.endDate = today.format('YYYY-MM-DD');
                    credentials.beforeDate = today.format('YYYY-MM-DD');

                    var csvData = '';

                    var request = $http({
                        method: 'get',
                        url: '/gtas/getMessagesCount',
                        params: {
                            startDate: credentials.startDate,
                            endDate: credentials.endDate
                        }
                    });

                    request.then(JSON2CSV, handleError);

                    function handleError(response) {

                    }

                    function JSON2CSV(objArray) {
                        var array = typeof objArray != 'object' ? JSON.parse(JSON.stringify(objArray.data)) : objArray.data;
                        var str = '';
                        var line = '';
                        var dataString = "";
                        var csvContent = "";

                        displayData (objArray.data);
                        return str;
                    }

                    d3.csv("data/data.csv", function (error, data) {
                        // displayData(data);
                    });

                    function displayData (data) {
                        //if (error) throw error;

                        var ageNames = d3.keys(data[0]).filter(function (key) {
                            return key.toUpperCase() !== "STATE";
                        });

                        data.forEach(function (d) {
                            d.ages = ageNames.map(function (name) {
                                return {name: name, value: +d[name]};
                            });
                        });

                        x0.domain(data.map(function (d) {
                            return d.state;
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
                                return "translate(" + x0(d.state) + ",0)";
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
                            .attr("height", function (d) {
                                return height - y(d.value);
                            })
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

                    } // End of Display Data

                }
            }
        })// END of Bar Chart Directive

        // Begin Sample Data Bar Chart
        .directive('sampleBarChart', function ($window) {
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
                            .attr("height", function (d) {
                                return height - y(d.value);
                            })
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
        })// END of Sample Data Bar Chart Directive

        .controller('NavCtrl', NavCtrl)

        .config(function ($provide, $httpProvider) {
            $httpProvider.interceptors.push('httpSecurityInterceptor');
        })
        .factory('httpSecurityInterceptor', function ($q, $rootScope ) {
            return {
                request: function(config){
                	$rootScope.triggerIdle();
                	return config;
                },
                responseError: function (response) {
                    if ([401, 403].indexOf(response.status) >= 0) {
                        $rootScope.$broadcast('operationNotAllowedEvent');
                    }

                    return $q.reject(response);
                }
            };
        }
    );
}());
