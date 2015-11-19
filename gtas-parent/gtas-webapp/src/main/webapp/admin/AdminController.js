app.controller('AdminCtrl', function ($scope, gridOptionsLookupService, userService, auditService, errorService, $location, $mdToast, uiGridConstants) {
    'use strict';
    
    var setUserData = function (data) { 
    	$scope.userGrid.data = data; 
    	};

    var setAuditData = function (data) { 
    	$scope.auditGrid.data = data; 
    	};
    var setErrorData = function (data) { 
    	$scope.errorGrid.data = data; 
    	};
    var setupUserGrid = function(){
        $scope.userGrid = gridOptionsLookupService.getGridOptions('admin');
        $scope.userGrid.columnDefs = gridOptionsLookupService.getLookupColumnDefs('admin');    	
    }
    var setupAuditGrid = function(){
        $scope.auditGrid = gridOptionsLookupService.getGridOptions('audit');
        $scope.auditGrid.columnDefs = gridOptionsLookupService.getLookupColumnDefs('audit');    	
    }
    var setupErrorGrid = function(){
        $scope.errorGrid = gridOptionsLookupService.getGridOptions('error');
        $scope.errorGrid.columnDefs = gridOptionsLookupService.getLookupColumnDefs('error');    	
    }
    var selectRow = function(gridApi) {
        // set gridApi on scope
        $scope.gridApi = gridApi;
        $scope.gridApi.selection.on.rowSelectionChanged($scope,
                function(row) {
        	      if($scope.selectedTabIndex == 2 || $scope.selectedTabIndex == 3){
                    $scope.selectedItem = row.entity;
                    if(row.isSelected){
                      $scope.showAuditDetails = true;
	        	    } else {
	        	    	  $scope.showAuditDetails = false;
	        	    }
        	      }
                });
	    //$scope.gridApi.core.notifyDataChange( uiGridConstants.dataChange.OPTIONS);
    };
    var selectErrorRow = function(gridApi) {
        // set gridApi on scope
        $scope.errorGridApi = gridApi;
        $scope.errorGridApi.selection.on.rowSelectionChanged($scope,
                function(row) {
        	      if($scope.selectedTabIndex == 3 && row.isSelected){                   
                    	$scope.selectedErrorItem = row.entity;
	        	  } else {
	        		  $scope.selectedErrorItem = null;
        	      }
                });
    };

    $scope.selectedTabIndex = 0;
    
    setupUserGrid();
    setupAuditGrid();
    setupErrorGrid();
    $scope.auditGrid.onRegisterApi = selectRow;
    $scope.errorGrid.onRegisterApi = selectErrorRow;
    
    $scope.$watch('selectedTabIndex', function(current, old){       
        switch ( current){
           case 0:        	    
        	    userService.getAllUsers().then(setUserData);       		
        	    break;
           case 2:       	    
        	    $scope.refreshAudit();
        	    break;
           case 3:       	    
	       	    $scope.refreshError();
	       	    break;
        }
      });
    
    $scope.createUser = function () { $location.path('/user/new'); };
    $scope.lastSelectedUser = function (user) { localStorage['lastSelectedUser'] = JSON.stringify(user); };
     
    $scope.showAuditDetails = false;
    $scope.auditActions = auditService.auditActions;
    var today = new Date();
    $scope.auditModel = {action:null, user:null, timestampStart:today, timestampEnd:today};
    $scope.errorModel = {code:null, timestampStart:today, timestampEnd:today};
    
    $scope.refreshAudit = function(){
    	var model = $scope.auditModel;
    	$scope.showAuditDetails = false;
    	auditService.getAuditData(model.action, model.user, model.timestampStart, model.timestampEnd).then(setAuditData, $scope.errorToast);
    };
    $scope.refreshError = function(){
    	var model = $scope.errorModel;
    	$scope.selectedErrorItem = null;
    	errorService.getErrorData(model.code, model.timestampStart, model.timestampEnd).then(setErrorData, $scope.errorToast);
    };
    $scope.errorToast = function(error){
    	$mdToast.show($mdToast.simple()
    	  .content(error)
    	  .action('OK')
    	  .highlightAction(true)
    	  .position('top right')
    	  .hideDelay(0));
    };
    
});
