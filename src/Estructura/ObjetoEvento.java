/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Estructura;

/**
 *
 * @author Patito
 */
public class ObjetoEvento {
    private int evento_id;
    private int fila,ColIni;
    private int ColFin;


    public void ObjetoEvento(int e, int f, int ci,int cfin){
        evento_id=e;
        fila=f;

        ColIni=ci;
        ColFin=cfin;
    }
    public int ObtenerEvento(){
        int e=evento_id;
        return e;
    }
    public int ObtenerFila(){
        int e=fila;
        return e;
    }
    public int ObtenerColumnaIni(){
        int e=ColIni;
        return e;
    }
    public int ObtenerColumnaFin(){
        int e=ColFin;
        return e;
    }
    public void mostrarEvento(){
        System.out.print(evento_id);
        System.out.print(fila);
        System.out.print(ColIni);
        System.out.print(ColFin);
        }
}
