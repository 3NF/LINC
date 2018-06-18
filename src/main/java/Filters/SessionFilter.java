package Filters;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "SessionFilter", urlPatterns = "/user/*")
public class SessionFilter implements Filter
{
    public void destroy()
    {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException
    {
        if(req instanceof HttpServletRequest && resp instanceof HttpServletResponse)
        {
            HttpServletRequest request = (HttpServletRequest) req;
            HttpServletResponse response = (HttpServletResponse) resp;

            if(request.getSession().isNew())
            {
                request.getRequestDispatcher("/loginPage.jsp").forward(request,response);
            }
        }
        chain.doFilter(req, resp);
    }

    public void init(FilterConfig config) throws ServletException
    {

    }

}
