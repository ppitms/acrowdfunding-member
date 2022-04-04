package com.zhijia.crowd.service.api;

import com.zhijia.crowd.entity.po.ProjectPO;
import com.zhijia.crowd.entity.vo.DetailProjectVO;
import com.zhijia.crowd.entity.vo.PortalTypeVO;
import com.zhijia.crowd.entity.vo.ProjectVO;

import java.util.List;

/**
 * @author zhijia
 * @create 2022-03-30 21:38
 */
public interface ProjectService {
    void saveProject(ProjectVO projectVO, Integer memberId);

    List<PortalTypeVO> getPortalProjectVO();

    DetailProjectVO getDetailProjectVO(Integer projectId);
}
