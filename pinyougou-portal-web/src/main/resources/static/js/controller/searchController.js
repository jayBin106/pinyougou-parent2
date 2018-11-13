//广告控制层（运营商后台）
app.controller("searchController", function ($scope, searchService) {
    //搜索
    $scope.search = function () {
        searchService.search($scope.searchMap).success(
            function (response) {
                $scope.resultMap = response;//搜索返回的结果
                $scope.resultMap = response;//搜索返回的结果
            }
        );
    }
    //增加到购物车
    $scope.addCart = function (cart) {
        searchService.addCart(cart).success(
            function (result) {
                alert(result)
            }
        )
    }
});
