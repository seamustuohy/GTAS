app.controller('UserCtrl', function ($scope, $stateParams, userService, $mdToast, $location) {
    'use strict';
    var backToAdmin = function (data) {
            console.log(data);
            $location.path('/admin');
        },
        SelectedRole = function (role, selected, disabled) {
            this.role = role;
            this.selected = selected;
            this.disabled = disabled;
        },
        enableRoles = function () {
            $scope.user.roles.forEach(function (obj) {
                if (obj.roleDescription !== 'Admin') {
                    $scope.roles[obj.roleDescription] = {
                        role: obj.roleDescription,
                        selected: true,
                        disabled: false
                    };
                } else if (obj.roleDescription === 'Admin') {
                    $scope.roles[obj.roleDescription] = {
                        role: obj.roleDescription,
                        selected: true,
                        disabled: false
                    };
                    $scope.rolesLookup.forEach(function (roleLookup) {
                        if (roleLookup.roleDescription !== obj.roleDescription) {
                            $scope.roles[roleLookup.roleDescription] = {
                                role: roleLookup.roleDescription,
                                selected: false,
                                disabled: false
                            };
                        }
                    });
                }
            });
        },
        setUser = function (users) {
            users.forEach(function (user) {
                if (user.userId === $stateParams.userId) {
                    $scope.user = user;
                }
            });
            $scope.persistUser = $scope.user;
            $scope.persistUser.activeName = $scope.persistUser.userId;
            $scope.persistUser.activeWord = $scope.persistUser.password;
            $scope.userStatus.enabled = $scope.user.active === 1;
            enableRoles();
        };
    $scope.userStatus = {
        enabled: true
    };
    $scope.action = $stateParams.userId === 'new' ? 'create' : 'modify';
    $scope.userPasswordChanged = false;
    $scope.persistUser = {
        activeName: '',
        activeWord: '',
        userId: '',
        firstName: '',
        lastName: '',
        active: 1,
        roles: []
    };

    $scope.roles = [];
    $scope.rolesLookup = [];
    $scope.selectedRoles = [];

    $scope.radioData = [
        {label: 'Enable', value: 1},
        {label: 'Disable', value: 0}
    ];

    //$scope.enableDisableUser = function (value) {
    //    $scope.persistUser.active = value === true ? 1 : 0;
    //};

    $scope.Init = function () {
        userService.getRoles()
            .then(function (roles) {
                // responses are redirected to confirmation page nothing do here..
                $scope.rolesLookup = roles;
                $scope.setRoles();
            });

        if ($scope.action === 'modify') {
            userService.getAllUsers().then(setUser);
        }
    };

    $scope.refreshSelectedRoles = function (sourceRoles, targetRoles) {
        var roleExists;
        sourceRoles.forEach(function (item) {
            roleExists = false;
            targetRoles.forEach(function (targetRole) {
                if (targetRole.role === item) {
                    roleExists = true;
                }
            });
            if (!roleExists) {
                $scope.selectedRoles.push(new SelectedRole(item, true, false));
            }
        });
    };

    $scope.sortRoles = function (items, field, reverse) {
        var filtered = [];
        items.forEach(function (item) {
            filtered.push(item);
        });
        filtered.sort(function (a, b) {
            return (a[field] > b[field] ? 1 : -1);
        });
        if (reverse) {
            filtered.reverse();
        }
        return filtered;
    };

    $scope.selectedRoles = [];

    $scope.toggleRole = function (item) {
        if (item === 'Admin') {
            if ($scope.roles[item].selected === true) {
                $scope.rolesLookup.forEach(function (obj) {
                    if (obj.roleDescription !== item) {
                        $scope.roles[obj.roleDescription] = {
                            role: obj.roleDescription,
                            selected: false,
                            disabled: true
                        };
                    }
                });
                return;
            }
            if ($scope.roles[item].selected === false) {
                $scope.roles[item] = {role: 'Admin', selected: false, disabled: false};
                $scope.setRoles();
                return;
            }
        }
        if (item !== 'Admin') {
            $scope.roles['Admin'] = {role: 'Admin', selected: false, disabled: false};
        }
    };

    $scope.getRoleFromLookup = function (roleDescription) {
        $scope.rolesLookup.forEach(function (obj) {
            if (obj.roleDescription === roleDescription) {
                return obj;
            }
        });
        return null;
    };

    $scope.setRoles = function () {
        $scope.rolesLookup.forEach(function (obj) {
            $scope.roles[obj.roleDescription] = {
                role: obj.roleDescription,
                selected: false,
                disabled: false
            };
            //$scope.selectedRoles.push(new SelectedRole($scope.rolesLookup[i].roleDescription, false, false));
        });
    };

    $scope.populateSelectedRoles = function () {
        var roleObject;
        $scope.rolesLookup.forEach(function (obj) {
            roleObject = $scope.roles[obj.roleDescription];
            if (roleObject.selected) {
                $scope.persistUser.roles.push($scope.getRoleFromLookup(roleObject.role));
            }
        });
    };

    $scope.encodePassword = function () {
        $scope.userPasswordChanged = true;
    };

    $scope.Init();

    $scope.showRoleToast = function () {
        $mdToast.show(
            $mdToast.simple()
                .content('One or More User Roles Have To Be Selected')
                .position("top right")
                .hideDelay(3000)
        );
    };

    $scope.showFieldToast = function () {
        $mdToast.show(
            $mdToast.simple()
                .content('All Input Fields are Required')
                .position("top right")
                .hideDelay(3000)
        );
    };

    $scope.saveUser = function (event) {
        if ($scope.persistUser.activeName !== undefined && $scope.persistUser.activeName !== null && $scope.persistUser.activeWord !== undefined && $scope.persistUser.activeWord !== null) {
            $scope.persistUser.userId = $scope.persistUser.activeName.replace(/\s+/g, '');
            $scope.persistUser.password = $scope.persistUser.activeWord.replace(/\s+/g, '');
        }

        $scope.populateSelectedRoles();
        if ($scope.persistUser.roles.length === 0) {
            $scope.showRoleToast();
            event.preventDefault();
            return;
        }
        userService[$scope.action === 'modify' ? 'updateUser' : 'createUser']($scope.persistUser).then(backToAdmin);
    };
});
