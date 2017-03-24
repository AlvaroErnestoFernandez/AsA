/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Interfaz;

import java.awt.event.ActionEvent;
import javax.swing.*;
import com.toedter.calendar.JDateChooser;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import javax.swing.table.DefaultTableModel;
import Estructura.Postgres;
import java.awt.Dimension;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import javax.swing.table.TableCellRenderer;
/**
 *
 * @author Alvaro
 */
public class ProgramarCursosOtros extends JInternalFrame implements ActionListener{
    private JLabel JLcurso,JLencargado,JLinscriptos,JLhoras,JLfecha;
    private GTextField curso,encargado;
    private JSpinner inscriptos,horas;
    private SpinnerNumberModel spinModel,spinModel2;
    private JDateChooser fecha;
    private JButton mas,continuar,limpiar,asignar;
    private JList fechas;
    private DefaultListModel mfechas;
    private JTable JTabladia;
    private DefaultTableModel DMTablaDia;
    private JScrollPane scrlpDia,scrolLista;
    private final String [] columtabladia = {"Aulas/hs.","8-9","9-10","10-11","11-12","12-13","13-14","14-15","15-16","16-17","17-18","18-19","19-20","20-21","21-22","22-23","23-24"};
    private Postgres basedato;
    private Vector<Integer> aulasids;
    private Vector<Integer> aulasfilas;
    private Vector<String> aulasnombres;
    private Vector<Integer> aulascapacidades;
    //elementos para ocultar barra de titulo
    private JComponent Barra = ((javax.swing.plaf.basic.BasicInternalFrameUI) getUI()).getNorthPane();
    private Dimension dimBarra = null;
    //fila siguiente por asignar
    private int filasiguiente;
    private boolean seleccion=true;
    private int cantrestante=-1;
    private Vector<Vector<Integer>> fechasxaulas;
    private Vector<Vector<Integer>> fechasxcolumnas;
    private Vector<Vector<Integer>> fechasxfilas;
    private Vector<Integer> fechaxaulas;
    private Vector<Integer> fechaxcolumnas;
    private Vector<Integer> fechaxfilas;

    private Vector<Integer> rojof;
    private Vector<Integer> rojoc;
    private Vector<Integer> azulf;
    private Vector<Integer> azulc;

