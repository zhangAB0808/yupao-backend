package com.yupi.yupao.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yupi.yupao.common.BaseResponse;
import com.yupi.yupao.common.ErrorCode;
import com.yupi.yupao.common.ResultUtils;
import com.yupi.yupao.exception.BusinessException;
import com.yupi.yupao.model.domain.User;
import com.yupi.yupao.model.domain.UserFriend;
import com.yupi.yupao.model.dto.ChatContentDto;
import com.yupi.yupao.model.request.DeleteRequest;
import com.yupi.yupao.model.request.FriendAddRequest;
import com.yupi.yupao.model.vo.UserFriendChatVo;
import com.yupi.yupao.model.vo.UserVo;
import com.yupi.yupao.service.UserFriendService;
import com.yupi.yupao.service.UserService;
import jodd.net.HttpStatus;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Administrator
 */
@RestController
@RequestMapping("/userFriend")
public class UserFriendController {

    @Resource
    private UserFriendService userFriendService;

    @Resource
    private UserService userService;

    /**
     * 查出好友信息
     *
     * @param request
     * @return
     */
    @GetMapping("/search")
    public BaseResponse<List<UserVo>> searchFriends(HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        List<UserVo> userFriendVoList = userFriendService.searchFriends(loginUser);
        return ResultUtils.success(userFriendVoList);
    }

    /**
     * 删除好友
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteFriend(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        Long id = deleteRequest.getId();
        if (id == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        Long userId = loginUser.getId();
        QueryWrapper<UserFriend> queryWrapper = new QueryWrapper<>();

        //delete from userFriend where (userId=? and friendId=?) or (friendId=? and userId=?)
        queryWrapper.and(qw -> {
            qw.eq("userId", userId).eq("friendId", id);
        })
                .or(qw -> {
                    qw.eq("userId", id).eq("friendId", userId);
                });
        boolean result = userFriendService.remove(queryWrapper);
        if (!result) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        return ResultUtils.success(true);
    }

    /**
     * 添加好友
     *
     * @param friendAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Boolean> addFriend(@RequestBody FriendAddRequest friendAddRequest, HttpServletRequest request) {
        long friendId = friendAddRequest.getFriendId();
        User loginUser = userService.getLoginUser(request);
        UserFriend userFriend = new UserFriend();
        userFriend.setUserId(loginUser.getId());
        userFriend.setFriendId(friendId);
        boolean result = userFriendService.save(userFriend);
        if (!result) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        return ResultUtils.success(true);
    }

    /**
     * 根据好友id查出其信息和聊天内容
     *
     * @param friendId
     * @return
     */
    @GetMapping("/searchChatById")
    public BaseResponse<UserFriendChatVo> searchFriendChatById(Long friendId, HttpServletRequest request) {
        if (friendId == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        //查出好友信息
        User friend = userService.getById(friendId);
        if (friend == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        Long loginUserId = loginUser.getId();
        QueryWrapper<UserFriend> queryWrapper = new QueryWrapper<>();

        //查出与好友的聊天记录
        //select * from userFriend where (userId=? and friendId=?) or (friendId=? and userId=?)
        queryWrapper.and(qw -> {
            qw.eq("userId", loginUserId).eq("friendId", friendId);
        }).or(qw -> {
            qw.eq("userId", friendId).eq("friendId", loginUserId);
        });
        UserFriend one = userFriendService.getOne(queryWrapper);
        String chatContent = one.getChatContent();
        Date updateTime = one.getUpdateTime();
        //封装返回
        UserFriendChatVo userFriendChatVo = new UserFriendChatVo();
        BeanUtils.copyProperties(friend, userFriendChatVo);
        userFriendChatVo.setChatContent(chatContent);
        userFriendChatVo.setUpdateTime(updateTime);
        return ResultUtils.success(userFriendChatVo);
    }


    /**
     * 更新聊天记录
     *
     * @param chatContentDto
     * @param request
     * @return
     */
    @PostMapping("/updateChat")
    public BaseResponse<Boolean> updateChatContent(@RequestBody ChatContentDto chatContentDto, HttpServletRequest request) {
        //传的是friendId 和 content,自己的id可以获取,friendId主要是为了查询其对应聊天记录
        Long friendId = chatContentDto.getId();
        User loginUser = userService.getLoginUser(request);
        Long loginUserId = loginUser.getId();
        chatContentDto.setId(loginUserId);
        String content = new Gson().toJson(chatContentDto);
        QueryWrapper<UserFriend> queryWrapper = new QueryWrapper<>();
        queryWrapper.and(qw -> {
            qw.eq("userId", loginUserId).eq("friendId", friendId);
        }).or(qw -> {
            qw.eq("userId", friendId).eq("friendId", loginUserId);
        });
        //查询聊天记录，并对其json字符串数组进行内容拼接，也可以用Gson
        UserFriend one = userFriendService.getOne(queryWrapper);
        String oldChatContent = one.getChatContent();
        String newChatContent;
        if(oldChatContent==null||oldChatContent.length()<1){
            newChatContent= "["+content+"]";
        }else{
            newChatContent = oldChatContent.substring(0, oldChatContent.length() - 1) + "," + content + "]";
        }
        //更新聊天记录
        UpdateWrapper<UserFriend> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", one.getId()).set("chatContent", newChatContent);
        boolean result = userFriendService.update(updateWrapper);
        if (!result) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        return ResultUtils.success(true);
    }

    /**
     * 撤回消息
     * @param index
     * @param friendId
     * @param request
     * @return
     */
    @PostMapping("/withdrawChatContent")
    public BaseResponse<Boolean> withdrawChatContent(Integer index, Long friendId, HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        Long loginUserId = loginUser.getId();
        QueryWrapper<UserFriend> queryWrapper = new QueryWrapper<>();
        queryWrapper.and(qw -> {
            qw.eq("userId", loginUserId).eq("friendId", friendId);
        }).or(qw -> {
            qw.eq("userId", friendId).eq("friendId", loginUserId);
        });
        UserFriend one = userFriendService.getOne(queryWrapper);
        String chatContent = one.getChatContent();
        ArrayList<ChatContentDto> list = new Gson().fromJson(chatContent, new TypeToken<ArrayList<ChatContentDto>>() {
        }.getType());
        list.remove(index);
        String s = new Gson().toJson(list);
        one.setChatContent(s);
        boolean result = userFriendService.updateById(one);
        if (!result) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        return ResultUtils.success(true);
    }

}
