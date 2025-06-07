package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import domain.Vehiculo;
import service.VehiculoService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

@WebServlet(name="VehiculoController", urlPatterns={"/api/vehiculos","/api/vehiculos/*"})
public class VehiculoController extends HttpServlet {
    private VehiculoService svc = new VehiculoService();
    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        String path=req.getPathInfo();
        try(PrintWriter out=resp.getWriter()){
            if(path==null||"/".equals(path)){
                List<Vehiculo> lst=svc.listarTodos();
                out.print(gson.toJson(lst));
            } else {
                int id=Integer.parseInt(path.substring(1));
                Vehiculo v=svc.obtenerPorId(id);
                if(v!=null) out.print(gson.toJson(v));
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
            int idM=j.get("idMarca").getAsInt();
            int idMo=j.get("idModelo").getAsInt();
            String pla=j.get("placa").getAsString();
            String cha=j.get("chasis").getAsString();
            String ani=j.get("anio").getAsString();
            int idC=j.get("idColor").getAsInt();
            int id=svc.crearVehiculo(idM,idMo,pla,cha,ani,idC);
            resp.setStatus(201);
            res.addProperty("idVehiculo",id);
        } catch(Exception e){
            resp.setStatus(400);
            res.addProperty("error",e.getMessage());
        }
        try(PrintWriter out=resp.getWriter()){out.print(gson.toJson(res));}
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        JsonObject res=new JsonObject();
        String path=req.getPathInfo();
        if(path==null||"/".equals(path)) {
            resp.setStatus(400);
            res.addProperty("error","ID requerido");
        } else {
            try {
                int id=Integer.parseInt(path.substring(1));
                BufferedReader r=req.getReader();
                JsonObject j=gson.fromJson(r,JsonObject.class);
                int idM=j.get("idMarca").getAsInt();
                int idMo=j.get("idModelo").getAsInt();
                String pla=j.get("placa").getAsString();
                String cha=j.get("chasis").getAsString();
                String ani=j.get("anio").getAsString();
                int idC=j.get("idColor").getAsInt();
                boolean ok=svc.actualizarVehiculo(id,idM,idMo,pla,cha,ani,idC);
                if(ok) res.addProperty("message","Actualizado");
                else { resp.setStatus(404);res.addProperty("error","No encontrado"); }
            } catch(Exception e){
                resp.setStatus(400);
                res.addProperty("error",e.getMessage());
            }
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
                if(svc.eliminarVehiculo(id)) res.addProperty("message","Eliminado");
                else{resp.setStatus(404);res.addProperty("error","No encontrado");}
            } catch(Exception e){
                resp.setStatus(400);res.addProperty("error",e.getMessage());
            }
        }
        try(PrintWriter out=resp.getWriter()){out.print(gson.toJson(res));}
    }
}
