package com.xxxx.api.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author write by zjy
 * @since 2021-04-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("m_github")
public class Github implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String nodeId;

    private String login;

    private String avatarUrl;

    private String role;


}
