package com.github.cores.service;

import com.github.cores.domain.Settings;
import com.github.cores.service.dto.SettingsDTO;
import com.github.cores.service.dto.SettingsQueryCriteria;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @author oldhand
* @date 2020-01-15
*/
public interface SettingsService {

    /**
    * 查询所有数据不分页
    * @return List<SettingsDTO>
    */
    List<SettingsDTO> query();

    SettingsDTO update(Settings resources);

}