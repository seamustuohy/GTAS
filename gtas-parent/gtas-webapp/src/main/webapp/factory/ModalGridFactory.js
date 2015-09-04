app.factory('Modal', ['$compile', '$rootScope', function ($compile, $rootScope) {
    'use strict';
    return function () {
        var elm,
            modal = {
                open: function () {
                    var html = '<div class="modal" ng-style="modalStyle">{{modalStyle}}<div class="modal-dialog"><div class="modal-content"><div class="modal-header"><h4 style="font-size: 18px;" align="left" class="ng-binding"><span class="glyphicon glyphicon-user glyphiconFlightPax img-circle"></span> Passengers on Flight - {{selectedFlightNumber}}</h4></div><div class="modal-body"><div id="grid1" ui-grid="gridOptions" ui-grid-expandable ui-grid-pagination ui-grid-exporter ui-grid-resize-columns ui-grid-move-columns class="grid"></div></div><div class="modal-footer"><button id="buttonClose" class="btn btn-primary" ng-click="close()">Close</button></div></div></div></div>';
                    elm = angular.element(html);
                    angular.element(document.body).prepend(elm);

                    $rootScope.close = function () {
                        modal.close();
                    };
                    $rootScope.modalStyle = {"display": "block"};
                    $compile(elm)($rootScope);
                },
                close: function () {
                    if (elm) {
                        elm.remove();
                    }
                }
            };

        return modal;
    };
}]);
