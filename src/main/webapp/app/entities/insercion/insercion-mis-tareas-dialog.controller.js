(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('InsercionMisTareasDialogController', InsercionMisTareasDialogController);

    InsercionMisTareasDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Insercion'];

    function InsercionMisTareasDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Insercion) {
        var vm = this;

        vm.insercion = entity;
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
            if (vm.insercion.id !== null) {
                Insercion.update(vm.insercion, onSaveSuccess, onSaveError);
            } else {
                Insercion.save(vm.insercion, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('jhipsterApp:insercionUpdate', result);
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
