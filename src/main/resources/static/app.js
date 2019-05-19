var app = angular.module("todooApp", []);
var baseUrl = "/api";

app.controller("taskController", function($scope, $http) {

  document.querySelector(".task-add-btn").addEventListener("click", addTask);
  getAllItems();

  function getAllItems() {
    console.log("retrieved records from DB");
    $http({
      method: "GET",
      url: baseUrl + "/tasks"
    }).then(
      function success(response) {
        $scope.tasks = response.data;
      },
      function error(response) {
        alert(response.statusText);
      }
    );
  }

  function addTask() {
    $http({
      method: "POST",
      url: baseUrl + "/tasks",
      data: {
        checklist: [],
        completed: false,
        contents: $scope.taskToAdd,
        creationDate: new Date(),
        deadline: "2019-04-22",
        userName: "admin"
      }
    }).then(
      function success() {
        console.log("successfully added new task");
        getAllItems();
      },
      function error(response) {
        alert(response.statusText);
      }
    );
    clearTodoField();
  }

  function clearTodoField() {
    $scope.taskToAdd = "";
  }

  $scope.markTask = function(x) {
    !$scope.tasks[x].completed;
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
        getAllItems();
      },
      function error(response) {
        alert(response.statusText);
      }
    );
  };

  $scope.deleteTask = function(x) {
    $http({
      method: "DELETE",
      url: baseUrl + "/tasks/" + $scope.tasks[x].id
    }).then(
      function success() {
        getAllItems();
      },
      function error(response) {
        alert(response.statusText);
      }
    );
  };

  $scope.logout = function() {
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

app.controller("securityController", function($scope, $http) {

  document.querySelector(".submit-btn").addEventListener("click", login);
  $scope.credentials = {};

  function login() {
  console.log("login fired")
    $http({
    method: "GET",
    url: baseUrl + "/user",
    headers: {"Authorization": "Basic " + btoa($scope.credentials.username + ":" + $scope.credentials.password)
    }}).then(
             function success(response) {
               console.log("success");
               window.location.href = "/home.html";
             },
             function error(response) {
               alert(response.statusText);
             }
           );
         }
});
