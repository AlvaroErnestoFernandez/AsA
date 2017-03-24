package Interfaz;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.awt.*;
import javax.swing.table.*;
import java.util.HashMap;

import Estructura.Postgres;
import com.toedter.calendar.JDateChooser;
import java.sql.ResultSet;
//IMPORTO QUIZAS DE MAS PERO DESPUES LO DEPURO
//PARA FILTRAR TABLA MATERIA
import java.awt.Dimension;
import java.awt.GridLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Date;
import java.util.Vector;
import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class ProgramarParcial extends JInternalFrame implements ActionListener{
    private JTable tablamaterias,tablafecha,JTabladia;
    private JScrollPane scrlptmaterias,scrlptfecha,scrlpDia;//scroll tabla
    private DefaultTableModel tmaterias,tfecha,DMTablaDia;
    private final String [] colsmaterias = {"Materia","Facultad","Jefe de Catedra","Alumnos"};
    private final String [] columtabladia = {"Aulas/hs.","8-9","9-10","10-11","11-12","12-13","13-14","14-15","15-16","16-17","17-18","18-19","19-20","20-21","21-22","22-23","23-24"};
    private Postgres basedato;
    //elementos para ocultar barra de titulo
    private JComponent Barra = ((javax.swing.plaf.basic.BasicInternalFrameUI) getUI()).getNorthPane();
    private Dimension dimBarra = null;
    //elementos  para filtrar tabla
    private JTextField filtro;
    private JLabel buscar,ayudausuario,lhorainicio,lhorafin,lcantalumnos,lfecha,mcantidad,cantidadrestante;
    private JComboBox JChorainicio,JChorafin;
    private JButton borrar,guardar,limpiar;

    private JPanel pnlasigmanual,pnlfiltro;
    TableRowSorter<DefaultTableModel> sorter;
    RowFilter<DefaultTableModel, Object> rf = null;
    private JScrollPane scrollasimanual;

    private JDateChooser fecha;

    private JSpinner spin;
    private SpinnerNumberModel spinModel;

    private JSpinner spin2;
    private SpinnerNumberModel spinModel2;

    private int idmateria;
    private String nombremateria;
    Vector<String> aulasasignadas;
    Vector<Integer> filaaulaasignada;
    Vector<Integer> colaulaasignada;
    Vector<Integer> aulasids;
    Vector<String> aulasnombres;
    Vector<Integer> aulascapacidades;
    Vector<Integer> aulasfilas;
    //para la depuracion deberiamos crear una clase aula donde adopte estos atributos
    private int colanterior;

    public ProgramarParcial(){
        super("Asignacion Manual", false, // resizable
                    false, // closable
                    false, // maximizable
                    false);// iconifiable
        this.getContentPane().setLayout(null);
        //this.getContentPane().setBackground(new Color(155,193,232));
        this.getContentPane().add(this.pnlfiltro());
        this.getContentPane().add(this.pnlTablaMaterias());
        this.getContentPane().add(this.pnlTablaDia());
        cargarInicioTablaDia();
        JTabladia.setEnabled(false);
        this.setBorder(null);
        this.setVisible(false);

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
            modelocentrar.setHorizontalAlignment(SwingConstants.CENTER); tablamaterias.getColumnModel().getColumn(2).setCellRenderer(modelocentrar);
            modelocentrar.setHorizontalAlignment(SwingConstants.CENTER); tablamaterias.getColumnModel().getColumn(3).setCellRenderer(modelocentrar);
        tablamaterias.setAutoCreateRowSorter(true);
        tablamaterias.setRowSorter(sorter);
        scrlptmaterias = new JScrollPane(tablamaterias);
        setEventoMouseClicked(tablamaterias);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        scrlptmaterias.setBounds(10,20,700,210);


        JPanel pnl = new JPanel();

        pnl.setLayout(null);
        pnl.setBounds(600, 5, 720, 240);
        pnl.setBorder(BorderFactory.createTitledBorder("Materias"));
        pnl.setOpaque(false);
        tablamaterias.getColumnModel().getColumn(3).setPreferredWidth(0);//intento de adecuar el ancho de las columnas
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
        aulasasignadas=new Vector<String>();
        filaaulaasignada=new Vector<Integer>();
        colaulaasignada= new Vector<Integer>();
        colanterior=0;
        if(limpiar.getText().equals("Limpiar")){
            limpiar.setText("Aceptar");
        }
        for (int i = 1; i < JTabladia.getRowCount(); i++) {
                    for (int j = 1; j < JTabladia.getColumnCount(); j++){
                DMTablaDia.setValueAt("", i, j);}
                }
        fecha.setBackground(Color.white);        
        fecha.setDate(null);
        int row = tablamaterias.rowAtPoint(evt.getPoint());
                    basedato = new Postgres();
                    basedato.estableceConexion();
                        if (row >= 0 && tablamaterias.isEnabled())
                        {
                              nombremateria=tablamaterias.getValueAt(row, 0).toString();
                              idmateria = basedato.obtnerIdMateria(tablamaterias.getValueAt(row, 0).toString(),tablamaterias.getValueAt(row, 2).toString(),tablamaterias.getValueAt(row, 1).toString());
                              ayudausuario.setText(tablamaterias.getValueAt(row, 0).toString());
                              spin.setValue(Integer.parseInt(tablamaterias.getValueAt(row, 3).toString()));
                              cantidadrestante.setText(spin.getValue().toString());
                        }
                    basedato.cierraConexion();
        habilitarDeshabilitar(true);
        guardar.setEnabled(false);
        borrar.setEnabled(false);
    }
    public JPanel pnlfiltro(){
       //filtro = new JTextField();
        filtro = new JTextField(15){
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
       buscar = new JLabel("Filtrar Materias");
       lhorainicio = new JLabel("Hora inicio");
       lhorafin = new JLabel("Horas");
       lcantalumnos = new JLabel("Cantidad alumnos");
       lfecha = new JLabel("Fecha");
       mcantidad = new JLabel("Alumnos fuera de alcance ");
       cantidadrestante = new JLabel("");

       JChorainicio = new JComboBox(new String[] {"08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"});
        //PARA UNA POSIBLE MEJORA SE PROPONE QUE LOS ITEMS DEL COMBOBOX DE HORA FIN DEBERIAN DEPENDER DE LOS QUE SE SELECIONE EN EL COMBOBOX DE HORA INICIO
       JChorafin = new JComboBox(new String[] {"09","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24"});

       spinModel = new SpinnerNumberModel(1,0,1000,1);
        spin = new JSpinner(spinModel);//cantidad alumnos
        spin.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                    cantidadrestante.setText(spin.getValue().toString());
            }

        });

        spinModel2 = new SpinnerNumberModel(1,0,10,1);
        spin2 = new JSpinner(spinModel2);//horas de dictado

        fecha = new JDateChooser();
            fecha.setBackground(new Color(155,193,232));
            borrar= new JButton("Borrar");
            borrar.setToolTipText("Borrar aula seleccionada");
            borrar.addActionListener(this);
            guardar= new JButton("Asignar");
            guardar.setToolTipText("Guardar asignaciones");
            guardar.addActionListener(this);
            limpiar= new JButton("Aceptar");
            limpiar.setToolTipText("Reiniciar los elementos");
            limpiar.addActionListener(this);
            //lfecha,mcantidad,cantidadrestante,borrar,guardar,limpiar,fecha,,
       ayudausuario = new JLabel("Seleccione una materia de la tabla para asignarle un parcial");
       ayudausuario.setFont(new java.awt.Font("Courier", Font.BOLD,14));
       //ayudausuario.setForeground(Color.red);
       int x = 50;//eje x
       int y = 30;//eje y
       int w = 400;//ancho
       int h = 30;//alto
       int i = 40;//incremento
       filtro.setBounds(x+i*3, y, w, h);
       buscar.setBounds(x, y, w-200, h);
       ayudausuario.setBounds(x, y+i, 500, h);
       lhorainicio.setBounds(x, y+i*2, w-300, h);
       lhorafin.setBounds(x+190, y+i*2, 60, h);
       lcantalumnos.setBounds(x+310, y+i*2, 130, h);
       JChorainicio.setBounds(x+70, y+i*2, 50, h);
       JChorafin.setBounds(x+180, y+i*2, 50, h);
       spin.setBounds(x+420, y+i*2, 65, h);
       spin2.setBounds(x+230, y+i*2, 65, h);
       //x+56, y+i*3,60, h
       lfecha.setBounds(x, y+i*2,60, h);
       fecha.setBounds(x+44, y+i*2, 120, h);
       limpiar.setBounds(x+120, y+i*3+5, 100, h+5);
       guardar.setBounds(x+330, y+i*3+5, 100, h+5);
       borrar.setBounds(x+60, y+i*3+5, 100, h+5);
       mcantidad.setBounds(x, y+i*4+5, 230, h);
       cantidadrestante.setBounds(x+170, y+i*4+5, 40, h);
       cantidadrestante.setFont(new java.awt.Font("Courier", Font.BOLD,15));
       pnlfiltro = new JPanel();
       pnlfiltro.setLayout(null);
       pnlfiltro.setBounds(10, 5, 590, 240);//posicion y tamaño del panel
       pnlfiltro.setOpaque(false);
       pnlfiltro.setBorder(BorderFactory.createTitledBorder("Filtro"));
       pnlfiltro.add(filtro);
       buscar.setFont(new java.awt.Font("Arial", 0, 16));
       pnlfiltro.add(buscar);
       pnlfiltro.add(ayudausuario);

       //pnlfiltro.add(lhorainicio);
       pnlfiltro.add(lhorafin);
       pnlfiltro.add(spin2);
       pnlfiltro.add(lcantalumnos);
       //pnlfiltro.add(JChorainicio);
       //pnlfiltro.add(JChorafin);
       pnlfiltro.add(spin);
       pnlfiltro.add(lfecha);
       pnlfiltro.add(fecha);
       pnlfiltro.add(limpiar);
       pnlfiltro.add(mcantidad);
       pnlfiltro.add(cantidadrestante);
       //pnlfiltro.add(borrar);
       pnlfiltro.add(guardar);
       habilitarDeshabilitar(false);
       return pnlfiltro;
    }
    public void habilitarDeshabilitar(Boolean b){
       lhorainicio.setEnabled(b);
       lhorafin.setEnabled(b);
       lcantalumnos.setEnabled(b);
       JChorainicio.setEnabled(b);
       JChorafin.setEnabled(b);
       spin.setEnabled(b);
       spin2.setEnabled(b);
       lfecha.setEnabled(b);
       fecha.setEnabled(b);
       limpiar.setEnabled(b);
       mcantidad.setVisible(b);
       cantidadrestante.setVisible(b);
       guardar.setEnabled(b);
       borrar.setEnabled(b);
       
    }

    public void ocultarBarraTitulo()
    {
        Barra = ((javax.swing.plaf.basic.BasicInternalFrameUI) getUI()).getNorthPane();
        dimBarra = Barra.getPreferredSize();
        Barra.setSize(0,0);
        Barra.setPreferredSize(new Dimension(0,0));
        repaint();
    }
    public void actionPerformed(ActionEvent g) {
         Object f = g.getSource();
        if(f.equals(limpiar)){
            Date dia = fecha.getDate();
            if(limpiar.getText()=="Aceptar"){
                if(dia!=null){
                 basedato = new Postgres();
                 basedato.estableceConexion();
                 boolean b=basedato.eventosAsignaciones(dia,idmateria);
                 basedato.cierraConexion();
                     if(!b){
                        limpiar.setText("Limpiar");
                        JTabladia.setEnabled(true);

                        this.habilitarDeshabilitar(false);
                        mcantidad.setVisible(true);
                        cantidadrestante.setVisible(true);
                        limpiar.setEnabled(true);
                        guardar.setEnabled(true);
                        borrar.setEnabled(true);
                        this.cargarTablaDia(dia);}
                     else{
                        JOptionPane.showMessageDialog(this,
                    "La materia ya tienen un parcial asignad para esta fecha",
                        "FALLO CONSULTA",JOptionPane.WARNING_MESSAGE);
                     }
                 }
                else{
                    JOptionPane.showMessageDialog(this,
                    "Seleccione una fecha por favor",
                        "FALLO CONSULTA",JOptionPane.WARNING_MESSAGE);
                }
            }
            else{
                for (int i = 0; i < JTabladia.getRowCount(); i++) {
                    for (int j = 1; j < JTabladia.getColumnCount(); j++){
                DMTablaDia.setValueAt("", i, j);}
                }
                colanterior=0;
                spin2.setValue(1);
                limpiar.setText("Aceptar");
                JTabladia.setEnabled(false);
                this.habilitarDeshabilitar(true);
                guardar.setEnabled(false);
                fecha.setDate(null);
                filaaulaasignada = new Vector<Integer>();
                colaulaasignada = new Vector<Integer>();
                aulasasignadas = new Vector<String>();
            }
        }
         if(f.equals(guardar)){
             Vector<Integer>idaulas = new Vector<Integer>();
             for(int i=0;i<filaaulaasignada.size();i++){
                 idaulas.add(filaaulaasignada.elementAt(i));
             }
             int horainicio=0;
             int horafin=0;
             switch (colanterior) {
                     case 1:
                        horainicio=8;
                        horafin=horainicio+Integer.parseInt(spin2.getValue().toString());
                        break;
                     case 2:
                        horainicio=9;
                        horafin=horainicio+Integer.parseInt(spin2.getValue().toString());
                        break;
                     case 3:
                         horainicio=10;
                        horafin=horainicio+Integer.parseInt(spin2.getValue().toString());
                        break;
                     case 4:
                        horainicio=11;
                        horafin=horainicio+Integer.parseInt(spin2.getValue().toString());
                        break;
                     case 5:
                         horainicio=12;
                        horafin=horainicio+Integer.parseInt(spin2.getValue().toString());
                        break;
                     case 6:
                         horainicio=13;
                        horafin=horainicio+Integer.parseInt(spin2.getValue().toString());
                        break;
                     case 7:
                         horainicio=14;
                        horafin=horainicio+Integer.parseInt(spin2.getValue().toString());
                        break;
                     case 8:
                         horainicio=15;
                        horafin=horainicio+Integer.parseInt(spin2.getValue().toString());
                        break;
                     case 9:
                         horainicio=16;
                        horafin=horainicio+Integer.parseInt(spin2.getValue().toString());
                        break;
                     case 10:
                         horainicio=17;
                        horafin=horainicio+Integer.parseInt(spin2.getValue().toString());
                        break;
                     case 11:
                         horainicio=18;
                        horafin=horainicio+Integer.parseInt(spin2.getValue().toString());
                        break;
                     case 12:
                        horainicio=19;
                        horafin=horainicio+Integer.parseInt(spin2.getValue().toString());
                        break;
                     case 13:
                         horainicio=20;
                        horafin=horainicio+Integer.parseInt(spin2.getValue().toString());
                        break;
                     case 14:
                         horainicio=21;
                        horafin=horainicio+Integer.parseInt(spin2.getValue().toString());
                        break;
                     case 15:
                         horainicio=22;
                        horafin=horainicio+Integer.parseInt(spin2.getValue().toString());
                        break;
                     case 16:
                         horainicio=23;
                        horafin=horainicio+Integer.parseInt(spin2.getValue().toString());
                        break;
                }
             basedato = new Postgres();
             basedato.estableceConexion();
             Date dia = fecha.getDate();
             String materia = ayudausuario.getText();
             basedato.cargarTablaEventosParciales(idmateria,Integer.parseInt(spin.getValue().toString()), dia, horainicio, horafin, idaulas,materia);
             basedato.cierraConexion();
             JOptionPane.showMessageDialog(this,
                                    "Las aulas seleccionadas se han asignado correctamente",
                                        "ASIGNACION REALIZADA",JOptionPane.WARNING_MESSAGE);
             for (int i = 0; i < JTabladia.getRowCount(); i++) {
                    for (int j = 1; j < JTabladia.getColumnCount(); j++){
                DMTablaDia.setValueAt("", i, j);}
                }
                colanterior=0;
                spin2.setValue(1);
                limpiar.setText("Aceptar");
                JTabladia.setEnabled(false);
                this.habilitarDeshabilitar(true);
                fecha.setDate(null);
                filaaulaasignada = new Vector<Integer>();
                colaulaasignada = new Vector<Integer>();
                aulasasignadas = new Vector<String>();
         }
                    
    }
     public void cargarTablaMaterias(){
         filtro.setText("");
         tablamaterias.getSelectionModel().clearSelection();
         for (int i = tmaterias.getRowCount()-1; i > -1 ; i--) {
            tmaterias.removeRow(i);
            }
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
            cant=basedato2.cantidadAlumnos(id);
            tmaterias.addRow(new Object[]{materia,sfacultad,jefecatedra,cant});
        }

        } catch(Exception e)
        {
            System.out.println("Problema al cargar la tabla materia ");
        }
        basedato.cierraConexion();
    }
     private JPanel pnlTablaDia()
    {/////lo pruebo con 10 filas pero no se xq no funca despues sin determinar el tamaño //// para solucionar esto la consulta la voy a tener antes asi que vere que se inicialce despues
        DMTablaDia = new DefaultTableModel(1,17){
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
        //for (int i = 0; i < 10; i++)
          int i=0;
            DMTablaDia.setValueAt("", i, 0);
// JTable al que se le pasa el modelo recien creado y se le sobreescribe el metodo changeSelection para que no permita seleccionar la primera columna.
        JTabladia= new JTable(DMTablaDia){
          @Override
          public void changeSelection(int rowIndex, int columnIndex,
					boolean toggle, boolean extend) {
				if (columnIndex == 0)
					super.changeSelection(rowIndex, columnIndex + 1, toggle,
							extend);
				else
					super.changeSelection(rowIndex, columnIndex, toggle,
									extend);
			}
          @Override
          public boolean isCellEditable(int rowIndex, int vColIndex) {
            return false;
           //CON ESTO DESHABILITO LA EDICION MANUAL DE LA TABLA
         }
        };//
//Se pone a la primera columna el render del JTableHeader superior.
		JTabladia.getColumnModel().getColumn(0).setCellRenderer(
				JTabladia.getTableHeader().getDefaultRenderer());
       // JTabladia.setRowSorter(new TableRowSorter(DMTablaDia)); //este lo comento xq no se par funciona
        scrlpDia= new JScrollPane(JTabladia);
        setEventoMouseClicked2(JTabladia);
        scrlpDia.setBounds(10,20,1280,400);
        //AQUI SE CENTRARIA LOS DTOS PERO LO VOY A VER DESPUS???
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        JPanel pnl = new JPanel();
        pnl.setLayout(null);
        pnl.setBounds(10, 245, 1300, 430);
        pnl.setBorder(BorderFactory.createTitledBorder("Tabla de ASIGANACIONES DEL DIA"));
        pnl.setOpaque(false);
        pnl.add(scrlpDia);
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
        if(JTabladia.isEnabled()){
       //System.exit(0);

            int row = JTabladia.rowAtPoint(evt.getPoint());
            int col = JTabladia.columnAtPoint(evt.getPoint());
            
                    basedato = new Postgres();
                    basedato.estableceConexion();
                    int horas = Integer.parseInt(spin2.getValue().toString());
                    boolean b = true;
                    int colb = col;
                    for(int i=0;i<horas;i++){//AQUI COMPARO POR EL NOMBRE DE LA MATERIA POR AHORA, IMPLEMENTAR DESPUES POR EL ID DE LA MATERIA
                        if(colb<17){
                            if(!DMTablaDia.getValueAt(row, colb).toString().equals("")){
                                if(!DMTablaDia.getValueAt(row, colb).toString().equals(nombremateria)){
                                b=false;}
                            }
                        }else{
                            b=false;
                        }
                        colb++;
                    }

                    if(b){
                        if(colanterior==0){
                            colanterior=col;
                        }
                        if (row >= 0 && JTabladia.isEnabled() && col >=1 && colanterior==col)
                        {
                              boolean b2=true;
                              for(int x=0;x<filaaulaasignada.size();x++){
                                  if(filaaulaasignada.get(x)==row){
                                      b2=false;
                                  }
                              }
                              if(b2){
                              int res = Integer.parseInt(cantidadrestante.getText());                              
                                  if(res>0){
                                      int suma=col+horas;
                                      if(suma<=17){
                                      res = res-aulascapacidades.elementAt(row);
                                      cantidadrestante.setText(String.valueOf(res));
                                      aulasasignadas.add(DMTablaDia.getValueAt(row, 0).toString());
                                      filaaulaasignada.add(row);
                                      colaulaasignada.add(col);
                                      for(int j=0;j<horas;j++){
                                      DMTablaDia.setValueAt(nombremateria, row, col);
                                      col++;}
                                      }else{
                                        colanterior=0;
                                        JOptionPane.showMessageDialog(this,
                                        "Mala definicion de las horas",
                                            "FALLO ASIGNACION",JOptionPane.WARNING_MESSAGE);
                                      }
                                  }else{
                                    JOptionPane.showMessageDialog(this,
                                    "El parcial ya cumple con la cantidad de alumnos",
                                        "FALLO ASIGNACION",JOptionPane.WARNING_MESSAGE);
                                  }

                              }

                        }
                        else{
                            JOptionPane.showMessageDialog(this,
                    "EL PARCIAL DEBE EMPEZAR A LA MISMA HORA",
                        "FALLO ASIGNACION",JOptionPane.WARNING_MESSAGE);
                        }
                    }else{
                        JOptionPane.showMessageDialog(this,
                    "EXISTEN MATERIAS ASIGNADAS DONDE QUIERE REALIZAR EL PARCIAL",
                        "FALLO ASIGNACION",JOptionPane.WARNING_MESSAGE);
                    }
                    basedato.cierraConexion();
        }
    }
     public void cargarTablaDia(Date fecha){
         
         /*for (int i = 0; i < JTabladia.getRowCount(); i++) {
            DMTablaDia.removeRow(i);
            i-=1;
            }*/
         //System.out.println(fecha);
         basedato = new Postgres();
         basedato.estableceConexion();
         ResultSet rs = null;
         rs = basedato.obtenerAsignacionesDia(fecha);
         int fila=0;
          try{
        while (rs.next())
        {
            //aulas.aula_id,aula_nombre,aula_capacidad,asignacion_horainicio,asignacion_horafin,comisiones.comision_id,comision_tipo,materias.materia_id,materia_nombre,comision_numero
            int aula_id=rs.getInt(1);
            String aula_nombre=rs.getString(2);
            int aula_capacidad=rs.getInt(3);
            int horainicio=rs.getInt(4);
            int horafin=rs.getInt(5);
            int comision_id=rs.getInt(6);
            String comision_tipo=rs.getString(7);
            int materia_id=rs.getInt(8);
            String materia_nombre=rs.getString(9);
            int comision_numero=rs.getInt(10);
            for (int i = 0; i < JTabladia.getRowCount(); i++) {
            if(DMTablaDia.getValueAt(i, 0).equals(aula_nombre)){
                int col=0;
                switch (horainicio) {
                     case 8:
                         col=1;
                         while(horainicio<horafin){
                             DMTablaDia.setValueAt(materia_nombre, i, col);
                             horainicio++;
                             col++;
                         }
                        break;
                     case 9:
                         col=2;
                         while(horainicio<horafin){
                             DMTablaDia.setValueAt(materia_nombre, i, col);
                             horainicio++;
                             col++;
                         }
                        break;
                     case 10:
                         col=3;
                         while(horainicio<horafin){
                             DMTablaDia.setValueAt(materia_nombre, i, col);
                             horainicio++;
                             col++;
                         }
                        break;
                     case 11:
                         col=4;
                         while(horainicio<horafin){
                             DMTablaDia.setValueAt(materia_nombre, i, col);
                             horainicio++;
                             col++;
                         }
                        break;
                     case 12:
                         col=5;
                         while(horainicio<horafin){
                             DMTablaDia.setValueAt(materia_nombre, i, col);
                             horainicio++;
                             col++;
                         }
                        break;
                     case 13:
                         col=6;
                         while(horainicio<horafin){
                             DMTablaDia.setValueAt(materia_nombre, i, col);
                             horainicio++;
                             col++;
                         }
                        break;
                     case 14:
                         col=7;
                         while(horainicio<horafin){
                             DMTablaDia.setValueAt(materia_nombre, i, col);
                             horainicio++;
                             col++;
                         }
                        break;
                     case 15:
                         col=8;
                         while(horainicio<horafin){
                             DMTablaDia.setValueAt(materia_nombre, i, col);
                             horainicio++;
                             col++;
                         }
                        break;
                     case 16:
                         col=9;
                         while(horainicio<horafin){
                             DMTablaDia.setValueAt(materia_nombre, i, col);
                             horainicio++;
                             col++;
                         }
                        break;
                     case 17:
                         col=10;
                         while(horainicio<horafin){
                             DMTablaDia.setValueAt(materia_nombre, i, col);
                             horainicio++;
                             col++;
                         }
                        break;
                     case 18:
                         col=11;
                         while(horainicio<horafin){
                             DMTablaDia.setValueAt(materia_nombre, i, col);
                             horainicio++;
                             col++;
                         }
                        break;
                     case 19:
                         col=12;
                         while(horainicio<horafin){
                             DMTablaDia.setValueAt(materia_nombre, i, col);
                             horainicio++;
                             col++;
                         }
                        break;
                     case 20:
                         col=13;
                         while(horainicio<horafin){
                             DMTablaDia.setValueAt(materia_nombre, i, col);
                             horainicio++;
                             col++;
                         }
                        break;
                     case 21:
                         col=14;
                         while(horainicio<horafin){
                             DMTablaDia.setValueAt(materia_nombre, i, col);
                             horainicio++;
                             col++;
                         }
                        break;
                     case 22:
                         col=15;
                         while(horainicio<horafin){
                             DMTablaDia.setValueAt(materia_nombre, i, col);
                             horainicio++;
                             col++;
                         }
                        break;
                     case 23:
                         col=16;
                         while(horainicio<horafin){
                             DMTablaDia.setValueAt(materia_nombre, i, col);
                             horainicio++;
                             col++;
                         }
                        break;
                }
            }          
            }
            fila++;
        }
        } catch(Exception e)
        {
            System.out.println("Problema al cargar la tabla");
        }
         rs=null;
         rs = basedato.obtenerAsignacionesDiaEventos(fecha);
         fila=0;
          try{
        while (rs.next())
        {
            //aulas.aula_id,aula_nombre,aula_capacidad,asignacion_horainicio,asignacion_horafin,comisiones.comision_id,comision_tipo,materias.materia_id,materia_nombre,comision_numero
            int aula_id=rs.getInt(1);
            String aula_nombre=rs.getString(2);
            int aula_capacidad=rs.getInt(3);
            int horainicio=rs.getInt(4);
            int horafin=rs.getInt(5);
            int materia_id=rs.getInt(6);
            String materia_nombre=rs.getString(7);
            for (int i = 0; i < JTabladia.getRowCount(); i++) {
            if(DMTablaDia.getValueAt(i, 0).equals(aula_nombre)){
                int col=0;
                switch (horainicio) {
                     case 8:
                         col=1;
                         while(horainicio<horafin){
                             DMTablaDia.setValueAt(materia_nombre, i, col);
                             horainicio++;
                             col++;
                         }
                        break;
                     case 9:
                         col=2;
                         while(horainicio<horafin){
                             DMTablaDia.setValueAt(materia_nombre, i, col);
                             horainicio++;
                             col++;
                         }
                        break;
                     case 10:
                         col=3;
                         while(horainicio<horafin){
                             DMTablaDia.setValueAt(materia_nombre, i, col);
                             horainicio++;
                             col++;
                         }
                        break;
                     case 11:
                         col=4;
                         while(horainicio<horafin){
                             DMTablaDia.setValueAt(materia_nombre, i, col);
                             horainicio++;
                             col++;
                         }
                        break;
                     case 12:
                         col=5;
                         while(horainicio<horafin){
                             DMTablaDia.setValueAt(materia_nombre, i, col);
                             horainicio++;
                             col++;
                         }
                        break;
                     case 13:
                         col=6;
                         while(horainicio<horafin){
                             DMTablaDia.setValueAt(materia_nombre, i, col);
                             horainicio++;
                             col++;
                         }
                        break;
                     case 14:
                         col=7;
                         while(horainicio<horafin){
                             DMTablaDia.setValueAt(materia_nombre, i, col);
                             horainicio++;
                             col++;
                         }
                        break;
                     case 15:
                         col=8;
                         while(horainicio<horafin){
                             DMTablaDia.setValueAt(materia_nombre, i, col);
                             horainicio++;
                             col++;
                         }
                        break;
                     case 16:
                         col=9;
                         while(horainicio<horafin){
                             DMTablaDia.setValueAt(materia_nombre, i, col);
                             horainicio++;
                             col++;
                         }
                        break;
                     case 17:
                         col=10;
                         while(horainicio<horafin){
                             DMTablaDia.setValueAt(materia_nombre, i, col);
                             horainicio++;
                             col++;
                         }
                        break;
                     case 18:
                         col=11;
                         while(horainicio<horafin){
                             DMTablaDia.setValueAt(materia_nombre, i, col);
                             horainicio++;
                             col++;
                         }
                        break;
                     case 19:
                         col=12;
                         while(horainicio<horafin){
                             DMTablaDia.setValueAt(materia_nombre, i, col);
                             horainicio++;
                             col++;
                         }
                        break;
                     case 20:
                         col=13;
                         while(horainicio<horafin){
                             DMTablaDia.setValueAt(materia_nombre, i, col);
                             horainicio++;
                             col++;
                         }
                        break;
                     case 21:
                         col=14;
                         while(horainicio<horafin){
                             DMTablaDia.setValueAt(materia_nombre, i, col);
                             horainicio++;
                             col++;
                         }
                        break;
                     case 22:
                         col=15;
                         while(horainicio<horafin){
                             DMTablaDia.setValueAt(materia_nombre, i, col);
                             horainicio++;
                             col++;
                         }
                        break;
                     case 23:
                         col=16;
                         while(horainicio<horafin){
                             DMTablaDia.setValueAt(materia_nombre, i, col);
                             horainicio++;
                             col++;
                         }
                        break;
                }
            }
            }
            fila++;
        }
        } catch(Exception e)
        {
            System.out.println("Problema al cargar la tabla dia con los eventos");
        }
         basedato.cierraConexion();
     }
     public void cargarInicioTablaDia(){
        basedato = new Postgres();
         basedato.estableceConexion();
         ResultSet rs = null;
         rs = basedato.obtenerAulas();
         int fila=0;
        aulasids=new Vector<Integer>();
        aulasfilas=new Vector<Integer>();
        aulasnombres=new Vector<String>();
        aulascapacidades=new Vector<Integer>();
        int cant=0;
          try{
        while (rs.next())
        {
            //aulas.aula_id,aula_nombre,aula_capacidad,asignacion_horainicio,asignacion_horafin,comisiones.comision_id,comision_tipo,materias.materia_id,materia_nombre,comision_numero
            int aula_id=rs.getInt(3);
            String aula_nombre=rs.getString(1);
            int aula_capacidad=rs.getInt(2);
            aulasids.add(aula_id);
            aulasnombres.add(aula_nombre);
            aulascapacidades.add(aula_capacidad);
            aulasfilas.add(cant);
            DMTablaDia.addRow(new Object[]{aula_nombre,"","","","","","","","","","","","","","","",""});
            cant++;
        }
        } catch(Exception e)
        {
            System.out.println("Problema al obtener al cargar el inicio de la tabla dias");
        }
        DMTablaDia.removeRow(0);
         basedato.cierraConexion();
     }

}