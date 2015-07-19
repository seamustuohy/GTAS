app.service("queryService", function( $rootScope, $http, $q ) {
    var baseUrl = 'gtas/query/';

    // Return public API.
    return({
        getList: getListByAuthor,
        loadRuleById: loadRuleById,
        deleteQuery: deleteQuery,
        saveQuery: saveQuery
    });

    // LOLA DID NOT PROVIDE
    function loadRuleById(ruleId, userId) {
        var request;

        if (typeof ruleId === 'undefined' || ruleId === null ) {
            console.log('not valid queryId');
            return;
        }

        request = $http({
            method: "get",
            url: baseUrl + "listQuery?userId=" + userId + "&id=" + ruleId,
            params: {
                action: "get"
            }
        });

        return( request.then( handleSuccess, handleError ) );
    }

    function deleteQuery(ruleId, userId) {
        var request;

        if (typeof userId === 'undefined' || userId === null ) {
            console.log('not valid user');
            return;
        }

        if (typeof ruleId === 'undefined' || ruleId === null ) {
            console.log('not valid queryId');
            return;
        }

        // uniform would be: url: '/gtas/query/deleteQuery/'+ userId + '/' + ruleId
        request = $http({
            method: 'delete',
            url: '/gtas/query/deleteQuery?userId='+ userId + '&id' + ruleId
        });

        return( request.then( handleSuccess, handleError ) );
    }

    function saveQuery(data) {
        var method, request, url = 'gtas/query/';
        if ( data.id === null ) {
            method = 'post';
            url = url + 'saveQuery';
        } else {
                method = 'put';
                url = url + 'editQuery';
        }

        request = $http({
            method: method,
            url: url,
            data: data
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
            url: baseUrl + "listQuery?userId=" + userId,
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
