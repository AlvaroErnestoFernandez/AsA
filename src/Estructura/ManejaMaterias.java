package Estructura;

import java.util.HashMap;
import java.util.Scanner;
import java.util.Vector;

public class ManejaMaterias {
	
	Vector <Materia> vectormujeres = new Vector <Materia>();
	HashMap <Integer,Integer> asignaciones = new HashMap <Integer,Integer>();
	int cantmujeres;
        Vector <Aula> Aulas = new Vector <Aula>();
	
	public void inciar(Vector<String> nombres, Vector<Integer> capacidades, Vector<Integer> comisiones, Vector<Integer> horasinicio, Vector<Integer> horasfin, Vector<String> orientaciones, Vector<Aula> aulas)
	{
		Materia m ; 
                cantmujeres = nombres.size();
                cargaraulas(aulas);
		for(int i=0; i<cantmujeres;i++)
		{
			m = new Materia();
			m.iniciar(i, nombres.elementAt(i), capacidades.elementAt(i), comisiones.elementAt(i), horasinicio.elementAt(i), horasfin.elementAt(i), orientaciones.elementAt(i), Aulas);
			vectormujeres.add(m);
		}		
	}
	
	public void cargarasignaciones()
	{
		int i = 0;
		int m = 0;
		while(i < cantmujeres)
		{
			if(vectormujeres.elementAt(i).necesitasesposo()){
			    if(vectormujeres.elementAt(i).puedeasignar()){
			    	vectormujeres.elementAt(i).asignaresposo();
				    if(asignaciones.containsKey(vectormujeres.elementAt(i).obteneresposo())){
					    m = asignaciones.get(vectormujeres.elementAt(i).obteneresposo());
					    if (vectormujeres.elementAt(m).puedeasignar())
					    {
					         asignaciones.remove(vectormujeres.elementAt(m).obteneresposo());
					         asignaciones.put(vectormujeres.elementAt(i).obteneresposo(), vectormujeres.elementAt(i).obtenerid());
					         i = m;
					        vectormujeres.elementAt(m).divorciar();
				         }
					    else {
					    	vectormujeres.elementAt(i).divorciar();
					    }
					    }
				    
				    	 
				    else{
					    asignaciones.put(vectormujeres.elementAt(i).obteneresposo(), vectormujeres.elementAt(i).obtenerid());
					    i++;
				    }
			    }
			    else{
			    	i++;
			    }
			   
			}
			else{
				i++;
			}
		}
               
	}
	
	public void mostrarasignaciones(){
		int m;
		int e;
		for(int i=0;i<cantmujeres;i++){
			e = vectormujeres.elementAt(i).obteneresposo();
			m = vectormujeres.elementAt(i).obtenerid();
			System.out.println(m+";"+e);
		}
	}
        public void cargaraulas(Vector <Aula> aulas){
            Aula aula;
            for (int i=0; i < aulas.size(); i++){
                aula = new Aula(aulas.elementAt(i).obtenerid(), aulas.elementAt(i).obtenercapacidad());
                Aulas.add(aula);
            }
        }
        
        public Vector<Materia> obtenermaterias(){
            return vectormujeres;
        }
        
        public int obtenerunaaula(int id){
            return vectormujeres.elementAt(id).obteneresposo();
        }
        
        public String obtenerunnombre(int id){
            return vectormujeres.elementAt(id).obtenernombre();
        }
        
        public String obtenerorientacion(int id){
            return vectormujeres.elementAt(id).obtenerorientacion();
        } 
        public int obtenerhorainicio(int id){
            return vectormujeres.elementAt(id).obtenerhorainicio();
        }        
        public int obtenerhorafin(int id){
            return vectormujeres.elementAt(id).obtenerhorafin();
        }        
        public int obtenercapacidad(int id){
            return vectormujeres.elementAt(id).obtenercapacidad();
        }        
        public int obtenercomision(int id){
            return vectormujeres.elementAt(id).obtenercomision();
        }
        
        public int obtenertamaño(){
            return vectormujeres.size();
        }
}
