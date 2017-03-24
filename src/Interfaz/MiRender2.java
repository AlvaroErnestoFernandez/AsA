/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Interfaz;

/**
 *
 * @author Patito
 */
import java.awt.Color;
import java.awt.Component;
import java.util.Vector;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class MiRender2 extends DefaultTableCellRenderer
{
    private Vector<Integer> econMoradoFila;
    private Vector<Integer> econMoradoCol;
    private Vector<Integer> saludNaranjFila;
    private Vector<Integer> saludNaranjCol;
    private Vector<Integer> naturCelesFila;
    private Vector<Integer> naturCelesCol;
    private Vector<Integer> exacRojoFila;
    private Vector<Integer> exacRojoCol;
    private Vector<Integer> ingVerdeFila;
    private Vector<Integer> ingVerdeCol;
    private Vector<Integer> humaAzulFila;
    private Vector<Integer> humaAzulCol;
    private Vector<Integer> otrosAmarFila;
    private Vector<Integer> otrosAmarCol;

    public MiRender2(Vector<Integer> ecof,Vector<Integer> ecoc,Vector<Integer> salf,Vector<Integer> salc,
            Vector<Integer> natf,Vector<Integer> natc,Vector<Integer> exf,Vector<Integer> exc,
            Vector<Integer> ingf,Vector<Integer> ingc,Vector<Integer> humf,Vector<Integer> humc,
            Vector<Integer> of,Vector<Integer> oc){
        super();

   econMoradoFila= new Vector<Integer>();
   econMoradoCol= new Vector<Integer>();
   saludNaranjFila= new Vector<Integer>();
   saludNaranjCol= new Vector<Integer>();
   naturCelesFila= new Vector<Integer>();
   naturCelesCol= new Vector<Integer>();
   exacRojoFila= new Vector<Integer>();
   exacRojoCol= new Vector<Integer>();
   ingVerdeFila= new Vector<Integer>();
   ingVerdeCol= new Vector<Integer>();
   humaAzulFila= new Vector<Integer>();
   humaAzulCol= new Vector<Integer>();
   otrosAmarFila= new Vector<Integer>();
   otrosAmarCol= new Vector<Integer>();

        for(int i=0;i<ecof.size();i++){
            this.econMoradoFila.add(ecof.get(i));
        }
        for(int i=0;i<ecoc.size();i++){
            this.econMoradoCol.add(ecoc.get(i));
        }
        for(int i=0;i<salf.size();i++){
            this.saludNaranjFila.add(salf.get(i));
        }
        for(int i=0;i<salc.size();i++){
            this.saludNaranjCol.add(salc.get(i));
        }
        for(int i=0;i<natf.size();i++){
            this.naturCelesFila.add(natf.get(i));
        }
        for(int i=0;i<natc.size();i++){
            this.naturCelesCol.add(natc.get(i));
        }
        for(int i=0;i<exf.size();i++){
            this.exacRojoFila.add(exf.get(i));
        }
        for(int i=0;i<exc.size();i++){
            this.exacRojoCol.add(exc.get(i));
        }
        for(int i=0;i<ingf.size();i++){
            this.ingVerdeFila.add(ingf.get(i));
        }
        for(int i=0;i<ingc.size();i++){
            this.ingVerdeCol.add(ingc.get(i));
        }
        for(int i=0;i<humf.size();i++){
            this.humaAzulFila.add(humf.get(i));
        }
        for(int i=0;i<humc.size();i++){
            this.humaAzulCol.add(humc.get(i));
        }
        for(int i=0;i<of.size();i++){
            this.otrosAmarFila.add(of.get(i));
        }
        for(int i=0;i<oc.size();i++){
            this.otrosAmarCol.add(oc.get(i));
         }
    }

    MiRender2() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
   public int obtener(int f,int c){//devuelve 1 si se debe pintar de rojo 2 de azul y 0 si no esta esta celda para pintar
       int p=-1;
       boolean b=true;
       int i=0;
       while((b)&&(i<econMoradoFila.size())){
           if((econMoradoFila.get(i)==f)&&(econMoradoCol.get(i)==c)){
               b=false;
               p=1;
           }
           i++;
       }
       i=0;
          while((b)&&(i<saludNaranjFila.size())){
           if((saludNaranjFila.get(i)==f)&&(saludNaranjCol.get(i)==c)){
               b=false;
               p=4;
           }
           i++;
       }
       i=0;
          while((b)&&(i<naturCelesFila.size())){
           if((naturCelesFila.get(i)==f)&&(naturCelesCol.get(i)==c)){
               b=false;
               p=3;
           }
           i++;
       }
       i=0;
          while((b)&&(i<exacRojoFila.size())){
           if((exacRojoFila.get(i)==f)&&(exacRojoCol.get(i)==c)){
               b=false;
               p=0;
           }
           i++;
       }
       i=0;
          while((b)&&(i<ingVerdeFila.size())){
           if((ingVerdeFila.get(i)==f)&&(ingVerdeCol.get(i)==c)){
               b=false;
               p=2;
           }
           i++;
       }
       i=0;

       while((b)&&(i<humaAzulFila.size())){
           if((humaAzulFila.get(i)==f)&&(humaAzulCol.get(i)==c)){
               b=false;
               p=5;
           }
           i++;
       }
       while((b)&&(i<otrosAmarFila.size())){
           if((otrosAmarFila.get(i)==f)&&(otrosAmarCol.get(i)==c)){
               b=false;
               p=6;
           }
           i++;
       }
       return p;
   }
   public Component getTableCellRendererComponent(JTable table,
      Object value,
      boolean isSelected,
      boolean hasFocus,
      int row,
      int column)
   {
      super.getTableCellRendererComponent (table, value, isSelected, hasFocus, row, column);
      if (!value.toString().equals(""))
      {
         switch(this.obtener(row, column)){
             case 0:
                 this.setOpaque(true);
                 this.setBackground(Color.RED);
                 this.setForeground(Color.WHITE);//esto es para el color de las letras
                 break;
             case 1:
                 this.setOpaque(true);
                 this.setBackground(Color.MAGENTA);
                 this.setForeground(Color.WHITE);//esto es para el color de las letras
                 break;
             case 2:
                 this.setOpaque(true);
                 this.setBackground(Color.GREEN);
                 this.setForeground(Color.WHITE);//esto es para el color de las letras
                 break;
             case 3:
                 this.setOpaque(true);
                 this.setBackground(Color.CYAN);
                 this.setForeground(Color.WHITE);//esto es para el color de las letras
                 break;
             case 4:
                 this.setOpaque(true);
                 this.setBackground(Color.ORANGE);
                 this.setForeground(Color.WHITE);//esto es para el color de las letras
                 break;
             case 5:
                 this.setOpaque(true);
                 this.setBackground(Color.BLUE);
                 this.setForeground(Color.WHITE);//esto es para el color de las letras
                 break;
             case 6:
                 this.setOpaque(true);
                 this.setBackground(Color.YELLOW);
                 this.setForeground(Color.WHITE);//esto es para el color de las letras
                 break;
             }
      } else {
         this.setOpaque(true);
         this.setBackground(Color.white);
         this.setForeground(Color.BLACK);
      }

      return this;
   }
}
