package com.tzp.ss.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "t_user")
public class User {
    private Long userId;
    private String userName;
    private String userStatus;
}
