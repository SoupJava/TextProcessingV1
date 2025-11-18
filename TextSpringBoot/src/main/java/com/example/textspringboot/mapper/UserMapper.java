package com.example.textspringboot.mapper;

import com.example.textspringboot.Entity.user;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserMapper {
    public user loginUser(String id);
    public int UserRegister(user u);
    public int UpdateUser(user u);
}
