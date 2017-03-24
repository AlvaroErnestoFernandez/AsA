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

public class BorrarAsignaciones extends JInternalFrame implements ActionListener {

    private JLabel JLdiai,JLdiaf,JLmesi,JLmesf,JLañoi,JLañof,desde,hasta,mborrar;
    private JComboBox JCdiai,JCdiaf,JCmesi,JCmesf,JCañoi,JCañof;
    private JPanel JPasignacionautomatica;
    private JButton BORRAR, VerAsignaciones,sino;
    //elementos para ocultar barra de titulo
    private JComponent Barra = ((javax.swing.plaf.basic.BasicInternalFrameUI) getUI()).getNorthPane();
    private Dimension dimBarra = null;
    //tabla de materias no asignadas
    private JTable tablaasignadas;
    private JScrollPane scrlptasignadas;//scroll tabla
    private DefaultTableModel tasignadas;
    private final String [] colstasignadas = {"Fecha","Aula","Materia","Facultad","Jefe de Catedra","Comision","Dia","Cantidad Alumnos","Hora Inicio","Hora Fin","Responsable Comision","Box del Responsable"};
    //imagen de fondo
    private FondoVentana fondo;
    //
    private JCalendar cdesde;
    private JCalendar chasta;
    private JTextField tdesde;
    private JTextField thasta;

    private Date fdesde;
    private Date fhasta;

