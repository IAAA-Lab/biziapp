(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('TratamientoMisTareasDeleteController',TratamientoMisTareasDeleteController);

    TratamientoMisTareasDeleteController.$inject = ['$uibModalInstance', 'entity', 'Tratamiento'];

    function TratamientoMisTareasDeleteController($uibModalInstance, entity, Tratamiento) {
        var vm = this;

        vm.tratamiento = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Tratamiento.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
