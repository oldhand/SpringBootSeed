package com.github.cores.service;

import com.github.cores.domain.Modentitynos;
import com.github.cores.service.dto.ModentitynosDTO;
import com.github.cores.service.dto.ModentitynosQueryCriteria;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @author oldhand
* @date 2020-01-15
*/
public interface ModentitynosService {

    /**
    * 查询数据分页
    * @param criteria 条件参数
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(ModentitynosQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<ModentitynosDTO>
    */
    List<ModentitynosDTO> queryAll(ModentitynosQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id ID
     * @return ModentitynosDTO
     */
    ModentitynosDTO findById(Long id);

    ModentitynosDTO create(Modentitynos resources);

    ModentitynosDTO update(Long id,Modentitynos resources);

    void delete(Long id);
	
	void makedelete(Long id);

    void download(List<ModentitynosDTO> all, HttpServletResponse response) throws IOException;
}