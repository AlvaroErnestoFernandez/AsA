package Interfaz;
import Estructura.ObjetoEvento;
import Estructura.ObjetoMateria;
import Estructura.Postgres;
import Estructura.Excel;
import com.toedter.calendar.JCalendar;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Vector;
import javax.swing.table.*;

import java.awt.Image;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;

public final class ConsultaDia extends JInternalFrame implements ActionListener, ChangeListener, ListSelectionListener
{
    private JLabel JLfecha;
   //// JLaabel para mostrar los detalles
    private JLabel evento,tipoe,encargadoe,facue,cante,hie,hfe,fe, aulae;
    private JLabel ex,hu,ec,in,sa,na,ot;
    private JLabel materia,mfacu,jefecat,comis,jtp,box,tipcomi,mcant,him,hfm,mdias,tipom, aulam;
    private JTextField JTfecha,exa,hum,eco,ing,salu,natu,otr;
    private JPanel JPdias,JPdetalle;
    private JButton buscardia, imprimir,excel;
    private Image icono;
    //imagen en button
//variables del panel tabla Aulas
    private JTable JTabladia;
    private JScrollPane scrlpDia;//scroll tabla
    private DefaultTableModel DMTablaDia;
    private final String [] columtabladia = {"Aulas/hs.","8-9","9-10","10-11","11-12","12-13","13-14","14-15","15-16","16-17","17-18","18-19","19-20","20-21","21-22","22-23"};
    private int filamodificar=-1;
    private TableRowSorter trsfiltro;
//atributos para ocultar marco
    private JComponent Barra = ((javax.swing.plaf.basic.BasicInternalFrameUI) getUI()).getNorthPane();
    private Dimension dimBarra = null;
    //////////// cambio a calendario
    private JCalendar JCfecha;
    private Postgres basedato, basedato2;
///////////////////////////////////////////////////PARA PONER COLOR EN LAS CELDAS
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
    ///////////////////////////////////////////////PARA EL EVENTO DE POSICIONAR EL MOUSE
    private ObjetoEvento VecEvento [];
    private ObjetoMateria VecMateria[];
    private ObjetoEvento event;
    private ObjetoMateria mat;

    private Vector<ObjetoMateria> vecm;
    private Vector<ObjetoEvento> vece;

    private JScrollPane scrollasimanual;
    
    private Excel archivoExcel;

