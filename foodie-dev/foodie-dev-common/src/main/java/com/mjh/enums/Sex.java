package com.mjh.enums;

/**
 * @PackageName: com.mjh.enums
 * @ClassName: Sex
 * @Author: majiahuan
 * @Date: 2020/10/20 21:42
 * @Description: enum 枚举类
 */
public enum  Sex {
    woman(0,"女"),
    man(1,"男"),
    secret(2,"保密");

    public final Integer type;
    public final String value;

    Sex(Integer type, String value) {
        this.type = type;
        this.value = value;
    }
}
