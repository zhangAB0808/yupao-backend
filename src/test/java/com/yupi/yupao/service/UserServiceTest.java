package com.yupi.yupao.service;

import java.util.ArrayList;
import java.util.Date;

import com.yupi.yupao.YuPaoApplication;
import com.yupi.yupao.model.domain.User;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StopWatch;

import javax.annotation.Resource;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {YuPaoApplication.class})
class UserServiceTest {

    @Resource
    private UserService userService;

    @Test
    void searchUserByTags() {
        List<String> tags = Arrays.asList("java");
        List<User> users = userService.searchUserByTags(tags);
        Assert.assertNotNull(users);
    }

    /**
     * 批量插入数据
     */
//    @Test
//    void insertUsers() {
//        StopWatch stopWatch = new StopWatch();
//        stopWatch.start();
//        ArrayList<User> userList = new ArrayList<>();
//        for (int i = 0; i < 100000; i++) {
//            User user = new User();
//            user.setUsername("zzklaus");
//            user.setUserAccount("zzklaus");
//            user.setAvatarUrl("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fi.qqkou.com%2Fi%2F0a3650915564x3837264329b26.jpg&refer=http%3A%2F%2Fi.qqkou.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1668154086&t=f1b7d0c1849d9152e78bd7e70b481907");
//            user.setGender(0);
//            user.setUserPassword("zz08089518");
//            user.setPhone("18438909039");
//            user.setEmail("3317306971@qq.com");
//            user.setTags("");
//            user.setUserProfile("哈哈哈");
//            user.setCreateTime(new Date());
//            user.setUpdateTime(new Date());
//            userList.add(user);
//        }
//        userService.saveBatch(userList, 10000);
//        stopWatch.stop();
//        System.out.println("运行时间为：" + stopWatch.getTotalTimeMillis());
//    }
//
//    /**
//     * 异步并发插入数据
//     */
//    @Test
//    void doCurrentInsertUsers() {
//        StopWatch stopWatch = new StopWatch();
//        stopWatch.start();
//        ArrayList<CompletableFuture<Void>> futureList = new ArrayList<>();
//        int j=0;
//        for (int i = 0; i < 20; i++) {
//            ArrayList<User> userList = new ArrayList<>();
//            while (true) {
//                j++;
//                User user = new User();
//                user.setUsername("zzklaus");
//                user.setUserAccount("zzklaus");
//                user.setAvatarUrl("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fi.qqkou.com%2Fi%2F0a3650915564x3837264329b26.jpg&refer=http%3A%2F%2Fi.qqkou.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1668154086&t=f1b7d0c1849d9152e78bd7e70b481907");
//                user.setGender(0);
//                user.setUserPassword("zz08089518");
//                user.setPhone("18438909039");
//                user.setEmail("3317306971@qq.com");
//                user.setTags("");
//                user.setUserProfile("哈哈哈");
//                user.setCreateTime(new Date());
//                user.setUpdateTime(new Date());
//                userList.add(user);
//                if(j%5000==0) {
//                    break;
//                }
//            }   //异步执行
//            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
//                userService.saveBatch(userList, 5000);
//            });
//           futureList.add(future);
//        }
//        CompletableFuture.allOf(futureList.toArray(new CompletableFuture[]{})).join();
//
//        stopWatch.stop();
//        System.out.println("运行时间为：" + stopWatch.getTotalTimeMillis());
//    }









    @Test
    void test() {

        }


}