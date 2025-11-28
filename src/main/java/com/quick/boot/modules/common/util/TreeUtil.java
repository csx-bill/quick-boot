package com.quick.boot.modules.common.util;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 万能树工具类 - 提供树形结构构建和排序功能
 */
public class TreeUtil {

    /**
     * 构建树形结构（核心方法）
     *
     * @param list 原始数据列表
     * @param idGetter 节点ID获取函数
     * @param parentIdGetter 父节点ID获取函数
     * @param childrenSetter 子节点设置函数
     * @param comparator 节点排序比较器（可为null）
     * @param rootValues 根节点标识值集合（可为null）
     * @param <T> 节点数据类型
     * @param <R> ID数据类型
     * @return 构建完成的树形结构列表
     */
    public static <T, R> List<T> buildTree(
            List<T> list,
            Function<T, R> idGetter,
            Function<T, R> parentIdGetter,
            BiConsumer<T, List<T>> childrenSetter,
            Comparator<T> comparator,
            Set<R> rootValues) {

        // 空列表安全处理
        if (Objects.isNull(list) || list.isEmpty()) {
            return Collections.emptyList();
        }

        // 创建ID到节点的映射，用于快速查找节点
        Map<R, T> idMap = list.stream()
                .collect(Collectors.toMap(idGetter, node -> node, (a, b) -> a));

        // 处理根节点标识值（防止空指针）
        Set<R> effectiveRootValues = Objects.nonNull(rootValues) ? rootValues : Collections.emptySet();

        // 构建父ID到子节点列表的映射
        Map<R, List<T>> parentMap = new HashMap<>();
        list.forEach(node -> {
            R parentId = parentIdGetter.apply(node);
            // 使用computeIfAbsent确保每个父ID都有对应的列表
            parentMap.computeIfAbsent(parentId, k -> new ArrayList<>()).add(node);
        });

        // 检测循环引用（防止无限递归）
        detectCycle(parentMap, idGetter, idMap);

        // 存储最终结果（根节点列表）
        List<T> result = new ArrayList<>();

        // 遍历所有节点构建树结构
        list.forEach(node -> {
            R id = idGetter.apply(node);
            // 获取当前节点的子节点列表
            List<T> children = parentMap.getOrDefault(id, Collections.emptyList());

            // 对子节点进行排序（如果提供了比较器）
            if (!children.isEmpty() && Objects.nonNull(comparator)) {
                children.sort(comparator);
            }

            // 设置子节点到当前节点
            childrenSetter.accept(node, children);

            // 判断当前节点是否为根节点：
            // 1. 父ID在根节点标识值集合中
            // 2. 或者根节点标识集合为空且当前节点没有父节点（即父ID不在parentMap的键中）
            R parentId = parentIdGetter.apply(node);
            if (effectiveRootValues.contains(parentId) ||
                    (!parentMap.containsKey(parentId) && effectiveRootValues.isEmpty())) {
                result.add(node);
            }
        });

        // 对根节点列表进行排序
        if (Objects.nonNull(comparator)) {
            result.sort(comparator);
        }
        return result;
    }

    /**
     * 检测循环引用（防止父节点和子节点相互引用导致无限递归）
     *
     * @param parentMap 父节点映射表
     * @param idGetter 节点ID获取函数
     * @param idMap ID到节点的映射
     */
    private static <T, R> void detectCycle(Map<R, List<T>> parentMap,
                                           Function<T, R> idGetter,
                                           Map<R, T> idMap) {
        // 遍历所有父节点ID及其子节点列表
        parentMap.forEach((parentId, children) -> {
            // 跳过无效的父节点ID（不存在对应节点）
            if (Objects.nonNull(parentId) && !idMap.containsKey(parentId)) return;

            // 检查每个子节点是否与父节点形成循环引用
            children.forEach(child -> {
                R childId = idGetter.apply(child);
                // 如果父节点ID等于子节点ID，说明存在循环引用
                if (Objects.equals(parentId, childId)) {
                    throw new IllegalStateException("发现循环引用: " + parentId);
                }
            });
        });
    }

    /**
     * 创建通用排序比较器（支持任意Comparable类型）
     *
     * @param sortGetter 排序值获取函数
     * @param ascending 排序方向：true=升序（小→大），false=降序（大→小）
     * @param nullsFirst 空值处理：true=空值排前面，false=空值排后面
     * @return 配置好的比较器
     */
    public static <T, S extends Comparable<S>> Comparator<T> createComparator(
            Function<T, S> sortGetter,
            boolean ascending,
            boolean nullsFirst) {

        // 使用Comparator工具构建安全的比较器
        return Comparator.nullsFirst(Comparator.comparing(
                sortGetter,
                (a, b) -> {
                    // 处理升序/降序逻辑
                    return ascending ? a.compareTo(b) : b.compareTo(a);
                }
        ));
    }
}