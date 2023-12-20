/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Acer
 */
public class PokreniServer extends Thread{

    private boolean kraj = false;
    private ServerSocket serverSocket;
    
    @Override
    public void run() {
        try {
            System.out.println("SERVERSKI SOKET JE OTVOREN.");
            serverSocket = new ServerSocket(9000);
            System.out.println("Cekaju se klijenti...");
            
            while(!kraj){
                Socket s = serverSocket.accept(); // jedan klijent je jedna nit, lista ulgoovanih korisnika je lista niti!!!
                System.out.println("Klijent je uspesno povezan.");
            
                ObradaKlijentskihZahteva nit = new ObradaKlijentskihZahteva(s);
                nit.start();
            }
        } catch (IOException ex) {
            Logger.getLogger(PokreniServer.class.getName()).log(Level.SEVERE, null, ex);
        }

 
    
    }
    
    
    public void zaustaviServer(){
        kraj = true;
        try {
            System.out.println("SERVERSKI SOKET JE ZATVOREN!");
            serverSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(PokreniServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
}
