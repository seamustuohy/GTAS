'use strict';
/* *************************************************** */
/* Miscellaneous Jasmine test specifications */
/* *************************************************** */

describe('Miscellaneous Tests:', function() {

  /* Test1: Invoke the controller on the default Tab (i.e., tab 0) */
  describe('HTTP functions:', function(){
    var scope, $httpBackend;

    beforeEach(module('myApp'));
    beforeEach(inject(function(_$httpBackend_, $rootScope, $http) {
      $httpBackend = _$httpBackend_;
      scope = $rootScope.$new();
  	  scope.test = function(code){
	    		$http.get('/test'+code)
	    		.then(
	    				function(resp){//success handler
	    					scope.status=resp.status;
	    					scope.result='OK';
	    					},
	    				function(resp){//error handler
	    					scope.status=resp.status;
	    					scope.result = 'FAIL';
	    					}
	    		);
	  }

    }));

    it('should handle http success', function() {
        $httpBackend.expectGET('/test200').
        respond(200, {status:'SUCCESS', message:'The request waas successful', result:{}});
          
        scope.test(200);
    
        $httpBackend.flush();
    
        expect(scope.status).toEqual(200);
        expect(scope.result).toEqual('OK');
    });

  
    it('should handle http error BAD_REQUEST (400)', function() {
        $httpBackend.expectGET('/test400').
        respond(400, {status:'FAILURE', message:'There was an error', result:{}});
          
        scope.test(400);
    
        $httpBackend.flush();
    
        expect(scope.status).toEqual(400);
        expect(scope.result).toEqual('FAIL');
    });

  });
});
