app.controller('WatchListController', function ($scope, $filter, $q, watchListService, $interval) {
    'use strict';
    var watchlist = localStorage["watchlist"] === undefined ? {} : JSON.parse(localStorage["watchlist"]);

    $scope.$scope = $scope;
    if (watchlist.types === undefined) {
        watchlist.types = {
            "document": {
                columns: [{
                    name: "id",
                    width: 50,
                    enableCellEdit: false,
                    enableColumnMenu: false,
                    enableFiltering: false,
                    "type": "number"
                }, {
                    name: "type",
                    enableCellEdit: true,
                    "type": "string"
                }, {
                    name: "number",
                    enableCellEdit: true,
                    "type": "string"
                }],
                data: [
                    {id: 1, "type": "P", "number": "76283AJLG"},
                    {id: 2, "type": "V", "number": "111123AJLV"}
                ]
            },
            "passenger": {
                columns: [{
                    name: "id",
                    width: 50,
                    enableColumnMenu: false,
                    enableFiltering: false,
                    enableSorting: false,
                    enableCellEdit: false,
                    "type": "number"
                }, {
                    name: "first name",
                    enableCellEdit: true,
                    "type": "string"
                }, {
                    name: "last name",
                    enableCellEdit: true,
                    "type": "string"
                }, {
                    name: "DOB",
                    enableCellEdit: true,
                    "type": "date"
                }],
                data: [
                    {id: 1, "first name": "John", "last name": "Johnnson", "DOB": "1977-01-01"},
                    {id: 2, "first name": "Jack", "last name": "Johnson", "DOB": "1978-01-01"},
                    {id: 3, "first name": "John", "last name": "Johnnson", "DOB": "1979-01-01"},
                    {id: 4, "first name": "Bobby", "last name": "Johnson", "DOB": "1980-01-01"},
                    {id: 5, "first name": "Billy Bob", "last name": "Thorton", "DOB": "1967-07-05"},
                    {id: 6, "first name": "Angelina", "last name": "Jolie", "DOB": "1976--12-01"},
                    {id: 7, "first name": "John", "last name": "Johnnson", "DOB": "1979-01-01"},
                    {id: 8, "first name": "Jack", "last name": "Johnson", "DOB": "1980-01-01"}
                ]
            }
        };
    }
    localStorage["watchlist"] = JSON.stringify(watchlist);
    $scope.tabs = watchListService.getListTypes();
    $scope.tabfields = watchlist.types;

    $scope.updateGrid = function (listName) {
        $scope.activeTab = listName;
        $scope.gridOpts.columnDefs = $scope.tabfields[listName].columns;
        $scope.gridOpts.data = $scope.tabfields[listName].data;
    };
    $scope.gridOpts = {
        paginationPageSize: 10,
        paginationPageSizes: [],
        enableFiltering: true,
        enableCellEditOnFocus: true,
        showGridFooter: true,
        enableGridMenu: true,
        enableSelectAll: true,
        exporterCsvFilename: 'myFile.csv',
        exporterPdfDefaultStyle: {fontSize: 9},
        exporterPdfTableStyle: {margin: [30, 30, 30, 30]},
        exporterPdfTableHeaderStyle: {fontSize: 10, bold: true, italics: true, color: 'red'},
        exporterPdfHeader: { text: "My Header", style: 'headerStyle' },
        exporterPdfFooter: function (currentPage, pageCount) {
            return { text: currentPage.toString() + ' of ' + pageCount.toString(), style: 'footerStyle' };
        },
        exporterPdfCustomFormatter: function (docDefinition) {
            docDefinition.styles.headerStyle = { fontSize: 22, bold: true };
            docDefinition.styles.footerStyle = { fontSize: 10, bold: true };
            return docDefinition;
        },
        exporterPdfOrientation: 'portrait',
        exporterPdfPageSize: 'LETTER',
        exporterPdfMaxGridWidth: 500,
        exporterCsvLinkElement: angular.element(document.querySelectorAll(".custom-csv-link-location"))
    };
    $scope.updateGrid('document');

    $scope.Add = function () {
        var starterData = {};
        $scope.gridOpts.columnDefs.forEach(function (field) {
            starterData[field.name] = null;
        });
        $scope.gridOpts.data.unshift(starterData);
    };

    $scope.saveRow = function (rowEntity) {
        // create a fake promise - normally you'd use the promise returned by $http or $resource
        var promise = $q.defer();
        console.log(rowEntity);

//        $scope.gridApi.rowEdit.setSavePromise(rowEntity, promise.promise);
        if (!rowEntity.id) {
            watchListService.addItem($scope.activeTab, rowEntity);
        } else {
            watchListService.updateItem($scope.activeTab, rowEntity);
        }
        $scope.gridApi.rowEdit.setSavePromise(rowEntity, promise.promise);
        $interval(function () {
            promise.resolve();
        }, 300, 1);
    };

    $scope.gridOpts.onRegisterApi = function (gridApi) {
        //set gridApi on scope
        $scope.gridApi = gridApi;
        gridApi.rowEdit.on.saveRow($scope, $scope.saveRow);
    };

    /*    var myRows = $resource('http://testsomethingtofail.com/:id', {id: '@id'}, {
     query: {method: 'GET', isArray: true},
     addRows: {method: 'POST', isArray: true},
     updateAllRows: {method: 'PUT', isArray: true},
     removeRow: {method: 'DELETE'}
     });
     */
    // call this to see if you can error out a row by calling a bogus URL to delete something.
    // called from the UI by pressing "Remove Row"
    $scope.removeRow = function () {
        var selectedRowEntities = $scope.gridApi.selection.getSelectedRows();

        angular.forEach(selectedRowEntities, function(rowEntity, key) {
            console.log('rowEntity: ' + angular.toJson(rowEntity));
            console.log('selected row: ' + rowEntity.id);
            var rowIndexToDelete = $scope.gridOpts.data.indexOf(rowEntity);

            // create a promise to reject errors from server side.
            var rowDeferred = $q.defer();

            // registering promise with the row to be deleted in the grid.
            //$scope.gridApi.rowEdit.setSavePromise( $scope.gridApi.grid, rowEntity, rowDeferred.promise );

            console.log('Selected row: ' + rowIndexToDelete + ' to delete.');
            var deferred = watchListService.removeItem({id: rowEntity.id});
            deferred.$promise.then(function (response) {
                    // success callback
                    var newLength = $scope.gridOpts.data.splice(rowIndexToDelete, 1);
                    console.log('success!! resolving the promise with the new array length.');
                    rowDeferred.resolve(newLength);
                },
                function (error) {
                    // will fail because URL is not real, but will resolve anyway for purposes of this test.

                    // this is what I expect will remove the row from the ui-grid in the UI.
                    var newLength = $scope.gridOpts.data.splice(rowIndexToDelete, 1);
                    rowDeferred.resolve(newLength);
                    //rowDeferred.reject(error);
                });
        });
    };
});
