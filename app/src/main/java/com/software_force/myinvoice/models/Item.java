package com.software_force.myinvoice.models;

import java.math.BigDecimal;

/**
 * Created by Matthew So on 10/4/2015.
 */
public class Item {
    private String code;
    private String description;
    private BigDecimal price;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}