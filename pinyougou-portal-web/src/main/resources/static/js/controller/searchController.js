//广告控制层（运营商后台）
app.controller("searchController", function ($scope, searchService) {
    //商品搜索
    $scope.search = function () {
        searchService.search($scope.searchMap).success(
            function (response) {
                $scope.resultMap = response;//搜索返回的结果
            }
        );
    }
    //增加到购物车
    $scope.addCart = function (itemId,num) {
        searchService.addCart(itemId,num).success(
            function (result) {
                if(result.success){
                    $scope.findCartList();
                }else {
                    alert(result.message);
                }
            }
        )
    }
    //购物车列表
    $scope.findCartList=function () {
        searchService.findCartList().success(
            function(result){
                $scope.cartObject = result;
                $scope.totalValue=searchService.sum(result);
            }
        )
    }
});
