package me.k28611.manage.dao;

import java.util.List;
import me.k28611.manage.model.po.HrMember;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface HrMemberMapper {
    int deleteByPrimaryKey(Integer workno);

    int insert(HrMember record);

    HrMember selectByPrimaryKey(Integer workno);

    List<HrMember> selectAll();

    int updateByPrimaryKey(HrMember record);

    HrMember selectByAccount(Integer account);

}