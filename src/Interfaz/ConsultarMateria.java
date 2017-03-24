package Interfaz;

import com.toedter.calendar.JYearChooser;
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
import java.util.Vector;

public class ConsultarMateria extends JInternalFrame implements ActionListener{
    private JComboBox facultades,cuatrimestre;
    private JYearChooser año;
    private JLabel lcuatrimestre,laño;
    private JButton continuar,limpiar,lunes,martes,miercoles,jueves,viernes,parciales,finales;//bueno la idea es utilizar una ventana modal
    private JTextField filtro;
    private JLabel Lfiltro;
    private JTable tablamaterias;

    private JScrollPane scrltmaterias;
    private DefaultTableModel tmaterias;
    private final Object[] colsmaterias = {"Materia","Jefe de Catedra"};

    TableRowSorter<DefaultTableModel> sorter;
    RowFilter<DefaultTableModel, Object> rf = null;

    private JComponent Barra = ((javax.swing.plaf.basic.BasicInternalFrameUI) getUI()).getNorthPane();
    private Dimension dimBarra = null;

    private JTable JTabladia;
    private DefaultTableModel DMTablaDia;
    private JScrollPane scrlpDia;
    private final String [] columtabladia = {"Aulas/hs.","8-9","9-10","10-11","11-12","12-13","13-14","14-15","15-16","16-17","17-18","18-19","19-20","20-21","21-22","22-23","23-24"};

    private Postgres basedato;
    private Vector<Integer> idsmaterias;
    private Vector<String> nombresmaterias;

    private Vector<Integer> aulasids;
    private Vector<Integer> aulasfilas;
    private Vector<String> aulasnombres;
    private Vector<Integer> aulascapacidades;

    private Vector<Vector<Integer>> Lunes,Martes,Miercoles,Jueves,Viernes;

    private JPanel pnTablaDia;

    ResultSet rmateria;

