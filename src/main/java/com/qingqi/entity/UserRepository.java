package com.qingqi.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Administrator on 2017/10/13 0013.
 */
public interface UserRepository extends JpaRepository<User, Long> {
    //查询是否有某电话号码对应的账户
    @Transactional
    public List<User> findAllByTel(String telnumber);

    //查询是否有相应账户和密码
    @Transactional
    public List<User> findAllByTelAndPassword(String tel, String password);

    @Query(" UPDATE User u SET u.password=?2 WHERE u.tel=?1")
    @Transactional
    @Modifying
    public int updateUser(@Param("tel") String tel, @Param("password") String newpassword);


    //更具用户的账户和密码返回用户的主键id
    @Transactional
    @Modifying
    public User findByTelAndPassword(String tel, String password);
}