    public ProgramarCursosOtros(){
        super("Cursos y Otros", false, // resizable
                    false, // closable
                    false, // maximizable
                    false);// iconifiable
        ocultarBarraTitulo();
        this.getContentPane().setLayout(null);
        //this.getContentPane().setBackground(new Color(155,193,232));
        this.getContentPane().add(this.pnlTablaDia());
        cargarInicioTablaDia();
        this.getContentPane().add(this.pnlCurso());
        this.getContentPane().add(this.pnlLista());
        JTabladia.setEnabled(false);
        this.setBorder(null);        
        this.setVisible(false);
    }
    private JPanel pnlLista()
    {
        fechas = new JList();
        fechas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        mfechas = new DefaultListModel();
        fechas.setModel(mfechas);
        //fechas.addListSelectionListener(this);
        scrolLista = new JScrollPane(fechas);
        scrolLista.setBounds(10,20,220,210);
        fechas.setEnabled(false);
        JPanel pnl = new JPanel();
        pnl.setLayout(null);
        pnl.setBounds(600, 5, 240, 240);
        pnl.setBorder(BorderFactory.createTitledBorder("Fechas de Dictado"));
        pnl.setOpaque(false);

        pnl.add(scrolLista);

        return pnl;
    }
    public JPanel pnlCurso(){
       JLcurso = new JLabel("Nombre");
       this.cargarAutocompletadoCurso();
       JLencargado = new JLabel("Encargado");
       this.cargarAutocompletadoEncargado();
       JLinscriptos = new JLabel("Inscriptos");
       spinModel = new SpinnerNumberModel(1,1,1000,1);
       inscriptos = new JSpinner(spinModel);
       JLhoras = new JLabel("Horas");
       spinModel2 = new SpinnerNumberModel(1,1,10,1);
       horas = new JSpinner(spinModel2);
       JLfecha = new JLabel("Fecha");
       fecha = new JDateChooser();
       mas = new JButton("+");
       continuar = new JButton("Continuar");
        asignar = new JButton("Asignar");
        limpiar = new JButton("Limpiar");
            mas.setToolTipText("Agregar mas dias");
            continuar.setToolTipText("Continuar");
            asignar.setToolTipText("Asignar aulas a las materias seleccionadas");
            limpiar.setToolTipText("Limpiar seleccion");
            mas.addActionListener(this);
            continuar.addActionListener(this);
            asignar.addActionListener(this);
            limpiar.addActionListener(this);
            mas.setBackground(Color.white);
            continuar.setBackground(Color.white);
            asignar.setBackground(Color.white);
            limpiar.setBackground(Color.white);       
       int x = 20;//eje x
       int y = 25;//eje y
       int w = 60;//ancho
       int h = 30;//alto
       int i = 45;//incremento
       JLcurso.setBounds(x+20, y, 100, h);
       curso.setBounds(x+100, y, w+340, h);
       JLencargado.setBounds(x, y+i, w, h);
       encargado.setBounds(x+60, y+i, w+140, h);
       JLinscriptos.setBounds(x+285, y+i, w+10, h);
       inscriptos.setBounds(x+340, y+i, w, h);
       JLhoras.setBounds(x+415, y+i, w, h);
       horas.setBounds(x+465, y+i, w, h);
       JLfecha.setBounds(x+70, y+i*2, w, h);
       fecha.setBounds(x+110, y+i*2, w+100, h);
       fecha.setBackground(Color.white);
       mas.setBounds(x+300, y+i*2, 40, h);
       continuar.setBounds(x+60, y+i*3+10, w+40, h+10);
       limpiar.setBounds(x+200, y+i*3+10, w+40, h+10);
       asignar.setBounds(x+340, y+3*i+10, w+40, h+10);
       JPanel pnlfiltro = new JPanel();
       pnlfiltro.setLayout(null);
       pnlfiltro.setBounds(10, 5, 590, 240);//posicion y tamaño del panel
       pnlfiltro.setOpaque(false);
       pnlfiltro.setBorder(BorderFactory.createTitledBorder("Curso u Otro"));
       pnlfiltro.add(JLcurso);
       pnlfiltro.add(curso);
       pnlfiltro.add(JLencargado);
       pnlfiltro.add(encargado);
       pnlfiltro.add(JLinscriptos);
       pnlfiltro.add(inscriptos);
       pnlfiltro.add(JLhoras);
       pnlfiltro.add(horas);
       pnlfiltro.add(JLfecha);JLfecha.setEnabled(false);
       pnlfiltro.add(fecha);fecha.setEnabled(false);
       pnlfiltro.add(mas);mas.setEnabled(false);
       pnlfiltro.add(continuar);
       pnlfiltro.add(limpiar);limpiar.setEnabled(false);
       pnlfiltro.add(asignar);asignar.setEnabled(false);
       return pnlfiltro;
    }
    public void cargarAutocompletadoCurso(){
        curso = new GTextField(0  , 0 , true );
	basedato = new Postgres();
        basedato.estableceConexion();
        ResultSet rs = null;
        rs = basedato.obtenerCursosOtros();

        try{
        while (rs.next())
        {
            String cursonombre=rs.getString(1);
            curso.getDataList().add(cursonombre);
        }

        } catch(Exception e)
        {
            System.out.println("Problema al cargar el autocompletado ");
        }
        curso.setWidthPopupPanel(160);
	curso.setHeightPopupPanel(100);
        basedato.cierraConexion();
    }
    public void cargarAutocompletadoEncargado(){
        encargado = new GTextField(0  , 0 , true );
	basedato = new Postgres();
        basedato.estableceConexion();
        ResultSet rs = null;
        rs = basedato.obtenerEncargadosMaterias();

        try{
        while (rs.next())
        {
            String encargado=rs.getString(1);
            this.encargado.getDataList().add(encargado);
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
            this.encargado.getDataList().add(encargado);
        }

        } catch(Exception e)
        {
            System.out.println("Problema al cargar el autocompletado 2");
        }
        rs = basedato.obtenerEncargadosCursos();

        try{
        while (rs.next())
        {
            String encargado=rs.getString(1);
            this.encargado.getDataList().add(encargado);
        }

        } catch(Exception e)
        {
            System.out.println("Problema al cargar el autocompletado 2");
        }
        encargado.setWidthPopupPanel(160);
	encargado.setHeightPopupPanel(100);
        basedato.cierraConexion();
    }

