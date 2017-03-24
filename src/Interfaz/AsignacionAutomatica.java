/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Interfaz;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;

import Estructura.*;
import java.sql.ResultSet;

import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import javax.swing.table.*;
//
import com.toedter.calendar.JCalendar;

public class AsignacionAutomatica extends JInternalFrame implements ActionListener {
    
    private JLabel JLdiai,JLdiaf,JLmesi,JLmesf,JLañoi,JLañof,desde,hasta;
    private JComboBox JCdiai,JCdiaf,JCmesi,JCmesf,JCañoi,JCañof;
    private JPanel JPasignacionautomatica;
    private JButton ASIGNAR, VerAsignaciones;
    //elementos para ocultar barra de titulo
    private JComponent Barra = ((javax.swing.plaf.basic.BasicInternalFrameUI) getUI()).getNorthPane();
    private Dimension dimBarra = null;
    //tabla de materias no asignadas
    private JTable tablanoasignadas;
    private JScrollPane scrlptnoasignadas;//scroll tabla
    private DefaultTableModel tnoasignadas;
    private final String [] colstnoasignadas = {"Materia","Facultad","Jefe de Catedra","Comision","Dia","Cantidad Alumnos","Hora Inicio","Hora Fin","Responsable Comision","Box del Responsable"};
    //imagen de fondo
    private FondoVentana fondo;
    //
    private JCalendar cdesde;
    private JCalendar chasta;
    private JTextField tdesde;
    private JTextField thasta;
    
