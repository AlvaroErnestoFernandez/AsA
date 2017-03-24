/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Interfaz;

import Estructura.Postgres;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.util.Vector;
import javax.swing.*;
import com.toedter.calendar.JDateChooser;
import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Date;


public class SubAsignacionManual extends JPanel implements ActionListener{

    private JLabel ncomision,tipo,alumnos,encargado,box;
    private int idcomision;
    private Postgres basedato;
    Vector<JLabel> vlabel;
    Vector<Integer> vdias;
    Vector<JLabel> vhi;
    Vector<JLabel> vhf;
    Vector<JComboBox> vcombobox;
    Vector<String> vid;
    Vector<JButton> vbutton;
    Vector<JDateChooser> vdesde;
    Vector<JDateChooser> vhasta;

    public SubAsignacionManual(int id,int ncomision,String tipo,int alumnos,String encargado,String box, int y){
        super();
        this.idcomision=id;
        this.setLayout(null);
        int alto = y+180;
        this.setBounds(10,10,700,alto);
        //this.setBorder(BorderFactory.createTitledBorder("Comisiones"));
        this.setOpaque(false);
        this.ncomision = new JLabel("Comision N°: "+ncomision+"");
        this.tipo = new JLabel("Tipo: "+tipo+"");
        this.alumnos = new JLabel("Alumnos: "+alumnos+"");
        this.encargado = new JLabel("Encargado: "+encargado+"");
        this.box = new JLabel("Box encargado: "+box+"");

        vlabel=new Vector<JLabel>();
        vdias=new Vector<Integer>();
        vhi=new Vector<JLabel>();
        vhf=new Vector<JLabel>();
        vcombobox=new Vector<JComboBox>();
        vid=new Vector<String>();
        vbutton=new Vector<JButton>();
        vdesde= new Vector<JDateChooser>();
        vhasta= new Vector<JDateChooser>();

        int x = 1;
        int w = 200;
        int h = 30;
        int i = 15;
        this.ncomision.setBounds(x, y, w, h);
        this.tipo.setBounds(x, y+i, w, h);
        this.alumnos.setBounds(x, y+i*2, w, h);
        this.encargado.setBounds(x, y+i*3, w, h);
        this.box.setBounds(x, y+i*4, w, h);
        this.agregarDias(x,y+i*5,w,h);
        for (int j=0;j<vlabel.size();j++){
            this.setBounds(10,10,600,alto);
            this.add(vlabel.elementAt(j));
            this.add(vcombobox.elementAt(j));
            this.add(vbutton.elementAt(j));
            this.add(vdesde.elementAt(j));
            this.add(vhasta.elementAt(j));
            alto=alto+150;
        }

        this.add(this.ncomision);
        this.add(this.tipo);
        this.add(this.alumnos);
        this.add(this.encargado);
        this.add(this.box);

    }
    public void agregarDias(int x,int y,int w,int h){
        ResultSet rs = null;
        y=y+15;
        basedato = new Postgres();
        basedato.estableceConexion();
        rs = basedato.obtenerDiasComision2(idcomision);
                    
        try{
        while (rs.next())
        {
            String dia;
            String hi;
            String hf;
            String idcxd;
            dia = rs.getString(1);
            hi = rs.getString(2);
            hf = rs.getString(3);
            idcxd=rs.getString(4);
            vid.addElement(idcxd);
            JLabel lb = new JLabel(dia+"de   "+hi+"   a   "+hf);
            int dialb = rs.getInt(5);
            vdias.add(dialb);
            JLabel hilb = new JLabel(hi);
            vhi.add(hilb);
            JLabel hflb = new JLabel(hf);
            vhf.add(hflb);

            lb.setBounds(x+10, y, w, h);
            vlabel.addElement(lb);
                ResultSet rs2 = null;
                Postgres basedato2 = new Postgres();
                basedato2.estableceConexion();
                rs2 = basedato2.obtenerAulas();
                JComboBox cb = new JComboBox();
                DefaultListCellRenderer dlcr = new DefaultListCellRenderer();
                 dlcr.setHorizontalAlignment(DefaultListCellRenderer.RIGHT);
                 cb.setRenderer(dlcr);
                cb.addItem("");
                try{
                while (rs2.next())
                {
                    
                    cb.addItem(rs2.getString(1));
                }

                } catch(Exception e)
                {
                    System.out.println("Problema al cargar las aulas");
                }
                int aulaid = basedato2.obtenerAulaAsignada(idcxd);
                String saula = basedato2.obtenerAulaNombre(aulaid);
                cb.setBounds(x+160, y, w-115, h);
                cb.setSelectedItem(saula);
                basedato2.cierraConexion(); 
                vcombobox.add(cb);
            JDateChooser chd = new JDateChooser();
            chd.setBounds(x+250, y, 120, h);
            chd.setBackground(new Color(155,193,232));
            vdesde.add(chd);
            JDateChooser chh = new JDateChooser();
            
            chh.getDateEditor().addPropertyChangeListener(new PropertyChangeListener(){

                    @Override
                    public void propertyChange(PropertyChangeEvent x) {
                        if(x.getPropertyName().compareTo("day")>0){
                            //JOptionPane.showMessageDialog(SubAsignacionManual.this,"la fecha es "+x.getPropertyName().compareTo("day"));
                        }
                    }
            });
            /*
             cdesde.getDayChooser().addPropertyChangeListener(
                new java.beans.PropertyChangeListener() {

                    @Override
                    public void propertyChange(java.beans.PropertyChangeEvent evt) {
                        if (evt.getPropertyName().compareTo("day") == 0) {
                            SimpleDateFormat formatoDeFecha = new SimpleDateFormat("dd/MM/yyyy");
                            tdesde.setText(formatoDeFecha.format(cdesde.getDate()));
                        }else{
                            SimpleDateFormat formatoDeFecha = new SimpleDateFormat("dd/MM/yyyy");
                            tdesde.setText(formatoDeFecha.format(cdesde.getDate()));
                        }
                    }
                });*/
            chh.setBounds(x+370, y, 120, h);
            chh.setBackground(new Color(155,193,232));

            
            vhasta.add(chh);

            JButton b=new JButton("Asignar");
            b.setToolTipText("ASIGNAR AULA");
            b.addActionListener(this);
            b.setBounds(x+500, y, w-130, h);
            vbutton.add(b);
            
            y=y+30;
        }

        } catch(Exception e)
        {
            System.out.println("Problema al cargar mostrar los dias");
        }
        basedato.cierraConexion();
    }

