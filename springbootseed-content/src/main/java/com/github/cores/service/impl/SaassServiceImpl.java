package com.github.cores.service.impl;

import com.github.cores.domain.Parenttabs;
import com.github.cores.domain.Saass;
import com.github.cores.domain.Tabs;
import com.github.cores.domain.Users;
import com.github.cores.repository.ParenttabsRepository;
import com.github.cores.repository.TabsRepository;
import com.github.cores.repository.UsersRepository;
import com.github.cores.service.dto.ParenttabsQueryCriteria;
import com.github.exception.EntityExistException;
import com.github.exception.EntityExistException;
import com.github.profile.domain.Profile;
import com.github.profile.repository.ProfileRepository;
import com.github.repository.ContentIdsRepository;
import com.github.service.ContentIdsService;
import com.github.utils.*;
import com.github.cores.repository.SaassRepository;
import com.github.cores.service.SaassService;
import com.github.cores.service.dto.SaassDTO;
import com.github.cores.service.dto.SaassQueryCriteria;
import com.github.cores.service.mapper.SaassMapper;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.io.IOException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
* @author oldhand
* @date 2020-01-14
*/
@Service
@CacheConfig(cacheNames = "Saass")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SaassServiceImpl implements SaassService {

    private final SaassRepository SaassRepository;
    private final UsersRepository usersrepository;
    private final ProfileRepository profilerepository;
    private final ParenttabsRepository parenttabsrepository;
    private final TabsRepository tabsrepository;

    private final SaassMapper SaassMapper;

    @PersistenceContext
    private EntityManager em;

    public SaassServiceImpl(SaassRepository SaassRepository, SaassMapper SaassMapper, UsersRepository usersrepository, ProfileRepository profilerepository, ParenttabsRepository parenttabsrepository, TabsRepository tabsrepository) {
        this.SaassRepository = SaassRepository;
        this.SaassMapper = SaassMapper;
        this.usersrepository = usersrepository;
        this.profilerepository = profilerepository;
        this.parenttabsrepository = parenttabsrepository;
        this.tabsrepository = tabsrepository;
    }

    @Override
    @Cacheable
    public Map<String,Object> queryAll(SaassQueryCriteria criteria, Pageable pageable){
        Page<Saass> page = SaassRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(SaassMapper::toDto));
    }

    @Override
    @Cacheable
    public List<SaassDTO> queryAll(SaassQueryCriteria criteria){
        return SaassMapper.toDto(SaassRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Cacheable(key = "#p0")
    public SaassDTO findById(Long id) {
        Saass Saass = SaassRepository.findById(id).orElseGet(Saass::new);
        ValidationUtil.isNull(Saass.getId(),"Saass","id",id);
        return SaassMapper.toDto(Saass);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public SaassDTO create(Saass resources) {
        if(SaassRepository.findByName(resources.getName()) != null){
            throw new EntityExistException(Saass.class,"name",resources.getName());
        }
        return SaassMapper.toDto(SaassRepository.saveAndFlush(resources));
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public SaassDTO update(Long id,Saass resources) {
        Saass Saass = SaassRepository.findById(id).orElseGet(Saass::new);
        ValidationUtil.isNull( Saass.getId(),"Saass","id",resources.getId());
        Saass Saass_name = SaassRepository.findByName(resources.getName());
        if(Saass_name != null && !Saass_name.getId().equals(Saass.getId())){
            throw new EntityExistException(Saass.class,"name",resources.getName());
        }
        Saass.copy(resources);
		return SaassMapper.toDto(SaassRepository.saveAndFlush(Saass));
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        SaassRepository.deleteById(id);
    }
	
    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void makedelete(Long id) {
         Saass Saass = SaassRepository.findById(id).orElseGet(Saass::new);
		 ValidationUtil.isNull( Saass.getId(),"Saass","id",id);
		 Saass.setDeleted(1);
		 SaassRepository.save(Saass);
    }


    @Override
    public void download(List<SaassDTO> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (SaassDTO Saass : all) {
            Map<String,Object> map = new LinkedHashMap<>();
		    map.put("ID", Saass.getId());
		    map.put("创建日期", Saass.getPublished());
		    map.put("更新日期", Saass.getUpdated());
		    map.put("创建者", Saass.getAuthor());
		    map.put("删除标记", Saass.getDeleted());
            map.put("名称", Saass.getName());
            map.put("公司名称", Saass.getCompanyname());
            map.put("短名称", Saass.getShortname());
            map.put("省份", Saass.getProvince());
            map.put("城市", Saass.getCity());
            map.put("创建人", Saass.getProfileid());
            map.put("联系人", Saass.getContact());
            map.put("联系电话", Saass.getMobile());
            map.put("审批状态", Saass.getApprovalstatus());
            map.put("审批人", Saass.getApprover());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }


    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void initdata(String author, Long id){
        Saass saass = SaassRepository.findById(id).orElseGet(Saass::new);
        ValidationUtil.isNull( saass.getId(),"Saass","id",id);
        String profileid = saass.getProfileid();

        Profile profile = profilerepository.myfindById(profileid);

        ParenttabsQueryCriteria criteria = new ParenttabsQueryCriteria();
        criteria.setSaasid(id);
        criteria.setTabname("Settings");

        Parenttabs parenttab = new Parenttabs();
        parenttab.setDeleted(0);
        parenttab.setSaasid(id);
        parenttab.setTabname("Settings");
        Example<Parenttabs> example = Example.of(parenttab);

        String sql = "delete from base_parenttabs where saasid = " + id;
        Query query = em.createNativeQuery(sql);
        query.executeUpdate();

        List<Parenttabs> parenttabs = parenttabsrepository.findAll(example);
        System.out.println("-----------parenttabs------"+parenttabs.toString()+"---------------");
        if (parenttabs.size() == 0) {
            parenttab.setId(ContentUtils.makeContentId("base_parenttabs"));
            parenttab.setAuthor(author);
            parenttab.setTablabel("Settings");
            parenttab.setSquence(100);
            parenttab.setIcon("settings");
            parenttab.setPresence(0);
            parenttabsrepository.saveAndFlush(parenttab);
        }

        Tabs tab = new Tabs();
        tab.setDeleted(0);
        tab.setSaasid(id);
        tab.setParenttab("Settings");
        tab.setTabname("Users");
        Example<Tabs> tabexample = Example.of(tab);
        List<Tabs> tabs = tabsrepository.findAll(tabexample);
        System.out.println("-----------tabs------"+tabs.toString()+"---------------");
        if (tabs.size() == 0) {
//            parenttab.setId(ContentUtils.makeContentId("base_parenttabs"));
//            parenttab.setAuthor(author);
//            parenttab.setTablabel("Settings");
//            parenttab.setSquence(100);
//            parenttab.setIcon("settings");
//            parenttab.setPresence(0);
//            parenttabsrepository.saveAndFlush(parenttab);
        }

//        // 删除所有后台用户
//        String sql = "select from base_users where saasid = " + id;
//        Query query = em.createNativeQuery(sql, SaassService.class);
//        query.executeUpdate();
//
//        // 校验管理员
//        Users user = usersrepository.findByProfileid(profileid);
//        if (user == null) {
//            Users resources = new Users();
//            resources.setDeleted(0);
//            resources.setProfileid(profileid);
//            resources.setRegioncode(profile.getRegioncode());
//            resources.setMobile(profile.getMobile());
//            resources.setGivenname(resources.getGivenname());
//            resources.setEmail(profile.getEmail());
//            resources.setLink(profile.getLink());
//            resources.setGender(profile.getGender());
//            resources.setCountry(profile.getCountry());
//            resources.setRegion(profile.getRegion());
//            resources.setBirthdate(profile.getBirthdate());
//            resources.setProvince(profile.getProvince());
//            resources.setCity(profile.getCity());
//            resources.setRealname(profile.getRealname());
//            resources.setIdentitycard(profile.getIdentitycard());
//            usersrepository.saveAndFlush(resources);
//        }


    }

}