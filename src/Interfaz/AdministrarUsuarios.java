package Interfaz;

import Estructura.Postgres;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class AdministrarUsuarios extends JInternalFrame implements ActionListener{

    //atributos de una tabla
    private JTable tablaUsuarios;
    private JScrollPane scrlptUsuarios;//scroll tabla
    private DefaultTableModel tUsuarios;
    private final String [] colsUsuarios = {"Nombre","Login","Mail","Cel","Tel","Tipo","Estado"};
    
    //atributos para ocultar marco
    private JComponent Barra = ((javax.swing.plaf.basic.BasicInternalFrameUI) getUI()).getNorthPane();
    private Dimension dimBarra = null;
    //base dato, buscar como utilizar solo una coneccion
    private Postgres basedato;
    //
    private int tipousuario;
    private String usuario;
    //atributos gestion datos de usuarios
    private JLabel JLnombreusuario,JLmail,JLtipousuario,JLestado,JLtel,JLcel,JLtitulocontraseña,JLlogin,JLcontraseña;
    private JTextField JTnombreusuario,JTmail,JTlogin,JTcel,JTtel;
    private JPasswordField JTcontraseña;
    private JComboBox JCtipousuario,JCestado;
    private JPanel JPgestionusuario;
    private JButton guardarusuario,modificarusuario,borrarusuario,modificarcontraseña,nuevousuario;
    //
    private String login;
    //variable control de estado
    private int control=0;   
    //fila de la tabla a modificar
    private int filamodificar=-1;

    public AdministrarUsuarios(Integer tipousuario,String usuario) {
        super("Administrar", false, // resizable
                    false, // closable
                    false, // maximizable
                    false);
        this.tipousuario=tipousuario;
        this.usuario=usuario;
        this.getContentPane().setLayout(null);//limpia la organizacion de los panel
        this.setBorder(null);//eliminar borde
        this.ocultarBarraTitulo();//eliminar barra de titulo
        this.getContentPane().add(this.pnlTablaUsuarios());
        this.getContentPane().add(this.pnlGestionUsuarios());
        this.cargarTablaUsuarios();
    }
    public JPanel pnlTablaUsuarios(){
        tUsuarios = new DefaultTableModel();
        tUsuarios.setColumnIdentifiers(colsUsuarios);
        tablaUsuarios = new JTable(tUsuarios){
        public boolean isCellEditable(int rowIndex, int vColIndex) {
            return false;
        }};//CON ESTO DESHABILITO LA EDICION MANUAL DE LA TABLA SOLO SE LA PUEDE EDITAR DESDE EL FORMUALRIO
        tablaUsuarios.setRowSorter(new TableRowSorter(tUsuarios));
        scrlptUsuarios = new JScrollPane(tablaUsuarios);
        scrlptUsuarios.setBounds(10,20,730,300);
        //centra los datos de la tabla
            DefaultTableCellRenderer modelocentrar = new DefaultTableCellRenderer();
            modelocentrar.setHorizontalAlignment(SwingConstants.CENTER); tablaUsuarios.getColumnModel().getColumn(0).setCellRenderer(modelocentrar);
            modelocentrar.setHorizontalAlignment(SwingConstants.CENTER); tablaUsuarios.getColumnModel().getColumn(1).setCellRenderer(modelocentrar);
            modelocentrar.setHorizontalAlignment(SwingConstants.CENTER); tablaUsuarios.getColumnModel().getColumn(2).setCellRenderer(modelocentrar);
            modelocentrar.setHorizontalAlignment(SwingConstants.CENTER); tablaUsuarios.getColumnModel().getColumn(3).setCellRenderer(modelocentrar);
        //INTENTANDO AGREGAR EVENTOS A LA TABLA
        setEventoMouseClicked(tablaUsuarios);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        JPanel pnl = new JPanel();
        pnl.setLayout(null);
        pnl.setBounds(600, 5, 750, 330);
        pnl.setBorder(BorderFactory.createTitledBorder("Usuarios"));
        pnl.setOpaque(false);
        tablaUsuarios.getColumnModel().getColumn(1).setPreferredWidth(0);
        tablaUsuarios.getColumnModel().getColumn(3).setPreferredWidth(0);
        tablaUsuarios.getColumnModel().getColumn(4).setPreferredWidth(0);
        tablaUsuarios.getColumnModel().getColumn(5).setPreferredWidth(0);
        tablaUsuarios.getColumnModel().getColumn(6).setPreferredWidth(0);
        pnl.add(scrlptUsuarios);
        return pnl;
    }

    private void setEventoMouseClicked(JTable tablaUsuarios) {
        tablaUsuarios.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            tblEjemploMouseClicked(e);
            }
        });
    }
   private void tblEjemploMouseClicked(java.awt.event.MouseEvent evt) {
       this.limpiarCampos();
       this.desactivarElementosPanel(1);
        int row = tablaUsuarios.rowAtPoint(evt.getPoint());
        filamodificar = row;
        if (row >= 0 && tablaUsuarios.isEnabled())
        {
            String tipo = tUsuarios.getValueAt(row,5).toString();
               JTnombreusuario.setText(tUsuarios.getValueAt(row,0).toString());
               JTmail.setText(tUsuarios.getValueAt(row,2).toString());
               if (tUsuarios.getValueAt(row,3)!=null){//valido que la tabla no tenga datos nulos
                   JTcel.setText(tUsuarios.getValueAt(row,3).toString());
               }
               if (tUsuarios.getValueAt(row,4)!=null){
                   JTtel.setText(tUsuarios.getValueAt(row,4).toString());
               }
               JCtipousuario.setSelectedItem(tipo);
               JCestado.setSelectedItem(tUsuarios.getValueAt(row,6).toString());
               login=tUsuarios.getValueAt(row,1).toString();
               JTlogin.setText(login);
               JTcontraseña.setText("1234");
        }
    }
    public void cargarTablaUsuarios(){
        basedato = new Postgres();
        basedato.estableceConexion();
        ResultSet rs = null;        
        rs = basedato.obtenerUsuarios();        
        try{
        while (rs.next())
        {
            int id;
            String nombre="";
            String mail="";
            String login="";
            String cel="";
            String tel="";
            String tipo_usuario="";
            String estado="";
            id = rs.getInt(1);
            nombre= rs.getString(2);
            login=rs.getString(3);
            mail=rs.getString(4);
            cel=rs.getString(5);
            tel=rs.getString(6);          
            tipo_usuario=rs.getString(7);
            estado=rs.getString(8);
            tUsuarios.addRow(new Object[]{nombre,login,mail,cel,tel,tipo_usuario,estado});
        }

        } catch(Exception e)
        {
            System.out.println("Problema al cargar los usuarios");
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
    public JPanel pnlGestionUsuarios(){
        //inicio los jlabel        
        JLnombreusuario = new JLabel("Nombre:");
        JLcel = new JLabel("Celular:");
        JLcontraseña = new JLabel("Contraseña:");
        JLestado = new JLabel("Estado:");
        JLlogin = new JLabel("Login:");
        JLmail = new JLabel("Mail:");
        JLtel = new JLabel("Telefono:");
        JLtipousuario = new JLabel("Tipo Usuario:");
        JLtitulocontraseña = new JLabel("Modificar Usuarios");
        //inicio los combobox
        JCtipousuario = new JComboBox(new String[] {"Usuario","Administrador","Super-Administrador"});
        JCestado = new JComboBox(new String[] {"Activo","Inactivo"});
        //inicio los textfield
        JTnombreusuario = new JTextField();
        
        JTcontraseña = new JPasswordField();
        
        JTlogin = new JTextField();
        JTmail = new JTextField();
        JTcel = new JTextField();
        JTtel = new JTextField();
        //inicio de botonos
        guardarusuario = new JButton("Guardar");
        modificarcontraseña = new JButton("Modificar Contraseña");
        modificarusuario = new JButton("Modificar");
        borrarusuario = new JButton("Borrar");
        nuevousuario = new JButton("Nuevo");
            //comentarios a los botones
            guardarusuario.setToolTipText("Guarda Usuario");
            modificarcontraseña.setToolTipText("Modificar Contraseña");            
            modificarusuario.setToolTipText("Modificar Usuario");            
            borrarusuario.setToolTipText("Borrar Usuario");            
            nuevousuario.setToolTipText("Nuevo Usuario");            
            //estamos pendientes de las acciones de los botones
            guardarusuario.addActionListener(this);
            modificarcontraseña.addActionListener(this);
            modificarusuario.addActionListener(this);
            borrarusuario.addActionListener(this);
            nuevousuario.addActionListener(this);
            //fondo de lo botones
            guardarusuario.setBackground(Color.white);
            modificarcontraseña.setBackground(Color.white);
            modificarusuario.setBackground(Color.white);
            borrarusuario.setBackground(Color.white);
            nuevousuario.setBackground(Color.white);
            //fondo de los jcombobox
            JCtipousuario.setBackground(Color.white);
            JCestado.setBackground(Color.white);
       //defino la posicion de los elementos
       int x = 50;//eje x
       int y = 30;//eje y
       int w = 60;//ancho
       int h = 30;//alto
       int i = 40;//incremento
       JLnombreusuario.setBounds(x, y, w, h);
       JTnombreusuario.setBounds(x+w, y, w*6, h);
       JLmail.setBounds(x, y+i, w, h);
       JTmail.setBounds(x+w, y+i, w*6, h);
       JLcel.setBounds(x, y+i*2, w, h);
       JTcel.setBounds(x+w, y+i*2, w*2, h);
       JLtel.setBounds(x+w*4, y+i*2, w*2, h);
       JTtel.setBounds(x+w*5, y+i*2, w*2, h);
       JLtipousuario.setBounds(x, y+i*3, w*2, h);
       JCtipousuario.setBounds(x+w+20, y+i*3, w*3, h);
       JLestado.setBounds(x+w*5, y+i*3, w*3, h);
       JCestado.setBounds(x+w*6, y+i*3, w*2, h);
       JLlogin.setBounds(x+20, y+i*4, w, h);
       JTlogin.setBounds(x+60, y+i*4, w*2, h);
       JLcontraseña.setBounds(x+w*3+10, y+i*4, w*2, h);
       JTcontraseña.setBounds(x+w*4+30, y+i*4, w*2, h);       
       guardarusuario.setBounds(x-30, y+i*5+20, w+30, h+10);
       nuevousuario.setBounds(x+65, y+i*5+20, w+30, h+10);
       modificarusuario.setBounds(x+160, y+i*5+20, w+30, h+10);
       modificarcontraseña.setBounds(x+255, y+i*5+20, w*2+30, h+10);
       borrarusuario.setBounds(x+410, y+i*5+20, w+30, h+10);
       //inicio panel materia y comision
       JPgestionusuario = new JPanel();
       JPgestionusuario.setLayout(null);
       JPgestionusuario.setBounds(5, 5, 590, 330);//posicion y tamaño del panel
       JPgestionusuario.setBorder(BorderFactory.createTitledBorder("Gestionar Usuarios"));
       JPgestionusuario.setOpaque(false);//defino el fondo
       //agrego los elementos al panel
       JPgestionusuario.add(JLnombreusuario);
       JPgestionusuario.add(JTnombreusuario);
       JPgestionusuario.add(JLmail);
       JPgestionusuario.add(JTmail);
       JPgestionusuario.add(JLcel);
       JPgestionusuario.add(JTcel);
       JPgestionusuario.add(JLtel);
       JPgestionusuario.add(JTtel);
       JPgestionusuario.add(JLtipousuario);
       JPgestionusuario.add(JCtipousuario);
       JPgestionusuario.add(JLestado);
       JPgestionusuario.add(JCestado);
       JPgestionusuario.add(JLlogin);
       JPgestionusuario.add(JTlogin);
       JPgestionusuario.add(JLcontraseña);
       JPgestionusuario.add(JTcontraseña);
       JPgestionusuario.add(guardarusuario);
       JPgestionusuario.add(modificarusuario);
       JPgestionusuario.add(modificarcontraseña);
       JPgestionusuario.add(borrarusuario);
       JPgestionusuario.add(nuevousuario);
       this.desactivarElementosPanel(2);
       
       return JPgestionusuario;
    }
    public void limpiarCampos(){
        JTnombreusuario.setText(null);
        JTmail.setText(null);
        JTtel.setText(null);
        JTcel.setText(null);
        JTlogin.setText(null);
        JTcontraseña.setText(null);
        JCtipousuario.setSelectedIndex(0);
        JCestado.setSelectedIndex(0);
    }
    public void desactivarElementosPanel(Integer c){
        if(c==1){
            JCestado.setEnabled(false);
            JCtipousuario.setEnabled(false);
            JTcel.setEditable(false);
            JTtel.setEditable(false);
            JTcontraseña.setEnabled(false);
            JTlogin.setEnabled(false);
            JTmail.setEditable(false);
            JTnombreusuario.setEditable(false);
            guardarusuario.setEnabled(false);
            borrarusuario.setEnabled(true);
            modificarcontraseña.setEnabled(false);
            nuevousuario.setEnabled(true);
            modificarusuario.setEnabled(true);
            control=1;
        }
        if(c==2){
            nuevousuario.setEnabled(false);
            modificarusuario.setEnabled(false);
            modificarcontraseña.setEnabled(false);  
            borrarusuario.setEnabled(false);
            control=2;
        }
        if(c==3){
            JCestado.setEnabled(true);
            JCtipousuario.setEnabled(true);
            JTcel.setEditable(true);
            JTtel.setEditable(true);
            JTcontraseña.setEnabled(true);
            JTlogin.setEnabled(true);
            JTmail.setEditable(true);
            JTnombreusuario.setEditable(true);
            JCestado.setSelectedIndex(0);
            JCtipousuario.setSelectedIndex(0);
            JTcel.setText(null);
            JTtel.setText(null);
            JTcontraseña.setText(null);
            JTlogin.setText(null);
            JTmail.setText(null);
            JTnombreusuario.setText(null);
            guardarusuario.setEnabled(true);
            modificarusuario.setEnabled(false);
            nuevousuario.setEnabled(false);
            modificarcontraseña.setEnabled(false);
            borrarusuario.setEnabled(false);
            control=3;
        }
        if(c==4){
            JCestado.setEnabled(true);
            JCtipousuario.setEnabled(true);
            JTcel.setEditable(true);
            JTtel.setEditable(true);
            JTcontraseña.setEnabled(true);
            JTlogin.setEnabled(true);
            JTmail.setEditable(true);
            JTnombreusuario.setEditable(true);
            JCestado.setSelectedIndex(0);
            JCtipousuario.setSelectedIndex(0);
            JTcel.setText(null);
            JTtel.setText(null);
            JTcontraseña.setText(null);
            JTlogin.setText(null);
            JTmail.setText(null);
            JTnombreusuario.setText(null);
            guardarusuario.setEnabled(true);
            modificarusuario.setEnabled(false);
            nuevousuario.setEnabled(false);
            modificarcontraseña.setEnabled(false);
            borrarusuario.setEnabled(false);
            control=4;
        }
        if(c==5){
            JCestado.setEnabled(true);
            JCtipousuario.setEnabled(true);
            JTcel.setEditable(true);
            JTtel.setEditable(true);
            JTcontraseña.setEnabled(false);
            JTlogin.setEnabled(false);
            JTmail.setEditable(true);
            JTnombreusuario.setEditable(true);
            guardarusuario.setEnabled(true);
            modificarusuario.setEnabled(false);
            nuevousuario.setEnabled(true);
            modificarcontraseña.setEnabled(true);
            borrarusuario.setEnabled(false);
            control=5;
        }
        if(c==6){
            modificarcontraseña.setEnabled(false);
            JTcontraseña.setEnabled(true);
            control=6;
        }
    }
    public void actionPerformed(ActionEvent g) {
        Object f = g.getSource();
        //controlar que todo los campos no nulos
        //controlar el login unico del nuevo usuario
        if(f.equals(guardarusuario))//guardar usuario en la tabla del panel
        {
            if(nuevousuario.isEnabled()){
                if((!JTnombreusuario.getText().equals(""))&&(!JTmail.getText().equals(""))&&(!JTcel.getText().equals(""))&&(!JTtel.getText().equals(""))&&(!JTlogin.getText().equals(""))&&(!JTcontraseña.getText().equals("")))
                    {
                        basedato = new Postgres();
                        basedato.estableceConexion();
                        String nombre = JTnombreusuario.getText();
                        String login = JTlogin.getText();
                        String contraseña = JTcontraseña.getText();
                        String mail = JTmail.getText();
                        String cel = JTcel.getText();
                        String tel = JTtel.getText();
                        int tipousuariotabla = JCtipousuario.getSelectedIndex();
                        String tipousuariolocal = (String)JCtipousuario.getSelectedItem();
                        int tu = 3;
                                switch ( tipousuariotabla ) {
                                    case 0:
                                     tu=3;
                                         break;
                                    case 1:
                                         tu=2;
                                         break;
                                    case 2:
                                         tu=1;
                                         break;
                                }
                        String estado = (String) JCestado.getSelectedItem();
                        if(!modificarcontraseña.isEnabled()){
                            basedato.actualizarUsuario(login,nombre,mail,cel,tel,contraseña,estado,tu);
                        }else{
                            basedato.actualizarUsuario(login,nombre,mail,cel,tel,estado,tu);                          
                        }
                        tUsuarios.setValueAt(nombre, filamodificar, 0);
                        tUsuarios.setValueAt(mail, filamodificar, 2);
                        tUsuarios.setValueAt(cel, filamodificar, 3);
                        tUsuarios.setValueAt(tel, filamodificar, 4);
                        tUsuarios.setValueAt(tipousuariolocal, filamodificar, 5);
                        tUsuarios.setValueAt(estado, filamodificar, 6);
                        basedato.cierraConexion();
                        this.desactivarElementosPanel(3);
                    }
                else{
                    JOptionPane.showMessageDialog(this,
                                    "No puede haber campos vacios!!!",
                                        "Error",JOptionPane.ERROR_MESSAGE);
                }
            }else{
                if((!JTnombreusuario.getText().equals(""))&&(!JTmail.getText().equals(""))&&(!JTcel.getText().equals(""))&&(!JTtel.getText().equals(""))&&(!JTlogin.getText().equals(""))&&(!JTcontraseña.getText().equals("")))
                    {
                        String nombre = JTnombreusuario.getText();
                        String login = JTlogin.getText();
                        String contraseña = JTcontraseña.getText();
                        String mail = JTmail.getText();
                        String cel = JTcel.getText();
                        String tel = JTtel.getText();
                        int tipousuariotabla = JCtipousuario.getSelectedIndex();
                        String tipousuariolocal = (String)JCtipousuario.getSelectedItem();
                        int tu = 3;
                        switch ( tipousuariotabla ) {
                            case 0:
                                 tu=3;
                                 break;
                            case 1:
                                 tu=2;
                                 break;
                            case 2:
                                 tu=1;
                                 break;
                        }
                        String estado = (String) JCestado.getSelectedItem();
                        basedato = new Postgres();
                        basedato.estableceConexion();                    
                        if(basedato.verificarLoginUsuario(login)== -1){
                                    basedato.cargarUsuario(nombre,mail,tel,cel,login,contraseña,estado,tu);
                                    tUsuarios.addRow(new Object[]{nombre,login,mail,cel,tel,tipousuariolocal,estado});                               
                            JOptionPane.showMessageDialog(this,
                                    "El Usuario se registro correctamente",
                                        "Exito",JOptionPane.INFORMATION_MESSAGE);
                            this.desactivarElementosPanel(3);
                        }else{
                            JOptionPane.showMessageDialog(this,
                                    "El login del nuevo usuario ya existe, por favor pruebe con otro!!!",
                                        "Error",JOptionPane.ERROR_MESSAGE);
                        }
                        basedato.cierraConexion(); 
                    }else{
                    JOptionPane.showMessageDialog(this,
                                    "Por favor completo todos los campos!!!",
                                        "Error",JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        if(f.equals(nuevousuario))//guardar usuario en la tabla del panel
        {
            this.desactivarElementosPanel(4);
        }
        if(f.equals(modificarusuario))//guardar usuario en la tabla del panel
        {
            String tipo = (String)JCtipousuario.getSelectedItem();
            if((tipousuario==1)&&((!tipo.equals("Super-Administrador"))||(usuario.equals(JTlogin.getText())))){
                this.desactivarElementosPanel(5);
            }else{
                if((tipo.equals("Usuario"))||(usuario.equals(JTlogin.getText()))){
                    this.desactivarElementosPanel(5);
                }else{
                JOptionPane.showMessageDialog(this,
                                    "Ud. no tiene los permisos necesario para modificar los datos de este usuario!!!",
                                        "Error",JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        if(f.equals(modificarcontraseña))//guardar usuario en la tabla del panel
        {
            this.desactivarElementosPanel(6);
        }
        if(f.equals(borrarusuario))//guardar usuario en la tabla del panel
        {
            String tipo = (String)JCtipousuario.getSelectedItem();
            if((tipousuario==1)&&(!tipo.equals("Super-Administrador"))){
                int h =JOptionPane.showConfirmDialog(null, "Esta seguro de borrar al usuario "+JTnombreusuario.getText()+"?", "Confirmar Borrar", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (h==0){
                    basedato = new Postgres();
                    basedato.estableceConexion(); 
                    basedato.eliminarUsuario(login);
                    basedato.cierraConexion();
                    tUsuarios.removeRow(filamodificar);
                    this.desactivarElementosPanel(3);
                }
            }else{
                if(tipo.equals("Usuario")){
                    int h =JOptionPane.showConfirmDialog(null, "Esta seguro de borrar al usuario "+JTnombreusuario.getText()+"?", "Confirmar Borrar", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                    if (h==0){
                        basedato = new Postgres();
                        basedato.estableceConexion(); 
                        basedato.eliminarUsuario(login);
                        basedato.cierraConexion();
                        tUsuarios.removeRow(filamodificar);
                        this.desactivarElementosPanel(3);
                    }
                }else{
                JOptionPane.showMessageDialog(this,
                                    "Ud. no tiene los permisos necesario para eliminar este usuario!!!",
                                        "Error",JOptionPane.ERROR_MESSAGE);
                }
            }                        
        }
    }
}
