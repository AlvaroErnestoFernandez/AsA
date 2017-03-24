package Interfaz;

import Estructura.Postgres;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.table.*;
//    //// cuando se busca hay que buscar en Jtable no en el DefoulTablaModelVer Despues
public class AgregarAulas extends JInternalFrame implements ActionListener, ChangeListener, ListSelectionListener{
    private JLabel JLaula,JLcapacida,JLzona, JLestado;
    private JTextField JTaula,JTcapacida;
    private JComboBox JCzona, JCestado;
    private JPanel JPaula;
    private JButton guardaraula,nuevaaula,modificaraula, borraraula;
    ////////
    private Postgres basedato;
   int idAula2;
    ////////
        //variables del panel tabla Aulas
    private JTable tablaaulas;
    private JScrollPane scrlpaula;//scroll tabla
    private DefaultTableModel taula;
    private final String [] colsaulas = {"Aula","Capacidad","Zona ","Estado"};
    private int filamodificar=-1;
    private    int idAula=-1;
    private TableRowSorter trsfiltro;
    //atributos para ocultar marco
    private JComponent Barra = ((javax.swing.plaf.basic.BasicInternalFrameUI) getUI()).getNorthPane();
    private Dimension dimBarra = null;
///////////////////////////////////////////////////////////////////////////////////////////////////////77

