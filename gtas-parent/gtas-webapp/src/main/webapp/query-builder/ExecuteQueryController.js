app.controller('ExecuteQueryController', function ($scope, executeQueryService) {
    'use strict';
    var paginationOptions = {
        pageNumber: 1,
        pageSize: 10,
        sort: null
    };

    $scope.gridOptions = {
        enableRowSelection: true,
        multiSelect: false,
        enableFiltering: true,
        paginationPageSizes: [10, 25, 50],
        paginationPageSize: 10,
        useExternalPagination: true,
        useExternalSorting: true,
        useExternalFiltering: true,

        onRegisterApi: function (gridApi) {
            $scope.gridApi = gridApi;

            gridApi.selection.on.rowSelectionChanged($scope, function (row) {
            });

            gridApi.core.on.sortChanged($scope, function (grid, sortColumns) {
                if (sortColumns.length === 0) {
                    paginationOptions.sort = null;
                } else {
                    paginationOptions.sort = sortColumns[0].sort.direction;
                }
                getPage();
            });

            gridApi.core.on.filterChanged($scope, function () {
                var grid = this.grid;
                //alert(grid.columns[4].filters[0].term);
            });

            gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                paginationOptions.pageNumber = newPage;
                paginationOptions.pageSize = pageSize;
                getPage();
            });
        }
    };

    var columnDefs = {
        FLIGHT: [
            {name: 'P', field: 'passengerCount', width: 50, enableFiltering: false},
            {name: 'H', field: 'ruleHits', width: 50, enableFiltering: false},
            {name: 'L', field: 'listHits', width: 50, enableFiltering: false},
            {name: 'Flight', field: 'flightNumber'},
            {name: 'Dir', field: 'direction', width: 50},
            {name: 'ETA', displayName: 'ETA', field: 'eta'},
            {name: 'ETD', displayName: 'ETD', field: 'etd'},
            {name: 'Origin', field: 'origin'},
            {name: 'OriginCountry', displayName: "Country", field: 'originCountry'},
            {name: 'Dest', field: 'destination'},
            {name: 'DestCountry', displayName: "Country", field: 'destinationCountry'}
        ],
        PASSENGER: []
    }
    $scope.gridOptions.columnDefs =

    $scope.serviceURLs = {
        FLIGHT: '/gtas/query/queryFlights/',
        PASSENGER: '/gtas/query/queryPassengers/'
    };

    var viewType = localStorage['qbType'] || 'FLIGHT',
        qbData = localStorage['qbData'] !== undefined ? JSON.parse(localStorage['qbData']) : undefined,
        qbTitle = localStorage['qbTitle'] || 'unnamed flight query';

    var getPage = function () {
        $scope.gridOptions.exporterPdfHeader.text = qbTitle;
        if (qbData !== undefined) {
            executeQueryService.executeQuery($scope.serviceURLs.FLIGHT, qbData).then(function (myData) {
                if (myData.result === undefined) {
                    return;
                }
                $scope.gridOptions.data = myData.result;
            });
        } else {
            $scope.gridOptions.totalItems = 0;
            $scope.gridOptions.data = [];
        }
    };

    getPage();
});
