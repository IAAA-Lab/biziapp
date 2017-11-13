(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('TratamientoMisTareasDetailController', TratamientoMisTareasDetailController);

    TratamientoMisTareasDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Tratamiento'];

    function TratamientoMisTareasDetailController($scope, $rootScope, $stateParams, previousState, entity, Tratamiento) {
        var vm = this;

        vm.tratamiento = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('jhipsterApp:tratamientoUpdate', function(event, result) {
            vm.tratamiento = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
