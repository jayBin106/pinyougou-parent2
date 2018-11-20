//广告控制层（运营商后台）
app.controller("contentController", function ($scope, contentService) {
    $scope.contentList = [];//广告集合
    $scope.findByCategoryId = function (categoryId) {
        contentService.findByCategoryId(categoryId).success(
            function (response) {
                $scope.contentList[categoryId] = response;
            }
        );
    }
    //获取收货地址列表
    $scope.addressList = function () {
        contentService.addressList().success(
            function (result) {
                $scope.addressList = result;
            }
        )
    }

    //生成订单
    $scope.addOrder = function (order) {
        contentService.addOrder(order).success(
            function (result) {

            }
        )
    }
});
