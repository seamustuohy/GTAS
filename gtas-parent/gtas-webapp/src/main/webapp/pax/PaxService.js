/*
 * All GTAS code is Copyright 2016, Unisys Corporation.
 * 
 * Please see LICENSE.txt for details.
 */
(function () {
    'use strict';
    app
        .service('paxDetailService', function ($http, $q) {
            function getPaxDetail(paxId, flightId) {
                var dfd = $q.defer();
                dfd.resolve($http.get("/gtas/passengers/passenger/" + paxId + "/details?flightId=" + flightId));
                return dfd.promise;
            }
            
            function getPaxFlightHistory(paxId){
                var dfd = $q.defer();
                dfd.resolve($http.get("/gtas/passengers/passenger/flighthistory?paxId=" + paxId));
                return dfd.promise;
            }

            return ({getPaxDetail: getPaxDetail,
                    getPaxFlightHistory: getPaxFlightHistory});
        })
        .service('caseService', function ($http, $q) {
            function createDisposition(disposition) {
                var dfd = $q.defer();
                dfd.resolve($http.post("/gtas/disposition", disposition));
                return dfd.promise;
            }

            function getDispositionStatuses() {
                var dfd = $q.defer();
                dfd.resolve($http.get("/gtas/dispositionstatuses"));
                return dfd.promise;
            }
            
            function getAllCases(){
            	var dfd = $q.defer();
            	dfd.resolve($http.get("/gtas/allcases"));
            	return dfd.promise;
            }

            return ({
                getDispositionStatuses: getDispositionStatuses,
                createDisposition: createDisposition,
                getAllCases:getAllCases
            });
        })
        .service("paxService", function (userService, $rootScope, $http, $q) {
            
            function getPassengersBasedOnUser(paxModel){
                var today = new Date();
                //first request
                 return userService.getUserData().then( function( user ) {
                    if(user.data.filter!=null) {
                        if (user.data.filter.flightDirection)
                            paxModel.model.direction = user.data.filter.flightDirection;
                        if (typeof user.data.filter.etaStart  != undefined && user.data.filter.etaStart != null) {
                            paxModel.model.starteeDate = new Date();
                            paxModel.model.etaStart.setDate(today.getDate() + user.data.filter.etaStart);
                        }
                        if (typeof user.data.filter.etaEnd  != undefined && user.data.filter.etaEnd != null) {
                            paxModel.model.endDate = new Date();
                            paxModel.model.etaEnd.setDate(today.getDate() + user.data.filter.etaEnd);
                        }
                        if (user.data.filter.originAirports != null)
                            paxModel.model.origin = paxModel.model.origins = user.data.filter.originAirports;
                        if (user.data.filter.destinationAirports != null)
                            paxModel.model.dest = paxModel.model.destinations = user.data.filter.destinationAirports;
                    }
                    //second request
                    return getAllPax(paxModel.model);
                });
            }
            
            
            function getPax(flightId, pageRequest) {
                var dfd = $q.defer();
                dfd.resolve($http({
                    method: 'post',
                    url: '/gtas/flights/flight/' + flightId + '/passengers',
                    data: pageRequest
                }));
                return dfd.promise;
            }

            function getAllPax(pageRequest) {
                var dfd = $q.defer();
                dfd.resolve($http({
                    method: 'post',
                    url: '/gtas/passengers/',
                    data: pageRequest
                }));
                return dfd.promise;
            }

            function getPaxDetail(passengerId, flightId) {
                var url = "/gtas/passengers/passenger/" + passengerId + "/details?flightId=" + flightId;
                return $http.get(url).then(function (res) {
                    return res.data;
                });
            }

            function handleSuccess(response) {
                return (response.data);
            }

            function handleError(response) {
                if (!angular.isObject(response.data) || !response.data.message) {
                    return ($q.reject("An unknown error occurred."));
                }
                return ($q.reject(response.data.message));
            }

            function broadcast(flightId) {
                $rootScope.$broadcast('handleBroadcast', flightId);
            }

            function getRuleHits(passengerId) {
                var request = $http({
                    method: "get",
                    url: "/gtas/hit/passenger" + (passengerId ? "?passengerId=" + passengerId : ""),
                    params: {
                        action: "get"
                    }
                });
                return (request.then(handleSuccess, handleError));
            }

            function broadcastRuleID(ruleID) {
                $rootScope.$broadcast('ruleIDBroadcast', ruleID);
            }

            // Return public API.
            return ({
                getPax: getPax,
                getAllPax: getAllPax,
                broadcast: broadcast,
                getRuleHits: getRuleHits,
                getPaxDetail: getPaxDetail,
                broadcastRuleID: broadcastRuleID,
                getPassengersBasedOnUser: getPassengersBasedOnUser
            });
        });
}());
