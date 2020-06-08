package cn.com.taiji.fsb.repository;


import cn.com.taiji.fsb.domain.Connect;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConnectRepository extends JpaRepository<Connect, String> {


}
