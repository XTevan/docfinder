package finder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Treino {

    private List<String> V;
    private Map<Integer, Double> priori;
    private Map<String, Double> probcond;

    public Treino() {
        this.priori = new HashMap<Integer, Double>();
        this.probcond = new HashMap<String, Double>();
        this.V = new ArrayList<String>();
    }

    public List<String> getV() {
        return V;
    }

    public void setV(List<String> V) {
        this.V = V;
    }  
    

    public Map<Integer, Double> getPriori() {
        return priori;
    }

    public void setPriori(Map<Integer, Double> priori) {
        this.priori = priori;
    }

    public Map<String, Double> getProbcond() {
        return probcond;
    }

    public void setProbcond(Map<String, Double> probcond) {
        this.probcond = probcond;
    }



}
