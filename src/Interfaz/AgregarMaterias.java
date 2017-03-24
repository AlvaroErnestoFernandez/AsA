
package Interfaz;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.awt.*;
import javax.swing.table.*;
import java.util.HashMap;

import Estructura.Postgres;
import java.sql.ResultSet;

public class AgregarMaterias extends JInternalFrame implements ActionListener, ChangeListener, ListSelectionListener{

    private JLabel JLnombremateria,JLfacultad,JLjefecatedra,JLorientacion,JLcomisionencargado,JLbox,JLcapacidad,JLhorainicio,JLhorafin,JLtmateria,JLtcomision,JLncomision;
    private JTextField JTnombremateria,JTfacultad,JTorientacion,JTbox,JTcapacidad,JThorainicio,JThorafin,JTncomision;
    private JComboBox JCfacultad,JCncomision,JCtipo,JChorainicio,JChorafin;
    private JPanel JPmateria;
    private JButton guardarmateria,nuevamateria,modificarmateria,guardarcomision,nuevacomision,modificarcomision,borrarmateria,borrarcomision;
    private Boolean modificarmateriaAI=true;
    //variables del panel tabla materias
    private JTable tablamaterias;
    private JScrollPane scrlptmaterias;//scroll tabla
    private DefaultTableModel tmaterias;
    private final String [] colsmaterias = {"Materia","Facultad","Jefe de Catedra","Comisiones"};
    private int filamodificar=-1;
    private TableRowSorter trsfiltro;
    //variables para el panel de la tabla de las comisones;
    private JTable tablacomisiones;
    private JScrollPane scrlptcomisiones;//scroll tabla
    private DefaultTableModel tcomisiones;
    private final String [] colscomisiones = {"Comision N°","Tipo","Responsable","Box","Capaciad"};
    private int filamodificarcomision=-1;
    private int idmateria = -1;
    private int idcomision = -1;
    //private TableRowSorter trsfiltro;
    //Inicializo la clase que se encarga de conectarce a la base del bato
    private Postgres basedato;
    //atributos para ocultar marco
    private JComponent Barra = ((javax.swing.plaf.basic.BasicInternalFrameUI) getUI()).getNorthPane();
    private Dimension dimBarra = null;
    //tabal de los dias de los dias en los que dictara la comision
    private JTable tabladias;
    private JScrollPane scrlptdias;//scroll tabla
    private DefaultTableModel tdias;
    private final Object[] colsdias = {"Si/No","Dia","Hora Inicio","Hora Fin"};
    public long serialVersionUID = 1L;
    private JComboBox horasinicio,horasfin;
    //autocompletado
    private GTextField autocompletado,JTjefecatedra,JTcomisionencargado;

    

    public AgregarMaterias(){
        super("Agregar Materias", false, // resizable
                    false, // closable
                    false, // maximizable
                    false);// iconifiable
        this.getContentPane().setLayout(null);
        this.getContentPane().add(this.pnlMateria());
        this.getContentPane().add(this.pnlTablaMaterias());
        this.getContentPane().add(this.pnlTablaComisiones());
        this.getContentPane().add(this.pnlTablaDias());
        this.cargarTablaMaterias();
        //this.getContentPane().setBackground(new Color(155,193,232));
        tabladias.setEnabled(false);
        this.setBorder(null);
        this.ocultarBarraTitulo();

        //this.desabilitar();

        this.setSize(1200,600);
        this.setResizable(true);
        //this.setLocationRelativeTo(null);
        this.setVisible(true);

        /*this.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                dispose();
            }
        });
        */
        
    }
    
