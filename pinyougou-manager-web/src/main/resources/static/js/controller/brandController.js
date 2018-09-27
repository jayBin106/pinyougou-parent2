//控制层
app.controller('brandController', function ($scope, $controller, brandService) {
    $controller('baseController', {$scope: $scope});//继承
    //读取列表数据绑定到表单中
    $scope.fallAll = function () {
        brandService.fallAll().success(
            function (result) {
                $scope.list = result;
            }
        )
    }

    //分页查询(停用)
    $scope.findPage = function (page, size) {
        brandService.findPage(page, size).success(
            function (result) {
                //显示当前页数据
                $scope.list = result.row;
                //更新总记录数
                $scope.paginationConf.totalItems = result.total;
            }
        )
    };

    //搜索分页;
    $scope.searchEntity = {}
    $scope.search = function (page, size) {
        brandService.search(page, size, $scope.searchEntity).success(
            function (result) {
                //显示当前页数据
                $scope.list = result.row;
                //更新总记录数
                $scope.paginationConf.totalItems = result.total;
            }
        )
    }


    //新增
    $scope.save = function () {
        var serviceObject;
        if ($scope.entity.id != null) {
            serviceObject = brandService.update($scope.entity);
        } else {
            serviceObject = brandService.add($scope.entity);
        }
        serviceObject.success(function (result) {
            if (result.success) {
                //重新加载
                $scope.reloadList();
            } else {
                alert(result.code);
            }
        })
    }

    $scope.delete = function () {
        brandService.delete($scope.selectIds).success(
            function (result) {
                alert(result.success);
                if (result.success) {
                    $scope.reloadList();
                }
            }
        )
    }

    //实体查询
    $scope.selectOne = function (id) {
        brandService.selectOne(id).success(
            function (result) {
                $scope.entity = result;
            }
        )
    }
})