package com.sogou.iplus.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.validation.constraints.NotNull;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

@ApiObject(name = "MarketingBill", description = "营销记账")
public class MarketingBill {
  @NotNull
  @ApiObjectField(name = "interfaceKeyWord", description = "接口关键字", order = 1, required = true)
  private String interfaceKeyWord;
  @NotNull
  @ApiObjectField(name = "batchSource", description = "批来源", order = 2, required = true)
  private String batchSource;
  @NotNull
  @ApiObjectField(name = "businessUnit", description = "业务实体名称", order = 3, required = true)
  private String businessUnit;
  @NotNull
  @ApiObjectField(name = "invoice", description = "发票编号", order = 4, required = true)
  private String invoice;
  @NotNull
  @ApiObjectField(name = "invoiceDate", description = "发票日期", order = 5, required = true, format = "yyyy-MM-dd")
  private LocalDate invoiceDate;
  @NotNull
  @ApiObjectField(name = "accountDate", description = "记账日期", order = 6, required = true, format = "yyyy-MM-dd")
  private LocalDate accountDate;
  @NotNull
  @ApiObjectField(name = "currency", description = "币种", order = 7, required = true)
  private String currency;
  @NotNull
  @ApiObjectField(name = "transactionType", description = "事务处理类型", order = 8, required = true)
  private String transactionType;
  @NotNull
  @ApiObjectField(name = "saler", description = "销售人员", order = 9, required = true)
  private String saler;
  @NotNull
  @ApiObjectField(name = "orderReceiver", description = "收单方客户名称", order = 10, required = true)
  private String orderReceiver;
  @NotNull
  @ApiObjectField(name = "orderReceiverLocation", description = "收单方客户地点", order = 11, required = true)
  private String orderReceiverLocation;
  @NotNull
  @ApiObjectField(name = "goodsReceiver", description = "收获方客户名称", order = 12, required = true)
  private String goodsReceiver;
  @NotNull
  @ApiObjectField(name = "goodsReceiverLocation", description = "收货方客户地点", order = 13, required = true)
  private String goodsReceiverLocation;
  @NotNull
  @ApiObjectField(name = "userExchangeRate", description = "用户汇率", order = 14, required = true)
  private BigDecimal userExchangeRate;
  @NotNull
  @ApiObjectField(name = "exchangeRateDate", description = "汇率日期", order = 15, required = true, format = "yyyy-MM-dd")
  private LocalDate exchangeRateDate;
  @NotNull
  @ApiObjectField(name = "exchangeRate", description = "汇率", order = 16, required = true)
  private BigDecimal exchangeRate;
  @NotNull
  @ApiObjectField(name = "restricts", description = "付款条件", order = 17, required = true)
  private String restricts;
  @NotNull
  @ApiObjectField(name = "reference", description = "参考", order = 18, required = true)
  private String reference;
  @NotNull
  @ApiObjectField(name = "transactionOperation", description = "事务处理说明", order = 19, required = true)
  private String transactionOperation;
  @NotNull
  @ApiObjectField(name = "orderExecutionId", description = "合同执行单号", order = 20, required = true)
  private String orderExecutionId;
  @NotNull
  @ApiObjectField(name = "orderAmount", description = "合同金额", order = 21, required = true)
  private BigDecimal orderAmount;
  @NotNull
  @ApiObjectField(name = "orderDiscountRate", description = "合同折扣率", order = 22, required = true)
  private BigDecimal orderDiscountRate;
  @NotNull
  @ApiObjectField(name = "orderExecutionStartDate", description = "合同执行开始日期", order = 23, required = true, format = "yyyy-MM-dd")
  private LocalDate orderExecutionStartDate;
  @NotNull
  @ApiObjectField(name = "orderExecutionEndDate", description = "合同执行结束日期", order = 24, required = true, format = "yyyy-MM-dd")
  private LocalDate orderExecutionEndDate;
  @NotNull
  @ApiObjectField(name = "isExpired", description = "合同是否跨期", order = 25, required = true)
  private Integer isExpired;
  @NotNull
  @ApiObjectField(name = "orderReceiveRange", description = "合同收到期间", order = 26, required = true)
  private String orderReceiveRange;
  @NotNull
  @ApiObjectField(name = "orderType", description = "合同类型", order = 27, required = true)
  private String orderType;
  @NotNull
  @ApiObjectField(name = "orderNo", description = "合同号", order = 28, required = true)
  private String orderNo;
  @NotNull
  @ApiObjectField(name = "industry", description = "大行业", order = 29, required = true)
  private String industry;
  @NotNull
  @ApiObjectField(name = "accountAgeGLDate", description = "账龄GL日期", order = 30, required = true, format = "yyyy-MM-dd")
  private LocalDate accountAgeGLDate;
  @NotNull
  @ApiObjectField(name = "accountAgeTransactionOperationDate", description = "账龄事务处理日期", order = 31, required = true, format = "yyyy-MM-dd")
  private LocalDate accountAgeTransactionOperationDate;
  @NotNull
  @ApiObjectField(name = "actualInvoiceNo", description = "实际发票编号", order = 32, required = true)
  private String actualInvoiceNo;
  @NotNull
  @ApiObjectField(name = "notice", description = "贷项通知单核销发票", order = 33, required = true)
  private String notice;
  @NotNull
  @ApiObjectField(name = "channelSaler", description = "渠道销售人员", order = 34, required = true)
  private String channelSaler;
  @NotNull
  @ApiObjectField(name = "channelSalerId", description = "渠道销售人员编号", order = 35, required = true)
  private String channelSalerId;
  @NotNull
  @ApiObjectField(name = "directSaler", description = "直客销售人员", order = 36, required = true)
  private String directSaler;
  @NotNull
  @ApiObjectField(name = "directSalerId", description = "直客销售人员编号", order = 37, required = true)
  private String directSalerId;
  @NotNull
  @ApiObjectField(name = "column", description = "行号", order = 38, required = true)
  private String column;
  @NotNull
  @ApiObjectField(name = "transactionAmount", description = "事务处理行金额", order = 39, required = true)
  private BigDecimal transactionAmount;
  @NotNull
  @ApiObjectField(name = "orderCount", description = "订单数量", order = 40, required = true)
  private Integer orderCount;
  @NotNull
  @ApiObjectField(name = "price", description = "价格", order = 41, required = true)
  private BigDecimal price;
  @NotNull
  @ApiObjectField(name = "noticeAbstract", description = "通知单行摘要", order = 42, required = true)
  private String noticeAbstract;
  @NotNull
  @ApiObjectField(name = "taxAmount", description = "税额", order = 43, required = true)
  private BigDecimal taxAmount;
  @NotNull
  @ApiObjectField(name = "taxCode", description = "税码", order = 44, required = true)
  private String taxCode;
  @NotNull
  @ApiObjectField(name = "costAndTax", description = "价税合计", order = 45, required = true)
  private BigDecimal costAndTax;
  @NotNull
  @ApiObjectField(name = "cost", description = "价", order = 46, required = true)
  private BigDecimal cost;
  @NotNull
  @ApiObjectField(name = "tax", description = "税", order = 47, required = true)
  private BigDecimal tax;
  @NotNull
  @ApiObjectField(name = "cultureTax", description = "文化事业建设税", order = 48, required = true)
  private BigDecimal cultureTax;

