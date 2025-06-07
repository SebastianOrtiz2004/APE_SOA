package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import domain.Marca;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.MarcaService;


import java.io.*;

import java.util.List;

@WebServlet(name="MarcaController", urlPatterns={"/api/marcas","/api/marcas/*"})
public class MarcaController extends HttpServlet {
    private MarcaService svc = new MarcaService();
    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        String path = req.getPathInfo();
        try (PrintWriter out = resp.getWriter()) {
            if (path==null||"/".equals(path)) {
                List<Marca> list = svc.listarTodas();
                out.print(gson.toJson(list));
            } else {
                int id = Integer.parseInt(path.substring(1));
                Marca m = svc.obtenerPorId(id);
                if (m!=null) out.print(gson.toJson(m));
                else {
                    resp.setStatus(404);
                    out.print("{\"error\":\"Marca no encontrada.\"}");
                }
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        JsonObject res = new JsonObject();
        try (BufferedReader r = req.getReader()) {
            JsonObject j = gson.fromJson(r, JsonObject.class);
            String nombre = j.get("nombre").getAsString();
            int id = svc.crear(nombre);
            resp.setStatus(201);
            res.addProperty("id", id);
        } catch (Exception e) {
            resp.setStatus(400);
            res.addProperty("error", e.getMessage());
        }
        try (PrintWriter out = resp.getWriter()) { out.print(gson.toJson(res)); }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        JsonObject res = new JsonObject();
        String path=req.getPathInfo();
        if (path==null||path.equals("/")) {
            resp.setStatus(400);
            res.addProperty("error","ID requerido");
        } else {
            try {
                int id=Integer.parseInt(path.substring(1));
                if (svc.eliminar(id)) res.addProperty("message","Eliminado");
                else {
                    resp.setStatus(404);
                    res.addProperty("error","Marca no encontrada");
                }
            } catch(Exception e) {
                resp.setStatus(400);
                res.addProperty("error","ID inválido");
            }
        }
        try(PrintWriter out=resp.getWriter()){out.print(gson.toJson(res));}
    }
}
