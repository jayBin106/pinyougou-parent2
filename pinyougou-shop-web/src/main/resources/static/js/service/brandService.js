//服务层
app.service("brandService", function ($http) {
    this.fallAll = function () {
        return $http.get("../brand/findAll");
    }
    this.findPage = function (page, size) {
        return $http.get("../brand/findPage?page=" + page + "&size=" + size);
    }
    this.search = function (page, size, searchEntity) {
        return $http.post("../brand/search?page=" + page + "&size=" + size, searchEntity);
    }
    this.delete = function (Ids) {
        return $http.get("../brand/delete?ids=" + Ids);
    }
    this.selectOne = function (id) {
        return $http.get("../brand/selectOne?id=" + id);
    }
    this.add = function (entity) {
        return $http.post("../brand/add", entity);
    }
    this.update = function (entity) {
        return $http.post("../brand/update", entity);
    }
    this.selectOptionList = function () {
        return $http.get("../brand/selectOptionList");
    }
});