    public void actionPerformed(ActionEvent g) {
        Object f = g.getSource();
        for(int i=0;i<vbutton.size();i++){
        if(f.equals(vbutton.elementAt(i))){
                int dia = vdias.elementAt(i);
                int hi = Integer.parseInt(vhi.elementAt(i).getText());
                int hf = Integer.parseInt(vhf.elementAt(i).getText());
                String saula = (String) vcombobox.elementAt(i).getSelectedItem();
                basedato = new Postgres();
                basedato.estableceConexion();
                int aula = basedato.obtenerAulaId(saula);
                basedato.cierraConexion();
                String id = vid.elementAt(i);
                Date desde = vdesde.elementAt(i).getDate();
                Date hasta = vhasta.elementAt(i).getDate();
                /*basedato = new Postgres();
                basedato.estableceConexion();
                basedato.cargarTablaAsignacions2(id, idcomision, id, aula, hi, hf, dia, aula, desde, hasta);//EL SEGUNDO ID Y EL PRIMER AULA SON DATOS QUE LA CLASE POSTGRES NO UTILIZA PARA CARGAR LAS ASIGNACIONES, ESTO ES ALGO PARA LA DEPURACION
                basedato.cierraConexion();*/
                //System.out.println("Comsion id: "+idcomision+" ComisionxDia id: "+id+" aula id: "+aula+" dia id: "+dia+" hora inicio: "+hi+" horafin:"+hf+" Desde: "+desde);
                if((aula!=0)&&(desde!=null)&&(hasta!=null)){
                    basedato = new Postgres();
                    basedato.estableceConexion();
                    Vector<Integer> v = new Vector<Integer>();
                    basedato.obtenerAsignaciones(v, aula, dia, desde, hasta, hi, hf);
                    /*String s = null;
                    for(int j=0;j<v.size();j++){
                         s = s+v.elementAt(j).toString();
                         s = s+", ";
                    }*/
                    if(v.size()>0){
                            int h;
                             h =JOptionPane.showConfirmDialog(null, "Ud.  tiene asignacion/es realizada/s para este dia, esta aula, entre estas fechas, desea eliminarla/s?", "Confirmar Asignacion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                            if (h==0){
                                 for(int j=0;j<v.size();j++){
                                 int a = v.elementAt(j);
                                 basedato.eliminarAsignacion(a);
                                }
                                basedato.cargarTablaAsignacions2(id, idcomision, id, aula, hi, hf, dia, aula, desde, hasta);//EL SEGUNDO ID Y EL PRIMER AULA SON DATOS QUE LA CLASE POSTGRES NO UTILIZA PARA CARGAR LAS ASIGNACIONES, ESTO ES ALGO PARA LA DEPURACION
                                JOptionPane.showMessageDialog(null,"Asignacion realizada");
                            }
                        }else{
                            basedato.cargarTablaAsignacions2(id, idcomision, id, aula, hi, hf, dia, aula, desde, hasta);//EL SEGUNDO ID Y EL PRIMER AULA SON DATOS QUE LA CLASE POSTGRES NO UTILIZA PARA CARGAR LAS ASIGNACIONES, ESTO ES ALGO PARA LA DEPURACION
                            JOptionPane.showMessageDialog(null,"Asignacion realizada");
                        }
                    basedato.cierraConexion();
                }else{
                    JOptionPane.showMessageDialog(this,
                    "No se pudo realizar la asignacion, asegurese de haber seleccionado un aula y de definir las fechas de asignacion",
                        "FALLO ASIGNACION",JOptionPane.WARNING_MESSAGE);
                }
            }
        }
    }
}