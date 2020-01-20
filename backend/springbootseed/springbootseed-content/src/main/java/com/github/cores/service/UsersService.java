package com.github.cores.service;

import com.github.cores.domain.Users;
import com.github.cores.service.dto.UsersDTO;
import com.github.cores.service.dto.UsersQueryCriteria;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @author oldhand
* @date 2020-01-02
*/
public interface UsersService {

    /**
    * 查询数据分页
    * @param criteria 条件参数
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(UsersQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<UsersDTO>
    */
    List<UsersDTO> queryAll(UsersQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id ID
     * @return UsersDTO
     */
    UsersDTO findById(Long id);

    UsersDTO findByProfileid(String profileid);

    UsersDTO create(Users resources);

    UsersDTO update(Long id,Users resources);

    void delete(Long id);

	void makedelete(Long id);

    void download(List<UsersDTO> all, HttpServletResponse response) throws IOException;
}