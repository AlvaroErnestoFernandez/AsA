/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Interfaz;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.awt.*;
import javax.swing.table.*;
import java.util.HashMap;

import Estructura.Postgres;
import java.sql.ResultSet;
import java.awt.*;
import java.awt.event.*;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import org.jvnet.substance.SubstanceLookAndFeel;
import org.jvnet.substance.watermark.SubstanceImageWatermark;





public class Principal extends JFrame implements ActionListener{

    private JDesktopPane jdpDesktop;
    private AgregarMaterias agregarmaterias;
    private AgregarAulas agregaraulas;
    private AsignacionAutomatica asigautomatica;
    private AsignacionManual asigmanual;
    private BorrarAsignaciones borrarasignaciones;
    private ConsultaDia consultadiac;
    private ProgramarParcial programarparcial;
    private ProgramarFinal programarfinal;
    private ProgramarCursosOtros programarcursos;
    private ConsultarMateria consultarmateria;
    private AdministrarUsuarios administrarusuarios;
    private JMenuBar barramenu;
    private JMenu cargar,inicio,asignacion,consulta,acercade,ayuda,eventos,administrar;
    private JMenuItem cargarmaterias,cargaraulas,iniciosalir,asignacionautomatica,asignacionmanual,consultadia,consultamateria,consultaaula,borrarasignacion,eventoparcial,eventofinal,eventocurso,eventootro,usuarios;
    
    private Postgres basedato;

    private int tipousuario;
    private String usuario;

