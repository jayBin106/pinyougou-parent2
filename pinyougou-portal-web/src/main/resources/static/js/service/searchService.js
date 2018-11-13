app.service("searchService", function ($http) {
    this.search = function (searchMap) {
        return $http.post('../search/query', searchMap);
    }
    this.addCart = function (cart) {
        return $http.post('../search/addCart', cart);
    }

});