    public BorrarAsignaciones(){
        super("Asignacion Automatica", false, // resizable
                    false, // closable
                    false, // maximizable
                    false);// iconifiable
        this.getContentPane().setLayout(null);
        this.getContentPane().add(this.pnlasignacionautomatica());
        this.getContentPane().add(this.pnlTablaMateriasAsignadas());
        this.getContentPane().add(this.pnlCalendarioHasta());
        this.getContentPane().add(this.pnlCalendarioDesde());
        //this.getContentPane().setBackground(new Color(155,193,232));
        //this.getContentPane().add(this.pnlImagen());
        this.setBorder(null);
        this.ocultarBarraTitulo();
        this.agregarOyenteDesde();
        this.agregarOyenteHasta();

        mborrar=new JLabel("Esta seguro que desea borrar las siguientes asignaciones");
        sino=new JButton("Si");
        sino.setToolTipText("Esta seguro?");
        sino.addActionListener(this);
        mborrar.setBounds(20, 300, 440, 30);
        mborrar.setFont(new java.awt.Font("Courier", Font.BOLD,14));
        mborrar.setForeground(Color.red);
        sino.setBounds(450, 300, 50, 30);
        mborrar.setVisible(false);
        sino.setVisible(false);
        this.add(mborrar);
        this.add(sino);


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
       BORRAR = new JButton("BORRAR ASIGNACIONES");
       VerAsignaciones = new JButton("Ver Asignaciones");
       BORRAR.setToolTipText("ASIGNAR AULAS AUTOMATICAMENTE");
       BORRAR.addActionListener(this);

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

       BORRAR.setBounds((x+i)+20, y+i*3, w+200, h);
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
       JPasignacionautomatica.add(BORRAR);
       //JPasignacionautomatica.add(VerAsignaciones);

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
        if(f.equals(BORRAR))
        {
         String sdesde = tdesde.getText();
         String shasta = thasta.getText();
         fdesde = this.ParseFecha(sdesde);
         fhasta = this.ParseFecha(shasta);
         this.cargarTablaMateriasAsigndas(fdesde, fhasta);
         sino.setVisible(true);         
         mborrar.setVisible(true);

        }
        if(f.equals(sino))
        {
            for (int i = 0; i < tablaasignadas.getRowCount(); i++) {
            tasignadas.removeRow(i);
            i-=1;
            }
            Postgres basedato = new Postgres();
            basedato.estableceConexion();
            basedato.borrarAsignaciones(fdesde, fhasta);
            basedato.cierraConexion();
            sino.setVisible(false);
            mborrar.setVisible(false);
            JOptionPane.showMessageDialog(this,
                    "Las Asignaciones se han BORRADO con exito",
                        "BORRAR",JOptionPane.WARNING_MESSAGE);
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
    private JPanel pnlTablaMateriasAsignadas()
    {
        tasignadas = new DefaultTableModel();
        tasignadas.setColumnIdentifiers(colstasignadas);
        tablaasignadas = new JTable(tasignadas){
        public boolean isCellEditable(int rowIndex, int vColIndex) {
            return false;
        }};//CON ESTO DESHABILITO LA EDICION MANUAL DE LA TABLA SOLO SE LA PUEDE EDITAR DESDE EL FORMUALRIO
        tablaasignadas.setRowSorter(new TableRowSorter(tasignadas));
        scrlptasignadas = new JScrollPane(tablaasignadas);
        scrlptasignadas.setBounds(10,20,1325,300);
        //centra los datos de la tabla
            DefaultTableCellRenderer modelocentrar = new DefaultTableCellRenderer();
            modelocentrar.setHorizontalAlignment(SwingConstants.CENTER); tablaasignadas.getColumnModel().getColumn(0).setCellRenderer(modelocentrar);
            modelocentrar.setHorizontalAlignment(SwingConstants.CENTER); tablaasignadas.getColumnModel().getColumn(1).setCellRenderer(modelocentrar);
            modelocentrar.setHorizontalAlignment(SwingConstants.CENTER); tablaasignadas.getColumnModel().getColumn(2).setCellRenderer(modelocentrar);
            modelocentrar.setHorizontalAlignment(SwingConstants.CENTER); tablaasignadas.getColumnModel().getColumn(3).setCellRenderer(modelocentrar);
            modelocentrar.setHorizontalAlignment(SwingConstants.CENTER); tablaasignadas.getColumnModel().getColumn(4).setCellRenderer(modelocentrar);
            modelocentrar.setHorizontalAlignment(SwingConstants.CENTER); tablaasignadas.getColumnModel().getColumn(5).setCellRenderer(modelocentrar);
            modelocentrar.setHorizontalAlignment(SwingConstants.CENTER); tablaasignadas.getColumnModel().getColumn(6).setCellRenderer(modelocentrar);
            modelocentrar.setHorizontalAlignment(SwingConstants.CENTER); tablaasignadas.getColumnModel().getColumn(7).setCellRenderer(modelocentrar);
            modelocentrar.setHorizontalAlignment(SwingConstants.CENTER); tablaasignadas.getColumnModel().getColumn(8).setCellRenderer(modelocentrar);
            modelocentrar.setHorizontalAlignment(SwingConstants.CENTER); tablaasignadas.getColumnModel().getColumn(9).setCellRenderer(modelocentrar);
            modelocentrar.setHorizontalAlignment(SwingConstants.CENTER); tablaasignadas.getColumnModel().getColumn(10).setCellRenderer(modelocentrar);
            modelocentrar.setHorizontalAlignment(SwingConstants.CENTER); tablaasignadas.getColumnModel().getColumn(11).setCellRenderer(modelocentrar);
        //INTENTANDO AGREGAR EVENTOS A LA TABLA
        JPanel pnl = new JPanel();
        pnl.setLayout(null);
        pnl.setBounds(5, 350, 1345, 330);
        pnl.setBorder(BorderFactory.createTitledBorder("Asignaciones POR BORRAR"));
        pnl.setOpaque(false);
        //tablaasignadas.getColumnModel().getColumn(0).setPreferredWidth(0);
        tablaasignadas.getColumnModel().getColumn(1).setPreferredWidth(0);
        tablaasignadas.getColumnModel().getColumn(2).setPreferredWidth(150);
        tablaasignadas.getColumnModel().getColumn(5).setPreferredWidth(0);
        tablaasignadas.getColumnModel().getColumn(6).setPreferredWidth(0);
        tablaasignadas.getColumnModel().getColumn(8).setPreferredWidth(0);
        tablaasignadas.getColumnModel().getColumn(9).setPreferredWidth(0);
        tablaasignadas.getColumnModel().getColumn(9).setPreferredWidth(0);
        pnl.add(scrlptasignadas);
        return pnl;
    }
    public void cargarTablaMateriasAsigndas(Date desde, Date hasta){
        for (int i = 0; i < tablaasignadas.getRowCount(); i++) {
            tasignadas.removeRow(i);
            i-=1;
            }
        Postgres basedato = new Postgres();
        basedato.estableceConexion();
        ResultSet rs = null;
        rs = basedato.obtenerMateriasAsignadas(desde, hasta);
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
            String fecha=rs.getString(11);
            String aula=rs.getString(12);
            tasignadas.addRow(new Object[]{fecha,aula,materia,facultad,jefecatedra,ncomision,dia,cantalumnos,horainicio,horafin,comisionresponsable,comisionbox});
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