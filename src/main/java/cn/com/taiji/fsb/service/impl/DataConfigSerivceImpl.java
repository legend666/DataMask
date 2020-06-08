package cn.com.taiji.fsb.service.impl;

import cn.com.taiji.fsb.domain.DataConfig;
import cn.com.taiji.fsb.dto.DataConfigDto;
import cn.com.taiji.fsb.repository.DataConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataConfigSerivceImpl {

    @Autowired
    private DataConfigRepository dataConfigRepository;
    public void save(DataConfigDto dto){
        DataConfig config = new DataConfig(dto);
        dataConfigRepository.save(config);
    }

    public List<DataConfig> getAllConnect(){
        return dataConfigRepository.findAll();
    }

    public DataConfig getDataConfigById(String dataConfigId){
        return dataConfigRepository.findOne(dataConfigId);
    }
}
