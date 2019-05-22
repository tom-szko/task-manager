var app = angular.module("todooApp", ['ngAnimate']);
var baseUrl = "/api";

app.controller("taskController", function($scope, $http) {

  getUserName();
  getAllItems();

  function getUserName() {
    console.log("getting user name");
        $http({
          method: "GET",
          url: baseUrl + "/user"
        }).then(
          function success(response) {
            $scope.username = response.data.name;
          },
          function error(response) {
            alert(response.statusText);
          }
        );
  }

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

  $scope.addTask = function() {
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

    $scope.markChecklistItem = function(parentIndex, index) {
      !$scope.tasks[parentIndex].checklist[index].completed;
      $http({
        method: "PUT",
        url: baseUrl + "/tasks/" + $scope.tasks[parentIndex].id,
        data: {
         checklist: $scope.tasks[parentIndex].checklist,
         completed: $scope.tasks[parentIndex].completed,
         contents: $scope.tasks[parentIndex].contents,
         creationDate: $scope.tasks[parentIndex].creationDate,
         deadline: $scope.tasks[parentIndex].deadline,
         userName: $scope.tasks[parentIndex].userName
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

  $scope.credentials = {};

  $scope.login = function() {
  console.log("login fired")
    $http({
    method: "GET",
    url: baseUrl + "/user",
    headers: {"Authorization": "Basic " + btoa($scope.credentials.username + ":" + $scope.credentials.password)
    }}).then(
             function success(response) {
               window.location.href = "/home.html";
             },
             function error(response) {
               alert(response.statusText);
             }
           );
         }
});
