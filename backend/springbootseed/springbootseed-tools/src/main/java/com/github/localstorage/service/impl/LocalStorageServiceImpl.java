package com.github.localstorage.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.github.localstorage.domain.LocalStorage;
import com.github.exception.BadRequestException;
import com.github.localstorage.repository.LocalStorageRepository;
import com.github.localstorage.service.dto.LocalStorageDTO;
import com.github.localstorage.service.dto.LocalStorageQueryCriteria;
import com.github.utils.*;
import com.github.localstorage.utils.MD5;
import com.github.localstorage.service.LocalStorageService;
import com.github.localstorage.service.mapper.LocalStorageMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
* @author oldhand
* @date 2019-12-16
*/
@Service
@CacheConfig(cacheNames = "localStorage")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class LocalStorageServiceImpl implements LocalStorageService {

    private final LocalStorageRepository localStorageRepository;

    private final LocalStorageMapper localStorageMapper;

    @Value("${file.path}")
    private String path;

    @Value("${file.maxSize}")
    private long maxSize;

    public LocalStorageServiceImpl(LocalStorageRepository localStorageRepository, LocalStorageMapper localStorageMapper) {
        this.localStorageRepository = localStorageRepository;
        this.localStorageMapper = localStorageMapper;
    }

    @Override
    @Cacheable
    public Object queryAll(LocalStorageQueryCriteria criteria, Pageable pageable){
        Page<LocalStorage> page = localStorageRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(localStorageMapper::toDto));
    }

    @Override
    @Cacheable
    public List<LocalStorageDTO> queryAll(LocalStorageQueryCriteria criteria){
        return localStorageMapper.toDto(localStorageRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Cacheable(key = "#p0")
    public LocalStorageDTO findById(Long id){
        LocalStorage localStorage = localStorageRepository.findById(id).orElseGet(LocalStorage::new);
        ValidationUtil.isNull(localStorage.getId(),"LocalStorage","id",id);
        return localStorageMapper.toDto(localStorage);
    }


    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public LocalStorageDTO create(String name, MultipartFile multipartFile) {
        FileUtil.checkSize(maxSize, multipartFile.getSize());
        String suffix = FileUtil.getExtensionName(multipartFile.getOriginalFilename());
        // 可自行选择方式
//        String type = FileUtil.getFileTypeByMimeType(suffix);
        String type = FileUtil.getFileType(suffix);
        String basePath = System.getProperty("user.dir");
        if (path.startsWith("./")) {
            path = path.substring(2);
        }
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        if (path.endsWith("/")) {
            path = path.substring(0,path.length()-1);
        }

        Date date = new Date();
        SimpleDateFormat year = new SimpleDateFormat("yyyy");
        SimpleDateFormat month = new SimpleDateFormat("MMM", Locale.ENGLISH);
        String fullpath = File.separator + path + File.separator + year.format(date) + File.separator + month.format(date);

        File pathfile = new File(basePath + File.separator + fullpath);
        if (!pathfile.exists()) {
            pathfile.mkdirs();
        }

        fullpath = fullpath + File.separator + type +  File.separator;
        String md5;
        try {
            md5 = MD5.calcMD5(multipartFile.getInputStream());
        }catch (IOException e) {
            e.printStackTrace();
            throw new BadRequestException("上传失败");
        }
        try {
            LocalStorage localStorage = localStorageRepository.findByMd5(md5);
            if (localStorage != null) {
                return localStorageMapper.toDto(localStorage);
            }
        }catch (Exception e) {
            e.printStackTrace();
            throw new BadRequestException("上传失败");
        }
        File file = FileUtil.upload(multipartFile, basePath + fullpath, md5);
        if(ObjectUtil.isNull(file)){
            throw new BadRequestException("上传失败");
        }
        try {
            String sourceName = multipartFile.getOriginalFilename();
            name = StringUtils.isBlank(name) ? FileUtil.getFileNameNoEx(multipartFile.getOriginalFilename()) : name;
            String username;
            try {
                username = SecurityUtils.getUsername();
            } catch (Exception e) {
                username = "anonymous";
            }
            LocalStorage localStorage = new LocalStorage(
                    file.getName(),
                    name,
                    sourceName,
                    suffix,
                    fullpath + file.getName(),
                    type,
                    md5,
                    FileUtil.getSize(multipartFile.getSize()),
                    username
            );
            return localStorageMapper.toDto(localStorageRepository.save(localStorage));
        }catch (Exception e){
            FileUtil.del(file);
            throw e;
        }
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(LocalStorage resources) {
        LocalStorage localStorage = localStorageRepository.findById(resources.getId()).orElseGet(LocalStorage::new);
        ValidationUtil.isNull( localStorage.getId(),"LocalStorage","id",resources.getId());
        localStorage.copy(resources);
        localStorageRepository.save(localStorage);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        LocalStorage storage = localStorageRepository.findById(id).orElseGet(LocalStorage::new);
        FileUtil.del(storage.getPath());
        localStorageRepository.delete(storage);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            LocalStorage storage = localStorageRepository.findById(id).orElseGet(LocalStorage::new);
            FileUtil.del(storage.getPath());
            localStorageRepository.delete(storage);
        }
    }

    @Override
    public void download(List<LocalStorageDTO> queryAll, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (LocalStorageDTO localStorageDTO : queryAll) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("文件名", localStorageDTO.getRealName());
            map.put("原文件名", localStorageDTO.getSourceName());
            map.put("存储相对路径", localStorageDTO.getPath());
            map.put("文件类型", localStorageDTO.getType());
            map.put("MD5", localStorageDTO.getMd5());
            map.put("文件大小", localStorageDTO.getSize());
            map.put("操作人", localStorageDTO.getOperate());
            map.put("备注名", localStorageDTO.getName());
            map.put("创建日期", localStorageDTO.getPublished());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
