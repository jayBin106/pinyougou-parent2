app.service("searchService", function ($http) {
    this.search = function (searchMap) {
        return $http.post('solr/search', searchMap);
    }

});
