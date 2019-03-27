package com.pinyougou.security.dao;

import com.pinyougou.security.pojo.Action;
import org.apache.ibatis.annotations.*;
import java.util.Set;

/**
 * 数据访问接口
 *
 * @author Administrator
 */
@Mapper
public interface ActionDao {
    @Select(value = "select a.*\n" +
            "from action a left join role_action ra on ra.actid = a.actid\n" +
            "left join member_role mr on mr.rid=ra.rid\n" +
            "where mr.mid=#{mid}")
    public Set<Action> getAction(String mid);

    /**
     * @param rid   角色id
     * @param actid 权限id
     */
    @Insert(value = "insert into role_action (rid, actid) VALUES (#{rid},#{actid})")
    public void addAction(@Param("rid") String rid, @Param("actid") String actid);

    @Delete(value = "delete from role_action where rid=#{rid} and actid=#{actid}")
    public void deleteAction(@Param("rid") String rid, @Param("actid") String actid);
}
