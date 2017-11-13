(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('DescargaMisTareasDialogController', DescargaMisTareasDialogController);

    DescargaMisTareasDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Descarga'];

    function DescargaMisTareasDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Descarga) {
        var vm = this;

        vm.descarga = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.descarga.id !== null) {
                Descarga.update(vm.descarga, onSaveSuccess, onSaveError);
            } else {
                Descarga.save(vm.descarga, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('jhipsterApp:descargaUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.createdAt = false;
        vm.datePickerOpenStatus.updatedAt = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
