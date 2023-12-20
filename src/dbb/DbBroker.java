/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dbb;

import model.User;

/**
 *
 * @author Acer
 */
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Admin;
import model.Poruka;

public class DbBroker {

    public Admin logIn(String username, String password) {
        Admin a =null;
        String upit ="SELECT * FROM admin WHERE korisnickoIme=? AND lozinka=?";
        Connection con = Konekcija.getInstance().getConnection();
        try {
            PreparedStatement ps = con.prepareStatement(upit);
            ps.setString(1,username);
            ps.setString(2,password);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                a = new Admin();
                int id = rs.getInt("adminId");
                String ime = rs.getString("ime");
                String prezime = rs.getString("prezime");
                String korisnickoIme = rs.getString("korisnickoIme");
                String loz = rs.getString("lozinka");
                a.setAdminId(id);
                a.setKorisnickoIme(korisnickoIme);
                a.setLozinka(loz);
                a.setIme(ime);
                a.setPrezime(prezime);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(DbBroker.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.println(a);
        return a;
    
    }

    public boolean proveriKorisnickoIme(String korisnickoIme) {
        String upit = "SELECT * FROM user WHERE korisnickoIme='"+korisnickoIme+"'";
        Connection con = Konekcija.getInstance().getConnection();
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(upit); // ako postoji takav red, on je vratio tog usera. 
            if(rs.next()){
                System.out.println("POSTOJI OSOBA SA TIM PODACIMA U BAZI PODATAKA. ");
                return false; //validacija je bezuspesna jer vec postoji takav korisnik, pa onda nece moci da bude unet 
            }else{
                return true; // validacija je uspesna, NE ZABORAVI OVAJ KORAK!!!
            }
        } catch (SQLException ex) {
            ex.getStackTrace();
            System.out.println(ex);
        }
        System.out.println("UHVACEN EXCEPTION U PROVERI KORISNICKO IME METODI");
        return false; //ako red ne posttoji, on nije vratio usera. 
    
    }

    public boolean unesiKorisnikaUBazu(String korisnickoIme, String password) {
        String upit = "INSERT INTO user(korisnickoIme,lozinka) VALUES(?,?)";
        Connection con = Konekcija.getInstance().getConnection(); 
        try {
            PreparedStatement ps = con.prepareStatement(upit,PreparedStatement.RETURN_GENERATED_KEYS);
            
            ps.setString(1,korisnickoIme);
            
            ps.setString(2,password);
            
            int ar  = ps.executeUpdate();
            con.commit();
            return ar>0;
        } catch (SQLException ex) {
            Logger.getLogger(DbBroker.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    
    }

    public List<Poruka> ucitajPorukeIzBaze(int offset) {
        
        /*
        SELECT * FROM poruka p JOIN USER pos ON(p.posiljalac=pos.userId) JOIN USER prim ON(p.primalac=prim.userId); 
        
        */
        
        List<Poruka> poruke = new ArrayList<>();
        String upit = "SELECT * FROM poruka p JOIN USER pos ON(p.posiljalac=pos.userId) JOIN USER prim ON(p.primalac=prim.userId) LIMIT 5 offset "+offset;
        Connection con = Konekcija.getInstance().getConnection();
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(upit);
            while(rs.next()){
                
                Poruka p = new Poruka();
                
                int idPoruke = rs.getInt("p.id");
                p.setId(idPoruke);
                
                int idPosiljaoca = rs.getInt("pos.userId");
                String korisnickoImePos = rs.getString("pos.korisnickoIme");
                String lozinkaPos = rs.getString("pos.lozinka");
                User posiljalac = new User(idPosiljaoca,korisnickoImePos,lozinkaPos);
                p.setPosiljalac(posiljalac);
                
                
                int idPrimaoca = rs.getInt("prim.userId");
                String korisnickoImePrimaoca = rs.getString("prim.korisnickoIme");
                String lozinkaPrimaoca = rs.getString("prim.lozinka");
                User primalac = new User(idPrimaoca,korisnickoImePrimaoca,lozinkaPrimaoca);
                p.setPrimalac(primalac);
                
                
                //DATUM I VREME PORUKE:
                
                Timestamp datumVremeSQL = rs.getTimestamp("p.datumvreme");
                long datumVremeLong = datumVremeSQL.getTime();
                java.util.Date datumVremeUtil = new java.util.Date(datumVremeLong);
                p.setDatum(datumVremeUtil);
                
                
                String text = rs.getString("p.tekst");
                p.setTekstPoruke(text);
                poruke.add(p);
            }   
            
            return poruke;
            
        } catch (SQLException ex) {
            Logger.getLogger(DbBroker.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }

    public User vratiUseraIzBaze(User u) {
        String upit = "SELECT * FROM user WHERE korisnickoIme='"+u.getKorisnickoIme()+"' AND lozinka='"+u.getLozinka()+"'";
        System.out.println(upit);
        Connection con = Konekcija.getInstance().getConnection();
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(upit);
            if(rs.next()){
               int id =  rs.getInt("userId");
               String korisnickoIme = rs.getString("korisnickoIme");
               String lozinka = rs.getString("lozinka");
               User us = new User(id,korisnickoIme,lozinka);
               return us;
            }else{
                return null;
            }
        } catch (SQLException ex) {
            Logger.getLogger(DbBroker.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }

    
    
    
    
    
    public void posaljiSvima(Poruka poruka) {
        String upit = "INSERT INTO poruka(datumvreme,posiljalac,primalac,tekst) VALUES(?,?,?,?)";
        List<User> sviKorisnici = vratiSveKorisnikeIzBaze();
        for(User u:sviKorisnici){
            System.out.println(u.toString());
        }
        Connection con = Konekcija.getInstance().getConnection();
        PreparedStatement ps;
        try {
            ps = con.prepareStatement(upit);
            for(User u:sviKorisnici){
               // ps.setInt(1,poruka.getId());
                Timestamp datTime = new Timestamp(poruka.getDatum().getTime());
                ps.setTimestamp(1, datTime);
                ps.setInt(2,poruka.getPosiljalac().getUserId());
                System.out.println("User id: "+poruka.getPosiljalac().getUserId());
                ps.setInt(3,u.getUserId());
                ps.setString(4, poruka.getTekstPoruke());
                ps.addBatch();
            }
            ps.executeBatch();
            con.commit();
            
            
            
            
        } catch (SQLException ex) {
            Logger.getLogger(DbBroker.class.getName()).log(Level.SEVERE, null, ex);
        }
       
        
        
        
    }
    
    
    

    private List<User> vratiSveKorisnikeIzBaze() {
        List<User> useri = new ArrayList<>();
        String upit = "SELECT * FROM user"; 
        Connection con = Konekcija.getInstance().getConnection();
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(upit);
            while(rs.next()){
                int id = rs.getInt("userId");
                String korisnickoIme = rs.getString("korisnickoIme");
                String lozinka = rs.getString("lozinka");
                User u = new User(id,korisnickoIme,lozinka);
                useri.add(u);
            }
            return useri;
        } catch (SQLException ex) {
            Logger.getLogger(DbBroker.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
}
