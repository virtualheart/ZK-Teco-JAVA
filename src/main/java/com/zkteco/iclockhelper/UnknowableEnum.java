package com.zkteco.iclockhelper;

public enum UnknowableEnum {
    unknown;

    public static UnknowableEnum _missing_(String value) {
        return UnknowableEnum.unknown;
    }
}