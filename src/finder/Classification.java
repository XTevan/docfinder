package finder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Classification {

    private Map<Integer, ArrayList<Classif>> hash;

    public Classification() {
        hash = new HashMap<>();
    }

    public void put(Integer idClasse, Classif c) {
        if (!hash.containsKey(idClasse)) {
            ArrayList<Classif> cl = new ArrayList<Classif>();
            cl.add(c);
            hash.put(idClasse, cl);
        } else {
            hash.get(idClasse).add(c);
        }
    }

    public Map<Integer, ArrayList<Classif>> getHash() {
        return hash;
    }


}
