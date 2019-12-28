package com.github.profile.service.impl;

import com.github.profile.domain.Profile;
import com.github.exception.EntityExistException;
import com.github.profile.domain.RegisterProfile;
import com.github.profile.domain.UpdateProfile;
import com.github.profile.service.utils.ProfileUtils;
import com.github.utils.*;
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
    @Cacheable(key = "#p0")
    public ProfileDTO findByUsername(String username) {
        Profile profile = profileRepository.findByUsername(username);
        ValidationUtil.isNull(profile.getUsername(),"Profile","username",username);
        return profileMapper.toDto(profile);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public ProfileDTO create(RegisterProfile resources) {
        Profile profile = new Profile();
        profile.setId(ProfileUtils.makeProfileId());
        profile.setUsername(resources.getUsername());
        profile.setPassword(EncryptUtils.encryptPassword(resources.getPassword()));
        profile.setType(resources.getType());
        profile.setRegioncode(resources.getRegioncode());
        profile.setMobile(resources.getMobile());
        profile.setGivenname(resources.getGivenname());
        profile.setStatus(0);
        profile.setEmail(resources.getEmail());
        profile.setLink(resources.getLink());
        profile.setGender(resources.getGender());
        profile.setCountry(resources.getCountry());
        profile.setRegion(resources.getRegion());
        profile.setBirthdate(resources.getBirthdate());
        profile.setProvince(resources.getProvince());
        profile.setCity(resources.getCity());
        profile.setRealname(resources.getRealname());
        profile.setIdentitycard(resources.getIdentitycard());
        profile.setRegIp(resources.getRegIp());
        profile.setSystem(resources.getSystem());
        profile.setBrowser(resources.getBrowser());

        if(profileRepository.findByUsername(profile.getUsername()) != null){
            throw new EntityExistException(Profile.class,"username",profile.getUsername());
        }
        return profileMapper.toDto(profileRepository.saveAndFlush(profile));
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(UpdateProfile resources) {
        Profile profile = profileRepository.myfindById(resources.getId());
        profile.setUsername(resources.getUsername());
        profile.setRegioncode(resources.getRegioncode());
        profile.setMobile(resources.getMobile());
        profile.setGivenname(resources.getGivenname());
        profile.setEmail(resources.getEmail());
        profile.setLink(resources.getLink());
        profile.setGender(resources.getGender());
        profile.setCountry(resources.getCountry());
        profile.setRegion(resources.getRegion());
        profile.setBirthdate(resources.getBirthdate());
        profile.setProvince(resources.getProvince());
        profile.setCity(resources.getCity());
        profile.setRealname(resources.getRealname());
        profile.setIdentitycard(resources.getIdentitycard());
        profile.setRegIp(resources.getRegIp());
        profile.setSystem(resources.getSystem());
        profile.setBrowser(resources.getBrowser());
        profileRepository.save(profile);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void disable(String id) {
        Profile profile = profileRepository.myfindById(id);
        profile.setStatus(1);
        profileRepository.save(profile);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void enable(String id) {
        Profile profile = profileRepository.myfindById(id);
        profile.setStatus(0);
        profileRepository.save(profile);
    }


    @Override
    public void download(List<ProfileDTO> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ProfileDTO profile : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("自增ID", profile.getIdentifier());
            map.put("用户ID", profile.getId());
            map.put("用户名称", profile.getUsername());
            map.put("创建日期", profile.getPublished());
            map.put("更新日期", profile.getUpdated());
            map.put("用户类型", profile.getType());
            map.put("国家代码", profile.getRegioncode());
            map.put("手机号码", profile.getMobile());
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