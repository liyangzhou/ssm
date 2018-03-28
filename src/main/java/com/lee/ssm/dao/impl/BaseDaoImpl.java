package com.lee.ssm.dao.impl;

import com.lee.ssm.dao.BaseDao;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @author: yz.li
 * @date: 2018/3/27
 */
@Repository
public class BaseDaoImpl implements BaseDao{

    @Autowired
    private SqlSession sqlSession;


}