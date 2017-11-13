(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('DescargaMisTareasDeleteController',DescargaMisTareasDeleteController);

    DescargaMisTareasDeleteController.$inject = ['$uibModalInstance', 'entity', 'Descarga'];

    function DescargaMisTareasDeleteController($uibModalInstance, entity, Descarga) {
        var vm = this;

        vm.descarga = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Descarga.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
