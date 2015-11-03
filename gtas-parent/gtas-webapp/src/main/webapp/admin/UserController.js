app.controller('UserCtrl', function ($scope, $stateParams, userService, $mdToast, $location) {
    'use strict';
    var backToAdmin = function () { $location.path('/admin'); },
        setNonAdminRoles = function (adminSelectedState) {
            $scope.roles.forEach(function (role) {
                if (role.roleDescription !== 'Admin') {
                    role.disabled = adminSelectedState;
                    if (adminSelectedState) {
                        role.selected = false;
                    }
                }
            });
        },
        setUser = function (users) {
            users.forEach(function (user) {
                if (user.userId === $stateParams.userId) {
                    var userSelectedRoles = user.roles.map(function (role) { return role.roleDescription; }),
                        admin = $scope.roles[3]; //not sure why this is 4th...
                    $scope.user = user;
                    $scope.roles.forEach(function (role) {
                        if (userSelectedRoles.indexOf(role.roleDescription) >= 0) {
                            role.selected = true;
                        }
                    });
                    setNonAdminRoles(admin.selected);
                }
            });
            $scope.action = $scope.user === undefined ? 'create' : 'modify';
            if ($scope.action === 'modify') {
                $scope.persistUser = $scope.user;
            }
        },
        getSelectedRoles = function () {
            var selectedRoles = [];
            $scope.roles.forEach(function (roleObject) {
                if (roleObject.selected && !(roleObject.disabled)) {
                    selectedRoles.push({
                        roleId: roleObject.roleId,
                        roleDescription: roleObject.roleDescription
                    });
                }
            });

            return selectedRoles;
        },
        alertUser = function (content) {
            $mdToast.show(
                $mdToast.simple()
                    .content(content)
                    .position("top right")
                    .hideDelay(3000)
            );
        },
        scopeRoles = function (roles) {
            $scope.roles = [];
            roles.forEach(function (obj) {
                $scope.roles.push({
                    roleDescription: obj.roleDescription,
                    roleId: obj.roleId,
                    selected: false,
                    disabled: false
                });
            });
        };

    $scope.persistUser = { password: '', userId: '', firstName: '', lastName: '', active: 1 };
    $scope.roles = [];
    $scope.Init = function () {
        userService.getRoles().then(scopeRoles);
        userService.getAllUsers().then(setUser);
    };

    $scope.toggleRole = function (role) {
        if (role.roleDescription === 'Admin') {
            setNonAdminRoles(role.selected);
        }
    };

    $scope.saveUser = function () {
        if ($scope.persistUser.userId !== null && $scope.persistUser.password !== null) {
            $scope.persistUser.userId = $scope.persistUser.userId.trim();
            $scope.persistUser.password = $scope.persistUser.password.trim();
        }
        $scope.persistUser.roles = getSelectedRoles();
        if ($scope.persistUser.userId.length === 0 || $scope.persistUser.password.length === 0) {
            alertUser('userId and | or password cannot be blank space(s)');
            return;
        }
        if ($scope.persistUser.roles.length === 0) {
            alertUser('One or More User Roles Have To Be Selected');
            return;
        }
        userService[$scope.action === 'modify' ? 'updateUser' : 'createUser']($scope.persistUser).then(backToAdmin);
    };

    $scope.Init();
});
