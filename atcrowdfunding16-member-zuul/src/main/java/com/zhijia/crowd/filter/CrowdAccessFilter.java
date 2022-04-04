package com.zhijia.crowd.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.zhijia.crowd.constart.AccessPassResources;
import com.zhijia.crowd.constart.CrowdConstant;
import com.zhijia.crowd.entity.vo.MemberLoginVO;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author zhijia
 * @create 2022-03-30 11:31
 */
@Component
public class CrowdAccessFilter extends ZuulFilter {
    @Override
    public String filterType() {
        //目标微服务前
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        //获取servletPath
        RequestContext currentContext = RequestContext.getCurrentContext();
        HttpServletRequest request = currentContext.getRequest();
        String servletPath = request.getServletPath();
        System.out.println(servletPath);
        //需要放行的页面和静态资源
        boolean containsResult = AccessPassResources.PASS_RES_SET.contains(servletPath);

        if (containsResult) {
            return false;
        }
        boolean judegeStaticResource = AccessPassResources.judegeCurrentServletPathWetherStaticResource(servletPath);

        if (judegeStaticResource) {
            return false;
        }
        //拦截，可以进行登录检查
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        //获取session对象
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpSession session = requestContext.getRequest().getSession();
        //获取已登录用户
        MemberLoginVO loginMember = (MemberLoginVO)session.getAttribute(CrowdConstant.ATTR_NAME_LOGIN_MEMBER);
        //判断是否为空
        if (loginMember == null) {
            //返回消息
            session.setAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_ACCESS_FORBIDEN);
            //重定向
            HttpServletResponse response = requestContext.getResponse();
            try {
                response.sendRedirect("/auth/member/to/login/page");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //放行
        return null;
    }
}
