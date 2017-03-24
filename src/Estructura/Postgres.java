/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Estructura;

import java.sql.*;
import java.text.DateFormat;
import java.util.*;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Postgres{

    private Connection conexion = null;
    private int idaulas = 0;
    private int idmaterias = 0;
    public void estableceConexion()
    {
        if (conexion != null)
            return;
        String url = "jdbc:postgresql://127.0.0.1:5432/Seminario";
        //String url = "Jdbc:postgresql://localhost:5432/Seminario/"; no funcionaba en la DELL
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
    //procedimientos y funciones de la señorita cariño
    public void cargarTablaAulaPato(String nombre,int capacidad,String zona,boolean estado)
    {
        int id=this.obtenerUltimoAulaId();
        String sql= "insert into aulas (aula_id,aula_nombre,aula_capacidad,aula_habilitada,aula_zona)values("+id+",'"+nombre+"',"+capacidad+","+estado+",'"+zona+"')";
        Statement s = null;
                try
        {
            s = conexion.createStatement();
            s.executeUpdate(sql);
        }
        catch (SQLException e)
        {
            System.out.println("Problema al cargar datos sobre la tabla AULAS");

        }
    }
    public int obtenerUltimoAulaId() //throws SQLException
    {
         ResultSet rs = null;
         Statement s = null;
         int id=0 ;
     try{
         s = conexion.createStatement();
         rs = s.executeQuery("SELECT aula_id FROM aulas ORDER BY aula_id DESC LIMIT 1");
     }
      catch (Exception e){
          System.out.println("Problema al obtener el ultimo ID de la tabla Aulas");
      }
     try{
        while (rs.next()){
                  id = rs.getInt(1);
                }
       }
     catch (Exception e){
         System.out.println ("Problema para mostrar id de ultima aula ingresada");
     }
      id=id+1;
      return id;
    }
    public int buscarAulaId(String nombre,int capacidad,String zona){

        ResultSet rs = null;
        Statement s = null;
        int id = 0;
        try
        {
            s = conexion.createStatement();
            rs = s.executeQuery("SELECT aula_id FROM  aulas where aula_nombre='"+nombre+"' and aula_capacidad="+capacidad+" and aula_zona='"+zona+"'");
        }catch (SQLException e)
        {
            System.out.println("Problema al obtener el id de la Aula");
        }
        try{
        while (rs.next())
        {
            id = rs.getInt(1);
        }
        } catch(SQLException e)
        {
            System.out.println("Problema al obtener el id de la materia 2");
        }
        return id;
    }
    public void moidificarAula(int id,String nombre,int capacidad,boolean estado,String zona)
    {//a este procedimiento debo revisarlo
        Statement s ;
        try
        {
            s = conexion.createStatement();
            s.executeQuery("update aulas set aula_nombre='"+nombre+"',aula_capacidad="+capacidad+",aula_habilitada="+estado+",aula_zona='"+zona+"' where aula_id="+id);
        }catch (SQLException e)
        {
            System.out.println("Problema al modificar la tabla Aulas "+nombre+"o xq falta el Result Set pero igual actualiza la tabla");
        }
    }
    //hasta aca estan los procedicimientos de la cariño
    public void cargarTablaMaterias(String nombre,int facultad,String encargado)
    {
        //idmaterias  = idmaterias + 1;
        //facultad=facultad + 1;
        String sql = "Insert into materias (materia_nombre,materia_jefcatedra,facultad_id) values ('"+nombre+"','"+encargado+"',"+facultad+")";
        Statement s = null;
        System.out.println(facultad);
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
    public ResultSet obtenerTabla(String tabla)
    {
        ResultSet rs = null;
        Statement s = null;
        try
        {
            s = conexion.createStatement();
            rs = s.executeQuery("SELECT * FROM  "+tabla+" order by 2");
        }catch (Exception e)
        {
            System.out.println("Problema al obtener la tabla "+tabla+" de a la base de datos");
        }
        return rs;
    }
    public ResultSet obtenerFacultad(int facultad){
        ResultSet rs = null;
        Statement s = null;
        String sfacultad;
        try
        {
            s = conexion.createStatement();
            rs = s.executeQuery("SELECT facultad_nombre FROM  facultades where facultad_id="+facultad);
        }catch (Exception e)
        {
            System.out.println("Problema al obtener la facultad "+facultad+" de a la base de datos");
        }
        return rs;
    }
    public int buscarMateriaId(String materia,int facultad,String encargado){

        ResultSet rs = null;
        Statement s = null;
        int id = 0;
        try
        {
            s = conexion.createStatement();
            rs = s.executeQuery("SELECT materia_id FROM  materias where materia_nombre='"+materia+"' and materia_jefcatedra='"+encargado+"' and facultad_id="+facultad);
        }catch (Exception e)
        {
            System.out.println("Problema al obtener el id de la materia");
        }
        try{
        while (rs.next())
        {
            id = rs.getInt(1);
        }
        } catch(Exception e)
        {
            System.out.println("Problema al obtener el id de la materia 2");
        }
        return id;
    }
    public void moidificarMateria(int id,String materia,int facultad,String encargado){//a este procedimiento debo revisarlo
        Statement s = null;
        try
        {
            s = conexion.createStatement();
            s.executeQuery("update materias set materia_nombre='"+materia+"',materia_jefcatedra='"+encargado+"',facultad_id="+facultad+" where materia_id="+id);
        }catch (Exception e)
        {
            System.out.println("Problema al modificar la tabla materia"+materia+encargado+facultad);
        }
    }
    public void cargarTablaComisiones(int idm,int ncomision, String tipo, String responsable, String box, int capacidad) {
        String sql = "Insert into comisiones (comision_numero,comision_encargado,comision_box,comision_tipo,materia_id,comision_alumnos) values ("+ncomision+",'"+responsable+"','"+box+"','"+tipo+"',"+idm+","+capacidad+")";
        Statement s = null;
        try
        {
            s = conexion.createStatement();
            s.executeUpdate(sql);
        }
        catch (Exception e)
        {
            System.out.println("Problema al cargar datos sobre la tabla comisiones");
        }
    }
    public ResultSet obtenerComisionesMateria(int id)
    {
        ResultSet rs = null;
        Statement s = null;
        try
        {
            s = conexion.createStatement();
            rs = s.executeQuery("SELECT * FROM  comisiones where materia_id="+id);
        }catch (Exception e)
        {
            System.out.println("Problema al obtener las comisiones de la materia");
        }
        return rs;
    }

    public int cantidadComisiones(int idm){
        ResultSet rs = null;
        Statement s = null;
        int cant = 0;
        try
        {
            s = conexion.createStatement();
            rs = s.executeQuery("SELECT * FROM  comisiones where materia_id="+idm);
        }catch (Exception e)
        {
            System.out.println("Problema al obtener la cantidad de comisones de una materia");
        }
         try{
        while (rs.next())
        {
            cant++;
        }
        } catch(Exception e)
        {
            System.out.println("Problema al obtener la cantidad de comisiones");
        }
        return cant;
    }
    public void cargarTablaComisionesxDia(int id_d,int id_c, int hi, int hf) {
        String sql = "Insert into comisionesxdia (dia_id,comision_id,cxd_horainicio,cxd_horafin) values ("+id_d+","+id_c+","+hi+","+hf+")";
        Statement s = null;
        try
        {
            s = conexion.createStatement();
            s.executeUpdate(sql);
        }
        catch (Exception e)
        {
            System.out.println("Problema al cargar datos sobre la tabla ComisionesxDia "+id_d+id_c+hi+hf);
        }
    }
    public int buscarComisionId(int idm,int ncomision, String tipo, String responsable, String box, int capacidad){
        ResultSet rs = null;
        Statement s = null;
        int id = 0;
        try
        {
            s = conexion.createStatement();
            rs = s.executeQuery("Select comision_id from comisiones where comision_numero="+ncomision+" and comision_encargado='"+responsable+"' and comision_box='"+box+"' and comision_tipo='"+tipo+"' and materia_id="+idm+" and comision_alumnos="+capacidad+"");
        }catch (Exception e)
        {
            System.out.println("Problema al obtener la cantidad de comisones de una materia");
        }
         try{
        while (rs.next())
        {
          id = rs.getInt(1);
        }
        } catch(Exception e)
        {
            System.out.println("Problema al obtener el id de la comision");
        }
        return id;
    }
    public ResultSet obtenerDiasComision(int id)
    {
        ResultSet rs = null;
        Statement s = null;
        try
        {
            s = conexion.createStatement();
            rs = s.executeQuery("select * from comisionesxdia where comision_id="+id);
        }catch (Exception e)
        {
            System.out.println("Problema al obtener los dias de la comision "+id);
        }
        return rs;
    }

    public void moidificarComision(int idc,int ncomision, String tipo, String responsable, String box, int capacidad){//a este procedimiento debo revisarlo
        Statement s = null;
        try
        {
            s = conexion.createStatement();
            s.executeQuery("update comisiones set comision_numero="+ncomision+",comision_encargado='"+responsable+"',comision_box='"+box+"',comision_tipo='"+tipo+"',comision_alumnos="+capacidad+" where comision_id="+idc);
        }catch (Exception e)
        {
            System.out.println("Problema al modificar la tabla comisiones "+idc+ncomision+tipo+responsable+box+capacidad);
        }
    }
    public void moidificarComisionDias(int id_d,int id_c, int hi, int hf){//a este procedimiento debo revisarlo
        Statement s = null;
        try
        {
            s = conexion.createStatement();
            s.executeQuery("update comisionesxdia set dia_id="+id_d+",cxd_horainicio="+hi+",cxd_horafin="+hf+" where comisionxdia="+id_c);
        }catch (Exception e)
        {
            System.out.println("Problema al modificar la tabla comisionesxdia ");
        }
    }
    public void borrarComisionesDia(int cm){
        Statement s = null;
        try
        {
            s = conexion.createStatement();
            s.executeQuery("delete from comisionesxdia where comision_id="+cm);
        }catch (Exception e)
        {
            System.out.println("Problema al borrar comisionesxdia");
        }
    }
    public void borrarComsionesAsignacion(int cm){
        Statement s = null;
        try
        {
            s = conexion.createStatement();
            s.executeQuery("delete from asignaciones where comision_id="+cm);
        }catch (Exception e)
        {
            System.out.println("Problema al borrar las comisiones asignadas");
        }
    
    }

    public ResultSet dameAulasDisponibles2(Integer dia,Integer hora_inicio)
    {
        ResultSet rs = null;
        Statement s = null;
        try
        {
            s = conexion.createStatement();//HAY QUE TENER EN CUENTA QUE LA SIGUIENTE CONSULTA SQL NO CONSIDERA LA HORA FIN POR QUE SE SUPONE QUE ESTAMOS TRABAJANDO EN FOR QUE RECORRE LAS HORAS DESDE LAS O HASTA LAS 23
            rs = s.executeQuery("select aula_id, aula_capacidad from aulas where not exists (select aula_id from asignaciones where aula_id = aulas.aula_id and asignacion_horainicio <= "+hora_inicio+" and asignacion_horafin >= "+hora_inicio+" and dia_id = "+dia+" )" );
        }catch (Exception e)
        {
            System.out.println("Problemas al obtener aulas disponible ");
        }
        return rs;
    }

    public ResultSet obtenerComisionesXDiaHora(int dia, int horainicio)
    {
        ResultSet rs = null;
        Statement s = null;
        try
        {
            s = conexion.createStatement();
            rs = s.executeQuery("select comisionxdia_id,comision_tipo,comisiones.comision_id,comision_alumnos,cxd_horainicio,cxd_horafin from comisiones inner join comisionesxdia on comisiones.comision_id = comisionesxdia.comision_id where dia_id="+dia+" and cxd_horainicio="+horainicio+"");
        }catch (Exception e)
        {
            System.out.println("Problema al obtener la tabla materias de la base de datos de acuerdo a dia y hora de inicio");
        }
        return rs;
    }
    public void cargarTablaAsignacions2(String nombre, Integer comision, String orientacion, Integer capacidad, Integer horainicio, Integer horafin, Integer dia, Integer aulaid, Date desde,Date hasta)
    {
        int nombrei=Integer.parseInt(nombre);
        while((this.obtenerDiaSemana(desde)-1)!=dia){
            desde = this.sumarRestarDiasFecha(desde, 1);
        }
        while (desde.before(hasta)){
            String sql = "Insert into asignaciones (asignacion_fecha,asignacion_horainicio,asignacion_horafin,dia_id,aula_id,comisionxdia_id,comision_id) values ('"+desde+"',"+horainicio+", "+horafin+","+dia+", "+aulaid+","+nombrei+","+comision+")";

            Statement s = null;
            try
            {
                s = conexion.createStatement();
                s.executeUpdate(sql);
            }
            catch (Exception e)
            {
                System.out.println("Problema al cargar datos sobre la tabla asiganciones "+horainicio+"; "+horafin+"; "+dia+"; "+aulaid+"; "+nombrei+"; "+comision+"");
            }
            desde = this.sumarRestarDiasFecha(desde, 7);
        }
        if(!(hasta.before(desde))){
            if((this.obtenerDiaSemana(desde)-1)==dia){
                String sql = "Insert into asignaciones (asignacion_fecha,asignacion_horainicio,asignacion_horafin,dia_id,aula_id,comisionxdia_id,comision_id) values ('"+desde+"',"+horainicio+", "+horafin+","+dia+", "+aulaid+","+nombrei+","+comision+")";

            Statement s = null;
            try
            {
                s = conexion.createStatement();
                s.executeUpdate(sql);
            }
            catch (Exception e)
            {
                System.out.println("Problema al cargar datos sobre la tabla asiganciones 2 "+horainicio+"; "+horafin+"; "+dia+"; "+aulaid+"; "+nombrei+"; "+comision+"");
            }
            }
        }
    }

    public static int obtenerDiaSemana(Date d){
	GregorianCalendar cal = new GregorianCalendar();
	cal.setTime(d);
	return cal.get(Calendar.DAY_OF_WEEK);
    }
    public static Date ParseFecha(String fecha)
    {
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        Date fechaDate = null;
        try {
            fechaDate = formato.parse(fecha);
        }
        catch (ParseException ex)
        {
            System.out.println(ex);
        }
        return fechaDate;
    }
    public Date sumarRestarDiasFecha(Date fecha, int dias){
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(fecha); // Configuramos la fecha que se recibe
      calendar.add(Calendar.DAY_OF_YEAR, dias);  // numero de días a añadir, o restar en caso de días<0
      return calendar.getTime(); // Devuelve el objeto Date con los nuevos días añadidos
    }
    public void borrarAsignaciones(){
        Statement s = null;
        try
        {
            s = conexion.createStatement();
            s.executeQuery("delete from asignaciones");
        }catch (Exception e)
        {
            System.out.println("Problema al borrar el contenido de la tabla asignaciones");
        }
    }
    public boolean asignacionesVacia(){
        boolean b = true;
        ResultSet rs = null;
        Statement s = null;
        try
        {
            s = conexion.createStatement();
            rs = s.executeQuery("select * from asignaciones");
        }catch (Exception e)
        {
            System.out.println("Problema al saber si la tabla asignaciones esta vacia");
        }
         try{
        while (rs.next())
        {
          b=false;
        }
        } catch(Exception e)
        {
            System.out.println("Problema el valor boolean");
        }
        return b;
    }
    public ResultSet obtenerMateriasNoAsignadas(){
        ResultSet rs = null;
        Statement s = null;
        try
        {
            s = conexion.createStatement();
            rs = s.executeQuery("SELECT materia_nombre,facultad_nombre,materia_jefcatedra,comision_numero,dia_nombre,comision_alumnos,cxd_horainicio,cxd_horafin,comision_encargado,comision_box FROM comisionesxdia inner join comisiones on comisiones.comision_id=comisionesxdia.comision_id inner join materias on materias.materia_id=comisiones.materia_id inner join facultades on facultades.facultad_id=materias.facultad_id inner join dias on dias.dia_id=comisionesxdia.dia_id WHERE NOT EXISTS (SELECT * FROM asignaciones WHERE comisionesxdia.comisionxdia_id=asignaciones.comisionxdia_id)");
        }catch (Exception e)
        {
            System.out.println("Problema al obtener las materias no asignadas");
        }
        return rs;
    }
    public void borrarComisionComisionesxDia(int idc){
        Statement s = null;
        try
        {
            s = conexion.createStatement();
            s.executeQuery("delete from asignaciones where comision_id="+idc+";delete from comisionesxdia where comision_id="+idc+";delete from comisiones where comision_id="+idc+"");
        }catch (Exception e)
        {
            System.out.println("Problema al borrar comisiones y comisionesxdia");
        }
    }
    public void borrarMateria(int idm){
        Statement s = null;
        try
        {
            s = conexion.createStatement();
            s.executeQuery("delete from materias where materia_id="+idm+"");
        }catch (Exception e)
        {
            System.out.println("Problema al borrar materia");
        }
    }
    public int obtnerIdMateria(String materia,String jefecatedra,String facultad){
        ResultSet rs = null;
        Statement s = null;
        int id = 0;
        try
        {
            s = conexion.createStatement();
            rs = s.executeQuery("select materia_id from materias inner join facultades on facultades.facultad_id=facultades.facultad_id where materia_nombre= '"+materia+"' and materia_jefcatedra='"+jefecatedra+"' and facultad_nombre='"+facultad+"'");
        }catch (Exception e)
        {
            System.out.println("Problema al obtener la cantidad de comisones de una materia");
        }
         try{
        while (rs.next())
        {
          id = rs.getInt(1);
        }
        } catch(Exception e)
        {
            System.out.println("Problema al obtener el id de la comision");
        }
        return id;
    }
    public ResultSet obtenerDiasComision2(int id)
    {
        ResultSet rs = null;
        Statement s = null;
        try
        {
            s = conexion.createStatement();
            rs = s.executeQuery("select dia_nombre,cxd_horainicio,cxd_horafin,comisionxdia_id,dias.dia_id from comisionesxdia inner join dias on comisionesxdia.dia_id=dias.dia_id where comision_id="+id);
        }catch (Exception e)
        {
            System.out.println("Problema al obtener los dias de la comision "+id);
        }
        return rs;
    }
    public ResultSet obtenerAulas(){
        ResultSet rs = null;
        Statement s = null;
        try
        {
            s = conexion.createStatement();
            rs = s.executeQuery("select aula_nombre, aula_capacidad, aula_id from aulas");//ojo no modifiquemos los metodos de otros
            //esto puede generar problemas cuando exportamos las versiones
        }catch (Exception e)
        {
            System.out.println("Problema al obtener aulas");
        }
        return rs;
    }
    public int obtenerAulaAsignada(String idcxd){
        ResultSet rs = null;
        Statement s = null;
        int id = 0;
        try
        {
            s = conexion.createStatement();
            rs = s.executeQuery("select aula_id from asignaciones where comisionxdia_id="+idcxd);
        }catch (Exception e)
        {
            System.out.println("Problema al obtener el aula ASIGNADA");
        }
         try{
        while (rs.next())
        {
          id = rs.getInt(1);
        }
        } catch(Exception e)
        {
            System.out.println("Problema al obtener el aula ASIGNADA");
        }
        return id;
    }
    public int obtenerAulaId(String nombre){
     ResultSet rs = null;
        Statement s = null;
        int id = 0;
        try
        {
            s = conexion.createStatement();
            rs = s.executeQuery("select aula_id from aulas where aula_nombre='"+nombre+"'");
        }catch (Exception e)
        {
            System.out.println("Problema al obtener el id del aula");
        }
         try{
        while (rs.next())
        {
          id = rs.getInt(1);
        }
        } catch(Exception e)
        {
            System.out.println("Problema al obtener el id del aula");
        }
        return id;
    }
    public String obtenerAulaNombre(int idaula){
     ResultSet rs = null;
        Statement s = null;
        String nombre=null;
        try
        {
            s = conexion.createStatement();
            rs = s.executeQuery("select aula_nombre from aulas where aula_id="+idaula);
        }catch (Exception e)
        {
            System.out.println("Problema al obtener el nombre del aula");
        }
         try{
        while (rs.next())
        {
          nombre = rs.getString(1);
        }
        } catch(Exception e)
        {
            System.out.println("Problema al obtener el nombre del aula");
        }
        return nombre;
    }
    public void obtenerAsignaciones(Vector<Integer> v,int aula,int dia,Date desde,Date hasta,int hi,int hf){
        //este metodo agrega los id de las asignaciones que estan en conflicto con la nueva asignacion
        //parece que esta ok pero mejor si le hacen mas pruebas
        ResultSet rs = null;
        Statement s = null;
        try
        {
            s = conexion.createStatement();
            rs = s.executeQuery("Select asignacion_id,asignacion_horainicio,asignacion_horafin,aula_id from asignaciones where aula_id="+aula+" and dia_id="+dia+" and asignacion_fecha>='"+desde+"' and asignacion_fecha<='"+hasta+"' and asignacion_horainicio>="+hi);
        }catch (Exception e)
        {
            System.out.println("Problema al obtener las asignaciones que estan cargadas");
        }
         try{
        while (rs.next())
        {

           int i = rs.getInt(2);//9   10
           int f = rs.getInt(3);//10   13
           System.out.println("hi="+hi+" hf="+hf+" i="+i+" f="+f);
           //hi=9
           //hf=14
           if((i<=hi&&hi<f)){
               v.add(rs.getInt(1));
           }else{
              if((f<=hf)){
               v.add(rs.getInt(1));
           }
           }
           if((i<hf&&hf<f)){
               v.add(rs.getInt(1));
           }
           
        }
        } catch(Exception e)
        {
            System.out.println("Problema al obtener las asignaciones que estan cargadas 2");
        }

    }
    public void eliminarAsignacion(int ida){
        Statement s = null;
        try
        {
            s = conexion.createStatement();
            s.executeQuery("delete from asignaciones where asignacion_id="+ida+"");
        }catch (Exception e)
        {
            System.out.println("Problema al eliminar asignacion");
        }
    }
    public void borrarAsignaciones(Date desde, Date hasta){

                Statement s = null;
        try
        {
            s = conexion.createStatement();
            s.executeQuery("delete from asignaciones where asignacion_fecha>='"+desde+"' and asignacion_fecha<='"+hasta+"'");
        }catch (Exception e)
        {
            System.out.println("Problema al borrar asignaciones");
        }
    }
    public boolean asignacionesVaciaFecha(Date desde,Date hasta){
        boolean b = true;
        ResultSet rs = null;
        Statement s = null;
        try
        {
            s = conexion.createStatement();
            rs = s.executeQuery("select * from asignaciones where asignacion_fecha>='"+desde+"' or asignacion_fecha<='"+hasta+"'");
        }catch (Exception e)
        {
            System.out.println("Problema al saber si existen asignaciones entre fechas");
        }
         try{
        while (rs.next())
        {
          b=false;
        }
        } catch(Exception e)
        {
            System.out.println("Problema el valor boolean");
        }
        return b;
    }
    public ResultSet obtenerMateriasAsignadas(Date desde, Date hasta){
        ResultSet rs = null;
        Statement s = null;
        try
        {
            s = conexion.createStatement();
            rs = s.executeQuery("select materia_nombre,facultad_nombre,materia_jefcatedra,comision_numero,dia_nombre,comision_alumnos,cxd_horainicio,cxd_horafin,comision_encargado,comision_box,asignacion_fecha,aula_nombre from asignaciones inner join aulas on asignaciones.aula_id=aulas.aula_id inner join comisionesxdia on asignaciones.comisionxdia_id=comisionesxdia.comisionxdia_id inner join comisiones on comisionesxdia.comision_id=comisiones.comision_id inner join materias on comisiones.materia_id=materias.materia_id inner join dias on comisionesxdia.dia_id=dias.dia_id inner join facultades on materias.facultad_id=facultades.facultad_id where asignacion_fecha>='"+desde+"' and asignacion_fecha<='"+hasta+"' order by asignacion_fecha, dias.dia_id");
        }catch (Exception e)
        {
            System.out.println("Problema al obtener las materias asignadas entre unas fechas");
        }
        return rs;
    }
    public ResultSet obtenerEncargadosComisiones(){
        ResultSet rs = null;
        Statement s = null;
        try
        {
            s = conexion.createStatement();
            rs = s.executeQuery("select comision_encargado from comisiones where NOT EXISTS (SELECT * FROM materias WHERE comisiones.comision_encargado=materias.materia_jefcatedra) group by comision_encargado;");
        }catch (Exception e)
        {
            System.out.println("Problema al obtener encargados comisiones");
        }
        return rs;
    }
    public ResultSet obtenerEncargadosMaterias(){
        ResultSet rs = null;
        Statement s = null;
        try
        {
            s = conexion.createStatement();
            rs = s.executeQuery("select materia_jefcatedra from materias where NOT EXISTS (SELECT * FROM comisiones WHERE materias.materia_jefcatedra=comisiones.comision_encargado) group by materia_jefcatedra;");
        }catch (Exception e)
        {
            System.out.println("Problema al obtener encargados materias");
        }
        return rs;
    }
     public ResultSet obtenerEncargadosCursos(){
        ResultSet rs = null;
        Statement s = null;
        try
        {
            s = conexion.createStatement();
            rs = s.executeQuery("select evento_encargado from eventos where NOT EXISTS (SELECT * FROM comisiones WHERE eventos.evento_encargado=comisiones.comision_encargado) and NOT EXISTS (SELECT * FROM materias WHERE eventos.evento_encargado=materias.materia_jefcatedra) group by evento_encargado;");
        }catch (Exception e)
        {
            System.out.println("Problema al obtener encargados cursos");
        }
        return rs;
    }
    public int cantidadAlumnos(int idm){
        ResultSet rs = null;
        Statement s = null;
        int cant = 0;
        try
        {
            s = conexion.createStatement();
            rs = s.executeQuery("SELECT comision_alumnos FROM  comisiones where materia_id="+idm);
        }catch (Exception e)
        {
            System.out.println("Problema al obtener la cantidad de comisones de una materia");
        }
         try{
        while (rs.next())
        {
            cant+=rs.getInt(1);
        }
        } catch(Exception e)
        {
            System.out.println("Problema al obtener la cantidad de comisiones");
        }
        return cant;
    }
    public ResultSet obtenerAsignacionesDia(Date fecha){
        ResultSet rs = null;
        Statement s = null;
        try
        {
            s = conexion.createStatement();
            rs = s.executeQuery("select aulas.aula_id,aula_nombre,aula_capacidad,asignacion_horainicio,asignacion_horafin,comisiones.comision_id,comision_tipo,materias.materia_id,materia_nombre,comision_numero from asignaciones inner join aulas on asignaciones.aula_id=aulas.aula_id inner join comisiones on asignaciones.comision_id=comisiones.comision_id inner join materias on comisiones.materia_id=materias.materia_id where asignacion_fecha='"+fecha+"' order by aulas.aula_id,asignacion_horainicio");
        }catch (Exception e)
        {
            System.out.println("Problema al obtener las asignaciones de un dia");
        }
        return rs;
    }
    public ResultSet obtenerAsignacionesDiaEventos(Date fecha){
        ResultSet rs = null;
        Statement s = null;
        try
        {
            s = conexion.createStatement();
            rs = s.executeQuery("select aulas.aula_id,aula_nombre,aula_capacidad,asignacion_horainicio,asignacion_horafin,materias.materia_id,materia_nombre from asignaciones inner join aulas on asignaciones.aula_id=aulas.aula_id inner join eventos on asignaciones.evento_id=eventos.evento_id inner join materias on eventos.materia_id=materias.materia_id where asignacion_fecha='"+fecha+"' order by aulas.aula_id,asignacion_horainicio");
        }catch (Exception e)
        {
            System.out.println("Problema al obtener las asignaciones eventos de un dia");
        }
        return rs;
    }
    public ResultSet obtenerAsignacionesDiaCursosOtros(Date fecha){
        ResultSet rs = null;
        Statement s = null;
        try
        {
            s = conexion.createStatement();
            rs = s.executeQuery("select aulas.aula_id,aula_nombre,aula_capacidad,asignacion_horainicio,asignacion_horafin,eventos.evento_id,evento_nombre from asignaciones inner join aulas on asignaciones.aula_id=aulas.aula_id inner join eventos on asignaciones.evento_id=eventos.evento_id where asignacion_fecha='"+fecha+"' and (tipoevento_id=3 or tipoevento_id=4) order by aulas.aula_id,asignacion_horainicio");
        }catch (Exception e)
        {
            System.out.println("Problema al obtener las asignaciones eventos de un dia");
        }
        return rs;
    }
    public void cargarTablaEventosParciales(int materia_id,int evento_participantes,Date fecha,int horainicio,int horafin,Vector<Integer> aulas,String materia) {
        String encargado = this.encargadoMateria(materia_id);
        String sql = "Insert into eventos (materia_id,evento_participantes,evento_fecha,evento_horafin,evento_horainicio,tipoevento_id,evento_encargado,evento_nombre) values ("+materia_id+","+evento_participantes+",'"+fecha+"',"+horafin+","+horainicio+",1,'"+encargado+"','Parcial de "+materia+"')";
        Statement s = null;
        try
        {
            s = conexion.createStatement();
            s.executeUpdate(sql);
        }
        catch (Exception e)
        {
            System.out.println("Problema al cargar datos sobre la tabla eventos "+materia_id+","+evento_participantes+","+fecha+","+horafin+","+horainicio);
        }
        ResultSet rs = null;
        int id=0;
        try
        {
            s = conexion.createStatement();
            rs = s.executeQuery("SELECT evento_id FROM eventos ORDER BY evento_id DESC LIMIT 1");
        }catch (Exception e)
        {
            System.out.println("Problema al obtener el id del evento");
        }
         try{
        while (rs.next())
        {
            id=rs.getInt(1);
        }
        } catch(Exception e)
        {
            System.out.println("Problema al obtener el id del evento");
        }
        for(int i=0;i<aulas.size();i++){
            String sql2 = "Insert into asignaciones (asignacion_fecha,asignacion_horainicio,asignacion_horafin,aula_id,evento_id) values ('"+fecha+"',"+horainicio+","+horafin+","+aulas.elementAt(i)+","+id+")";
            try
            {
                s = conexion.createStatement();
                s.executeUpdate(sql2);
            }
            catch (Exception e)
            {
                System.out.println("Problema al cargar la tabla asignaciones");
            }
        }
    }
    public boolean eventosAsignaciones(Date fecha,int idm){
        boolean b = false;
        ResultSet rs = null;
        Statement s = null;
        try
        {
            s = conexion.createStatement();
            rs = s.executeQuery("select * from eventos where evento_fecha='"+fecha+"' and materia_id="+idm);
        }catch (Exception e)
        {
            System.out.println("Problema al saber si existen eventos de una materia");
        }
         try{
        while (rs.next())
        {
          b=true;
        }
        } catch(Exception e)
        {
            System.out.println("Problema el valor boolean");
        }
        return b;
    }
    public ResultSet obtenerMateriasFacultad(int idf){
        ResultSet rs = null;
        Statement s = null;
        try
        {
            s = conexion.createStatement();
            rs = s.executeQuery("select materia_nombre,materia_jefcatedra,materia_id from materias where facultad_id="+idf);
        }catch (Exception e)
        {
            System.out.println("Problema al obtener las materias por facultad");
        }
        return rs;
    }
    public void dameAulasDisponiblesFinal(Vector<Aula> aulas,Date fecha,int hi,int hf,int facultad){      
        Vector<Integer> v = new Vector<Integer>();//creo e inicializo el vector con los id de las aulas en conflicto
        ResultSet rs = null;
        Statement s = null;
        try
        {
            s = conexion.createStatement();
            rs = s.executeQuery("Select aula_id,asignacion_horainicio,asignacion_horafin,comision_id,evento_id from asignaciones where asignacion_fecha='"+fecha+"' and asignacion_horainicio>="+hi);
        }catch (Exception e)
        {
            System.out.println("Problema al obtener las asignaciones que estan cargadas");
        }
         try{
        while (rs.next())//al terminar este while voy a obtener los ids de las aulas que estan en conflicto
        {
           String sidc=rs.getString(4);
           String side=rs.getString(5);
           int idc=0;
           int ide=0;
           if(sidc.equals("")){//consiferando que la consulta devuelve "" cuando no tienen nada
               ide=Integer.parseInt(side);
           }else{
               idc=Integer.parseInt(sidc);
           }
           if(this.perteneceFacultad(ide, idc, facultad)){//pregunto si pertenece a la facultad, devuelve true si no pertenece, realmente es asi?revisar
               int i = rs.getInt(2);//si pertenece debo controlar la hora de finalizacion del parcial
               int f = rs.getInt(3);
               if((i<=hi&&hi<f)){// esto me baso en una consulta anterior que la hice medio dormido igual que ahora asi que revisar si cumple
                   v.add(rs.getInt(1));//si cumple la agrego al vector de ids de aulas en conflicto
               }else{
                  if((f<=hf)){
                   v.add(rs.getInt(1));//si cumple la agrego al vector de ids de aulas en conflicto
               }
               }
               if((i<hf&&hf<f)){
                   v.add(rs.getInt(1));//si cumple la agrego al vector de ids de aulas en conflicto
               }
           }

        }
        } catch(Exception e)
        {
            System.out.println("Problema las aulas en conflicto");
        }//ya tenemos las aulas en conflicto
        //ahora vamos a cargar el vector de aulas con aquellas que no esten en conflicto
        //para ello primero vamos a consultar todas las aulas que tenemos cargadas
                    rs = null;
                    String idaula;
                    Aula aula;
                    String capacidad;
                    s = null;
                    try
                    {
                        s = conexion.createStatement();
                        rs = s.executeQuery("Select aula_id, aula_capacidad from aulas");
                    }catch (Exception e)
                    {
                        System.out.println("Problema al obtener las asignaciones que estan cargadas");
                    }
                    try{
                    while (rs.next())//dentro de el while vamos a realizar la depuracion de las aulas que estan en conflico
                    {//esto es solo voy a cargar al vector las aulas que no esten en el vector
                        idaula = rs.getString (1);
                        capacidad = rs.getString(2);
                        int i=0;
                        boolean b=true;//es true si no esta y se hace false si la encontramos en el vector
                        while((i>v.size())&&(b)){
                            if((v.get(i))==(Integer.parseInt(idaula))){
                                b=false;
                            }
                            i++;
                        }
                        if(b){// si b es true no esta en el vector de las aulas en conflicto asi que la cargamos
                        aula = new Aula(Integer.parseInt(idaula), Integer.parseInt(capacidad));
                        aulas.add(aula);}
                    }

                    } catch(Exception e)
                    {
                        System.out.println("Problema al obtener las aulas disponibles ");
                    }//HOJALA FUNCIONE ESTO YA ME VOLVIO LOCO
    }
    public boolean perteneceFacultad(int ide,int idc,int facultad){
        boolean b = true;
        ResultSet rs = null;
        Statement s = null;
        if(idc>0){
            try
            {
                s = conexion.createStatement();
                rs = s.executeQuery("select facultad_id from eventos inner join materias on materias.materia_id=eventos.materia_id where materias.facultad_id="+facultad+" and evento_id="+ide+" and tipoevento_id=1");
            }catch (Exception e)
            {
                System.out.println("Problema el valor boolean por el id de un evento");
            }
             try{
            while (rs.next())
            {
              b=false;
            }
            } catch(Exception e)
            {
                System.out.println("Problema el valor boolean");
            }
        }else{
            try
            {
                s = conexion.createStatement();
                rs = s.executeQuery("select facultad_id from comisiones inner join materias on materias.materia_id=comisiones.materia_id where materias.facultad_id="+facultad+" and comision_id="+idc);
            }catch (Exception e)
            {
                System.out.println("Problema al saber si pertenece a la facultad por el id de una comision");
            }
             try{
            while (rs.next())
            {
              b=false;
            }
            } catch(Exception e)
            {
                System.out.println("Problema el valor boolean");
            }
        }
        return b;
    }
    public String encargadoMateria(int idm){
        ResultSet rs = null;
        Statement s = null;
        String encargado = null;
        try
        {
            s = conexion.createStatement();
            rs = s.executeQuery("SELECT materia_jefcatedra FROM  materias where materia_id="+idm);
        }catch (Exception e)
        {
            System.out.println("Problema al obtener el encargado de una materia");
        }
         try{
        while (rs.next())
        {
            encargado=rs.getString(1);
        }
        } catch(Exception e)
        {
            System.out.println("Problema al obtener la cantidad de comisiones");
        }
        return encargado;
    }
    public void cargarTablaAsignacionsFinales(String nombre, Integer comision, String orientacion, Integer capacidad, Integer horainicio, Integer horafin, Integer aulaid,Date fecha)
    {
        String encargado = this.encargadoMateria(comision);
        String sql = "Insert into eventos (materia_id,evento_participantes,evento_fecha,evento_horafin,evento_horainicio,tipoevento_id,evento_encargado,evento_nombre) values ("+comision+","+capacidad+",'"+fecha+"',"+horafin+","+horainicio+",2,'"+encargado+"','Final de "+nombre+"')";
        Statement s = null;
        try
        {
            s = conexion.createStatement();
            s.executeUpdate(sql);
        }
        catch (Exception e)
        {
            System.out.println("Problema al cargar datos sobre la tabla eventos finales ");
        }
        ResultSet rs = null;
        int id=0;
        try
        {
            s = conexion.createStatement();
            rs = s.executeQuery("SELECT evento_id FROM eventos ORDER BY evento_id DESC LIMIT 1");
        }catch (Exception e)
        {
            System.out.println("Problema al obtener el id del evento");
        }
         try{
        while (rs.next())
        {
            id=rs.getInt(1);
        }
        } catch(Exception e)
        {
            System.out.println("Problema al obtener el id del evento");
        }
            String sql2 = "Insert into asignaciones (asignacion_fecha,asignacion_horainicio,asignacion_horafin,aula_id,evento_id) values ('"+fecha+"',"+horainicio+","+horafin+","+aulaid+","+id+")";
            try
            {
                s = conexion.createStatement();
                s.executeUpdate(sql2);
            }
            catch (Exception e)
            {
                System.out.println("Problema al cargar la tabla asignaciones");
            }
            System.out.println("hora inicio"+horainicio+" horafin"+horafin);
    }
    public boolean materiFinal(int idm, Date fecha){//devuelve true si la materia ya tiene un final asignado para una fecha
        boolean b = false;
        ResultSet rs = null;
        Statement s = null;
            try
            {
                s = conexion.createStatement();
                rs = s.executeQuery("Select evento_id from eventos where materia_id="+idm+" and evento_fecha='"+fecha+"' and tipoevento_id=2");
            }catch (Exception e)
            {
                System.out.println("Problema al saber si una materia tiene un final");
            }
             try{
            while (rs.next())
            {
              b=true;
            }
            } catch(Exception e)
            {
                System.out.println("Problema el valor boolean");
            }

        return b;
    }
    public ResultSet obtenerCursosOtros(){
        ResultSet rs = null;
        Statement s = null;
        try
        {
            s = conexion.createStatement();
            rs = s.executeQuery("select evento_nombre from eventos where tipoevento_id=3 or tipoevento_id=4 group by evento_nombre");
        }catch (Exception e)
        {
            System.out.println("Problema al obtener cursos");
        }
        return rs;
    }
    public void cargarTablaCursosOtros(String nombre,String encargado, Integer capacidad, Integer horainicio, Integer horafin, Integer aulaid,Date fecha,Integer eventotipo)
    {

        String sql = "Insert into eventos (evento_participantes,evento_fecha,evento_horafin,evento_horainicio,tipoevento_id,evento_encargado,evento_nombre) values ("+capacidad+",'"+fecha+"',"+horafin+","+horainicio+","+eventotipo+",'"+encargado+"','"+nombre+"')";
        Statement s = null;
        try
        {
            s = conexion.createStatement();
            s.executeUpdate(sql);
        }
        catch (Exception e)
        {
            System.out.println("Problema al cargar datos sobre la tabla eventos cursos otros ");
        }
        ResultSet rs = null;
        int id=0;
        try
        {
            s = conexion.createStatement();
            rs = s.executeQuery("SELECT evento_id FROM eventos ORDER BY evento_id DESC LIMIT 1");
        }catch (Exception e)
        {
            System.out.println("Problema al obtener el id del evento");
        }
         try{
        while (rs.next())
        {
            id=rs.getInt(1);
        }
        } catch(Exception e)
        {
            System.out.println("Problema al obtener el id del evento");
        }
            String sql2 = "Insert into asignaciones (asignacion_fecha,asignacion_horainicio,asignacion_horafin,aula_id,evento_id) values ('"+fecha+"',"+horainicio+","+horafin+","+aulaid+","+id+")";
            try
            {
                s = conexion.createStatement();
                s.executeUpdate(sql2);
            }
            catch (Exception e)
            {
                System.out.println("Problema al cargar la tabla asignaciones");
            }
            System.out.println("hora inicio"+horainicio+" horafin"+horafin);
    }
    public ResultSet obtenerMateriasFacultadAñoCuatriemestre(int idf,int año,int c){
        ResultSet rs = null;
        Statement s = null;
        if(c==0){
        try
        {
            s = conexion.createStatement();
            rs = s.executeQuery("select materia_nombre,materia_jefcatedra,materias.materia_id from asignaciones inner join comisiones on asignaciones.comision_id=comisiones.comision_id inner join materias on comisiones.materia_id=materias.materia_id where date_part('year', asignacion_fecha)="+año+" and date_part('month', asignacion_fecha)<7 and facultad_id="+idf+"group by materia_nombre,materia_jefcatedra,materias.materia_id");
        }catch (Exception e)
        {
            System.out.println("Problema al obtener las materias por facultad");
        }}else{
            try
        {
            s = conexion.createStatement();
            rs = s.executeQuery("select materia_nombre,materia_jefcatedra,materias.materia_id from asignaciones inner join comisiones on asignaciones.comision_id=comisiones.comision_id inner join materias on comisiones.materia_id=materias.materia_id where date_part('year', asignacion_fecha)="+año+" and date_part('month', asignacion_fecha)>6 and facultad_id="+idf+"group by materia_nombre,materia_jefcatedra,materias.materia_id");
        }catch (Exception e)
        {
            System.out.println("Problema al obtener las materias por facultad");
        }
        }
        return rs;
    }
    public ResultSet obtenerMateriasConsulta(int idm){
        ResultSet rs = null;
        Statement s = null;
        try
        {
            s = conexion.createStatement();
            rs = s.executeQuery("select asignaciones.dia_id, asignacion_horainicio,asignacion_horafin,aula_id,comision_numero from asignaciones inner join comisiones on asignaciones.comision_id=comisiones.comision_id inner join materias on materias.materia_id=comisiones.materia_id where materias.materia_id="+idm+" order by asignaciones.dia_id");
        }catch (Exception e)
        {
            System.out.println("Problema al obtener los dias de las materias");
        }
        return rs;
    }
    public ResultSet consultaDatosEvento(int id_event){
         ResultSet rs = null;
        Statement s = null;
            try
        {
            s = conexion.createStatement();
            System.out.println(id_event);
            rs = s.executeQuery("select eventos.evento_nombre,tiposeventos.tipoevent_nombre,eventos.evento_encargado,eventos.materia_id, eventos.evento_participantes, asignaciones.asignacion_horainicio,asignaciones.asignacion_horafin, asignaciones. asignacion_fecha from asignaciones inner join eventos on asignaciones.evento_id= eventos.evento_id  inner join tiposeventos on tiposeventos.tipoevento_id=eventos.tipoevento_id where eventos.evento_id="+id_event+";");
        }catch (Exception e)
        {
            System.out.println("Problema al obtener la consulta 180 ");
        }
        return rs;
     }
    public ResultSet consultaDatosComision(int id_comi){
         ResultSet rs = null;
        Statement s = null;

        try
        {
            s = conexion.createStatement();
            rs = s.executeQuery("select materias.materia_nombre, facultades.facultad_nombre,materias.materia_jefcatedra,comisiones.comision_numero,comisiones.comision_encargado,comisiones.comision_box,comisiones.comision_tipo,comisiones.comision_alumnos,comisionesxdia.cxd_horainicio,comisionesxdia.cxd_horafin,dias.dia_nombre from comisiones inner join materias on comisiones.materia_id=materias.materia_id inner join facultades on materias.facultad_id=facultades.facultad_id inner join comisionesxdia on comisiones.comision_id=comisionesxdia.comision_id inner join dias on comisionesxdia.dia_id=dias.dia_id where comisiones.comision_id="+id_comi+";");//, materias.materia_tipo
        }catch (Exception e)
        {
            System.out.println("Problema al obtener la consulta 169 ");
        }
        return rs;
     }
    public ResultSet consultaPorDiaMaterias(String fecha)
    {
         ResultSet rs = null;
        Statement s = null;

        try
        {
            s = conexion.createStatement();
            rs = s.executeQuery("Select materias.materia_nombre,comisiones.comision_numero, materias.facultad_id, asignaciones.asignacion_horainicio, asignaciones.asignacion_horafin, aulas.aula_nombre, asignaciones.comision_id from asignaciones inner join comisiones on asignaciones.comision_id=comisiones.comision_id inner join materias on comisiones.materia_id=materias.materia_id inner join aulas on asignaciones.aula_id=aulas.aula_id where asignacion_fecha ='"+fecha+"';");

        }catch (Exception e)
        {
            System.out.println("Problema al obtener la consulta por DIA de las materias");
        }

        return rs;
    }
     public ResultSet consultaPorDiaEventos(String fecha)
    {
         ResultSet rs = null;
        Statement s = null;

        try
        {
            s = conexion.createStatement();
            rs = s.executeQuery("Select eventos.evento_nombre, tipoevento_id, asignaciones.asignacion_horainicio, asignaciones.asignacion_horafin, aulas.aula_nombre, eventos.evento_id, materia_id "
                    + "from asignaciones inner join eventos on eventos.evento_id=asignaciones.evento_id inner join aulas on asignaciones.aula_id=aulas.aula_id where asignacion_fecha ='"+fecha+"';");

        }catch (Exception e)
        {
            System.out.println("Problema al obtener la consulta por DIA de los eventos ");
        }

        return rs;
    }
     public int obtenerFacultadId(int idm){
        ResultSet rs = null;
        Statement s = null;
        int id = -1;
        try
        {
            s = conexion.createStatement();
            rs = s.executeQuery("SELECT facultad_id FROM  materias where materia_id="+idm);
        }catch (Exception e)
        {
            System.out.println("Problema al obtener el id de la facultad");
        }
         try{
        while (rs.next())
        {
            id=rs.getInt(1);
        }
        } catch(Exception e)
        {
            System.out.println("Problema al leer el id de la facultad");
        }
        return id;
    }
     public String obtenerFacultadNombre(int idm){
        ResultSet rs = null;
        Statement s = null;
        String nom ="";
        try
        {
            s = conexion.createStatement();
            rs = s.executeQuery("SELECT facultad_nombre FROM  materias inner join facultades on materias.facultad_id=facultades.facultad_id where materia_id="+idm);
        }catch (Exception e)
        {
            System.out.println("Problema al obtener el id de la facultad");
        }
         try{
        while (rs.next())
        {
            nom=rs.getString(1);
        }
        } catch(Exception e)
        {
            System.out.println("Problema al leer el id de la facultad");
        }
        return nom;
    }
     public Integer verificarLogin(String login, String pass){
        int b = -1;
        ResultSet rs = null;
        Statement s = null;
            try
            {
                s = conexion.createStatement();
                rs = s.executeQuery("Select * from usuarios where usuario_login='"+login+"' and usuario_pass='"+pass+"' and usuario_estado='Activo'");
            }catch (Exception e)
            {
                System.out.println("Problema al verificar el login de un usuario");
            }
             try{
            while (rs.next())
            {
              b=rs.getInt("tipousuario_id");
            }
            } catch(Exception f)
            {
                System.out.println("Problema al verificar el login");
            }
                Date fechaActual = new Date();        
                DateFormat formatoHora = new SimpleDateFormat("HH:mm:ss");
                DateFormat formatoFecha = new SimpleDateFormat("yyyy/MM/dd");
                String fecha = formatoFecha.format(fechaActual)+" "+formatoHora.format(fechaActual);
                String estado = "ERROR";
                if(b!=-1){
                    estado="OK";
                }
                try {            
                    s.executeQuery("insert into auditorias (auditoria_fechora,usuario_login,auditoria_tipo,auditoria_estado) values ('"+fecha+"','"+login+"','login','"+estado+"')");
                } catch (SQLException ex) {
                    System.out.println("Problema al guarda accion login");
                }
        return b;
    }
    public ResultSet obtenerUsuarios()
    {
         ResultSet rs = null;
        Statement s = null;

        try
        {
            s = conexion.createStatement();
            rs = s.executeQuery("Select usuario_id,usuario_nombre,usuario_login,usuario_mail,usuario_cel,usuario_tel,tipousuario_nombre,usuario_estado from usuarios as u,tiposusuarios as tu where u.tipousuario_id=tu.tipousuario_id");

        }catch (Exception e)
        {
            System.out.println("Problema al obtener la consulta por DIA de los eventos ");
        }

        return rs;
    }
    public void cargarUsuario(String nombre,String mail,String tel,String cel,String login,String contraseña,String estado,Integer tipousuario)
    {
        String sql = "Insert into usuarios(usuario_nombre, usuario_login,usuario_pass,usuario_mail,usuario_cel,usuario_tel,tipousuario_id,usuario_estado) values ('"+nombre+"', '"+login+"', '"+contraseña+"', '"+mail+"', '"+cel+"', '"+tel+"',"+tipousuario+", '"+estado+"')";
        Statement s = null;
        try
        {
            s = conexion.createStatement();
            s.executeUpdate(sql);
        }
        catch (Exception e)
        {
            System.out.println("Problema al cargar un usuario");
        }
    }
    public Integer verificarLoginUsuario(String login){//devuelve -1 si el login del nuevo usuario existe
        int b = -1;
        ResultSet rs = null;
        Statement s = null;
            try
            {
                s = conexion.createStatement();
                rs = s.executeQuery("Select usuario_id from usuarios where usuario_login='"+login+"'");
            }catch (Exception e)
            {
                System.out.println("Problema al saber si un login es unico");
            }
             try{
            while (rs.next())
            {
              b=rs.getInt("usuario_id");
            }
            } catch(Exception e)
            {
                System.out.println("Problema al verificar la existencia de un login");
            }

        return b;
    }
    public void actualizarUsuario(String login,String nombre,String mail,String cel,String tel,String contraseña,String estado,Integer tipousuario)
    {//a este procedimiento debo revisarlo
        ResultSet rs = null;
        Statement s ;
        try
        {
            s = conexion.createStatement();
            rs = s.executeQuery("update usuarios set usuario_nombre='"+nombre+"',usuario_mail='"+mail+"',usuario_cel='"+cel+"',usuario_tel='"+tel+"',usuario_estado='"+estado+"',tipousuario_id="+tipousuario+",usuario_pass='"+contraseña+"' where usuario_login='"+login+"'");
        }catch (SQLException e)
        {
            System.out.println("Problema al acutualizar los datos de un usuario");
        }
    }
    public void actualizarUsuario(String login,String nombre,String mail,String cel,String tel,String estado,Integer tipousuario)
    {//a este procedimiento debo revisarlo
        ResultSet rs = null;
        Statement s ;
        try
        {
            s = conexion.createStatement();
            rs = s.executeQuery("update usuarios set usuario_nombre='"+nombre+"',usuario_mail='"+mail+"',usuario_cel='"+cel+"',usuario_tel='"+tel+"',usuario_estado='"+estado+"',tipousuario_id="+tipousuario+" where usuario_login='"+login+"'");
        }catch (SQLException e)
        {
            System.out.println("Problema al acutualizar los datos de un usuario");
        }
    }
    public void eliminarUsuario(String login){
        Statement s = null;
        try
        {
            s = conexion.createStatement();
            s.executeUpdate("delete from usuarios where usuario_login = '"+login+"'");
        }
        catch (Exception e)
        {
            System.out.println("Problema al eliminar un usuario");
        }
    }
    //hasta aqui terminan los procedimientos testeados



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
        Integer id = obtenerIdAula(nombre.replace(" ", ""));

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