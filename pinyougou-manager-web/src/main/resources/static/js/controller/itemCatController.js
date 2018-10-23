//控制层
app.controller('itemCatController', function ($scope, $controller, itemCatService, typeTemplateService) {

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

    //修改查询实体
    $scope.findItemCatById = function (id) {
        itemCatService.findItemCatById(id).success(
            function (response) {
                $scope.entity = {itemCat: {}};
                $scope.entity = response;
                $scope.entity.itemCat = JSON.parse(response.itemCat);
                var typeId = $scope.entity.itemCat.typeId;
                $scope.entity.itemCat.typeId = [];
                /*类型模版下拉获取*/
                var typeTemplate = $scope.TemplateList.data;
                for (var i = 0; i < typeTemplate.length; i++) {
                    var id = typeTemplate[i].id;
                    if (id == typeId) {
                        $scope.entity.itemCat.typeId[0] = typeTemplate[i];
                    }
                }
            }
        );
    }

    //保存
    $scope.save = function () {
        var serviceObject;//服务层对象
        var typeId = $scope.entity.itemCat.typeId[0];
        if (typeId.length != undefined) {
            $scope.entity.itemCat.typeId = typeId[0].id;
        } else {
            $scope.entity.itemCat.typeId = typeId.id;
        }
        if ($scope.entity.itemCat.id != null) {//如果有ID
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
        if (parentId != 0) {
            $("#breadcrumbOl").append("<li><a>" + name + "</a></li>");
        } else {
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
    //商品分类下拉
    $scope.selectItemCat = function () {
        itemCatService.findByParentId(0, 1, 100).success(
            function (result) {
                $scope.itemCat1List1 = result.row;
            }
        )
    }
    //二级菜单
    //$watch方法用于监控某个变量的值，当被监控的值发生变化，就自动执行相应的函数。
    $scope.$watch("entity.id", function (newValue, oldValue) {
        itemCatService.findByParentId(newValue, 1, 100).success(
            function (result) {
                $scope.itemCat1List2 = result.row;
            }
        )
    })
    //模版下拉列表
    // $scope.brandList={data:[{id:1,text:'联想'},{id:2,text:'华为'},{id:3,text:'小米'}]};//品牌列表
    $scope.itemCatList = [];
    $scope.typeTemplateList = function () {
        typeTemplateService.selectOptionList().success(
            function (result) {
                $scope.TemplateList = {data: result};
                /*类型模版下拉获取*/
                for (var i = 0; i < result.length; i++) {
                    $scope.itemCatList[result[i].id] = result[i].text;
                }
            }
        )
    }

});	
