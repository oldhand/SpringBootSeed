package com.github.samples.service.impl;

import com.github.samples.domain.User;
import com.github.exception.EntityExistException;
import com.github.utils.ValidationUtil;
import com.github.utils.FileUtil;
import com.github.samples.repository.UserRepository;
import com.github.samples.service.UserService;
import com.github.samples.service.dto.UserDTO;
import com.github.samples.service.dto.UserQueryCriteria;
import com.github.samples.service.mapper.UserMapper;
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
* @date 2019-12-19
*/
@Service
@CacheConfig(cacheNames = "user")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    @Cacheable
    public Map<String,Object> queryAll(UserQueryCriteria criteria, Pageable pageable){
        Page<User> page = userRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(userMapper::toDto));
    }

    @Override
    @Cacheable
    public List<UserDTO> queryAll(UserQueryCriteria criteria){
        return userMapper.toDto(userRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Cacheable(key = "#p0")
    public UserDTO findById(Long id) {
        User user = userRepository.findById(id).orElseGet(User::new);
        ValidationUtil.isNull(user.getId(),"User","id",id);
        return userMapper.toDto(user);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public UserDTO create(User resources) {
        if(userRepository.findByUsername(resources.getUsername()) != null){
            throw new EntityExistException(User.class,"username",resources.getUsername());
        }
        return userMapper.toDto(userRepository.save(resources));
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(User resources) {
        User user = userRepository.findById(resources.getId()).orElseGet(User::new);
        ValidationUtil.isNull( user.getId(),"User","id",resources.getId());
        user = userRepository.findByUsername(resources.getUsername());
        if(user != null && !user.getId().equals(user.getId())){
            throw new EntityExistException(User.class,"username",resources.getUsername());
        }
        user.copy(resources);
        userRepository.save(user);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        userRepository.deleteById(id);
    }


    @Override
    public void download(List<UserDTO> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (UserDTO user : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("创建时间", user.getCreateTime());
            map.put("email",  user.getEmail());
            map.put("enabled",  user.getEnabled());
            map.put("lastPasswordResetTime",  user.getLastPasswordResetTime());
            map.put("password",  user.getPassword());
            map.put("phone",  user.getPhone());
            map.put("用户名", user.getUsername());
            map.put("deptId",  user.getDeptId());
            map.put("jobId",  user.getJobId());
            map.put("avatarId",  user.getAvatarId());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}