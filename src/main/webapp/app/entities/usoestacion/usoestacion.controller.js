(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .controller('UsoestacionController', UsoestacionController);

    UsoestacionController.$inject = ['$state', 'Usoestacion', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams', 'FileSaver', 'Blob'];

    function UsoestacionController($state, Usoestacion, ParseLinks, AlertService, paginationConstants, pagingParams, FileSaver, Blob) {

        var vm = this;

        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;

        vm.datosPagina = null;
        vm.sizeAllData = 429496729;

        loadAll();

        function loadAll () {
            Usoestacion.query({
                page: pagingParams.page - 1,
                size: vm.itemsPerPage,
                sort: sort()
            }, onSuccess, onError);
            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }
            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                vm.usoestacions = data;
                vm.page = pagingParams.page;
                vm.datosPagina = JSON.stringify(data);
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function loadPage(page) {
            vm.page = page;
            vm.transition();
        }

        function transition() {
            $state.transitionTo($state.$current, {
                page: vm.page,
                sort: vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc'),
                search: vm.currentSearch
            });
        }

        vm.download = function(text) {
            var datos = new Blob([text], { type: 'application/json' });
            FileSaver.saveAs(datos, 'datos.json');
        };

        vm.downloadAll = function() {
            Usoestacion.query({
                page: 0,
                size: vm.sizeAllData,
                sort: sort()
            }, onSuccess, onError);
            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }
            function onSuccess(data) {
                var datosJson = JSON.stringify(data);
                    var datosBlob = new Blob([datosJson], { type: 'application/json' });
                        FileSaver.saveAs(datosBlob, 'datos.json');
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

    }
})();
