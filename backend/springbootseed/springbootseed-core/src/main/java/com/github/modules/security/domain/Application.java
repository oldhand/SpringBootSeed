package com.github.modules.security.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Set;

/**
 * @author oldhand
 * @date 2019-12-16 *
 */

@Entity
@Getter
@Setter
@Table(name="application")
public class Application implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(groups = Update.class)
    private Long id;

    @NotBlank
    @Column(unique = true)
    private String appid;

    @NotBlank
    private String secret;

    private String description;

    @Column(name = "published")
    @CreationTimestamp
    private Timestamp published;

    public @interface Update {}

}