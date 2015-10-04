package com.software_force.myinvoice.models;

import java.math.BigDecimal;

/**
 * Created by Matthew So on 10/4/2015.
 */
public class InvoiceLine {
    private long invoiceId ;
    private Integer seq ;
    private String itemCode;
    private BigDecimal price;
    private Integer qty;
    private BigDecimal amount;

    public long getInvoiceId() { return InvoiceLine.this.invoiceId; }
    public void setInvoiceId(long value) { InvoiceLine.this.invoiceId =  value; }
    public int getSeq() { return InvoiceLine.this.seq; }
    public void setSeq(int value) { InvoiceLine.this.seq =  value; }

    public String getItemCode() { return InvoiceLine.this.itemCode ; }
    public void setItemCode(String value) { InvoiceLine.this.itemCode = value; }
    public BigDecimal getPrice() { return InvoiceLine.this.price ; }
    public void setPrice(BigDecimal value) { InvoiceLine.this.price = value; }
    public Integer getQty() { return InvoiceLine.this.qty ; }
    public void setQty(Integer value) { InvoiceLine.this.qty = value; }
    public BigDecimal getAmount() { return InvoiceLine.this.amount; }
    public void setAmount(BigDecimal value) { InvoiceLine.this.amount = value; }
}
