package com.xiaojiezhu.simpletx.client.configuration.enter;

import com.xiaojiezhu.simpletx.core.info.SimpletxTransactionUtil;
import com.xiaojiezhu.simpletx.util.Constant;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author xiaojie.zhu
 * time 2019-01-01 11:40
 */
public class HttpEnterConfigure implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        SimpletxTransactionUtil.clearThreadResource();

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String transactionId = request.getHeader(Constant.SIMPLETX_ENTER_HEADER);

        if(transactionId != null){
            SimpletxTransactionUtil.setTransactionGroupId(transactionId);
        }

        filterChain.doFilter(servletRequest , servletResponse);

    }

    @Override
    public void destroy() {

    }
}
