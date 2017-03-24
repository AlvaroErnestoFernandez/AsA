/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Interfaz;

import java.awt.Color;
import java.awt.Component;
import java.util.Vector;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class MiRender extends DefaultTableCellRenderer
{
    private Vector<Integer> rojof;
    private Vector<Integer> rojoc;
    private Vector<Integer> azulf;
    private Vector<Integer> azulc;

    public MiRender(Vector<Integer> af,Vector<Integer> ac,Vector<Integer> rf,Vector<Integer> rc){
        super();
        
        rojof = new Vector<Integer>();
        rojoc = new Vector<Integer>();
        azulf = new Vector<Integer>();
        azulc = new Vector<Integer>();

        for(int i=0;i<af.size();i++){
            this.azulf.add(af.get(i));
        }
        for(int i=0;i<ac.size();i++){
            this.azulc.add(ac.get(i));
        }
        for(int i=0;i<rf.size();i++){
            this.rojof.add(rf.get(i));
        }
        for(int i=0;i<rc.size();i++){
            this.rojoc.add(rc.get(i));
        }
    }
   public int obtener(int f,int c){//devuelve 1 si se debe pintar de rojo 2 de azul y 0 si no esta esta celda para pintar
       int p=0;
       boolean b=true;
       int i=0;
       while((b)&&(i<rojof.size())){
           if((rojof.get(i)==f)&&(rojoc.get(i)==c)){
               b=false;
               p=1;
           }
           i++;
       }
       i=0;
       while((b)&&(i<azulf.size())){
           if((azulf.get(i)==f)&&(azulc.get(i)==c)){
               b=false;
               p=2;
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
             case 1:
                 this.setOpaque(true);
                 this.setBackground(Color.RED);
                 this.setForeground(Color.WHITE);//esto es para el color de las letras
                 break;
             case 2:
                 this.setOpaque(true);
                 this.setBackground(Color.BLUE);
                 this.setForeground(Color.WHITE);//esto es para el color de las letras
                 break;
             default:
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