    public ConsultarMateria(){
        super("Programar Final", false, // resizable
                    false, // closable
                    false, // maximizable
                    false);// iconifiable
        this.getContentPane().setLayout(null);
        //this.getContentPane().setBackground(new Color(155,193,232));
        this.getContentPane().add(this.pnlSeleccion());
        this.getContentPane().add(this.pnlTablaMaterias());
        
        int x=10;
        int y=205;
        int w=100;
        int h=35;
        int i=150;
        lunes.setBounds(x, y, w, h);
        martes.setBounds(x+i, y, w, h);
        miercoles.setBounds(x+i*2, y, w, h);
        jueves.setBounds(x+i*3, y, w, h);
        viernes.setBounds(x+i*4, y, w, h);
        parciales.setBounds(x+i*5, y, w, h);
        finales.setBounds(x+i*6, y, w, h);
        this.add(lunes);
        this.add(martes);
        this.add(miercoles);
        this.add(jueves);
        this.add(viernes);
        this.add(parciales);
        this.add(finales);

        limpiar.setEnabled(false);
        filtro.setEnabled(false);
        lunes.setEnabled(false);
        martes.setEnabled(false);
        miercoles.setEnabled(false);
        jueves.setEnabled(false);
        viernes.setEnabled(false);
        parciales.setEnabled(false);
        finales.setEnabled(false);

        this.getContentPane().add(this.pnlTablaDia());
        cargarInicioTablaDia();
        JTabladia.setEnabled(false);
        ocultarBarraTitulo();        

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
        scrlpDia.setBounds(10,20,1280,400);
        //AQUI SE CENTRARIA LOS DTOS PERO LO VOY A VER DESPUS???
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
         pnTablaDia = new JPanel();
        pnTablaDia.setLayout(null);
        pnTablaDia.setBounds(10, 245, 1300, 430);
        pnTablaDia.setBorder(BorderFactory.createTitledBorder("Tabla de ASIGANACIONES DEL DIA"));
        pnTablaDia.setOpaque(false);
        pnTablaDia.add(scrlpDia);
      return pnTablaDia;
    }
    public void ocultarBarraTitulo()
    {
        Barra = ((javax.swing.plaf.basic.BasicInternalFrameUI) getUI()).getNorthPane();
        dimBarra = Barra.getPreferredSize();
        Barra.setSize(0,0);
        Barra.setPreferredSize(new Dimension(0,0));
        repaint();
    }
    public JPanel pnlSeleccion(){
        facultades = new JComboBox(new String[] {"Ciencias Exactas","Ciencias Económicas","Ingenieria","Ciencas Naturales","Ciencias de la Salud","Humanidades"});
            facultades.setBackground(Color.white);
        cuatrimestre = new JComboBox(new String[] {"1°","2°"});
            cuatrimestre.setBackground(Color.white);
        lcuatrimestre = new JLabel("Cuatrimestre");
        año = new JYearChooser();
            año.setBackground(Color.white);
            laño = new JLabel("Año");
        continuar = new JButton("Continuar");
        limpiar = new JButton("Limpiar");
            continuar.setToolTipText("Continuar");
            limpiar.setToolTipText("Limpiar seleccion");
            continuar.addActionListener(this);
            limpiar.addActionListener(this);
            continuar.setBackground(Color.white);
            limpiar.setBackground(Color.white);
        lunes = new JButton("Lunes");
            lunes.setToolTipText("Lunes");
            lunes.addActionListener(this);
            lunes.setBackground(Color.white);
        martes = new JButton("Martes");
            martes.setToolTipText("Martes");
            martes.addActionListener(this);
            martes.setBackground(Color.white);
        miercoles = new JButton("Miercoles");
            miercoles.setToolTipText("Miercoles");
            miercoles.addActionListener(this);
            miercoles.setBackground(Color.white);
        jueves = new JButton("Jueves");
            jueves.setToolTipText("Jueves");
            jueves.addActionListener(this);
            jueves.setBackground(Color.white);
        viernes = new JButton("Viernes");
            viernes.setToolTipText("Viernes");
            viernes.addActionListener(this);
            viernes.setBackground(Color.white);
        parciales = new JButton("Parciales");
            parciales.setToolTipText("Parciales");
            parciales.addActionListener(this);
            parciales.setBackground(Color.white);
        finales = new JButton("Finales");
            finales.setToolTipText("Finales");
            finales.addActionListener(this);
            finales.setBackground(Color.white);
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
        int x = 20;//eje x
        int y = 30;//eje y
        int w = 60;//ancho
        int h = 30;//alto
        int i = 50;//incremento
        facultades.setBounds(x, y-10, w+130, h);
        año.setBounds(x+260, y-8, w, h-5);
        laño.setBounds(x+230, y-10, w+160, h);//nota cambiar la fuente de un label genera RETARDOS
        cuatrimestre.setBounds(x+440, y-10, w, h);
        lcuatrimestre.setBounds(x+360, y-10, w+40, h);
        filtro.setBounds(x+120, y+i, w+300, h);
        Lfiltro.setBounds(x+20, y+i, w+40, h);
        continuar.setBounds(x+150, y+i*2+5, w+40, h+10);
        limpiar.setBounds(x+290, y+i*2+5, w+40, h+10);

        //Lfecha.setFont(new java.awt.Font("Courier", Font.BOLD,14));
        JPanel pnl = new JPanel();
        pnl.setLayout(null);
        pnl.setBounds(5, 5, 600, 195);//posicion y tamaño del panel
        pnl.setBorder(BorderFactory.createTitledBorder(""));
        pnl.setOpaque(false);//defino el fondo
        pnl.add(facultades);
        pnl.add(año);
        pnl.add(laño);
        pnl.add(cuatrimestre);
        pnl.add(lcuatrimestre);
        pnl.add(filtro);
        pnl.add(Lfiltro);
        pnl.add(continuar);
        pnl.add(limpiar);
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
        scrltmaterias.setBounds(10,5,700,175);


        JPanel pnl = new JPanel();

        pnl.setLayout(null);
        pnl.setBounds(610, 5, 720, 195);
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
        //deberia inicilizar la tabla y los botones cada ves que los toque
        //limpio la tabla del dia
        for (int i = 0; i < JTabladia.getRowCount(); i++) {
                                for (int j = 1; j < JTabladia.getColumnCount(); j++){
                            DMTablaDia.setValueAt("", i, j);}
                            }

        Lunes = new Vector<Vector<Integer>>();
        Martes = new Vector<Vector<Integer>>();
        Miercoles = new Vector<Vector<Integer>>();
        Jueves = new Vector<Vector<Integer>>();
        Viernes = new Vector<Vector<Integer>>();

        Vector cl = new Vector<Integer>();
        Vector il = new Vector<Integer>();
        Vector fl = new Vector<Integer>();
        Vector al = new Vector<Integer>();
        Vector cm = new Vector<Integer>();
        Vector im = new Vector<Integer>();
        Vector fm = new Vector<Integer>();
        Vector am = new Vector<Integer>();
        Vector cmi = new Vector<Integer>();
        Vector imi = new Vector<Integer>();
        Vector fmi = new Vector<Integer>();
        Vector ami = new Vector<Integer>();
        Vector cj = new Vector<Integer>();
        Vector ij = new Vector<Integer>();
        Vector fj = new Vector<Integer>();
        Vector aj = new Vector<Integer>();
        Vector cv = new Vector<Integer>();
        Vector iv = new Vector<Integer>();
        Vector fv = new Vector<Integer>();
        Vector av = new Vector<Integer>();

        limpiar.setEnabled(true);
        filtro.setEnabled(false);
        lunes.setEnabled(false);
        martes.setEnabled(false);
        miercoles.setEnabled(false);
        jueves.setEnabled(false);
        viernes.setEnabled(false);
        parciales.setEnabled(false);
        finales.setEnabled(false);
        rmateria=null;
        int row = tablamaterias.rowAtPoint(evt.getPoint());
        if (row >= 0 && tablamaterias.isEnabled())        {

            String materia = tmaterias.getValueAt(row, 0).toString();
            int id = buscarMateria(materia);
            int dia;
            int com;
            int hi;
            int hf;
            int aula;
            basedato = new Postgres();
            basedato.estableceConexion();
            rmateria=basedato.obtenerMateriasConsulta(id);
                try{
                while (rmateria.next())
                {
                    com=rmateria.getInt("comision_numero");;
                    hi=rmateria.getInt("asignacion_horainicio");;
                    hf=rmateria.getInt("asignacion_horafin");;
                    dia=rmateria.getInt("dia_id");
                    aula=rmateria.getInt("aula_id");

                        switch (dia) {
                          case 1:
                               lunes.setEnabled(true);
                               cl.add(com);
                               il.add(hi);
                               fl.add(hf);
                               al.add(aulafila(aula));
                               break;
                          case 2:
                               martes.setEnabled(true);
                               cm.add(com);
                               im.add(hi);
                               fm.add(hf);
                               am.add(aulafila(aula));
                               break;
                          case 3:
                               miercoles.setEnabled(true);
                               cmi.add(com);
                               imi.add(hi);
                               fmi.add(hf);
                               ami.add(aulafila(aula));
                               break;
                          case 4:
                               jueves.setEnabled(true);
                               cj.add(com);
                               ij.add(hi);
                               fj.add(hf);
                               aj.add(aulafila(aula));
                               break;
                          case 5:
                               viernes.setEnabled(true);
                               cv.add(com);
                               iv.add(hi);
                               fv.add(hf);
                               av.add(aulafila(aula));
                               break;
                          }
                }
                } catch(Exception e)
                {
                    System.out.println("Problema al definir los dia de la materia");
                }
            Lunes.add(cl);
            Lunes.add(il);
            Lunes.add(fl);
            Lunes.add(al);

            Martes.add(cm);
            Martes.add(im);
            Martes.add(fm);
            Martes.add(am);

            Miercoles.add(cmi);
            Miercoles.add(imi);
            Miercoles.add(fmi);
            Miercoles.add(ami);

            Jueves.add(cj);
            Jueves.add(ij);
            Jueves.add(fj);
            Jueves.add(aj);

            Viernes.add(cv);
            Viernes.add(iv);
            Viernes.add(fv);
            Viernes.add(av);


            basedato.cierraConexion();

        }
            /*rs = basedato.obtenerMateriasFacultadAñoCuatriemestre(facultades.getSelectedIndex(),año.getValue(),cuatrimestre.getSelectedIndex());
            String materia;
            String jefecatedra;
            
            
        }
        /*
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
        }*/
    }
    public void cargarTablaMaterias(){
         filtro.setText("");
         tablamaterias.getSelectionModel().clearSelection();
        basedato = new Postgres();
        basedato.estableceConexion();
        ResultSet rs = null;
        idsmaterias = new Vector<Integer>();
        nombresmaterias = new Vector<String>();
        rs = basedato.obtenerMateriasFacultadAñoCuatriemestre(facultades.getSelectedIndex(),año.getValue(),cuatrimestre.getSelectedIndex());
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
    public int aulafila(int id){
        int fila=-1;
        int i=0;
        while((i<aulasids.size()) && (fila==-1)){
            if(id==aulasids.elementAt(i)){
                fila=i;
            }
            i++;
        }
        return fila;
    }
    public void actionPerformed(ActionEvent e) {
        Object f = e.getSource();
        if(e.getSource()==continuar){
            cuatrimestre.setEnabled(false);
            año.setEnabled(false);
            facultades.setEnabled(false);
            filtro.setEnabled(true);
            limpiar.setEnabled(true);
            continuar.setEnabled(false);

            int a=año.getValue();//año de la consulta
            int c=cuatrimestre.getSelectedIndex();//cuatriemestre
            int facu=facultades.getSelectedIndex();//facultad donde realizar la consulta
            this.cargarTablaMaterias();
        }
        if(e.getSource()==limpiar){
            cuatrimestre.setEnabled(true);
            año.setEnabled(true);
            facultades.setEnabled(true);
            filtro.setEnabled(false);
            filtro.setText("");
            limpiar.setEnabled(false);
            continuar.setEnabled(true);
            lunes.setEnabled(false);
            martes.setEnabled(false);
            miercoles.setEnabled(false);
            jueves.setEnabled(false);
            viernes.setEnabled(false);
            parciales.setEnabled(false);
            finales.setEnabled(false);
            for (int i = tmaterias.getRowCount()-1; i > -1 ; i--) {
            tmaterias.removeRow(i);
            }
            for (int i = 0; i < JTabladia.getRowCount(); i++) {
                                for (int j = 1; j < JTabladia.getColumnCount(); j++){
                            DMTablaDia.setValueAt("", i, j);}
                            }
        }
        if(e.getSource()==lunes){
            //Con este limpio la tabla
            pnTablaDia.setBorder(javax.swing.BorderFactory.createTitledBorder("Asignacion del dia LUNES"));
            for (int i = 0; i < JTabladia.getRowCount(); i++) {
                                for (int j = 1; j < JTabladia.getColumnCount(); j++){
                            DMTablaDia.setValueAt("", i, j);}
                            }

                for(int j=0;j<Lunes.elementAt(1).size();j++){
                    int col=0;
                    int horainicio=Lunes.elementAt(1).get(j);
                    int horafin=Lunes.elementAt(2).get(j);
                    int com=Lunes.elementAt(0).get(j);
                    int aula=Lunes.elementAt(3).get(j);
                    switch (horainicio) {
                         case 8:
                             col=1;
                             while(horainicio<horafin){
                                 DMTablaDia.setValueAt("Com n° "+com, aula, col);
                                 horainicio++;
                                 col++;
                             }
                            break;
                         case 9:
                             col=2;
                             while(horainicio<horafin){
                                 DMTablaDia.setValueAt("Com n° "+com, aula, col);
                                 horainicio++;
                                 col++;
                             }
                            break;
                         case 10:
                             col=3;
                             while(horainicio<horafin){
                                 DMTablaDia.setValueAt("Com n° "+com, aula, col);
                                 horainicio++;
                                 col++;
                             }
                            break;
                         case 11:
                             col=4;
                             while(horainicio<horafin){
                                 DMTablaDia.setValueAt("Com n° "+com, aula, col);
                                 horainicio++;
                                 col++;
                             }
                            break;
                         case 12:
                             col=5;
                             while(horainicio<horafin){
                                 DMTablaDia.setValueAt("Com n° "+com, aula, col);
                                 horainicio++;
                                 col++;
                             }
                            break;
                         case 13:
                             col=6;
                             while(horainicio<horafin){
                                 DMTablaDia.setValueAt("Com n° "+com, aula, col);
                                 horainicio++;
                                 col++;
                             }
                            break;
                         case 14:
                             col=7;
                             while(horainicio<horafin){
                                 DMTablaDia.setValueAt("Com n° "+com, aula, col);
                                 horainicio++;
                                 col++;
                             }
                            break;
                         case 15:
                             col=8;
                             while(horainicio<horafin){
                                 DMTablaDia.setValueAt("Com n° "+com, aula, col);
                                 horainicio++;
                                 col++;
                             }
                            break;
                         case 16:
                             col=9;
                             while(horainicio<horafin){
                                 DMTablaDia.setValueAt("Com n° "+com, aula, col);
                                 horainicio++;
                                 col++;
                             }
                            break;
                         case 17:
                             col=10;
                             while(horainicio<horafin){
                                 DMTablaDia.setValueAt("Com n° "+com, aula, col);
                                 horainicio++;
                                 col++;
                             }
                            break;
                         case 18:
                             col=11;
                             while(horainicio<horafin){
                                 DMTablaDia.setValueAt("Com n° "+com, aula, col);
                                 horainicio++;
                                 col++;
                             }
                            break;
                         case 19:
                             col=12;
                             while(horainicio<horafin){
                                 DMTablaDia.setValueAt("Com n° "+com, aula, col);
                                 horainicio++;
                                 col++;
                             }
                            break;
                         case 20:
                             col=13;
                             while(horainicio<horafin){
                                 DMTablaDia.setValueAt("Com n° "+com, aula, col);
                                 horainicio++;
                                 col++;
                             }
                            break;
                         case 21:
                             col=14;
                             while(horainicio<horafin){
                                 DMTablaDia.setValueAt("Com n° "+com, aula, col);
                                 horainicio++;
                                 col++;
                             }
                            break;
                         case 22:
                             col=15;
                             while(horainicio<horafin){
                                 DMTablaDia.setValueAt("Com n° "+com, aula, col);
                                 horainicio++;
                                 col++;
                             }
                            break;
                         case 23:
                             col=16;
                             while(horainicio<horafin){
                                 DMTablaDia.setValueAt("Com n° "+com, aula, col);
                                 horainicio++;
                                 col++;
                             }
                            break;
                    }

            }

        }
        if(e.getSource()==martes){
            //Con este limpio la tabla
            pnTablaDia.setBorder(javax.swing.BorderFactory.createTitledBorder("Asignacion del dia MARTES"));
            for (int i = 0; i < JTabladia.getRowCount(); i++) {
                                for (int j = 1; j < JTabladia.getColumnCount(); j++){
                            DMTablaDia.setValueAt("", i, j);}
                            }

                for(int j=0;j<Martes.elementAt(1).size();j++){
                    int col=0;
                    int horainicio=Martes.elementAt(1).get(j);
                    int horafin=Martes.elementAt(2).get(j);
                    int com=Martes.elementAt(0).get(j);
                    int aula=Martes.elementAt(3).get(j);
                    switch (horainicio) {
                         case 8:
                             col=1;
                             while(horainicio<horafin){
                                 DMTablaDia.setValueAt(com, aula, col);
                                 horainicio++;
                                 col++;
                             }
                            break;
                         case 9:
                             col=2;
                             while(horainicio<horafin){
                                 DMTablaDia.setValueAt(com, aula, col);
                                 horainicio++;
                                 col++;
                             }
                            break;
                         case 10:
                             col=3;
                             while(horainicio<horafin){
                                 DMTablaDia.setValueAt(com, aula, col);
                                 horainicio++;
                                 col++;
                             }
                            break;
                         case 11:
                             col=4;
                             while(horainicio<horafin){
                                 DMTablaDia.setValueAt(com, aula, col);
                                 horainicio++;
                                 col++;
                             }
                            break;
                         case 12:
                             col=5;
                             while(horainicio<horafin){
                                 DMTablaDia.setValueAt(com, aula, col);
                                 horainicio++;
                                 col++;
                             }
                            break;
                         case 13:
                             col=6;
                             while(horainicio<horafin){
                                 DMTablaDia.setValueAt(com, aula, col);
                                 horainicio++;
                                 col++;
                             }
                            break;
                         case 14:
                             col=7;
                             while(horainicio<horafin){
                                 DMTablaDia.setValueAt(com, aula, col);
                                 horainicio++;
                                 col++;
                             }
                            break;
                         case 15:
                             col=8;
                             while(horainicio<horafin){
                                 DMTablaDia.setValueAt(com, aula, col);
                                 horainicio++;
                                 col++;
                             }
                            break;
                         case 16:
                             col=9;
                             while(horainicio<horafin){
                                 DMTablaDia.setValueAt(com, aula, col);
                                 horainicio++;
                                 col++;
                             }
                            break;
                         case 17:
                             col=10;
                             while(horainicio<horafin){
                                 DMTablaDia.setValueAt(com, aula, col);
                                 horainicio++;
                                 col++;
                             }
                            break;
                         case 18:
                             col=11;
                             while(horainicio<horafin){
                                 DMTablaDia.setValueAt(com, aula, col);
                                 horainicio++;
                                 col++;
                             }
                            break;
                         case 19:
                             col=12;
                             while(horainicio<horafin){
                                 DMTablaDia.setValueAt(com, aula, col);
                                 horainicio++;
                                 col++;
                             }
                            break;
                         case 20:
                             col=13;
                             while(horainicio<horafin){
                                 DMTablaDia.setValueAt(com, aula, col);
                                 horainicio++;
                                 col++;
                             }
                            break;
                         case 21:
                             col=14;
                             while(horainicio<horafin){
                                 DMTablaDia.setValueAt(com, aula, col);
                                 horainicio++;
                                 col++;
                             }
                            break;
                         case 22:
                             col=15;
                             while(horainicio<horafin){
                                 DMTablaDia.setValueAt(com, aula, col);
                                 horainicio++;
                                 col++;
                             }
                            break;
                         case 23:
                             col=16;
                             while(horainicio<horafin){
                                 DMTablaDia.setValueAt(com, aula, col);
                                 horainicio++;
                                 col++;
                             }
                            break;
                    }

            }

        }
        if(e.getSource()==miercoles){
            //Con este limpio la tabla
            pnTablaDia.setBorder(javax.swing.BorderFactory.createTitledBorder("Asignacion del dia MIERCOLES"));
            for (int i = 0; i < JTabladia.getRowCount(); i++) {
                                for (int j = 1; j < JTabladia.getColumnCount(); j++){
                            DMTablaDia.setValueAt("", i, j);}
                            }

                for(int j=0;j<Miercoles.elementAt(1).size();j++){
                    int col=0;
                    int horainicio=Miercoles.elementAt(1).get(j);
                    int horafin=Miercoles.elementAt(2).get(j);
                    int com=Miercoles.elementAt(0).get(j);
                    int aula=Miercoles.elementAt(3).get(j);
                    switch (horainicio) {
                         case 8:
                             col=1;
                             while(horainicio<horafin){
                                 DMTablaDia.setValueAt(com, aula, col);
                                 horainicio++;
                                 col++;
                             }
                            break;
                         case 9:
                             col=2;
                             while(horainicio<horafin){
                                 DMTablaDia.setValueAt(com, aula, col);
                                 horainicio++;
                                 col++;
                             }
                            break;
                         case 10:
                             col=3;
                             while(horainicio<horafin){
                                 DMTablaDia.setValueAt(com, aula, col);
                                 horainicio++;
                                 col++;
                             }
                            break;
                         case 11:
                             col=4;
                             while(horainicio<horafin){
                                 DMTablaDia.setValueAt(com, aula, col);
                                 horainicio++;
                                 col++;
                             }
                            break;
                         case 12:
                             col=5;
                             while(horainicio<horafin){
                                 DMTablaDia.setValueAt(com, aula, col);
                                 horainicio++;
                                 col++;
                             }
                            break;
                         case 13:
                             col=6;
                             while(horainicio<horafin){
                                 DMTablaDia.setValueAt(com, aula, col);
                                 horainicio++;
                                 col++;
                             }
                            break;
                         case 14:
                             col=7;
                             while(horainicio<horafin){
                                 DMTablaDia.setValueAt(com, aula, col);
                                 horainicio++;
                                 col++;
                             }
                            break;
                         case 15:
                             col=8;
                             while(horainicio<horafin){
                                 DMTablaDia.setValueAt(com, aula, col);
                                 horainicio++;
                                 col++;
                             }
                            break;
                         case 16:
                             col=9;
                             while(horainicio<horafin){
                                 DMTablaDia.setValueAt(com, aula, col);
                                 horainicio++;
                                 col++;
                             }
                            break;
                         case 17:
                             col=10;
                             while(horainicio<horafin){
                                 DMTablaDia.setValueAt(com, aula, col);
                                 horainicio++;
                                 col++;
                             }
                            break;
                         case 18:
                             col=11;
                             while(horainicio<horafin){
                                 DMTablaDia.setValueAt(com, aula, col);
                                 horainicio++;
                                 col++;
                             }
                            break;
                         case 19:
                             col=12;
                             while(horainicio<horafin){
                                 DMTablaDia.setValueAt(com, aula, col);
                                 horainicio++;
                                 col++;
                             }
                            break;
                         case 20:
                             col=13;
                             while(horainicio<horafin){
                                 DMTablaDia.setValueAt(com, aula, col);
                                 horainicio++;
                                 col++;
                             }
                            break;
                         case 21:
                             col=14;
                             while(horainicio<horafin){
                                 DMTablaDia.setValueAt(com, aula, col);
                                 horainicio++;
                                 col++;
                             }
                            break;
                         case 22:
                             col=15;
                             while(horainicio<horafin){
                                 DMTablaDia.setValueAt(com, aula, col);
                                 horainicio++;
                                 col++;
                             }
                            break;
                         case 23:
                             col=16;
                             while(horainicio<horafin){
                                 DMTablaDia.setValueAt(com, aula, col);
                                 horainicio++;
                                 col++;
                             }
                            break;
                    }

            }

        }
        if(e.getSource()==jueves){
            //Con este limpio la tabla
            pnTablaDia.setBorder(javax.swing.BorderFactory.createTitledBorder("Asignacion del dia JUEVES"));
            for (int i = 0; i < JTabladia.getRowCount(); i++) {
                                for (int j = 1; j < JTabladia.getColumnCount(); j++){
                            DMTablaDia.setValueAt("", i, j);}
                            }

                for(int j=0;j<Jueves.elementAt(1).size();j++){
                    int col=0;
                    int horainicio=Jueves.elementAt(1).get(j);
                    int horafin=Jueves.elementAt(2).get(j);
                    int com=Jueves.elementAt(0).get(j);
                    int aula=Jueves.elementAt(3).get(j);
                    switch (horainicio) {
                         case 8:
                             col=1;
                             while(horainicio<horafin){
                                 DMTablaDia.setValueAt(com, aula, col);
                                 horainicio++;
                                 col++;
                             }
                            break;
                         case 9:
                             col=2;
                             while(horainicio<horafin){
                                 DMTablaDia.setValueAt(com, aula, col);
                                 horainicio++;
                                 col++;
                             }
                            break;
                         case 10:
                             col=3;
                             while(horainicio<horafin){
                                 DMTablaDia.setValueAt(com, aula, col);
                                 horainicio++;
                                 col++;
                             }
                            break;
                         case 11:
                             col=4;
                             while(horainicio<horafin){
                                 DMTablaDia.setValueAt(com, aula, col);
                                 horainicio++;
                                 col++;
                             }
                            break;
                         case 12:
                             col=5;
                             while(horainicio<horafin){
                                 DMTablaDia.setValueAt(com, aula, col);
                                 horainicio++;
                                 col++;
                             }
                            break;
                         case 13:
                             col=6;
                             while(horainicio<horafin){
                                 DMTablaDia.setValueAt(com, aula, col);
                                 horainicio++;
                                 col++;
                             }
                            break;
                         case 14:
                             col=7;
                             while(horainicio<horafin){
                                 DMTablaDia.setValueAt(com, aula, col);
                                 horainicio++;
                                 col++;
                             }
                            break;
                         case 15:
                             col=8;
                             while(horainicio<horafin){
                                 DMTablaDia.setValueAt(com, aula, col);
                                 horainicio++;
                                 col++;
                             }
                            break;
                         case 16:
                             col=9;
                             while(horainicio<horafin){
                                 DMTablaDia.setValueAt(com, aula, col);
                                 horainicio++;
                                 col++;
                             }
                            break;
                         case 17:
                             col=10;
                             while(horainicio<horafin){
                                 DMTablaDia.setValueAt(com, aula, col);
                                 horainicio++;
                                 col++;
                             }
                            break;
                         case 18:
                             col=11;
                             while(horainicio<horafin){
                                 DMTablaDia.setValueAt(com, aula, col);
                                 horainicio++;
                                 col++;
                             }
                            break;
                         case 19:
                             col=12;
                             while(horainicio<horafin){
                                 DMTablaDia.setValueAt(com, aula, col);
                                 horainicio++;
                                 col++;
                             }
                            break;
                         case 20:
                             col=13;
                             while(horainicio<horafin){
                                 DMTablaDia.setValueAt(com, aula, col);
                                 horainicio++;
                                 col++;
                             }
                            break;
                         case 21:
                             col=14;
                             while(horainicio<horafin){
                                 DMTablaDia.setValueAt(com, aula, col);
                                 horainicio++;
                                 col++;
                             }
                            break;
                         case 22:
                             col=15;
                             while(horainicio<horafin){
                                 DMTablaDia.setValueAt(com, aula, col);
                                 horainicio++;
                                 col++;
                             }
                            break;
                         case 23:
                             col=16;
                             while(horainicio<horafin){
                                 DMTablaDia.setValueAt(com, aula, col);
                                 horainicio++;
                                 col++;
                             }
                            break;
                    }

            }

        }
        if(e.getSource()==viernes){
            //Con este limpio la tabla
            pnTablaDia.setBorder(javax.swing.BorderFactory.createTitledBorder("Asignacion del dia VIERNES"));
            for (int i = 0; i < JTabladia.getRowCount(); i++) {
                                for (int j = 1; j < JTabladia.getColumnCount(); j++){
                            DMTablaDia.setValueAt("", i, j);}
                            }

                for(int j=0;j<Viernes.elementAt(1).size();j++){
                    int col=0;
                    int horainicio=Viernes.elementAt(1).get(j);
                    int horafin=Viernes.elementAt(2).get(j);
                    int com=Viernes.elementAt(0).get(j);
                    int aula=Viernes.elementAt(3).get(j);
                    switch (horainicio) {
                         case 8:
                             col=1;
                             while(horainicio<horafin){
                                 DMTablaDia.setValueAt(com, aula, col);
                                 horainicio++;
                                 col++;
                             }
                            break;
                         case 9:
                             col=2;
                             while(horainicio<horafin){
                                 DMTablaDia.setValueAt(com, aula, col);
                                 horainicio++;
                                 col++;
                             }
                            break;
                         case 10:
                             col=3;
                             while(horainicio<horafin){
                                 DMTablaDia.setValueAt(com, aula, col);
                                 horainicio++;
                                 col++;
                             }
                            break;
                         case 11:
                             col=4;
                             while(horainicio<horafin){
                                 DMTablaDia.setValueAt(com, aula, col);
                                 horainicio++;
                                 col++;
                             }
                            break;
                         case 12:
                             col=5;
                             while(horainicio<horafin){
                                 DMTablaDia.setValueAt(com, aula, col);
                                 horainicio++;
                                 col++;
                             }
                            break;
                         case 13:
                             col=6;
                             while(horainicio<horafin){
                                 DMTablaDia.setValueAt(com, aula, col);
                                 horainicio++;
                                 col++;
                             }
                            break;
                         case 14:
                             col=7;
                             while(horainicio<horafin){
                                 DMTablaDia.setValueAt(com, aula, col);
                                 horainicio++;
                                 col++;
                             }
                            break;
                         case 15:
                             col=8;
                             while(horainicio<horafin){
                                 DMTablaDia.setValueAt(com, aula, col);
                                 horainicio++;
                                 col++;
                             }
                            break;
                         case 16:
                             col=9;
                             while(horainicio<horafin){
                                 DMTablaDia.setValueAt(com, aula, col);
                                 horainicio++;
                                 col++;
                             }
                            break;
                         case 17:
                             col=10;
                             while(horainicio<horafin){
                                 DMTablaDia.setValueAt(com, aula, col);
                                 horainicio++;
                                 col++;
                             }
                            break;
                         case 18:
                             col=11;
                             while(horainicio<horafin){
                                 DMTablaDia.setValueAt(com, aula, col);
                                 horainicio++;
                                 col++;
                             }
                            break;
                         case 19:
                             col=12;
                             while(horainicio<horafin){
                                 DMTablaDia.setValueAt(com, aula, col);
                                 horainicio++;
                                 col++;
                             }
                            break;
                         case 20:
                             col=13;
                             while(horainicio<horafin){
                                 DMTablaDia.setValueAt(com, aula, col);
                                 horainicio++;
                                 col++;
                             }
                            break;
                         case 21:
                             col=14;
                             while(horainicio<horafin){
                                 DMTablaDia.setValueAt(com, aula, col);
                                 horainicio++;
                                 col++;
                             }
                            break;
                         case 22:
                             col=15;
                             while(horainicio<horafin){
                                 DMTablaDia.setValueAt(com, aula, col);
                                 horainicio++;
                                 col++;
                             }
                            break;
                         case 23:
                             col=16;
                             while(horainicio<horafin){
                                 DMTablaDia.setValueAt(com, aula, col);
                                 horainicio++;
                                 col++;
                             }
                            break;
                    }

            }

        }
    }

}