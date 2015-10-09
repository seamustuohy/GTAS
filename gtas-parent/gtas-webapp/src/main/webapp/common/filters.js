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
                if (reverse) {
                    filtered.reverse();
                }
                return filtered;
            };
        })
        .filter('flagImageFilter', function () {
            return function (hits) {
                if (hits === '0') {
                    return 'padding: 1.5px; color: #007500;';
                }
                if (hits != '0') {
                    return 'padding: 1.5px; color: #d9534f;';
                }
                return 'color: #007500;';
            };
        })
        .filter('watchListImageFilter', function () {
            return function (hits) {
                if (hits === 1) {
                    return 'icon-user-check glyphiconWLPax col-sm-4';
                } // glyphiconWLPax
                if (hits === 2) {
                    return 'icon-book glyphiconWLDocs col-sm-4';
                } // glyphiconWLDocs
                if (hits === 3) {
                    return 'icon-user-check glyphiconWLPaxDocs col-sm-4';
                } // glyphiconWLPaxDocs  glyphicon-user
                return '';
            };
        })
        .filter('watchListImageInsertFilter', function () {
            return function (hits) {
                if (hits === 1) {
                    return '';
                } // glyphiconWLPax
                if (hits === 2) {
                    return '';
                } // glyphiconWLDocs
                if (hits === 3) {
                    return 'icon-book';
                } // glyphiconWLPaxDocs
                return '';
            };

        })
        .filter('watchListImageColorFilter', function () {
            return function (hits) {
                if (hits === 1) {
                    return 'padding: 7.5px; color: #d9534f;';
                } // glyphiconWLPax
                if (hits === 2) {
                    return 'padding: 7.5px; color: #d9534f;';
                } // glyphiconWLDocs
                if (hits === 3) {
                    return 'padding: 7.5px; color: #d9534f;';
                } // glyphiconWLPaxDocs
                return '';
            };
        });
}())
