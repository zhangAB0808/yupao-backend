package com.yupi.yupao.job;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yupi.yupao.common.ResultUtils;
import com.yupi.yupao.model.domain.User;
import com.yupi.yupao.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * 缓存预热,定时任务
 */
@Component
@Slf4j
public class PreCacheJob {

    @Resource
    private UserService userService;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private RedissonClient redissonClient;

    //每天16:38执行，预热推荐用户
    @Scheduled(cron = "0 38 16 * * *")   //定时任务，cron表达式
    public void doPreCacheRecommendUser() {

        //redisson分布式锁，只执行一次
        RLock lock = redissonClient.getLock("preCacheJo  b:doCache:lock");
        try {
            //tryLock参数等待时间0，如果没有抢到锁直接返回false，不等待，锁过期释放时间30s
            if (lock.tryLock(0,-1,TimeUnit.SECONDS)) {
                //将key格式化存取,便于与其他模块区别，防止混淆0
                String key = String.format("user:recommend:%s", 3);
                //无缓存，读数据库
                QueryWrapper<User> queryWrapper = new QueryWrapper<>();
                Page<User> userPage = userService.page(new Page<>(1, 30), queryWrapper);
                //写入缓存中,设置过期时间,如果缓存存入失败了，是要后台捕获异常，数据正常返回给用户，而不是抛异常！！！
                try {
                    redisTemplate.opsForValue().set(key, userPage, 30, TimeUnit.MINUTES);
                } catch (Exception e) {
                    log.error("redis set key error", e);
                }
            }
        } catch (InterruptedException e) {
            log.error("doPreCacheRecommendUser error",e);
        }finally {
            //判断是自己的锁再释放，写到finally里，如果中间执行出错的话，最终还是会释放锁
            if(lock.isHeldByCurrentThread()){
                lock.unlock();
            }
        }


    }
}
