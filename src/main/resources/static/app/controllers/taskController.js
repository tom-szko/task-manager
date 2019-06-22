var baseUrl = '/api';

angular.module('todooApp').controller('taskController', taskController);

taskController.$inject = ['$rootScope', '$scope', '$http'];

function taskController($rootScope, $scope, $http) {

    getUserName();

    function getUserName() {
        $http({
            method: 'GET',
            url: baseUrl + '/user'
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
            method: 'GET',
            url: baseUrl + '/' + username + '/tasks'
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
            method: 'POST',
            url: baseUrl + '/tasks',
            data: {
                checklist: [],
                completed: false,
                contents: $scope.taskToAdd,
                creationDate: new Date(),
                deadline: '2019-04-22',
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
        $scope.taskToAdd = '';
    }

    $scope.markTask = function (index) {
        $scope.tasks[index].completed = !$scope.tasks[index].completed;
        $scope.updateTask(index);
    };

    $scope.addChecklistItem = function (parentIndex) {
        $scope.tasks[parentIndex].checklist.push({
            description: 'New item',
            completed: 'false'
        });
        $scope.updateTask(parentIndex);
    };

    $scope.updateTask = function (x) {
        $http({
            method: 'PUT',
            url: baseUrl + '/tasks/' + $scope.tasks[x].id,
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
            method: 'DELETE',
            url: baseUrl + '/tasks/' + $scope.tasks[x].id
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
        $http.post('/logout', {})
            .then(function success() {
                    window.location.href = '/';
                },
                function error(response) {
                    alert(response.statusText);
                }
            );
    }
}
