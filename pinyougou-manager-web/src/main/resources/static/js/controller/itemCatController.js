//控制层
app.controller('itemCatController', function ($scope, $controller, itemCatService) {

    $controller('baseController', {$scope: $scope});//继承

    //读取列表数据绑定到表单中  
    $scope.findAll = function () {
        itemCatService.findAll().success(
            function (response) {
                $scope.list = response;
            }
        );
    }

    //分页
    $scope.findPage = function (page, row) {
        itemCatService.findPage(page, row).success(
            function (response) {
                $scope.list = response.row;
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        );
    }

    //查询实体
    $scope.findOne = function (id) {
        itemCatService.findOne(id).success(
            function (response) {
                $scope.entity = response;
            }
        );
    }

    //保存
    $scope.save = function () {
        var serviceObject;//服务层对象
        if ($scope.entity.id != null) {//如果有ID
            serviceObject = itemCatService.update($scope.entity); //修改
        } else {
            serviceObject = itemCatService.add($scope.entity);//增加
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
        itemCatService.dele($scope.selectIds).success(
            function (response) {
                if (response.success) {
                    $scope.reloadList();//刷新列表
                    $scope.selectIds = [];
                }
            }
        );
    }

    $scope.searchEntity = {parentId: 0};//定义搜索对象

    //搜索
    $scope.search = function (page, row) {
        itemCatService.findByParentId($scope.searchEntity.parentId, page, row).success(
            function (result) {
                $scope.list = result.row;
                $scope.paginationConf.totalItems = result.total;//更新总记录数
            }
        )
    }

    //根据父id查询数据
    $scope.findByParentId = function (parentId, name, page, row) {
        // alert(parentId);
        // alert(name);
        if(parentId != 0){
            $("#breadcrumbOl").append("<li><a>" + name + "</a></li>");
        }else {
            // $("#breadcrumbOl").innerHTML=("<li><a>" + name + "</a></li>");
            // $("#breadcrumbOl").append("<li><a>" + name + "</a></li>");
        }
        $scope.searchEntity.parentId = parentId;
        itemCatService.findByParentId(parentId, page, row).success(
            function (result) {
                $scope.list = result.row;
                $scope.paginationConf.totalItems = result.total;//更新总记录数
            }
        )
    }

});	
