package com.github.cores.service.impl;

import com.github.cores.domain.Parenttabs;
import com.github.cores.domain.Tabs;
import com.github.cores.domain.Tabs2permissions;
import com.github.cores.domain.Users;
import com.github.cores.repository.ParenttabsRepository;
import com.github.cores.repository.Tabs2permissionsRepository;
import com.github.cores.repository.UsersRepository;
import com.github.exception.EntityExistException;
import com.github.utils.ValidationUtil;
import com.github.utils.FileUtil;
import com.github.cores.repository.TabsRepository;
import com.github.cores.service.TabsService;
import com.github.cores.service.dto.TabsDTO;
import com.github.cores.service.dto.TabsQueryCriteria;
import com.github.cores.service.mapper.TabsMapper;
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
import com.github.utils.PageUtil;
import com.github.utils.QueryHelp;

import java.util.*;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @author oldhand
* @date 2020-01-14
*/
@Service
@CacheConfig(cacheNames = "Tabs")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class TabsServiceImpl implements TabsService {
    private final ParenttabsRepository parenttabsrepository;
    private final TabsRepository TabsRepository;
    private final Tabs2permissionsRepository tabs2permissionsrepository;
    private final UsersRepository usersrepository;
    private final TabsMapper TabsMapper;

    public TabsServiceImpl(TabsRepository TabsRepository, TabsMapper TabsMapper,Tabs2permissionsRepository tabs2permissionsrepository,UsersRepository usersrepository,ParenttabsRepository parenttabsrepository) {
        this.TabsRepository = TabsRepository;
        this.tabs2permissionsrepository = tabs2permissionsrepository;
        this.usersrepository = usersrepository;
        this.parenttabsrepository = parenttabsrepository;
        this.TabsMapper = TabsMapper;
    }

    @Override
    @Cacheable
    public Map<String,Object> queryAll(TabsQueryCriteria criteria, Pageable pageable){
        Page<Tabs> page = TabsRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(TabsMapper::toDto));
    }

    @Override
    @Cacheable
    public List<TabsDTO> queryAll(TabsQueryCriteria criteria){
        return TabsMapper.toDto(TabsRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Cacheable(key = "#p0")
    public TabsDTO findById(Long id) {
        Tabs Tabs = TabsRepository.findById(id).orElseGet(Tabs::new);
        ValidationUtil.isNull(Tabs.getId(),"Tabs","id",id);
        return TabsMapper.toDto(Tabs);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public TabsDTO create(Tabs resources) {
        return TabsMapper.toDto(TabsRepository.saveAndFlush(resources));
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public TabsDTO update(Long id,Tabs resources) {
        Tabs Tabs = TabsRepository.findById(id).orElseGet(Tabs::new);
        ValidationUtil.isNull( Tabs.getId(),"Tabs","id",resources.getId());
        Tabs.copy(resources);
		return TabsMapper.toDto(TabsRepository.saveAndFlush(Tabs));
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        TabsRepository.deleteById(id);
    }
	
    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void makedelete(Long id) {
         Tabs Tabs = TabsRepository.findById(id).orElseGet(Tabs::new);
		 ValidationUtil.isNull( Tabs.getId(),"Tabs","id",id);
		 Tabs.setDeleted(1);
		 TabsRepository.save(Tabs);
    }


    @Override
    public void download(List<TabsDTO> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (TabsDTO Tabs : all) {
            Map<String,Object> map = new LinkedHashMap<>();
		    map.put("ID", Tabs.getId());
		    map.put("创建日期", Tabs.getPublished());
		    map.put("更新日期", Tabs.getUpdated());
		    map.put("创建者", Tabs.getAuthor());
		    map.put("删除标记", Tabs.getDeleted());
            map.put("saasid",  Tabs.getSaasid());
            map.put("模块名称", Tabs.getTabname());
            map.put("模块标签", Tabs.getTablabel());
            map.put("排序号", Tabs.getSequence());
            map.put("模块ID", Tabs.getTabid());
            map.put("图标", Tabs.getIcon());
            map.put("存储类型", Tabs.getDatatype());
            map.put("是否可见", Tabs.getPresence());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    @Cacheable(key = "T(String).valueOf('menus::').concat(#p1)")
    public List<Object> buildMemus(long saasid, String profileid) {
        List<Object> menus = new ArrayList();
        if (saasid > 0) {
            final Users user = usersrepository.findByProfileid(profileid);
            if (user != null) {
                long premissionid = user.getPermissionid();

                Parenttabs parenttab = new Parenttabs();
                parenttab.setDeleted(0);
                parenttab.setSaasid(saasid);
                parenttab.setTabname("Settings");
                Example<Parenttabs> example = Example.of(parenttab);
                List<Parenttabs> parenttabs = parenttabsrepository.findAll(example);

                Tabs tab = new Tabs();
                tab.setDeleted(0);
                tab.setSaasid(saasid);
                Example<Tabs> tabexample = Example.of(tab);
                List<Tabs> tabs = TabsRepository.findAll(tabexample);

                Tabs2permissions tabs2permission = new Tabs2permissions();
                tabs2permission.setDeleted(0);
                tabs2permission.setSaasid(saasid);
                tabs2permission.setPermissionid(premissionid);
                Example<Tabs2permissions> tabs2permissionexample = Example.of(tabs2permission);
                List<Tabs2permissions> tabs2permissions = tabs2permissionsrepository.findAll(tabs2permissionexample);
                List<Integer> showtabs = new ArrayList();
                for (Tabs2permissions permission : tabs2permissions) {
                    int tabid = permission.getTabid();
                    if (permission.getAll()) {
                        showtabs.add(tabid);
                    } else if (permission.getEdit()) {
                        showtabs.add(tabid);
                    } else if (permission.getQuery()) {
                        showtabs.add(tabid);
                    }
                }
                for (Parenttabs parenttab_item : parenttabs) {
                    String parenttabname = parenttab_item.getTabname();
                    Map<String,Object> parenttab_menu = new HashMap<>();


                    parenttab_menu.put("name",parenttabname);
                    parenttab_menu.put("path","/" + parenttabname.toLowerCase());
                    parenttab_menu.put("hidden",false);
                    parenttab_menu.put("redirect","noredirect");
                    parenttab_menu.put("component","Layout");
                    parenttab_menu.put("alwaysShow",true);

                    Map<String,Object> meta = new HashMap<>();
                    meta.put("title",parenttab_item.getTablabel());
                    meta.put("icon",parenttabname.toLowerCase());
                    meta.put("noCache",true);
                    parenttab_menu.put("meta",meta);

                    List<Object> childrenmenus = new ArrayList();
                    for (Tabs tab_item : tabs) {
                        if (showtabs.indexOf(tab_item.getTabid()) >= 0) {
                            String tabname = tab_item.getTabname();
                            Map<String,Object> tab_menu = new HashMap<>();
                            tab_menu.put("name",tabname);
                            tab_menu.put("path",tabname.toLowerCase());
                            tab_menu.put("hidden",false);
                            tab_menu.put("redirect","noredirect");
                            tab_menu.put("component",parenttabname.toLowerCase()+"/"+tabname.toLowerCase()+"/index");

                            Map<String,Object> childmeta = new HashMap<>();
                            childmeta.put("title",tab_item.getTablabel());
                            childmeta.put("icon",tabname.toLowerCase());
                            childmeta.put("noCache",true);
                            tab_menu.put("meta",childmeta);

                            childrenmenus.add(tab_menu);
                        }
                    }
                    parenttab_menu.put("children",childrenmenus);
                    menus.add(parenttab_menu);
                }

            }
        }
        return menus;
    }
}