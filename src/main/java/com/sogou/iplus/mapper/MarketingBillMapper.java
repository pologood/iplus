package com.sogou.iplus.mapper;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.stereotype.Repository;

import com.sogou.iplus.entity.MarketingBill;

@Repository
public interface MarketingBillMapper {

  class Sql {

    public static final String TABLE = "`marketingBill`";

    public static final List<String> FIELDS = Arrays.asList("interfaceKeyWord", "batchSource", "businessUnit",
        "invoice", "invoiceDate", "accountDate", "currency", "transactionType", "saler", "orderReceiver",
        "orderReceiverLocation", "goodsReceiver", "goodsReceiverLocation", "userExchangeRate", "exchangeRateDate",
        "exchangeRate", "restricts", "reference", "transactionOperation", "orderExecutionId", "orderAmount",
        "orderDiscountRate", "orderExecutionStartDate", "orderExecutionEndDate", "isExpired", "orderReceiveRange",
        "orderType", "orderNo", "industry", "accountAgeGLDate", "accountAgeTransactionOperationDate", "actualInvoiceNo",
        "notice", "channelSaler", "channelSalerId", "directSaler", "directSalerId", "column", "transactionAmount",
        "orderCount", "price", "noticeAbstract", "taxAmount", "taxCode", "costAndTax", "cost", "tax", "cultureTax");

    public static final List<String> SAFE_FIELDS = FIELDS.stream().map(s -> String.format("`%s`", s))
        .collect(Collectors.toList());

    public static String add(Map<String, Object> map) {
      return String.format("insert into %s (%s) values %s", TABLE, String.join(", ", SAFE_FIELDS),
          String.join(", ", getValues(((List<?>) map.get("list")).size())));
    }

    private static List<String> getValues(int size) {
      return IntStream.range(0, size)
          .mapToObj(i -> String.join(", ",
              FIELDS.stream().map(field -> String.format("#{list[%d].%s}", i, field)).collect(Collectors.toList())))
          .map(values -> "(" + values + ")").collect(Collectors.toList());
    }

    public static String get() {
      return new SQL().SELECT("*").FROM(TABLE).toString();
    }

  }

  @InsertProvider(type = Sql.class, method = "add")
  public void add(@Param("list") List<MarketingBill> list);

  @SelectProvider(type = Sql.class, method = "get")
  public List<MarketingBill> get();

}
