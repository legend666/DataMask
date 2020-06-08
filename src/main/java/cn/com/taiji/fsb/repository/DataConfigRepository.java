package cn.com.taiji.fsb.repository;

import cn.com.taiji.fsb.domain.DataConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DataConfigRepository extends JpaRepository<DataConfig, String> {
}