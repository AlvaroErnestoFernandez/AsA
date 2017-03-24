
package Interfaz;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.awt.*;
import javax.swing.table.*;
import java.util.HashMap;

import Estructura.Postgres;
import java.sql.ResultSet;
//IMPORTO QUIZAS DE MAS PERO DESPUES LO DEPURO
//PARA FILTRAR TABLA MATERIA
import java.awt.Dimension;
import java.awt.GridLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class AsignacionManual extends JInternalFrame implements ActionListener{
    private JTable tablamaterias;
    private JScrollPane scrlptmaterias;//scroll tabla
    private DefaultTableModel tmaterias;
    private final String [] colsmaterias = {"Materia","Facultad","Jefe de Catedra","Comisiones"};
    private Postgres basedato;
    //elementos para ocultar barra de titulo
    private JComponent Barra = ((javax.swing.plaf.basic.BasicInternalFrameUI) getUI()).getNorthPane();
    private Dimension dimBarra = null;
    //elementos  para filtrar tabla
    private JTextField filtro;
    private JLabel buscar,ayudausuario;
    private JPanel pnlasigmanual,pnlfiltro;
    TableRowSorter<DefaultTableModel> sorter;
    RowFilter<DefaultTableModel, Object> rf = null;
    //vector de panel
    private Vector <JPanel> vectorpanels;
    private JScrollPane scrollasimanual;

    public AsignacionManual(){
        super("Asignacion Manual", false, // resizable
                    false, // closable
                    false, // maximizable
                    false);// iconifiable
        this.getContentPane().setLayout(null);
        //this.getContentPane().setBackground(new Color(155,193,232));
        //AGREGO UN SCROLL AL PANEL POR QUE NOSE BIEN CUAL ES LA CONTIDAD FIJA DE COMISIONES
        scrollasimanual = new JScrollPane(this.pnlasignacionManual());
        scrollasimanual.setBounds(5, 90, 590, 560);
        //scrollasimanual.getViewport().setBackground(new Color(155,193,232));
        scrollasimanual.getViewport().setBackground(Color.WHITE);
        //AGREGO EL SCROLL A LA VENTANA PRINCIPAL
        this.getContentPane().add(scrollasimanual);
        //
        this.getContentPane().add(this.pnlfiltro());
        this.getContentPane().add(this.pnlTablaMaterias());
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
        scrlptmaterias.setBounds(10,20,700,620);


        JPanel pnl = new JPanel();

        pnl.setLayout(null);
        pnl.setBounds(600, 5, 720, 650);
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

        pnlasigmanual.removeAll();
        ayudausuario.setText("");
        JLabel materia= new JLabel();
        materia.setBounds(1, 1, 300, 20);
        int idmateria=-1;
        
                    basedato = new Postgres();
                    basedato.estableceConexion();

                    int row = tablamaterias.rowAtPoint(evt.getPoint());
                        if (row >= 0 && tablamaterias.isEnabled())
                        {
                              
                              idmateria = basedato.obtnerIdMateria(tablamaterias.getValueAt(row, 0).toString(),tablamaterias.getValueAt(row, 2).toString(),tablamaterias.getValueAt(row, 1).toString());
                              materia.setFont(new java.awt.Font("Arial", 0, 22));
                              materia.setText(tablamaterias.getValueAt(row, 0).toString());
                        }
                    basedato.cierraConexion();
        Vector<SubAsignacionManual> v = new Vector<SubAsignacionManual>();
        basedato = new Postgres();
        basedato.estableceConexion();
        ResultSet rs = null;
        rs = basedato.obtenerComisionesMateria(idmateria);
                    String tipo;
                    String ncomision;
                    String box;
                    String responsable;
                    String capacidad;
                    String idcomision;
        int y =10;
        try{
        while (rs.next())
        {
            pnlasigmanual.setPreferredSize(new Dimension(500, y+200));
            idcomision=rs.getString(1);
            ncomision = rs.getString (2);
            responsable = rs.getString (3);
            box = rs.getString(4);
            tipo = rs.getString(5);
            capacidad = rs.getString(7);
            SubAsignacionManual lb1 = new SubAsignacionManual(Integer.parseInt(idcomision),Integer.parseInt(ncomision),tipo,Integer.parseInt(capacidad),responsable,box,y);
            v.add(lb1);
            y=y+145;

        }

        } catch(Exception e)
        {
            System.out.println("Problema al cargar el vector");
        }
        basedato.cierraConexion();
        for (int i=0;i<v.size();i++){
        pnlasigmanual.add(v.elementAt(i));
        }
        pnlasigmanual.add(materia);
        //pnlasigmanual.setPreferredSize(new Dimension(500, 550));//modifico el tamaño del panel
        pnlasigmanual.validate();
        pnlasigmanual.updateUI();
        scrlptmaterias.validate();
        scrlptmaterias.updateUI();
    }
    public JPanel pnlfiltro(){
       //filtro = new JTextField();
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
       buscar = new JLabel("Filtrar Materias");
       ayudausuario = new JLabel("Para asignar a un materia un aula primero debe seleccionar la materia en la tabla");
       int x = 50;//eje x
       int y = 30;//eje y
       int w = 400;//ancho
       int h = 30;//alto
       int i = 40;//incremento
       filtro.setBounds(x+i*3, y, w, h);
       buscar.setBounds(x, y, w-200, h);
       ayudausuario.setBounds(x, y+20, 500, h);
       pnlfiltro = new JPanel();
       pnlfiltro.setLayout(null);
       pnlfiltro.setBounds(5, 5, 590, 120);//posicion y tamaño del panel
       pnlfiltro.setOpaque(false);
       //pnlasigmanual.setBorder(BorderFactory.createTitledBorder("Materia por ASIGNAR"));
       pnlfiltro.add(filtro);
       buscar.setFont(new java.awt.Font("Arial", 0, 16));
       pnlfiltro.add(buscar);
       pnlfiltro.add(ayudausuario);

       return pnlfiltro;
    }
    public JPanel pnlasignacionManual(){
        pnlasigmanual=new JPanel();
        pnlasigmanual.setLayout(null);
        pnlasigmanual.setPreferredSize(new Dimension(500, 550));
        pnlasigmanual.setOpaque(false);
        //pnlasigmanual.setBorder(BorderFactory.createTitledBorder("Comisiones"));

        return pnlasigmanual;
    }

    public void ocultarBarraTitulo()
    {
        Barra = ((javax.swing.plaf.basic.BasicInternalFrameUI) getUI()).getNorthPane();
        dimBarra = Barra.getPreferredSize();
        Barra.setSize(0,0);
        Barra.setPreferredSize(new Dimension(0,0));
        repaint();
    }
    public void actionPerformed(ActionEvent arg0) {

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
            cant=basedato2.cantidadComisiones(id);
            tmaterias.addRow(new Object[]{materia,sfacultad,jefecatedra,cant});
        }

        } catch(Exception e)
        {
            System.out.println("Problema al cargar la tabla materia ");
        }
        basedato.cierraConexion();
    }

}
