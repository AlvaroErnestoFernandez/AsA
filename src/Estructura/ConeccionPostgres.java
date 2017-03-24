package Estructura;

import java.sql.*;

public class ConeccionPostgres{
    
    private Connection conexion = null;
    private int idaulas = 0;
    private int idmaterias = 0;
    public void estableceConexion()
    {
        if (conexion != null)
            return;
        String url = "Jdbc:postgresql://localhost:5432/Seminario/";
        try
        {
           Class.forName("org.postgresql.Driver");
           conexion = DriverManager.getConnection(url,"postgres","410741");
           if (conexion !=null){
               System.out.println("Conexión a base de datos seminario... Ok");
           }
        } catch (Exception e) {
            System.out.println("Problema al establecer la Conexión a la base de datos seminario NO SE POR QUE");
        }
    }
    
    public ResultSet dameTabla(String tabla)
    {
        ResultSet rs = null;
        Statement s = null;
        try
        {
            s = conexion.createStatement();
            rs = s.executeQuery("SELECT * FROM  "+tabla);
        }catch (Exception e)
        {
            System.out.println("Problema al obtener la tabla "+tabla+" de a la base de datos");
        }
        return rs;
    }
    public ResultSet dameTablaMaterias(String tabla, int dia)
    {
        ResultSet rs = null;
        Statement s = null;
        try
        {
            s = conexion.createStatement();
            rs = s.executeQuery("SELECT * FROM  "+tabla+" where materia_dia_id = "+dia+"");
        }catch (Exception e)
        {
            System.out.println("Problema al obtener la tabla materias de la base de datos");
        }
        return rs;
    }
    
    public ResultSet dameTablaMateriasNoAsignadas(int dia)
    {
        ResultSet rs = null;
        Statement s = null;
        try
        {
            s = conexion.createStatement();
            if (dia == 0){
                rs = s.executeQuery("select m.*, d.dia_nombre from materias_no_asignadas m inner join dias d on m.materia_dia_id = d.dia_id");
            }
            else{
                rs = s.executeQuery("select m.*, d.dia_nombre from materias_no_asignadas m inner join dias d on m.materia_dia_id = d.dia_id where d.dia_id = "+dia+"");
            }
        }catch (Exception e)
        {
            System.out.println("PROBLEMA AL OBTENER LAS MATERIAS NO ASIGNADAS");
        }
        return rs;
    }
    public ResultSet dameMateriasNoAsignadas(int dia)
    {
        ResultSet rs = null;
        Statement s = null;
        try
        {
            s = conexion.createStatement();
            if (dia == 0){
                rs = s.executeQuery("select * from materias where not exists(select materia_id from asignaciones)");
            }
            else{
                rs = s.executeQuery("select * from materias where not exists(select materia_id from asignaciones) and materia_dia_id = "+dia+"");
            }
        }catch (Exception e)
        {
            System.out.println("PROBLEMA AL OBTENER LAS MATERIAS NO ASIGNADAS");
        }
        return rs;
    }
    public ResultSet dameTablaMateriasHoras(String tabla, int dia, int horainicio)
    {
        ResultSet rs = null;
        Statement s = null;
        try
        {
            s = conexion.createStatement();
            rs = s.executeQuery("SELECT * FROM  "+tabla+" where materia_dia_id = "+dia+" and materia_hora_inicio = "+horainicio+"");
        }catch (Exception e)
        {
            System.out.println("Problema al obtener la tabla materias de la base de datos de acuerdo a dia y hora de inicio");
        }
        return rs;
    }
    public ResultSet dameTablaAsiganacions(Integer dia)
    {
        ResultSet rs = null;
        Statement s = null;
        try
        {
            s = conexion.createStatement();
            if (dia == 0){
                rs = s.executeQuery("select m.*, au.*, d.dia_nombre from materias m inner join asignaciones a on m.materia_id = a.materia_id inner join aulas au on a.aula_id = au.aula_id inner join dias d on a.dia_id = d.dia_id order by d.dia_id, a.hora_inicio");
            }
            else{
                rs = s.executeQuery("select m.*, au.*, d.dia_nombre from materias m inner join asignaciones a on m.materia_id = a.materia_id inner join aulas au on a.aula_id = au.aula_id inner join dias d on a.dia_id = d.dia_id where  a.dia_id = "+dia+" order by a.hora_inicio");
            }
        }catch (Exception e)
        {
            System.out.println("Problema al obtener la tabla asigancions respecto un dia");
        }
        return rs;
    }
    public void cargarTablaMaterias(String nombre, Integer comision, String orientacion, Integer capacidad, Integer horainicio, Integer horafin)
    {
        idmaterias  = idmaterias + 1;
        String sql = "Insert into materias values ("+idmaterias+", '"+nombre+"', "+comision+", '"+orientacion+"', "+capacidad+", "+horainicio+", "+horafin+", 1)";
        Statement s = null;
        try
        {
            s = conexion.createStatement();
            s.executeUpdate(sql);
        }
        catch (Exception e)
        {
            System.out.println("Problema al cargar datos sobre la tabla materias");
        }
    }
    public void cargarTablaAulas(String nombre, Integer capacidad)
    {
        idaulas  = idaulas + 1;
        String sql = "Insert into aulas(aula_id, aula_nombre, aula_capacidad) values ("+idaulas+", '"+nombre+"', "+capacidad+")";
        Statement s = null;
        try
        {
            s = conexion.createStatement();
            s.executeUpdate(sql);
        }
        catch (Exception e)
        {
            System.out.println("Problema al cargar datos sobre la tabla aulas");
        }
    }

