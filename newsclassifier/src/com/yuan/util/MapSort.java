package com.yuan.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

public class MapSort {
	/**
	 * 使用 Map按key进行排序
	 * @param map
	 * @return
	 */
	public static Map<Float, String> sortMapByKey(Map<Float, String> map) {
		if (map == null || map.isEmpty()) {
			return null;
		}

		Map<Float, String> sortMap = new TreeMap<Float, String>(
				new Comparator<Float>(){
					public int compare(Float o1, Float o2) {
						//降序排
						return o2.compareTo(o1);
					}
				});
		sortMap.putAll(map);
		return sortMap;
	}
	
	/**
	 * 使用 Map按value进行排序
	 * @param map
	 * @return
	 */
	public static Map<String, Integer> sortMapByValue(Map<String, Integer> map) {
		if (map == null || map.isEmpty()) {
			return null;
		}
		//这里将map.entrySet()转换成list
        List<Map.Entry<String,Integer>> list = new ArrayList<Map.Entry<String,Integer>>(map.entrySet());
        //然后通过比较器来实现排序
        Collections.sort(list,new Comparator<Map.Entry<String,Integer>>() {
            //升序排序
            public int compare(Entry<String, Integer> o1,
                    Entry<String, Integer> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });
        
        Iterator<Map.Entry<String, Integer>> iter = list.iterator();
		Map.Entry<String, Integer> tmpEntry = null;
		Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
		while (iter.hasNext()) {
			tmpEntry = iter.next();
			sortedMap.put(tmpEntry.getKey(), tmpEntry.getValue());
		}
		return sortedMap;
	}
}
