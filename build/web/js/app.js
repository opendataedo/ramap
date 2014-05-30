'use strict';

// Declare app level module which depends on filters, and services
var app = angular.module('myApp', ['myApp.filters', 'myApp.services']).
  config(['$routeProvider', function($routeProvider) {
    $routeProvider.when('/home', {templateUrl: 'partials/home.html', controller: 'mainCtrl'});
    $routeProvider.when('/about', {templateUrl: 'partials/about.html', controller: 'MyCtrl2'});
    $routeProvider.when('/terms', {templateUrl: 'partials/terms.html', controller: 'MyCtrl2'});
    $routeProvider.otherwise({redirectTo: '/home'});
  }]);

app.directive('showInfo',function(){
    return {
        restrict: "A",
        link: function(scope, element, attributes){
            element.bind('mouseover',function(){
                var data = getData(attributes.id);
                scope.area = data[3];
                scope.amount = currency('N', data[5]);
                scope.percent = diffFormatter(data[7]);
                scope.$apply();                
            });            
        }
    }
});
