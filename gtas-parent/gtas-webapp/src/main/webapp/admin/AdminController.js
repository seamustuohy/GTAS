app.controller('AdminCtrl', ['$scope', '$http', '$state','$stateParams','$interval', 'uiGridConstants', '$injector', 'GridControl',
                             function ($scope, $http,$state,$stateParams,$interval, uiGridConstants, $injector, GridControl) {
	
	$scope.gridOptions = { enableRowSelection: true, enableRowHeaderSelection: false };

	$scope.selectedUser=';'
  $scope.gridOptions.columnDefs = [
    { name: 'userId' ,field: 'userId'},
    { name: 'firstName',field:'firstName'},
    { name: 'lastName',field:'lastName'} ,
    { name: 'active' ,field:'active'},
    {name: 'roles', field:'roles'}
  ];
  
	/*angular.forEach($scope.gridOptions.data,function(row){
		  row.getNameAndAge = function(){
		    return this.name + '-' + this.age;
		  }
		});*/
 
  $scope.gridOptions.multiSelect = false;
  $scope.gridOptions.modifierKeysToMultiSelect = false;
  $scope.gridOptions.noUnselect = true;
  $scope.gridOptions.onRegisterApi = function( gridApi ) {
	  
    $scope.gridApi = gridApi;
    gridApi.selection.on.rowSelectionChanged($scope,function(row){
        var msg = 'row selected ' + row.isSelected;
        $scope.selectedUser=row.entity;
       
      });
 
  };
 
  $scope.toggleRowSelection = function(gridApi) {
	  $scope.gridApi = gridApi;
    $scope.gridApi.selection.clearSelectedRows();
    $scope.gridOptions.enableRowSelection = !$scope.gridOptions.enableRowSelection;
    $scope.gridApi.core.notifyDataChange( uiGridConstants.dataChange.OPTIONS);
  };
 
  $http.get('/gtas/users/')
    .success(function(data) {    	
    	
      $scope.gridOptions.data = data;
 
      // $interval whilst we wait for the grid to digest the data we just gave it
      $interval( function() {$scope.gridApi.selection.selectRow($scope.gridOptions.data[0]);}, 0, 1);
    });
  
  $scope.addData = function() {
	  
	  $state.go('admin.addUser', { action: 'add', user: $scope.selectedUser });
	    
	  };
	  
	  $state.go('admin.users');
}]);