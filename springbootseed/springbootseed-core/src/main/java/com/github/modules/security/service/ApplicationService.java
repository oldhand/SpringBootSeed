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

    ApplicationDTO findByAppid(String appid);

}
