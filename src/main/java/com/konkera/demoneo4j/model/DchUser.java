package com.konkera.demoneo4j.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author konkera
 * @date 2021/8/19
 */
@Data
@TableName("dch_user")
public class DchUser {
    private Long id;
    private String userName;
}
