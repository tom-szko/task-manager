angular.module('todooApp').controller('securityController', securityController);

securityController.$inject = ['$scope', '$http', 'baseUrl'];

function securityController($scope, $http, baseUrl) {

    $scope.credentials = {};

    $scope.login = function () {
        $http({
            method: 'GET',
            url: baseUrl + '/user',
            headers: {
                'Authorization': 'Basic ' + btoa($scope.credentials.username + ':' + $scope.credentials.password)
            }
        }).then(
            function success() {
                window.location.href = '/index.html';
            },
            function error(response) {
                alert(response.statusText);
            }
        );
    }
}
