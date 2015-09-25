app.controller('PaxController', function ($scope, $rootScope, $injector, GridControl, jqueryQueryBuilderWidget, $filter,
                                          $q, paxService, sharedPaxData, $stateParams, $state,
                                          $timeout, $interval, uiGridConstants,passengers) {
  console.log(passengers);

  var paginationOptions = {
    pageNumber: 1,
    pageSize: 10,
    sort: null
  };

  $scope.selectedFlight = $stateParams.flight;
  $scope.parent = $stateParams.parent;

  var self = this;
  $injector.invoke(jqueryQueryBuilderWidget, this, {$scope: $scope});
  $injector.invoke(GridControl, this, {$scope: $scope});

  $scope.isExpanded = true;
  $scope.paxHitList = [];
  $scope.list = sharedPaxData.list;
  $scope.add = sharedPaxData.add;
  $scope.getAll = sharedPaxData.getAll;

  $scope.getPaxSpecificList = function (index) {
    return $scope.list(index);
  };
    
  $scope.passengerGrid = {
    enableSorting: false,
    multiSelect: false,
    enableFiltering: false,     
    enableRowSelection: true, 
    enableSelectAll: false,
    enableGridMenu: false,    
    paginationPageSizes: [10, 25, 50],
    paginationPageSize: 10,
    useExternalPagination: true,
    useExternalSorting: true,
    useExternalFiltering: true,
    saveGroupingExpandedStates: true,
    
    onRegisterApi: function (gridApi) {
      $scope.gridApi = gridApi;

      gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
        paginationOptions.pageNumber = newPage;
        paginationOptions.pageSize = pageSize;
        getPage();
      });

      gridApi.selection.on.rowSelectionChanged($scope, function (row) {
        // show the detail screen
        var title;
        if (row.isSelected) {
          if( $scope.parent==='flights') {
            $stateParams.id=row.entity.id;
            $stateParams.flightId= row.entity.flightId;
            $state.go('flights.passengers.detail', {id: row.entity.id, flightId: row.entity.flightId,parent:$scope.parent });
          }
          else
          $state.go('pax.detail', {id: row.entity.id, flightId: row.entity.flightId,parent:$scope.parent });
        }
      });
    }
  };
  
  $scope.passengerGrid.columnDefs = [
    { "name": "ruleHits", "displayName": "H", width: 50, enableFiltering: false,
      "sort": {
        direction: uiGridConstants.DESC,
        priority: 0
      }
    },
    {"name": "onWatchList", "displayName": "L", width: 50, enableFiltering: false},
    {
      "name": "lastName", "displayName": "Last Name", width: 175/*, enableFiltering: false*/,

      "sort": {
        direction: uiGridConstants.DESC,
        priority: 1
      }
    },
    {"name": "firstName", "displayName": "First Name", width: 150/*, enableFiltering: false*/},
    {"name": "middleName", "displayName": "Middle", width: 100/*, enableFiltering: false*/},
    {"name": "flightNumber", "displayName": "Flight", width: 90/*, enableFiltering: false*/},
    {"name": "flightETA", "displayName": "ETA", width: 175, enableFiltering: false},
    {"name": "flightETD", "displayName": "ETD", width: 175, enableFiltering: false},
    {"name": "passengerType", "displayName": "Type", width: 50, enableFiltering: false},
    {"name": "gender", "displayName": "G", width: 50, enableFiltering: false},
    {"name": "dob", "displayName": "DOB", field: 'dob', cellFilter: 'date', width: 175, enableFiltering: false},
    {"name": "citizenshipCountry", "displayName": "CTZ", width: 75/*, enableFiltering: false*/},
    {"name": "passengerType", "displayName": "T", width: 100, enableFiltering: false},
    {"name": "documentType", "displayName": "T", width: 50, enableFiltering: false},
    {"name": "carrier", "displayName": "Carrier", width: 50/*, enableFiltering: false*/},
    {"name": "seat", "displayName": "Seat", width: 75, enableFiltering: false}
  ];

  var getPage = function() {
    console.log('requesting pax page #' + paginationOptions.pageNumber);
    if ($scope.parent === 'flights') {
      //$scope.passengerGrid.data = passengers;
      paxService.getPax($stateParams.flight.id).then(function (myData) {
        //$scope.passengerGrid.totalItems = myData.totalPassengers;
        $scope.passengerGrid.data = myData;
      });
      
    } else {
      paxService.getAllPax(paginationOptions).then(function (myData) {
        $scope.passengerGrid.totalItems = myData.totalPassengers;
        $scope.passengerGrid.data = myData.passengers;
      });
    }   
  };


  getPage();






  // removed this from grid options for now
  var pdf_opts = {
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
    exporterCsvLinkElement: angular.element(document.querySelectorAll(".custom-csv-link-location")),
    exporterCsvFilename: 'Passengers.csv',
    exporterPdfHeader: {text: "Passengers", style: 'headerStyle'},
  };
  
  //------- Pre-Refactor-------------------
  //Function to get Rule Hits per Passenger
  $scope.getRuleHits = function (passengerId) {
      var j, i;
      $scope.isExpanded = !$scope.isExpanded;
      if (!$scope.isExpanded) {
          paxService.getRuleHits(passengerId).then(function (myData) { // Begin

              $scope.paxHitList = [];
              $scope.tempPaxHitDetail = [];
              $scope.tempPaxHitList = [];
              var tempObj = [];

              for (j = 0; j < myData.length; j++) {
                  $scope.tempPaxHitList = myData[j].hitsDetailsList;
                  for (i = 0; i < $scope.tempPaxHitList.length; i++) {
                      tempObj = $scope.tempPaxHitList[i];
                      tempObj.ruleTitle = myData[j].ruleTitle;
                      tempObj.ruleConditions = tempObj.ruleConditions.substring(0, (tempObj.ruleConditions.length - 3));
                      $scope.tempPaxHitDetail[i] = tempObj;
                  }

                  $scope.paxHitList.push($scope.tempPaxHitDetail.pop());
                  $scope.tempPaxHitDetail = [];
              }
          }); // END of paxService getRuleHits
      }
  };
}); // END of PaxController

// Customs Filters

app.filter('capitalize', function () {
    return function (input, all) {
        return (!!input) ? input.replace(/([^\W_]+[^\s-]*) */g, function (txt) {
            return txt.charAt(0).toUpperCase() + txt.substr(1).toLowerCase();
        }) : '';
    };
});

app.filter('DobDateFormat', function myDateFormat($filter) {
    return function (text) {
        var tempdate = new Date(text.replace(/-/g, "/"));
        return $filter('date')(tempdate, "MMM-dd-yyyy");
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

app.filter('flagImageFilter', function () {
    return function (hits) {
        if (hits === '0') {
            return 'padding: 1.5px; color: #007500;';
        }
        if (hits != '0') {
            return 'padding: 1.5px; color: #d9534f;';
        }
        return 'color: #007500;';
    };
});

//flagImageFilter

app.filter('watchListImageFilter', function () {
    return function (hits) {
        switch (hits) {
            case 1:
                return 'icon-user-check glyphiconWLPax col-sm-4';
            case 2:
                return 'icon-book glyphiconWLDocs col-sm-4';
            case 3:
                return 'icon-user-check glyphiconWLPaxDocs col-sm-4';
            default:
                return '';
        }
    };
});

app.filter('watchListImageInsertFilter', function () {
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
});

app.filter('watchListImageColorFilter', function () {
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
  