app.service("gridOptionsLookupService", function () {
    'use strict';
    var today = moment().format('YYYY-MM-DD'),
        pageOfPages = function (currentPage, pageCount) {
            return today + (pageCount === 1 ? '' : '\t' + currentPage.toString() + ' of ' + pageCount.toString());
        },
        gridOptions = {
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
        columns = {
            flights: [
                {
                    name: 'P', field: 'passengerCount', width: 50, enableFiltering: false,
                    cellTemplate: '<button id="editBtn" type="button" class="btn-small" ng-click="grid.appScope.passengerNav()">{{COL_FIELD}}</button> ',
                },
                {name: 'H', field: 'ruleHitCount', width: 50, enableFiltering: false},
                {name: 'L', field: 'listHitCount', width: 50, enableFiltering: false},
                {name: 'Carrier', field: 'carrier', width: 75},
                {name: 'Flight', field: 'flightNumber', width: 75},
                {name: 'Dir', field: 'direction', width: 50},
                {name: 'ETA', displayName: 'ETA', field: 'eta'},
                {name: 'ETD', displayName: 'ETD', field: 'etd'},
                {name: 'Origin', field: 'origin'},
                {name: 'OriginCountry', displayName: "Country", field: 'originCountry'},
                {name: 'Dest', field: 'destination'},
                {name: 'DestCountry', displayName: "Country", field: 'destinationCountry'}
            ],
            passengers: [
                {
                "name": "ruleHit",
                "displayName": "H",
                width: 50
            }, {
                "name": "onWatchList",
                "displayName": "L",
                width: 50
            }, {
                "name": "lastName",
                "displayName": "Last Name",
                width: "*"
            }, {
                "name": "firstName",
                "displayName": "First Name",
                width: "*"
            }, {
                "name": "middleName",
                "displayName": "Middle Name",
                width: "*"
            }, {
                "name": "passengerType",
                "displayName": "Type",
                width: 25
            }, {
                "name": "gender",
                "displayName": "GEN",
                width: 25
            }, {
                "name": "dob",
                "displayName": "DOB",
                width: 125
            }, {
                "name": "citizenshipCountry",
                "displayName": "CIT",
                width: 50
            }, {
                "name": "documentNumber",
                "displayName": "Doc #",
                width: 125
            }, {
                "name": "documentType",
                "displayName": "T",
                width: 25
            }, {
                "name": "residencyCountry",
                "displayName": "Issuer",
                width: 50
            }],
            queries: [
                {
                    name: "title",
                    displayName: "Name",
                    enableCellEdit: false,
                    enableColumnMenu: false
                }, {
                    name: "description",
                    enableCellEdit: false,
                    enableColumnMenu: false
                }
            ],
            riskCriteria: [
                {
                    name: "title",
                    displayName: "Name",
                    enableCellEdit: false,
                    enableColumnMenu: false
                }, {
                    name: "description",
                    enableCellEdit: false,
                    enableColumnMenu: false
                }, {
                    name: "startDate",
                    enableCellEdit: false,
                    enableColumnMenu: false
                }, {
                    name: "endDate",
                    enableCellEdit: false,
                    enableColumnMenu: false
                }, {
                    name: "enabled",
                    enableCellEdit: false,
                    enableColumnMenu: false
                }
            ]
        };
    return {
        defaultGridOptions: function () {
            return gridOptions;
        },
        getLookupColumnDefs: function (entity) {
            return columns[entity] || [];
        }
    };
});
