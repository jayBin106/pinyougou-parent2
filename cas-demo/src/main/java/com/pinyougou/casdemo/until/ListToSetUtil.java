package com.pinyougou.casdemo.until;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * ListToSetUtil
 * <p>
 * liwenbin
 * 2019/1/18 17:24
 */
public class ListToSetUtil {
    public static Set<String> listToSet(List<String> list) {
        Set<String> sets = new LinkedHashSet<>();
        for (String o : list) {
            sets.add(o);
        }
        return sets;
    }
}
