package filter;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(urlPatterns={"/api/*"})
public class ApiExceptionFilter implements Filter {
    private Gson gson = new Gson();

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletResponse resp = (HttpServletResponse)res;
        try {
            chain.doFilter(req,res);
        } catch(Exception e) {
            e.printStackTrace();
            resp.setContentType("application/json;charset=UTF-8");
            resp.setStatus(500);
            JsonObject j=new JsonObject();
            j.addProperty("status","error");
            j.addProperty("error","Error interno. Contacta al administrador.");
            resp.getWriter().print(gson.toJson(j));
        }
    }
}
