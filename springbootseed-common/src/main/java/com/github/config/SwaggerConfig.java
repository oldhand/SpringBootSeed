package com.github.config;

import com.fasterxml.classmate.TypeResolver;
import com.google.common.base.Predicates;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.data.domain.Pageable;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.schema.AlternateTypeRule;
import springfox.documentation.schema.AlternateTypeRuleConvention;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import static com.google.common.collect.Lists.newArrayList;
import static springfox.documentation.schema.AlternateTypeRules.newRule;

/**
 * api页面 /swagger-ui.html
 * @author oldhand
 * @date 2019-12-16
*/

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Value("${jwt.header}")
    private String tokenHeader;

    @Value("${swagger.enabled}")
    private Boolean enabled;

    @Bean(value = "defaultApi")
    public Docket defaultApi() {
        ParameterBuilder ticketPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<>();
        ticketPar.name(tokenHeader).description("token")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .defaultValue("anonymous")
                .description("令牌")
                .required(true)
                .build();
        pars.add(ticketPar.build());
        return new Docket(DocumentationType.SWAGGER_2)
                .enable(enabled)
                .directModelSubstitute(Timestamp.class, String.class)
                .directModelSubstitute(Date.class, String.class)
                .apiInfo(apiInfo())
                .select()
                .paths(Predicates.and(Predicates.not(PathSelectors.regex("/error.*")),Predicates.not(PathSelectors.regex("/auth/login"))))
                .build()
                .groupName("Authentication required")
                .globalOperationParameters(pars);
    }

    @Bean(value = "publicApi")
    public Docket publicApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .enable(enabled)
                .directModelSubstitute(Timestamp.class, String.class)
                .directModelSubstitute(Date.class, String.class)
                .apiInfo(apiInfo())
                .select()
                .paths(PathSelectors.regex("/auth/login"))
                .build()
                .groupName("No Authentication required");
    }


    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("SpringBootSeed 接口文档 by 网数科技")
                .version("1.0")
                .description("SpringBoot的种子框架项目，一个拿来即用的快速框架。")
                .contact("网数科技 (手机: 15111122026)")
                .build();
    }

}

/**
 *  将Pageable转换展示在swagger中
 */
@Configuration
class SwaggerDataConfig {

    @Bean
    public AlternateTypeRuleConvention pageableConvention(final TypeResolver resolver) {
        return new AlternateTypeRuleConvention() {
            @Override
            public int getOrder() {
                return Ordered.HIGHEST_PRECEDENCE;
            }

            @Override
            public List<AlternateTypeRule> rules() {
                return newArrayList(newRule(resolver.resolve(Pageable.class), resolver.resolve(Page.class)));
            }
        };
    }

    @ApiModel
    @Data
    private static class Page {
        @ApiModelProperty("页码 (0..N)")
        private Integer page;

        @ApiModelProperty("每页显示的数目")
        private Integer size;

        @ApiModelProperty("以下列格式排序标准：property[,asc | desc]。 默认排序顺序为升序。 支持多种排序条件：如：id,asc")
        private List<String> sort;
    }
}
