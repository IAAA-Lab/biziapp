(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('InsercionMisTareasDetailController', InsercionMisTareasDetailController);

    InsercionMisTareasDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Insercion'];

    function InsercionMisTareasDetailController($scope, $rootScope, $stateParams, previousState, entity, Insercion) {
        var vm = this;

        vm.insercion = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('jhipsterApp:insercionUpdate', function(event, result) {
            vm.insercion = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
