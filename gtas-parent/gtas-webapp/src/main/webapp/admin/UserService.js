/**
 * Created by GTAS on 9/18/2015.
 */
app.service('UserService', [ '$http', '$q', function($http, $q) {

	return({
		getRoles: getRoles,
        updateUser:updateUser,
        createUser:createUser
	});


	function getRoles() {
		var request = $http({
			method: "get",
			url: "/gtas/roles/" ,
			params: {
				action: "get"
			}
		});
		return( request.then( handleSuccess, handleError ) );
	}

	function updateUser(user) {

		var PUT_USER_URL = '/gtas/users/' + user.userId;
		var request = $http({
			method: "put",
			url: PUT_USER_URL ,
			data:user
		});
		return( request.then( handleSuccess, handleError ) );
	}
	
	function createUser(user) {

		var POST_USER_URL = '/gtas/users/' + user.userId;
		var request = $http({
			method: "post",
			url:POST_USER_URL ,
			data:user

		});
		return( request.then( handleSuccess, handleError ) );
	}

	function handleError( response ) {
		if (! angular.isObject( response.data ) || ! response.data.message) {
			return( $q.reject( "An unknown error occurred." ) );
		}
		return( $q.reject( response.data.message ) );
	}

	function handleSuccess( response ) {
		return( response.data );
	}

} ]);
