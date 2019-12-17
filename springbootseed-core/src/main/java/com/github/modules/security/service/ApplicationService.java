package com.github.modules.security.service;

import com.github.modules.security.domain.Application;
import com.github.modules.security.service.dto.ApplicationDTO;
import com.github.modules.security.service.dto.ApplicationQueryCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author oldhand
 * @date 2019-12-16
*/
public interface ApplicationService {

//    UserDTO findById(long id);
//
//    UserDTO create(User resources);
//
//    void update(User resources);
//
//    void delete(Long id);
//
    ApplicationDTO findByAppid(String appid);


//
//    void updatePass(String username, String encryptPassword);
//
//    void updateAvatar(MultipartFile file);
//
//    void updateEmail(String username, String email);
//
//    Object queryAll(UserQueryCriteria criteria, Pageable pageable);
//
//    List<UserDTO> queryAll(UserQueryCriteria criteria);
//
//    void download(List<UserDTO> queryAll, HttpServletResponse response) throws IOException;
}
