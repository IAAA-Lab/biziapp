(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('usoestacion', {
            parent: 'entity',
            url: '/usoestacion?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jhipsterApp.usoestacion.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/usoestacion/usoestacions.html',
                    controller: 'UsoestacionController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('usoestacion');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('usoestacion-detail', {
            parent: 'usoestacion',
            url: '/usoestacion/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jhipsterApp.usoestacion.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/usoestacion/usoestacion-detail.html',
                    controller: 'UsoestacionDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('usoestacion');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Usoestacion', function($stateParams, Usoestacion) {
                    return Usoestacion.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'usoestacion',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('usoestacion-detail.edit', {
            parent: 'usoestacion-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/usoestacion/usoestacion-dialog.html',
                    controller: 'UsoestacionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Usoestacion', function(Usoestacion) {
                            return Usoestacion.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('usoestacion.new', {
            parent: 'usoestacion',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/usoestacion/usoestacion-dialog.html',
                    controller: 'UsoestacionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                nombreCompleto: null,
                                idEstacion: null,
                                nombreEstacion: null,
                                fechaDeUso: null,
                                intervaloDeTiempo: null,
                                devolucionTotal: null,
                                devolucionMedia: null,
                                retiradasTotal: null,
                                retiradasMedia: null,
                                neto: null,
                                total: null,
                                fechaObtencionDatos: null,
                                ficheroCSV: null,
                                ficheroXLS: null,
                                hashcode: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('usoestacion', null, { reload: 'usoestacion' });
                }, function() {
                    $state.go('usoestacion');
                });
            }]
        })
        .state('usoestacion.edit', {
            parent: 'usoestacion',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/usoestacion/usoestacion-dialog.html',
                    controller: 'UsoestacionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Usoestacion', function(Usoestacion) {
                            return Usoestacion.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('usoestacion', null, { reload: 'usoestacion' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('usoestacion.delete', {
            parent: 'usoestacion',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/usoestacion/usoestacion-delete-dialog.html',
                    controller: 'UsoestacionDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Usoestacion', function(Usoestacion) {
                            return Usoestacion.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('usoestacion', null, { reload: 'usoestacion' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
