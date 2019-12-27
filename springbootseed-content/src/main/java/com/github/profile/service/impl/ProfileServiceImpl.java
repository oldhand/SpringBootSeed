package com.github.profile.service.impl;

import com.github.profile.domain.Profile;
import com.github.exception.EntityExistException;
import com.github.utils.ValidationUtil;
import com.github.utils.FileUtil;
import com.github.profile.repository.ProfileRepository;
import com.github.profile.service.ProfileService;
import com.github.profile.service.dto.ProfileDTO;
import com.github.profile.service.dto.ProfileQueryCriteria;
import com.github.profile.service.mapper.ProfileMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.github.utils.PageUtil;
import com.github.utils.QueryHelp;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
* @author oldhand
* @date 2019-12-27
*/
@Service
@CacheConfig(cacheNames = "profile")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;

    private final ProfileMapper profileMapper;

    public ProfileServiceImpl(ProfileRepository profileRepository, ProfileMapper profileMapper) {
        this.profileRepository = profileRepository;
        this.profileMapper = profileMapper;
    }

    @Override
    @Cacheable
    public Map<String,Object> queryAll(ProfileQueryCriteria criteria, Pageable pageable){
        Page<Profile> page = profileRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(profileMapper::toDto));
    }

    @Override
    @Cacheable
    public List<ProfileDTO> queryAll(ProfileQueryCriteria criteria){
        return profileMapper.toDto(profileRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Cacheable(key = "#p0")
    public ProfileDTO findById(String id) {
        Profile profile = profileRepository.findById(id).orElseGet(Profile::new);
        ValidationUtil.isNull(profile.getId(),"Profile","id",id);
        return profileMapper.toDto(profile);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public ProfileDTO create(Profile resources) {
        if(profileRepository.findByUsername(resources.getUsername()) != null){
            throw new EntityExistException(Profile.class,"username",resources.getUsername());
        }
        return profileMapper.toDto(profileRepository.save(resources));
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(Profile resources) {
        Profile profile = profileRepository.findById(resources.getId()).orElseGet(Profile::new);
        ValidationUtil.isNull( profile.getId(),"Profile","id",resources.getId());
        profile = profileRepository.findByUsername(resources.getUsername());
        if(profile != null && !profile.getId().equals(profile.getId())){
            throw new EntityExistException(Profile.class,"username",resources.getUsername());
        }
        profile.copy(resources);
        profileRepository.save(profile);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id) {
        profileRepository.deleteById(id);
    }


    @Override
    public void download(List<ProfileDTO> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ProfileDTO profile : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("用户名称", profile.getUsername());
            map.put("创建日期", profile.getPublished());
            map.put("更新日期", profile.getUpdated());
            map.put("用户类型", profile.getType());
            map.put("国家代码", profile.getRegioncode());
            map.put("手机号码", profile.getMobile());
            map.put("密码", profile.getPassword());
            map.put("昵称", profile.getGivenname());
            map.put("用户状态", profile.getStatus());
            map.put("用户邮箱", profile.getEmail());
            map.put("用户头像地址", profile.getLink());
            map.put("性别", profile.getGender());
            map.put("所属国家", profile.getCountry());
            map.put("地区", profile.getRegion());
            map.put("出生日期", profile.getBirthdate());
            map.put("省份", profile.getProvince());
            map.put("城市", profile.getCity());
            map.put("真实姓名", profile.getRealname());
            map.put("身份证号码", profile.getIdentitycard());
            map.put("注册IP", profile.getRegIp());
            map.put("注册系统", profile.getSystem());
            map.put("注册浏览器", profile.getBrowser());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}