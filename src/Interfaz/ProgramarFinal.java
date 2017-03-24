/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//me gustaria ir eliminando las materias de la tabla materias que se agregaron a la tabla finales
package Interfaz;

import com.toedter.calendar.JDateChooser;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import Estructura.*;
import java.sql.ResultSet;
import java.util.Date;
import java.util.Vector;
import javax.swing.table.TableColumn;

/**
 *
 * @author Alvaro
 */
public class ProgramarFinal extends JInternalFrame implements ActionListener{
    private JComboBox facultades;
    private JDateChooser fecha;
    private JLabel Lfecha;
    private JButton continuar,asignar,limpiar;
    private JSpinner spin;
    private SpinnerNumberModel spinModel;
    private JLabel Lspin;
    private JTextField filtro;
    private JLabel Lfiltro;
    TableRowSorter<DefaultTableModel> sorter;
    RowFilter<DefaultTableModel, Object> rf = null;
    private JTable tablamaterias,tablafinales;
    private JScrollPane scrltmaterias,scrltfinales;
    private DefaultTableModel tmaterias,tfinales;

    private final Object[] colsmaterias = {"Materia","Jefe de Catedra"};
    private final Object[] colsfinales = {"Materia","Cantidad de Inscriptos","Hora Inicio","Hora Fin"};

    private JComponent Barra = ((javax.swing.plaf.basic.BasicInternalFrameUI) getUI()).getNorthPane();
    private Dimension dimBarra = null;

    private Postgres basedato;

    private Vector<Integer> idsmaterias;
    private Vector<String> nombresmaterias;

    private Vector<Integer> horasinicio2;
    private Vector<Integer> horasfin2;
    private Vector<Integer> inscriptos2;
    private Vector<String> nombres2;

    public ProgramarFinal(){
        super("Programar Final", false, // resizable
                    false, // closable
                    false, // maximizable
                    false);// iconifiable
        this.getContentPane().setLayout(null);
        //this.getContentPane().setBackground(new Color(155,193,232));
        this.getContentPane().add(this.pnlSeleccion());
        this.getContentPane().add(this.pnlTablaDias());  
        ocultarBarraTitulo();
        this.getContentPane().add(this.pnlTablaMaterias());
        //this.getContentPane().add(this.pnlTablaFinales());

    }
    public JPanel pnlSeleccion(){
        facultades = new JComboBox(new String[] {"Ciencias Exactas","Ciencias Económicas","Ingenieria","Ciencas Naturales","Ciencias de la Salud","Humanidades"});
            facultades.setBackground(Color.white);
        fecha = new JDateChooser();
            fecha.setBackground(Color.white);
            Lfecha = new JLabel("Fecha del Final");
        continuar = new JButton("Continuar");
        asignar = new JButton("Asignar");
        limpiar = new JButton("Limpiar");
            continuar.setToolTipText("Continuar");
            asignar.setToolTipText("Asignar aulas a las materias seleccionadas");
            limpiar.setToolTipText("Limpiar seleccion");
            continuar.addActionListener(this);
            asignar.addActionListener(this);
            limpiar.addActionListener(this);
            continuar.setBackground(Color.white);
            asignar.setBackground(Color.white);
            limpiar.setBackground(Color.white);
        filtro = new JTextField(50){
			@Override
			protected void fireCaretUpdate(CaretEvent arg0) {
				try {
			        rf = RowFilter.regexFilter(filtro.getText(),0);
			    } catch (java.util.regex.PatternSyntaxException e) {
			        return;
			    } sorter.setRowFilter(rf);
				super.fireCaretUpdate(arg0);
			}
		};
           Lfiltro = new JLabel("Filtro Materias");
           
        spinModel = new SpinnerNumberModel(1,1,10,1);
        spin = new JSpinner(spinModel);//horas de dictado
        Lspin = new JLabel("Horas");
        int x = 20;//eje x
        int y = 30;//eje y
        int w = 60;//ancho
        int h = 30;//alto
        int i = 50;//incremento
        facultades.setBounds(x, y-10, w+130, h);
        fecha.setBounds(x+400, y-10, w+100, h);
        Lfecha.setBounds(x+310, y-10, w+160, h);//nota cambiar la fuente de un label genera RETARDOS
        spin.setBounds(x+240, y-10, w, h);
        Lspin.setBounds(x+207, y-10, w, h);
        filtro.setBounds(x+120, y+i, w+300, h);
        Lfiltro.setBounds(x+20, y+i, w+40, h);
        continuar.setBounds(x+60, y+i*2+10, w+40, h+10);
        limpiar.setBounds(x+200, y+i*2+10, w+40, h+10);
        asignar.setBounds(x+340, y+2*i+10, w+40, h+10);

        
        //Lfecha.setFont(new java.awt.Font("Courier", Font.BOLD,14));
        JPanel pnl = new JPanel();
        pnl.setLayout(null);
        pnl.setBounds(5, 5, 600, 250);//posicion y tamaño del panel
        pnl.setBorder(BorderFactory.createTitledBorder(""));
        pnl.setOpaque(false);//defino el fondo
        pnl.add(facultades);
        pnl.add(fecha);
        pnl.add(Lfecha);
        pnl.add(filtro);
        pnl.add(Lfiltro);
        pnl.add(continuar);
        pnl.add(limpiar);
        pnl.add(asignar);
        //pnl.add(spin);
        //pnl.add(Lspin);
        habilitarDeshabilitar(false);
        return pnl;
    }
    private JPanel pnlTablaMaterias()
    {

        tmaterias = new DefaultTableModel();
        tmaterias.setColumnIdentifiers(colsmaterias);
        sorter = new TableRowSorter<DefaultTableModel>(tmaterias);
        tablamaterias = new JTable(tmaterias){
        public boolean isCellEditable(int rowIndex, int vColIndex) {
            return false;
        }};//CON ESTO DESHABILITO LA EDICION MANUAL DE LA TABLA SOLO SE LA PUEDE EDITAR DESDE EL FORMUALRIO;
        DefaultTableCellRenderer modelocentrar = new DefaultTableCellRenderer();
            modelocentrar.setHorizontalAlignment(SwingConstants.CENTER); tablamaterias.getColumnModel().getColumn(0).setCellRenderer(modelocentrar);
            modelocentrar.setHorizontalAlignment(SwingConstants.CENTER); tablamaterias.getColumnModel().getColumn(1).setCellRenderer(modelocentrar);
        tablamaterias.setAutoCreateRowSorter(true);
        tablamaterias.setRowSorter(sorter);
        scrltmaterias = new JScrollPane(tablamaterias);
        setEventoMouseClicked(tablamaterias);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        scrltmaterias.setBounds(10,5,700,230);


        JPanel pnl = new JPanel();

        pnl.setLayout(null);
        pnl.setBounds(610, 5, 720, 250);
        pnl.setBorder(BorderFactory.createTitledBorder(""));
        pnl.setOpaque(false);
        
        pnl.add(scrltmaterias);

        return pnl;
    }
     private void setEventoMouseClicked(JTable tbl)
    {
        tbl.addMouseListener(new java.awt.event.MouseAdapter() {

        @Override
        public void mouseClicked(MouseEvent e) {
        tblEjemploMouseClicked(e);
        }
        });
    }

