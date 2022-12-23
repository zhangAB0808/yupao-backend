package com.yupi.yupao.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yupi.yupao.common.BaseResponse;
import com.yupi.yupao.common.ErrorCode;
import com.yupi.yupao.common.ResultUtils;
import com.yupi.yupao.exception.BusinessException;
import com.yupi.yupao.model.domain.Tag;
import com.yupi.yupao.model.domain.User;
import com.yupi.yupao.model.dto.TagChildrenDto;
import com.yupi.yupao.model.dto.TagDto;
import com.yupi.yupao.model.request.TagAddRequest;
import com.yupi.yupao.service.TagService;
import com.yupi.yupao.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 标签接口
 *
 * @author zhangzheng
 */
@RestController
@CrossOrigin(origins = {"http://localhost:3000"})
@RequestMapping("/tag")
public class TagController {

    @Resource
    private TagService tagService;
    @Resource
    private UserService userService;

    /**
     * 新增标签
     */
    @PostMapping("/add")
    public BaseResponse<Boolean> addTags(@RequestBody TagAddRequest tagAddRequest, HttpServletRequest request) {
        if (tagAddRequest.getParentTag() == null||tagAddRequest.getSonTag()==null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        Long userId = loginUser.getId();
        String parentTagName = tagAddRequest.getParentTag();
        Tag one = tagService.getOne(new QueryWrapper<Tag>().eq("tagName", parentTagName));
        //查出表中是否有父标签，有的话直接添加子标签设置父标签id,没的话，先添加父标签，获得父标签id再添加子标签
        Long parentTagId;
        if (one != null) {
            parentTagId = one.getId();
        } else {
            Tag parentTag = new Tag();
            parentTag.setTagName(parentTagName);
            parentTag.setIsParent((byte) 1);
            parentTag.setUserId(userId);
            boolean result = tagService.save(parentTag);
            if (!result) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR);
            }
            Tag newParentTag = tagService.getOne(new QueryWrapper<Tag>().eq("tagName", parentTagName));
            parentTagId = newParentTag.getId();
        }
        Tag sonTag = new Tag();
        sonTag.setTagName(tagAddRequest.getSonTag());
        sonTag.setIsParent((byte) 0);
        sonTag.setParentId(parentTagId);
        sonTag.setUserId(userId);
        boolean save = tagService.save(sonTag);
        if (!save) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        return ResultUtils.success(true);
    }

    /**
     * 查出所有父标签返回
     * @return
     */
    @GetMapping("/listParent")
    public BaseResponse<List<String>> listParent() {
        // 查出所有父标签
        QueryWrapper<Tag> tagQueryWrapper = new QueryWrapper<>();
        tagQueryWrapper.isNull("parentId");
        tagQueryWrapper.eq("isParent", 1);
        List<Tag> parentTagList = tagService.list(tagQueryWrapper);
        List<String> result = parentTagList.stream().map(Tag::getTagName).collect(Collectors.toList());
        return ResultUtils.success(result);
    }



    /**
     * 查询将标签封装成前端需要的格式返回
     * @return
     */
    @GetMapping("/list")
    public BaseResponse<List<TagDto>> list() {
        //先查出所有父标签
        QueryWrapper<Tag> tagQueryWrapper = new QueryWrapper<>();
        tagQueryWrapper.isNull("parentId");
        tagQueryWrapper.eq("isParent", 1);
        List<Tag> parentTagList = tagService.list(tagQueryWrapper);
        //根据父标签id查出对应的子标签列表，进行封装转换
        //[{"学习方向":["java","c++","前端"] }, {"性别": ["男","女"]}]
        List<TagDto> result = parentTagList.stream().map(parent -> {
            TagDto tagDto = new TagDto();
            tagDto.setText(parent.getTagName());

            QueryWrapper<Tag> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("parentId", parent.getId()).eq("isParent", 0);
            List<Tag> childrenList = tagService.list(queryWrapper);
            List<TagChildrenDto> tagChildrenDtoList = childrenList.stream().map(child -> {
                TagChildrenDto tagChildrenDto = new TagChildrenDto();
                tagChildrenDto.setText(child.getTagName());
                tagChildrenDto.setId(child.getTagName());
                return tagChildrenDto;
            }).collect(Collectors.toList());

            tagDto.setChildren(tagChildrenDtoList);
            return tagDto;

        }).collect(Collectors.toList());

        return ResultUtils.success(result);
    }


}
