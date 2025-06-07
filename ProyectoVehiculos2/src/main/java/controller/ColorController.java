package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import domain.Color;
import service.ColorService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

@WebServlet(name="ColorController", urlPatterns={"/api/colores","/api/colores/*"})
public class ColorController extends HttpServlet {
    private ColorService svc = new ColorService();
    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        String path=req.getPathInfo();
        try(PrintWriter out=resp.getWriter()){
            if(path==null||"/".equals(path)){
                List<Color> lst=svc.listarTodas();
                out.print(gson.toJson(lst));
            } else {
                int id=Integer.parseInt(path.substring(1));
                Color c=svc.obtenerPorId(id);
                if(c!=null) out.print(gson.toJson(c));
                else{resp.setStatus(404);out.print("{\"error\":\"No encontrado\"}");}
            }
        } catch(Exception e){throw new ServletException(e);}
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        JsonObject res=new JsonObject();
        try(BufferedReader r=req.getReader()){
            JsonObject j=gson.fromJson(r,JsonObject.class);
            String nom=j.get("nombre").getAsString();
            int id=svc.crear(nom);
            resp.setStatus(201);
            res.addProperty("id",id);
        } catch(Exception e){
            resp.setStatus(400);
            res.addProperty("error",e.getMessage());
        }
        try(PrintWriter out=resp.getWriter()){out.print(gson.toJson(res));}
    }

@Override
protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
    resp.setContentType("application/json;charset=UTF-8");
    JsonObject json = new JsonObject();

    String path = req.getPathInfo(); // "/5"
    if (path == null || path.equals("/")) {
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        json.addProperty("error", "ID requerido en la URL.");
        resp.getWriter().print(json.toString());
        return;
    }

    int id;
    try {
        id = Integer.parseInt(path.substring(1));
    } catch (NumberFormatException e) {
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        json.addProperty("error", "ID inválido");
        resp.getWriter().print(json.toString());
        return;
    }

    try {
        boolean ok = svc.eliminar(id);
        if (ok) {
            json.addProperty("message", "Eliminado");
            resp.getWriter().print(json.toString());
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            json.addProperty("error", "No encontrado");
            resp.getWriter().print(json.toString());
        }
    } catch (Exception e) {
        String msg = e.getMessage();
        if (msg.startsWith("No se puede eliminar el color")) {
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
            json.addProperty("error", msg);
        } else {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            json.addProperty("error", "Error interno: " + msg);
        }
        resp.getWriter().print(json.toString());
    }
}



}
