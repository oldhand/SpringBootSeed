package com.github.modules.security.service;

import com.github.exception.BadRequestException;
import com.github.modules.security.security.JwtAuthentication;
import com.github.modules.security.service.dto.ApplicationDTO;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

/**
 * @author oldhand
 * @date 2019-12-16
*/
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class JwtApplicationDetailsService implements UserDetailsService {

    private final ApplicationService applicationService;


    public JwtApplicationDetailsService(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @Override
    public UserDetails loadUserByUsername(String appid){

        ApplicationDTO application = applicationService.findByAppid(appid);
        if (application == null) {
            throw new BadRequestException("账号不存在");
        } else {
            return createJwtUser(application);
        }
    }

    public UserDetails createJwtUser(ApplicationDTO application) {
        return new JwtAuthentication(
                application.getId(),
                application.getAppid(),
                application.getSecret(),
                application.getDescription(),
                "",
                "",
                null,
                null,
                null,
                true,
                application.getPublished(),
                null
        );
    }
}
