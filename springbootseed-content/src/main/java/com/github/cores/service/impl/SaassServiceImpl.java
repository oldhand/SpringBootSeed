package com.github.cores.service.impl;

import com.github.content.ContentUtils;
import com.github.cores.domain.*;
import com.github.cores.repository.*;
import com.github.exception.BadRequestException;
import com.github.exception.EntityExistException;
import com.github.profile.domain.Profile;
import com.github.profile.repository.ProfileRepository;
import com.github.utils.*;
import com.github.cores.service.SaassService;
import com.github.cores.service.dto.SaassDTO;
import com.github.cores.service.dto.SaassQueryCriteria;
import com.github.cores.service.mapper.SaassMapper;
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
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpServletResponse;

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
    private final PermissionsRepository permissionsrepository;
    private final DeptsRepository deptsrepository;
    private final Tabs2permissionsRepository tabs2permissionsrepository;
    private final ModentitynosRepository modentitynosrepository;

    private final SaassMapper SaassMapper;

    @PersistenceContext
    private EntityManager em;

    public SaassServiceImpl(SaassRepository SaassRepository, SaassMapper SaassMapper, UsersRepository usersrepository, ProfileRepository profilerepository, ParenttabsRepository parenttabsrepository, TabsRepository tabsrepository,PermissionsRepository permissionsrepository, DeptsRepository deptsrepository, Tabs2permissionsRepository tabs2permissionsrepository,ModentitynosRepository modentitynosrepository) {
        this.SaassRepository = SaassRepository;
        this.SaassMapper = SaassMapper;
        this.usersrepository = usersrepository;
        this.profilerepository = profilerepository;
        this.parenttabsrepository = parenttabsrepository;
        this.tabsrepository = tabsrepository;
        this.permissionsrepository = permissionsrepository;
        this.deptsrepository = deptsrepository;
        this.tabs2permissionsrepository = tabs2permissionsrepository;
        this.modentitynosrepository = modentitynosrepository;
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
    @CacheEvict(allEntries = true, cacheNames = {"Users","Depts","Tabs","Parenttabs","Permissions","Tabs2permissions"})
    @Transactional(rollbackFor = Exception.class)
    public void initdata(String author, Long saasid){
        Saass saass = SaassRepository.findById(saasid).orElseGet(Saass::new);
        ValidationUtil.isNull( saass.getId(),"Saass","id",saasid);
        String profileid = saass.getProfileid();

        Profile profile = profilerepository.myfindById(profileid);
        if (profile == null) {
            throw new BadRequestException("没有找到云服务的创建用户");
        }

        String sql = "delete from base_parenttabs where saasid = " + saasid;
        Query query = em.createNativeQuery(sql);
        query.executeUpdate();

        Parenttabs parenttab = new Parenttabs();
        parenttab.setDeleted(0);
        parenttab.setSaasid(saasid);
        parenttab.setTabname("Settings");
        Example<Parenttabs> example = Example.of(parenttab);
        List<Parenttabs> parenttabs = parenttabsrepository.findAll(example);
        //System.out.println("-----------parenttabs------"+parenttabs.toString()+"---------------");
        if (parenttabs.size() == 0) {
            parenttab.setId(ContentUtils.makeContentId("base_parenttabs"));
            parenttab.setAuthor(author);
            parenttab.setTablabel("Settings");
            parenttab.setSquence(100);
            parenttab.setIcon("settings");
            parenttab.setPresence(true);
            parenttabsrepository.saveAndFlush(parenttab);
        }

        sql = "delete from base_tabs where saasid = " + saasid;
        query = em.createNativeQuery(sql);
        query.executeUpdate();

        List<String> tabsConfig = Arrays.asList("Users","Depts","Permission","Roles","Setting");
        Map<String, Integer> tabids = new HashMap<>();
        int tabid = 101;
        int sequence = 100;
        for (String item : tabsConfig) {
            Tabs tab = new Tabs();
            tab.setDeleted(0);
            tab.setSaasid(saasid);
            tab.setParenttab("Settings");
            tab.setTabname(item);
            Example<Tabs> tabexample = Example.of(tab);
            List<Tabs> tabs = tabsrepository.findAll(tabexample);
            if (tabs.size() == 0) {
                tab.setId(ContentUtils.makeContentId("base_tabs"));
                tab.setAuthor(author);
                tab.setTablabel(item);
                tab.setSequence(sequence);
                tab.setIcon(item.toLowerCase());
                tab.setPresence(true);
                tab.setDatatype(0);
                tab.setTabid(tabid);
                tabids.put(item,tabid);
                tabsrepository.saveAndFlush(tab);
            }
            tabid ++;
            sequence ++;
        }
        sql = "delete from base_permissions where saasid = " + saasid;
        query = em.createNativeQuery(sql);
        query.executeUpdate();
        sql = "delete from base_tabs2permissions where saasid = " + saasid;
        query = em.createNativeQuery(sql);
        query.executeUpdate();

        Permissions permission = new Permissions();
        permission.setDeleted(0);
        permission.setSaasid(saasid);
        permission.setName("administrator");
        Example<Permissions> permissionexample = Example.of(permission);
        List<Permissions> permissions = permissionsrepository.findAll(permissionexample);
        if (permissions.size() == 0) {
            permission.setId(ContentUtils.makeContentId("base_permissions"));
            permission.setAuthor(author);
            permission.setDescription("");
            permission.setAllowdeleted(false);
            permission.setGlobalAllEdit(true);
            permission.setGlobalAllView(true);
            permission = permissionsrepository.saveAndFlush(permission);
            for (String item : tabsConfig) {
                Tabs2permissions tabs2permission = new Tabs2permissions();
                tabs2permission.setId(ContentUtils.makeContentId("base_tabs2permissions"));
                tabs2permission.setAuthor(author);
                tabs2permission.setDeleted(0);
                tabs2permission.setSaasid(saasid);
                tabs2permission.setPermissionid(permission.getId());
                tabs2permission.setTabid(tabids.get(item));
                tabs2permission.setAll(true);
                tabs2permission.setDelete(true);
                tabs2permission.setAdd(true);
                tabs2permission.setQuery(true);
                tabs2permission.setEdit(true);
                tabs2permissionsrepository.saveAndFlush(tabs2permission);
            }
        }
        else {
            permission = permissions.get(0);
        }


        Modentitynos modentityno = new Modentitynos();
        modentityno.setDeleted(0);
        modentityno.setSaasid(saasid);
        modentityno.setTabid(tabids.get("Users"));
        Example<Modentitynos> modentitynosexample = Example.of(modentityno);
        List<Modentitynos> modentitynos = modentitynosrepository.findAll(modentitynosexample);
        if (modentitynos.size() == 0) {
            modentityno.setId(ContentUtils.makeContentId("base_modentitynos"));
            modentityno.setAuthor(author);
            modentityno.setActive(true);
            modentityno.setCurId(1);
            modentityno.setIncludeDate(true);
            modentityno.setPrefix("USR");
            modentityno.setStartId(1);
            modentityno.setLength(3);
            modentityno.setCurDate(DateTimeUtils.getDatetime("yyMMdd"));
            modentitynosrepository.saveAndFlush(modentityno);
        }

        sql = "delete from base_depts where saasid = " + saasid;
        query = em.createNativeQuery(sql);
        query.executeUpdate();

        Depts dept = new Depts();
        dept.setDeleted(0);
        dept.setSaasid(saasid);
        dept.setDeptid("H1");
        Example<Depts> deptexample = Example.of(dept);
        List<Depts> depts = deptsrepository.findAll(deptexample);
        if (depts.size() == 0) {
            dept.setId(ContentUtils.makeContentId("base_depts"));
            dept.setAuthor(author);
            dept.setSaasid(saasid);
            dept.setName("ROOT");
            dept.setDepth(0);
            dept.setParentid("H1");
            dept.setSequence(1);
            dept = deptsrepository.saveAndFlush(dept);
        }
        else {
            dept = depts.get(0);
        }



        // 删除所有后台用户
        sql = "delete from base_users where saasid = " + saasid;
        query = em.createNativeQuery(sql);
        query.executeUpdate();

        Users user = new Users();
        user.setDeleted(0);
        user.setSaasid(saasid);
        user.setProfileid(profileid);
        Example<Users> userexample = Example.of(user);
        List<Users> users = usersrepository.findAll(userexample);

        if (users.size() == 0) {
            user.setAuthor(author);
            user.setId(ContentUtils.makeContentId("base_users"));
            user.setRegioncode(profile.getRegioncode());
            user.setMobile(profile.getMobile());
            user.setGivenname(profile.getGivenname());
            user.setEmail(profile.getEmail());
            user.setLink(profile.getLink());
            user.setGender(profile.getGender());
            user.setCountry(profile.getCountry());
            user.setRegion(profile.getRegion());
            user.setBirthdate(profile.getBirthdate());
            user.setProvince(profile.getProvince());
            user.setCity(profile.getCity());
            user.setRealname(profile.getRealname());
            user.setIdentitycard(profile.getIdentitycard());
            user.setSequence(100);
            user.setIsadmin(true);
            user.setStatus(true);
            user.setUsersNo("USR001");
            user.setDeptid(dept.getId());
            user.setPermissionid(permission.getId());
            usersrepository.saveAndFlush(user);
        }
    }
}