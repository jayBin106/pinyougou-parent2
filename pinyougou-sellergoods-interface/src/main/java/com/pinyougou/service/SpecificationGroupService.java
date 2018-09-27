package com.pinyougou.service;

import com.pinyougou.entity.Result;
import com.pinyougou.pojoGroup.Specification;

/**
 * Created by lenovo on 2018/9/27.
 */
public interface SpecificationGroupService {
    /**
     * 新增规格
     *
     * @param specification
     */
    public void add(Specification specification);

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    public Specification selectOne(long id);

    /**
     * 更新规格详情
     *
     * @param specification
     * @return
     */
    public Result updateGroup(Specification specification);
}