    public ConsultaDia()
        {
            exa=new JTextField();
            exa.setBounds(1100,5,30,30);
            exa.setEditable(false);
            exa.setBackground(Color.RED);
            ex= new JLabel("CS. EXACTAS");
            ex.setBounds(1132,5,120,30);
            ex.setFont(new java.awt.Font("Courier", Font.BOLD,13));
            hum= new JTextField();
            hum.setBounds(1100,40,30,30);
            hum.setEditable(false);
            hum.setBackground(Color.BLUE);
            hu= new JLabel("HUMANIDADES");
            hu.setBounds(1132,40,120,30);
            hu.setFont(new java.awt.Font("Courier", Font.BOLD,13));
            eco= new JTextField();
            eco.setBounds(1100,75,30,30);
            eco.setEditable(false);
            eco.setBackground(Color.MAGENTA);
            ec= new JLabel("CS. ECONOMICAS");
            ec.setBounds(1132,75,120,30);
            ec.setFont(new java.awt.Font("Courier", Font.BOLD,13));
            ing= new JTextField();
            ing.setBounds(1100,110,30,30);
            ing.setEditable(false);
            ing.setBackground(Color.GREEN);
            in= new JLabel("INGENIERIA");
            in.setBounds(1132,110,120,30);
            in.setFont(new java.awt.Font("Courier", Font.BOLD,13));
            salu= new JTextField();
            salu.setBounds(1100,145,30,30);
            salu.setEditable(false);
            salu.setBackground(Color.ORANGE);
            sa= new JLabel("CS. de la SALUD");
            sa.setBounds(1132,145,120,30);
            sa.setFont(new java.awt.Font("Courier", Font.BOLD,13));
            natu= new JTextField();
            natu.setBounds(1100,180,30,30);
            natu.setEditable(false);
            natu.setBackground(Color.CYAN);
            na= new JLabel("CS. NATURALES");
            na.setBounds(1132,180,120,30);
            na.setFont(new java.awt.Font("Courier", Font.BOLD,13));
            otr= new JTextField();
            otr.setBounds(1100,215,30,30);
            otr.setEditable(false);
            otr.setBackground(Color.YELLOW);
            ot= new JLabel("OTROS");
            ot.setBounds(1132,215,120,30);
            ot.setFont(new java.awt.Font("Courier", Font.BOLD,13));
            
            //boton excel
            excel= new JButton();
            excel.setIcon(new ImageIcon(getClass().getResource("../imagenes/excel.png")));
            excel.setToolTipText("Generar Reporte Excel");
            excel.addActionListener(this);
            excel.setBounds(1262, 255, 50, 50);
            
             this.getContentPane().setLayout(null);
             this.getContentPane().add(this.pnlDia());// CAMBIAR ESTO CUANDO IMPLEMENTE LOS PROCEDIMIENTOS
             this.getContentPane().add(this.pnlTablaDia());// ya lo cambie
             this.getContentPane().add(this.pnlCalendarioFecha());
             //this.getContentPane().add(this.pnlDetalle());
             scrollasimanual = new JScrollPane(this.pnlDetalle());
             scrollasimanual.setBounds(750,5,330,225);
             scrollasimanual.getViewport().setBackground(Color.WHITE);
             this.getContentPane().add(scrollasimanual);
             //this.getContentPane().setBackground(new Color(220,220,220));//155,193,232));
        //elimino el borde
             this.cargarAulas1();
             this.getContentPane().add(exa);
             this.getContentPane().add(hum);
             this.getContentPane().add(natu);
             this.getContentPane().add(eco);
             this.getContentPane().add(ing);
             this.getContentPane().add(salu);
             this.getContentPane().add(otr);
             /////
             this.getContentPane().add(ex);
             this.getContentPane().add(hu);
             this.getContentPane().add(na);
             this.getContentPane().add(ec);
             this.getContentPane().add(in);
             this.getContentPane().add(sa);
             this.getContentPane().add(ot);
             //this.getContentPane().add(excel);
             this.setBorder(null);
             this.setSize(1200,600);
             this.setResizable(true);
             this.agregarFechaAtexfield();
             this.setVisible(true);
         }
    public JPanel pnlCalendarioFecha(){
        JCfecha=new JCalendar();
        JCfecha.setVisible(true);
        JCfecha.setBackground(Color.white);

        JPanel c =new JPanel();
        c.setBounds(20,5, 400,290);
        //c.setBorder(BorderFactory.createTitledBorder("HASTA"));
        c.setOpaque(false);
        c.add(JCfecha);
        return c;
    }
    public JPanel pnlDia()
    {
        JCfecha=new JCalendar();
//icio los jlabel
        JLfecha = new JLabel("Fecha Seleccionada");
        JLfecha.setBounds(85, 28, 140, 40);
        JLfecha.setFont(new java.awt.Font("Courier", Font.BOLD,13));
//nicio JTextField
        JTfecha = new JTextField();
        JTfecha.setBounds(85,58,140,40);
        JTfecha.setEditable(false);
        JTfecha.setDisabledTextColor(Color.RED);
//inicio de botonos, comentarios, color
        buscardia = new JButton("Consultar");
        imprimir= new JButton("Imprimir");
        
        buscardia.setToolTipText("Buscar Dia");
        imprimir.setToolTipText("Imprimir");

        buscardia.addActionListener(this);
        imprimir.addActionListener(this);

        buscardia.setBackground(Color.white);
        imprimir.setBackground(Color.white);

        buscardia.setBounds(50,120,100,40);
        imprimir.setBounds(180,120,100,40);        
        
//inicio panel de Aulas.........................IMPORTANTE
        JPdias = new JPanel();
        JPdias.setLayout(null);
        JPdias.setBounds(420,5,320,225);//posicion y tamaño del panel
        JPdias.setBorder(BorderFactory.createTitledBorder("Consultar Distribucion de Aulas en una Fecha"));
        JPdias.setOpaque(false);//defino el fondo
//agrego los elementos al panel de aula
       JPdias.add(JTfecha);
       JPdias.add(JLfecha);
       JPdias.add(buscardia);
       JPdias.add(imprimir);
    return JPdias;
    }
     public JPanel pnlDetalle()
    {
//icio los jlabel
        evento=new JLabel("Evento / Materia");
         evento.setBounds(10,10,150, 40);
         tipoe=new JLabel();
         tipoe.setBounds(180,10,100, 40);
         encargadoe=new JLabel();
         encargadoe.setBounds(10,50,260, 40);
         facue=new JLabel();
         facue.setBounds(10,90,300, 40);
         cante=new JLabel();
         cante.setBounds(10,10,260, 40);
         hie=new JLabel();
         hie.setBounds(10,120,120, 40);
         hfe=new JLabel();
         hfe.setBounds(130,120,260, 40);
         fe=new JLabel();
         fe.setBounds(10,10,260, 40);
         //////////////////////////////////////////////////////////////
         materia=new JLabel();
         materia.setBounds(10,10,180,35);
         mfacu=new JLabel();
         mfacu.setBounds(10,60,260, 30);
         jefecat=new JLabel();
         jefecat.setBounds(30,35,260, 30);
         comis=new JLabel();
         comis.setBounds(10,110,100,30);
         jtp=new JLabel();
         jtp.setBounds(10,80,260, 30);
         box=new JLabel();
         box.setBounds(200,80,260, 30);
         tipcomi=new JLabel();
         tipcomi.setBounds(200,110,260, 30);
         mcant=new JLabel();
         mcant.setBounds(10,10,260, 30);
         him=new JLabel();
         him.setBounds(10,130,260, 30);
         hfm=new JLabel();
         hfm.setBounds(100,130,260, 30);
         mdias=new JLabel();
         mdias.setBounds(10,150,260, 30);
         tipom=new JLabel();
         tipom.setBounds(220,10,100, 30);
         aulae=new JLabel();
         aulae.setBounds(50,160,260, 30);
         aulam=new JLabel();
         aulam.setBounds(10,185,260, 30);
//inicio de botonos, comentarios, color
        JPdetalle = new JPanel();
        JPdetalle.setLayout(null);
        JPdetalle.setBounds(750,5,330,225);//posicion y tamaño del panel
        JPdetalle.setBorder(BorderFactory.createTitledBorder("Detalles de HORARIO"));
        JPdetalle.setOpaque(false);//defino el fondo
//agrego los elementos al panel de aula
        JPdetalle.add(evento);
        JPdetalle.add(tipoe);
        JPdetalle.add(encargadoe);
        JPdetalle.add(facue);
        JPdetalle.add(cante);
        JPdetalle.add(hie);
        JPdetalle.add(hfe);
        JPdetalle.add(fe);
        JPdetalle.add(aulae);
        JPdetalle.add(materia);
        JPdetalle.add(mfacu);
        JPdetalle.add(jefecat);
        JPdetalle.add(comis);
        JPdetalle.add(jtp);
        JPdetalle.add(box);
        JPdetalle.add(tipcomi);
        JPdetalle.add(mcant);
        JPdetalle.add(him);
        JPdetalle.add(hfm);
        JPdetalle.add(mdias);
        JPdetalle.add(tipom);
        JPdetalle.add(aulam);
    return JPdetalle;
    }
     private void agregarFechaAtexfield() {
        JCfecha.getDayChooser().addPropertyChangeListener(
                new java.beans.PropertyChangeListener() {

                    @Override
                    public void propertyChange(java.beans.PropertyChangeEvent evt) {
                        if (evt.getPropertyName().compareTo("day") == 0) {
                            SimpleDateFormat formatoDeFecha = new SimpleDateFormat("dd/MM/yyyy");
                            JTfecha.setText(formatoDeFecha.format(JCfecha.getDate()));
                        }else{
                            SimpleDateFormat formatoDeFecha = new SimpleDateFormat("dd/MM/yyyy");
                            JTfecha.setText(formatoDeFecha.format(JCfecha.getDate()));
                        }
                    }
                });
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
    private JPanel pnlTablaDia()
    {
        DMTablaDia = new DefaultTableModel(){
/////////////////////comienzo a probar para hacer el lateral isquierdo///Un modelo de datos que hace la primera columna la de la cabecera lateral no editable.
               @Override
 		public boolean isCellEditable(int row, int column) {
			if (0 == column)
			   return false;
			return super.isCellEditable(row, column);
			}
        };
        // Titulos para la cabecera superior. El primero es Aulas despues siguen los horarios
        DMTablaDia.setColumnIdentifiers(columtabladia);
// JTable al que se le pasa el modelo recien creado y se le sobreescribe el metodo changeSelection para que no permita seleccionar la primera columna.
        JTabladia= new JTable(DMTablaDia){
          @Override
          public void changeSelection(int rowIndex, int columnIndex,
					boolean toggle, boolean extend) {
				if (columnIndex == 0)
					super.changeSelection(rowIndex, columnIndex + 1, toggle,extend);
				else
					super.changeSelection(rowIndex, columnIndex, toggle,extend);
			}
          @Override
          public boolean isCellEditable(int rowIndex, int vColIndex) {
            return false;   //CON ESTO DESHABILITO LA EDICION MANUAL DE LA TABLA
         }
        };//
//Se pone a la primera columna el render del JTableHeader superior.
         JTabladia.getColumnModel().getColumn(0).setCellRenderer(
	 JTabladia.getTableHeader().getDefaultRenderer());
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
       setEventoMouseClicked(JTabladia);
            ///////////////////// estableciendo el alto
        scrlpDia = new JScrollPane(JTabladia);
        JTabladia.setRowHeight(60);
        scrlpDia.setBounds(10,20,1220,455);
        //AQUI SE CENTRARIA LOS DTOS PERO LO VOY A VER DESPUS???
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        JPanel pnl = new JPanel();
        pnl.setLayout(null);
        pnl.setBounds(10, 240, 1240, 485);
        pnl.setBorder(BorderFactory.createTitledBorder("Tabla de ASIGANACIONES DEL DIA"));
        pnl.setOpaque(false);
        pnl.add(scrlpDia);
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
///////////////////////////// es para cuando toco/hago click una linea de la tabla de Aulas
    private void tblEjemploMouseClicked(java.awt.event.MouseEvent evt) {
        basedato=new Postgres();
        basedato.estableceConexion();
        ResultSet rsm=null;
        ResultSet rse=null;

        int row = JTabladia.rowAtPoint(evt.getPoint());
        int fila =JTabladia.getSelectedRow();//columnAtPoint(evt.getPoint());
        int columna=JTabladia.getSelectedColumn();
          //////////////////////////////////////////////////////////////////
       if (row >= 0 && JTabladia.isEnabled())
        {//captura los valores de estaran en pantalla en el detalle
            String valor=JTabladia.getValueAt(fila,columna).toString();
            if(!valor.equals("")){   ///////////////////////////////////////////////////////////////////////////////////////////
                int e=BuscarEvento(fila,columna);
                System.out.println("este es el id del evento:"+e);
                if (e>-1){////////////////////////////////////////////////////////////////////////////////////////////////////////
                   this.OcultarMateria();
                   this.DesocultarEvento();
                   rse=basedato.consultaDatosEvento(e);
                   try{
                     while (rse.next())
                       {
                         String eve= rse.getString (1);
                          String tipeve=rse.getString(2);
                          String encargado=rse.getString(3);


                          String facu="Otra";
                          Integer mid=rse.getInt(4);
                          if(mid!=null){
                              facu=basedato.obtenerFacultadNombre(mid);
                          }

                          
                          int cant=rse.getInt(5);
                          String hsin=rse.getString(6);
                          String hsf=rse.getString(7);
                          String fech=rse.getString(8);
                          String aula=(String)JTabladia.getValueAt(fila,0);
/////////////////////////////////
                          evento.setText("Evento: "+eve);
                          tipoe.setText("Tipo: "+tipeve);
                          encargadoe.setText("Responsable: "+encargado);
                          facue.setText("Facultad o Dependencia: "+facu);
                          //cante.setText("Total ");
                         hie.setText("Hora Inicio: "+hsin);
                         hfe.setText("Hora Fin: "+hsf);
                         aulae.setText("Aula: "+aula);
                          validate();
                     }
                 }
              catch(SQLException ee)
                {
                  System.out.println("Problema al imprimir la base de datos 322");
                }
              }
            else{
                System.out.println("fila:"+ fila);
                System.out.println("columna"+columna);
                  int c=BuscarMateria(fila,columna);
                  System.out.println("este es el id de la comision"+c);
                  this.OcultarEvento();
                  this.DesocultarMateria();
                  rsm=basedato.consultaDatosComision(c);
                  boolean band=true;
                  String dia="";
                  try{
                     while (rsm.next())
                       {
                        if (band) {
                          String m= rsm.getString (1);
                          String facm=rsm.getString(2);
                          String jefe=rsm.getString(3);
                          String com=rsm.getString(4);
                          String jtpm=rsm.getString(5);
                          String bm=rsm.getString(6);
                          String tcom=rsm.getString(7);
                          String hi=rsm.getString(9);
                          String hf=rsm.getString(10);
                          dia=rsm.getString(11);
                          //String tmat=rsm.getString(12);
                          String aula1=(String)JTabladia.getValueAt(fila,0);
    //                   ////////////////////////////////////////////////////////////////
                          materia.setText("Materia: "+m);
                          mfacu.setText("Facultad: "+facm);
                          jefecat.setText("Jefe de Catedra: "+jefe);
                          comis.setText("Comision: "+com);
                          jtp.setText("JTP: "+jtpm);
                          box.setText("Box: "+bm);
                          tipcomi.setText(tcom);
                          him.setText("Hora Inicio: "+hi);
                          hfm.setText("Hora Fin: "+hf);
                          //tipom.setText(tmat);
                          aulam.setText("Aula: "+aula1);
                          band=false;
                        }
                       else {
                             String axDia=rsm.getString(11);
                             dia= dia+" "+axDia;
                               }
                       }
                        mdias.setText("Dias: "+dia);
                        validate();
                     }
              catch(SQLException eee)
                {
                  System.out.println("Problema al imprimir la base de datos 408");
                }
              }
        }
        }
       basedato.cierraConexion();
   //    basedato2.cierraConexion();
       }
    public void OcultarMateria(){
        materia.setVisible(false);
        mfacu.setVisible(false);
        jefecat.setVisible(false);
        comis.setVisible(false);
        jtp.setVisible(false);
        box.setVisible(false);
        tipcomi.setVisible(false);
        mcant.setVisible(false);
        him.setVisible(false);
        hfm.setVisible(false);
        mdias.setVisible(false);
        tipom.setVisible(false);
        aulam.setVisible(false);
    }
    public void OcultarEvento(){
        evento.setVisible(false);
        tipoe.setVisible(false);
        encargadoe.setVisible(false);
        facue.setVisible(false);
        cante.setVisible(false);
        hie.setVisible(false);
        hfe.setVisible(false);
        fe.setVisible(false);
        aulae.setVisible(false);
    }
     public void DesocultarMateria(){
        materia.setVisible(true);
        mfacu.setVisible(true);
        jefecat.setVisible(true);
        comis.setVisible(true);
        jtp.setVisible(true);
        box.setVisible(true);
        tipcomi.setVisible(true);
        mcant.setVisible(true);
        him.setVisible(true);
        hfm.setVisible(true);
        mdias.setVisible(true);
        tipom.setVisible(true);
        aulam.setVisible(true);
    }
    public void DesocultarEvento(){
        evento.setVisible(true);
        tipoe.setVisible(true);
        encargadoe.setVisible(true);
        facue.setVisible(true);
        cante.setVisible(true);
        hie.setVisible(true);
        hfe.setVisible(true);
        fe.setVisible(true);
        aulae.setVisible(true);
    }
    public int BuscarEvento (int f, int c){
        int e,fila,ci,cf;      e=-1;        int j=0;
        boolean b=true;
        while((j<vece.size())&&(b))
        {
            fila=vece.elementAt(j).ObtenerFila();
            ci=vece.elementAt(j).ObtenerColumnaIni();
            cf=vece.elementAt(j).ObtenerColumnaFin();

            if(fila==f){
                if (c<=cf){
                        if(c>=ci){
                 e=vece.elementAt(j).ObtenerEvento();
                 b=false;
              }}}
            j++;}
        return e;
    }
       public int BuscarMateria (int f, int c){
           int comi,fila,ci,cf;   comi=-1;     int j=0;
           boolean b=true;
           while((j<vecm.size())&&(b))
           {
               fila=vecm.elementAt(j).ObtenerFila();
               ci=vecm.elementAt(j).ObtenerColumnaIni();
               cf=vecm.elementAt(j).ObtenerColumnaFin();

               if((fila==f)&&(c<=cf)&&(c>=ci)){
                    comi=vecm.elementAt(j).ObtenerComiMat();
                    b=false;
              }
            j++;}
        return comi;
    }
    public void actionPerformed(ActionEvent e){
        Object f = e.getSource();
        if(f.equals(buscardia))//guardar aula en la tabla del panel
          {
            if(!JTfecha.getText().equals(""))
                {
                    String fech=JTfecha.getText();
                    Date fec2 = this.ParseFecha(fech);
                    this.borrarTabla();
                    this.cargarAulas();
                    this.cargarTablaConsultaDia(fec2.toString());//hacer consulta a la base de Datos de esa fecha
                }
            else{
                JOptionPane.showMessageDialog(this,
                    "No se pudo realizar la operacion.ingrese una Fecha Valida",
                        "Error",JOptionPane.WARNING_MESSAGE);
            }
        }
        if(f.equals(imprimir))
        {
            try {
                //deberia permitirme imprimir los resultados de la consulta o mandar a un excel por lo menos
                archivoExcel = new Excel(JTabladia);
            } catch (IOException ex) {
                Logger.getLogger(ConsultaDia.class.getName()).log(Level.SEVERE, null, ex);
            }
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

    public void stateChanged(ChangeEvent ce) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void valueChanged(ListSelectionEvent lse) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void cargarTablaConsultaDia (String fecha)
    {//este es para cargar el JTable de la consulta de un dia especifico
        basedato = new Postgres();
        basedato.estableceConexion();
        ResultSet rs = null;
        ResultSet rs2 = null;
        rs = basedato.consultaPorDiaMaterias(fecha);
        rs2= basedato.consultaPorDiaEventos(fecha);
        //int idc,cant,ide,cant2;
        //String materia,evento, aula, aula1,mas, mas1;
        //int facultad, facultad1,fila, comision, hsInicio, hsFin, hsInicio1, hsFin1,ind2;
 ///////////////////////////////// PARA EL COLOR DE LA CELDA
    econMoradoFila=new Vector<Integer>();
    econMoradoCol=new Vector<Integer>();
    saludNaranjFila=new Vector<Integer>();
    saludNaranjCol=new Vector<Integer>();
    naturCelesFila=new Vector<Integer>();
    naturCelesCol=new Vector<Integer>();
    exacRojoFila=new Vector<Integer>();
    exacRojoCol=new Vector<Integer>();
    ingVerdeFila=new Vector<Integer>();
    ingVerdeCol=new Vector<Integer>();
    humaAzulFila=new Vector<Integer>();
    humaAzulCol=new Vector<Integer>();
    otrosAmarFila=new Vector<Integer>();
    otrosAmarCol=new Vector<Integer>();
    /////////////////////////////////////////////////////////////////////////PARA PASAR EL MOUSE
    VecEvento= new ObjetoEvento[500];
    vecm= new Vector<ObjetoMateria>();
    VecMateria=new ObjetoMateria[500];
    vece= new Vector<ObjetoEvento>();


    ///////////////////////////////////////////////////////////////
    int ind  =-1;
    try{
        while (rs.next())
        {
            mat=new ObjetoMateria();
            ind++;

            String mate = rs.getString(1);
            int comision = rs.getInt(2);
            int facultad = rs.getInt(3);
            int hsInicio=rs.getInt(4);
            int hsFin=rs.getInt(5)-1;
            String aula= rs.getString(6);
            int idc=rs.getInt(7);
            String mas=mate+" "+"Com.N°"+comision;
            System.out.println(aula);
            int fila= buscarPosicion(aula);
  /////////////////////////////////////////////////////////////////
            System.out.println(idc);
            mat.ObjetoMateria(idc, fila, hsInicio-7,hsFin-7);
            vecm.add(mat);
            //VecMateria[ind]=mat;
            for (int x=hsInicio-7;x<=hsFin-7;x++)
            {
              DMTablaDia.setValueAt(mas, fila,x);
                switch(facultad){
             case 0:
                 exacRojoFila.add(fila);
                 exacRojoCol.add(x);
                 break;
             case 1:
                 econMoradoFila.add(fila);
                 econMoradoCol.add(x);
                 break;
             case 2:
                 ingVerdeFila.add(fila);
                 ingVerdeCol.add(x);
                 break;
             case 3:
                 naturCelesFila.add(fila);
                 naturCelesCol.add(x);
                 break;
             case 4:
                 saludNaranjFila.add(fila);
                 saludNaranjCol.add(x);
                 break;
             case 5:
                 humaAzulFila.add(fila);
                 humaAzulCol.add(x);
                 break;
             case 6:
                 otrosAmarFila.add(fila);
                 otrosAmarCol.add(x);
                 break;
             }
               }
         }
        } catch(SQLException e)
        {
            System.out.println("Problema al imprimir la base de datos 596");
        }
     int ind2=-1;
        try{
        while (rs2.next())
        {
            event=new ObjetoEvento();
            ind2++;
            String ev = rs2.getString (1);

            int tipo = rs2.getInt(2);//tipo del evento
            int facultad = 6;

            int hsInicio1=rs2.getInt(3);
            int hsFin1=rs2.getInt(4)-1;
            String aula1= rs2.getString(5);
            int ide=rs2.getInt(6);

            if((tipo==1)||(tipo==2)){
            int idm=rs2.getInt(7);//obtendo el id de materia si existe
                ResultSet rs3 = null;
                //basedato2 = new Postgres();
                //basedato2.estableceConexion();
                facultad=basedato.obtenerFacultadId(idm);
            }



            int fila1= buscarPosicion(aula1);
  /////////////////////////////////////////////////////////////////
             event.ObjetoEvento(ide, fila1, hsInicio1-7,hsFin1-7);
             vece.add(event);
             VecEvento[ind2]=event;
            for (int x=hsInicio1-7;x<=hsFin1-7;x++)
            {
                DMTablaDia.setValueAt("<html>"+ev+"</html>", fila1,x);
               switch(facultad){
             case 0:
                 exacRojoFila.add(fila1);
                 exacRojoCol.add(x);
                 break;
             case 1:
                 econMoradoFila.add(fila1);
                 econMoradoCol.add(x);
                 break;
             case 2:
                 ingVerdeFila.add(fila1);
                 ingVerdeCol.add(x);
                 break;
             case 3:
                 naturCelesFila.add(fila1);
                 naturCelesCol.add(x);
                 break;
             case 4:
                 saludNaranjFila.add(fila1);
                 saludNaranjCol.add(x);
                 break;
             case 5:
                 humaAzulFila.add(fila1);
                 humaAzulCol.add(x);
                 break;
             case 6:
                 otrosAmarFila.add(fila1);
                 otrosAmarCol.add(x);
                 break;
             }
            }
         }
        } catch(SQLException e)
        {
            System.out.println("Problema al imprimir la base de datos 650 ");
        }
        basedato.cierraConexion();
       JTabladia.setDefaultRenderer(Object.class, new MiRender2(econMoradoFila,econMoradoCol,saludNaranjFila,saludNaranjCol,naturCelesFila,naturCelesCol,exacRojoFila,exacRojoCol,ingVerdeFila,ingVerdeCol,humaAzulFila,humaAzulCol,otrosAmarFila,otrosAmarCol));//pinto la tabla

    }
    public int buscarPosicion(String v)
    {
        String valor;
        int fila=-1;
        do {
           fila++;
           valor=DMTablaDia.getValueAt(fila,0).toString();
        }
        while (!v.equals(valor));
        return fila;
    }
     public void cargarAulas(){//este es para cargar el JTable de aulas Existentes
         /// INTENTO CARGAR AULAS DE HOY POR DEFECTO
         //Date fechaActual = new Date();
        basedato = new Postgres();
        basedato.estableceConexion();
        ResultSet rs = null;
        rs = basedato.obtenerAulas();
        //int id,fila;
        String aula;
        //fila =0;
        try{
        while (rs.next())
        {
            aula = rs.getString (1);
           DMTablaDia.addRow(new Object[]{aula,"","","","","","","","","","","","","","","",""});
          // fila++;
        }
        TableColumnModel columnModel = JTabladia.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(100);
        } catch(SQLException e)
        {
            System.out.println("Problema cargar Tabla consulta por Dia patito 701");
        }
        basedato.cierraConexion();
    }
          public void cargarAulas1(){//este es para cargar el JTable de aulas Existentes
         /// INTENTO CARGAR AULAS DE HOY POR DEFECTO
         Date fechaActual = new Date();
        basedato = new Postgres();
        basedato.estableceConexion();
        ResultSet rs = null;
        rs = basedato.obtenerAulas();
        String aula;
        try{
        while (rs.next())
        {
            aula = rs.getString (1);
           DMTablaDia.addRow(new Object[]{aula,"","","","","","","","","","","","","","","",""});
          // fila++;
        }
        TableColumnModel columnModel = JTabladia.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(100);

       SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
       String f=sdf.format(fechaActual);

        this.cargarTablaConsultaDia(f);
        } catch(SQLException e)
        {
            System.out.println("Problema cargar Tabla consulta por Dia patito 725");
        }
        basedato.cierraConexion();
    }
     public void borrarTabla(){
         for (int i = 0; i < JTabladia.getRowCount(); i++) {
           DMTablaDia.removeRow(i);
           i-=1;
       }
     }
}

