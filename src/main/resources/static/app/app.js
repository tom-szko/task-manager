var app = angular.module('todooApp', ['ngAnimate', 'ngRoute', 'ngFileUpload']);

app.config(function ($routeProvider) {

    $routeProvider
        .when('/tasks', {
            templateUrl: '../../views/tasks.html',
            controller: 'taskController'
        })
        .when('/account', {
            templateUrl: '../../views/account.html',
            controller: 'accountController'
        })
        .when('/about', {
            templateUrl: '../../views/about.html'
        })
        .otherwise({
            redirectTo: '/tasks'
        });
});

app.constant('baseUrl', '/api');
