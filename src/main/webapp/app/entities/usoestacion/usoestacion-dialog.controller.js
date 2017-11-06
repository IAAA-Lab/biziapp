(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('UsoestacionDialogController', UsoestacionDialogController);

    UsoestacionDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Usoestacion'];

    function UsoestacionDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Usoestacion) {
        var vm = this;

        vm.usoestacion = entity;
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
            if (vm.usoestacion.id !== null) {
                Usoestacion.update(vm.usoestacion, onSaveSuccess, onSaveError);
            } else {
                Usoestacion.save(vm.usoestacion, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('jhipsterApp:usoestacionUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.fechaDeUso = false;
        vm.datePickerOpenStatus.fechaObtencionDatos = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
