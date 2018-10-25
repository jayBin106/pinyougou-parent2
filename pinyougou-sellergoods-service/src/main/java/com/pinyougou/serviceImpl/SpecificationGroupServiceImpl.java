package com.pinyougou.serviceImpl;

import com.pinyougou.dao.TbSpecificationMapper;
import com.pinyougou.dao.TbSpecificationOptionMapper;
import com.pinyougou.entity.Result;
import com.pinyougou.pojo.TbSpecification;
import com.pinyougou.pojo.TbSpecificationOption;
import com.pinyougou.pojo.TbSpecificationOptionExample;
import com.pinyougou.pojoGroup.Specification;
import com.pinyougou.service.SpecificationGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;

import java.util.List;

/**
 * Created by lenovo on 2018/9/27.
 */
@Service
public class SpecificationGroupServiceImpl implements SpecificationGroupService {
    @Autowired
    TbSpecificationMapper tbSpecificationMapper;
    @Autowired
    TbSpecificationOptionMapper tbSpecificationOptionMapper;

    /**
     * 新增规格
     *
     * @param specification
     */
    @Override
    public void add(Specification specification) {
        //新增规格
        tbSpecificationMapper.insert(specification.getTbSpecification());

        //新增规格详情
        List<TbSpecificationOption> tbSpecificationOptionList = specification.getTbSpecificationOptionList();
        for (TbSpecificationOption tbSpecificationOption : tbSpecificationOptionList) {
            //设置规格的id
            tbSpecificationOption.setSpecId(specification.getTbSpecification().getId());
            tbSpecificationOptionMapper.insert(tbSpecificationOption);
        }
    }

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    @Override
    public Specification selectOne(long id) {
        Specification specification = new Specification();
        TbSpecification tbSpecification = tbSpecificationMapper.selectByPrimaryKey(id);
        if (tbSpecification != null) {
            TbSpecificationOptionExample tbSpecificationOptionExample = new TbSpecificationOptionExample();
            TbSpecificationOptionExample.Criteria criteria = tbSpecificationOptionExample.createCriteria();
            criteria.andSpecIdEqualTo(tbSpecification.getId());
            List<TbSpecificationOption> tbSpecificationOptions = tbSpecificationOptionMapper.selectByExample(tbSpecificationOptionExample);
            if (tbSpecificationOptions != null && tbSpecificationOptions.size() > 0) {
                specification.setTbSpecificationOptionList(tbSpecificationOptions);
            }
            specification.setTbSpecification(tbSpecification);
        }
        return specification;
    }

    /**
     * 更新规格详情
     *
     * @param specification
     * @return
     */
    @Override
    public Result updateGroup(Specification specification) {
        try {
            tbSpecificationMapper.updateByPrimaryKey(specification.getTbSpecification());

            TbSpecificationOptionExample tbSpecificationOptionExample = new TbSpecificationOptionExample();
            TbSpecificationOptionExample.Criteria criteria = tbSpecificationOptionExample.createCriteria();
            criteria.andSpecIdEqualTo(specification.getTbSpecification().getId());
            tbSpecificationOptionMapper.deleteByExample(tbSpecificationOptionExample);

            //新增规格详情
            List<TbSpecificationOption> tbSpecificationOptionList = specification.getTbSpecificationOptionList();
            for (TbSpecificationOption tbSpecificationOption : tbSpecificationOptionList) {
                //设置规格的id
                tbSpecificationOption.setSpecId(specification.getTbSpecification().getId());
                tbSpecificationOptionMapper.insert(tbSpecificationOption);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return new Result(false, "更新失败");
        }
        return new Result();
    }
}
