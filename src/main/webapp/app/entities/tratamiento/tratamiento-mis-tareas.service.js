(function() {
    'use strict';
    angular
        .module('jhipsterApp')
        .factory('Tratamiento', Tratamiento);

    Tratamiento.$inject = ['$resource', 'DateUtils'];

    function Tratamiento ($resource, DateUtils) {
        var resourceUrl =  'api/tratamientos/:id';

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
