(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('UsoestacionDetailController', UsoestacionDetailController);

    UsoestacionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Usoestacion'];

    function UsoestacionDetailController($scope, $rootScope, $stateParams, previousState, entity, Usoestacion) {
        var vm = this;

        vm.usoestacion = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('jhipsterApp:usoestacionUpdate', function(event, result) {
            vm.usoestacion = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