    public void cargarTablaAsignacions(String nombre, Integer comision, String orientacion, Integer capacidad, Integer horainicio, Integer horafin, Integer dia, Integer aulaid)
    {
        Integer idmateria = obtenerIdMateria(nombre, capacidad, comision, horainicio, horafin, orientacion, dia);
        String sql = "Insert into asignaciones values ("+idmateria+", 1, "+horainicio+", "+horafin+", "+aulaid+")";
    
        Statement s = null;
        try
        {
            s = conexion.createStatement();
            s.executeUpdate(sql);
        }
        catch (Exception e)
        {
            System.out.println("Problema al cargar datos sobre la tabla asiganciones");
        }
    }  
   
    
    public int tamañotabla(String tabla){
        int cont = 0;
        Statement s = null;
        ResultSet rs = null;
        try
        {
            s = conexion.createStatement();
            rs = s.executeQuery("select count(*) from "+tabla+"");;
        }catch (Exception e)
        {
            System.out.println("Problema al consultar el tamaño de una tabla");
        }
        try{
        while (rs.next())
        {
            cont = rs.getInt(1);
        }
        } catch(Exception e)
        {
            System.out.println("Problema al obtener datos para consultar el tamaño de una tabla");
        }                
        return cont;             
    }
    public Integer obtenerIdAula(String nombre){
        Integer id = 0;
        Statement s = null;
        ResultSet rs = null;
        try
        {
            s = conexion.createStatement();
            rs = s.executeQuery("select aula_id from aulas where upper(trim(both ' ' from aula_nombre)) = upper(trim(both '' from '"+nombre+"'))");
        }catch (Exception e)
        {
            System.out.println("Problema al obtener el id de un aula ");
        }
        try{
        while (rs.next())
        {
            id = rs.getInt(1);
        }
        } catch(Exception e)
        {
            System.out.println("Problema al leer el id de un aula");
        }  
        return id;
    }
    public void eliminarAula(String nombre){
        Statement s = null;
        Integer id = obtenerIdAula(nombre);
        
        try
        {
            s = conexion.createStatement();
            s.executeUpdate("delete from aulas where aula_id = "+id+"");
        }
        catch (Exception e)
        {
            System.out.println("Problema al eliminar una fila de la tabla aulas POSGREST");
        }        
    }
    public void eliminarAulas(){
        Statement s = null;
        try
        {
            s = conexion.createStatement();
            s.executeUpdate("delete from aulas");
        }
        catch (Exception e)
        {
            System.out.println("Problema al eliminar una fila de la tabla aulas ");
        }
    }
    public void eliminarMateriaYsusAsinganciones(Integer dia){
        Statement s = null;
       
        try
        {
            s = conexion.createStatement();
            s.executeUpdate(" delete from asignaciones where dia_id = "+dia+"; delete from materias where materia_dia_id = "+dia+"");//upper(trim(both '' from materia_nombre)) = upper(trim(both ' ' from '"+nombre+"')) and materia_comision = "+comision+" and upper(trim(both ' ' from materia_orientacion)) = upper(trim(both '' from '"+orientacion+"'));");
        }
        catch (Exception e)
        {
            System.out.println("Problema al eliminar una fila de la tabla materias y asignaciones");
        }        
    }
    public void eliminarAsinganciones(Integer aula){
        Statement s = null;

        try
        {
            s = conexion.createStatement();
            s.executeUpdate(" delete from asignaciones where aula_id = "+aula+";");//upper(trim(both '' from materia_nombre)) = upper(trim(both ' ' from '"+nombre+"')) and materia_comision = "+comision+" and upper(trim(both ' ' from materia_orientacion)) = upper(trim(both '' from '"+orientacion+"'));");
        }
        catch (Exception e)
        {
            System.out.println("Problema al eliminar una fila de la tabla materias y asignaciones");
        }
    }
    
