package xyz.zhiweicoding.bike.support;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class CaffeineSupport {

    private final Cache<String, Object> cache;
    // 用于保存单独设置的过期时间，单位：毫秒
    private final ConcurrentMap<String, Long> expireMap = new ConcurrentHashMap<>();

    public CaffeineSupport() {
        this.cache = Caffeine.newBuilder().build();
    }

    // 内部方法：检查 key 是否过期，过期则移除
    private boolean isExpired(String key) {
        Long expireTime = expireMap.get(key);
        if (expireTime == null) {
            return false;
        }
        if (System.currentTimeMillis() > expireTime) {
            cache.invalidate(key);
            expireMap.remove(key);
            return true;
        }
        return false;
    }

    /**
     * 写入缓存（无过期时间）
     */
    public boolean set(final String key, Object value) {
        try {
            cache.put(key, value);
            expireMap.remove(key);
            return true;
        } catch (Exception e) {
            log.error("set cache error, key : {}, value : {}", key, value, e);
            return false;
        }
    }

    /**
     * 写入缓存，并设置过期时间
     */
    public boolean set(final String key, Object value, Long expireTime, TimeUnit timeUnit) {
        try {
            cache.put(key, value);
            expireMap.put(key, System.currentTimeMillis() + timeUnit.toMillis(expireTime));
            return true;
        } catch (Exception e) {
            log.error("set cache error, key : {}, value : {}", key, value, e);
            return false;
        }
    }

    /**
     * 批量删除指定 key
     */
    public void remove(final String... keys) {
        for (String key : keys) {
            remove(key);
        }
    }

    /**
     * 根据模式删除 key（支持简单的 "*" 通配符）
     */
    public void removePattern(final String pattern) {
        String regex = pattern.replace("*", ".*");
        for (String key : cache.asMap().keySet()) {
            if (key.matches(regex)) {
                remove(key);
            }
        }
    }

    /**
     * 删除对应的 key
     */
    public void remove(final String key) {
        cache.invalidate(key);
        expireMap.remove(key);
    }

    /**
     * 判断缓存中是否存在 key
     */
    public boolean exists(final String key) {
        if (cache.getIfPresent(key) == null) {
            return false;
        }
        if (isExpired(key)) {
            return false;
        }
        return true;
    }

    /**
     * 查询 key 的剩余过期时间，单位：秒（无过期返回 -1）
     */
    public long expireTime(final String key) {
        if (!exists(key)) {
            return -1;
        }
        Long expire = expireMap.get(key);
        if (expire == null) {
            return -1;
        }
        long remaining = expire - System.currentTimeMillis();
        return remaining > 0 ? TimeUnit.MILLISECONDS.toSeconds(remaining) : -1;
    }

    /**
     * 读取缓存
     */
    public Object get(final String key) {
        if (isExpired(key)) {
            return null;
        }
        return cache.getIfPresent(key);
    }

    // ----------------- Hash 操作 -----------------

    /**
     * Hash 添加
     */
    public void hmSet(String key, Object hashKey, Object value) {
        Object obj = get(key);
        Map<Object, Object> map;
        if (obj != null && obj instanceof Map) {
            map = (Map<Object, Object>) obj;
        } else {
            map = new ConcurrentHashMap<>();
        }
        map.put(hashKey, value);
        set(key, map);
    }

    /**
     * Hash 获取
     */
    public Object hmGet(String key, Object hashKey) {
        Object obj = get(key);
        if (obj != null && obj instanceof Map) {
            return ((Map<?, ?>) obj).get(hashKey);
        }
        return null;
    }

    // ----------------- List 操作 -----------------

    /**
     * 列表添加（右侧添加）
     */
    public void lPush(String key, Object v) {
        Object obj = get(key);
        List<Object> list;
        if (obj != null && obj instanceof List) {
            list = (List<Object>) obj;
        } else {
            list = Collections.synchronizedList(new ArrayList<>());
        }
        list.add(v);
        set(key, list);
    }

    /**
     * 列表获取：根据起始和结束索引（闭区间）
     */
    public List<Object> lRange(String key, long start, long end) {
        Object obj = get(key);
        if (obj != null && obj instanceof List) {
            List<Object> list = (List<Object>) obj;
            int size = list.size();
            int s = (int) start;
            int e = (int) end;
            if (e >= size) {
                e = size - 1;
            }
            if (s < 0) s = 0;
            if (s > e) {
                return new ArrayList<>();
            }
            // 注意：subList 的结束索引为独占，因此 e + 1
            return list.subList(s, e + 1);
        }
        return new ArrayList<>();
    }

    // ----------------- Set 操作 -----------------

    /**
     * 集合添加
     */
    public void add(String key, Object value) {
        Object obj = get(key);
        Set<Object> set;
        if (obj != null && obj instanceof Set) {
            set = (Set<Object>) obj;
        } else {
            set = Collections.newSetFromMap(new ConcurrentHashMap<>());
        }
        set.add(value);
        set(key, set);
    }

    /**
     * 获取集合中的所有成员
     */
    public Set<Object> setMembers(String key) {
        Object obj = get(key);
        if (obj != null && obj instanceof Set) {
            return (Set<Object>) obj;
        }
        return new HashSet<>();
    }

    // ----------------- Sorted Set 操作 -----------------

    /**
     * 有序集合添加
     */
    public void zAdd(String key, Object value, double score) {
        Object obj = get(key);
        SortedSet<SortedSetEntry> sortedSet;
        if (obj != null && obj instanceof SortedSet) {
            sortedSet = (SortedSet<SortedSetEntry>) obj;
        } else {
            sortedSet = new TreeSet<>();
        }
        // 若该元素已存在，则先移除旧记录
        sortedSet.remove(new SortedSetEntry(value, score));
        sortedSet.add(new SortedSetEntry(value, score));
        set(key, sortedSet);
    }

    /**
     * 有序集合按分数区间获取元素
     */
    public Set<Object> rangeByScore(String key, double scoreStart, double scoreEnd) {
        Object obj = get(key);
        Set<Object> result = new LinkedHashSet<>();
        if (obj != null && obj instanceof SortedSet) {
            SortedSet<SortedSetEntry> sortedSet = (SortedSet<SortedSetEntry>) obj;
            for (SortedSetEntry entry : sortedSet) {
                if (entry.getScore() >= scoreStart && entry.getScore() <= scoreEnd) {
                    result.add(entry.getValue());
                }
            }
        }
        return result;
    }

    // ----------------- 内部辅助类 -----------------

    /**
     * 用于模拟 SortedSet 中的元素
     */
    private static class SortedSetEntry implements Comparable<SortedSetEntry> {
        private final Object value;
        private final double score;

        public SortedSetEntry(Object value, double score) {
            this.value = value;
            this.score = score;
        }

        public Object getValue() {
            return value;
        }

        public double getScore() {
            return score;
        }

        @Override
        public int compareTo(SortedSetEntry o) {
            int cmp = Double.compare(this.score, o.score);
            if (cmp != 0) {
                return cmp;
            }
            // 如果分数相同，则根据 value 的 hashCode 比较
            return Integer.compare(this.value.hashCode(), o.value.hashCode());
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            SortedSetEntry that = (SortedSetEntry) obj;
            return Double.compare(that.score, score) == 0 &&
                    Objects.equals(value, that.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(value, score);
        }
    }
}
