package com.github.cores.service;

import com.github.cores.domain.Suppliers;
import com.github.cores.service.dto.SuppliersDTO;
import com.github.cores.service.dto.SuppliersQueryCriteria;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @author oldhand
* @date 2020-01-02
*/
public interface SuppliersService {

    /**
    * 查询数据分页
    * @param criteria 条件参数
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(SuppliersQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<SuppliersDTO>
    */
    List<SuppliersDTO> queryAll(SuppliersQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id ID
     * @return SuppliersDTO
     */
    SuppliersDTO findById(Long id);

    SuppliersDTO create(Suppliers resources);

    SuppliersDTO update(Long id,Suppliers resources);

    void delete(Long id);
	
	void makedelete(Long id);

    void download(List<SuppliersDTO> all, HttpServletResponse response) throws IOException;
}