    public JPanel pnlMateria(){
        //icio los jlabel
        
        JLnombremateria = new JLabel("Materia");
        JLfacultad = new JLabel("Facultad");
        JLjefecatedra = new JLabel("Jefe de Catedra");
        JLorientacion = new JLabel("Tipo");
        JLcomisionencargado = new JLabel("Encargado");
        JLbox = new JLabel("Box");
        JLcapacidad = new JLabel("Capacidad");
        JLhorainicio = new JLabel("Hora de Inicio");
        JLhorafin = new JLabel("Hora de Finalizacion");
        JLtcomision  = new JLabel("");
        JLncomision = new JLabel("Comision N°");
        //inicio los combobox
        JCfacultad = new JComboBox(new String[] {"Ciencias Exactas","Ciencias Económicas","Ingenieria","Ciencas Naturales","Ciencias de la Salud","Humanidades"});
        JCncomision = new JComboBox(new String[] {"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20",});
        JCtipo = new JComboBox(new String[] {"Teoria","Pratica"});
        JChorainicio = new JComboBox(new String[] {"08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"});
        //PARA UNA POSIBLE MEJORA SE PROPONE QUE LOS ITEMS DEL COMBOBOX DE HORA FIN DEBERIAN DEPENDER DE LOS QUE SE SELECIONE EN EL COMBOBOX DE HORA INICIO
        JChorafin = new JComboBox(new String[] {"09","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24"});
        //inicio los textos
        JTnombremateria = new JTextField();
        //JTfacultad = new JTextField();
        //JTjefecatedra = new JTextField();
        //JTorientacion = new JTextField();
        //JTcomisionencargado = new JTextField();
        JTbox = new JTextField();
        JTcapacidad = new JTextField();
        //JThorainicio = new JTextField();
        //JThorafin = new JTextField();
        //JTncomision = new JTextField();



        //inicio de botonos
        guardarmateria = new JButton("Guardar");
        nuevamateria = new JButton("Nueva");
        guardarcomision = new JButton("Guardar");
        nuevacomision = new JButton("Nueva");
        modificarmateria = new JButton("Modificar");
        modificarcomision = new JButton("Modificar");
        borrarmateria = new JButton("Borrar");
        borrarcomision = new JButton("Borrar");
            //comentarios a los botones
            guardarmateria.setToolTipText("Guarda Materia");
            nuevamateria.setToolTipText("Nueva Materia");
            guardarcomision.setToolTipText("Guardar Comision");
            nuevacomision.setToolTipText("Nueva Comision");
            modificarmateria.setToolTipText("Modificar Materia");
            modificarcomision.setToolTipText("Modificar Comision");
            borrarmateria.setToolTipText("Borrar Materia");
            borrarcomision.setToolTipText("Borrar Comision");
            //estamos pendientes de las acciones de los botones
            guardarmateria.addActionListener(this);
            nuevamateria.addActionListener(this);
            guardarcomision.addActionListener(this);
            nuevacomision.addActionListener(this);
            modificarmateria.addActionListener(this);
            modificarcomision.addActionListener(this);
            borrarmateria.addActionListener(this);
            borrarcomision.addActionListener(this);
            //fondo de lo botones
            guardarmateria.setBackground(Color.white);
            nuevamateria.setBackground(Color.white);
            guardarcomision.setBackground(Color.white);
            nuevacomision.setBackground(Color.white);
            modificarmateria.setBackground(Color.white);
            modificarcomision.setBackground(Color.white);
            borrarmateria.setBackground(Color.white);
            borrarcomision.setBackground(Color.white);
            //fondo de los jcombobox
            JCfacultad.setBackground(Color.white);
            JCncomision.setBackground(Color.white);
            JCtipo.setBackground(Color.white);
            JChorainicio.setBackground(Color.white);
            JChorafin.setBackground(Color.white);

       //defino la posicion de los elementos
       int x = 20;//eje x
       int y = 30;//eje y
       int w = 60;//ancho
       int h = 30;//alto
       int i = 40;//incremento
       JLnombremateria.setBounds(x, y, w, h);
       JLfacultad.setBounds(x, y+i, w, h);
       JLjefecatedra.setBounds(x+250, y+i, w+50, h);

       JTnombremateria.setBounds(x+50, y, w+200, h);
       //JTfacultad.setBounds(x+50, y+i, w+100, h);
       JCfacultad.setBounds(x+50, y+i, w+100, h);
       this.cargarAutocompletadoMateria();
       //autocompletado.setBounds(x+350, y, w+100, h);
       JTjefecatedra.setBounds(x+350, y+i, w+100, h);

       i=i+40;

       nuevamateria .setBounds(x+30,y+i,w+50,h);
       guardarmateria.setBounds(x+180,y+i,w+50,h);
       modificarmateria .setBounds(x+330,y+i,w+50,h);
       borrarmateria.setBounds(x+480, y+i, w+50, h);
       //titulo del formulario de las comisiones
       i=i+40;
       JLtcomision.setBounds(x+100,y+i,w+300,h);
       //termina titulo del formulario de las comisiones
       i=i+40;
       JLncomision.setBounds(x, y+i, w+20, h);
       JLorientacion.setBounds(x+185, y+i, w+20, h);
       JLcomisionencargado.setBounds(x, y+200, w+70, h);
       JLbox.setBounds(x+270, y+200, w, h);
       JCncomision.setBounds(x+80, y+i, w, h);
       JCtipo.setBounds(x+225, y+i, w+40, h);
       this.cargarAutocompletadoComision();
       JTcomisionencargado.setBounds(x+70, y+200, w+100, h);
       JTbox.setBounds(x+320, y+200, w-5, h);
      
       JLcapacidad.setBounds(x+360, y+i, w+20, h);
       JTcapacidad.setBounds(x+430, y+i, w+40, h);

       nuevacomision.setBounds(x+30, y+250, w+50, h);
       guardarcomision.setBounds(x+180, y+250, w+50, h);
       modificarcomision.setBounds(x+330, y+250, w+50, h);
       borrarcomision.setBounds(x+480, y+250, w+50, h);

       //inicio panel materia y comision
       JPmateria = new JPanel();
       JPmateria.setLayout(null);
       JPmateria.setBounds(5, 5, 700, 330);//posicion y tamaño del panel
       JPmateria.setBorder(BorderFactory.createTitledBorder("Agregar Materia"));
       JPmateria.setOpaque(false);//defino el fondo
       //agrego los elementos al panel
       JPmateria.add(JLnombremateria);
       JPmateria.add(JLfacultad);
       JPmateria.add(JLjefecatedra);
       JPmateria.add(JTnombremateria);
       //JPmateria.add(JTfacultad);
       JPmateria.add(JCfacultad);
       JPmateria.add(JTjefecatedra);
       JPmateria.add(guardarmateria);
       JPmateria.add(nuevamateria);
       JPmateria.add(JLorientacion);
       JPmateria.add(JLcomisionencargado);
       JPmateria.add(JLbox);
       JPmateria.add(JCtipo);
       JPmateria.add(JTcomisionencargado);
       JPmateria.add(JTbox);
       JPmateria.add(JLcapacidad);
       //JPmateria.add(JLhorainicio);
       //materia.add(JLhorafin);
       JPmateria.add(JTcapacidad);
       //JPmateria.add(JChorainicio);
       //JPmateria.add(JChorafin);
       JPmateria.add(guardarcomision);
       JPmateria.add(nuevacomision);
       JPmateria.add(modificarmateria);
       JPmateria.add(modificarcomision);
       JPmateria.add(JLtcomision);
       JPmateria.add(JLncomision);
       //JPmateria.add(JTncomision);
       JPmateria.add(JCncomision);

       JPmateria.add(borrarcomision);
       JPmateria.add(borrarmateria);
       // cargo un autocompletado
       //JPmateria.add(autocompletado);
       //desactivo los elemtos de las comisiones
       this.deshabilitarComision();
       return JPmateria;
    }
     private JPanel pnlTablaDias()
    {
        Object[][] data = {
            {false,"Lunes"},
            {false,"Martes"},
            {false,"Miercoles"},
            {false,"Jueves"},
            {false,"Viernes"},
            {false,"Sabado"}
        };
        tdias = new DefaultTableModel(data,colsdias);
        tabladias = new JTable(tdias){

            public long serialVersionUID = 1L;

            /*@Override
            public Class getColumnClass(int column) {
            return getValueAt(0, column).getClass();
            }*/
            @Override
            public Class getColumnClass(int column) {
                switch (column) {
                   case 1:
                        return String.class;
                    case 2:
                        return String.class;
                    case 3:
                        return String.class;
                    default:
                        return Boolean.class;
                }
            }
            public boolean isCellEditable(int rowIndex, int vColIndex) {
                boolean b=true;
                if (vColIndex==1){
                 b=false;}
                return b;
        }};

        TableColumn col=tabladias.getColumnModel().getColumn(2);

        TableColumn col2=tabladias.getColumnModel().getColumn(3);

        String op[]={"08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
        String op2[]={"09","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24"};

        horasinicio=new JComboBox(op);
        horasfin=new JComboBox(op2);

        col.setCellEditor(new DefaultCellEditor(horasinicio));
        col2.setCellEditor(new DefaultCellEditor(horasfin));

        scrlptdias = new JScrollPane(tabladias);
        scrlptdias.setBounds(10,20,310,280);
        //centra los datos de la tabla
        DefaultTableCellRenderer modelocentrar = new DefaultTableCellRenderer();
            modelocentrar.setHorizontalAlignment(SwingConstants.CENTER); tabladias.getColumnModel().getColumn(1).setCellRenderer(modelocentrar);
            modelocentrar.setHorizontalAlignment(SwingConstants.CENTER); tabladias.getColumnModel().getColumn(2).setCellRenderer(modelocentrar);
            modelocentrar.setHorizontalAlignment(SwingConstants.CENTER); tabladias.getColumnModel().getColumn(3).setCellRenderer(modelocentrar);

        JPanel pnl = new JPanel();
        pnl.setLayout(null);
        pnl.setBounds(5, 340, 330, 310);
        pnl.setBorder(BorderFactory.createTitledBorder("Dias"));
        pnl.setOpaque(false);

        pnl.add(scrlptdias);
        return pnl;
    }

    private JPanel pnlTablaComisiones(){
        tcomisiones = new DefaultTableModel();
        tcomisiones.setColumnIdentifiers(colscomisiones);
        tablacomisiones = new JTable(tcomisiones){
        public boolean isCellEditable(int rowIndex, int vColIndex) {
            return false;
        }};//CON ESTO DESHABILITO LA EDICION MANUAL DE LA TABLA SOLO SE LA PUEDE EDITAR DESDE EL FORMUALRIO
        tablacomisiones.setRowSorter(new TableRowSorter(tcomisiones));
        scrlptcomisiones = new JScrollPane(tablacomisiones);
        scrlptcomisiones.setBounds(10,20,950,280);
         //centra los datos de la tabla
            DefaultTableCellRenderer modelocentrar = new DefaultTableCellRenderer();
            modelocentrar.setHorizontalAlignment(SwingConstants.CENTER); tablacomisiones.getColumnModel().getColumn(0).setCellRenderer(modelocentrar);
            modelocentrar.setHorizontalAlignment(SwingConstants.CENTER); tablacomisiones.getColumnModel().getColumn(1).setCellRenderer(modelocentrar);
            modelocentrar.setHorizontalAlignment(SwingConstants.CENTER); tablacomisiones.getColumnModel().getColumn(2).setCellRenderer(modelocentrar);
            modelocentrar.setHorizontalAlignment(SwingConstants.CENTER); tablacomisiones.getColumnModel().getColumn(3).setCellRenderer(modelocentrar);
            modelocentrar.setHorizontalAlignment(SwingConstants.CENTER); tablacomisiones.getColumnModel().getColumn(4).setCellRenderer(modelocentrar);
        //INTENTANDO AGREGAR EVENTOS A LA TABLA
        setEventoMouseClicked2(tablacomisiones);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        JPanel pnl = new JPanel();
        pnl.setLayout(null);
        pnl.setBounds(350, 340, 975, 310);
        pnl.setBorder(BorderFactory.createTitledBorder("Comisiones de una materia"));
        pnl.setOpaque(false);
        tablacomisiones.getColumnModel().getColumn(0).setPreferredWidth(0);
        pnl.add(scrlptcomisiones);
        //tablamaterias.setVisible(false);

        return pnl;
    }
     private void setEventoMouseClicked2(JTable tbl)
    {
        tbl.addMouseListener(new java.awt.event.MouseAdapter() {

        @Override
        public void mouseClicked(MouseEvent e) {
        tblEjemploMouseClicked2(e);
        }
        });
    }

    private void tblEjemploMouseClicked2(java.awt.event.MouseEvent evt) {
        int idc=0;
        JTcomisionencargado.setAutocomplete(false);
        int row = tablacomisiones.rowAtPoint(evt.getPoint());
        if (row >= 0 && tablacomisiones.isEnabled())
        {
               //JTncomision.setText(tcomisiones.getValueAt(row,0).toString());
               JCncomision.setSelectedItem(tcomisiones.getValueAt(row,0).toString());
               JCtipo.setSelectedItem(tcomisiones.getValueAt(row,1).toString());
               JTcomisionencargado.setText(tcomisiones.getValueAt(row,2).toString());
               JTbox.setText(tcomisiones.getValueAt(row,3).toString());
               JTcapacidad.setText(tcomisiones.getValueAt(row,4).toString());
                   basedato = new Postgres();
                   basedato.estableceConexion();
                    int idm = basedato.buscarMateriaId(JTnombremateria.getText(),JCfacultad.getSelectedIndex(),JTjefecatedra.getText());
                    //int idm,int ncomision, String tipo, String responsable, String box, int capacidad
                    String nc = tcomisiones.getValueAt(row,0).toString();
                    int nci =Integer.parseInt(nc);
                    String cp = tcomisiones.getValueAt(row,4).toString();
                    int cpi =Integer.parseInt(cp);
                    idc = basedato.buscarComisionId(idm,nci,tcomisiones.getValueAt(row,1).toString(),tcomisiones.getValueAt(row,2).toString(),tcomisiones.getValueAt(row,3).toString(),cpi);
                    basedato.cierraConexion();
                    
        }

        this.deshabilitarComision();
        
        tabladias.setEnabled(false);
        nuevacomision.setEnabled(true);
        modificarcomision.setEnabled(true);
        nuevamateria.setEnabled(true);
        modificarmateria.setEnabled(true);
        borrarcomision.setEnabled(true);
        borrarmateria.setEnabled(true);
        this.limpiarTablaDias();
        this.cargarDiasComisiones(idc);
    }

    private JPanel pnlTablaMaterias()
    {
        tmaterias = new DefaultTableModel();
        tmaterias.setColumnIdentifiers(colsmaterias);
        tablamaterias = new JTable(tmaterias){
        public boolean isCellEditable(int rowIndex, int vColIndex) {
            return false;
        }};//CON ESTO DESHABILITO LA EDICION MANUAL DE LA TABLA SOLO SE LA PUEDE EDITAR DESDE EL FORMUALRIO
        tablamaterias.setRowSorter(new TableRowSorter(tmaterias));
        scrlptmaterias = new JScrollPane(tablamaterias);
        scrlptmaterias.setBounds(10,20,600,300);
        //centra los datos de la tabla
            DefaultTableCellRenderer modelocentrar = new DefaultTableCellRenderer();
            modelocentrar.setHorizontalAlignment(SwingConstants.CENTER); tablamaterias.getColumnModel().getColumn(0).setCellRenderer(modelocentrar);
            modelocentrar.setHorizontalAlignment(SwingConstants.CENTER); tablamaterias.getColumnModel().getColumn(1).setCellRenderer(modelocentrar);
            modelocentrar.setHorizontalAlignment(SwingConstants.CENTER); tablamaterias.getColumnModel().getColumn(2).setCellRenderer(modelocentrar);
            modelocentrar.setHorizontalAlignment(SwingConstants.CENTER); tablamaterias.getColumnModel().getColumn(3).setCellRenderer(modelocentrar);
        //INTENTANDO AGREGAR EVENTOS A LA TABLA
        setEventoMouseClicked(tablamaterias);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        JPanel pnl = new JPanel();
        pnl.setLayout(null);
        pnl.setBounds(720, 5, 630, 330);
        pnl.setBorder(BorderFactory.createTitledBorder("Materias"));
        pnl.setOpaque(false);
        tablamaterias.getColumnModel().getColumn(3).setPreferredWidth(0);
        pnl.add(scrlptmaterias);

        return pnl;
    }
    //a partir de aqui comenzamos con el evento al seleccionar una fila
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

        //bueno el siguiente codigo muestra toda la fila
       /*String cadena="";

        int row = tablamaterias.rowAtPoint(evt.getPoint());
        if (row >= 0 && tablamaterias.isEnabled())
        {
            for (int i=0; i < tablamaterias.getColumnCount();i++)
            {
               cadena=cadena + " " +  tmaterias.getValueAt(row,i).toString();
            }
        }

        JOptionPane.showMessageDialog(null, cadena);*/
        tabladias.setEnabled(true);
        //borro la tabla comisiones
        limpiarTablaDias();
           for (int i = 0; i < tablacomisiones.getRowCount(); i++) {
            tcomisiones.removeRow(i);
            i-=1;
            }
        int idm = -1;
        int row = tablamaterias.rowAtPoint(evt.getPoint());
        if (row >= 0 && tablamaterias.isEnabled())
        {
               JTnombremateria.setText(tmaterias.getValueAt(row,0).toString());
               JCfacultad.setSelectedItem(tmaterias.getValueAt(row,1).toString());
               //JTfacultad.setText(tmaterias.getValueAt(row,1).toString());
               JTjefecatedra.setAutocomplete(false);//desactivo el popup para que no se muestre nada cuando lo recargo
               JTjefecatedra.setText(tmaterias.getValueAt(row,2).toString());
               JLtcomision.setFont(new java.awt.Font("Arial", 0, 22));
               JLtcomision.setText(tmaterias.getValueAt(row,0).toString());
               basedato = new Postgres();
                    basedato.estableceConexion();
                    idm = basedato.buscarMateriaId(tmaterias.getValueAt(row,0).toString(),JCfacultad.getSelectedIndex(),tmaterias.getValueAt(row,2).toString());
                    basedato.cierraConexion();
        }

        JTnombremateria.setEditable(false);
        //JTfacultad.setEditable(false);
        JCfacultad.setEnabled(false);
        JTjefecatedra.setEditable(false);
        this.limpiarComision();
        
        this.habilitarComision();
        tabladias.setEnabled(true);

        this.cargarTablaComisiones(idm);
        JCncomision.setSelectedIndex(tcomisiones.getRowCount());

    }
    //aqui terminan los eventos que utilizamos para seleccionar una fila
    //ahora empezamos con el metodo que sive para filtrar la tabla

