(function () {
    'use strict';
    app
        .factory('GridControl', function () {
            return function ($scope) {
                var today = moment().format('YYYY-MM-DD'),
                    pageOfPages = function (currentPage, pageCount) {
                        return today + (pageCount === 1 ? '' : '\t' + currentPage.toString() + ' of ' + pageCount.toString());
                    };
                $scope.gridOpts = {
                    paginationPageSize: 10,
                    paginationPageSizes: [],
                    enableFiltering: true,
                    enableCellEditOnFocus: false,
                    showGridFooter: true,
                    multiSelect: false,
                    enableGridMenu: true,
                    enableSelectAll: false,
                    exporterPdfDefaultStyle: {fontSize: 9},
                    exporterPdfTableStyle: {margin: [10, 10, 10, 10]},
                    exporterPdfTableHeaderStyle: {
                        fontSize: 10,
                        bold: true,
                        italics: true
                    },
                    exporterPdfFooter: function (currentPage, pageCount) {
                        return {
                            text: pageOfPages(currentPage, pageCount),
                            style: 'footerStyle'
                        };
                    },
                    exporterPdfCustomFormatter: function (docDefinition) {
                        docDefinition.pageMargins = [0, 40, 0, 40];
                        docDefinition.styles.headerStyle = {
                            fontSize: 22,
                            bold: true,
                            alignment: 'center',
                            lineHeight: 1.5
                        };
                        docDefinition.styles.footerStyle = {
                            fontSize: 10,
                            italic: true,
                            alignment: 'center'
                        };
                        return docDefinition;
                    },
                    exporterPdfOrientation: 'landscape',
                    exporterPdfPageSize: 'LETTER',
                    exporterPdfMaxGridWidth: 600,
                    exporterCsvLinkElement: angular.element(document.querySelectorAll(".custom-csv-link-location"))
                };

            };
        })
        .factory('Base64', function () {
            var keyStr = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=';
            return {
                encode: function (input) {
                    var output = "";
                    var chr1, chr2, chr3 = "";
                    var enc1, enc2, enc3, enc4 = "";
                    var i = 0;

                    do {
                        chr1 = input.charCodeAt(i++);
                        chr2 = input.charCodeAt(i++);
                        chr3 = input.charCodeAt(i++);

                        enc1 = chr1 >> 2;
                        enc2 = ((chr1 & 3) << 4) | (chr2 >> 4);
                        enc3 = ((chr2 & 15) << 2) | (chr3 >> 6);
                        enc4 = chr3 & 63;

                        if (isNaN(chr2)) {
                            enc3 = enc4 = 64;
                        } else if (isNaN(chr3)) {
                            enc4 = 64;
                        }

                        output = output +
                            keyStr.charAt(enc1) +
                            keyStr.charAt(enc2) +
                            keyStr.charAt(enc3) +
                            keyStr.charAt(enc4);
                        chr1 = chr2 = chr3 = "";
                        enc1 = enc2 = enc3 = enc4 = "";
                    } while (i < input.length);

                    return output;
                },
                decode: function (input) {
                    var output = "";
                    var chr1, chr2, chr3 = "";
                    var enc1, enc2, enc3, enc4 = "";
                    var i = 0;

                    // remove all characters that are not A-Z, a-z, 0-9, +, /, or =
                    var base64test = /[^A-Za-z0-9\+\/\=]/g;
                    if (base64test.exec(input)) {
                        window.alert("There were invalid base64 characters in the input text.\n" +
                            "Valid base64 characters are A-Z, a-z, 0-9, '+', '/',and '='\n" +
                            "Expect errors in decoding.");
                    }
                    input = input.replace(/[^A-Za-z0-9\+\/\=]/g, "");

                    do {
                        enc1 = keyStr.indexOf(input.charAt(i++));
                        enc2 = keyStr.indexOf(input.charAt(i++));
                        enc3 = keyStr.indexOf(input.charAt(i++));
                        enc4 = keyStr.indexOf(input.charAt(i++));

                        chr1 = (enc1 << 2) | (enc2 >> 4);
                        chr2 = ((enc2 & 15) << 4) | (enc3 >> 2);
                        chr3 = ((enc3 & 3) << 6) | enc4;

                        output = output + String.fromCharCode(chr1);

                        if (enc3 != 64) {
                            output = output + String.fromCharCode(chr2);
                        }
                        if (enc4 != 64) {
                            output = output + String.fromCharCode(chr3);
                        }

                        chr1 = chr2 = chr3 = "";
                        enc1 = enc2 = enc3 = enc4 = "";

                    } while (i < input.length);

                    return output;
                }
            };
        })
        .service('UserService', function ($http, $q) {
            function handleError( response ) {
                if (! angular.isObject( response.data ) || ! response.data.message) {
                    return( $q.reject( "An unknown error occurred." ) );
                }
                return( $q.reject( response.data.message ) );
            }

            function handleSuccess( response ) {
                return( response.data );
            }

            function getRoles() {
                var request = $http({
                    method: "get",
                    url: "/gtas/roles/" ,
                    params: {
                        action: "get"
                    }
                });
                return(request.then(handleSuccess, handleError) );
            }

            function updateUser(user) {

                var PUT_USER_URL = '/gtas/users/' + user.userId;
                var request = $http({
                    method: "put",
                    url: PUT_USER_URL ,
                    data:user
                });
                return( request.then( handleSuccess, handleError ) );
            }

            function createUser(user) {

                var POST_USER_URL = '/gtas/users/' + user.userId;
                var request = $http({
                    method: "post",
                    url:POST_USER_URL ,
                    data:user

                });
                return( request.then( handleSuccess, handleError ) );
            }

            return {
                getRoles: getRoles,
                updateUser: updateUser,
                createUser: createUser
            };
        })
        .service("gridOptionsLookupService", function (uiGridConstants) {
            var today = moment().format('YYYY-MM-DD'),
                pageOfPages = function (currentPage, pageCount) {
                    return today + (pageCount === 1 ? '' : '\t' + currentPage.toString() + ' of ' + pageCount.toString());
                },
                standardOptions = {
                    paginationPageSize: 10,
                    paginationPageSizes: [],
                    enableHorizontalScrollbar: 0,
                    enableVerticalScrollbar: 0,                    
                    enableFiltering: true,
                    enableCellEditOnFocus: false,
                    showGridFooter: true,
                    multiSelect: false,
                    enableGridMenu: true,
                    enableSelectAll: false,
                    exporterPdfDefaultStyle: {fontSize: 9},
                    exporterPdfTableStyle: {margin: [10, 10, 10, 10]},
                    exporterPdfTableHeaderStyle: {
                        fontSize: 10,
                        bold: true,
                        italics: true
                    },
                    exporterPdfFooter: function (currentPage, pageCount) {
                        return {
                            text: pageOfPages(currentPage, pageCount),
                            style: 'footerStyle'
                        };
                    },
                    exporterPdfCustomFormatter: function (docDefinition) {
                        docDefinition.pageMargins = [0, 40, 0, 40];
                        docDefinition.styles.headerStyle = {
                            fontSize: 22,
                            bold: true,
                            alignment: 'center',
                            lineHeight: 1.5
                        };
                        docDefinition.styles.footerStyle = {
                            fontSize: 10,
                            italic: true,
                            alignment: 'center'
                        };
                        return docDefinition;
                    },
                    exporterPdfOrientation: 'landscape',
                    exporterPdfPageSize: 'LETTER',
                    exporterPdfMaxGridWidth: 600,
                    exporterCsvLinkElement: angular.element(document.querySelectorAll(".custom-csv-link-location"))
                },
                gridOptions = {
                    flights: {
                        enableSorting: false,
                        multiSelect: false,
                        enableFiltering: false,
                        enableRowSelection: true,
                        enableSelectAll: false,
                        enableRowHeaderSelection: false,
                        enableGridMenu: false,
                        paginationPageSizes: [10, 25, 50],
                        paginationPageSize: 10,
                        useExternalPagination: true,
                        useExternalSorting: true,
                        useExternalFiltering: true
                    },
                    passengers: {
                        enableSorting: false,
                        multiSelect: false,
                        enableFiltering: false,
                        enableRowSelection: false,
                        enableSelectAll: false,
                        enableGridMenu: false,
                        paginationPageSizes: [15, 25, 50],
                        paginationPageSize: 15,
                        useExternalPagination: true,
                        useExternalSorting: true,
                        useExternalFiltering: true,
                        expandableRowTemplate: '<div ui-grid="row.entity.subGridOptions"></div>'
                    },
                    query : standardOptions,
                    rule: standardOptions,
                    watchlist: standardOptions
                },
                columns = {
                    flights: [
                        {
                            name: 'P',
                            field: 'passengerCount',
                            width: 50,
                            enableFiltering: false,
                            cellTemplate: '<md-button class="md-primary" style="min-width: 0; margin: 0 auto; width: 100%;" ng-click="grid.appScope.passengerNav(row)">{{COL_FIELD}}</md-button>'
                        },
                        {
                            name: 'H',
                            field: 'ruleHitCount',
                            width: 50,
                            enableFiltering: false
                        },
                        {
                            name: 'L',
                            field: 'listHitCount',
                            width: 50,
                            enableFiltering: false
                        },
                        {
                            name: 'Carrier',
                            field: 'carrier',
                            width: 75
                        },
                        {
                            name: 'Flight',
                            field: 'flightNumber',
                            width: 75
                        },
                        {
                            name: 'Dir',
                            field: 'direction',
                            width: 50
                        },
                        {
                            name: 'ETA',
                            displayName: 'ETA',
                            field: 'eta'
                        },
                        {
                            name: 'ETD',
                            displayName: 'ETD',
                            field: 'etd'
                        },
                        {
                            name: 'Origin',
                            field: 'origin'
                        },
                        {
                            name: 'OriginCountry',
                            displayName: "Country",
                            field: 'originCountry'
                        },
                        {
                            name: 'Dest',
                            field: 'destination'
                        },
                        {
                            name: 'DestCountry',
                            displayName: "Country",
                            field: 'destinationCountry'
                        }
                    ],
                    passengers: [
                        //needs gridService reference
                    ],
                    query: [
                        {
                            name: "title",
                            displayName: "Name",
                            field: "title",
                            cellTemplate: '<md-button aria-label="title" ng-click="grid.api.selection.selectRow(row.entity)">{{COL_FIELD}}</md-button>',
                            enableCellEdit: false,
                            enableColumnMenu: false
                        }, {
                            name: "description",
                            field: "description",
                            cellTemplate: '<md-button aria-label="description" ng-click="grid.api.selection.selectRow(row.entity)">{{COL_FIELD}}</md-button>',
                            enableCellEdit: false,
                            enableColumnMenu: false
                        }
                    ],
                    rule: [
                        {
                            name: "title",
                            displayName: "Name",
                            field: "title",
                            cellTemplate: '<md-button aria-label="title" ng-click="grid.api.selection.selectRow(row.entity)">{{COL_FIELD}}</md-button>',
                            enableCellEdit: false,
                            enableColumnMenu: false
                        }, {
                            name: "description",
                            field: "description",
                            cellTemplate: '<md-button aria-label="description" ng-click="grid.api.selection.selectRow(row.entity)">{{COL_FIELD}}</md-button>',
                            enableCellEdit: false,
                            enableColumnMenu: false
                        }, {
                            name: "startDate",
                            field: "startDate",
                            cellTemplate: '<md-button aria-label="start date" ng-click="grid.api.selection.selectRow(row.entity)">{{COL_FIELD}}</md-button>',
                            enableCellEdit: false,
                            enableColumnMenu: false
                        }, {
                            name: "endDate",
                            field: "endDate",
                            cellTemplate: '<md-button aria-label="end date" ng-click="grid.api.selection.selectRow(row.entity)">{{COL_FIELD}}</md-button>',
                            enableCellEdit: false,
                            enableColumnMenu: false
                        }, {
                            name: "enabled",
                            field: "enabled",
                            cellTemplate: '<md-button aria-label="enabled" ng-click="grid.api.selection.selectRow(row.entity)">{{COL_FIELD}}</md-button>',
                            enableCellEdit: false,
                            enableColumnMenu: false
                        },
                        {
                            name: "modifiedOn",
                            field: "modifiedOn",
                            cellTemplate: '<md-button aria-label="modified" ng-click="grid.api.selection.selectRow(row.entity)">{{row.entity.modifiedBy}} {{row.entity.modifiedOn}}</md-button>',
                            enableCellEdit: false,
                            enableColumnMenu: false,
                        }
                    ]
                };
            return {
                paginationOptions: {
                    pageNumber: 1,
                    pageSize: 10,
                    sort: null
                },
                getGridOptions: function (entity) {
                    return gridOptions[entity];
                },
                getLookupColumnDefs: function (entity) {
                    return columns[entity] || [];
                }
            };
        })
        .service("gridService", function ($http, $q) {
            /**
             * Take the number of rows in the grid and calculate the
             * correct 'height' style to show all of the data at once.
             * We use this as part of auto-resizing grids.
             */
            var calculateGridHeight = function (numRows) {
                    var MIN_NUM_ROWS = 10;

                    var rowHeight = 30;
                    var headerHeight = 30;
                    var n = numRows >= MIN_NUM_ROWS ? numRows : MIN_NUM_ROWS;
                    return {
                        height: (n * rowHeight + 3 * headerHeight) + "px"
                    };
                },
                /**
                 * @return the cell color for the rule hit and watch list hit
                 * columns.  cellValue can either be a count or a boolean value.
                 */
                colorHits = function (grid, row, col) {
                    if (grid.getCellValue(row ,col) > 0) {
                        return 'red';
                    }
                },
                ruleHit = function(grid, row, col) {
                    return grid.getCellValue(row ,col) ? 'rule-hit' : 'invisible';
                },
                anyWatchlistHit = function(grid, row) {
                    if (row.entity.onWatchList || row.entity.onWatchListDoc) {
                        return 'watchlist-hit';
                    }
                };

            return ({
                anyWatchlistHit: anyWatchlistHit,
                calculateGridHeight: calculateGridHeight,
                colorHits: colorHits,
                ruleHit: ruleHit
            });
        })
        .service("watchListService", function ($http, $q) {
            var baseUrl = '/gtas/wl/',
                handleError = function (response) {
                    if (!angular.isObject(response.data) || !response.data.message) {
                        return ($q.reject("An unknown error occurred."));
                    }
                    return ($q.reject(response.data.message));
                },
                handleSuccess = function (response) {
                    return (response);
                };

            return {
                compile: function () {
                    var request = $http({
                        method: "get",
                        url: baseUrl + 'compile'
                    });

                    return (request.then(handleSuccess, handleError));
                },
                getTabs: function () {
                    var request = $http({
                        method: "get",
                        url: baseUrl + 'list'
                    });

                    return (request.then(handleSuccess, handleError));
                },
                getListItems: function (entity, listTypeName) {
                    if (!entity || !listTypeName) {
                        return false;
                    }
                    var request = $http({
                        method: "get",
                        url: baseUrl + entity + "/" + listTypeName
                    });

                    return (request.then(handleSuccess, handleError));
                },
                deleteItem: function (listTypeName, entity, id, terms) {
                    var request,
                        url = baseUrl + 'adelorie',
                        action = !id ? 'Create' : !terms ? 'Delete' : 'Update';

                    if (!listTypeName || !entity || !id) {
                        return false;
                    }

                    request = $http({
                        method: 'put',
                        url: url,
                        data: {
                            "@class": "gov.gtas.model.watchlist.json.WatchlistSpec",
                            "name": listTypeName,
                            "entity": entity,
                            "watchlistItems": [{
                                "id": id,
                                "action": action,
                                "terms": null
                            }]
                        }
                    });

                    return (request.then(handleSuccess, handleError));
                },
                addItem: function (listTypeName, entity, id, terms) {
                    var request,
                        url = baseUrl + 'adelorie',
                        action = !id ? 'Create' : !terms ? 'Delete' : 'Update';

                    if (!listTypeName || !entity || !terms) {
                        return false;
                    }

                    request = $http({
                        method: 'post',
                        url: url,
                        data: {
                            "@class": "gov.gtas.model.watchlist.json.WatchlistSpec",
                            "name": listTypeName,
                            "entity": entity,
                            "watchlistItems": [{
                                "id": id,
                                "action": action,
                                "terms": terms
                            }]
                        }
                    });

                    return (request.then(handleSuccess, handleError));
                },
                updateItem: function (listTypeName, entity, id, terms) {
                    var request,
                        url = baseUrl + 'adelorie',
                        action = !id ? 'Create' : !terms ? 'Delete' : 'Update';

                    if (!listTypeName || !entity || !id || !terms) {
                        return false;
                    }

                    request = $http({
                        method: 'put',
                        url: url,
                        data: {
                            "@class": "gov.gtas.model.watchlist.json.WatchlistSpec",
                            "name": listTypeName,
                            "entity": entity,
                            "watchlistItems": [{
                                "id": id,
                                "action": action,
                                "terms": terms
                            }]
                        }
                    });

                    return (request.then(handleSuccess, handleError));
                },
                createListType: function (listName, columns) {
                    //watchlist.types[listName] = {columns: columns, data: []};
                    //return watchlist.types;
                }
            };
        })
        .service("jqueryQueryBuilderService", function ($http, $q) {
            var baseUrl,
                URLS = {
                    query: '/gtas/query/',
                    rule: '/gtas/udr/'
                },
                handleError = function (response) {
                    if (!angular.isObject(response.data) || !response.data.message) {
                        return ($q.reject("An unknown error occurred."));
                    }
                    return ($q.reject(response.data.message));
                },
                handleSuccess = function (response) {
                    return (response.data);
                },
                services = {
                    init: function (mode) {
                        baseUrl = URLS[mode];
                    },
                    loadRuleById: function (ruleId) {
                        var request;

                        if (!ruleId) { return false; }

                        request = $http({
                            method: "get",
                            url: [baseUrl, ruleId].join('')
                        });

                        return (request.then(handleSuccess, handleError));
                    },
                    delete: function (ruleId) {
                        var request;

                        if (!ruleId) { return false; }

                        request = $http({
                            method: 'delete',
                            url: [baseUrl, ruleId].join('')
                        });

                        return (request.then(handleSuccess, handleError));
                    },
                    save: function (data) {
                        var method, request, url;

                        if (data.id === null) {
                            method = 'post';
                            url = baseUrl;
                        } else {
                            method = 'put';
                            url = baseUrl + data.id;
                        }

                        request = $http({
                            method: method,
                            url: url,
                            data: data
                        });

                        return (request.then(handleSuccess, handleError));
                    },
                    getList: function () {
                        var request;

                        request = $http({
                            method: "get",
                            url: baseUrl
                        });

                        return (request.then(handleSuccess, handleError));
                    }
                };

            // Return public API.
            return ({
                init: services.init,
                getList: services.getList,
                loadRuleById: services.loadRuleById,
                delete: services.delete,
                save: services.save
            });
        })
        .service("executeQueryService", function ($http, $q) {
            var serviceURLs = {
                    flights: '/gtas/query/queryFlights/',
                    passengers: '/gtas/query/queryPassengers/'
                },
                queryFlights = function (qbData) {
                    var dfd = $q.defer();
                    dfd.resolve($http({
                        method: 'post',
                        url: serviceURLs.flights,
                        data: qbData
                    }));
                    return dfd.promise;
                },
                queryPassengers = function (qbData) {
                    var dfd = $q.defer();
                    dfd.resolve($http({
                        method: 'post',
                        url: serviceURLs.passengers,
                        data: qbData
                    }));
                    return dfd.promise;
                };
            // Return public API.
            return ({
                queryFlights: queryFlights,
                queryPassengers: queryPassengers
            });
        });
}());
