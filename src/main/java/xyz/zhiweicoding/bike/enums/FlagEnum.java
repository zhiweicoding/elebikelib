package xyz.zhiweicoding.bike.enums;

import lombok.Getter;

/**
 * @author zhiweicoding.xyz
 * @date 1/2/24
 * @email diaozhiwei2k@gmail.com
 */
@Getter
public enum FlagEnum {
    NONE("none"),
    DESC("desc"),
    ASC("asc");

    private final String name;

    FlagEnum(String name) {
        this.name = name;
    }

    /**
     * 根据名称获取枚举
     */
    public static FlagEnum getByName(String name) {
        for (FlagEnum flagEnum : FlagEnum.values()) {
            if (flagEnum.getName().equals(name)) {
                return flagEnum;
            }
        }
        return NONE;
    }
}