    //aqui termina el filtro todavia sin exito
    public void valueChanged(ListSelectionEvent e){

    }

    public void stateChanged(ChangeEvent e){

    }

    public void actionPerformed(ActionEvent g){
        Object f = g.getSource();
        if(f.equals(guardarmateria))//guardar materia en la tabla del panel
        {
            if((!JTnombremateria.getText().equals(""))&&(!JTjefecatedra.getText().equals("")))
                {
                    //desabilito los elementos de las materias
                    JTnombremateria.setEditable(false);
                    //JTfacultad.setEditable(false);
                    JCfacultad.setEnabled(false);
                    JTjefecatedra.setEditable(false);
                    guardarmateria.setEnabled(false);
                    //recojo los datos de las materias;
                    String materia = JTnombremateria.getText();
                    String facultad = (String) JCfacultad.getSelectedItem();
                    String jefecatedra = JTjefecatedra.getText();
                    //reinicio los compos de las materias
                    //JTnombremateria.setText(null);
                    //JTfacultad.setText(null);
                    //JTjefecatedra.setText(null);
                    //habilito los elementos de la comision
                    tabladias.setEnabled(true);
                    this.habilitarComision();
                    //defino el titutlo de las comisiones
                        //defino un nuevo tamaño de fuente
                    JLtcomision.setFont(new java.awt.Font("Tahoma", 0, 22));
                    JLtcomision.setText(materia);
                    //agrego una fila
                    tmaterias.addRow(new Object[]{materia,facultad,jefecatedra,"0"});
                    basedato = new Postgres();
                    basedato.estableceConexion();
                    basedato.cargarTablaMaterias(materia,JCfacultad.getSelectedIndex(), jefecatedra);
                    basedato.cierraConexion();
                }
            else{
                JOptionPane.showMessageDialog(this,
                    "No se pudo realizar la operacion. Existe un Campo vacio",
                        "FALLO",JOptionPane.WARNING_MESSAGE);
            }
        }
        if(f.equals(modificarmateria)){//aun no estoy tan de conbencido de dejar que modifique todos los datos
            if(!JTnombremateria.isEditable()){//con esto pregunto si el JTextfield no esta seleccionado
                JTjefecatedra.setAutocomplete(true);
                String materia = JTnombremateria.getText();
                String facultad = (String) JCfacultad.getSelectedItem();
                String jefecatedra = JTjefecatedra.getText();
                filamodificar = this.buscarMateriaTabla(materia,facultad,jefecatedra);
                    basedato = new Postgres();
                    basedato.estableceConexion();
                    idmateria = basedato.buscarMateriaId(materia,JCfacultad.getSelectedIndex(), jefecatedra);
                    basedato.cierraConexion();
                //JLnombremateria.setText(Integer.toString(filamodificar));
                JTnombremateria.setEditable(true);
                //JTfacultad.setEditable(true);
                JCfacultad.setEnabled(true);
                JTjefecatedra.setEditable(true);
                this.deshabilitarComision();
                tabladias.setEnabled(false);
                modificarmateria.setEnabled(true);
            }else{
                JTjefecatedra.setAutocomplete(false);
                //modifico las celdas
                tmaterias.setValueAt(JTnombremateria.getText(), filamodificar, 0);
                tmaterias.setValueAt((String) JCfacultad.getSelectedItem(), filamodificar, 1);
                tmaterias.setValueAt(JTjefecatedra.getText(), filamodificar, 2);
                basedato = new Postgres();
                    basedato.estableceConexion();
                    basedato.moidificarMateria(idmateria,JTnombremateria.getText(),JCfacultad.getSelectedIndex(), JTjefecatedra.getText());
                    basedato.cierraConexion();
                //oculto los campos de las materias
                JTnombremateria.setEditable(false);
                JCfacultad.setEnabled(false);
                //JTfacultad.setEditable(false);
                JTjefecatedra.setEditable(false);
                guardarmateria.setEnabled(false);
                //habilito los campos de las comisiones
                this.habilitarComision();
                tabladias.setEnabled(true);
                    //defino el titutlo de las comisiones
                        //defino un nuevo tamaño de fuente
                JLtcomision.setFont(new java.awt.Font("Tahoma", 0, 22));
                JLtcomision.setText(JTnombremateria.getText());
                filamodificar=-1;
                idmateria=-1;
            }     
        }
        if(f.equals(nuevamateria)){
                //reinicio los compos de las materias
                JTjefecatedra.setAutocomplete(true);
                JTnombremateria.setText(null);
                //JTfacultad.setText(null);
                //JCfacultad.setSelectedIndex(0);
                JTjefecatedra.setText(null);
                //JTncomision.setText(null);
                JCncomision.setSelectedIndex(0);
                JCncomision.setEnabled(false);
                //JTorientacion.setText(null);
                JCtipo.setSelectedIndex(0);
                JTcomisionencargado.setText(null);
                JTbox.setText(null);
                //JThorainicio.setText(null);
                JChorainicio.setSelectedIndex(0);
                //JThorafin.setText(null);
                JChorafin.setSelectedIndex(0);
                JTcapacidad.setText(null);
                JTnombremateria.setEditable(true);
                //JTfacultad.setEditable(true);
                JCfacultad.setEnabled(true);
                JTjefecatedra.setEditable(true);
                guardarmateria.setEnabled(true);
                JLtcomision.setText("");
                this.deshabilitarComision();
                limpiarTablaDias();
                tabladias.setEnabled(false);
            }
        if(f.equals(borrarmateria)){//guardar materia en la tabla del panel

            int h =JOptionPane.showConfirmDialog(null, "Esta seguro de borrar "+JTnombremateria.getText()+"?", "Confirmar Borrar", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (h==0){
                JTjefecatedra.setAutocomplete(true);
                String materia = JTnombremateria.getText();
                String facultad = (String) JCfacultad.getSelectedItem();
                String jefecatedra = JTjefecatedra.getText();
                filamodificar = this.buscarMateriaTabla(materia,facultad,jefecatedra);
                    basedato = new Postgres();
                    basedato.estableceConexion();
                    idmateria = basedato.buscarMateriaId(materia,JCfacultad.getSelectedIndex(), jefecatedra);
                    ResultSet rs=null;
                    rs = basedato.obtenerComisionesMateria(idmateria);

                    try{
                        while (rs.next())
                        {
                            Postgres basedato2 = new Postgres();
                            basedato2.estableceConexion();
                            int idc = rs.getInt(1);
                            basedato2.borrarComisionComisionesxDia(idc);
                        }

                        } catch(Exception e)
                        {
                            System.out.println("Problema al imprimir la base de datos ");
                        }
                    basedato.borrarMateria(idmateria);
                    tmaterias.removeRow(filamodificar);
                        for (int i = 0; i < tablacomisiones.getRowCount(); i++) {
                        tcomisiones.removeRow(i);
                        i-=1;
                        }
                    basedato.cierraConexion();
                    filamodificar=-1;
                    idmateria=-1;
                    //reinicio los compos de las materias
                JTnombremateria.setText(null);
                //JTfacultad.setText(null);
                //JCfacultad.setSelectedIndex(0);
                JTjefecatedra.setText(null);
                //JTncomision.setText(null);
                JCncomision.setSelectedIndex(0);
                //JTorientacion.setText(null);
                JCtipo.setSelectedIndex(0);
                JTcomisionencargado.setText(null);
                JTbox.setText(null);
                //JThorainicio.setText(null);
                JChorainicio.setSelectedIndex(0);
                //JThorafin.setText(null);
                JChorafin.setSelectedIndex(0);
                JTcapacidad.setText(null);
                JTnombremateria.setEditable(true);
                //JTfacultad.setEditable(true);
                JCfacultad.setEnabled(true);
                JTjefecatedra.setEditable(true);
                guardarmateria.setEnabled(true);
                JLtcomision.setText("");
                this.deshabilitarComision();
                limpiarTablaDias();
                tabladias.setEnabled(false);
            }
        }
        if(f.equals(guardarcomision)){//guardar materia en la tabla del panel
            if((!JTbox.getText().equals(""))&&(!JTcomisionencargado.getText().equals(""))&&(!JTcapacidad.getText().equals(""))&&(verificarTablaDias()))
                {
                    //desabilito los elementos de la comision
                    this.deshabilitarComision2();
                    tabladias.setEnabled(false);
                    //recojo los datos de la materia para saber a cual debo incrementar la cantidad de comisiones
                    String materia = JTnombremateria.getText();
                    String facultad = (String) JCfacultad.getSelectedItem();
                    String jefecatedra = JTjefecatedra.getText();
                    //recojo los datos de la comision;
                    String tipo = (String) JCtipo.getSelectedItem();
                    //String ncomision = JTncomision.getText();
                    String ncomision = (String) JCncomision.getSelectedItem();
                    String box = JTbox.getText();
                    String responsable = JTcomisionencargado.getText();
                    String capacidad = JTcapacidad.getText();
                    
                    //busco la fila de la tabla materia a la que tengo que incrementar la cantidad de comisiones
                    int fb=this.buscarMateriaTabla(materia, facultad, jefecatedra);
                    basedato = new Postgres();
                    basedato.estableceConexion();
                    int idm = basedato.buscarMateriaId(materia,JCfacultad.getSelectedIndex(),jefecatedra);
                    basedato.cierraConexion();
                    //agrego la comision
                    tcomisiones.addRow(new Object[]{ncomision,tipo,responsable,box,capacidad});
                    //modifico la cantidad
                    tmaterias.setValueAt(tcomisiones.getRowCount(), fb, 3);
                    basedato = new Postgres();
                    basedato.estableceConexion();
                    int icapacidad = Integer.parseInt(capacidad);
                    
                    int incomision = Integer.parseInt(ncomision);
                    basedato.cargarTablaComisiones(idm,incomision,tipo,responsable,box,icapacidad);
                    basedato.cierraConexion();

                    basedato = new Postgres();
                    basedato.estableceConexion();
                    int idc = basedato.buscarComisionId(idm, incomision, tipo, responsable, box, icapacidad);
                    for (int i = 0; i < tabladias.getRowCount(); i++) {
                        if(tdias.getValueAt(i, 0).toString().equals("true")){
                              String hi = tdias.getValueAt(i,2).toString();
                              int hii = Integer.parseInt(hi);
                              String hf = tdias.getValueAt(i,3).toString();
                              int hfi = Integer.parseInt(hf);
                              basedato.cargarTablaComisionesxDia(i+1, idc, hii, hfi);
                        }
                    }
                    basedato.cierraConexion();
                }
            else{
                JOptionPane.showMessageDialog(this,
                    "No se pudo realizar la operacion. Existe un Campo vacio",
                        "FALLO",JOptionPane.WARNING_MESSAGE);
            }
        }
        if(f.equals(modificarcomision)){//modificarcomsion
            if(!JTcomisionencargado.isEnabled()){//con esto pregunto si el JTextfield no esta seleccionado
                JTcomisionencargado.setAutocomplete(true);
                    String tipo = (String) JCtipo.getSelectedItem();
                    //String ncomision = JTncomision.getText();
                    String ncomision =(String) JCncomision.getSelectedItem();
                    String box = JTbox.getText();
                    String responsable = JTcomisionencargado.getText();
                    String capacidad = JTcapacidad.getText();
                    
                filamodificarcomision = this.buscarComisionTabla(ncomision,tipo,responsable,box,capacidad);

                basedato = new Postgres();
                   basedato.estableceConexion();
                    int idm = basedato.buscarMateriaId(JTnombremateria.getText(),JCfacultad.getSelectedIndex(),JTjefecatedra.getText());
                    //int idm,int ncomision, String tipo, String responsable, String box, int capacidad

                    idcomision = basedato.buscarComisionId(idm,Integer.parseInt(ncomision),tipo,responsable,box,Integer.parseInt(capacidad));
                    basedato.cierraConexion();

                //JLnombremateria.setText(Integer.toString(filamodificar));
                this.habilitarComision();
                tabladias.setEnabled(true);
                guardarcomision.setEnabled(false);
                nuevacomision.setEnabled(false);
                borrarcomision.setEnabled(false);
                nuevamateria.setEnabled(false);
                borrarmateria.setEnabled(false);
                modificarmateria.setEnabled(false);
                modificarcomision.setEnabled(true);

            }else{

                //modifico las celdas
                tcomisiones.setValueAt((String) JCncomision.getSelectedItem(), filamodificarcomision, 0);
                tcomisiones.setValueAt((String) JCtipo.getSelectedItem(), filamodificarcomision, 1);
                tcomisiones.setValueAt(JTcomisionencargado.getText(), filamodificarcomision, 2);
                tcomisiones.setValueAt(JTbox.getText(), filamodificarcomision, 3);
                tcomisiones.setValueAt(JTcapacidad.getText(), filamodificarcomision, 4);
                //oculto los campos de las comisiones
                this.deshabilitarComision();
                tabladias.setEnabled(false);
                    basedato = new Postgres();
                    basedato.estableceConexion();
                    basedato.borrarComsionesAsignacion(idcomision);
                    basedato.borrarComisionesDia(idcomision);
                    
                    basedato.moidificarComision(idcomision, Integer.parseInt((String) JCncomision.getSelectedItem()), (String) JCtipo.getSelectedItem(), JTcomisionencargado.getText(), JTbox.getText(), Integer.parseInt(JTcapacidad.getText()));
                   
                    for (int i = 0; i < tabladias.getRowCount(); i++) {
                        if(tdias.getValueAt(i, 0).toString().equals("true")){
                              String hi = tdias.getValueAt(i,2).toString();
                              int hii = Integer.parseInt(hi);
                              String hf = tdias.getValueAt(i,3).toString();
                              int hfi = Integer.parseInt(hf);
                              basedato.cargarTablaComisionesxDia(i+1, idcomision, hii, hfi);
                        }
                    }
                    basedato.cierraConexion();
                guardarcomision.setEnabled(false);
                nuevacomision.setEnabled(true);
                modificarcomision.setEnabled(true);
                nuevamateria.setEnabled(true);
                modificarmateria.setEnabled(true);
                borrarcomision.setEnabled(true);
                borrarmateria.setEnabled(true);
                filamodificarcomision=-1;
                idcomision=-1;
            }
        }
        if(f.equals(nuevacomision)){//modificarcomsion
                //JTncomision.setText(null);
            JTcomisionencargado.setAutocomplete(true);
                JCncomision.setSelectedIndex(tcomisiones.getRowCount());
                //JTorientacion.setText(null);
                JTcomisionencargado.setText(null);
                JTbox.setText(null);
                JChorainicio.setSelectedIndex(0);
                JChorafin.setSelectedIndex(0);
                JTcapacidad.setText(null);
                this.habilitarComision();

                tabladias.setEnabled(true);
                //limpio la tabla dias
                    limpiarTablaDias();
                //agrego filas
                
                guardarmateria.setEnabled(false);
                nuevacomision.setEnabled(false);
                modificarcomision.setEnabled(false);
        }
        if(f.equals(borrarcomision)){//guardar materia en la tabla del panel
            int h =JOptionPane.showConfirmDialog(null, "Esta seguro de borrar una comsion de "+JTnombremateria.getText()+"?", "Confirmar Borrar", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (h==0){
                    JTcomisionencargado.setAutocomplete(true);
                    String tipo = (String) JCtipo.getSelectedItem();
                    String ncomision =(String) JCncomision.getSelectedItem();
                    String box = JTbox.getText();
                    String responsable = JTcomisionencargado.getText();
                    String capacidad = JTcapacidad.getText();
                    filamodificarcomision = this.buscarComisionTabla(ncomision,tipo,responsable,box,capacidad);
                    basedato = new Postgres();
                    basedato.estableceConexion();
                    int idm = basedato.buscarMateriaId(JTnombremateria.getText(),JCfacultad.getSelectedIndex(),JTjefecatedra.getText());
                    idcomision = basedato.buscarComisionId(idm,Integer.parseInt(ncomision),tipo,responsable,box,Integer.parseInt(capacidad));
                    basedato.borrarComisionComisionesxDia(idcomision);
                    tcomisiones.removeRow(filamodificarcomision);
                    basedato.cierraConexion();
                    idcomision=-1;
                    filamodificarcomision=-1;
                String materia = JTnombremateria.getText();
                String facultad = (String) JCfacultad.getSelectedItem();
                String jefecatedra = JTjefecatedra.getText();
                int fb=this.buscarMateriaTabla(materia, facultad, jefecatedra);
                tmaterias.setValueAt(tcomisiones.getRowCount(), fb, 3);
                JCncomision.setSelectedIndex(tcomisiones.getRowCount());
                JTcomisionencargado.setText(null);
                JTbox.setText(null);
                JChorainicio.setSelectedIndex(0);
                JChorafin.setSelectedIndex(0);
                JTcapacidad.setText(null);
                this.habilitarComision();
                tabladias.setEnabled(true);
                limpiarTablaDias();
                guardarmateria.setEnabled(false);
                nuevacomision.setEnabled(false);
                modificarcomision.setEnabled(false);

            }
        }

    }
    public void deshabilitarComision(){
        borrarcomision.setEnabled(false);
       JLncomision.setEnabled(false);
       //JTncomision.setEnabled(false);
       JCncomision.setEnabled(false);
       JLorientacion.setEnabled(false);
       JLcomisionencargado.setEnabled(false);
       JLbox.setEnabled(false);
       //JTorientacion.setEnabled(false);
       JCtipo.setEnabled(false);
       JTcomisionencargado.setEnabled(false);
       JTbox.setEnabled(false);
       JLcapacidad.setEnabled(false);
       JLhorainicio.setEnabled(false);
       JLhorafin.setEnabled(false);
       JTcapacidad.setEnabled(false);
       JChorainicio.setEnabled(false);
       JChorafin.setEnabled(false);
       guardarcomision.setEnabled(false);
       nuevacomision.setEnabled(false);
       modificarcomision.setEnabled(false);
       //desactivar elementos de las materias
       modificarmateria.setEnabled(false);
       nuevamateria.setEnabled(false);
       borrarmateria.setEnabled(false);
    }
    public void deshabilitarComision2(){
       JLncomision.setEnabled(false);
       //JTncomision.setEnabled(false);
       JCncomision.setEnabled(false);
       JLorientacion.setEnabled(false);
       JLcomisionencargado.setEnabled(false);
       JLbox.setEnabled(false);
       //JTorientacion.setEnabled(false);
       JCtipo.setEnabled(false);
       JTcomisionencargado.setEnabled(false);
       JTbox.setEnabled(false);
       JLcapacidad.setEnabled(false);
       JLhorainicio.setEnabled(false);
       JLhorafin.setEnabled(false);
       JTcapacidad.setEnabled(false);
       JChorainicio.setEnabled(false);
       JChorafin.setEnabled(false);
       guardarcomision.setEnabled(false);
       nuevacomision.setEnabled(true);
       modificarcomision.setEnabled(true);
       //desactivar elementos de las materias
       //modificarmateria.setEnabled(false);
       //nuevamateria.setEnabled(false);
    }

    public void habilitarComision(){
       JLncomision.setEnabled(true);
       //JTncomision.setEnabled(true);
       JCncomision.setEnabled(false);
       JLorientacion.setEnabled(true);
       JLcomisionencargado.setEnabled(true);
       JLbox.setEnabled(true);
       //JTorientacion.setEnabled(true);
       JCtipo.setEnabled(true);
       JTcomisionencargado.setEnabled(true);
       JTbox.setEnabled(true);
       JLcapacidad.setEnabled(true);
       JLhorainicio.setEnabled(true);
       JLhorafin.setEnabled(true);
       JTcapacidad.setEnabled(true);
       JChorainicio.setEnabled(true);
       JChorafin.setEnabled(true);
       guardarcomision.setEnabled(true);
       //nuevacomision.setEnabled(true);
       //modificarcomision.setEnabled(true);
       //desactivar elementos de las materias
       modificarmateria.setEnabled(true);
       nuevamateria.setEnabled(true);
       borrarmateria.setEnabled(true);
    }
    public void limpiarComision(){
                //JTncomision.setText(null);
                //JCncomision.setSelectedIndex(0);
                JCncomision.setSelectedIndex(tcomisiones.getRowCount());
                //JTorientacion.setText(null);
                JCtipo.setSelectedIndex(0);
                JTcomisionencargado.setText(null);
                JTbox.setText(null);
                JChorainicio.setSelectedIndex(0);
                JChorafin.setSelectedIndex(0);
                JTcapacidad.setText(null);
                this.habilitarComision();
                tabladias.setEnabled(true);
                guardarmateria.setEnabled(false);
                nuevacomision.setEnabled(false);
                modificarcomision.setEnabled(false);
    }
    public int buscarMateriaTabla(String materia,String facultad,String jefecatedra){
        String bmateria;
        String bfacultad;
        String bjefecatedra;
        int f = tmaterias.getRowCount();
        int c = tmaterias.getColumnCount();
        int bf1=-1;
          for(int i=0; i<f; i++) //recorro las filas
            {
             bmateria=tmaterias.getValueAt(i, 0).toString();
             bfacultad=tmaterias.getValueAt(i, 1).toString();
             bjefecatedra=tmaterias.getValueAt(i, 2).toString();
             if((bmateria.equals(materia))&&(bfacultad.equals(facultad))&&(bjefecatedra.equals(jefecatedra))){
                 bf1=i;
             }
            }
         return bf1;
    }
    public int buscarComisionTabla(String ncomision,String tipo,String responsable,String box,String capacidad){
        String bncomision;
        String btipo;
        String bresponsable;
        String bbox;
        String bcapacidad;
        String bhorainicio;
        String bhorafin;
        int f = tcomisiones.getRowCount();
        int c = tcomisiones.getColumnCount();
        int bf1=-1;
          for(int i=0; i<f; i++) //recorro las filas
            {
             bncomision=tcomisiones.getValueAt(i, 0).toString();
             btipo=tcomisiones.getValueAt(i, 1).toString();
             bresponsable=tcomisiones.getValueAt(i, 2).toString();
             bbox=tcomisiones.getValueAt(i, 3).toString();
             bcapacidad=tcomisiones.getValueAt(i, 4).toString();
             if((bncomision.equals(ncomision))&&(btipo.equals(tipo))&&(bresponsable.equals(responsable))&&(bbox.equals(box))&&(bcapacidad.equals(capacidad))){
                 bf1=i;
             }
            }
         return bf1;
    }
    public void cargarTablaMaterias(){
        basedato = new Postgres();
        basedato.estableceConexion();
        ResultSet rs = null;
        
        rs = basedato.obtenerTabla("materias");
        int id;
        String materia;
        int facultad;
        String sfacultad;
        String jefecatedra;
        Postgres basedato2;
        int cant = 0;
        try{
        while (rs.next())
        {
            id = rs.getInt(1);
            materia = rs.getString (2);
            jefecatedra = rs.getString(3);
            facultad = rs.getInt(4);

            sfacultad=null;

            ResultSet rs2 = null;
            basedato2 = new Postgres();
            basedato2.estableceConexion();
            rs2=basedato2.obtenerFacultad(facultad);
                        try{
                        while (rs2.next())
                        {
                            sfacultad = rs2.getString(1);
                        }

                        } catch(Exception e)
                        {
                            System.out.println("Problema al obtener facultad");
                        }
            cant=basedato2.cantidadComisiones(id);
            tmaterias.addRow(new Object[]{materia,sfacultad,jefecatedra,cant});
        }

        } catch(Exception e)
        {
            System.out.println("Problema al imprimir la base de datos ");
        }
        basedato.cierraConexion();
    }

    public void cargarTablaComisiones(int id){
        basedato = new Postgres();
        basedato.estableceConexion();
        ResultSet rs = null;

        rs = basedato.obtenerComisionesMateria(id);
        //tablacomisiones.removeAll();
                    String tipo;
                    String ncomision;
                    String box;
                    String responsable;
                    String capacidad;
                    String horainicio;
                    String horafin;
        try{
        while (rs.next())
        {
            ncomision = rs.getString (2);
            responsable = rs.getString (3);
            box = rs.getString(4);
            tipo = rs.getString(5);
            capacidad = rs.getString(7);

            tcomisiones.addRow(new Object[]{ncomision,tipo,responsable,box,capacidad});
        }

        } catch(Exception e)
        {
            System.out.println("Problema al cargar la tabla comisiones ");
        }
        basedato.cierraConexion();

    }
    
    public void ocultarBarraTitulo()
    {
        Barra = ((javax.swing.plaf.basic.BasicInternalFrameUI) getUI()).getNorthPane();
        dimBarra = Barra.getPreferredSize();
        Barra.setSize(0,0);
        Barra.setPreferredSize(new Dimension(0,0));
        repaint();
    }
    public  boolean verificarTablaDias(){
        boolean b=false;
        for (int i = 0; i < tabladias.getRowCount(); i++) {
                        if(tdias.getValueAt(i, 0).toString().equals("true")){
                         
                              
                                    b=true;
                                
                            
                        }
                    }
        return b;
    }
    public void limpiarTablaDias(){
        for (int i = 0; i < tabladias.getRowCount(); i++) {
                        tdias.setValueAt(false , i, 0);
                        tdias.setValueAt("", i, 2);
                        tdias.setValueAt("", i, 3);
                    }
    }
    public void cargarDiasComisiones(int idc){
        ResultSet rs = null;
        basedato = new Postgres();
        basedato.estableceConexion();


        rs = basedato.obtenerDiasComision(idc);
        //tablacomisiones.removeAll();
                    int dia;
                    int hi;
                    int hf;
        try{
        while (rs.next())
        {
            dia = 0;
            hi = 0;
            hf = 0;
            dia = rs.getInt(2);
            hi = rs.getInt(4);
            hf = rs.getInt(5);
            if(dia!=0){
                tdias.setValueAt(true,(dia-1),0);
                tdias.setValueAt(hi,(dia-1),2);
                tdias.setValueAt(hf,(dia-1),3);
            }
        }

        } catch(Exception e)
        {
            System.out.println("Problema al cargar la tabla comisiones "+idc);
        }
        basedato.cierraConexion();
    }
    public void cargarAutocompletadoMateria(){
        JTjefecatedra = new GTextField(0  , 0 , true );
	basedato = new Postgres();
        basedato.estableceConexion();
        ResultSet rs = null;
        rs = basedato.obtenerEncargadosMaterias();

        try{
        while (rs.next())
        {
            String encargado=rs.getString(1);
            JTjefecatedra.getDataList().add(encargado);
        }

        } catch(Exception e)
        {
            System.out.println("Problema al cargar el autocompletado ");
        }

        rs = basedato.obtenerEncargadosComisiones();

        try{
        while (rs.next())
        {
            String encargado=rs.getString(1);
            JTjefecatedra.getDataList().add(encargado);
        }

        } catch(Exception e)
        {
            System.out.println("Problema al cargar el autocompletado 2");
        }
        JTjefecatedra.setWidthPopupPanel(160);
	JTjefecatedra.setHeightPopupPanel(100);
        basedato.cierraConexion();
    }
    public void cargarAutocompletadoComision(){
        JTcomisionencargado = new GTextField(0  , 0 , true );
	basedato = new Postgres();
        basedato.estableceConexion();
        ResultSet rs = null;
        rs = basedato.obtenerEncargadosMaterias();

        try{
        while (rs.next())
        {
            String encargado=rs.getString(1);
            JTcomisionencargado.getDataList().add(encargado);
        }

        } catch(Exception e)
        {
            System.out.println("Problema al cargar el autocompletado ");
        }

        rs = basedato.obtenerEncargadosComisiones();

        try{
        while (rs.next())
        {
            String encargado=rs.getString(1);
            JTcomisionencargado.getDataList().add(encargado);
        }

        } catch(Exception e)
        {
            System.out.println("Problema al cargar el autocompletado 2");
        }
        JTcomisionencargado.setWidthPopupPanel(160);
	JTcomisionencargado.setHeightPopupPanel(100);
        basedato.cierraConexion();
    }
    /*
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new AgregarMaterias();
            }
        });
        
    }*/

}
