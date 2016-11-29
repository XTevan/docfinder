package finder;

import java.util.HashMap;
import java.util.Map;

public class Query {
    private Map<Integer, String> hash;
    private int id;

    public Query(){
        hash = new HashMap<Integer,String>();
        id = 1;
    }

    public Map<Integer, String> getHash() {
        return hash;
    }
    
    public int addQuery(String query){
        int aux = id;
        id++;
        hash.put(aux, query);
        return aux;
    }
    
}
