package com.github.cores.service;

import com.github.cores.domain.Parenttabs;
import com.github.cores.service.dto.ParenttabsDTO;
import com.github.cores.service.dto.ParenttabsQueryCriteria;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @author oldhand
* @date 2020-01-14
*/
public interface ParenttabsService {

    /**
    * 查询数据分页
    * @param criteria 条件参数
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(ParenttabsQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<ParenttabsDTO>
    */
    List<ParenttabsDTO> queryAll(ParenttabsQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id ID
     * @return ParenttabsDTO
     */
    ParenttabsDTO findById(Long id);

    ParenttabsDTO create(Parenttabs resources);

    ParenttabsDTO update(Long id,Parenttabs resources);

    void delete(Long id);
	
	void makedelete(Long id);

    void download(List<ParenttabsDTO> all, HttpServletResponse response) throws IOException;
}