app.service("queryService", function( $rootScope, $http, $q ) {
    var baseUrl = '/gtas/udr/';

    // Return public API.
    return({
        getList: getListByAuthor,
        loadRuleById: loadRuleById,
        ruleDelete: ruleDelete,
        ruleSave: ruleSave
    });

    function loadRuleById(ruleId) {
        var request;

        if (typeof ruleId === 'undefined' || ruleId === null ) {
            console.log('not valid ruleId');
            return;
        }

        request = $http({
            method: "get",
            url: baseUrl + "get/" + ruleId,
            params: {
                action: "get"
            }
        });

        return( request.then( handleSuccess, handleError ) );
    }

    function ruleDelete(ruleId, userId) {
        var request;

        if (typeof userId === 'undefined' || userId === null ) {
            console.log('not valid user');
            return;
        }

        if (typeof ruleId === 'undefined' || ruleId === null ) {
            console.log('not valid ruleId');
            return;
        }

        request = $http({
            method: 'delete',
            url: baseUrl + userId + '/' + ruleId
        });

        return( request.then( handleSuccess, handleError ) );
    }

    function ruleSave(ruleObj, userId) {
        var method, request;

        if (typeof userId === 'undefined' || userId === null ) {
            console.log('not valid user');
            return;
        }

        method = ruleObj.id === null ? 'post' : 'put';
        request = $http({
            method: method,
            url: baseUrl + userId,
            data: ruleObj
        });

        return( request.then( handleSuccess, handleError ) );
    }

    function getListByAuthor(userId) {
        var request;

        if (typeof userId === 'undefined' || userId === null ) {
            console.log('not valid user');
            return;
        }

        request = $http({
            method: "get",
            url: baseUrl + "list/" + userId,
            params: {
                action: "get"
            }
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
});
