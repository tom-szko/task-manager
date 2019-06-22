var baseUrl = '/api';

angular.module('todooApp').controller('securityController', securityController);

securityController.$inject = ['$scope', '$http'];

function securityController($scope, $http) {

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
