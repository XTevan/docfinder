package finder;

import java.util.HashMap;
import java.util.Map;

public class Class {

    private Map<Integer, String> hash;
    private int id;

    public Class() {
        hash = new HashMap<Integer, String>();
        id = 1;
    }

    public Map<Integer, String> getHash() {
        return hash;
    }
    
    public void addClass(String newClass){
        hash.put(id, newClass.toUpperCase());
        id++;
    }

    public int getClassId(String classe){
          int x=0;
        classe = classe.toUpperCase();
        for(Integer i: hash.keySet()){
            if(hash.get(i).equals(classe)){
                x = i;
                break;
            }
        }
        return x;
    }
    
    public String getClassName(int id){
        if(hash.containsKey(id)){
            return hash.get(id);
        } else{
            return "-1";
        }
    }
    
}
