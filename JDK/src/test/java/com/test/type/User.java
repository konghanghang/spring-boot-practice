package com.test.type;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author yslao@outlook.com
 * @since 2022/3/3
 */
public class User<T> {

    private List<T> list = null;

    private Set<T> set = null;

    private Map<String ,T> map = null;

    private Map.Entry<String,Integer>  map2;

    private T t;

    private T[] ts;
    private List<String>[] lists;

    private List<? extends String> aa;

}
