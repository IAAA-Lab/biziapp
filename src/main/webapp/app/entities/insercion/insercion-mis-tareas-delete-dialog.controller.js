(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('InsercionMisTareasDeleteController',InsercionMisTareasDeleteController);

    InsercionMisTareasDeleteController.$inject = ['$uibModalInstance', 'entity', 'Insercion'];

    function InsercionMisTareasDeleteController($uibModalInstance, entity, Insercion) {
        var vm = this;

        vm.insercion = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Insercion.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
