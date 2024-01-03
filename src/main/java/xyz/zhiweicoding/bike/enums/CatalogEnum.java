package xyz.zhiweicoding.bike.enums;

import lombok.Getter;

/**
 * @author zhiweicoding.xyz
 * @date 1/2/24
 * @email diaozhiwei2k@gmail.com
 */
@Getter
public enum CatalogEnum {
    DEFAULT("default"),
    PRICE("price"),
    CATEGORY("category");

    private final String name;

    CatalogEnum(String name) {
        this.name = name;
    }

    /**
     * 根据名称获取枚举
     */
    public static CatalogEnum getByName(String name) {
        for (CatalogEnum flagEnum : CatalogEnum.values()) {
            if (flagEnum.getName().equals(name)) {
                return flagEnum;
            }
        }
        return null;
    }
}