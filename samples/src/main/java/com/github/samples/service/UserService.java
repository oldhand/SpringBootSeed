package com.github.samples.service;

import com.github.samples.domain.User;
import com.github.samples.service.dto.UserDTO;
import com.github.samples.service.dto.UserQueryCriteria;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @author oldhand
* @date 2019-12-19
*/
public interface UserService {

    /**
    * 查询数据分页
    * @param criteria 条件参数
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(UserQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<UserDTO>
    */
    List<UserDTO> queryAll(UserQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id ID
     * @return UserDTO
     */
    UserDTO findById(Long id);

    UserDTO create(User resources);

    void update(User resources);

    void delete(Long id);

    void download(List<UserDTO> all, HttpServletResponse response) throws IOException;
}