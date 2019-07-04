package com.lazy.bike.pojo;

import java.util.List;

public class ZoneVO {

    //区的名字
    private List<String> names;

    //区名和数量的集合
    private List<ValueName> valueNames;

    public List<String> getNames() {
        return names;
    }

    public void setNames(List<String> names) {
        this.names = names;
    }

    public List<ValueName> getValueNames() {
        return valueNames;
    }

    public void setValueNames(List<ValueName> valueNames) {
        this.valueNames = valueNames;
    }
}
