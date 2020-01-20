package com.github.cores.service;

import com.github.cores.domain.Permissions;
import com.github.cores.service.dto.PermissionsDTO;
import com.github.cores.service.dto.PermissionsQueryCriteria;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @author oldhand
* @date 2020-01-02
*/
public interface PermissionsService {

    /**
    * 查询数据分页
    * @param criteria 条件参数
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(PermissionsQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<PermissionsDTO>
    */
    List<PermissionsDTO> queryAll(PermissionsQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id ID
     * @return PermissionsDTO
     */
    PermissionsDTO findById(Long id);

    PermissionsDTO create(Permissions resources);

    PermissionsDTO update(Long id,Permissions resources);

    void delete(Long id);
	
	void makedelete(Long id);

    void download(List<PermissionsDTO> all, HttpServletResponse response) throws IOException;
}