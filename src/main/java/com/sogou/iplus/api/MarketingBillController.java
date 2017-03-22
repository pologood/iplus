package com.sogou.iplus.api;

import java.util.List;

import javax.validation.Valid;

import org.jsondoc.core.annotation.Api;
import org.jsondoc.core.annotation.ApiBodyObject;
import org.jsondoc.core.annotation.ApiMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sogou.iplus.entity.MarketingBill;
import com.sogou.iplus.manager.MarketingBillManager;
import com.sogou.iplus.model.ApiResult;

@Api(name = "营销记账 API", description = "Read/Write/Update/Delete 营销记账记录")
@RestController
@RequestMapping("/api")
public class MarketingBillController {

  private @Autowired MarketingBillManager marketingBillManager;

  @ApiMethod(description = "add record")
  @RequestMapping(value = "/bill", method = RequestMethod.POST, consumes = { "application/json" })
  public ApiResult<?> add(@ApiBodyObject @RequestBody @Valid List<MarketingBill> list) {
    return marketingBillManager.add(list);
  }

  @ApiMethod(description = "get record")
  @RequestMapping(value = "/bill", method = RequestMethod.GET)
  public ApiResult<?> get() {
    return marketingBillManager.get();
  }

}
