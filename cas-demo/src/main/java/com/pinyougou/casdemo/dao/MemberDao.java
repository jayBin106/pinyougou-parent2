package com.pinyougou.casdemo.dao;

import com.pinyougou.casdemo.pojo.Member;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * 数据访问接口
 *
 * @author Administrator
 */
@Mapper
public interface MemberDao {
    @Select(value = "select * from member where mid = #{mid}")
    Member selectByPrimaryKey(String mid);

    @Update(value = "update member set locked=#{locked} where mid= #{mid}")
    Integer updateByPrimaryKey(@Param("locked") String locked, @Param("mid") String mid);
}
