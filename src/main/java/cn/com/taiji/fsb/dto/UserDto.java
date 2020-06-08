package cn.com.taiji.fsb.dto;

import lombok.Data;
import org.springframework.beans.BeanUtils;
import cn.com.taiji.fsb.domain.User;

import java.io.Serializable;

@Data
public class UserDto implements Serializable {
    private String userid;
    private String loginname;
    private String password;
    private String username;
    private String createtime;
    private String userrole;

    public UserDto() {

    }

    public UserDto(User u) {
        BeanUtils.copyProperties(u, this);
    }
}