    public AsignacionAutomatica(){
        super("Asignacion Automatica", false, // resizable
                    false, // closable
                    false, // maximizable
                    false);// iconifiable
        this.getContentPane().setLayout(null);
        this.getContentPane().add(this.pnlasignacionautomatica());
        this.getContentPane().add(this.pnlTablaMateriasNoAsignadas());
        this.getContentPane().add(this.pnlCalendarioHasta());
        this.getContentPane().add(this.pnlCalendarioDesde());
        //this.getContentPane().setBackground(new Color(155,193,232));
        //this.getContentPane().add(this.pnlImagen());
        this.setBorder(null);
        this.ocultarBarraTitulo();
        this.agregarOyenteDesde();
        this.agregarOyenteHasta();
        this.setVisible(false);
        //this.cargarTablaMateriasNoAsigndas();
        /*
        Date dia=ParseFecha("06/10/2014");
        System.out.println(this.obtenerDiaSemana(dia));
        dia = this.sumarRestarDiasFecha(dia, 1);
        System.out.println(dia);*/

    }
    public JPanel pnlCalendarioHasta(){
        chasta=new JCalendar();
        chasta.setVisible(true);
        chasta.setBackground(Color.white);

        JPanel c =new JPanel();
        c.setBounds(900, 50, 400, 300);
        //c.setBorder(BorderFactory.createTitledBorder("HASTA"));
        c.setOpaque(false);
        c.add(chasta);

        return c;
    }
    public JPanel pnlCalendarioDesde(){
        cdesde = new JCalendar();
        cdesde.setVisible(true);
        JPanel c =new JPanel();
        c.setBounds(50, 50, 400, 300);
        //c.setBorder(BorderFactory.createTitledBorder("HASTA"));
        c.setOpaque(false);
        c.add(cdesde);

        return c;
    }
    public JPanel pnlasignacionautomatica(){
       JLdiai = new JLabel("Dia");
       JLdiaf = new JLabel("Dia");
       JLmesi = new JLabel("Mes");
       JLmesf = new JLabel("Mes");
       JLañoi = new JLabel("Año");
       JLañof = new JLabel("Año");
       desde = new JLabel("Fecha desde");
       hasta = new JLabel("Fecha hasta");
       tdesde=new JTextField();
       thasta=new JTextField();

       JCdiai = new JComboBox(new String[] {"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31"});
       JCdiaf = new JComboBox(new String[] {"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31"});
       JCmesi = new JComboBox(new String[] {"1","2","3","4","5","6","7","8","9","10","11","12"});
       JCmesf = new JComboBox(new String[] {"1","2","3","4","5","6","7","8","9","10","11","12"});
       JCañoi = new JComboBox(new String[] {"2014","2015","2016","2017","2018","2019","2020","2020","2021","2022","2023","2024"});
       JCañof = new JComboBox(new String[] {"2014","2015","2016","2017","2018","2019","2020","2020","2021","2022","2023","2024"});
       //calendario
       
       chasta = new JCalendar();
       //
       ASIGNAR = new JButton("ASIGNAR AULAS");
       VerAsignaciones = new JButton("Ver Asignaciones");
       ASIGNAR.setToolTipText("ASIGNAR AULAS AUTOMATICAMENTE");
       ASIGNAR.addActionListener(this);
       VerAsignaciones.setToolTipText("VER ASIGNACIONES DEL DIA");
       VerAsignaciones.addActionListener(this);
       int x = 150;//eje x
       int y = 30;//eje y
       int w = 40;//ancho
       int h = 30;//alto
       int i = 40;//incremento
       tdesde.setBounds(x, y+i+30, 100, 30);
       tdesde.setEditable(false);
       thasta.setBounds(x+260, y+i+30, 100, 30);
       thasta.setEditable(false);

       JLdiai.setBounds(x+5, y+20, w, h);
       JLmesi.setBounds(x+i+5, y+20, w, h);
       JLañoi.setBounds(x+(i*2)+5, y+20, w, h);
       JLdiaf.setBounds(x+(i*6)+5, y+20, w, h);
       JLmesf.setBounds(x+(i*7)+5, y+20, w, h);
       JLañof.setBounds(x+(i*8)+5, y+20, w, h);

       JCdiai.setBounds(x, y+i, w, h);
       JCmesi.setBounds(x+i, y+i, w, h);
       JCañoi.setBounds(x+i*2, y+i, w+23, h);
       JCdiaf.setBounds(x+i*6, y+i, w, h);
       JCmesf.setBounds(x+i*7, y+i, w, h);
       JCañof.setBounds(x+i*8, y+i, w+23, h);

       desde.setBounds(x+10, y+i, w+100, h);
       desde.setFont(new java.awt.Font("Courier", Font.BOLD,13));
       hasta.setBounds(x+270, y+i, w+100, h);
       hasta.setFont(new java.awt.Font("Courier", Font.BOLD,13));

       ASIGNAR.setBounds((x+i)+20, y+i*3, w+200, h);
       VerAsignaciones.setBounds((x+i)+20, y+i*5, w+200, h);
       
       JPasignacionautomatica = new JPanel();
       JPasignacionautomatica.setLayout(null);
       JPasignacionautomatica.setBounds(x+i*5, 5, 620, 300);//posicion y tamaño del panel
       //JPasignacionautomatica.setBorder(BorderFactory.createTitledBorder("Asignacion Automatica"));
       JPasignacionautomatica.setOpaque(false);//defino el fondo
       /*
       JPasignacionautomatica.add(JLdiai);
       JPasignacionautomatica.add(JLdiaf);
       JPasignacionautomatica.add(JLmesi);
       JPasignacionautomatica.add(JLmesf);
       JPasignacionautomatica.add(JLañoi);
       JPasignacionautomatica.add(JLañof);
       JPasignacionautomatica.add(JCdiai);
       JPasignacionautomatica.add(JCdiaf);
       JPasignacionautomatica.add(JCmesi);
       JPasignacionautomatica.add(JCmesf);
       JPasignacionautomatica.add(JCañoi);
       JPasignacionautomatica.add(JCañof);
       */
       JPasignacionautomatica.add(desde);
       JPasignacionautomatica.add(hasta);
       JPasignacionautomatica.add(tdesde);
       JPasignacionautomatica.add(thasta);
       JPasignacionautomatica.add(ASIGNAR);
       JPasignacionautomatica.add(VerAsignaciones);
       
       VerAsignaciones.setVisible(false);
       return JPasignacionautomatica;
    }
    private void agregarOyenteDesde() {
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
                });
    }
    private void agregarOyenteHasta() {
        chasta.getDayChooser().addPropertyChangeListener(
                new java.beans.PropertyChangeListener() {

                    @Override
                    public void propertyChange(java.beans.PropertyChangeEvent evt) {
                        if (evt.getPropertyName().compareTo("day") == 0) {
                            SimpleDateFormat formatoDeFecha = new SimpleDateFormat("dd/MM/yyyy");
                            thasta.setText(formatoDeFecha.format(chasta.getDate()));
                        }else{
                            SimpleDateFormat formatoDeFecha = new SimpleDateFormat("dd/MM/yyyy");
                            thasta.setText(formatoDeFecha.format(chasta.getDate()));
                        }
                    }
                });
    }
    public void actionPerformed(ActionEvent g) {
        Object f = g.getSource();
        if(f.equals(ASIGNAR))
        {
            
                String sdesde = tdesde.getText();
                String shasta = thasta.getText();
                Date desde = this.ParseFecha(sdesde);
                Date hasta = this.ParseFecha(shasta);
            Postgres basedato = new Postgres();
            basedato.estableceConexion();
            if(!basedato.asignacionesVaciaFecha(desde,hasta)){
                JOptionPane.showMessageDialog(this,
                    "No se pudo realizar la asignacion. Ud. tiene asignaciones realizadas en las fechas señaladas.",
                        "FALLO",JOptionPane.WARNING_MESSAGE);
                 }else{
                //basedato.borrarAsignaciones();
                //String sdesde = JCdiai.getSelectedItem().toString()+"/"+JCmesi.getSelectedItem().toString()+"/"+JCañoi.getSelectedItem().toString();
                //String shasta = JCdiaf.getSelectedItem().toString()+"/"+JCmesf.getSelectedItem().toString()+"/"+JCañof.getSelectedItem().toString();
                
                Vector <Integer> capacidades;
                Vector <String> nombres;
                Vector <Integer> comisiones;
                Vector <Integer> horasinicio;
                Vector <Integer> horasfin;
                Vector <String> orientaciones;
                String capacidad;
                String nombre;
                String orientacion;
                String comision;
                String horainicio;
                String horafin;
                ManejaMaterias asignador;
                Vector <Aula> aulas;
                Aula aula;
             for (int d=1; d<7;d++){//for de los dias
                for (int i=8; i <23; i++){//for de las horas
                    ResultSet rs = null;
                    aulas = new Vector <Aula>();
                    capacidades = new Vector <Integer>();
                    nombres = new Vector <String>();
                    comisiones = new Vector <Integer>();
                    horasinicio = new Vector <Integer>();
                    horasfin = new Vector <Integer>();
                    orientaciones = new Vector <String>();
                    rs = basedato.dameAulasDisponibles2(d,i);//REVEER ESTA FUNCION YA NO VAMOS A BORRAR ASIGNACIONES DE AÑOS ANTERIORES
                    String idaula;
                    try{
                    while (rs.next())
                    {
                        idaula = rs.getString (1);
                        capacidad = rs.getString(2);

                        aula = new Aula(Integer.parseInt(idaula), Integer.parseInt(capacidad));
                        aulas.add(aula);
                    }

                    } catch(Exception e)
                    {
                        System.out.println("Problema al obtener las aulas disponibles ");
                    }
                    rs = null;
                    rs = basedato.obtenerComisionesXDiaHora(d, i);
                    try{
                    while (rs.next())
                    {
                        nombre = rs.getString (1);
                        orientacion = rs.getString(2);
                        comision = rs.getString(3);
                        capacidad = rs.getString(4);
                        horainicio = rs.getString(5);
                        horafin = rs.getString(6);
                        capacidades.add(Integer.parseInt(capacidad));
                        nombres.add(nombre);
                        orientaciones.add(orientacion);
                        comisiones.add(Integer.parseInt(comision));
                        horasinicio.add(Integer.parseInt(horainicio));
                        horasfin.add(Integer.parseInt(horafin));
                    }

                    } catch(Exception e)
                    {
                        System.out.println("PROBLEMA A CONSULTAR TABLA MATERIAS HORAS");
                    }
                    asignador = new ManejaMaterias();
                    asignador.inciar(nombres, capacidades, comisiones, horasinicio, horasfin, orientaciones, aulas);
                    asignador.cargarasignaciones();
                    rs = null;
                    for (int j=0; j < asignador.obtenertamaño(); j++){
                        basedato.cargarTablaAsignacions2(asignador.obtenerunnombre(j), asignador.obtenercomision(j), asignador.obtenerorientacion(j), asignador.obtenercapacidad(j), asignador.obtenerhorainicio(j), asignador.obtenerhorafin(j), d, asignador.obtenerunaaula(j),desde,hasta);
                    }
                }
             }
           }

           basedato.cierraConexion();
           this.cargarTablaMateriasNoAsigndas();
           VerAsignaciones.setVisible(true);
        }
    }
    
    public void ocultarBarraTitulo()
    {
        Barra = ((javax.swing.plaf.basic.BasicInternalFrameUI) getUI()).getNorthPane();
        dimBarra = Barra.getPreferredSize();
        Barra.setSize(0,0);
        Barra.setPreferredSize(new Dimension(0,0));
        repaint();
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
    private JPanel pnlTablaMateriasNoAsignadas()
    {
        tnoasignadas = new DefaultTableModel();
        tnoasignadas.setColumnIdentifiers(colstnoasignadas);
        tablanoasignadas = new JTable(tnoasignadas){
        public boolean isCellEditable(int rowIndex, int vColIndex) {
            return false;
        }};//CON ESTO DESHABILITO LA EDICION MANUAL DE LA TABLA SOLO SE LA PUEDE EDITAR DESDE EL FORMUALRIO
        tablanoasignadas.setRowSorter(new TableRowSorter(tnoasignadas));
        scrlptnoasignadas = new JScrollPane(tablanoasignadas);
        scrlptnoasignadas.setBounds(10,20,1325,300);
        //centra los datos de la tabla
            DefaultTableCellRenderer modelocentrar = new DefaultTableCellRenderer();
            modelocentrar.setHorizontalAlignment(SwingConstants.CENTER); tablanoasignadas.getColumnModel().getColumn(0).setCellRenderer(modelocentrar);
            modelocentrar.setHorizontalAlignment(SwingConstants.CENTER); tablanoasignadas.getColumnModel().getColumn(1).setCellRenderer(modelocentrar);
            modelocentrar.setHorizontalAlignment(SwingConstants.CENTER); tablanoasignadas.getColumnModel().getColumn(2).setCellRenderer(modelocentrar);
            modelocentrar.setHorizontalAlignment(SwingConstants.CENTER); tablanoasignadas.getColumnModel().getColumn(3).setCellRenderer(modelocentrar);
            modelocentrar.setHorizontalAlignment(SwingConstants.CENTER); tablanoasignadas.getColumnModel().getColumn(4).setCellRenderer(modelocentrar);
            modelocentrar.setHorizontalAlignment(SwingConstants.CENTER); tablanoasignadas.getColumnModel().getColumn(5).setCellRenderer(modelocentrar);
            modelocentrar.setHorizontalAlignment(SwingConstants.CENTER); tablanoasignadas.getColumnModel().getColumn(6).setCellRenderer(modelocentrar);
            modelocentrar.setHorizontalAlignment(SwingConstants.CENTER); tablanoasignadas.getColumnModel().getColumn(7).setCellRenderer(modelocentrar);
            modelocentrar.setHorizontalAlignment(SwingConstants.CENTER); tablanoasignadas.getColumnModel().getColumn(8).setCellRenderer(modelocentrar);
            modelocentrar.setHorizontalAlignment(SwingConstants.CENTER); tablanoasignadas.getColumnModel().getColumn(9).setCellRenderer(modelocentrar);
        //INTENTANDO AGREGAR EVENTOS A LA TABLA
        JPanel pnl = new JPanel();
        pnl.setLayout(null);
        pnl.setBounds(5, 350, 1345, 330);
        pnl.setBorder(BorderFactory.createTitledBorder("Materias NO ASIGNADAS"));
        pnl.setOpaque(false);
        tablanoasignadas.getColumnModel().getColumn(3).setPreferredWidth(0);
        tablanoasignadas.getColumnModel().getColumn(4).setPreferredWidth(0);
        tablanoasignadas.getColumnModel().getColumn(5).setPreferredWidth(10);
        tablanoasignadas.getColumnModel().getColumn(6).setPreferredWidth(0);
        tablanoasignadas.getColumnModel().getColumn(7).setPreferredWidth(0);
        tablanoasignadas.getColumnModel().getColumn(0).setPreferredWidth(150);
        pnl.add(scrlptnoasignadas);
        return pnl;
    }
    public void cargarTablaMateriasNoAsigndas(){
        for (int i = 0; i < tablanoasignadas.getRowCount(); i++) {
            tnoasignadas.removeRow(i);
            i-=1;
            }
        Postgres basedato = new Postgres();
        basedato.estableceConexion();
        ResultSet rs = null;
        rs = basedato.obtenerMateriasNoAsignadas();
        try{
        while (rs.next())
        {
            VerAsignaciones.setVisible(true);
            String materia=rs.getString(1);
            String facultad=rs.getString(2);
            String jefecatedra=rs.getString(3);
            String ncomision=rs.getString(4);
            String dia=rs.getString(5);
            String cantalumnos=rs.getString(6);
            String horainicio=rs.getString(7);
            String horafin=rs.getString(8);
            String comisionresponsable=rs.getString(9);
            String comisionbox=rs.getString(10);
            tnoasignadas.addRow(new Object[]{materia,facultad,jefecatedra,ncomision,dia,cantalumnos,horainicio,horafin,comisionresponsable,comisionbox});
        }

        } catch(Exception e)
        {
            System.out.println("Problema al cargar la tabla comisiones no asignadas ");
        }
        basedato.cierraConexion();
    }
    public FondoVentana pnlImagen(){
                  fondo = new FondoVentana();
          this.add(fondo);
          fondo.setBounds(50, 50, 70, 70);
          fondo.setOpaque(false);//defino el fondo
          return fondo;
    }

}