    private void tblEjemploMouseClicked(java.awt.event.MouseEvent evt) {        
        int row = tablamaterias.rowAtPoint(evt.getPoint());
        if (row >= 0 && tablamaterias.isEnabled())
        {
               String materia=tmaterias.getValueAt(row,0).toString();
               boolean b = buscarMateriaTablaFinales(materia);
               if(b){
                   int idm = this.buscarMateria(materia);
                   basedato = new Postgres();
                   basedato.estableceConexion();
                   if(!basedato.materiFinal(idm, fecha.getDate())){
                   tfinales.addRow(new Object[]{materia});
                   }else{
                      JOptionPane.showMessageDialog(this,
                        "La materia ya tiene un final asignado para la fecha seleccionada",
                            "FALLO CONSULTA",JOptionPane.WARNING_MESSAGE); 
                   }
                   basedato.cierraConexion();
               }else{
                   JOptionPane.showMessageDialog(this,
                        "La materia ya esta en la lista de materias por asignar",
                            "FALLO CONSULTA",JOptionPane.WARNING_MESSAGE);
               }
        }
    }
    public void cargarTablaMaterias(){
         filtro.setText("");
         tablamaterias.getSelectionModel().clearSelection();
        basedato = new Postgres();
        basedato.estableceConexion();
        ResultSet rs = null;
        idsmaterias = new Vector<Integer>();
        nombresmaterias = new Vector<String>();
        rs = basedato.obtenerMateriasFacultad(facultades.getSelectedIndex());
        int id;//id de la materia pronto me gustaria implemetarlo en un vector
        String materia;
        String jefecatedra;
        try{
        while (rs.next())
        {
            id = rs.getInt(3);
            materia = rs.getString (1);
            jefecatedra = rs.getString(2);
            idsmaterias.add(id);
            nombresmaterias.add(materia);
            tmaterias.addRow(new Object[]{materia,jefecatedra});
        }

        } catch(Exception e)
        {
            System.out.println("Problema al cargar la tabla materia por facultad");
        }
        basedato.cierraConexion();
    }
    private JPanel pnlTablaDias()
    {
        tfinales = new DefaultTableModel();
        tfinales.setColumnIdentifiers(colsfinales);
        
        tablafinales = new JTable(tfinales){

            public long serialVersionUID = 1L;
            @Override
            public Class getColumnClass(int column) {
                switch (column) {
                   case 0:
                        return String.class;
                    case 1:
                        return Integer.class;
                    case 2:
                        return Integer.class;
                    default:
                        return Integer.class;
                }
            }
            public boolean isCellEditable(int rowIndex, int vColIndex) {
                boolean b=true;
                if (vColIndex==0){
                 b=false;}
                return b;
        }};

        TableColumn col=tablafinales.getColumnModel().getColumn(2);

        TableColumn col2=tablafinales.getColumnModel().getColumn(3);

        String op[]={"08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
        String op2[]={"09","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24"};

        JComboBox horasinicio=new JComboBox(op);
        JComboBox horasfin=new JComboBox(op2);

        col.setCellEditor(new DefaultCellEditor(horasinicio));
        col2.setCellEditor(new DefaultCellEditor(horasfin));

        scrltfinales = new JScrollPane(tablafinales);
        scrltfinales.setBounds(10,20,1280,380);
        //centra los datos de la tabla
        DefaultTableCellRenderer modelocentrar = new DefaultTableCellRenderer();
            modelocentrar.setHorizontalAlignment(SwingConstants.CENTER); tablafinales.getColumnModel().getColumn(0).setCellRenderer(modelocentrar);
            modelocentrar.setHorizontalAlignment(SwingConstants.CENTER); tablafinales.getColumnModel().getColumn(1).setCellRenderer(modelocentrar);
            modelocentrar.setHorizontalAlignment(SwingConstants.CENTER); tablafinales.getColumnModel().getColumn(2).setCellRenderer(modelocentrar);
            modelocentrar.setHorizontalAlignment(SwingConstants.CENTER); tablafinales.getColumnModel().getColumn(3).setCellRenderer(modelocentrar);
        JPanel pnl = new JPanel();
        pnl.setLayout(null);
        pnl.setBounds(5, 250, 1300, 410);
        pnl.setBorder(BorderFactory.createTitledBorder("Materias para asignar"));
        pnl.setOpaque(false);

        pnl.add(scrltfinales);
        return pnl;
    }
    public void ocultarBarraTitulo()
    {
        Barra = ((javax.swing.plaf.basic.BasicInternalFrameUI) getUI()).getNorthPane();
        dimBarra = Barra.getPreferredSize();
        Barra.setSize(0,0);
        Barra.setPreferredSize(new Dimension(0,0));
        repaint();
    }
    public void habilitarDeshabilitar(boolean b){
        filtro.setEnabled(b);
        Lfiltro.setEnabled(b);
        asignar.setEnabled(b);
        limpiar.setEnabled(b);
    }
    public int buscarMateria(String nombre){
        //boolean b=true;
        int i=idsmaterias.size()-1;
        while((!nombresmaterias.get(i).equals(nombre))&&(i>=0)){
            i=i-1;
        }
        int id=-1;
        if(i>=0){
            id=idsmaterias.get(i);
        }
        return id;
    }
    public boolean buscarMateriaTablaFinales(String nombre){
        boolean b=true;
        int i=0;
        while((i<tfinales.getRowCount())&&(b)){
            if(tfinales.getValueAt(i, 0).toString().equals(nombre)){
                b=false;
            }
            i++;
        }
        return b;
    }
    public void actionPerformed(ActionEvent g) {
        Object f = g.getSource();
        if(g.getSource()==continuar){
            Date dia = fecha.getDate();
            if(dia!=null){
            habilitarDeshabilitar(true);
            continuar.setEnabled(false);
            facultades.setEnabled(false);
            fecha.setEnabled(false);
            cargarTablaMaterias();}
            else{
                JOptionPane.showMessageDialog(this,
                        "Seleccione una fecha por favor",
                            "FALLO CONSULTA",JOptionPane.WARNING_MESSAGE);
            }
        }
        if(g.getSource()==limpiar){
            fecha.setDate(null);
            habilitarDeshabilitar(false);
            continuar.setEnabled(true);
            facultades.setEnabled(true);
            fecha.setEnabled(true);
            for (int i = tfinales.getRowCount()-1; i > -1 ; i--) {
            tfinales.removeRow(i);
            }
            for (int i = tmaterias.getRowCount()-1; i > -1 ; i--) {
            tmaterias.removeRow(i);
            }
        }
        if(g.getSource()==asignar)//listo solo falta controlar la no existencia de finales de esa facultad en esa fecha;
        {    //hora me surgio la duda si debo realizar el control por la facultad o por la fecha mmmmm
            boolean b=true;//igual aun falta hacer mas pruebas a la consulta a la base que obtinene las aulas disponibles
            horasinicio2 = new Vector<Integer>();
            horasfin2 = new Vector<Integer>();
            inscriptos2 = new Vector<Integer>();
            nombres2 = new Vector<String>();
            for(int h=0;h<tfinales.getRowCount();h++){
                if((tfinales.getValueAt(h, 1)==null)||(tfinales.getValueAt(h, 2)==null)||(tfinales.getValueAt(h, 3)==null)){
                    b=false;
                }
                if(b){
                nombres2.add(tfinales.getValueAt(h, 0).toString());
                inscriptos2.add(Integer.parseInt(tfinales.getValueAt(h, 1).toString()));
                horasinicio2.add(Integer.parseInt(tfinales.getValueAt(h, 2).toString()));
                horasfin2.add(Integer.parseInt(tfinales.getValueAt(h, 3).toString()));
                }
            }
            if(b){
            Date dia = fecha.getDate();
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
                basedato = new Postgres();
                    basedato.estableceConexion();
            for(int i=8;i<23;i++){//para los horarios                
                for(int j=1;j<6;j++){//para la duracion, hago un filtro de las materias de acuerdo a la duracion
                    int horafinal = i+j;                   
                    aulas = new Vector<Aula>();   
                    capacidades = new Vector <Integer>();
                    nombres = new Vector <String>();
                    comisiones = new Vector <Integer>();
                    horasinicio = new Vector <Integer>();
                    horasfin = new Vector <Integer>();
                    orientaciones = new Vector <String>();
                    boolean c = false;
                    for(int k=0;k<horasinicio2.size();k++){
                        if((horasinicio2.get(k)==i)&&(horasfin2.get(k)==horafinal)){
                        nombre = nombres2.get(k);
                        orientacion = "Final";
                        comision = String.valueOf(this.buscarMateria(nombre));
                        capacidad = String.valueOf(inscriptos2.get(k));
                        horainicio = String.valueOf(horasinicio2.get(k));
                        horafin = String.valueOf(horasfin2.get(k));
                        capacidades.add(Integer.parseInt(capacidad));
                        nombres.add(nombre);
                        orientaciones.add(orientacion);
                        comisiones.add(Integer.parseInt(comision));
                        horasinicio.add(Integer.parseInt(horainicio));
                        horasfin.add(Integer.parseInt(horafin));
                        c=true;
                        }
                    }
                    if(c){
                        basedato.dameAulasDisponiblesFinal(aulas,dia,i,horafinal,facultades.getSelectedIndex());
                        asignador = new ManejaMaterias();
                        asignador.inciar(nombres, capacidades, comisiones, horasinicio, horasfin, orientaciones, aulas);
                        asignador.cargarasignaciones();
                        for (int k=0; k < asignador.obtenertamaño(); k++){
                            basedato.cargarTablaAsignacionsFinales(asignador.obtenerunnombre(k), asignador.obtenercomision(k), asignador.obtenerorientacion(k), asignador.obtenercapacidad(k), asignador.obtenerhorainicio(k), asignador.obtenerhorafin(k), asignador.obtenerunaaula(k), dia);
                        }
                    }
                }
                
            }
                    basedato.cierraConexion();
            JOptionPane.showMessageDialog(this,
                        "SE HAN ASIGNADO CORRECTAMENTE AULAS A LAS MATERIAS SELECCIONADAS",
                            "EXITO",JOptionPane.WARNING_MESSAGE);
            fecha.setDate(null);
            habilitarDeshabilitar(false);
            continuar.setEnabled(true);
            facultades.setEnabled(true);
            fecha.setEnabled(true);
            for (int i = tfinales.getRowCount()-1; i > -1 ; i--) {
            tfinales.removeRow(i);
            }
            for (int i = tmaterias.getRowCount()-1; i > -1 ; i--) {
            tmaterias.removeRow(i);
            }
         }
        else{
                JOptionPane.showMessageDialog(this,
                        "Existen materias por asignar que tienen campos vacion, por favor complete todo los campos",
                            "FALLO ASIGNACION",JOptionPane.WARNING_MESSAGE);
         }
        }
        
    }
    

}
