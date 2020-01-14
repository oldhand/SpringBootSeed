package com.github.cores.service;

import com.github.cores.domain.Saass;
import com.github.cores.service.dto.SaassDTO;
import com.github.cores.service.dto.SaassQueryCriteria;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @author oldhand
* @date 2020-01-14
*/
public interface SaassService {

    /**
    * 查询数据分页
    * @param criteria 条件参数
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(SaassQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<SaassDTO>
    */
    List<SaassDTO> queryAll(SaassQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id ID
     * @return SaassDTO
     */
    SaassDTO findById(Long id);

    SaassDTO create(Saass resources);

    SaassDTO update(Long id,Saass resources);

    void delete(Long id);
	
	void makedelete(Long id);

    void download(List<SaassDTO> all, HttpServletResponse response) throws IOException;
}