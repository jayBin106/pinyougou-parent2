//控制层
app.controller('goodsController', function ($scope, $controller, goodsService, uploadFileService, itemCatService, brandService, typeTemplateService) {

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
        $scope.entity.tbGoodsDesc.itemImages = JSON.parse($scope.entity.tbGoodsDesc.itemImages);
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
    //定义页面实体结构
    $scope.entity = {tbGoods: {}, tbGoodsDesc: {itemImages: [], specificationItems: []}};
    //上传图片保存
    $scope.addImage_entity = function () {
        $scope.entity.tbGoodsDesc.itemImages.push($scope.image_entity);
    }
    //移除图片
    $scope.removeImage = function (index) {
        $scope.entity.tbGoodsDesc.itemImages.splice(index, 1);
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
    $scope.$watch("entity.tbGoods.category1Id", function (newValue, oldValue) {
        itemCatService.findByParentId(newValue, 1, 100).success(
            function (result) {
                $scope.itemCat1List2 = result.row;
            }
        )
    })
    $scope.$watch("entity.tbGoods.category2Id", function (newValue, oldValue) {
        itemCatService.findByParentId(newValue, 1, 100).success(
            function (result) {
                $scope.itemCat1List3 = result.row;
            }
        )
    })
    $scope.$watch("entity.tbGoods.category3Id", function (newValue) {
        itemCatService.findOne(newValue).success(
            function (result) {
                $scope.entity.tbGoods.typeTemplateId = result.typeId;
            }
        )
    })


    //品牌分类下拉
    $scope.$watch("entity.tbGoods.typeTemplateId", function (newValue, oldValue) {
        typeTemplateService.findOne(newValue).success(
            function (result) {
                $scope.typeTemplate = result;// 模板对象

                $scope.typeTemplate.brandIds = JSON.parse($scope.typeTemplate.brandIds);//品牌列表类型转换
                $scope.entity.tbGoodsDesc.customAttributeItems = JSON.parse($scope.typeTemplate.customAttributeItems);//扩展属性
            }
        )
        //规格分类下拉
        typeTemplateService.findSpecList(newValue).success(
            function (result) {
                $scope.specList = result;// 模板对象
            }
        )
    })

    //规格选中
    $scope.updateSpecAttribute = function ($event, name, value) {
        var object = $scope.searchObjectByKey($scope.entity.tbGoodsDesc.specificationItems, 'attributeName', name);
        if (object != null) {
            if ($event.target.checked) {
                object.attributeValue.push(value);
            } else {
                //取消勾选
                object.attributeValue.splice(object.attributeValue.indexOf(value), 1);//移除选项
                //如果选项都取消了，将此条记录移除
                if (object.attributeValue.length == 0) {
                    $scope.entity.tbGoodsDesc.specificationItems.splice($scope.entity.tbGoodsDesc.specificationItems.indexOf(object), 1);
                }
            }
        } else {
            $scope.entity.tbGoodsDesc.specificationItems.push(
                {"attributeName": name, "attributeValue": [value]});
        }
    }

    //创建SKU列表
    $scope.createItemList = function () {
        $scope.entity.itemList = [{spec: {}, price: 0, num: 99999, status: '0', isDefault: '0'}];//初始
        var items = $scope.entity.tbGoodsDesc.specificationItems;
        for (var i = 0; i < items.length; i++) {
            $scope.entity.itemList = addColumn($scope.entity.itemList, items[i].attributeName, items[i].attributeValue);
        }
    }
    //添加列值
    addColumn = function (list, columnName, conlumnValues) {
        var newList = [];//新的集合
        for (var i = 0; i < list.length; i++) {
            var oldRow = list[i];
            for (var j = 0; j < conlumnValues.length; j++) {
                var newRow = JSON.parse(JSON.stringify(oldRow));//深克隆
                newRow.spec[columnName] = conlumnValues[j];
                newList.push(newRow);
            }
        }
        return newList;
    }


});
