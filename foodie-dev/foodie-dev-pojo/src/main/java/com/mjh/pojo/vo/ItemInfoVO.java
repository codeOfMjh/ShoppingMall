package com.mjh.pojo.vo;

import com.mjh.pojo.Items;
import com.mjh.pojo.ItemsImg;
import com.mjh.pojo.ItemsParam;
import com.mjh.pojo.ItemsSpec;

import java.util.List;

/**
 * @PackageName: com.mjh.pojo.vo
 * @ClassName: SimpleItemVO
 * @Author: majiahuan
 * @Date: 2020/11/29 13:51
 * @Description: 商品详情VO
 */
public class ItemInfoVO {
    private Items item;
    private List<ItemsImg> itemImgList;
    private List<ItemsSpec> itemSpecList;
    private ItemsParam itemParams;

    public Items getItem() {
        return item;
    }

    public void setItem(Items item) {
        this.item = item;
    }

    public List<ItemsImg> getItemImgList() {
        return itemImgList;
    }

    public void setItemImgList(List<ItemsImg> itemImgList) {
        this.itemImgList = itemImgList;
    }

    public List<ItemsSpec> getItemSpecList() {
        return itemSpecList;
    }

    public void setItemSpecList(List<ItemsSpec> itemSpecList) {
        this.itemSpecList = itemSpecList;
    }

    public ItemsParam getItemParams() {
        return itemParams;
    }

    public void setItemParams(ItemsParam itemParams) {
        this.itemParams = itemParams;
    }
}
