package Estructura;

import java.util.Scanner;
import java.util.Vector;

public class Materia {
   int id;
   Vector <Integer> preferencias = new Vector <Integer> ();
   int esposo;
   int posicionesposo;
   int cantprefe;
   boolean soltera;
   int posicionmujer;
   int comision;
   int horainicio;
   int horafin;
   String orientacion;
   
   
   String nombre;
   int capacidad;
  
   
   public void iniciar(int id, String nombre, int capacidad, int comision, int horainicio, int horafin, String orientacion, Vector<Aula> aulas) {
	   Integer j;
           this.nombre = nombre;
           this.capacidad = capacidad;
           this.comision = comision;
           this.horainicio = horainicio;
           this.horafin = horafin;
           this.orientacion = orientacion;
           
           int c;
           for (int i=0; i< aulas.size(); i++){
               c = (aulas.elementAt(i).obtenercapacidad() - capacidad);
               if(abs(c) <= 10){
                   preferencias.add(aulas.elementAt(i).obtenerid());
               }
                   
           }
           
	   esposo = -1;
	   this.id = id;
	   posicionesposo=-1;
	   cantprefe = preferencias.size();
	   soltera = true;
	   posicionmujer = 500;
           
	   
   }
   public int obtenercantprefe(){
	   return cantprefe;
   }
   public void cambiarposicion(int p)
   {
	   posicionmujer=p;   
   }
   public int obtenerpos()
   {
	   return posicionmujer;
   }
   public int obteneresposo()
   {
	   return esposo;
   }
   
   public int posesposo(){
	   return posicionesposo;
   }
   
   public int obtenerid(){
	   return id;
   }
   
   public void asignaresposo(){
	       esposo = preferencias.elementAt(posicionesposo+1);
		   posicionesposo = posicionesposo+1;
		   soltera = false;
	      }
	   
  public boolean puedeasignar()
  {
	  boolean resp= false;
	  if (posicionesposo+1<preferencias.size())
	  {
		   resp=true;
	  }
	  
	  return resp;
  }
   public boolean necesitasesposo(){
	   return soltera;
   }
   
   public void divorciar(){
	   soltera = true;
	   esposo = -1;
   }
   
   public int abs(int c){
       if (c < 0){
           c = c * (-1);
       }
       return c;
   }
   
   public String obtenernombre(){
       return nombre;
   }
   
   public int obtenercomision(){
       return comision;
   }
   
   public int obtenerhorainicio(){
       return horainicio;
   }
   
   public int obtenerhorafin(){
       return horafin;
   }
   
   public String obtenerorientacion(){
       return orientacion;
   }
   public int obtenercapacidad(){
       return capacidad;
   }
   
}