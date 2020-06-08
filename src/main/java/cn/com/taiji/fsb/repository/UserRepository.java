package cn.com.taiji.fsb.repository;


import cn.com.taiji.fsb.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    User findByloginname(String loginname);
}
