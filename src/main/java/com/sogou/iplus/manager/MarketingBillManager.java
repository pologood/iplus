package com.sogou.iplus.manager;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sogou.iplus.entity.MarketingBill;
import com.sogou.iplus.mapper.MarketingBillMapper;
import com.sogou.iplus.model.ApiResult;

@Service
public class MarketingBillManager {

  private @Autowired MarketingBillMapper mapper;

  private static final Logger LOGGER = LoggerFactory.getLogger(MarketingBillManager.class);

  public ApiResult<?> add(List<MarketingBill> list) {
    LOGGER.info("add list {}", list);
    mapper.add(list);
    return ApiResult.ok();
  }

  public ApiResult<?> get() {
    return new ApiResult<>(mapper.get());
  }

}
