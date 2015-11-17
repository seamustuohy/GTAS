app.controller('AdminCtrl', function ($scope, gridOptionsLookupService, userService, auditService, $location, $mdToast) {
    'use strict';
    
    var setData = function (data) { 
    	$scope.adminGrid.data = data; 
    	};

    var setupGrid = function(opt, coldef){
        $scope.adminGrid = gridOptionsLookupService.getGridOptions(opt);
        $scope.adminGrid.columnDefs = gridOptionsLookupService.getLookupColumnDefs(coldef);    	
    }
    $scope.selectedTabIndex = 0;
    
    $scope.adminGrid = {columnDefs:{}, data:[]};
    
    $scope.$watch('selectedTabIndex', function(current, old){       
        switch ( current){
           case 0:
        	    setupGrid('admin', 'admin');
        	    userService.getAllUsers().then(setData);       		
        	    break;
           case 2:
        	    setupGrid('admin', 'audit');
        	    $scope.refreshAudit();
        	    break;
        }
      });
    
    $scope.createUser = function () { $location.path('/user/new'); };
    $scope.lastSelectedUser = function (user) { localStorage['lastSelectedUser'] = JSON.stringify(user); };
      
    $scope.auditActions = auditService.auditActions;
    var today = new Date();
    $scope.auditModel = {action:null, user:null, timestampStart:today, timestampEnd:today};
    $scope.refreshAudit = function(){
    	var model = $scope.auditModel;
    	auditService.getAuditData(model.action, model.user, model.timestampStart, model.timestampEnd).then(setData, $scope.errorToast);
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
