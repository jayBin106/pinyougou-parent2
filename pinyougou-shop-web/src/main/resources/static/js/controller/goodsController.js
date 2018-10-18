//控制层
app.controller('goodsController', function ($scope, $controller, goodsService, uploadFileService) {

    $controller('baseController', {$scope: $scope});//继承

    //读取列表数据绑定到表单中  
    $scope.findAll = function () {
        goodsService.findAll().success(
            function (response) {
                $scope.list = response;
            }
        );
    }

    //分页
    $scope.findPage = function (page, row) {
        goodsService.findPage(page, row).success(
            function (response) {
                $scope.list = response.row;
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        );
    }
    //分页
    $scope.findPage2 = function (page, row) {
        goodsService.findPage2(page, row).success(
            function (response) {
                $scope.list = response.row;
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        );
    }

    //查询实体
    $scope.findOne = function (id) {
        goodsService.findOne(id).success(
            function (response) {
                $scope.entity = response;
            }
        );
    }

    //保存
    $scope.save = function () {
        var serviceObject;//服务层对象
        if ($scope.entity.id != null) {//如果有ID
            serviceObject = goodsService.update($scope.entity); //修改
        } else {
            serviceObject = goodsService.add($scope.entity);//增加
        }
        serviceObject.success(
            function (response) {
                if (response.success) {
                    //重新查询
                    $scope.reloadList();//重新加载
                } else {
                    alert(response.message);
                }
            }
        );
    }


    //批量删除
    $scope.dele = function () {
        //获取选中的复选框
        goodsService.dele($scope.selectIds).success(
            function (response) {
                if (response.success) {
                    $scope.reloadList();//刷新列表
                    $scope.selectIds = [];
                }
            }
        );
    }

    $scope.searchEntity = {};//定义搜索对象

    //搜索
    $scope.search = function (page, row) {
        goodsService.search(page, row, $scope.searchEntity).success(
            function (response) {
                $scope.list = response.row;
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        );
    }

    //保存
    $scope.addGoods = function () {
        goodsService.addGoods($scope.entity).success(
            function (response) {
                if (response.success) {
                    alert('保存成功');
                    $scope.entity = {};
                } else {
                    alert(response.message);
                }
            })
    }
    //fastDFS文件上传
    $scope.uploadFile = function () {
        uploadFileService.uploadFile().success(function (result) {
            if (result.success) {
                $scope.image_entity.url = result.message;//设置文件地址
            } else {
                alert(result.message);
            }
        }).error(
            function () {
                alert("上传发生错误");
            }
        )
    }

});
