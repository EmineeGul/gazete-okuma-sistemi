package filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "SessionFilter", urlPatterns = {"/panel/*", "/app/*", "/giris.xhtml"})
public class SessionFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (!(request instanceof HttpServletRequest httpRequest)
                || !(response instanceof HttpServletResponse httpResponse)) {
            chain.doFilter(request, response);
            return;
        }

        boolean girisYapilmis = girisYapilmisMi(httpRequest);
        String istekYolu = httpRequest.getRequestURI();
        String loginURI = httpRequest.getContextPath() + "/giris.xhtml";
        String loggedURI = httpRequest.getContextPath() + "/index.xhtml";

        if (girisYapilmis && istekYolu.endsWith("/giris.xhtml")) {
            httpResponse.sendRedirect(loggedURI);
            return;
        }

        if (girisYapilmis) {
            chain.doFilter(request, response);
            return;
        }

        if (istekYolu.endsWith("/giris.xhtml")) {
            chain.doFilter(request, response);
            return;
        }

        httpResponse.sendRedirect(loginURI);
    }

    private boolean girisYapilmisMi(HttpServletRequest httpRequest) {
        try {
            return httpRequest.getSession(false) != null
                    && httpRequest.getSession(false).getAttribute("user") != null;
        } catch (Exception e) {
            return false;
        }
    }
}
