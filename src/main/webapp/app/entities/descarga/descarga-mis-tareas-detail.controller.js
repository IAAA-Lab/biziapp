(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('DescargaMisTareasDetailController', DescargaMisTareasDetailController);

    DescargaMisTareasDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Descarga'];

    function DescargaMisTareasDetailController($scope, $rootScope, $stateParams, previousState, entity, Descarga) {
        var vm = this;

        vm.descarga = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('jhipsterApp:descargaUpdate', function(event, result) {
            vm.descarga = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
