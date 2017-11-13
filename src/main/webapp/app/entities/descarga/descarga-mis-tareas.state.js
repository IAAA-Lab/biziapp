(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('descarga-mis-tareas', {
            parent: 'entity',
            url: '/descarga-mis-tareas?page&sort&search',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'jhipsterApp.descarga.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/descarga/descargasmisTareas.html',
                    controller: 'DescargaMisTareasController',
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
                    $translatePartialLoader.addPart('descarga');
                    $translatePartialLoader.addPart('tipo');
                    $translatePartialLoader.addPart('estado');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('descarga-mis-tareas-detail', {
            parent: 'descarga-mis-tareas',
            url: '/descarga-mis-tareas/{id}',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'jhipsterApp.descarga.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/descarga/descarga-mis-tareas-detail.html',
                    controller: 'DescargaMisTareasDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('descarga');
                    $translatePartialLoader.addPart('tipo');
                    $translatePartialLoader.addPart('estado');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Descarga', function($stateParams, Descarga) {
                    return Descarga.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'descarga-mis-tareas',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('descarga-mis-tareas-detail.edit', {
            parent: 'descarga-mis-tareas-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/descarga/descarga-mis-tareas-dialog.html',
                    controller: 'DescargaMisTareasDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Descarga', function(Descarga) {
                            return Descarga.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('descarga-mis-tareas.new', {
            parent: 'descarga-mis-tareas',
            url: '/new',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/descarga/descarga-mis-tareas-dialog.html',
                    controller: 'DescargaMisTareasDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                tipo: null,
                                fechaFichero: null,
                                categoria: null,
                                subcategoria: null,
                                estado: null,
                                createdAt: null,
                                updatedAt: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('descarga-mis-tareas', null, { reload: 'descarga-mis-tareas' });
                }, function() {
                    $state.go('descarga-mis-tareas');
                });
            }]
        })
        .state('descarga-mis-tareas.edit', {
            parent: 'descarga-mis-tareas',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/descarga/descarga-mis-tareas-dialog.html',
                    controller: 'DescargaMisTareasDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Descarga', function(Descarga) {
                            return Descarga.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('descarga-mis-tareas', null, { reload: 'descarga-mis-tareas' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('descarga-mis-tareas.delete', {
            parent: 'descarga-mis-tareas',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/descarga/descarga-mis-tareas-delete-dialog.html',
                    controller: 'DescargaMisTareasDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Descarga', function(Descarga) {
                            return Descarga.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('descarga-mis-tareas', null, { reload: 'descarga-mis-tareas' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
