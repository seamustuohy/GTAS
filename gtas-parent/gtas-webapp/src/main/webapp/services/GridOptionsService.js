app.service("gridOptionsLookupService", function (uiGridConstants) {
    'use strict';
    var today = moment().format('YYYY-MM-DD'),
        pageOfPages = function (currentPage, pageCount) {
            return today + (pageCount === 1 ? '' : '\t' + currentPage.toString() + ' of ' + pageCount.toString());
        },
        standardOptions = {
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
            queries : standardOptions,
            riskCriteria: standardOptions,
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
                { name: "onRuleHitList", "displayName": "H", width: 50,
                    cellClass: gridService.colorHits,
                    cellTemplate: '<div></div>',
                    "sort": {
                        direction: uiGridConstants.DESC,
                        priority: 0
                    }
                },
                { name: "onWatchList", "displayName": "L", width: 50,
                    cellClass: gridService.colorHits,
                    cellTemplate: '<div></div>'
                },
                { name: "passengerType", "displayName": "Type", width: 50 },
                { name: "lastName", "displayName": "Last Name", width: 175,
                    "sort": {
                        direction: uiGridConstants.DESC,
                        priority: 1
                    }
                },
                { name: "firstName", "displayName": "First Name", width: 150 },
                { name: "middleName", "displayName": "Middle", width: 100 },
                { name: "flightNumber", "displayName": "Flight", width: 90, visible: ($scope.parent !== 'flights') },
                { name: "eta", "displayName": "ETA", width: 175, visible: ($scope.parent !== 'flights') },
                { name: "etd", "displayName": "ETD", width: 175, visible: ($scope.parent !== 'flights') },
                { name: "gender", "displayName": "G", width: 50 },
                { name: "dob", "displayName": "DOB", cellFilter: 'date', width: 175 },
                { name: "citizenshipCountry", "displayName": "CTZ", width: 75 },
                { name: "documentType", "displayName": "T", width: 50 },
                { name: "seat", "displayName": "Seat", width: 75 }
            ],
            queries: [
                {
                    name: "title",
                    displayName: "Name",
                    field: "title",
                    enableCellEdit: false,
                    enableColumnMenu: false
                }, {
                    name: "description",
                    field: "description",
                    enableCellEdit: false,
                    enableColumnMenu: false
                }
            ],
            riskCriteria: [
                {
                    name: "title",
                    displayName: "Name",
                    field: "title",
                    enableCellEdit: false,
                    enableColumnMenu: false
                }, {
                    name: "description",
                    field: "description",
                    enableCellEdit: false,
                    enableColumnMenu: false
                }, {
                    name: "startDate",
                    field: "startDate",
                    enableCellEdit: false,
                    enableColumnMenu: false
                }, {
                    name: "endDate",
                    field: "endDate",
                    enableCellEdit: false,
                    enableColumnMenu: false
                }, {
                    name: "enabled",
                    field: "enabled",
                    enableCellEdit: false,
                    enableColumnMenu: false
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
});
