/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Estructura;

public class ObjetoMateria {
    private int materia_id;
    private int fila,ColIni;
    private int ColFin;

    public void ObjetoMateria(int m, int f, int ci,int cfin){
        materia_id=m;
        fila=f;
        ColIni=ci;
        ColFin=cfin;
    }
    public int ObtenerComiMat(){
        int c=materia_id;
        return c;
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
    public void mostrarMateria(){
        System.out.println("comision"+materia_id);
        System.out.println("Fila"+fila);
        System.out.println("ColIni"+ColIni);
        System.out.println("ColFin"+ColFin);
        }
}
