/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import dbb.DbBroker;
import java.util.List;
import model.Admin;
import model.Poruka;
import model.User;


/**
 *
 * @author Acer
 */
public class Controller {
    private static Controller instance;
    private DbBroker dbb;
    private Admin admin;

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }
    
    private Controller(){
        dbb = new DbBroker();
        admin =null;
    }
    
    public static Controller getInstance(){
        if(instance == null){
            instance = new Controller();
        }
        return instance;
    }

    public boolean logIn(String username, String password) {
        Admin a= dbb.logIn(username,password);
        admin = a;
        
        if(a!=null){
            return true;
        }
        return false;
    }

    public boolean proveriKorisnickoIme(String korisnickoIme) {
        return dbb.proveriKorisnickoIme(korisnickoIme); 
    }

    public boolean unesiKorisnikaUBazu(String korisnickoIme, String password) {
        return dbb.unesiKorisnikaUBazu(korisnickoIme,password); 
    }

    public List<Poruka> vratiPorukeIzBaze(int offset) {
        return dbb.ucitajPorukeIzBaze(offset   );
    }

    public User vratiUseraIzBaze(User u) {
        return dbb.vratiUseraIzBaze(u);
    
    }

    public void posaljiSvima(Poruka poruka) {
         dbb.posaljiSvima(poruka);
    }
    
    
}
