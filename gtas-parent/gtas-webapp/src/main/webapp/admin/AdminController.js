app.controller('AdminCtrl', ['$scope', '$http', '$state','$stateParams', 'uiGridConstants','$interval',
                             function ($scope, $http,$state,$stateParams, uiGridConstants,$interval) {

  $scope.gridOptions = { enableRowSelection: true, enableRowHeaderSelection: false };

  $scope.selectedUser=$stateParams.user;
  $scope.gridOptions.columnDefs = [
    { name: 'userId' ,field: 'userId', width:'15%'},
    { name: 'firstName',field:'firstName',width:'15%'},
    { name: 'lastName',field:'lastName',width:'20%'} ,
    { name: 'active' ,field:'active',cellFilter: 'userStatusFilter',width:'10%'},
    {name: 'roles', field:'roles' ,cellFilter: 'roleDescriptionFilter',width:'40%',
        cellTooltip: function(row){ return row.entity.roles; },
        cellTemplate: '<div class="ui-grid-cell-contents wrap" style="white-space: normal" title="TOOLTIP">{{COL_FIELD CUSTOM_FILTERS}}</div>'}
  ];
  $scope.gridOptions.multiSelect = false;
  $scope.gridOptions.modifierKeysToMultiSelect = false;
  $scope.gridOptions.noUnselect = true;
  $scope.gridOptions.onRegisterApi = function( gridApi) {
        $scope.gridApi = gridApi;
        gridApi.selection.on.rowSelectionChanged($scope,function(row){
        $scope.selectedUser=row.entity;
        });

        if($scope.selectedUser!=null && $scope.selectedUser.userId.length>0 ) {
          var selectedRowIndex=$scope. getSelectedRowIndexOnUserId($scope.selectedUser.userId);
          if(selectedRowIndex!=null) {
              $interval(function () {
                  $scope.gridApi.selection.selectRow($scope.gridOptions.data[selectedRowIndex]);
              }, 0, 1);
          }
          else
          {   //User created ,refresh all users
              $http.get('/gtas/users/')
                  .success(function(data) {
                      $scope.gridOptions.data = data;
                      // $interval whilst we wait for the grid to digest the data we just gave it
                      $interval( function() {$scope.gridApi.selection.selectRow($scope.gridOptions.data[0]);}, 0, 1);
                  });
          }
      }
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
  
  $scope.modifyUser = function() {
	  $state.go('admin.addUser', { action: 'modify', user: $scope.selectedUser });
  };

 $scope.getSelectedRowIndexOnUserId=function (userId) {
     var i = 0, len = $scope.gridOptions.data.length;
     if (userId.length <= 0) {
     } else {
         for (i = 0; i < len; i++) {
             if ( $scope.gridOptions.data[i].userId === userId) {
                 {
                     //alert("Found match");
                     return i;
                 }
             }
         }
     }
     return null;
 };
 $scope.createUser = function() {
   $state.go('admin.addUser', { action: 'create', user: null });
 };
	  $state.go('admin.users');
}]);