var app = angular.module("todooApp", ["ngAnimate", "ngRoute", "ngFileUpload"]);
var baseUrl = "/api";

app.config(function ($routeProvider) {

    $routeProvider
        .when('/tasks', {
            templateUrl: 'views/tasks.html',
            controller: 'taskController'
        })
        .when('/account', {
            templateUrl: 'views/account.html',
            controller: 'accountController'
        })
        .when('/about', {
            templateUrl: 'views/about.html'
        })
        .otherwise({
            redirectTo: '/tasks'
        });
});

app.controller("taskController", function ($rootScope, $scope, $http) {

    getUserName();

    function getUserName() {
        $http({
            method: "GET",
            url: baseUrl + "/user"
        }).then(
            function success(response) {
                $rootScope.username = response.data.name;
                getAllItems($rootScope.username);
            },
            function error(response) {
                alert(response.statusText);
            }
        );
    }

    function getAllItems(username) {

        $http({
            method: "GET",
            url: baseUrl + "/" + username + "/tasks"
        }).then(
            function success(response) {
                $scope.tasks = response.data;
            },
            function error(response) {
                alert(response.statusText);
            }
        );
    }

    $scope.addTask = function () {
        $http({
            method: "POST",
            url: baseUrl + "/tasks",
            data: {
                checklist: [],
                completed: false,
                contents: $scope.taskToAdd,
                creationDate: new Date(),
                deadline: "2019-04-22",
                userName: $scope.username
            }
        }).then(
            function success() {
                getAllItems($rootScope.username);
            },
            function error(response) {
                alert(response.statusText);
            }
        );
        clearTodoField();
    };

    function clearTodoField() {
        $scope.taskToAdd = "";
    }

    $scope.markTask = function (index) {
        $scope.tasks[index].completed = !$scope.tasks[index].completed;
        $scope.updateTask(index);
    };

    $scope.addChecklistItem = function (parentIndex) {
        $scope.tasks[parentIndex].checklist.push({
            description: "New item",
            completed: "false"
        });
        $scope.updateTask(parentIndex);
    };

    $scope.updateTask = function (x) {
        $http({
            method: "PUT",
            url: baseUrl + "/tasks/" + $scope.tasks[x].id,
            data: {
                checklist: $scope.tasks[x].checklist,
                completed: $scope.tasks[x].completed,
                contents: $scope.tasks[x].contents,
                creationDate: $scope.tasks[x].creationDate,
                deadline: $scope.tasks[x].deadline,
                userName: $scope.tasks[x].userName
            }
        }).then(
            function success() {
                getAllItems($rootScope.username);
            },
            function error(response) {
                alert(response.statusText);
            }
        );
    };

    $scope.deleteChecklistItem = function (parentIndex, index) {
        $scope.tasks[parentIndex].checklist.splice(index, 1);
        $scope.updateTask(parentIndex);
    };

    $scope.markChecklistItem = function (parentIndex, index) {
        $scope.tasks[parentIndex].checklist[index].completed = !$scope.tasks[parentIndex].checklist[index].completed;
        $scope.updateTask(parentIndex);
    };

    $scope.deleteTask = function (x) {
        $http({
            method: "DELETE",
            url: baseUrl + "/tasks/" + $scope.tasks[x].id
        }).then(
            function success() {
                getAllItems($rootScope.username);
            },
            function error(response) {
                alert(response.statusText);
            }
        );
    };

    $scope.logout = function () {
        $http.post("/logout", {})
            .then(function success() {
                    window.location.href = "/";
                },
                function error(response) {
                    alert(response.statusText);
                }
            );
    }
});

app.controller("securityController", function ($scope, $http) {

    $scope.credentials = {};

    $scope.login = function () {
        $http({
            method: "GET",
            url: baseUrl + "/user",
            headers: {
                "Authorization": "Basic " + btoa($scope.credentials.username + ":" + $scope.credentials.password)
            }
        }).then(
            function success() {
                window.location.href = "/index.html";
            },
            function error(response) {
                alert(response.statusText);
            }
        );
    }
});

app.controller("accountController", function ($rootScope, $scope, $http, Upload, $timeout) {

    getAccount();

    $scope.uploadFiles = function (file, errFiles) {
        $scope.f = file;
        $scope.errFile = errFiles && errFiles[0];
        if (file) {
            file.upload = Upload.upload({
                url: baseUrl + '/images/uploadFile',
                data: {file: file}
            });

            file.upload.then(function (response) {
                $timeout(function () {
                    file.result = response.data;
                    $scope.avatarUrl = file.result.fileDownloadUri;
                });
            }, function (response) {
                if (response.status > 0)
                    $scope.errorMsg = response.status + ': ' + response.data;
            }, function (evt) {
                file.progress = Math.min(100, Math.round(100.0 * evt.loaded / evt.total));
            });
        }
    };

    function getAccount() {
        $http({
            method: "GET",
            url: baseUrl + "/accounts/" + $rootScope.username
        }).then(
            function success(response) {
                $scope.accountData = response.data;
                if ($scope.accountData.avatar) {
                    $scope.avatarUrl = baseUrl + "/images/downloadFile/" + $scope.accountData.avatar.id;
                } else {
                    $scope.avatarUrl = "images/default_avatar.png";
                }
                console.log($scope.accountData);
            },
            function error(response) {
                alert(response.statusText);
            }
        );
    }

});