    public Principal(Integer tipousuario,String usuario){

        super("AsA");

        this.tipousuario=tipousuario;
        this.usuario=usuario;
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);//MAXIMIZA VENTANA
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // A specialized layered pane to be used with JInternalFrames
        jdpDesktop = new JDesktopPane() {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(800, 800);
            }
        };
        this.setContentPane(jdpDesktop);
        this.setJMenuBar(createMenuBar());
        jdpDesktop.putClientProperty("JDesktopPane.dragMode", "outline");
        this.pack();
                Image icon = Toolkit.getDefaultToolkit().getImage(getClass().getResource("logoUNSa.png"));
               setIconImage(icon);

        if(tipousuario==3){
                crearConsultaDia();
                crearConsultarMateria();
            }
        else{
            crearAgregarAulas();
            crearAgregarMaterias();
            crearAsignacionAutomatica();
            crearAsignacionManual();
            crearBorrarAsignaciones();
            crearConsultaDia();
            crearProgramarParcial();
            crearProgramarFinal();
            crearProgramarCursos();
            crearConsultarMateria();
            crearAdministrarUsuarios(tipousuario,usuario);
        }
        ocultarTodo();
        this.setVisible(true);
    }

    public void actionPerformed(ActionEvent e){//nota quizas sea mas eficiente la interfaz si en lugar de ocular todo de una hago una ocultacion selectiva
        Container f=this.getContentPane();
        if(e.getSource()==cargarmaterias){
            if(!agregarmaterias.isVisible()){
                ocultarTodo();
                agregarmaterias.setVisible(true);
                this.setTitle("AsA-Cargar Materias");
            }
        }
        if(e.getSource()==iniciosalir){
            System.exit(0);
        }
        if(e.getSource()==inicio){//aqui estoy queriendo ver el fondo inicial
            ocultarTodo();
        }
        if(e.getSource()==cargaraulas){
            if(!agregaraulas.isVisible()){
                ocultarTodo();
                agregaraulas.setVisible(true);
                this.setTitle("AsA-Cargar Aulas");
            }
        }
        if(e.getSource()==asignacionautomatica){
            if(!asigautomatica.isVisible()){
                ocultarTodo();
                asigautomatica.cargarTablaMateriasNoAsigndas();
                asigautomatica.setVisible(true);
                this.setTitle("AsA-Asignacion Automatica");
            }
        }
        if(e.getSource()==asignacionmanual){
            if(!asigmanual.isVisible()){
                ocultarTodo();
                asigmanual.cargarTablaMaterias();
                asigmanual.setVisible(true);
                this.setTitle("AsA-Asignacion Manual");
            }
        }
        if(e.getSource()==borrarasignacion){
            if(!borrarasignaciones.isVisible()){
                ocultarTodo();
                //borrarasignaciones.cargarTablaMaterias();
                borrarasignaciones.setVisible(true);
                this.setTitle("AsA-Borrar Asignacion");
            }
        }
        if(e.getSource()==consultadia){
            if(!consultadiac.isVisible()){
                ocultarTodo();
                //borrarasignaciones.cargarTablaMaterias();
                consultadiac.setVisible(true);
                this.setTitle("AsA-Consultar Dia");
            }
        }
        if(e.getSource()==eventoparcial){
            if(!programarparcial.isVisible()){
                ocultarTodo();
                //borrarasignaciones.cargarTablaMaterias();
                programarparcial.cargarTablaMaterias();
                programarparcial.setVisible(true);
                this.setTitle("AsA-Evento Parcial");
            }
        }
        if(e.getSource()==eventofinal){
            if(!programarfinal.isVisible()){
                ocultarTodo();
                programarfinal.setVisible(true);
                this.setTitle("AsA-Evento Final");
            }
        }
        if(e.getSource()==eventocurso){
            if(!programarcursos.isVisible()){
                ocultarTodo();
                programarcursos.setVisible(true);
                this.setTitle("AsA-Evento Curso u Otro");
            }
        }
        if(e.getSource()==consultamateria){
            if(!consultarmateria.isVisible()){
                ocultarTodo();
                consultarmateria.setVisible(true);
                this.setTitle("AsA-Consultar Materia");
            }
        }
        if(e.getSource()==usuarios){
            if(!administrarusuarios.isVisible()){
                ocultarTodo();
                administrarusuarios.setVisible(true);
                this.setTitle("AsA-Consultar Materia");
            }
        }
    }

    protected JMenuBar createMenuBar() {
        barramenu = new JMenuBar();
        //menu inicio
        inicio = new JMenu("Inicio");
        iniciosalir = new JMenuItem("Salir");
        iniciosalir.setMnemonic(KeyEvent.VK_N);
        iniciosalir.addActionListener(this);
        inicio.add(iniciosalir);
 
        barramenu.add(inicio);
        if((tipousuario==1) || (tipousuario==2)){
                //menu cargar
                cargar = new JMenu("Cargar");
                cargar.setMnemonic(KeyEvent.VK_N);

                cargaraulas = new JMenuItem("Cargar Aulas");
                cargaraulas.setMnemonic(KeyEvent.VK_N);
                cargaraulas.addActionListener(this);
                cargar.add(cargaraulas);

                cargarmaterias = new JMenuItem("Cargar Materias");
                cargarmaterias.setMnemonic(KeyEvent.VK_N);
                cargarmaterias.addActionListener(this);
                cargar.add(cargarmaterias);
                barramenu.add(cargar);

                //menu asignacion
                asignacion = new JMenu("Asignacion");
                asignacion.setMnemonic(KeyEvent.VK_N);

                asignacionautomatica = new JMenuItem("Realizar Asignacion Automatica");
                asignacionautomatica.setMnemonic(KeyEvent.VK_N);
                asignacionautomatica.addActionListener(this);
                asignacion.add(asignacionautomatica);

                asignacionmanual = new JMenuItem("Realizar Asignaicon Manual");
                asignacionmanual.setMnemonic(KeyEvent.VK_N);
                asignacionmanual.addActionListener(this);
                asignacion.add(asignacionmanual);

                borrarasignacion = new JMenuItem("Borrar Asignacion");
                borrarasignacion.setMnemonic(KeyEvent.VK_N);
                borrarasignacion.addActionListener(this);
                asignacion.add(borrarasignacion);

                barramenu.add(asignacion);

                //menu consulta
                consulta = new JMenu("Consultas");
                consulta.setMnemonic(KeyEvent.VK_N);

                consultadia = new JMenuItem("Realizar Consulta de un Dia");
                consultadia.setMnemonic(KeyEvent.VK_N);
                consultadia.addActionListener(this);
                consulta.add(consultadia);

                consultamateria = new JMenuItem("Realizar Consulta de una Materia");
                consultamateria.setMnemonic(KeyEvent.VK_N);
                consultamateria.addActionListener(this);
                consulta.add(consultamateria);
                barramenu.add(consulta);
                //menu envento
                eventos = new JMenu("Eventos");
                eventos.setMnemonic(KeyEvent.VK_N);

                eventoparcial = new JMenuItem("Programar Parcial");
                eventoparcial.setMnemonic(KeyEvent.VK_N);
                eventoparcial.addActionListener(this);
                eventos.add(eventoparcial);

                eventofinal = new JMenuItem("Programar Finales");
                eventofinal.setMnemonic(KeyEvent.VK_N);
                eventofinal.addActionListener(this);
                eventos.add(eventofinal);

                eventocurso = new JMenuItem("Cursos u Otros");
                eventocurso.setMnemonic(KeyEvent.VK_N);
                eventocurso.addActionListener(this);
                eventos.add(eventocurso);
                barramenu.add(eventos);

                administrar = new JMenu("Administrar");
                administrar.setMnemonic(KeyEvent.VK_N);

                usuarios = new JMenuItem("Usuarios");
                usuarios.setMnemonic(KeyEvent.VK_N);
                usuarios.addActionListener(this);
                administrar.add(usuarios);
                barramenu.add(administrar);

        }else{
            if(tipousuario==3){
                //menu consulta
                consulta = new JMenu("Consultas");
                consulta.setMnemonic(KeyEvent.VK_N);

                consultadia = new JMenuItem("Realizar Consulta de un Dia");
                consultadia.setMnemonic(KeyEvent.VK_N);
                consultadia.addActionListener(this);
                consulta.add(consultadia);

                consultamateria = new JMenuItem("Realizar Consulta de una Materia");
                consultamateria.setMnemonic(KeyEvent.VK_N);
                consultamateria.addActionListener(this);
                consulta.add(consultamateria);
                barramenu.add(consulta);
            }
        }
        //menu ayuda
        ayuda = new JMenu("Ayuda");
        ayuda.setMnemonic(KeyEvent.VK_N);
        barramenu.add(ayuda);

        //menu acerca de
        acercade = new JMenu("Acerca de..");
        acercade.setMnemonic(KeyEvent.VK_N);
        barramenu.add(acercade);

        return barramenu;
    }

    protected void crearAgregarMaterias() {
        agregarmaterias = new AgregarMaterias();
        agregarmaterias.setVisible(false);
        // Every JInternalFrame must be added to content pane using JDesktopPane
        jdpDesktop.add(agregarmaterias);
        try {
            agregarmaterias.setMaximum(true);
            agregarmaterias.setSelected(true);
        } catch (java.beans.PropertyVetoException e) {
        }
        //agregarmaterias.ocultarBarraTitulo();
    }

    protected void crearAgregarAulas() {
        agregaraulas = new AgregarAulas();
        agregaraulas.setVisible(false);
        // Every JInternalFrame must be added to content pane using JDesktopPane
        jdpDesktop.add(agregaraulas);
        try {
            agregaraulas.setMaximum(true);
            agregaraulas.setSelected(true);
        } catch (java.beans.PropertyVetoException e) {
        }
        agregaraulas.ocultarBarraTitulo();
    }
    protected void crearConsultarMateria() {
        consultarmateria = new ConsultarMateria();
        jdpDesktop.add(consultarmateria);
        try {
            consultarmateria.setMaximum(true);
            consultarmateria.setSelected(true);
        } catch (java.beans.PropertyVetoException e) {
        }
    }

    protected void crearAdministrarUsuarios(Integer tipousuario,String usuario) {
        administrarusuarios = new AdministrarUsuarios(tipousuario,usuario);
        jdpDesktop.add(administrarusuarios);
        try {
            administrarusuarios.setMaximum(true);
            administrarusuarios.setSelected(true);
        } catch (java.beans.PropertyVetoException e) {
        }
    }

    public void ocultarTodo(){
        if((tipousuario==1)||(tipousuario==2)){
        asigmanual.setVisible(false);
        agregaraulas.setVisible(false);
        agregarmaterias.setVisible(false);
        asigautomatica.setVisible(false);
        borrarasignaciones.setVisible(false);
        consultadiac.setVisible(false);
        programarparcial.setVisible(false);
        programarfinal.setVisible(false);
        programarcursos.setVisible(false);
        consultarmateria.setVisible(false);
        administrarusuarios.setVisible(false);
        }else{
            if(tipousuario==3){
                consultadiac.setVisible(false);
                consultarmateria.setVisible(false);
            }
        }
    }
    protected void crearAsignacionAutomatica() {
        asigautomatica = new AsignacionAutomatica();
        asigautomatica.setVisible(false);
        // Every JInternalFrame must be added to content pane using JDesktopPane
        jdpDesktop.add(asigautomatica);
        try {
            asigautomatica.setMaximum(true);
            asigautomatica.setSelected(true);
        } catch (java.beans.PropertyVetoException e) {
        }
        asigautomatica.ocultarBarraTitulo();
    }

    protected void crearAsignacionManual() {
        asigmanual = new AsignacionManual();
        asigmanual.setVisible(false);
        // Every JInternalFrame must be added to content pane using JDesktopPane
        jdpDesktop.add(asigmanual);
        try {
            asigmanual.setMaximum(true);
            asigmanual.setSelected(true);
        } catch (java.beans.PropertyVetoException e) {
        }
        asigmanual.ocultarBarraTitulo();
    }
     protected void crearBorrarAsignaciones() {
        borrarasignaciones = new BorrarAsignaciones();
        borrarasignaciones.setVisible(false);
        // Every JInternalFrame must be added to content pane using JDesktopPane
        jdpDesktop.add(borrarasignaciones);
        try {
            borrarasignaciones.setMaximum(true);
            borrarasignaciones.setSelected(true);
        } catch (java.beans.PropertyVetoException e) {
        }
        borrarasignaciones.ocultarBarraTitulo();
    }
     protected void crearConsultaDia() {
        consultadiac = new ConsultaDia();
        consultadiac.setVisible(false);
        // Every JInternalFrame must be added to content pane using JDesktopPane
        jdpDesktop.add(consultadiac);
        try {
            consultadiac.setMaximum(true);
            consultadiac.setSelected(true);
        } catch (java.beans.PropertyVetoException e) {
        }
        consultadiac.ocultarBarraTitulo();
    }
    protected void crearProgramarParcial() {
        programarparcial = new ProgramarParcial();
        programarparcial.setVisible(false);
        // Every JInternalFrame must be added to content pane using JDesktopPane
        jdpDesktop.add(programarparcial);
        try {
            programarparcial.setMaximum(true);
            programarparcial.setSelected(true);
        } catch (java.beans.PropertyVetoException e) {
        }
        programarparcial.ocultarBarraTitulo();
    }

    protected void crearProgramarFinal() {
        programarfinal = new ProgramarFinal();
        programarfinal.setVisible(false);
        // Every JInternalFrame must be added to content pane using JDesktopPane
        jdpDesktop.add(programarfinal);
        try {
            programarfinal.setMaximum(true);
            programarfinal.setSelected(true);
        } catch (java.beans.PropertyVetoException e) {
        }
    }
    protected void crearProgramarCursos() {
        programarcursos = new ProgramarCursosOtros();
        jdpDesktop.add(programarcursos);
        try {
            programarcursos.setMaximum(true);
            programarcursos.setSelected(true);
        } catch (java.beans.PropertyVetoException e) {
        }
    }

    /*public static void main(String[] args) {

       // JFrame.setDefaultLookAndFeelDecorated(true);
        //JDialog.setDefaultLookAndFeelDecorated(true);

       try{

            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        new Principal();
        

    }*/
}
