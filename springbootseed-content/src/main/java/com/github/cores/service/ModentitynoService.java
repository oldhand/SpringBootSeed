package com.github.cores.service;

import com.github.cores.domain.Modentityno;
import com.github.cores.service.dto.ModentitynoDTO;
import com.github.cores.service.dto.ModentitynoQueryCriteria;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @author oldhand
* @date 2020-01-15
*/
public interface ModentitynoService {

    /**
    * 查询数据分页
    * @param criteria 条件参数
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(ModentitynoQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<ModentitynoDTO>
    */
    List<ModentitynoDTO> queryAll(ModentitynoQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id ID
     * @return ModentitynoDTO
     */
    ModentitynoDTO findById(Long id);

    ModentitynoDTO create(Modentityno resources);

    ModentitynoDTO update(Long id,Modentityno resources);

    void delete(Long id);
	
	void makedelete(Long id);

    void download(List<ModentitynoDTO> all, HttpServletResponse response) throws IOException;
}