//服务层
app.service('goodsService', function ($http) {

    //读取列表数据绑定到表单中
    this.findAll = function () {
        return $http.get('../shopGoods/findAll');
    }
    //分页
    this.findPage = function (page, rows) {
        return $http.get('../shopGoods/findPage?page=' + page + '&rows=' + rows);
    }
    //分页
    this.findPage2 = function (page, rows) {
        return $http.get('../shopGoods/findPage2?page=' + page + '&rows=' + rows);
    }
    //查询实体
    this.findOne = function (id) {
        return $http.get('../shopGoods/findOne?id=' + id);
    }
    //增加
    this.add = function (entity) {
        return $http.post('../shopGoods/add', entity);
    }
    //增加
    this.saveGoods = function (entity) {
        return $http.post('../shopGoods/saveGoods', entity);
    }
    //修改
    this.update = function (entity) {
        return $http.post('../shopGoods/update', entity);
    }
    //删除
    this.dele = function (ids) {
        return $http.get('../shopGoods/delete?ids=' + ids);
    }
    //搜索
    this.search = function (page, rows, searchEntity) {
        return $http.post('../shopGoods/search?page=' + page + "&rows=" + rows, searchEntity);
    }
    //状态更新
    this.updateStatus = function (sign, status, ids) {
        return $http.get('../shopGoods/UpDownGoods?ids=' + ids + "&status=" + status + "&sign=" + sign);
    }
//删除
    this.findGoods = function (id) {
        return $http.get('../shopGoods/findGoods?id=' + id);
    }

});
