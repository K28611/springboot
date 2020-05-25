package me.k28611.manage.service;

import me.k28611.manage.model.po.HrMember;

import java.util.List;

public interface UserService {
    //查找所有用户
    public List<HrMember> findAll();
    //通过用户名查找用户
    //查找某个用户组下的所有用户
    public List<HrMember> findUsersByAuthority();
    //添加用户
    public void addUser(HrMember user);
    //删除用户
    public void delUser(HrMember user);

    public HrMember selectByPrimaryKey(Integer kId);

    public HrMember findUsersByAccount(Integer account);

    public int updateByPrimaryKey(HrMember user);
}
