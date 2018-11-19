app.service("searchService", function ($http) {
    this.search = function (searchMap) {
        return $http.post('../search/query', searchMap);
    }
    this.addCart = function (itemId, num) {
        return $http.get('../content/addCart?itemId=' + itemId + "&num=" + num);
    }
    this.findCartList = function () {
        return $http.post('../content/findCartList');
    }
    this.sum=function (cartList) {
        var totalValueObject={value:0,num:0};
        for (var obj in cartList) {
           var orderItem= cartList[obj].orderItemList;
            for (var obj1 in orderItem) {
                totalValueObject.value+= orderItem[obj1].totalFee;
                totalValueObject.num+= orderItem[obj1].num;
            }
        }
        return totalValueObject;
    }
});
