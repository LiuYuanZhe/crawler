package com.sdust.crawler.mapper;

import com.sdust.crawler.dto.PhonePriceDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PhonePriceMapper extends BaseMapper<PhonePriceDto> {

  List<PhonePriceDto> queryByConditions(PhonePriceDto phonePriceDto);

  void insertBatch(List<PhonePriceDto> phonePriceDtos);

}