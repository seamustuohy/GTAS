app.controller('PaxController', function ($scope, $rootScope, $injector, GridControl, jQueryBuilderFactory, $filter, $q, paxService, sharedPaxData, riskCriteriaService) {
    var self = this;
    $injector.invoke(jQueryBuilderFactory, this, {$scope: $scope});
    $injector.invoke(GridControl, this, {$scope: $scope});

    $scope.isExpanded = true;
    $scope.paxHitList = [];
    $scope.list = sharedPaxData.list;
    $scope.add = sharedPaxData.add;
    $scope.getAll = sharedPaxData.getAll;

    $scope.getPaxSpecificList = function (index) {
        return $scope.list(index);
    };

    $scope.passengerGrid = $.extend({
        columnDefs: $rootScope.columns.PASSENGER,
        exporterCsvFilename: 'Passengers.csv',
        exporterPdfHeader: {text: "Passengers", style: 'headerStyle'}
    }, $scope.gridOpts);


    paxService.getAllPax().then(function (myData) {
        $scope.passengerGrid.data = myData;
    });

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
});
