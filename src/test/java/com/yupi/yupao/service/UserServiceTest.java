package com.yupi.yupao.service;

import com.yupi.yupao.YuPaoApplication;
import com.yupi.yupao.model.domain.User;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import java.util.Arrays;
import java.util.List;

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


}