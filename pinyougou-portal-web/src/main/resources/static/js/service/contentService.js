app.service("contentService", function ($http) {
    //根据分类ID查询广告列表
    this.findByCategoryId = function (categoryId) {
        return $http.get("../content/findByCategoryId?categoryId=" + categoryId);
    }
    this.addOrder = function (order) {
        return $http.post("../order/addOrder", order);
    }
    //收货地址列表
    this.addressList = function () {
        return $http.get("../content/getAdressList");
    }
});
