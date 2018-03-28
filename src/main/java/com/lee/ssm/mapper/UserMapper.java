package com.lee.ssm.mapper;

import com.lee.ssm.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author: yz.li
 * @date: 2018/3/27
 */
@Mapper
@Repository
public interface UserMapper {

    @Insert("INSERT INTO user(name, age) VALUES(#{name}, #{age})")
    int insert(User user);
}
