package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import domain.Modelo;
import service.ModeloService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

@WebServlet(name="ModeloController", urlPatterns={"/api/modelos","/api/modelos/*"})
public class ModeloController extends HttpServlet {
    private ModeloService svc = new ModeloService();
    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        String path=req.getPathInfo();
        try(PrintWriter out=resp.getWriter()){
            if (path==null||"/".equals(path)) {
                List<Modelo> lst=svc.listarTodas();
                out.print(gson.toJson(lst));
            } else {
                String[] parts = path.substring(1).split("/");
                if (parts[0].equals("marca") && parts.length==2) {
                    int idm=Integer.parseInt(parts[1]);
                    List<Modelo> lst=svc.listarPorMarca(idm);
                    out.print(gson.toJson(lst));
                } else {
                    int id=Integer.parseInt(parts[0]);
                    Modelo m=svc.obtenerPorId(id);
                    if (m!=null) out.print(gson.toJson(m));
                    else {resp.setStatus(404);out.print("{\"error\":\"No encontrado\"}");}
                }
            }
        } catch(Exception e){throw new ServletException(e);}
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        JsonObject res=new JsonObject();
        try(BufferedReader r=req.getReader()){
            JsonObject j=gson.fromJson(r,JsonObject.class);
            int idm=j.get("idMarca").getAsInt();
            String nom=j.get("nombre").getAsString();
            int id=svc.crear(idm,nom);
            resp.setStatus(201);
            res.addProperty("id",id);
        } catch(Exception e){
            resp.setStatus(400);
            res.addProperty("error",e.getMessage());
        }
        try(PrintWriter out=resp.getWriter()){out.print(gson.toJson(res));}
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        JsonObject res=new JsonObject();
        String path=req.getPathInfo();
        if(path==null||"/".equals(path)){
            resp.setStatus(400);res.addProperty("error","ID requerido");
        } else {
            try {
                int id=Integer.parseInt(path.substring(1));
                if(svc.eliminar(id)) res.addProperty("message","Eliminado");
                else {resp.setStatus(404);res.addProperty("error","No encontrado");}
            } catch(Exception e){
                resp.setStatus(400);res.addProperty("error","ID inválido");
            }
        }
        try(PrintWriter out=resp.getWriter()){out.print(gson.toJson(res));}
    }
}
