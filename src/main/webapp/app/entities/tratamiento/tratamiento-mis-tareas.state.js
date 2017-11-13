(function() {
    'use strict';

    angular
        .module('jhipsterApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('tratamiento-mis-tareas', {
            parent: 'entity',
            url: '/tratamiento-mis-tareas?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jhipsterApp.tratamiento.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/tratamiento/tratamientosmisTareas.html',
                    controller: 'TratamientoMisTareasController',
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
                    $translatePartialLoader.addPart('tratamiento');
                    $translatePartialLoader.addPart('tipo');
                    $translatePartialLoader.addPart('estado');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('tratamiento-mis-tareas-detail', {
            parent: 'tratamiento-mis-tareas',
            url: '/tratamiento-mis-tareas/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'jhipsterApp.tratamiento.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/tratamiento/tratamiento-mis-tareas-detail.html',
                    controller: 'TratamientoMisTareasDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('tratamiento');
                    $translatePartialLoader.addPart('tipo');
                    $translatePartialLoader.addPart('estado');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Tratamiento', function($stateParams, Tratamiento) {
                    return Tratamiento.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'tratamiento-mis-tareas',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('tratamiento-mis-tareas-detail.edit', {
            parent: 'tratamiento-mis-tareas-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tratamiento/tratamiento-mis-tareas-dialog.html',
                    controller: 'TratamientoMisTareasDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Tratamiento', function(Tratamiento) {
                            return Tratamiento.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('tratamiento-mis-tareas.new', {
            parent: 'tratamiento-mis-tareas',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tratamiento/tratamiento-mis-tareas-dialog.html',
                    controller: 'TratamientoMisTareasDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                idTarea: null,
                                tipo: null,
                                fechaFichero: null,
                                pathFicheroXLS: null,
                                estado: null,
                                createdAt: null,
                                updatedAt: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('tratamiento-mis-tareas', null, { reload: 'tratamiento-mis-tareas' });
                }, function() {
                    $state.go('tratamiento-mis-tareas');
                });
            }]
        })
        .state('tratamiento-mis-tareas.edit', {
            parent: 'tratamiento-mis-tareas',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tratamiento/tratamiento-mis-tareas-dialog.html',
                    controller: 'TratamientoMisTareasDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Tratamiento', function(Tratamiento) {
                            return Tratamiento.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('tratamiento-mis-tareas', null, { reload: 'tratamiento-mis-tareas' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('tratamiento-mis-tareas.delete', {
            parent: 'tratamiento-mis-tareas',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tratamiento/tratamiento-mis-tareas-delete-dialog.html',
                    controller: 'TratamientoMisTareasDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Tratamiento', function(Tratamiento) {
                            return Tratamiento.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('tratamiento-mis-tareas', null, { reload: 'tratamiento-mis-tareas' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
