package com.xxxx.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xxxx.api.entity.Spikebook;
import com.xxxx.api.entity.dto.spikebookDto;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author write by zjy
 * @since 2021-04-30
 */
public interface SpikebookService extends IService<Spikebook> {
    void setSpikeBook();
    String insertOrder(String username,String bookname);

    List<spikebookDto> getSpike_Book();

    List<spikebookDto> getSpike_Book_Main(LocalDateTime middle);
}
