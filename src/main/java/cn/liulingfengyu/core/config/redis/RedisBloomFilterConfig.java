package cn.liulingfengyu.core.config.redis;

import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisBloomFilterConfig {

    private static final String BLOOM_FILTER_NAME = "idBloomFilter";
    private static final long EXPECTED_INSERTIONS = 100000L;
    private static final double FPP = 0.01;

    @Bean
    public RBloomFilter<String> redisBloomFilter(RedissonClient redissonClient) {
        RBloomFilter<String> bloomFilter = redissonClient.getBloomFilter(BLOOM_FILTER_NAME);
        // 初始化布隆过滤器
        bloomFilter.tryInit(EXPECTED_INSERTIONS, FPP);
        return bloomFilter;
    }
}