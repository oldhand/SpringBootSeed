package com.github.cores.service;

import com.github.cores.domain.Tabs2permissions;
import com.github.cores.service.dto.Tabs2permissionsDTO;
import com.github.cores.service.dto.Tabs2permissionsQueryCriteria;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @author oldhand
* @date 2020-01-14
*/
public interface Tabs2permissionsService {

    /**
    * 查询数据分页
    * @param criteria 条件参数
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(Tabs2permissionsQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<Tabs2permissionsDTO>
    */
    List<Tabs2permissionsDTO> queryAll(Tabs2permissionsQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id ID
     * @return Tabs2permissionsDTO
     */
    Tabs2permissionsDTO findById(Long id);

    Tabs2permissionsDTO create(Tabs2permissions resources);

    Tabs2permissionsDTO update(Long id,Tabs2permissions resources);

    void delete(Long id);
	
	void makedelete(Long id);

    void download(List<Tabs2permissionsDTO> all, HttpServletResponse response) throws IOException;
}