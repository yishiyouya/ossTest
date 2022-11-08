package com.badfox.osstest.util.generator.system.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author badfox
 * @since 2022-11-08
 */
@ApiModel(value = "Productinfo对象", description = "")
public class Productinfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    @ApiModelProperty("产品中文名")
    private String productName;

    @ApiModelProperty("产品对应表")
    private String productTable;

    @ApiModelProperty("订阅用户")
    private String shareCompany;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductTable() {
        return productTable;
    }

    public void setProductTable(String productTable) {
        this.productTable = productTable;
    }

    public String getShareCompany() {
        return shareCompany;
    }

    public void setShareCompany(String shareCompany) {
        this.shareCompany = shareCompany;
    }

    @Override
    public String toString() {
        return "Productinfo{" +
            "id = " + id +
            ", productName = " + productName +
            ", productTable = " + productTable +
            ", shareCompany = " + shareCompany +
        "}";
    }
}
