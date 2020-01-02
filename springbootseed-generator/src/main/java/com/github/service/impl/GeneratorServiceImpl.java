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
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public boolean checkTableIsExist(String name) {
        String sql = "select COUNT(*) from information_schema.columns where table_name = ? and table_schema = (select database())";
        Query query = em.createNativeQuery(sql);
        query.setParameter(1, StringUtils.isNotBlank(name) ? name : null);
        List result = query.getResultList();
        if (Integer.parseInt(result.get(0).toString()) > 0) {
            return true;
        }
        return false;
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

    /**
     * 验证表结构是否符合标准,并修正表结构
     */
    @Override
    @Transactional
    @Modifying
    public void verify(String tableName) throws Exception {
        try {
            ColumnInfo columnIdinfo = getColumnInfoByName(tableName,"id");
            if (columnIdinfo == null) {
                String sql = "ALTER TABLE base_users ADD COLUMN id bigint(20) NOT NULL COMMENT 'ID' FIRST";
                Query query = em.createNativeQuery(sql);
                query.executeUpdate();
                query = em.createNativeQuery("ALTER TABLE base_users ADD PRIMARY KEY (id)");
                query.executeUpdate();
            }
            else {
                if (!columnIdinfo.getIsNullable().equals("NO") || !columnIdinfo.getColumnType().equals("bigint") || !columnIdinfo.getColumnComment().equals("ID") || !columnIdinfo.getColumnKey().equals("PRI") || !columnIdinfo.getExtra().equals("")) {
                    String sql = "ALTER TABLE base_users MODIFY COLUMN id bigint(20) NOT NULL COMMENT 'ID' FIRST";
                    Query query = em.createNativeQuery(sql);
                    query.executeUpdate();
                }
            }
            ColumnInfo columnPublishedinfo = getColumnInfoByName(tableName,"published");
            if (columnPublishedinfo == null) {
                String sql = "ALTER TABLE base_users ADD COLUMN published timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建日期' AFTER id";
                Query query = em.createNativeQuery(sql);
                query.executeUpdate();
            }
            else {
                if (!columnPublishedinfo.getIsNullable().equals("NO") || !columnPublishedinfo.getColumnType().equals("timestamp") || !columnPublishedinfo.getColumnComment().equals("创建日期") || !columnPublishedinfo.getColumnKey().equals("") || !columnPublishedinfo.getExtra().equals("")) {
                    String sql = "ALTER TABLE base_users MODIFY COLUMN published timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建日期' AFTER id";
                    Query query = em.createNativeQuery(sql);
                    query.executeUpdate();
                }
            }
            ColumnInfo columnUpdatedinfo = getColumnInfoByName(tableName,"updated");
            if (columnUpdatedinfo == null) {
                String sql = "ALTER TABLE base_users ADD COLUMN updated timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '更新日期' AFTER published";
                Query query = em.createNativeQuery(sql);
                query.executeUpdate();
            }
            else {
                if (!columnUpdatedinfo.getIsNullable().equals("NO") || !columnUpdatedinfo.getColumnType().equals("timestamp") || !columnUpdatedinfo.getColumnComment().equals("更新日期") || !columnUpdatedinfo.getColumnKey().equals("") || !columnUpdatedinfo.getExtra().equals("")) {
                    String sql = "ALTER TABLE base_users MODIFY COLUMN updated timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '更新日期' AFTER published";
                    Query query = em.createNativeQuery(sql);
                    query.executeUpdate();
                }
            }
            ColumnInfo columnAuthorinfo = getColumnInfoByName(tableName,"author");
            if (columnAuthorinfo == null) {
                String sql = "ALTER TABLE base_users ADD COLUMN author varchar(32) NOT NULL COMMENT '创建者' AFTER updated";
                Query query = em.createNativeQuery(sql);
                query.executeUpdate();
            }
            else {
                if (!columnAuthorinfo.getIsNullable().equals("NO") || !columnAuthorinfo.getColumnType().equals("varchar") || !columnAuthorinfo.getColumnComment().equals("创建者") || !columnAuthorinfo.getColumnKey().equals("") || !columnAuthorinfo.getExtra().equals("")) {
                    String sql = "ALTER TABLE base_users MODIFY COLUMN author varchar(32) NOT NULL COMMENT '创建者' AFTER updated";
                    Query query = em.createNativeQuery(sql);
                    query.executeUpdate();
                }
            }
            ColumnInfo columnDeletedinfo = getColumnInfoByName(tableName,"deleted");
            if (columnDeletedinfo == null) {
                String sql = "ALTER TABLE base_users ADD COLUMN deleted tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除标记' AFTER author";
                Query query = em.createNativeQuery(sql);
                query.executeUpdate();
            }
            else {
                if (!columnDeletedinfo.getIsNullable().equals("NO") || !columnDeletedinfo.getColumnType().equals("tinyint") || !columnDeletedinfo.getColumnComment().equals("删除标记") || !columnDeletedinfo.getColumnKey().equals("") || !columnDeletedinfo.getExtra().equals("")) {
                    String sql = "ALTER TABLE base_users MODIFY COLUMN deleted tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除标记' AFTER author";
                    Query query = em.createNativeQuery(sql);
                    query.executeUpdate();
                }
            }
            veriyPrimaryKey(tableName);

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    private ColumnInfo getColumnInfoByName(String tablename,String column_name)  {
        // 使用预编译防止sql注入
        String sql = "select column_name, is_nullable, data_type, column_comment, column_key, extra from information_schema.columns " +
                "where table_name = ? and column_name = ? and table_schema = (select database())";
        Query query = em.createNativeQuery(sql);
        query.setParameter(1, tablename);
        query.setParameter(2, column_name);
        List result = query.getResultList();
        if (result.size() > 0 ) {
            Object[] arr = (Object[]) result.get(0);
            return new ColumnInfo(arr[0],arr[1],arr[2],arr[3],arr[4],arr[5],null,"true");
        }
        return null;
    }
    private void veriyPrimaryKey(String tablename) throws Exception {
        // 使用预编译防止sql注入
        String sql = "select column_name from information_schema.key_column_usage " +
                "where table_name = ? and column_name != 'id' and constraint_name = 'PRIMARY'";
        Query query = em.createNativeQuery(sql);
        query.setParameter(1, tablename);
        List result = query.getResultList();
        if (result.size() > 0 ) {
            throw new Exception(result.get(0).toString() + "被设置为主键，不能设置除id字段以外的主键！");
        }
    }
}
