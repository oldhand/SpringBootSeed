package com.github.cores.service;

import com.github.cores.domain.Tabs;
import com.github.cores.service.dto.TabsDTO;
import com.github.cores.service.dto.TabsQueryCriteria;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @author oldhand
* @date 2020-01-14
*/
public interface TabsService {

    /**
    * 查询数据分页
    * @param criteria 条件参数
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(TabsQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<TabsDTO>
    */
    List<TabsDTO> queryAll(TabsQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id ID
     * @return TabsDTO
     */
    TabsDTO findById(Long id);

    TabsDTO create(Tabs resources);

    TabsDTO update(Long id,Tabs resources);

    void delete(Long id);
	
	void makedelete(Long id);

    void download(List<TabsDTO> all, HttpServletResponse response) throws IOException;
}