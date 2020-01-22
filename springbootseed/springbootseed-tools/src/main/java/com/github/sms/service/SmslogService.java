package com.github.sms.service;

import com.github.sms.domain.Smslog;
import com.github.sms.service.dto.SmslogDTO;
import com.github.sms.service.dto.SmslogQueryCriteria;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
* @author oldhand
* @date 2020-01-21
*/
public interface SmslogService {

    /**
    * 查询数据分页
    * @param criteria 条件参数
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(SmslogQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<SmslogDTO>
    */
    List<SmslogDTO> queryAll(SmslogQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id ID
     * @return SmslogDTO
     */
    SmslogDTO findById(Long id);

    SmslogDTO create(Smslog resources);

    SmslogDTO update(Long id, Smslog resources);

    void update(Long id, int status,String result);

    Map<String, Object> search(String mobile, String regioncode);

    void download(List<SmslogDTO> all, HttpServletResponse response) throws IOException;
}