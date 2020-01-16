package com.github.profile.service.impl;

import com.github.cores.domain.Tabs;
import com.github.cores.domain.Tabs2permissions;
import com.github.cores.domain.Users;
import com.github.cores.repository.Tabs2permissionsRepository;
import com.github.cores.repository.TabsRepository;
import com.github.cores.repository.UsersRepository;
import com.github.exception.BadRequestException;
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
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.*;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @author oldhand
* @date 2019-12-27
*/
@Service
@CacheConfig(cacheNames = "profile")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;
    private final UsersRepository usersrepository;
    private final TabsRepository tabsrepository;
    private final Tabs2permissionsRepository tabs2permissionsrepository;

    private final ProfileMapper profileMapper;

    public ProfileServiceImpl(ProfileRepository profileRepository, ProfileMapper profileMapper, UsersRepository usersrepository,TabsRepository tabsrepository,Tabs2permissionsRepository tabs2permissionsrepository) {
        this.profileRepository = profileRepository;
        this.usersrepository = usersrepository;
        this.tabsrepository = tabsrepository;
        this.tabs2permissionsrepository = tabs2permissionsrepository;

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
        Profile profile = profileRepository.myfindById(id);
        if (profile == null) throw new BadRequestException("用户不存在");
        ValidationUtil.isNull(profile.getId(),"Profile","id",id);
        return profileMapper.toDto(profile);
    }

    @Override
    @Cacheable(key = "#p0")
    public ProfileDTO findByUsername(String username) {
        Profile profile = profileRepository.findByUsername(username);
        if (profile == null) throw new BadRequestException("用户不存在");
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
        profile.setPassword(PasswordUtils.encryptPassword(resources.getPassword()));
        profile.setType(resources.getType());
        profile.setRegioncode(resources.getRegioncode());
        profile.setMobile(resources.getMobile());
        profile.setGivenname(resources.getGivenname());
        profile.setStatus(true);
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
        if (profile == null) {
            throw new BadRequestException("用户不存在");
        }
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
        if (profile == null) {
            throw new BadRequestException("用户不存在");
        }
        profile.setStatus(false);
        profileRepository.save(profile);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void enable(String id) {
        Profile profile = profileRepository.myfindById(id);
        if (profile == null) {
            throw new BadRequestException("用户不存在");
        }
        profile.setStatus(true);
        profileRepository.save(profile);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void changePassword(String id,String password) {
        Profile profile = profileRepository.myfindById(id);
        if (profile == null) {
            throw new BadRequestException("用户不存在");
        }
        profile.setPassword(PasswordUtils.encryptPassword(password));
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
    @Override
    @Cacheable(key = "T(String).valueOf('info::').concat(#p1)")
    public Map<String, Object> info(long saasid, String profileid) {
        Map<String, Object> info = new HashMap<>();
        info.put("profileid", profileid);
        final Profile profile = profileRepository.myfindById(profileid);
        Map<String,Object> profileinfo = new LinkedHashMap<>();
        profileinfo.put("identifier", profile.getIdentifier());
        profileinfo.put("username", profile.getUsername());
        profileinfo.put("published", profile.getPublished());
        profileinfo.put("updated", profile.getUpdated());
        profileinfo.put("type", profile.getType());
        profileinfo.put("regioncode", profile.getRegioncode());
        profileinfo.put("mobile", profile.getMobile());
        profileinfo.put("givenname", profile.getGivenname());
        profileinfo.put("status", profile.getStatus());
        profileinfo.put("email", profile.getEmail());
        profileinfo.put("link", profile.getLink());
        profileinfo.put("gender", profile.getGender());
        profileinfo.put("country", profile.getCountry());
        profileinfo.put("region", profile.getRegion());
        profileinfo.put("birthday", profile.getBirthdate());
        profileinfo.put("province", profile.getProvince());
        profileinfo.put("city", profile.getCity());
        profileinfo.put("realname", profile.getRealname());
        profileinfo.put("identitycard", profile.getIdentitycard());
        profileinfo.put("regip", profile.getRegIp());
        profileinfo.put("system", profile.getSystem());
        profileinfo.put("browser", profile.getBrowser());
        info.put("info", profileinfo);

        if (saasid > 0) {
            final Users user = usersrepository.findByProfileid(profileid);
            if (user != null) {
                info.put("isadmin", user.getIsadmin());
                info.put("deptid", user.getDeptid());
                info.put("permissionid", user.getPermissionid());
            }
            else {
                info.put("isadmin", null);
            }
            if (saasid != user.getSaasid()) {
                throw new BadRequestException("saasid error");
            }
            long premissionid = user.getPermissionid();
            Tabs tab = new Tabs();
            tab.setDeleted(0);
            tab.setSaasid(saasid);
            Example<Tabs> tabexample = Example.of(tab);
            List<Tabs> tabs = tabsrepository.findAll(tabexample);
            Map<Integer,String> tabids = new HashMap<>();
            List<String> roles = new ArrayList();
            if (tabs.size() > 0) {
                for (Tabs item : tabs) {
                    tabids.put(item.getTabid(),item.getTabname());
                }
                Tabs2permissions tabs2permission = new Tabs2permissions();
                tabs2permission.setDeleted(0);
                tabs2permission.setSaasid(saasid);
                tabs2permission.setPermissionid(premissionid);
                Example<Tabs2permissions> tabs2permissionexample = Example.of(tabs2permission);
                List<Tabs2permissions> tabs2permissions = tabs2permissionsrepository.findAll(tabs2permissionexample);
                for (Tabs2permissions item : tabs2permissions) {
                    int tabid = item.getTabid();
                    if (tabids.containsKey(tabid)) {
                        String tabname = tabids.get(tabid).toLowerCase();
                        if (item.getAll()) {
                            roles.add(tabname + ":all");
                        } else {
                            if (item.getEdit()) {
                                roles.add(tabname + ":edit");
                            }
                            if (item.getQuery()) {
                                roles.add(tabname + ":query");
                            }
                            if (item.getAdd()) {
                                roles.add(tabname + ":add");
                            }
                        }
                    }

                }
            }
            info.put("roles", roles);
        }
        return info;
    }
}