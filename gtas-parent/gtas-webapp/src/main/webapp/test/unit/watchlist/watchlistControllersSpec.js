'use strict';
/* *************************************************** */
/* Jasmine test specifications for the WatchlistController */
/* *************************************************** */

describe('Watchlist controller:', function() {

  /* Test1: Invoke the controller on the default Document Tab */
  describe('Activation of Default(Document) Tab:', function(){
    var scope, ctrl, $httpBackend, gridOptionsLookupService;

    beforeEach(module('myApp'));
    beforeEach(inject(function(_$httpBackend_, $rootScope, $controller, _gridOptionsLookupService_) {
      $httpBackend = _$httpBackend_;
      scope = $rootScope.$new();
      gridOptionsLookupService = _gridOptionsLookupService_;
      ctrl = $controller('WatchListController', {$scope: scope});
    }));

    it('should set the default scope elements', function() {
      expect(scope.tabs.length).toBe(2);
      expect(scope.documentTypes).toEqual([
                                          {id: "P", label: "PASSPORT"},
                                          {id: "V", label: "VISA"}
                                      ]);
      expect(scope.watchlistGrid.enableRowHeaderSelection).toBe(true);
      expect(scope.watchlistGrid.enableSelectAll).toBe(true);
      expect(scope.watchlistGrid.multiSelect).toBe(true);
      expect(scope.watchlistGrid.columnDefs).toEqual(gridOptionsLookupService.getLookupColumnDefs('watchlist').DOCUMENT);
      expect(scope.activeTab).toBe('Document');
      //expect(scope.icon).toBe('file');
      expect(scope.rowSelected).toBe(null);
    });

/* was expecting a document fetch call ??!! */
    
//    it('should fetch two documents from xhr', function() {
//      $httpBackend.expectGET('/gtas//wl/Document/Document').
//          respond([{documentType: 'P', documentNumber: '12345'}, {documentType: 'V', documentNumber: 'V7657'}]);
//
//      $httpBackend.flush();
//      expect(scope.watchlistGrid.data).toEqual([{documentType: 'P', documentNumber: '12345'}, {documentType: 'V', documentNumber: 'V7657'}]);
//    });

  });

});