    private JPanel pnlTablaDia()
    {
        DMTablaDia = new DefaultTableModel(1,17){
            @Override
 		public boolean isCellEditable(int row, int column) {
			if (0 == column)
			   return false;
			return super.isCellEditable(row, column);
			}
        };
        DMTablaDia.setColumnIdentifiers(columtabladia);
          int i=0;
            DMTablaDia.setValueAt("", i, 0);
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
         }
        };
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
            int row = JTabladia.rowAtPoint(evt.getPoint());
            int col = JTabladia.columnAtPoint(evt.getPoint());
                    int horas = Integer.parseInt(this.horas.getValue().toString());
                    boolean b = true;
                    int colb = col;
            for(int i=0;i<horas;i++){//AQUI COMPARO POR EL NOMBRE DE LA MATERIA POR AHORA, IMPLEMENTAR DESPUES POR EL ID DE LA MATERIA
                        if((colb>=17)||(!DMTablaDia.getValueAt(row, colb).toString().equals(""))){
                            b=false;}
                        colb++;
                    }
            if(b){
                if(cantrestante>0){
                    int suma=col+horas;
                                      if(suma<=17){
                                      cantrestante=cantrestante-aulascapacidades.get(row);                                      
                                      fechaxaulas.add(aulasids.get(row));
                                      fechaxfilas.add(row);
                                      fechaxcolumnas.add(col);
                                      if(cantrestante<1){
                                          fechasxaulas.add(fechaxaulas);
                                          fechasxfilas.add(fechaxfilas);
                                          fechasxcolumnas.add(fechaxcolumnas);
                                      }
                                      for(int j=0;j<horas;j++){
                                      DMTablaDia.setValueAt(curso.getText(), row, col);
                                      col++;}
                                      }else{
                                        JOptionPane.showMessageDialog(this,
                                        "Mala definicion de las horas",
                                            "FALLO ASIGNACION",JOptionPane.WARNING_MESSAGE);
                                      }


                }else{
                    JOptionPane.showMessageDialog(this,
                    "YA A ASIGNADO LAS AULAS NECESARIAS PARA LOS INSCRIPTOS",
                        "FALLO ASIGNACION",JOptionPane.WARNING_MESSAGE);
                }
            }else{
                JOptionPane.showMessageDialog(this,
                    "NO ES POSIBLE ASIGNAR EN ESTE HORARIO",
                        "FALLO ASIGNACION",JOptionPane.WARNING_MESSAGE);
            }
        }
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
    public void ocultarBarraTitulo()
    {
        Barra = ((javax.swing.plaf.basic.BasicInternalFrameUI) getUI()).getNorthPane();
        dimBarra = Barra.getPreferredSize();
        Barra.setSize(0,0);
        Barra.setPreferredSize(new Dimension(0,0));
        repaint();
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
     public void cargarTablaDia(Date fecha){
         rojof=new Vector<Integer>();
         rojoc=new Vector<Integer>();
         azulf=new Vector<Integer>();
         azulc=new Vector<Integer>();

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
                             rojof.add(i);
                             rojoc.add(col);
                             horainicio++;
                             col++;
                         }
                        break;
                     case 9:
                         col=2;
                         while(horainicio<horafin){
                             DMTablaDia.setValueAt(materia_nombre, i, col);
                             rojof.add(i);
                             rojoc.add(col);
                             horainicio++;
                             col++;
                         }
                        break;
                     case 10:
                         col=3;
                         while(horainicio<horafin){
                             DMTablaDia.setValueAt(materia_nombre, i, col);
                             rojof.add(i);
                             rojoc.add(col);
                             horainicio++;
                             col++;
                         }
                        break;
                     case 11:
                         col=4;
                         while(horainicio<horafin){
                             DMTablaDia.setValueAt(materia_nombre, i, col);
                             rojof.add(i);
                             rojoc.add(col);
                             horainicio++;
                             col++;
                         }
                        break;
                     case 12:
                         col=5;
                         while(horainicio<horafin){
                             DMTablaDia.setValueAt(materia_nombre, i, col);
                             rojof.add(i);
                             rojoc.add(col);
                             horainicio++;
                             col++;
                         }
                        break;
                     case 13:
                         col=6;
                         while(horainicio<horafin){
                             DMTablaDia.setValueAt(materia_nombre, i, col);
                             rojof.add(i);
                             rojoc.add(col);
                             horainicio++;
                             col++;
                         }
                        break;
                     case 14:
                         col=7;
                         while(horainicio<horafin){
                             DMTablaDia.setValueAt(materia_nombre, i, col);
                             rojof.add(i);
                             rojoc.add(col);
                             horainicio++;
                             col++;
                         }
                        break;
                     case 15:
                         col=8;
                         while(horainicio<horafin){
                             DMTablaDia.setValueAt(materia_nombre, i, col);
                             rojof.add(i);
                             rojoc.add(col);
                             horainicio++;
                             col++;
                         }
                        break;
                     case 16:
                         col=9;
                         while(horainicio<horafin){
                             DMTablaDia.setValueAt(materia_nombre, i, col);
                             rojof.add(i);
                             rojoc.add(col);
                             horainicio++;
                             col++;
                         }
                        break;
                     case 17:
                         col=10;
                         while(horainicio<horafin){
                             DMTablaDia.setValueAt(materia_nombre, i, col);
                             rojof.add(i);
                             rojoc.add(col);
                             horainicio++;
                             col++;
                         }
                        break;
                     case 18:
                         col=11;
                         while(horainicio<horafin){
                             DMTablaDia.setValueAt(materia_nombre, i, col);
                             rojof.add(i);
                             rojoc.add(col);
                             horainicio++;
                             col++;
                         }
                        break;
                     case 19:
                         col=12;
                         while(horainicio<horafin){
                             DMTablaDia.setValueAt(materia_nombre, i, col);
                             rojof.add(i);
                             rojoc.add(col);
                             horainicio++;
                             col++;
                         }
                        break;
                     case 20:
                         col=13;
                         while(horainicio<horafin){
                             DMTablaDia.setValueAt(materia_nombre, i, col);
                             rojof.add(i);
                             rojoc.add(col);
                             horainicio++;
                             col++;
                         }
                        break;
                     case 21:
                         col=14;
                         while(horainicio<horafin){
                             DMTablaDia.setValueAt(materia_nombre, i, col);
                             rojof.add(i);
                             rojoc.add(col);
                             horainicio++;
                             col++;
                         }
                        break;
                     case 22:
                         col=15;
                         while(horainicio<horafin){
                             DMTablaDia.setValueAt(materia_nombre, i, col);
                             rojof.add(i);
                             rojoc.add(col);
                             horainicio++;
                             col++;
                         }
                        break;
                     case 23:
                         col=16;
                         while(horainicio<horafin){
                             DMTablaDia.setValueAt(materia_nombre, i, col);
                             rojof.add(i);
                             rojoc.add(col);
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
                             azulf.add(i);
                             azulc.add(col);
                             horainicio++;
                             col++;
                         }
                        break;
                     case 9:
                         col=2;
                         while(horainicio<horafin){
                             DMTablaDia.setValueAt(materia_nombre, i, col);
                             azulf.add(i);
                             azulc.add(col);
                             horainicio++;
                             col++;
                         }
                        break;
                     case 10:
                         col=3;
                         while(horainicio<horafin){
                             DMTablaDia.setValueAt(materia_nombre, i, col);
                             azulf.add(i);
                             azulc.add(col);
                             horainicio++;
                             col++;
                         }
                        break;
                     case 11:
                         col=4;
                         while(horainicio<horafin){
                             DMTablaDia.setValueAt(materia_nombre, i, col);
                             azulf.add(i);
                             azulc.add(col);
                             horainicio++;
                             col++;
                         }
                        break;
                     case 12:
                         col=5;
                         while(horainicio<horafin){
                             DMTablaDia.setValueAt(materia_nombre, i, col);
                             azulf.add(i);
                             azulc.add(col);
                             horainicio++;
                             col++;
                         }
                        break;
                     case 13:
                         col=6;
                         while(horainicio<horafin){
                             DMTablaDia.setValueAt(materia_nombre, i, col);
                             azulf.add(i);
                             azulc.add(col);
                             horainicio++;
                             col++;
                         }
                        break;
                     case 14:
                         col=7;
                         while(horainicio<horafin){
                             DMTablaDia.setValueAt(materia_nombre, i, col);
                             azulf.add(i);
                             azulc.add(col);
                             horainicio++;
                             col++;
                         }
                        break;
                     case 15:
                         col=8;
                         while(horainicio<horafin){
                             DMTablaDia.setValueAt(materia_nombre, i, col);
                             azulf.add(i);
                             azulc.add(col);
                             horainicio++;
                             col++;
                         }
                        break;
                     case 16:
                         col=9;
                         while(horainicio<horafin){
                             DMTablaDia.setValueAt(materia_nombre, i, col);
                             azulf.add(i);
                             azulc.add(col);
                             horainicio++;
                             col++;
                         }
                        break;
                     case 17:
                         col=10;
                         while(horainicio<horafin){
                             DMTablaDia.setValueAt(materia_nombre, i, col);
                             azulf.add(i);
                             azulc.add(col);
                             horainicio++;
                             col++;
                         }
                        break;
                     case 18:
                         col=11;
                         while(horainicio<horafin){
                             DMTablaDia.setValueAt(materia_nombre, i, col);
                             azulf.add(i);
                             azulc.add(col);
                             horainicio++;
                             col++;
                         }
                        break;
                     case 19:
                         col=12;
                         while(horainicio<horafin){
                             DMTablaDia.setValueAt(materia_nombre, i, col);
                             azulf.add(i);
                             azulc.add(col);
                             horainicio++;
                             col++;
                         }
                        break;
                     case 20:
                         col=13;
                         while(horainicio<horafin){
                             DMTablaDia.setValueAt(materia_nombre, i, col);
                             azulf.add(i);
                             azulc.add(col);
                             horainicio++;
                             col++;
                         }
                        break;
                     case 21:
                         col=14;
                         while(horainicio<horafin){
                             DMTablaDia.setValueAt(materia_nombre, i, col);
                             azulf.add(i);
                             azulc.add(col);
                             horainicio++;
                             col++;
                         }
                        break;
                     case 22:
                         col=15;
                         while(horainicio<horafin){
                             DMTablaDia.setValueAt(materia_nombre, i, col);
                             azulf.add(i);
                             azulc.add(col);
                             horainicio++;
                             col++;
                         }
                        break;
                     case 23:
                         col=16;
                         while(horainicio<horafin){
                             DMTablaDia.setValueAt(materia_nombre, i, col);
                             azulf.add(i);
                             azulc.add(col);
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
         rs=null;
         rs = basedato.obtenerAsignacionesDiaCursosOtros(fecha);
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
    public void actionPerformed(ActionEvent e) {
        Object f = e.getSource();
        if(e.getSource()==continuar){
            if(!limpiar.isEnabled()){
                
                fechasxaulas= new Vector<Vector<Integer>>();
                fechasxcolumnas= new Vector<Vector<Integer>>();
                fechasxfilas= new Vector<Vector<Integer>>();
                filasiguiente=0;
            //continuar.setEnabled(false);
                if(!(curso.getText().equals(""))&&(!encargado.getText().equals(""))){
                curso.setEditable(false);
                encargado.setEditable(false);
                inscriptos.setEnabled(false);
                horas.setEnabled(false);
                fecha.setEnabled(true);
                mas.setEnabled(true);
                JLfecha.setEnabled(true);
                limpiar.setEnabled(true);
                asignar.setEnabled(true);
                }else{
                    JOptionPane.showMessageDialog(this,
                        "Existen campos vacios, por favor complete todo el formulario",
                            "FALLO CONSULTA",JOptionPane.WARNING_MESSAGE);
                }
            }else{
                if(mfechas.size()>0){
                    fecha.setEnabled(false);
                    mas.setEnabled(false);
                    if(cantrestante<1){
                        if(filasiguiente<mfechas.size()){
                            for (int i = 0; i < JTabladia.getRowCount(); i++) {
                                for (int j = 1; j < JTabladia.getColumnCount(); j++){
                            DMTablaDia.setValueAt("", i, j);}
                            }
                            JTabladia.setEnabled(true);
                            fechas.setSelectedIndex(filasiguiente);
                            Date dia = this.ParseFecha(mfechas.get(filasiguiente).toString());
                            this.cargarTablaDia(dia);//cargo la tabla dias con las asignaciones del dia
                            JTabladia.setDefaultRenderer(Object.class, new MiRender(azulf,azulc,rojof,rojoc));//pinto la tabla
                            seleccion=false;
                            cantrestante=Integer.parseInt(inscriptos.getValue().toString());

                            fechaxaulas = new Vector<Integer>();
                            fechaxcolumnas = new Vector<Integer>();
                            fechaxfilas = new Vector<Integer>();

                            filasiguiente++;
                        }
                        else{
                            JOptionPane.showMessageDialog(this,
                                "Ya ha terminado de asignar aulas a las fechas de dictado, confirme las asignaciones realizadas o limpie los datos",
                                    "FALLO CONSULTA",JOptionPane.WARNING_MESSAGE);
                        }
                    }else{
                        JOptionPane.showMessageDialog(this,
                                "Aun no a terminado de asignar las aulas necesarias para los inscriptos",
                                    "FALLO CONSULTA",JOptionPane.WARNING_MESSAGE);
                    }
                }else{
                    JOptionPane.showMessageDialog(this,
                            "Aun no a seleccionado ninguna fecha para el evento",
                                "FALLO CONSULTA",JOptionPane.WARNING_MESSAGE);
                }
            }
        }
        if(e.getSource()==limpiar){
            //continuar.setEnabled(true);
            filasiguiente=0;
            curso.setEditable(true);curso.setText("");
            encargado.setEditable(true);encargado.setText("");
            inscriptos.setEnabled(true);inscriptos.setValue(1);
            horas.setEnabled(true);horas.setValue(1);
            fecha.setEnabled(false);fecha.setDate(null);
            mas.setEnabled(false);
            JLfecha.setEnabled(false);
            limpiar.setEnabled(false);
            asignar.setEnabled(false);
            mfechas.removeAllElements();
            JTabladia.setEnabled(false);
            seleccion=true;
            cantrestante=-1;
            for (int i = 0; i < JTabladia.getRowCount(); i++) {
                                for (int j = 1; j < JTabladia.getColumnCount(); j++){
                            DMTablaDia.setValueAt("", i, j);}
                            }
        }
        if(e.getSource()==mas){
            Date dia = fecha.getDate();
            if(dia!=null){
            DateFormat fecha = new SimpleDateFormat("dd/MM/yyyy");
	    String convertido = fecha.format(dia);
            int i=0;
            boolean b=true;
            while((b)&&(i<mfechas.size())){
                if(convertido.equals(mfechas.get(i))){
                    b=false;
                }
                i++;
            }
                if(b){
                    mfechas.addElement(convertido);
                }else{/*
                    int h;
                    h =JOptionPane.showConfirmDialog(null, "Ud. ya tiene la siguiente la fecha "+convertido+", agregada en la lista de dictado, desea agregarga?", "Confirmar Agregado", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                            if (h==0){
                                 mfechas.addElement(convertido);
                                }*/
                    JOptionPane.showMessageDialog(this,
                        "La fecha "+convertido+" ya se ha agregado a lista",
                            "FALLO CONSULTA",JOptionPane.WARNING_MESSAGE);
                }
            }else{
               JOptionPane.showMessageDialog(this,
                        "No ha seleccionado una fecha, por favor hagalo",
                            "FALLO CONSULTA",JOptionPane.WARNING_MESSAGE);
            }
        }
        if(e.getSource()==asignar){            
            if((fechasxaulas.size()==mfechas.size())&&(mfechas.size()>0)){
                int eventotipo=4;
                            int h;
                            h =JOptionPane.showConfirmDialog(null, "Desea catologar al evento como un curso?", "Confirmar Agregado", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                            if (h==0){
                                 eventotipo=3;
                                }
                basedato = new Postgres();
                basedato.estableceConexion();
                for(int i=0;i<fechasxaulas.size();i++){
                    for(int j=0;j<fechasxaulas.get(i).size();j++){                        
                        int horainicio=0;
                        int horafin=0;
                        switch (fechasxcolumnas.get(i).get(j)) {
                             case 1:
                                horainicio=8;
                                horafin=horainicio+Integer.parseInt(horas.getValue().toString());
                                break;
                             case 2:
                                horainicio=9;
                                horafin=horainicio+Integer.parseInt(horas.getValue().toString());
                                break;
                             case 3:
                                 horainicio=10;
                                horafin=horainicio+Integer.parseInt(horas.getValue().toString());
                                break;
                             case 4:
                                horainicio=11;
                                horafin=horainicio+Integer.parseInt(horas.getValue().toString());
                                break;
                             case 5:
                                 horainicio=12;
                                horafin=horainicio+Integer.parseInt(horas.getValue().toString());
                                break;
                             case 6:
                                 horainicio=13;
                                horafin=horainicio+Integer.parseInt(horas.getValue().toString());
                                break;
                             case 7:
                                 horainicio=14;
                                horafin=horainicio+Integer.parseInt(horas.getValue().toString());
                                break;
                             case 8:
                                 horainicio=15;
                                horafin=horainicio+Integer.parseInt(horas.getValue().toString());
                                break;
                             case 9:
                                 horainicio=16;
                                horafin=horainicio+Integer.parseInt(horas.getValue().toString());
                                break;
                             case 10:
                                 horainicio=17;
                                horafin=horainicio+Integer.parseInt(horas.getValue().toString());
                                break;
                             case 11:
                                 horainicio=18;
                                horafin=horainicio+Integer.parseInt(horas.getValue().toString());
                                break;
                             case 12:
                                horainicio=19;
                                horafin=horainicio+Integer.parseInt(horas.getValue().toString());
                                break;
                             case 13:
                                 horainicio=20;
                                horafin=horainicio+Integer.parseInt(horas.getValue().toString());
                                break;
                             case 14:
                                 horainicio=21;
                                horafin=horainicio+Integer.parseInt(horas.getValue().toString());
                                break;
                             case 15:
                                 horainicio=22;
                                horafin=horainicio+Integer.parseInt(horas.getValue().toString());
                                break;
                             case 16:
                                 horainicio=23;
                                horafin=horainicio+Integer.parseInt(horas.getValue().toString());
                                break;
                        }
                        Date dia=this.ParseFecha(mfechas.get(i).toString());
                        basedato.cargarTablaCursosOtros(curso.getText(),encargado.getText(), Integer.parseInt(inscriptos.getValue().toString()), horainicio, horafin, fechasxaulas.get(i).get(j),dia,eventotipo);
                    }
                }
                filasiguiente=0;
            curso.setEditable(true);curso.setText("");
            encargado.setEditable(true);encargado.setText("");
            inscriptos.setEnabled(true);inscriptos.setValue(1);
            horas.setEnabled(true);horas.setValue(1);
            fecha.setEnabled(false);fecha.setDate(null);
            mas.setEnabled(false);
            JLfecha.setEnabled(false);
            limpiar.setEnabled(false);
            asignar.setEnabled(false);
            mfechas.removeAllElements();
            JTabladia.setEnabled(false);
            seleccion=true;
            cantrestante=-1;
            for (int i = 0; i < JTabladia.getRowCount(); i++) {
                                for (int j = 1; j < JTabladia.getColumnCount(); j++){
                            DMTablaDia.setValueAt("", i, j);}
                            }
                basedato.cierraConexion();
                JOptionPane.showMessageDialog(this,
                                        "Las asignaciones se han realizado correctamente",
                                            "EXITO",JOptionPane.WARNING_MESSAGE);
            }else{
                JOptionPane.showMessageDialog(this,
                        "Aun faltan por asignar aulas a las fechas de dictado",
                            "FALLO CONSULTA",JOptionPane.WARNING_MESSAGE);
            }
        }
    }


}
