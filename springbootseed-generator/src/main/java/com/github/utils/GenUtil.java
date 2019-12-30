package com.github.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.template.*;
import lombok.extern.slf4j.Slf4j;
import com.github.domain.GenConfig;
import com.github.domain.vo.ColumnInfo;
import org.springframework.util.ObjectUtils;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.time.LocalDate;
import java.util.*;

/**
 * 代码生成
 * @author oldhand
 * @date 2019-12-16
*/
@Slf4j
public class GenUtil {

    private static final String TIMESTAMP = "Timestamp";

    private static final String BIGDECIMAL = "BigDecimal";

    private static final String PK = "PRI";

    private static final String EXTRA = "auto_increment";

    private final static List<String> ignoreColumns = Arrays.asList("published","updated","author","deleted","createnew");

    /**
     * 获取后端代码模板名称
     * @return List
     */
    private static List<String> getAdminTemplateNames() {
        List<String> templateNames = new ArrayList<>();
        templateNames.add("Entity");
        templateNames.add("Dto");
        templateNames.add("Mapper");
        templateNames.add("Repository");
        templateNames.add("Service");
        templateNames.add("ServiceImpl");
        templateNames.add("QueryCriteria");
        templateNames.add("Controller");
        return templateNames;
    }

    /**
     * 生成代码
     * @param columnInfos 表元数据
     * @param genConfig 生成代码的参数配置，如包路径，作者
     */
    public static void generatorCode(List<ColumnInfo> columnInfos, GenConfig genConfig, String tableName) throws IOException {
        Map<String,Object> map = new HashMap<>();
        map.put("package",genConfig.getPackageName());
        map.put("moduleName",genConfig.getModuleName());
        map.put("author",genConfig.getAuthor());
        map.put("date", LocalDate.now().toString());
        map.put("tableName",tableName);
        String className = StringUtils.toCapitalizeCamelCase(tableName);
        String changeClassName = StringUtils.toCamelCase(tableName);

        // 判断是否去除表前缀
        if (StringUtils.isNotEmpty(genConfig.getPrefix())) {
            className = StringUtils.toCapitalizeCamelCase(StrUtil.removePrefix(tableName,genConfig.getPrefix()));
            changeClassName = StringUtils.toCamelCase(StrUtil.removePrefix(tableName,genConfig.getPrefix()));
        }
        map.put("className", className);
        map.put("upperCaseClassName", className.toUpperCase());
        map.put("changeClassName", changeClassName);
        map.put("hasTimestamp",false);
        map.put("queryHasTimestamp",false);
        map.put("queryHasBigDecimal",false);
        map.put("hasBigDecimal",false);
        map.put("hasQuery",false);
        map.put("auto",false);

        List<Map<String,Object>> columns = new ArrayList<>();
        List<Map<String,Object>> queryColumns = new ArrayList<>();
        for (ColumnInfo column : columnInfos) {

            if (ignoreColumns.indexOf(column.getColumnName()) >= 0) continue;
            Map<String,Object> listMap = new HashMap<>();
            listMap.put("columnComment",column.getColumnComment());
            listMap.put("columnKey",column.getColumnKey());

            String colType = ColUtil.cloToJava(column.getColumnType().toString());
            String changeColumnName = StringUtils.toCamelCase(column.getColumnName().toString());
            String capitalColumnName = StringUtils.toCapitalizeCamelCase(column.getColumnName().toString());
            if(PK.equals(column.getColumnKey())){
                map.put("pkColumnType",colType);
                map.put("pkChangeColName",changeColumnName);
                map.put("pkCapitalColName",capitalColumnName);
            }
            if(TIMESTAMP.equals(colType)){
                map.put("hasTimestamp",true);
            }
            if(BIGDECIMAL.equals(colType)){
                map.put("hasBigDecimal",true);
            }
            if(EXTRA.equals(column.getExtra())){
                map.put("auto",true);
            }
            listMap.put("columnType",colType);
            listMap.put("columnName",column.getColumnName());
            listMap.put("isNullable",column.getIsNullable());
            listMap.put("columnShow",column.getColumnShow());
            listMap.put("changeColumnName",changeColumnName);
            listMap.put("capitalColumnName",capitalColumnName);

            // 判断是否有查询，如有则把查询的字段set进columnQuery
            if(!StringUtils.isBlank(column.getColumnQuery())){
                listMap.put("columnQuery",column.getColumnQuery());
                map.put("hasQuery",true);
                if(TIMESTAMP.equals(colType)){
                    map.put("queryHasTimestamp",true);
                }
                if(BIGDECIMAL.equals(colType)){
                    map.put("queryHasBigDecimal",true);
                }
                queryColumns.add(listMap);
            }
            columns.add(listMap);
        }
        map.put("columns",columns);
        map.put("queryColumns",queryColumns);
        TemplateEngine engine = TemplateUtil.createEngine(new TemplateConfig("template", TemplateConfig.ResourceMode.CLASSPATH));

        // 生成后端代码
        List<String> templates = getAdminTemplateNames();
        for (String templateName : templates) {
            Template template = engine.getTemplate("generator/"+templateName+".ftl");
            String filePath = getAdminFilePath(templateName,genConfig,className);

            assert filePath != null;
            File file = new File(filePath);

            // 如果非覆盖生成
            if(!genConfig.getCover() && FileUtil.exist(file)){
                continue;
            }
            // 生成代码
            genFile(file, template, map);
        }
    }

    /**
     * 定义后端文件路径以及名称
     */
    private static String getAdminFilePath(String templateName, GenConfig genConfig, String className) {
        String projectPath = System.getProperty("user.dir") + File.separator + genConfig.getModuleName();
        String packagePath = projectPath + File.separator + "src" +File.separator+ "main" + File.separator + "java" + File.separator;
        if (!ObjectUtils.isEmpty(genConfig.getPackageName())) {
            packagePath += genConfig.getPackageName().replace(".", File.separator) + File.separator;
        }

        if ("Entity".equals(templateName)) {
            return packagePath + "domain" + File.separator + className + ".java";
        }

        if ("Controller".equals(templateName)) {
            return packagePath + "rest" + File.separator + className + "Controller.java";
        }

        if ("Service".equals(templateName)) {
            return packagePath + "service" + File.separator + className + "Service.java";
        }

        if ("ServiceImpl".equals(templateName)) {
            return packagePath + "service" + File.separator + "impl" + File.separator + className + "ServiceImpl.java";
        }

        if ("Dto".equals(templateName)) {
            return packagePath + "service" + File.separator + "dto" + File.separator + className + "DTO.java";
        }

        if ("QueryCriteria".equals(templateName)) {
            return packagePath + "service" + File.separator + "dto" + File.separator + className + "QueryCriteria.java";
        }

        if ("Mapper".equals(templateName)) {
            return packagePath + "service" + File.separator + "mapper" + File.separator + className + "Mapper.java";
        }

        if ("Repository".equals(templateName)) {
            return packagePath + "repository" + File.separator + className + "Repository.java";
        }

        return null;
    }


    private static void genFile(File file, Template template, Map<String, Object> map) throws IOException {
        // 生成目标文件
        Writer writer = null;
        try {
            FileUtil.touch(file);
            writer = new FileWriter(file);
            template.render(map, writer);
        } catch (TemplateException | IOException e) {
            throw new RuntimeException(e);
        } finally {
            assert writer != null;
            writer.close();
        }
    }
}
