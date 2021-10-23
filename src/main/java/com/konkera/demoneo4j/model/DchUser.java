package com.konkera.demoneo4j.model;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author konkera
 * @date 2021/8/19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("dch_user")
public class DchUser extends Model<DchUser> {
    @TableId(type = IdType.AUTO)
    private Long id;
    @TableField(exist = true, condition = SqlCondition.EQUAL)
    private String userName;
}
