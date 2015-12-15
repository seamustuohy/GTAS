
(function () {
    'use strict';
    app.controller('LoginController',
        function($state, $scope, $rootScope, $q, $stateParams, userService, $mdToast, AuthService,
                 Session, sessionFactory, APP_CONSTANTS, $sessionStorage, $location) {

            $scope.currentUser = {};
            $scope.credentials = {
                j_username: '',
                j_password: ''
            };

            $scope.login = function (credentials) {
                
                AuthService.login(credentials).then(function (user){
                    if($rootScope.authenticated){
                        AuthService.getCurrentUser().then(function (user){
                            $scope.currentUser.data = user;
                        });
                    }else {
                        if(user.status == 401){
                        }
                    }
                });
            };

            $scope.$watch('currentUser.data', function (user) {
                
                if (angular.isDefined(user)) {
                    console.log("$scope.currentUser has data");
                    Session.create(user.firstName, user.userId,
                        user.roles);
                    $sessionStorage.put(APP_CONSTANTS.CURRENT_USER, user);
                    //window.location.href = APP_CONSTANTS.HOME_PAGE;
                    window.location.href = APP_CONSTANTS.MAIN_PAGE;
                    // $state.go('home',{ reload: true, inherit: false, notify: true });
                }
            });

        });

    }());

