package com.test.greedy;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 贪心算法
 * @author yslao@outlook.com
 * @since 2021/4/4
 */
public class GreedyAlgorithm {

    public void greedy() {
        List<String> k1 = new ArrayList<>();
        k1.add("北京");
        k1.add("上海");
        k1.add("天津");
        List<String> k2 = new ArrayList<>();
        k2.add("广州");
        k2.add("北京");
        k2.add("深圳");
        List<String> k3 = new ArrayList<>();
        k3.add("成都");
        k3.add("上海");
        k3.add("杭州");
        List<String> k4 = new ArrayList<>();
        k4.add("上海");
        k4.add("天津");
        List<String> k5 = new ArrayList<>();
        k5.add("杭州");
        k5.add("大连");
        List<List<String>> list = new ArrayList<>();
        list.add(k1);
        list.add(k2);
        list.add(k3);
        list.add(k4);
        list.add(k5);
        List<String> allAreas = list.stream().flatMap(k -> k.stream()).distinct().collect(Collectors.toList());
        List<String> selects = new ArrayList<>();
        Map<String, List<String>> key2Size = new LinkedHashMap<>();
        String maxKey = null;
        while (!allAreas.isEmpty()) {
            maxKey = null;
            for (int i = 0; i < list.size(); i++) {
                List<String> k = list.get(i);
                k.retainAll(allAreas);
                if (k.size() > 0 && (maxKey == null || k.size() > key2Size.get(maxKey).size())) {
                    maxKey = "k" + (i+1);
                }
                key2Size.put("k" + (i+1), k);
            }
            if (maxKey != null) {
                selects.add(maxKey);
                allAreas.removeAll(key2Size.get(maxKey));
            }
            key2Size.clear();
        }
        System.out.println("选择了：" + Arrays.toString(selects.toArray()));
    }

}
