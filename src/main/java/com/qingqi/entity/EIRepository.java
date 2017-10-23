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
public interface EIRepository extends JpaRepository<EI,Long>{

    //用于辅助用户查询，返回某用户的所有EI表中的快递消息
    @Query("select p from EI p where p.userid=:userid")
    @Transactional
    @Modifying
    List<EI> withUseridQuery(@Param("userid") Long userid);

    //用于辅助后台查询，返回所有EI表中处于某种状态下的消息
    @Transactional
    @Modifying
    List<EI> findAllByStateAndSmsaddress(Long state, Long smsaddress);

    //用户辅助用户查询自己的某条信息
    @Transactional
    @Modifying
    List<EI> findAllByIdAndUserid(Long id, Long userid);

    //删除EI表中的某条数据
    @Transactional
    @Query("delete from EI where id=?1 and userid=?2")
    @Modifying
    void deleteOrderById(Long id, Long userid);

    //返回某一状态下的所有EI记录
    @Transactional
    @Modifying
    List<EI> findAllByState(Long state);

    //返回某一主键值对应的记录
    @Transactional
    EI findById(Long id);

    @Transactional
    @Modifying
    @Query(value = "update EI set state=?2 where id=?1 ")
    Integer updateStateById(Long id, Long state);

}
