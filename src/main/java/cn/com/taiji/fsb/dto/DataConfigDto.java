package cn.com.taiji.fsb.dto;

import cn.com.taiji.fsb.domain.DataConfig;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;

@Data
public class DataConfigDto implements Serializable {
    private String configid;
    private String configname;
    private String configtype;
    private String configcontent;
    private String userid;
    private String createtime;
    private String connectionid;

    public DataConfigDto() {

    }

    public DataConfigDto(DataConfig u) {
        BeanUtils.copyProperties(u, this);
    }
}
