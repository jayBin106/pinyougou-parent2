package com.pinyougou.security.dao;

import com.pinyougou.security.pojo.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Set;

/**
 * 数据访问接口
 *
 * @author Administrator
 */
@Mapper
public interface RoleDao {
    @Select(value = "select r.* from role r left join member_role mr on r.rid=mr.rid where mr.mid=#{mid}")
    public Set<Role> getRole(String mid);
}
