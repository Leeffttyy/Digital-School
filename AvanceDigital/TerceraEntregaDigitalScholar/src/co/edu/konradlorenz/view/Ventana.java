/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package co.edu.konradlorenz.view;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 *
 * @author Usuario
 */
public class Ventana {
   
    private Scanner sc = new Scanner(System.in);

    public String pedirTexto(String mensaje) throws IllegalArgumentException {
        System.out.println(mensaje);
        String texto = sc.nextLine();
        if (texto == null || texto.isEmpty()) {
            throw new IllegalArgumentException("El texto no puede estar vacío.");
        }
        return texto;
    }
  
    public void mostrarTexto(String txt) {
        System.out.println(txt);
    }
    
    public int pedirEntero(String txt) {
        while (true) {
            try {
                System.out.println(txt);
                int n = sc.nextInt();
                sc.nextLine();
                return n;
            } catch (InputMismatchException e) {
                System.out.println("Dato inválido");
                sc.nextLine();
            }
        }
    }
    
    public double pedirDouble(String txt) {
        while (true) {
            try {
                System.out.println(txt);
                double n = sc.nextDouble();
                sc.nextLine();
                return n;
            } catch(InputMismatchException e) {
                System.out.println("Dato inválido");
                sc.nextLine();
            }
        }
    }
    
}

