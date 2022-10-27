package com.yupi.yupao.service.impl;

import java.util.Date;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yupi.yupao.common.ErrorCode;
import com.yupi.yupao.exception.BusinessException;
import com.yupi.yupao.mapper.TeamMapper;
import com.yupi.yupao.mapper.UserMapper;
import com.yupi.yupao.model.domain.Team;
import com.yupi.yupao.model.domain.User;
import com.yupi.yupao.model.domain.UserTeam;
import com.yupi.yupao.model.dto.TeamQuery;
import com.yupi.yupao.model.enums.TeamStatusEnum;
import com.yupi.yupao.model.request.TeamJoinRequest;
import com.yupi.yupao.model.request.TeamQuitRequest;
import com.yupi.yupao.model.request.TeamUpdateRequest;
import com.yupi.yupao.model.vo.TeamUserVo;
import com.yupi.yupao.model.vo.UserVo;
import com.yupi.yupao.service.TeamService;
import com.yupi.yupao.service.UserService;
import com.yupi.yupao.service.UserTeamService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 队伍服务实现类
 */
@Service
public class TeamServiceImpl extends ServiceImpl<TeamMapper, Team>
        implements TeamService {

    @Resource
    private UserTeamService userTeamService;

    @Resource
    private UserService userService;

    @Resource
    private UserMapper userMapper;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public long addTeam(Team team, User loginUser) {
        if (team == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //是否登录
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        //校验信息，队伍人数>=1且<=20
        int maxNum = Optional.ofNullable(team.getMaxNum()).orElse(0);
        if (maxNum < 1 || maxNum > 20) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍人数不满足要求");
        }
        //队伍标题<=20
        String teamName = team.getName();
        if (StringUtils.isBlank(teamName) || teamName.length() > 20) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍标题不满足要求");
        }
        //队伍描述<=512
        String description = team.getDescription();
        if (StringUtils.isNotBlank(description) && teamName.length() > 512) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍描述过长");
        }
        //status是否公开，不传默认为0
        int status = Optional.ofNullable(team.getStatus()).orElse(0);
        TeamStatusEnum statusEnum = TeamStatusEnum.getEnumByValue(status);
        if (statusEnum == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍状态不满足要求");
        }
        //如果status加密，一定要有密码，密码<=32
        String password = team.getPassword();
        if (TeamStatusEnum.SECRET.equals(statusEnum)) {
            if (StringUtils.isBlank(password) || password.length() > 32) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码设置不正确");
            }
        }
        //过期时间>当前时间
        Date expireTime = team.getExpireTime();
        if (new Date().after(expireTime)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "过期时间>当前时间");
        }
        //检验队伍最多创建5个队伍
        QueryWrapper<Team> queryWrapper = new QueryWrapper<>();
        final long userId = loginUser.getId();
        queryWrapper.eq("userId", userId);
        long count = this.count(queryWrapper);
        if (count >= 5) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "最多创建5个队伍");
        }
        //插入队伍信息
        team.setId(null);
        team.setUserId(userId);
        boolean result = this.save(team);
        Long teamId = team.getId();
        if (!result || teamId == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "创建队伍失败");
        }
        //插入用户队伍关系表
        UserTeam userTeam = new UserTeam();
        userTeam.setUserId(userId);
        userTeam.setTeamId(teamId);
        userTeam.setJoinTime(new Date());
        result = userTeamService.save(userTeam);
        if (!result) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "创建队伍失败");
        }
        return teamId;
    }

    @Override
    public List<TeamUserVo> listTeams(TeamQuery teamQuery, boolean isAdmin, User loginUser,boolean isFromTeam) {
        //组合条件查询
        QueryWrapper<Team> queryWrapper = new QueryWrapper<>();
        if (teamQuery != null) {
            Long id = teamQuery.getId();
            if (id != null && id > 0) {
                queryWrapper.eq("id", id);
            }
            List<Long> idList = teamQuery.getIdList();
            if (idList != null) {
                queryWrapper.in("id", idList);
            }
            String searchText = teamQuery.getSearchText();
            if (StringUtils.isNotBlank(searchText)) {
                queryWrapper.and(qw -> qw.like("name", searchText).or().like("description", searchText));
            }
            String name = teamQuery.getName();
            if (StringUtils.isNotBlank(name)) {
                queryWrapper.like("name", name);
            }
            String description = teamQuery.getDescription();
            if (StringUtils.isNotBlank(description)) {
                queryWrapper.like("description", description);
            }
            Integer maxNum = teamQuery.getMaxNum();
            if (maxNum != null && maxNum > 0) {
                queryWrapper.eq("maxNum", maxNum);
            }
            //根据创建人来查询
            Long userId = teamQuery.getUserId();
            if (userId != null && userId > 0) {
                queryWrapper.eq("userId", userId);
            }
            //根据状态查询，只有管理员可以查看非公开的队伍
            //如果是展示队伍页，根据具体要求查询状态，如果是查看自己加入或创建的队伍，则不需要加，直接查出公开和加密的队伍
            if (isFromTeam){
                Integer status = teamQuery.getStatus();
                TeamStatusEnum statusEnum = TeamStatusEnum.getEnumByValue(status);
                if (statusEnum == null) {
                    statusEnum = TeamStatusEnum.PUBLIC;
                }
                if (!isAdmin && TeamStatusEnum.PRIVATE.equals(statusEnum)) {
                    throw new BusinessException(ErrorCode.NO_AUTH);
                }
                queryWrapper.eq("status", statusEnum.getValue());
            }
        }
        //不展示过期的队伍
        //expireTime is null or expireTime>now()
        queryWrapper.and(qw -> qw.isNull("expireTime").or().gt("expireTime", new Date()));

        List<Team> teamList = this.list(queryWrapper);
        if (teamList == null) {
            return new ArrayList<>();
        }
        List<TeamUserVo> teamUserVoList = new ArrayList<>();
        //1.关联查询创建人用户信息，封装到TeamUserVo
        for (Team team : teamList) {
            Long userId = team.getUserId();
            if (userId == null) {
                continue;
            }
            User user = userService.getById(userId);
            TeamUserVo teamUserVo = new TeamUserVo();
            BeanUtils.copyProperties(team, teamUserVo);
            //脱敏创建人用户信息，封装到teamUserVo的createUser属性中
            if (user != null) {
                UserVo userVo = new UserVo();
                BeanUtils.copyProperties(user, userVo);
                teamUserVo.setCreateUser(userVo);
            }
            teamUserVoList.add(teamUserVo);
        }
        //2.判断当前用户是否已加入队伍
        List<Long> teamIdList = teamUserVoList.stream().map(TeamUserVo::getId).collect(Collectors.toList());
        try {
            QueryWrapper<UserTeam> userTeamQueryWrapper = new QueryWrapper<>();
            userTeamQueryWrapper.in("teamId", teamIdList);
            userTeamQueryWrapper.eq("userId", loginUser.getId());
            //查询出当前用户加入了哪些队伍
            List<UserTeam> userTeamList = userTeamService.list(userTeamQueryWrapper);
            //取出用户加入队伍的id列表,对每个队伍进行判断是否包含其中,设置hasJoin
            Set<Long> hasJoinTeamIdList = userTeamList.stream().map(UserTeam::getTeamId).collect(Collectors.toSet());

            teamUserVoList.forEach(teamUserVo -> {
                Long teamId = teamUserVo.getId();
                boolean hasJoin = hasJoinTeamIdList.contains(teamId);
                teamUserVo.setHasJoin(hasJoin);
                //3.查询已加入队伍的人数，并设置hasJoinNum
                QueryWrapper<UserTeam> userJoinNumQueryWrapper = new QueryWrapper<>();
                userJoinNumQueryWrapper.eq("teamId", teamId);
                long count = userTeamService.count(userJoinNumQueryWrapper);
                teamUserVo.setHasJoinNum((int) count);
            });
        } catch (Exception e) {
        }
        return teamUserVoList;
    }

    @Override
    public boolean updateTeam(TeamUpdateRequest teamUpdateRequest, User loginUser) {
        if (teamUpdateRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //判断队伍是否存在
        Long id = teamUpdateRequest.getId();
        Team oldTeam = getTeamById(id);
        //只有队长和管理员可以修改
        if (!loginUser.getId().equals(oldTeam.getUserId()) && userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        //如果修改为加密状态，房间必须设置密码
        TeamStatusEnum statusEnum = TeamStatusEnum.getEnumByValue(teamUpdateRequest.getStatus());
        if (statusEnum.equals(TeamStatusEnum.SECRET)) {
            if (StringUtils.isBlank(teamUpdateRequest.getPassword())) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "加密房间必须要设置密码");
            }
        }
        Team newTeam = new Team();
        BeanUtils.copyProperties(teamUpdateRequest, newTeam);
        boolean result = this.updateById(newTeam);
        return result;
    }

    @Override
    public boolean joinTeam(TeamJoinRequest teamJoinRequest, User loginUser) {
        if (teamJoinRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //判断队伍是否存在
        Long teamId = teamJoinRequest.getTeamId();
        Team team = getTeamById(teamId);
        Date expireTime = team.getExpireTime();
        if (expireTime != null && expireTime.before(new Date())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍已过期");
        }
        Integer status = team.getStatus();
        TeamStatusEnum statusEnum = TeamStatusEnum.getEnumByValue(status);
        if (TeamStatusEnum.PRIVATE.equals(statusEnum)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "禁止加入私有队伍");
        }
        String password = teamJoinRequest.getPassword();
        if (TeamStatusEnum.SECRET.equals(statusEnum)) {
            if (StringUtils.isBlank(password) || !password.equals(team.getPassword())) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码错误");
            }
        }
        //该用户加入的队伍数量
        Long userId = loginUser.getId();
        synchronized (this) {
            QueryWrapper<UserTeam> userTeamQueryWrapper = new QueryWrapper<>();
            userTeamQueryWrapper.eq("userId", userId);
            long hasJoinNum = userTeamService.count(userTeamQueryWrapper);
            if (hasJoinNum >= 5) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户最多创建和加入5个队伍");
            }
            //不能重复加入已加入的队伍
            userTeamQueryWrapper = new QueryWrapper();
            userTeamQueryWrapper.eq("teamId", teamId);
            userTeamQueryWrapper.eq("userId", userId);
            long hasUserJoinTeam = userTeamService.count(userTeamQueryWrapper);
            if (hasUserJoinTeam > 0) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户已加入该队伍");
            }

            //不能加入已满的队伍
            userTeamQueryWrapper = new QueryWrapper();
            userTeamQueryWrapper.eq("teamId", teamId);
            long count = userTeamService.count(userTeamQueryWrapper);
            if (count >= team.getMaxNum()) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍已满");
            }
            //修改用户关联队伍表信息
            UserTeam userTeam = new UserTeam();
            userTeam.setUserId(userId);
            userTeam.setTeamId(teamId);
            userTeam.setJoinTime(new Date());
            return userTeamService.save(userTeam);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean quitTeam(TeamQuitRequest teamQuitRequest, User loginUser) {
        if (teamQuitRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //判断队伍是否存在
        Long teamId = teamQuitRequest.getTeamId();
        Team team = getTeamById(teamId);
        //判断用户是否已加入队伍
        Long userId = loginUser.getId();
        QueryWrapper<UserTeam> userTeamQueryWrapper = new QueryWrapper<>();
        userTeamQueryWrapper.eq("userId", userId);
        userTeamQueryWrapper.eq("teamId", teamId);
        long count = userTeamService.count(userTeamQueryWrapper);
        if (count == 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "未加入队伍");
        }
        //判断已加入队伍的人数
        QueryWrapper<UserTeam> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("teamId", teamId);
        long hasUserJoinTeam = userTeamService.count(queryWrapper);
        //如果队伍只剩一人，解散队伍，
        if (hasUserJoinTeam == 1) {
            //删除队伍,移除关系在最后，提取出来了
            this.removeById(teamId);
        } else {
            //否则判断是否为队长
            if (userId.equals(team.getUserId())) {
                //把队长转移给最早加入队伍的用户
                //1.查出所有用户队伍关系，根据id升序排序，只需取出前两个，队长转让给第二个
                queryWrapper = new QueryWrapper<UserTeam>();
                queryWrapper.eq("teamId", teamId);
                queryWrapper.orderByAsc("id");
                queryWrapper.last("limit 2");
                List<UserTeam> userTeamList = userTeamService.list(queryWrapper);
                if (CollectionUtils.isEmpty(userTeamList) || userTeamList.size() <= 1) {
                    throw new BusinessException(ErrorCode.SYSTEM_ERROR);
                }
                //查出转让的用户
                UserTeam nextUserTeam = userTeamList.get(1);
                Long nextTeamLeaderId = nextUserTeam.getUserId();
                //更新当前队伍的队长id
                Team updateTeam = new Team();
                updateTeam.setId(teamId);
                updateTeam.setUserId(nextTeamLeaderId);
                boolean result = this.updateById(updateTeam);
                if (!result) {
                    throw new BusinessException(ErrorCode.SYSTEM_ERROR, "更新队伍队长失败");
                }
                //更新后，是不是队长最后都要移除关系，提出到外面，减少重复代码
            }
        }
        //移除关系
        return userTeamService.remove(userTeamQueryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteTeam(long id, User loginUser) {
        //验证队伍是否存在
        Team team = getById(id);
        long teamId = team.getId();
        //判断是不是队长
        if (!loginUser.getId().equals(team.getUserId())) {
            throw new BusinessException(ErrorCode.NO_AUTH, "无访问权限");
        }
        //移除所有加入队伍的关联信息
        QueryWrapper<UserTeam> queryWrapper = new QueryWrapper();
        queryWrapper.eq("teamId", teamId);
        boolean remove = userTeamService.remove(queryWrapper);
        if (!remove) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "删除队伍关联信息失败");
        }
        //删除队伍
        boolean result = this.removeById(teamId);
        return result;
    }


    /**
     * 根据teamId查询队伍信息，判断队伍是否存在
     *
     * @param teamId
     * @return
     */
    private Team getTeamById(Long teamId) {
        if (teamId == null || teamId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Team team = this.getById(teamId);
        if (team == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "队伍不存在");
        }
        return team;
    }


}




