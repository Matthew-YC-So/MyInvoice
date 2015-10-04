package com.software_force.myinvoice.models;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Matthew So on 10/4/2015.
 */

public class Invoice  {
    private long id ;
    private String member;
    private Date invoiceDate;
    private BigDecimal gross;
    private BigDecimal discount;
    private BigDecimal net;
    private BigDecimal gst;

    public long getId() { return Invoice.this.id; }
    public void setId(long value) { Invoice.this.id = value; }

    public String getMember() { return Invoice.this.member ; }
    public void setMember(String value) { Invoice.this.member = value; }
    public Date getInvoiceDate() { return Invoice.this.invoiceDate ; }
    public void setInvoiceDate(Date value) { Invoice.this.invoiceDate = value; }
    public BigDecimal getGross() { return Invoice.this.gross ; }
    public void setGross(BigDecimal value) { Invoice.this.gross = value; }
    public BigDecimal getDiscount() { return Invoice.this.discount ; }
    public void setDiscount(BigDecimal value) { Invoice.this.discount = value; }
    public BigDecimal getNet() { return Invoice.this.net ; }
    public void setNet(BigDecimal value) { Invoice.this.net = value; }
    public BigDecimal getGst() { return Invoice.this.gst ; }
    public void setGst(BigDecimal value) { Invoice.this.gst = value; }


    private ArrayList<InvoiceLine> invoiceLines ;
    public ArrayList<InvoiceLine> getInvoiceLines() { return Invoice.this.invoiceLines ; }
    public void setInvoiceLines(ArrayList<InvoiceLine> lines) { Invoice.this.invoiceLines = lines ; }

    public  Invoice() {
        Invoice.this.invoiceLines = new ArrayList<InvoiceLine>();
    }


}
