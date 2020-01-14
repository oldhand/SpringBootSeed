package com.github.cores.service.impl;

import com.github.cores.domain.Users;
import com.github.exception.EntityExistException;
import com.github.exception.EntityExistException;
import com.github.utils.ValidationUtil;
import com.github.utils.FileUtil;
import com.github.cores.repository.UsersRepository;
import com.github.cores.service.UsersService;
import com.github.cores.service.dto.UsersDTO;
import com.github.cores.service.dto.UsersQueryCriteria;
import com.github.cores.service.mapper.UsersMapper;
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
import java.util.List;
import java.util.Map;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
* @author oldhand
* @date 2020-01-02
*/
@Service
@CacheConfig(cacheNames = "Users")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class UsersServiceImpl implements UsersService {

    private final UsersRepository UsersRepository;

    private final UsersMapper UsersMapper;

    public UsersServiceImpl(UsersRepository UsersRepository, UsersMapper UsersMapper) {
        this.UsersRepository = UsersRepository;
        this.UsersMapper = UsersMapper;
    }

    @Override
    @Cacheable
    public Map<String,Object> queryAll(UsersQueryCriteria criteria, Pageable pageable){
        Page<Users> page = UsersRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(UsersMapper::toDto));
    }

    @Override
    @Cacheable
    public List<UsersDTO> queryAll(UsersQueryCriteria criteria){
        return UsersMapper.toDto(UsersRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Cacheable(key = "#p0")
    public UsersDTO findById(Long id) {
        Users Users = UsersRepository.findById(id).orElseGet(Users::new);
        ValidationUtil.isNull(Users.getId(),"Users","id",id);
        return UsersMapper.toDto(Users);
    }

    @Override
    @Cacheable(key = "#p0")
    public UsersDTO findByProfileid(String profileid) {
        Users Users = UsersRepository.findByProfileid(profileid);
        ValidationUtil.isNull(Users.getProfileid(),"Users","profileid",profileid);
        return UsersMapper.toDto(Users);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public UsersDTO create(Users resources) {
        if(UsersRepository.findByProfileid(resources.getProfileid()) != null){
            throw new EntityExistException(Users.class,"profileid",resources.getProfileid());
        }
        return UsersMapper.toDto(UsersRepository.saveAndFlush(resources));
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public UsersDTO update(Long id,Users resources) {
        Users Users = UsersRepository.findById(id).orElseGet(Users::new);
        ValidationUtil.isNull( Users.getId(),"Users","id",resources.getId());
        Users Users_profileid = UsersRepository.findByProfileid(resources.getProfileid());
        if(Users_profileid != null && !Users_profileid.getId().equals(Users.getId())){
            throw new EntityExistException(Users.class,"profileid",resources.getProfileid());
        }
        Users.copy(resources);
		return UsersMapper.toDto(UsersRepository.saveAndFlush(Users));
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        UsersRepository.deleteById(id);
    }
	
    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void makedelete(Long id) {
         Users Users = UsersRepository.findById(id).orElseGet(Users::new);
		 ValidationUtil.isNull( Users.getId(),"Users","id",id);
		 Users.setDeleted(1);
		 UsersRepository.save(Users);
    }


    @Override
    public void download(List<UsersDTO> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (UsersDTO Users : all) {
            Map<String,Object> map = new LinkedHashMap<>();
		    map.put("ID", Users.getId());
		    map.put("创建日期", Users.getPublished());
		    map.put("更新日期", Users.getUpdated());
		    map.put("创建者", Users.getAuthor());
		    map.put("删除标记", Users.getDeleted());
            map.put("用户ID", Users.getProfileid());
            map.put("编号", Users.getUsersNo());
            map.put("排序号", Users.getSequence());
            map.put("国家代码", Users.getRegioncode());
            map.put("手机号码", Users.getMobile());
            map.put("昵称", Users.getGivenname());
            map.put("用户状态", Users.getStatus());
            map.put("用户邮箱", Users.getEmail());
            map.put("用户头像地址", Users.getLink());
            map.put("性别", Users.getGender());
            map.put("所属国家", Users.getCountry());
            map.put("地区", Users.getRegion());
            map.put("出生日期", Users.getBirthdate());
            map.put("省份", Users.getProvince());
            map.put("城市", Users.getCity());
            map.put("真实姓名", Users.getRealname());
            map.put("身份证号码", Users.getIdentitycard());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}