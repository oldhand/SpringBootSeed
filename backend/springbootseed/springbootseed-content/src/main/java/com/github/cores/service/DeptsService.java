package com.github.cores.service;

import com.github.cores.domain.Depts;
import com.github.cores.service.dto.DeptsDTO;
import com.github.cores.service.dto.DeptsQueryCriteria;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @author oldhand
* @date 2020-01-02
*/
public interface DeptsService {

    /**
    * 查询数据分页
    * @param criteria 条件参数
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(DeptsQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<DeptsDTO>
    */
    List<DeptsDTO> queryAll(DeptsQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id ID
     * @return DeptsDTO
     */
    DeptsDTO findById(Long id);

    DeptsDTO create(Depts resources);

    DeptsDTO update(Long id,Depts resources);

    void delete(Long id);
	
	void makedelete(Long id);

    void download(List<DeptsDTO> all, HttpServletResponse response) throws IOException;
}