package com.github.service;

import com.github.domain.GenConfig;
import com.github.domain.vo.ColumnInfo;
import java.util.List;

/**
 * @author oldhand
 * @date 2019-12-16
*/
public interface GeneratorService {

    /**
     * 查询数据库元数据
     * @param name 表名
     * @param startEnd 分页参数
     * @return /
     */
    Object getTables(String name, int[] startEnd);

    /**
     * 得到数据表的元数据
     * @param name 表名
     * @return /
     */
    Object getColumns(String name);

    /**
     * 数据表
     * @param name 表名
     * @return /
     */
    boolean checkTableIsExist(String name);

    /**
     * 生成代码
     * @param columnInfos 表字段数据
     * @param genConfig 代码生成配置
     * @param tableName 表名
     */
    void generator(List<ColumnInfo> columnInfos, GenConfig genConfig, String tableName);

    /**
     * 验证表结构是否符合标准,并修正表结构
     * @param name 表名
     * @return /
     */
    void verify(String name);
}
