(function() {
    'use strict';
    angular
        .module('jhipsterApp')
        .factory('Insercion', Insercion);

    Insercion.$inject = ['$resource', 'DateUtils'];

    function Insercion ($resource, DateUtils) {
        var resourceUrl =  'api/insercions/:id';

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
