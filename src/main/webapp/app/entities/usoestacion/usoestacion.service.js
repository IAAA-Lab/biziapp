(function() {
    'use strict';
    angular
        .module('jhipsterApp')
        .factory('Usoestacion', Usoestacion);

    Usoestacion.$inject = ['$resource', 'DateUtils'];

    function Usoestacion ($resource, DateUtils) {
        var resourceUrl =  'api/usoestacions/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.fechaDeUso = DateUtils.convertLocalDateFromServer(data.fechaDeUso);
                        data.fechaObtencionDatos = DateUtils.convertLocalDateFromServer(data.fechaObtencionDatos);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.fechaDeUso = DateUtils.convertLocalDateToServer(copy.fechaDeUso);
                    copy.fechaObtencionDatos = DateUtils.convertLocalDateToServer(copy.fechaObtencionDatos);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.fechaDeUso = DateUtils.convertLocalDateToServer(copy.fechaDeUso);
                    copy.fechaObtencionDatos = DateUtils.convertLocalDateToServer(copy.fechaObtencionDatos);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
