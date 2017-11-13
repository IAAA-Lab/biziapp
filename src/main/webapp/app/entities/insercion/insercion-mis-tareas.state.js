(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('insercion-mis-tareas', {
            parent: 'entity',
            url: '/insercion-mis-tareas?page&sort&search',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'jhipsterApp.insercion.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/insercion/insercionsmisTareas.html',
                    controller: 'InsercionMisTareasController',
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
                    $translatePartialLoader.addPart('insercion');
                    $translatePartialLoader.addPart('tipo');
                    $translatePartialLoader.addPart('estado');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('insercion-mis-tareas-detail', {
            parent: 'insercion-mis-tareas',
            url: '/insercion-mis-tareas/{id}',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'jhipsterApp.insercion.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/insercion/insercion-mis-tareas-detail.html',
                    controller: 'InsercionMisTareasDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('insercion');
                    $translatePartialLoader.addPart('tipo');
                    $translatePartialLoader.addPart('estado');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Insercion', function($stateParams, Insercion) {
                    return Insercion.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'insercion-mis-tareas',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('insercion-mis-tareas-detail.edit', {
            parent: 'insercion-mis-tareas-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/insercion/insercion-mis-tareas-dialog.html',
                    controller: 'InsercionMisTareasDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Insercion', function(Insercion) {
                            return Insercion.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('insercion-mis-tareas.new', {
            parent: 'insercion-mis-tareas',
            url: '/new',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/insercion/insercion-mis-tareas-dialog.html',
                    controller: 'InsercionMisTareasDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                idTarea: null,
                                tipo: null,
                                fechaFichero: null,
                                pathFicheroCSV: null,
                                estado: null,
                                createdAt: null,
                                updatedAt: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('insercion-mis-tareas', null, { reload: 'insercion-mis-tareas' });
                }, function() {
                    $state.go('insercion-mis-tareas');
                });
            }]
        })
        .state('insercion-mis-tareas.edit', {
            parent: 'insercion-mis-tareas',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/insercion/insercion-mis-tareas-dialog.html',
                    controller: 'InsercionMisTareasDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Insercion', function(Insercion) {
                            return Insercion.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('insercion-mis-tareas', null, { reload: 'insercion-mis-tareas' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('insercion-mis-tareas.delete', {
            parent: 'insercion-mis-tareas',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/insercion/insercion-mis-tareas-delete-dialog.html',
                    controller: 'InsercionMisTareasDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Insercion', function(Insercion) {
                            return Insercion.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('insercion-mis-tareas', null, { reload: 'insercion-mis-tareas' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
