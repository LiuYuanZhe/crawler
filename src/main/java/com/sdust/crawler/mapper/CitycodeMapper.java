package com.sdust.crawler.mapper;

import com.sdust.crawler.dto.CitycodeDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created by LiuYuanZhe on 18/5/15.
 */
@Mapper
public interface CitycodeMapper {
    /**
     * 插入citycode
     * @param list
     */
    public void insertBatchCity(List<CitycodeDto> list);

    /**
     * 根据传入的num查询citycode
     * @param num
     * @return
     */
    public List<String> queryCitycodeBynum(int num);
}
