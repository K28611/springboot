package me.k28611.manage.service.Imp;

import me.k28611.manage.dao.HrMemberMapper;
import me.k28611.manage.model.po.HrMember;
import me.k28611.manage.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service("userService")
public class UserServiceImp implements UserService {
    @Autowired
    HrMemberMapper userMapper;
    @Override
    public List<HrMember> findAll() {
        return userMapper.selectAll();
    }

    @Override
    public List<HrMember> findUsersByAuthority() {
        return null;
    }

    @Override
    public void addUser(HrMember user) {

        userMapper.insert(user);
    }

    @Override
    public void delUser(HrMember user) {
        userMapper.deleteByPrimaryKey(userMapper.deleteByPrimaryKey(user.getWorkno()));
    }

    @Override
    public HrMember selectByPrimaryKey(Integer kId) {
        return userMapper.selectByPrimaryKey(kId);
    }

    @Override
    public HrMember findUsersByAccount(Integer account) { return userMapper.selectByAccount(account); }

    @Override
    public int updateByPrimaryKey(HrMember user) {
        try{
            userMapper.updateByPrimaryKey(user);
            return 0;
        }
       catch (Exception e){
            e.printStackTrace();
       }
        return -1;
    }


}
