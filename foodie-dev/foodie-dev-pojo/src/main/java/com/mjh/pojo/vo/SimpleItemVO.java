package com.mjh.pojo.vo;

/**
 * @PackageName: com.mjh.pojo.vo
 * @ClassName: SimpleItemVO
 * @Author: majiahuan
 * @Date: 2020/11/29 13:51
 * @Description: 6个商品的简单数据类型
 */
public class SimpleItemVO {
    private String itemId;
    private String itemName;
    private String itemUrl;
    private String createTime;

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemUrl() {
        return itemUrl;
    }

    public void setItemUrl(String itemUrl) {
        this.itemUrl = itemUrl;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
