app.controller('PaxController', function ($scope, $injector, jqueryQueryBuilderWidget, $mdDialog,
                                             paxService, sharedPaxData, $stateParams, $state, uiGridConstants, gridService,
                                          queryBuilderFactory,jqueryQueryBuilderService, $http) {

  $scope.model = paxService.model;

  $injector.invoke(jqueryQueryBuilderWidget, this, {$scope: $scope});
  $injector.invoke(queryBuilderFactory, this, {$scope: $scope });

  var paginationOptions = {
        pageNumber: 1,
        pageSize: 15,
        sort: null
      },
      selectedPassenger,
      PassengerDetailsDialogController = function ($scope, passengerObj) {
        $scope.passenger = passengerObj;
      };

  $scope.selectedFlight = $stateParams.flight;
  $scope.parent = $stateParams.parent;

  jqueryQueryBuilderService.init('riskcriteria');

  $scope.ruleIdClick = function(row) {
    $scope.getRuleObject(row.entity.ruleId);
  };

  $scope.getRuleObject = function (ruleID) {
    jqueryQueryBuilderService.loadRuleById(ruleID).then(function (myData) {
      $scope.$builder.queryBuilder('readOnlyRules', myData.result.details);
      $scope.hitDetailDisplay = myData.result.summary.title;
      document.getElementById("QBModal").style.display = "block";

      $scope.closeDialog = function () {
        document.getElementById("QBModal").style.display = "none";
      };
    });
  };

  $scope.showPaxDetailsModal = function (passenger) {
    selectedPassenger = passenger;
    $scope.passenger = paxService.getPaxDetail(passenger.id,passenger.flightId);
    $mdDialog.show({
      controller: PassengerDetailsDialogController,
      templateUrl: 'pax/pax.detail.html',
      parent: angular.element(document.body),
      clickOutsideToClose: true,
      locals: {
        passengerObj: $scope.passenger
              }
    });
  };

  $scope.isExpanded = true;
  $scope.paxHitList = [];
  $scope.list = sharedPaxData.list;
  $scope.add = sharedPaxData.add;
  $scope.getAll = sharedPaxData.getAll;

  $scope.getPaxSpecificList = function (index) {
    return $scope.list(index);
  };

  var ruleGridColumns = [
    { name: 'ruleId', "width": 60, displayName: 'Id',
      cellTemplate: ' <button id="editBtn" type="button" class="btn-small" ng-click="grid.appScope.ruleIdClick(row)">{{COL_FIELD}}</button>'
    },
    { name: 'ruleTitle', displayName: 'Title' },
    { name: 'ruleConditions', displayName: 'Conditions',field:'hitsDetailsList[0]',cellFilter: 'hitsConditionDisplayFilter' }
  ];


  $scope.buildAfterEntitiesLoaded();

  $scope.passengerGrid = {
    enableSorting: false,
    multiSelect: false,
    enableFiltering: false,
    enableRowSelection: false,
    enableSelectAll: false,
    enableGridMenu: false,
    paginationPageSizes: [10, 15, 25],
    paginationPageSize: $scope.model.pageSize,
    useExternalPagination: true,
    useExternalSorting: true,
    useExternalFiltering: true,

    expandableRowTemplate: '<div ui-grid="row.entity.subGridOptions"></div>',

    onRegisterApi: function (gridApi) {
      $scope.gridApi = gridApi;

      gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
        paginationOptions.pageNumber = newPage;
        paginationOptions.pageSize = pageSize;
        $scope.model.pageNumber = newPage;
        $scope.model.pageSize = pageSize;
        getPage();
      });

      gridApi.selection.on.rowSelectionChanged($scope, function (row) {
        if (row.isSelected) {
          $scope.showPaxDetailsModal(row.entity);
        }
      });

      gridApi.expandable.on.rowExpandedStateChanged($scope, function (row) {
        if (row.isExpanded) {
          paxService.getRuleHits(row.entity.id).then(function (data) {
            row.entity.subGridOptions.data = data;
          });
        }
      });
    }
  };

  $scope.passengerGrid.columnDefs = [
    { name: 'onRuleHitList', displayName: 'H', width: 50,
      cellClass: gridService.colorHits,
      cellTemplate: '<div></div>',
      sort: {
        direction: uiGridConstants.DESC,
        priority: 0
      }
    },
    { name: 'onWatchList', displayName: 'L', width: 50,
      cellClass: gridService.colorHits,
      cellTemplate: '<div></div>'
    },
    { name: 'passengerType', displayName: 'Type', width: 50 },
    { name: 'lastName', displayName: 'Last Name', width: 175,
      sort: {
        direction: uiGridConstants.DESC,
        priority: 1
      }
    },
    { name: 'firstName', displayName: 'First Name', width: 150 },
    { name: 'middleName', displayName: 'Middle', width: 100 },
    { name: 'flightNumber', displayName: 'Flight', width: 90, visible: ($scope.parent !== 'flights') },
    { name: 'eta', displayName: 'ETA', width: 175, visible: ($scope.parent !== 'flights') },
    { name: 'etd', displayName: 'ETD', width: 175, visible: ($scope.parent !== 'flights') },
    { name: 'gender', displayName: 'G', width: 50 },
    { name: 'dob', displayName: 'DOB', cellFilter: 'date', width: 175 },
    { name: 'citizenshipCountry', displayName: 'CTZ', width: 75 },
    { name: 'documentType', displayName: 'T', width: 50 },
    { name: 'seat', displayName: 'Seat', width: 75 }
  ];

  var getPage = function() {
    if ($scope.parent === 'flights') {
      paxService.getPax($stateParams.flight.id, paginationOptions).then(function (data) {
        setSubGridOptions(data);
        $scope.passengerGrid.totalItems = data.totalPassengers;
        $scope.passengerGrid.data = data.passengers;
      });

    } else {
      paxService.getAllPax($scope.model).then(function (data) {
        setSubGridOptions(data);
        //$scope.passengerGrid.totalItems = data.totalPassengers;
        $scope.passengerGrid.data = data.passengers;
      });
    }
  };

  var setSubGridOptions = function(data) {
    for (i = 0; i < data.passengers.length; i++) {
      var rowScope = data.passengers[i];
      rowScope.subGridOptions = {
        appScopeProvider: $scope,
        columnDefs: ruleGridColumns,
        data: []
      }
    }
  }


  $scope.getTableHeight = function() {
    return gridService.calculateGridHeight($scope.passengerGrid.data.length);
  };

  getPage();
});

// Customs Filters

app.filter('capitalize', function () {
  return function (input, all) {
    return (!!input) ? input.replace(/([^\W_]+[^\s-]*) */g, function (txt) {
      return txt.charAt(0).toUpperCase() + txt.substr(1).toLowerCase();
    }) : '';
  };
});


app.filter('orderObjectBy', function () {
  return function (items, field, reverse) {
    var filtered = [];
    angular.forEach(items, function (item) {
      filtered.push(item);
    });
    filtered.sort(function (a, b) {
      return (a[field] > b[field] ? 1 : -1);
    });
    if (reverse) filtered.reverse();
    return filtered;
  };
});
