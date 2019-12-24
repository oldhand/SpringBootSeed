package com.github.localstorage.service;

import com.github.localstorage.domain.LocalStorage;
import com.github.localstorage.service.dto.LocalStorageDTO;
import com.github.localstorage.service.dto.LocalStorageQueryCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
* @author oldhand
* @date 2019-12-16
*/
public interface LocalStorageService {

    Object queryAll(LocalStorageQueryCriteria criteria, Pageable pageable);

    List<LocalStorageDTO> queryAll(LocalStorageQueryCriteria criteria);

    LocalStorageDTO findById(Long id);


    LocalStorageDTO create(String name, MultipartFile file);

    void update(LocalStorage resources);

    void delete(Long id);

    void deleteAll(Long[] ids);

    void download(List<LocalStorageDTO> queryAll, HttpServletResponse response) throws IOException;
}