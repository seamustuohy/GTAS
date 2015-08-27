app.factory('GridControl', function () {
    'use strict';
    return function ($scope) {
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
                    text: function (currentPage, pageCount) {
                        return moment().format('YYYY-MM-DD') + (pageCount === 1 ? '' : '\t' + currentPage.toString() + ' of ' + pageCount.toString());
                    },
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
});
