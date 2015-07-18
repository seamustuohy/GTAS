app.service("queryService", function( $rootScope, $http, $q ) {
    // Return public API.
    return({
        getList: getListByAuthor,
        loadRuleById: loadRuleById,
        ruleDelete: ruleDelete,
        ruleSave: ruleSave,
        broadcast: broadcast
    });

    function loadRuleById(ruleId) {
        var request = $http({
            method: "get",
            url: "/gtas/udr/get/" + ruleId,
            params: {
                action: "get"
            }
        });
        return( request.then( handleSuccess, handleError ) );
    }

    function ruleDelete(ruleId, authorId) {
        var request;

        if (ruleId === null) return;

        request = $http({
            method: 'delete',
            url: '/gtas/udr/'+ authorId + '/' + ruleId
        });
        return( request.then( handleSuccess, handleError ) );
    }

    function ruleSave(ruleObj, authorId) {
        var method = ruleObj.id === null ? 'post' : 'put';
        var request = $http({
            method: method,
            url: '/gtas/udr/'+ authorId,
            data: ruleObj
        });
        return( request.then( handleSuccess, handleError ) );
    }

    function getListByAuthor(authorId) {
        var request = $http({
            method: "get",
            url: "/gtas/udr/list/" + authorId,
            params: {
                action: "get"
            }
        });
        return( request.then( handleSuccess, handleError ) );
    }

    function getListForAnthony() {
        var request = $http({
            method: "get",
            url: "/gtas/udr/list/adelorie",
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

    function broadcast(authorId) {
        $rootScope.$broadcast('handleBroadcast', flightId);
    }
});
