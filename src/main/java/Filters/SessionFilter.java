package Filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Data.Constraints;

@WebFilter(filterName = "SessionFilter", urlPatterns = "/user/*")
public class SessionFilter implements Filter
{
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException
    {
        if(req instanceof HttpServletRequest && resp instanceof HttpServletResponse)
        {
            HttpServletRequest request = (HttpServletRequest) req;
            HttpServletResponse response = (HttpServletResponse) resp;
            if(request.getSession().isNew() || request.getSession().getAttribute(Constraints.USER) == null)
            {
                request.getRequestDispatcher("/loginPage.jsp").forward(request,response);
                return;
            }
        }
        chain.doFilter(req, resp);
    }

    public void init(FilterConfig config)
    {

    }

}
