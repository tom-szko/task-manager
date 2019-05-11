var app = angular.module("todooApp", []);
var baseUrl = "http://localhost:8080/api";

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
        creationDate: "2019-04-22",
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

});
