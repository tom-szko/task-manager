var baseUrl = '/api';

angular.module('todooApp').controller('accountController', accountController);

accountController.$inject = ['$rootScope', '$scope', '$http', 'Upload', '$timeout'];

function accountController($rootScope, $scope, $http, Upload, $timeout) {

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
            method: 'GET',
            url: baseUrl + '/accounts/' + $rootScope.username
        }).then(
            function success(response) {
                $scope.accountData = response.data;
                if ($scope.accountData.avatar) {
                    $scope.avatarUrl = baseUrl + '/images/downloadFile/' + $scope.accountData.avatar.id;
                } else {
                    $scope.avatarUrl = 'images/default_avatar.png';
                }
                console.log($scope.accountData);
            },
            function error(response) {
                alert(response.statusText);
            }
        );
    }
}
