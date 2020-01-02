package com.github.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.lang.reflect.Field;

/**
 * @author oldhand
 * @Date 2019-12-16
 */
@Getter
@Setter
@MappedSuperclass
public class BaseEntity implements Serializable {

    // ID
    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable=false,nullable = false)
    @ApiModelProperty("ID")
    protected Long id;

    // 创建日期
    @Column(name = "published",insertable=false, updatable=false,nullable = false)
    @ApiModelProperty("创建日期")
    @JsonIgnore
    @CreationTimestamp
    protected Timestamp published;

    // 更新日期
    @Column(name = "updated",insertable=false, updatable=false,nullable = false)
    @ApiModelProperty("更新日期")
    @JsonIgnore
    @UpdateTimestamp
    protected Timestamp updated;

    // 创建者
    @Column(name = "author",updatable=false,nullable = false)
    @ApiModelProperty("创建者")
    @JsonIgnore
    protected String author;

    // 删除标记
    @Column(name = "deleted",nullable = false)
    @ApiModelProperty("删除标记")
    protected Integer deleted;

    public @interface Update {}

    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this);
        Field[] fields = this.getClass().getDeclaredFields();
        try {
            for (Field f : fields) {
                f.setAccessible(true);
                builder.append(f.getName(), f.get(this)).append("\n");
            }
        } catch (Exception e) {
            builder.append("toString builder encounter an error");
        }
        return builder.toString();
    }
}