    public void eliminarMateriaYsusAsinganciones(String nombre, Integer comision, String orientacion, Integer capacidad, Integer horainicio, Integer horafin, Integer dia){
        Statement s = null;
        Integer materia_id = obtenerIdMateria(nombre, capacidad, comision, horainicio, horafin, orientacion, dia);
        System.out.println("ENTRA EN ELIMINAR MATERIAS Y SUS ASIGNACIONES");
        try
        {
            s = conexion.createStatement();
            s.executeUpdate(" delete from asignaciones where materia_id = "+materia_id+"; delete from materias where materia_id = "+materia_id+"");//upper(trim(both '' from materia_nombre)) = upper(trim(both ' ' from '"+nombre+"')) and materia_comision = "+comision+" and upper(trim(both ' ' from materia_orientacion)) = upper(trim(both '' from '"+orientacion+"'));");
        }
        catch (Exception e)
        {
            System.out.println("Problema al eliminar una fila de la tabla materias");
        }        
    }
    
    public int obtenerIdMateria(String nombre, Integer capacidad, Integer comision, Integer horainicio, Integer horafin, String orientacion, Integer dia){
        Integer id = 0;
        Statement s = null;
        ResultSet rs = null;
        try
        {
            s = conexion.createStatement();
            rs = s.executeQuery("select materia_id from materias where upper(trim(both ' ' from materia_nombre)) = upper(trim(both ' ' from '"+nombre+"')) and materia_comision = "+comision+" and upper(trim(both ' ' from materia_orientacion)) = upper(trim(both ' ' from '"+orientacion+"')) and materia_capacidad = "+capacidad+" and materia_dia_id = "+dia+" and materia_hora_inicio = "+horainicio+" and materia_hora_fin = "+horafin+"");
        }catch (Exception e)
        {
            System.out.println("Problema al obtener el ID de una materia ");
        }
        try{
        while (rs.next())
        {
            id = rs.getInt(1);
        }
        } catch(Exception e)
        {
            System.out.println("Problema al leer el id de una materia");
        }          
        return id;
    }
    
    public void cierraConexion()
    {
        try
        {
            conexion.close();
        }catch(Exception e)
        {
            System.out.println("Problema para cerrar la Conexión a la base de datos ");
        }
    }
    public void idaulascero(){
        idaulas = 0;
    }
    public void idmateriascero(){
        idmaterias = 0;
    }
    public ResultSet dameAulasDisponibles(Integer hora_inicio,  Integer dia)
    {
        ResultSet rs = null;
        Statement s = null;
        try
        {
            s = conexion.createStatement();
            rs = s.executeQuery("select aula_id, aula_capacidad from aulas where not exists (select aula_id from asignaciones where aula_id = aulas.aula_id and hora_inicio <= "+hora_inicio+" and hora_fin > "+hora_inicio+" and dia_id = "+dia+" )" );
        }catch (Exception e)
        {
            System.out.println("Problema al consultar la base de datos ");
        }
        return rs;
    }  
}