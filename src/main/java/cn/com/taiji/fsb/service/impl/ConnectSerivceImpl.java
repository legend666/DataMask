package cn.com.taiji.fsb.service.impl;

import cn.com.taiji.fsb.domain.Connect;
import cn.com.taiji.fsb.dto.ConnectDto;
import cn.com.taiji.fsb.repository.ConnectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConnectSerivceImpl {

    @Autowired
    private ConnectRepository connectRepository;
    public void save(ConnectDto dto){
        Connect con = new Connect(dto);
        connectRepository.save(con);
    }

    public List<Connect> getAllConnect(){
        return connectRepository.findAll();
    }
    public void deleteConnectById(String connectionId){
        connectRepository.delete(connectionId);
    }

    public Connect getConnectById(String connectionId){
        return connectRepository.findOne(connectionId);
    }
}
