/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package forme;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import model.Poruka;

/**
 *
 * @author Acer
 */
public class ModelTabelePoruka extends AbstractTableModel{

    List<Poruka> poruke = new ArrayList<>();
    String[] kolone = {"Sifra","Datum i vreme","Posiljalac","Primalac","tekst"};

    public ModelTabelePoruka(List<Poruka> poruke) {
        this.poruke = poruke;
    }

    @Override
    public int getRowCount() {
        return poruke.size();
    }

    @Override
    public int getColumnCount() {
        return kolone.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Poruka poruka = poruke.get(rowIndex);
        switch(columnIndex){
            case 0:
                return poruka.getId();
            case 1:
                return poruka.getDatum();
            case 2:
                return poruka.getPosiljalac().toString();
            case 3:
                return poruka.getPrimalac().toString();
            case 4:
                return poruka.getTekstPoruke().length()>20? 
                        poruka.getTekstPoruke().substring(0,20)+"...":
                        poruka.getTekstPoruke();
            default:
                return "N/A";
        
        }
    
    }

    @Override
    public String getColumnName(int column) {
        return kolone[column];
    }

    public List<Poruka> getPoruke() {
        return poruke;
    }
    
    
    
    
}