    public AgregarAulas() {
        super("Agregar Aulas", true, // resizable
                    true, // closable
                    true, // maximizable
                    true);// iconifiable
        this.getContentPane().setLayout(null);
        this.getContentPane().add(this.pnlAula());// CAMBIAR ESTO CUANDO IMPLEMENTE LOS PROCEDIMIENTOS
        this.getContentPane().add(this.pnlTablaAula());// ya lo cambie
        ////
        this.cargarTablaAulas();
                /////
        //this.getContentPane().setBackground(new Color(155,193,232));
        //elimino el borde
        this.setBorder(null);
        this.ocultarBarraTitulo();

        this.setSize(1200,600);
        this.setResizable(true);
        this.setVisible(true);
    }
public JPanel pnlAula(){
        //icio los jlabel
        JLaula = new JLabel("Nombre Aula");
        JLcapacida = new JLabel("Capacidad");
        JLzona = new JLabel("Zona");
        JLestado = new JLabel("Estado");//
         //inicio los textos
        JTaula = new JTextField();
        JTcapacida= new JTextField();
        JCzona = new JComboBox(new String[] {"Norte","Sur"});
//        JTzona = new JTextField();
//        JTestado= new JTextField();
        JCestado=new JComboBox(new String []{"Habilitada","Deshabilitada"});
                    //inicio de botonos
        guardaraula = new JButton("Guardar");
        nuevaaula = new JButton("Nueva");
        modificaraula = new JButton("Modificar");
        borraraula= new JButton("Borrar");
//comentarios a los botones
            guardaraula.setToolTipText("Guarda Aula");
            nuevaaula.setToolTipText("Nueva Aula");
            modificaraula.setToolTipText("Modificar Aula");
            borraraula.setToolTipText("Borrar Aula Seleccionada");
//estamos pendientes de las acciones de los botones
            guardaraula.addActionListener(this);
            nuevaaula.addActionListener(this);
            modificaraula.addActionListener(this);
            borraraula.addActionListener(this);
//establcer colores de fondo de lo botones
            guardaraula.setBackground(Color.white);
            nuevaaula.setBackground(Color.white);
            modificaraula.setBackground(Color.white);
            borraraula.setBackground(Color.white);

            JCzona.setBackground(Color.white);
            JCestado.setBackground(Color.white);
//defino la posicion de los elementos

       JLaula.setBounds(20, 30, 140, 30);
       JLcapacida.setBounds(20, 80, 60,30);
       JLzona.setBounds(170, 80, 60, 30);
       JLestado.setBounds(320,80 ,60,30);
//
       JCzona.setBounds(215,80,100,30);
        JCestado.setBounds(380,80,100,30);
       JTaula.setBounds(100,30,200, 30);
       JTcapacida.setBounds(90,80,60,30);
//posicion de los botones
       nuevaaula.setBounds(20,150,100,40);
       guardaraula.setBounds(160,150,100,40);
       modificaraula .setBounds(300,150,100,40);
       borraraula.setBounds(440,150,100,40);
       JPaula = new JPanel();
       JPaula.setLayout(null);
       JPaula.setBounds(5, 5, 600, 210);//posicion y tamaño del panel
       JPaula.setBorder(BorderFactory.createTitledBorder("Agregar Aula"));
       JPaula.setOpaque(false);//defino el fondo
       //agrego los elementos al panel de aula
       JPaula.add(JLaula);
       JPaula.add(JTaula);
       JPaula.add(JLcapacida);
       JPaula.add(JTcapacida);
       JPaula.add(JLzona);
       JPaula.add(JCzona);
       JPaula.add(JCestado);
       JPaula.add(JLestado);
       JPaula.add(nuevaaula);
       JPaula.add(guardaraula);
       JPaula.add(modificaraula);
       JPaula.add(borraraula);
       nuevaaula.setEnabled(false);
       modificaraula.setEnabled(false);
       borraraula.setEnabled(false);
    return JPaula;
    }
    private JPanel pnlTablaAula(){
        //taula es la tabla donde se iran mostrando los ingresos por defecto
        taula = new DefaultTableModel();
        taula.setColumnIdentifiers(colsaulas);
        tablaaulas = new JTable(taula){
          @Override
          public boolean isCellEditable(int rowIndex, int vColIndex) {
            return false;
           }};//CON ESTO DESHABILITO LA EDICION MANUAL DE LA TABLA SOLO SE LA PUEDE EDITAR DESDE EL FORMUALRIO
        tablaaulas.setRowSorter(new TableRowSorter(taula));
        scrlpaula= new JScrollPane(tablaaulas);
        scrlpaula.setBounds(10,20,900,425);
 //centra los datos de la tabla
            DefaultTableCellRenderer modelocentrar = new DefaultTableCellRenderer();
            modelocentrar.setHorizontalAlignment(SwingConstants.CENTER); tablaaulas.getColumnModel().getColumn(0).setCellRenderer(modelocentrar);
            modelocentrar.setHorizontalAlignment(SwingConstants.CENTER); tablaaulas.getColumnModel().getColumn(1).setCellRenderer(modelocentrar);
            modelocentrar.setHorizontalAlignment(SwingConstants.CENTER); tablaaulas.getColumnModel().getColumn(2).setCellRenderer(modelocentrar);
            modelocentrar.setHorizontalAlignment(SwingConstants.CENTER); tablaaulas.getColumnModel().getColumn(3).setCellRenderer(modelocentrar);
//INTENTANDO AGREGAR EVENTOS A LA TABLA
        setEventoMouseClicked(tablaaulas);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        JPanel pnl = new JPanel();
        pnl.setLayout(null);
        pnl.setBounds(5, 220, 920, 460);
        pnl.setBorder(BorderFactory.createTitledBorder("Tabla de Aulas Cargadas"));
        pnl.setOpaque(false);
        pnl.add(scrlpaula);
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
// es para cuando toco/hago click una linea de la tabla de Aulas
    private void tblEjemploMouseClicked(java.awt.event.MouseEvent evt) {
        int row = tablaaulas.rowAtPoint(evt.getPoint());
        if (row >= 0 && tablaaulas.isEnabled())
        {//captura los valores de estra en pantalla en los JTex y TCombo
               JTaula.setText(taula.getValueAt(row,0).toString());
               JTcapacida.setText(taula.getValueAt(row,1).toString());
               JCzona.setSelectedItem((taula.getValueAt(row,2).toString()).replace(" ", ""));
               JCestado.setSelectedItem((taula.getValueAt(row,3).toString()).replace(" ", ""));
         }
        //////////////////// no se si dejar visbles las casillas o no
      JTaula.setEditable(false);// el nombre ya esta definido no se debe cambiar

       JTcapacida.setEditable(false);
       JCzona.setEnabled(false);/// si lo cambio por Enabled
       JCestado.setEnabled(false);

//para que me quede directo la opcion de borrar o modificar la fila seccionada
        nuevaaula.setEnabled(true);
       modificaraula.setEnabled(true);// para cuando selecciones una fla este activo
       guardaraula.setEnabled(false);
       borraraula.setEnabled(true);// para cuando selecciones una fla este activo
/////////////////////////////////////////////
//       int a=Integer.parseInt(JTcapacida.getText());
  //     String b=(String)JCzona.getSelectedItem();

    }
 public void valueChanged(ListSelectionEvent e){
    }
 public void stateChanged(ChangeEvent e){
    }
    //trabajo con los botones
    public void actionPerformed(ActionEvent e){
        Object f = e.getSource();
        if(f.equals(guardaraula))//guardar aula en la tabla del panel
        {
            if((!JTaula.getText().equals(""))&&(!JTcapacida.getText().equals("")))
                {  //desabilito los elementos del Aula esto no se muy bien para que lo hago
                    JTaula.setEditable(false);
                    JTcapacida.setEditable(false);
                    JCzona.setEditable(false);
                    JCestado.setEditable(false);
  //recojo los datos de las AULAS
                    boolean estado2;
                    String aula = JTaula.getText();
                    int capacida = Integer.parseInt(JTcapacida.getText());
                    String zona = (String) JCzona.getSelectedItem();
                    String estado1=(String)JCestado.getSelectedItem() ;
                    if ("Habilitada".equals(estado1))
                        estado2= true;
                    else
                        estado2= false;
                         //agrego una fila ala tabla se la interfaz
                    taula.addRow(new Object[]{aula,capacida,zona,JCestado.getSelectedItem()});
            //////
                    basedato = new Postgres();
                    basedato.estableceConexion();
                    basedato.cargarTablaAulaPato(aula,capacida,zona,estado2);
                    basedato.cierraConexion();
       //habilito los otros botones
                    nuevaaula.setEnabled(true);
                    modificaraula.setEnabled(true);
                    borraraula.setEnabled(true);
                    guardaraula.setEnabled(false);
                   // this.limpiarCampos();
                }
            else{
                JOptionPane.showMessageDialog(this,
                    "No se pudo realizar la operacion. Existe un Campo vacio",
                        "Error",JOptionPane.WARNING_MESSAGE);
            }
        }
        if(f.equals(modificaraula)){//aun no estoy tan de convencido de dejar que modifique todos los datos
            if(!JTaula.isEditable()){//con esto pregunto si el JTextfield no esta seleccionado
      //tomo los valores de los JT
                String aula = JTaula.getText();//
                String capacida = JTcapacida.getText();
                String zona = (String)JCzona.getSelectedItem();
                String estado=(String)JCestado.getSelectedItem();
     // obtengo el num de fila en donde se encuentran en la tabla de aula
                filamodificar = this.buscarAulaTabla(aula,capacida,zona,estado);
                System.out.println(aula+" "+capacida+" "+zona+" "+estado);
                ////////////////////////////Para obtener id de aula
                int cap=Integer.parseInt(capacida);
                basedato = new Postgres();
                    basedato.estableceConexion();
                    idAula = basedato.buscarAulaId(aula,cap,zona);
                    basedato.cierraConexion();
/////////////////////////
                JTaula.setEditable(true);
                JTcapacida.setEditable(true);
                JCzona.setEnabled(true);
                JCestado.setEnabled(true);

                modificaraula.setEnabled(true);
                guardaraula.setEnabled(false);
                borraraula.setEnabled(false);
                nuevaaula.setEnabled(false);

            }else{
          ////agrego la fila modificada a la tabla de la interfaz
                if((!JTaula.getText().equals(""))&&(!JTcapacida.getText().equals("")))
                {
                System.out.println(idAula);
                System.out.println(filamodificar);
                taula.setValueAt(JTaula.getText(), filamodificar, 0);
                taula.setValueAt(JTcapacida.getText(), filamodificar, 1);
                taula.setValueAt((String) JCzona.getSelectedItem(), filamodificar, 2);
                taula.setValueAt((String)JCestado.getSelectedItem(), filamodificar,3);

                boolean estado2;
                    String aula = JTaula.getText();
                    int capacida = Integer.parseInt(JTcapacida.getText());
                    String zona = (String) JCzona.getSelectedItem();
                    String estado1=(String)JCestado.getSelectedItem() ;
                    if (estado1!="Habilitada")
                        estado2= false;
                    else
                        estado2= true;
                         ////debo modificar en la base de tados
                basedato= new Postgres();
                basedato.estableceConexion();
                basedato.moidificarAula(idAula, aula, capacida, estado2, zona);
                basedato.cierraConexion();

                 JTaula.setEditable(false);
                JTcapacida.setEditable(false);
                JCzona.setEnabled(false);
                JCestado.setEnabled(false);
                modificaraula.setEnabled(true);
                guardaraula.setEnabled(false);
                borraraula.setEnabled(true);
                nuevaaula.setEnabled(true);
                }else{
                JOptionPane.showMessageDialog(this,
                    "No se pudo realizar la operacion. Existe un Campo vacio",
                        "Error",JOptionPane.WARNING_MESSAGE);
                }
            }
        }
        if(f.equals(nuevaaula)){
                //reinicio los compos de las materias
                JTaula.setText(null);
                JTcapacida.setText(null);
                //JTzona.setText(null);
                JCzona.setSelectedIndex(0);
                JCestado.setSelectedItem(0);

                JTaula.setEditable(true);
                JTcapacida.setEditable(true);

                JCzona.setEnabled(true);

                //JTzona.setEditable(true);
                JCestado.setEnabled(true);
                guardaraula.setEnabled(true);
                //desabilito los botones
                nuevaaula.setEnabled(false);
                borraraula.setEnabled(false);
                modificaraula.setEnabled(false);
            }
        if(f.equals(borraraula)){
          int h =JOptionPane.showConfirmDialog(null, "Esta seguro de borrar el aula seleccionada?", "Confirmar Borrar", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (h==0){
          boolean estado2;
                    String aula = JTaula.getText();
                    int capacida = Integer.parseInt(JTcapacida.getText());
                    String zona = (String) JCzona.getSelectedItem();
                    String estado1=(String)JCestado.getSelectedItem() ;
                    if (estado1!="Habilitada")
                        estado2= false;
                    else
                        estado2= true;          
          //idAula = basedato.buscarAulaId(aula,capacida,zona);
          //System.out.println(idAula);
          basedato=new Postgres();
          basedato.estableceConexion();
          basedato.eliminarAula(aula);
          basedato.cierraConexion();
          DefaultTableModel dtm = (DefaultTableModel) tablaaulas.getModel(); //TableProducto es el nombre de mi tabla ;)
          dtm.removeRow(tablaaulas.getSelectedRow());
          this.limpiarCampos(); 
            }
        }
    }
    public void limpiarCampos(){
        //pongo los JT para que se muestren vacios
        JTaula.setText(null);
        JTcapacida.setText(null);

        JCzona.setSelectedIndex(0);
        JCestado.setSelectedIndex(0);
        //JTzona.setText(null);
        //JTestado.setText(null);
    }
    public int buscarAulaTabla(String aula,String capacida,String zona, String estado){
        String baula;
        String bcapacida;
        String bzona;
        String bestado;
        int f = taula.getRowCount();
        int c = taula.getColumnCount();
        int bf1=-1;
       //7 int i=0;
         for (int i= 0;i<f;i++) //recorro las filas
            {
             baula=(taula.getValueAt(i, 0).toString()).replace(" ", "");
             bcapacida=(taula.getValueAt(i, 1).toString()).replace(" ", "");
             bzona=(taula.getValueAt(i, 2).toString()).replace(" ", "");
             bestado=(taula.getValueAt(i, 3).toString()).replace(" ", "");
             if((baula.equals(aula.replace(" ", "")))&&(bcapacida.equals(capacida.replace(" ", "")))&&(bzona.equals(zona.replace(" ", "")))&&(bestado.equals(estado.replace(" ", "")))){     
                 bf1=i;
                }
         }
         
         return bf1;
    }
    public void ocultarBarraTitulo()
    {
        Barra = ((javax.swing.plaf.basic.BasicInternalFrameUI) getUI()).getNorthPane();
        dimBarra = Barra.getPreferredSize();
        Barra.setSize(0,0);
        Barra.setPreferredSize(new Dimension(0,0));
        repaint();
    }
    public void cargarTablaAulas(){//este es para cargar el JTable de aula Existentes en la tabla que muestra la interfaz
        basedato = new Postgres();
        basedato.estableceConexion();
        ResultSet rs = null;

        rs = basedato.obtenerTabla("aulas");
        int id;
        String aula, estadoTabla;
        int capacidad;
        String zona;
        boolean estado;

        try{
        while (rs.next())
        {
            id = rs.getInt(1);
            aula = rs.getString (2);
            capacidad = rs.getInt(3);
            estado = rs.getBoolean(4);
            zona = rs.getString(5);
            if (estado){
                estadoTabla="Habilitada";}
            else{
                estadoTabla="Deshabilitada";}
            taula.addRow(new Object[]{aula,capacidad,zona,estadoTabla});
         }

        } catch(SQLException e)
        {
            System.out.println("Problema al imprimir la base de datos ");
        }
        basedato.cierraConexion();
    }
}