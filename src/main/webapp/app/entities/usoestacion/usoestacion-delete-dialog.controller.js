(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('UsoestacionDeleteController',UsoestacionDeleteController);

    UsoestacionDeleteController.$inject = ['$uibModalInstance', 'entity', 'Usoestacion'];

    function UsoestacionDeleteController($uibModalInstance, entity, Usoestacion) {
        var vm = this;

        vm.usoestacion = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Usoestacion.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
