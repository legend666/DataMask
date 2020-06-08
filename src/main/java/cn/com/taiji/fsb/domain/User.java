package cn.com.taiji.fsb.domain;


import cn.com.taiji.fsb.dto.UserDto;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

/**
 * @description : 用户实体
 */
@Data
@Entity
@Table(name = "SYSTEMUSERS")
public class User  implements UserDetails,Serializable{

    @Id
    private String userid;
    private String loginname;
    private String password;
    private String username;
    private String createtime;
    private String userrole;
//    @ManyToOne(fetch = FetchType.EAGER)
//    private Dept dept;
    public User(){

    }

    public User(UserDto u){
        BeanUtils.copyProperties(u,this);
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> list = new ArrayList<>();
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(userrole);
        list.add(grantedAuthority);
        return list;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
