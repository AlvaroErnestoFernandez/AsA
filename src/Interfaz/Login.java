

package Interfaz;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.*;
import Estructura.*;


public class Login extends JFrame implements ActionListener, KeyListener{
	private JTextField txtusuario;
	private JPasswordField txtpassword;
	private JButton btnAceptar, btnCancelar;
	private JLabel lblusuario, lblpassword;
        private FondoVentana fondo;
        Postgres basedato;

	public Login()
	{
            super("AsA-Login");
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
            //this.getContentPane().add(this.pnlImagen());
            this.getContentPane().add(this.pnlLogin());
            this.getContentPane().setBackground(Color.WHITE);
            this.setSize(350,350);
            setLocationRelativeTo(this.getParent());
            Image icon = Toolkit.getDefaultToolkit().getImage(getClass().getResource("logoUNSa.png"));
            setIconImage(icon);
            //this.setUndecorated(true);

            this.setResizable(false);
            this.setVisible(true);
               
        }
        public JPanel pnlLogin(){
        //icio los jlabel

        lblusuario = new JLabel("Usuario");
        lblpassword = new JLabel("Contraseña");
        txtusuario = new JTextField();
        txtusuario.addKeyListener(this);
        txtpassword = new JPasswordField();
        txtpassword.addKeyListener(this);
        btnAceptar = new JButton("Aceptar");
        btnCancelar = new JButton("Cancelar");
        btnAceptar.addActionListener(this);
        btnCancelar.addActionListener(this);
       int x = 20;//eje x
       int y = 45;//eje y
       int w = 170;//ancho
       int h = 35;//alto
       int i = 40;//incremento
       lblusuario.setBounds(x, y, w, h);
       txtusuario.setBounds(x+125, y, w, h);
       i=i+40;

       lblpassword .setBounds(x,y+i,w,h);
       txtpassword.setBounds(x+125, y+i, w, h);
       i=i+80;
       x=x+20;
       btnAceptar.setBounds(x, y+i, 100, h);
       btnCancelar.setBounds(x+150, y+i, 100, h);

       JPanel JPmateria = new JPanel();
       JPmateria.setLayout(null);
       JPmateria.setBounds(1, 1, 350, 350);//posicion y tamaño del panel
       JPmateria.setBorder(null);
       JPmateria.setOpaque(false);
       JPmateria.add(lblusuario);
       JPmateria.add(txtusuario);
       JPmateria.add(lblpassword);
       JPmateria.add(txtpassword);
       JPmateria.add(btnAceptar);
       JPmateria.add(btnCancelar);
       return JPmateria;
    }
        public FondoVentana pnlImagen(){
          fondo = new FondoVentana();
          this.add(fondo);
          fondo.setBounds(70, 1,182, 286);
          fondo.setOpaque(false);//defino el fondo
          return fondo;
        }

        public void actionPerformed(ActionEvent g) {
        Object f = g.getSource();
            if(f.equals(btnAceptar))
            {
                String usuario,clave;
                usuario = txtusuario.getText();
		clave = txtpassword.getText();
                basedato = new Postgres();
                basedato.estableceConexion();
                int b = basedato.verificarLogin(usuario, clave);
                basedato.cierraConexion();
                if(b>0){
                    new Principal(b,usuario);
                    dispose();
                }else{
                    JOptionPane.showMessageDialog( Login.this,
								"Usuario inactivo o contraseña incorrecta, comuníquese con el administrador",
								"AsA-Error", JOptionPane.ERROR_MESSAGE );
                }
            }
            if(f.equals(btnCancelar))
            {
                System.exit(0);
            }
        }
        public static void main(String[] args) {

       try{

            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        new Login();


    }

    public void keyTyped(KeyEvent e) {
        //System.out.println(e.getKeyCode());//no puedo obtener el codigo aqui
        //Este método se ejecuta cada vez que se presiona y se suelta una tecla.
    }

    public void keyPressed(KeyEvent arg0) {
        if(arg0.getKeyCode()==10)
            {
                String usuario,clave;
                usuario = txtusuario.getText();
		clave = txtpassword.getText();
                basedato = new Postgres();
                basedato.estableceConexion();
                int b = basedato.verificarLogin(usuario, clave);
                basedato.cierraConexion();
                if(b>0){
                    new Principal(b,usuario);
                    dispose();
                }else{
                    JOptionPane.showMessageDialog( Login.this,
								"Usuario inactivo o contraseña incorrecta, comuníquese con el administrador",
								"AsA-Error", JOptionPane.ERROR_MESSAGE );
                }
            }
    }//Este método se ejecuta cada vez que presionas una tecla.

    public void keyReleased(KeyEvent arg0) {
        //Este método se ejecuta cada vez que sueltas una tecla.
    }
}