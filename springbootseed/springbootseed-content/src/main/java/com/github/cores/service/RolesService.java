package com.github.cores.service;

import com.github.cores.domain.Roles;
import com.github.cores.service.dto.RolesDTO;
import com.github.cores.service.dto.RolesQueryCriteria;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @author oldhand
* @date 2020-01-14
*/
public interface RolesService {

    /**
    * 查询数据分页
    * @param criteria 条件参数
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(RolesQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<RolesDTO>
    */
    List<RolesDTO> queryAll(RolesQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id ID
     * @return RolesDTO
     */
    RolesDTO findById(Long id);

    RolesDTO create(Roles resources);

    RolesDTO update(Long id,Roles resources);

    void delete(Long id);
	
	void makedelete(Long id);

    void download(List<RolesDTO> all, HttpServletResponse response) throws IOException;
}