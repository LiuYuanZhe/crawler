package com.sdust.crawler.mapper;

import com.sdust.crawler.dto.JobDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created by LiuYuanZhe on 18/4/24.
 */
@Mapper
public interface JobMapper extends BaseMapper<JobDto>{
    /**
     * insert joblist<jobdto>
     * @param jobDtos JobDto
     */
    public void insertBatch(List<JobDto> jobDtos);
}
