package com.eyas.framework;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * @author Created by yixuan on 2018/12/27.
 */
@Slf4j
public class ListUtil {

    /**
     * 将list进行拆分，可以用于批量更新
     *
     * @param strList     用于拆分的集合
     * @param splitNumber 拆个数
     * @return 拆分后的多个子集合
     */
    public static <T> List<List<T>> splitList(List<T> strList, Integer splitNumber) {
        List<List<T>> lists = new ArrayList<>();
        List<T> list = new ArrayList<>();
        int i = 0;
        int j = 0;
        if (EmptyUtil.isEmpty(strList)) {
            return lists;
        }
        for (T t : strList) {
            i++;
            j++;
            list.add(t);
            if (i % splitNumber == 0 || j == strList.size()) {
                lists.add(list);
                list = new ArrayList<>();
                i = 0;
            }
        }
        return lists;
    }

    /**
     * 将list进行拆分，可以用于批量更新，去空
     *
     * @param strList     用于拆分的集合
     * @param splitNumber 拆个数
     * @param removeNull 是否需要进行空数据移除
     * @return 拆分后的多个子集合
     */
    public static <T> List<List<T>> splitList(List<T> strList, Integer splitNumber, boolean removeNull) {
        strList.removeIf(Objects::isNull);
        return ListUtil.splitList(strList, splitNumber);
    }

    /**
     * 获取一个list前几条的数据
     *
     * @param tList 集合
     * @param index 条数
     * @param <T> 泛型
     * @return list
     */
    public static <T> List<T> getIndexList(List<T> tList, Integer index) {
        List<T> tListNew = new ArrayList<>();
        for (T t : tList) {
            if (index != 0) {
                tListNew.add(t);
                index--;
            } else {
                break;
            }
        }
        return tListNew;
    }

    /**
     * list<dto> 转换成单条对象
     *
     * @param tList 集合
     * @param <T> 泛型
     * @return 对象
     */
    public static <T> T listDtoToDto(List<T> tList) {
        if (EmptyUtil.isNotEmpty(tList) || tList.size() > 0) {
            return tList.get(0);
        }
        return null;
    }

    /**
     * 数组长度动态切割算法
     * 切割树的高度为数组长度/数组长度开根号
     *
     * @param tList 切割list
     * @param <T> 切割数据返回多list集合
     * @return List<List<T>> 切割以后的数据
     * v1-2022-07-12
     * 扩展因子固定为10
     * v2-2022-07-12
     * 修改扩展因子为数组长度开根号
     */
    public static <T> List<List<T>> getListLengthDynamicExpansion(List<T> tList){
        Iterator<T> it = tList.iterator();
        while (it.hasNext()) {
            if (it.next() == null) {
                it.remove();
            }
        }
        int size = tList.size();
        double sqrtDouble = Math.sqrt(size);
        int sqrtInt = (int) sqrtDouble;
        int specialLength = size/sqrtInt;
        return ListUtil.splitList(tList, specialLength);
    }

    public static void main(String[] args) {
        List<Integer> aa = new ArrayList<>();
        for (int i = 0; i < 100000; i++) {
            if (i / 100 == 0){
                aa.add(null);
            }else {
                aa.add(i);
            }
        }
        Long startTime = System.currentTimeMillis();
        List<List<Integer>> bb = ListUtil.getListLengthDynamicExpansion(aa);
        Long endTime = System.currentTimeMillis();
        System.out.println("时间:" + (endTime - startTime));
    }
}
