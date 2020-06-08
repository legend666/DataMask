package cn.com.taiji.fsb.service.impl;

import cn.com.taiji.fsb.domain.User;
import cn.com.taiji.fsb.dto.UserDto;
import cn.com.taiji.fsb.repository.UserRepository;
import cn.com.taiji.fsb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserSerivceImpl implements UserService {


    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user =  userRepository.findByloginname(s);
        if(user == null){
            throw new UsernameNotFoundException("用户不存在");
        }
        return user;
    }

    public void save(UserDto dto){
        User u = new User(dto);
        userRepository.save(u);
    }

    /**
     * 获取所有用户
     * @return
     */
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public void deleteUserByUserId(String userId){
        userRepository.delete(userId);
    }

    public User getUserById(String userId){
        return userRepository.findOne(userId);
    }
}
