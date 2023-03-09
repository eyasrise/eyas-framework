package com.eyas.framework;

import lombok.Data;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author Created by yixuan on 2019/5/13.
 */
@Data
public class LambdaUtil {

    private Long id;

    private String code;

    private Integer age;

    /**
     * list去重算法
     *
     * @param tList        去重集合
     * @param keyExtractor 去重对象属性
     * @param <T>          泛型
     * @return
     */
    public static <T> List<T> distinct(List<T> tList, Function<? super T, Object> keyExtractor) {
        //list是需要去重的list，返回值是去重后的list
        List<T> distinctTs = tList.stream().filter(distinctByKey(keyExtractor)).collect(Collectors.toList());
        return distinctTs;
    }

    /**
     * list分组算法
     *
     * @param tList 需要分组的集合
     * @param <T>   泛型
     * @return 分组的条件的值作为map的key，分组结果作为Value
     */
    public static <T, E> Map<E, List<T>> groupByToMap(List<T> tList, Function<? super T, E> keyExtractor) {
        return tList.stream().collect(Collectors.groupingBy(keyExtractor));
    }

    private static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return object -> seen.putIfAbsent(keyExtractor.apply(object), Boolean.TRUE) == null;
    }

    /**
     * 排序工具——由小到大
     *
     * @param tList        排序集合
     * @param keyExtractor 排序字段
     * @param <T>          忽略
     * @param <U>          忽略
     * @return 排序结果
     */
    public static <T, U extends Comparable<? super U>> List<T> sort01(List<T> tList, Function<? super T, ? extends U> keyExtractor) {
        //list是需要去重的list，返回值是去重后的list
        List<T> distinctTs = tList.stream().sorted(Comparator.comparing(keyExtractor)).collect(Collectors.toList());
        return distinctTs;
    }

    /**
     * 排序工具——由大到小
     *
     * @param tList        排序集合
     * @param keyExtractor 排序字段
     * @param <T>          忽略
     * @param <U>          忽略
     * @return 排序结果
     */
    public static <T, U extends Comparable<? super U>> List<T> sort10(List<T> tList, Function<? super T, ? extends U> keyExtractor) {
        //list是需要去重的list，返回值是去重后的list
        List<T> distinctTs = tList.stream().sorted(Comparator.comparing(keyExtractor).reversed()).collect(Collectors.toList());
        return distinctTs;
    }


    public static void main(String[] args) {
        List<LambdaUtil> lambdaUtilList = new ArrayList<>();
        LambdaUtil lambdaUtil = new LambdaUtil();
        lambdaUtil.setId(1L);
        lambdaUtil.setAge(10);
        LambdaUtil lambdaUtil2 = new LambdaUtil();
        lambdaUtil2.setId(2L);
        lambdaUtil2.setAge(30);
        LambdaUtil lambdaUtil3 = new LambdaUtil();
        lambdaUtil3.setId(3L);
        lambdaUtil3.setAge(20);
        lambdaUtilList.add(lambdaUtil);
        lambdaUtilList.add(lambdaUtil2);
        lambdaUtilList.add(lambdaUtil3);
        List<LambdaUtil> lambdaUtilList1 = LambdaUtil.sort10(lambdaUtilList, LambdaUtil::getAge);
        System.out.println(lambdaUtilList1);
        System.out.println(LambdaUtil.sort01(lambdaUtilList, LambdaUtil::getAge));
    }
}