  public String getInterfaceKeyWord() {
    return interfaceKeyWord;
  }

  public void setInterfaceKeyWord(String interfaceKeyWord) {
    this.interfaceKeyWord = interfaceKeyWord;
  }

  public String getBatchSource() {
    return batchSource;
  }

  public void setBatchSource(String batchSource) {
    this.batchSource = batchSource;
  }

  public String getBusinessUnit() {
    return businessUnit;
  }

  public void setBusinessUnit(String businessUnit) {
    this.businessUnit = businessUnit;
  }

  public String getInvoice() {
    return invoice;
  }

  public void setInvoice(String invoice) {
    this.invoice = invoice;
  }

  public LocalDate getInvoiceDate() {
    return invoiceDate;
  }

  public void setInvoiceDate(LocalDate invoiceDate) {
    this.invoiceDate = invoiceDate;
  }

  public LocalDate getAccountDate() {
    return accountDate;
  }

  public void setAccountDate(LocalDate accountDate) {
    this.accountDate = accountDate;
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public String getTransactionType() {
    return transactionType;
  }

  public void setTransactionType(String transactionType) {
    this.transactionType = transactionType;
  }

  public String getSaler() {
    return saler;
  }

  public void setSaler(String saler) {
    this.saler = saler;
  }

  public String getOrderReceiver() {
    return orderReceiver;
  }

  public void setOrderReceiver(String orderReceiver) {
    this.orderReceiver = orderReceiver;
  }

  public String getOrderReceiverLocation() {
    return orderReceiverLocation;
  }

  public void setOrderReceiverLocation(String orderReceiverLocation) {
    this.orderReceiverLocation = orderReceiverLocation;
  }

  public String getGoodsReceiver() {
    return goodsReceiver;
  }

  public void setGoodsReceiver(String goodsReceiver) {
    this.goodsReceiver = goodsReceiver;
  }

  public String getGoodsReceiverLocation() {
    return goodsReceiverLocation;
  }

  public void setGoodsReceiverLocation(String goodsReceiverLocation) {
    this.goodsReceiverLocation = goodsReceiverLocation;
  }

  public BigDecimal getUserExchangeRate() {
    return userExchangeRate;
  }

  public void setUserExchangeRate(BigDecimal userExchangeRate) {
    this.userExchangeRate = userExchangeRate;
  }

  public LocalDate getExchangeRateDate() {
    return exchangeRateDate;
  }

  public void setExchangeRateDate(LocalDate exchangeRateDate) {
    this.exchangeRateDate = exchangeRateDate;
  }

  public BigDecimal getExchangeRate() {
    return exchangeRate;
  }

  public void setExchangeRate(BigDecimal exchangeRate) {
    this.exchangeRate = exchangeRate;
  }

  public String getRestricts() {
    return restricts;
  }

  public void setRestricts(String restricts) {
    this.restricts = restricts;
  }

  public String getReference() {
    return reference;
  }

  public void setReference(String reference) {
    this.reference = reference;
  }

  public String getTransactionOperation() {
    return transactionOperation;
  }

  public void setTransactionOperation(String transactionOperation) {
    this.transactionOperation = transactionOperation;
  }

  public String getOrderExecutionId() {
    return orderExecutionId;
  }

  public void setOrderExecutionId(String orderExecutionId) {
    this.orderExecutionId = orderExecutionId;
  }

  public BigDecimal getOrderAmount() {
    return orderAmount;
  }

  public void setOrderAmount(BigDecimal orderAmount) {
    this.orderAmount = orderAmount;
  }

  public BigDecimal getOrderDiscountRate() {
    return orderDiscountRate;
  }

  public void setOrderDiscountRate(BigDecimal orderDiscountRate) {
    this.orderDiscountRate = orderDiscountRate;
  }

  public LocalDate getOrderExecutionStartDate() {
    return orderExecutionStartDate;
  }

  public void setOrderExecutionStartDate(LocalDate orderExecutionStartDate) {
    this.orderExecutionStartDate = orderExecutionStartDate;
  }

  public LocalDate getOrderExecutionEndDate() {
    return orderExecutionEndDate;
  }

  public void setOrderExecutionEndDate(LocalDate orderExecutionEndDate) {
    this.orderExecutionEndDate = orderExecutionEndDate;
  }

  public Integer getIsExpired() {
    return isExpired;
  }

  public void setIsExpired(Integer isExpired) {
    this.isExpired = isExpired;
  }

  public String getOrderReceiveRange() {
    return orderReceiveRange;
  }

  public void setOrderReceiveRange(String orderReceiveRange) {
    this.orderReceiveRange = orderReceiveRange;
  }

  public String getOrderType() {
    return orderType;
  }

  public void setOrderType(String orderType) {
    this.orderType = orderType;
  }

  public String getOrderNo() {
    return orderNo;
  }

  public void setOrderNo(String orderNo) {
    this.orderNo = orderNo;
  }

  public String getIndustry() {
    return industry;
  }

  public void setIndustry(String industry) {
    this.industry = industry;
  }

  public LocalDate getAccountAgeGLDate() {
    return accountAgeGLDate;
  }

  public void setAccountAgeGLDate(LocalDate accountAgeGLDate) {
    this.accountAgeGLDate = accountAgeGLDate;
  }

  public LocalDate getAccountAgeTransactionOperationDate() {
    return accountAgeTransactionOperationDate;
  }

  public void setAccountAgeTransactionOperationDate(LocalDate accountAgeTransactionOperationDate) {
    this.accountAgeTransactionOperationDate = accountAgeTransactionOperationDate;
  }

  public String getActualInvoiceNo() {
    return actualInvoiceNo;
  }

  public void setActualInvoiceNo(String actualInvoiceNo) {
    this.actualInvoiceNo = actualInvoiceNo;
  }

  public String getNotice() {
    return notice;
  }

  public void setNotice(String notice) {
    this.notice = notice;
  }

  public String getChannelSaler() {
    return channelSaler;
  }

  public void setChannelSaler(String channelSaler) {
    this.channelSaler = channelSaler;
  }

  public String getChannelSalerId() {
    return channelSalerId;
  }

  public void setChannelSalerId(String channelSalerId) {
    this.channelSalerId = channelSalerId;
  }

  public String getDirectSaler() {
    return directSaler;
  }

  public void setDirectSaler(String directSaler) {
    this.directSaler = directSaler;
  }

  public String getDirectSalerId() {
    return directSalerId;
  }

  public void setDirectSalerId(String directSalerId) {
    this.directSalerId = directSalerId;
  }

  public String getColumn() {
    return column;
  }

  public void setColumn(String column) {
    this.column = column;
  }

  public BigDecimal getTransactionAmount() {
    return transactionAmount;
  }

  public void setTransactionAmount(BigDecimal transactionAmount) {
    this.transactionAmount = transactionAmount;
  }

  public Integer getOrderCount() {
    return orderCount;
  }

  public void setOrderCount(Integer orderCount) {
    this.orderCount = orderCount;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  public String getNoticeAbstract() {
    return noticeAbstract;
  }

  public void setNoticeAbstract(String noticeAbstract) {
    this.noticeAbstract = noticeAbstract;
  }

  public BigDecimal getTaxAmount() {
    return taxAmount;
  }

  public void setTaxAmount(BigDecimal taxAmount) {
    this.taxAmount = taxAmount;
  }

  public String getTaxCode() {
    return taxCode;
  }

  public void setTaxCode(String taxCode) {
    this.taxCode = taxCode;
  }

  public BigDecimal getCostAndTax() {
    return costAndTax;
  }

  public void setCostAndTax(BigDecimal costAndTax) {
    this.costAndTax = costAndTax;
  }

  public BigDecimal getCost() {
    return cost;
  }

  public void setCost(BigDecimal cost) {
    this.cost = cost;
  }

  public BigDecimal getTax() {
    return tax;
  }

  public void setTax(BigDecimal tax) {
    this.tax = tax;
  }

  public BigDecimal getCultureTax() {
    return cultureTax;
  }

  public void setCultureTax(BigDecimal cultureTax) {
    this.cultureTax = cultureTax;
  }

}
