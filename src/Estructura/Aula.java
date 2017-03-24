package Estructura;

public class Aula {
    int id;
    int capacidad;
 
    
    public Aula (int id, int capacidad){
        this.id = id;
        this.capacidad = capacidad;
    }
    
    public int obtenerid(){
        return id;
    }
    
    public int obtenercapacidad(){
        return capacidad;
    }
    
}
