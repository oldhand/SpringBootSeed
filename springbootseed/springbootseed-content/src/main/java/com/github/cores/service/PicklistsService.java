package com.github.cores.service;

import com.github.cores.domain.Picklists;
import com.github.cores.service.dto.PicklistsDTO;
import com.github.cores.service.dto.PicklistsQueryCriteria;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @author oldhand
* @date 2020-01-14
*/
public interface PicklistsService {

    /**
    * 查询数据分页
    * @param criteria 条件参数
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(PicklistsQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<PicklistsDTO>
    */
    List<PicklistsDTO> queryAll(PicklistsQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id ID
     * @return PicklistsDTO
     */
    PicklistsDTO findById(Long id);

    PicklistsDTO create(Picklists resources);

    PicklistsDTO update(Long id,Picklists resources);

    void delete(Long id);
	
	void makedelete(Long id);

    void download(List<PicklistsDTO> all, HttpServletResponse response) throws IOException;
}