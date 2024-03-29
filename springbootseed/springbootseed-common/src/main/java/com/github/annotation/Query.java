package com.github.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author oldhand
 * @date 2019-12-16
*/
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Query {

    //  2017/8/7 基本对象的属性名
    String propName() default "";
    //  2017/8/7 查询方式
    Type type() default Type.EQUAL;

    /**
     * 连接查询的属性名，如User类中的dept
     */
    String joinName() default "";

    /**
     * 默认左连接
     */
    Join join() default Join.LEFT;

    /**
     * 多字段模糊搜索，仅支持String类型字段，多个用逗号隔开, 如@Query(blurry = "email,username")
     */
    String blurry() default "";

    enum Type {
        // jie 2019/6/4 相等
        EQUAL
        //  2017/8/7 大于等于
        , GREATER_THAN
        //  2017/8/7 小于等于
        , LESS_THAN
        //  2017/8/7 中模糊查询
        , INNER_LIKE
        //  2017/8/7 左模糊查询
        , LEFT_LIKE
        //  2017/8/7 右模糊查询
        , RIGHT_LIKE
        //  2017/8/7 小于
        , LESS_THAN_NQ
        // jie 2019/6/4 包含
        , IN
    }

    /**
     * @author oldhand
     * 适用于简单连接查询，复杂的请自定义该注解，或者使用sql查询
     */
    enum Join {
        /** 左连接 */
        LEFT
        /** 右连接 */
        , RIGHT
    }

}

