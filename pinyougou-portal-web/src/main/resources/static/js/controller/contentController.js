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
    $scope.addOrder = function () {
        $scope.order.receiverAreaName = $scope.address.address;//地址
        $scope.order.receiverMobile = $scope.address.mobile;//手机
        $scope.order.receiver = $scope.address.contact;//联系人
        contentService.addOrder($scope.order).success(
            function (result) {

            }
        )
    }
    //选择地址
    $scope.selectAddress = function (address) {
        $scope.address = address;
    }

    //判断是否是当前选中的地址
    $scope.isSelectedAddress = function (address) {
        if (address == $scope.address) {
            return true;
        } else {
            return false;
        }
    }

    $scope.order = {paymentType: '1'};
    //选择支付方式
    $scope.selectPayType = function (type) {
        $scope.order.paymentType = type;
    }
});
