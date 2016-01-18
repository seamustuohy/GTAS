(function () {
    'use strict';
    app
        .service('errorService', function ($http, $q) {
            var GET_ERROR_RECORDS_URL = "/gtas/errorlog";

            function handleError(response) {
                if (response.data.message === undefined) {
                    return $q.reject("An unknown error occurred.");
                }
                return $q.reject(response.data.message);
            }

            function handleSuccess(response) {
                return response.data;
            }

            return {
                getErrorData: function (code, commence, fin) {
                    var st = commence != null ? moment(commence).format('YYYY-MM-DD') : null;
                    var nd = fin != null ? moment(fin).format('YYYY-MM-DD') : null;
                    var urlString = GET_ERROR_RECORDS_URL;
                    if (code != null || st != null || nd != null) {
                        urlString += '?';
                    }
                    var sep = '';
                    if (code != null) {
                        urlString += sep.concat('code=', code);
                        sep = '&';
                    }
                    if (st != null) {
                        urlString += sep.concat('startDate=', st);
                        sep = '&';
                    }
                    if (nd != null) {
                        urlString += sep.concat('endDate=', nd);
                    }
                    var request = $http({
                        method: "get",
                        url: urlString
                    });

                    return (request.then(handleSuccess, handleError));
                }
            };
        })
        /* audit log viewer service */
        .service('auditService', function ($http, $q) {
            var GET_AUDIT_RECORDS_URL = "/gtas/auditlog";

//            function handleError(response) {
//                if (response.data.message === undefined) {
//                    return $q.reject("An unknown error occurred.");
//                }
//                return $q.reject(response.data.message);
//            }

            function handleSuccess(response) {
            	if(response.status > 299){
                    if (response.data.message === undefined) {
                        return $q.reject("An unknown error occurred.");
                    }
                    return $q.reject(response.data.message);
            	}
                return response.data;
            }

            return {
                getAuditData: function (action, user, commence, fin) {
                    var st = commence != null ? moment(commence).format('YYYY-MM-DD') : null;
                    var nd = fin != null ? moment(fin).format('YYYY-MM-DD') : null;
                    var urlString = GET_AUDIT_RECORDS_URL;
                    if (action != null || user != null || st != null || nd != null) {
                        urlString += '?';
                    }
                    var sep = '';
                    if (user != null) {
                        urlString += sep.concat('user=', user);
                        sep = '&';
                    }
                    if (action != null) {
                        urlString += sep.concat('action=', action);
                        sep = '&';
                    }
                    if (st != null) {
                        urlString += sep.concat('startDate=', st);
                        sep = '&';
                    }
                    if (nd != null) {
                        urlString += sep.concat('endDate=', nd);
                    }
                    var request = $http({
                        method: "get",
                        url: urlString
                    });

                    return (request.then(handleSuccess));
                },
                auditActions: [
                    'ALL_ACTIONS',
                    'CREATE_UDR',
                    'UPDATE_UDR',
                    'UPDATE_UDR_META',
                    'DELETE_UDR',
                    'CREATE_WL',
                    'UPDATE_WL',
                    'DELETE_WL',
                    'LOAD_APIS',
                    'LOAD_PNR',
                    'CREATE_USER',
                    'UPDATE_USER',
                    'SUSPEND_USER',
                    'DELETE_USER',
                    'TARGETING_RUN',
                    'LOADER_RUN'
                ]
            };
        })
        .service('userService', function ($http, $q) {
            var USER_ROLES_URL = "/gtas/roles/",
                USERS_URL = "/gtas/users/",
                USER_URL = "/gtas/user/";

            function handleError(response) {
                if (response.data.message !== undefined) {
                    return $q.reject("An unknown error occurred.");
                }
                return $q.reject(response.data.message);
            }

            function handleSuccess(response) {
                return response.data;
            }

            function getRoles() {
                var request = $http({
                    method: "get",
                    url: USER_ROLES_URL,
                    params: {
                        action: "get"
                    }
                });
                return (request.then(handleSuccess, handleError));
            }

            //function saveUser(user, method) {
            //    return request = $http({
            //        method: method,
            //        url: baseUsersURL + user.userId,
            //        data: user
            //    });
            //}
            function updateUser(user) {
                var request = $http({
                    method: "put",
                    url: USERS_URL + user.userId,
                    data: user
                });
                return (request.then(handleSuccess, handleError));
            }

            function createUser(user) {
                var request = $http({
                    method: "post",
                    url: USERS_URL + user.userId,
                    data: user

                });
                return (request.then(handleSuccess, handleError));
            }

            function getUserData() {
                var dfd = $q.defer();
                dfd.resolve($http({
                    method: 'get',
                    url: USER_URL
                }));
                return dfd.promise;
            }

            return {
                getRoles: getRoles,
                createUser: createUser,
                updateUser: updateUser,
                getUserData: getUserData,
                getAllUsers: function () {
                    var request = $http({
                        method: "get",
                        url: USERS_URL
                    });

                    return (request.then(handleSuccess, handleError));
                }
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
                    enableSelectAll: false
                },
                exporterOptions = {
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
                defaultOptions = $.extend({}, standardOptions, exporterOptions),
                gridOptions = {
                    admin: defaultOptions,
                    audit: {
                        enableRowSelection: true,
                        enableRowHeaderSelection: false,
                        enableFullRowSelection: true,
                        paginationPageSize: 10,
                        paginationPageSizes: [],
                        enableHorizontalScrollbar: 0,
                        enableVerticalScrollbar: 0,
                        enableFiltering: true,
                        enableCellEditOnFocus: false,
                        showGridFooter: true,
                        multiSelect: false,
                        enableGridMenu: true,
                        enableSelectAll: true
                    },
                    error: {
                        enableRowSelection: true,
                        enableRowHeaderSelection: false,
                        enableFullRowSelection: true,
                        paginationPageSize: 10,
                        paginationPageSizes: [],
                        enableHorizontalScrollbar: 0,
                        enableVerticalScrollbar: 0,
                        enableFiltering: true,
                        enableCellEditOnFocus: false,
                        showGridFooter: true,
                        multiSelect: false,
                        enableGridMenu: true,
                        enableSelectAll: true
                    },
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
                    query: defaultOptions,
                    rule: defaultOptions,
                    watchlist: defaultOptions
                },
                columns = {
                    audit: [
                        {
                            name: 'action',
                            field: 'actionType',
                            width: '10%',
                            sort: {
                                direction: uiGridConstants.DESC,
                                priority: 1
                            }
                        }, {
                            name: 'user',
                            field: 'user',
                            width: '15%'
                        }, {
                            name: 'status',
                            field: 'status',
                            width: '10%'
                        }, {
                            name: 'message',
                            field: 'message',
                            width: '20%'
                        }, {
                            name: 'timestamp',
                            field: 'timestamp',
                            width: '45%'
                        }
                    ],
                    error: [
                        {
                            name: 'Error ID',
                            field: 'errorId',
                            width: '15%',
                            sort: {
                                direction: uiGridConstants.DESC,
                                priority: 1
                            }
                        }, {
                            name: 'Error Code',
                            field: 'errorCode',
                            width: '15%'
                        }, {
                            name: 'DateTime',
                            field: 'errorTimestamp',
                            width: '15%'
                        }, {
                            name: 'Error Description',
                            field: 'errorDescription',
                            width: '55%'
                        }
                    ],
                    admin: [
                        {
                            name: 'active',
                            field: 'active',
                            cellFilter: 'userStatusFilter',
                            width: '10%',
                            sort: {
                                direction: uiGridConstants.DESC,
                                priority: 1
                            }
                        }, {
                            name: 'userId',
                            field: 'userId',
                            width: '15%',
                            cellTemplate: '<div><md-button class="md-primary md-button md-default-theme" ng-click="grid.appScope.lastSelectedUser(row.entity)" href="#/user/{{COL_FIELD}}">{{COL_FIELD}}</md-button></div>'
                        }, {
                            name: 'firstName',
                            field: 'firstName',
                            width: '15%'
                        }, {
                            name: 'lastName',
                            field: 'lastName',
                            width: '20%'
                        }, {
                            name: 'roles',
                            field: 'roles',
                            cellFilter: 'roleDescriptionFilter',
                            width: '40%',
                            cellTooltip: function (row) {
                                return row.entity.roles;
                            },
                            cellTemplate: '<div class="ui-grid-cell-contents wrap" style="white-space: normal" title="TOOLTIP">{{COL_FIELD CUSTOM_FILTERS}}</div>'
                        }
                    ],
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
                            name: "hitCount",
                            displayName: "Hits",
                            field: "hitCount",
                            cellTemplate: '<md-button aria-label="title" ng-click="grid.api.selection.selectRow(row.entity)">{{COL_FIELD}}</md-button>',
                            enableCellEdit: false,
                            enableColumnMenu: false
                        },
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
                            displayName: "Modidified On | By",
                            field: "modifiedOn",
                            cellTemplate: '<md-button aria-label="modified" ng-click="grid.api.selection.selectRow(row.entity)">{{row.entity.modifiedOn}} | {{row.entity.modifiedBy}}</md-button>',
                            enableCellEdit: false,
                            enableColumnMenu: false
                        }
                    ],
                    watchlist: {
                        DOCUMENT: [
                            {
                                field: "documentType",
                                name: "documentType",
                                displayName: "Type",
                                cellTemplate: "<md-button class=\"md-primary\"  ng-click=\"grid.appScope.editRecord(row.entity)\" style=\"min-width: 0; margin: 0 auto; width: 100%;\" >{{COL_FIELD}}</md-button>",
                                "type": "string"
                            }, {
                                field: "documentNumber",
                                name: "documentNumber",
                                displayName: "Number",
                                cellTemplate: "<md-button class=\"md-primary\"  ng-click=\"grid.appScope.editRecord(row.entity)\" style=\"min-width: 0; margin: 0 auto; width: 100%;\" >{{COL_FIELD}}</md-button>",
                                "type": "string"
                            }
                        ],
                        PASSENGER: [
                            {
                                field: "dob",
                                name: "dob",
                                displayName: "DOB",
                                width: 100,
                                cellTemplate: "<md-button class=\"md-primary\"  ng-click=\"grid.appScope.editRecord(row.entity)\" style=\"min-width: 0; margin: 0 auto; width: 100%;\" >{{COL_FIELD | date:'yyyy-MM-dd'}}</md-button>",
                                "type": "date"
                            },
                            {
                                field: "firstName",
                                name: "firstName",
                                displayName: "First Name",
                                cellTemplate: "<md-button class=\"md-primary\"  ng-click=\"grid.appScope.editRecord(row.entity)\" style=\"min-width: 0; margin: 0 auto; width: 100%;\" >{{COL_FIELD}}</md-button>",
                                "type": "string"
                            }, {
                                field: "lastName",
                                name: "lastName",
                                displayName: "Last Name",
                                cellTemplate: "<md-button class=\"md-primary\"  ng-click=\"grid.appScope.editRecord(row.entity)\" style=\"min-width: 0; margin: 0 auto; width: 100%;\" >{{COL_FIELD}}</md-button>",
                                "type": "string"
                            }
                        ]
                    }
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
        .service("gridService", function () {
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
                    if (grid.getCellValue(row, col) > 0) {
                        return 'red';
                    }
                },
                ruleHit = function (grid, row, col) {
                    return grid.getCellValue(row, col) ? 'rule-hit' : 'invisible';
                },
                anyWatchlistHit = function (grid, row) {
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
                deleteItems: function (listTypeName, entity, watchlistItems) {
                    var request,
                        url = baseUrl + entity;

                    if (!listTypeName || !watchlistItems || !watchlistItems.length) {
                        return false;
                    }

                    request = $http({
                        method: 'put',
                        url: url,
                        data: {
                            "@class": "gov.gtas.model.watchlist.json.WatchlistSpec",
                            "name": listTypeName,
                            "entity": entity,
                            "watchlistItems": watchlistItems
                        }
                    });

                    return (request.then(handleSuccess, handleError));
                },
                addItem: function (listTypeName, entity, id, terms) {
                    var request,
                        url = baseUrl + entity,
                        action = 'Create';

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
                        url = baseUrl + entity,
                        action = 'Update';

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
            var URLS = {
                    query: '/gtas/query/',
                    rule: '/gtas/udr/',
                    all: '/gtas/all_udr/',
                    copy: '/gtas/copy_udr/'
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
                    loadRuleById: function (mode, ruleId) {
                        var request, baseUrl = URLS[mode];

                        if (!ruleId) {
                            return false;
                        }

                        request = $http({
                            method: "get",
                            url: [baseUrl, ruleId].join('')
                        });

                        return (request.then(handleSuccess, handleError));
                    },
                    copyRule: function (ruleId) {
                        var request, baseUrl = URLS.copy;

                        if (!ruleId) {
                            return false;
                        }

                        request = $http({
                            method: 'post',
                            url: [baseUrl, ruleId].join('')
                        });

                        return (request.then(handleSuccess, handleError));
                    },
                    delete: function (mode, ruleId) {
                        var request, baseUrl = URLS[mode];

                        if (!ruleId) {
                            return false;
                        }

                        request = $http({
                            method: 'delete',
                            url: [baseUrl, ruleId].join('')
                        });

                        return (request.then(handleSuccess, handleError));
                    },
                    save: function (mode, data) {
                        var method, request, url, baseUrl = URLS[mode];

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
                    getList: function (mode) {
                        var request, baseUrl = URLS[mode];

                        request = $http({
                            method: "get",
                            url: baseUrl
                        });

                        return (request.then(handleSuccess, handleError));
                    }
                };

            // Return public API.
            return ({
                copyRule: services.copyRule,
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
                queryFlights = function () {
                    var query = JSON.parse(localStorage['query']),
                        dfd = $q.defer();

                    dfd.resolve($http({
                        method: 'post',
                        url: serviceURLs.flights,
                        data: {
                            pageNumber: 1,
                            pageSize: -1,
                            query: query
                        }
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
        })
        .service("filterService", function ($http, $q) {
            var filterURLS = {
                    filter: '/gtas/filter/'
                },
                getFilter = function (userId) {
                    var dfd = $q.defer();
                    dfd.resolve($http({
                        method: 'get',
                        url: serviceURLs.filter + userId
                    }));
                    return dfd.promise;
                },
                setFilter = function (filter, userId) {

                    var dfd = $q.defer();
                    dfd.resolve($http({
                        method: 'post',
                        url: filterURLS.filter + userId,
                        data: filter
                    }));
                    return dfd.promise;
                },

                updateFilter = function (filter, userId) {
                    var dfd = $q.defer();
                    dfd.resolve($http({
                        method: 'put',
                        url: filterURLS.filter + userId,
                        data: filter
                    }));
                    return dfd.promise;
                };
            // Return public API.
            return ({
                getFilter: getFilter,
                setFilter: setFilter,
                updateFilter: updateFilter
            });
        })
    .service("passengersBasedOnUserFilter", function(userService, flightService, flightsModel, $q) {

        var today = new Date(),
            loadUser = function()
            {
                return userService
                    .getUserData(  )                     // Request #1
                    .then( function( user ) {
                        if(user.data.filter!=null) {
                            if (user.data.filter.flightDirection)
                                flightsModel.direction = user.data.filter.flightDirection;
                            if (user.data.filter.etaStart) {
                                flightsModel.starteeDate = new Date();
                                flightsModel.etaStart.setDate(today.getDate() + user.data.filter.etaStart);
                            }
                            if (user.data.filter.etaEnd) {
                                flightsModel.endDate = new Date();
                                flightsModel.etaEnd.setDate(today.getDate() + user.data.filter.etaEnd);
                            }// Response Handler #1
                            if (user.data.filter.originAirports != null)
                                flightsModel.origins = user.data.filter.originAirports;
                            if (user.data.filter.destinationAirports != null)
                                flightsModel.destinations = user.data.filter.destinationAirports;
                        }
                        return flightsModel;
                    });
            },
            loadPassenger = function( flightsModel)
            {
                var dfd = $q.defer();
                dfd.resolve(flightService.getFlights(flightsModel));
                return dfd.promise;
            },
            load = function ()
            {
                return loadUser().then(loadPassenger );
            };
        // Return public API.
        return ({
            load: load
        });
    });
}());
