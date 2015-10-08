(function () {
    'use strict';
    app
        .filter('hitsConditionDisplayFilter', function () {
            return function (ruleDetail) {
                return ruleDetail.ruleConditions.replace(/[$]/g, '');
            };
        })
        .filter('roleDescriptionFilter', function () {
            return function (roles) {
                return roles.map(function (role) {
                    return role.roleDescription;
                }).join(', ');
            };
        })
        .filter('userStatusFilter', function () {
            return function (value) {
                return !!value;
            };
        })
        /* NOT USED */
        .filter('capitalize', function () {
            return function (input) {
                return (!!input) ? input.replace(/([^\W_]+[^\s-]*) */g, function (txt) {
                    return txt.charAt(0).toUpperCase() + txt.substr(1).toLowerCase();
                }) : '';
            };
        })
        /* NOT USED */
        .filter('orderObjectBy', function () {
            return function (items, field, reverse) {
                var filtered = [];
                items.forEach(function (item) {
                    filtered.push(item);
                });
                filtered.sort(function (a, b) {
                    return (a[field] > b[field] ? 1 : -1);
                });
                if (reverse) { filtered.reverse(); }
                return filtered;
            };
        });
})();
