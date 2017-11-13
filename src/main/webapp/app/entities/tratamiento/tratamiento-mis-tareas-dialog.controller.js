(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('TratamientoMisTareasDialogController', TratamientoMisTareasDialogController);

    TratamientoMisTareasDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Tratamiento'];

    function TratamientoMisTareasDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Tratamiento) {
        var vm = this;

        vm.tratamiento = entity;
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
            if (vm.tratamiento.id !== null) {
                Tratamiento.update(vm.tratamiento, onSaveSuccess, onSaveError);
            } else {
                Tratamiento.save(vm.tratamiento, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('jhipsterApp:tratamientoUpdate', result);
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
