package com.mjh.enums;

/**
 * @Desc: 商品分类等级 枚举
 */
public enum CategoryLevel {
    ONELEVEL(1, "一级分类"),
    TWOLEVEL(2, "二级分类"),
    THREELEVEL(3, "三级级分类");

    public final Integer type;
    public final String value;

    CategoryLevel(Integer type, String value) {
        this.type = type;
        this.value = value;
    }
}
