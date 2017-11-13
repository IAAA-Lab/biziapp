(function() {
    'use strict';
    angular
        .module('jhipsterApp')
        .factory('Descarga', Descarga);

    Descarga.$inject = ['$resource', 'DateUtils'];

    function Descarga ($resource, DateUtils) {
        var resourceUrl =  'api/descargas/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.createdAt = DateUtils.convertDateTimeFromServer(data.createdAt);
                        data.updatedAt = DateUtils.convertDateTimeFromServer(data.updatedAt);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
