/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Interfaz;

import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JDesktopPane;
import javax.swing.JPanel;

public class FondoVentana extends JPanel {

  private Image imagen;

     public FondoVentana() {

       this.setLayout(null);
       this.setToolTipText("UNSa");

  try {
       imagen=ImageIO.read(getClass().getResource("/imagenes/unsa.jpg"));
  }
 catch (IOException e) {
   e.printStackTrace();
  }

  }

 public void paintComponent(Graphics g){

     super.paintComponent(g);
     g.drawImage(imagen, 0, 0, getWidth(), getHeight(), this);
     setOpaque(false);
 }

}