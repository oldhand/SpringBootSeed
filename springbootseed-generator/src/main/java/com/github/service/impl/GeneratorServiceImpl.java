package com.github.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.github.domain.GenConfig;
import com.github.domain.vo.ColumnInfo;
import com.github.domain.vo.TableInfo;
import com.github.exception.BadRequestException;
import com.github.service.GeneratorService;
import com.github.utils.GenUtil;
import com.github.utils.PageUtil;
import com.github.utils.StringUtils;
import org.springframework.stereotype.Service;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author oldhand
 * @date 2019-12-16
*/
@Service
public class GeneratorServiceImpl implements GeneratorService {

    @PersistenceContext
    private EntityManager em;

    @Override
    @SuppressWarnings("all")
    public Object getTables(String name, int[] startEnd) {
        // 使用预编译防止sql注入
        String sql = "select table_name ,create_time , engine, table_collation, table_comment from information_schema.tables " +
                "where table_schema = (select database()) " +
                "and table_name like ? order by create_time desc";
        Query query = em.createNativeQuery(sql);
        query.setFirstResult(startEnd[0]);
        query.setMaxResults(startEnd[1]-startEnd[0]);
        query.setParameter(1, StringUtils.isNotBlank(name) ? ("%" + name + "%") : "%%");
        List result = query.getResultList();
        List<TableInfo> tableInfos = new ArrayList<>();
        for (Object obj : result) {
            Object[] arr = (Object[]) obj;
            tableInfos.add(new TableInfo(arr[0],arr[1],arr[2],arr[3], ObjectUtil.isNotEmpty(arr[4])? arr[4] : "-"));
        }
        Query query1 = em.createNativeQuery("SELECT COUNT(*) from information_schema.tables where table_schema = (select database())");
        Object totalElements = query1.getSingleResult();
        return PageUtil.toPage(tableInfos,totalElements);
    }

    @Override
    @SuppressWarnings("all")
    public Object getColumns(String name) {
        // 使用预编译防止sql注入
        String sql = "select column_name, is_nullable, data_type, column_comment, column_key, extra from information_schema.columns " +
                "where table_name = ? and table_schema = (select database()) order by ordinal_position";
        Query query = em.createNativeQuery(sql);
        query.setParameter(1, StringUtils.isNotBlank(name) ? name : null);
        List result = query.getResultList();
        List<ColumnInfo> columnInfos = new ArrayList<>();
        for (Object obj : result) {
            Object[] arr = (Object[]) obj;
			if (arr[0].toString().equals("id")) {
				columnInfos.add(new ColumnInfo(arr[0],arr[1],arr[2],arr[3],arr[4],arr[5],"2","true"));
			}
            else if (arr[4].toString().equals("UNI")) {
                columnInfos.add(new ColumnInfo(arr[0],arr[1],arr[2],arr[3],arr[4],arr[5],"2","true"));
            }
			else {
				columnInfos.add(new ColumnInfo(arr[0],arr[1],arr[2],arr[3],arr[4],arr[5],null,"true"));
			} 
        }
        return PageUtil.toPage(columnInfos,columnInfos.size());
    }

    @Override
    public void generator(List<ColumnInfo> columnInfos, GenConfig genConfig, String tableName) {
        if(genConfig.getId() == null){
            throw new BadRequestException("请先配置生成器");
        }
        try {
            GenUtil.generatorCode(columnInfos,genConfig,tableName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
