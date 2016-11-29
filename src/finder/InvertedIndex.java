package finder;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InvertedIndex {

    private Map<String, ArrayList<Token>> hash;
    private Map<Integer, Double> hashTamanhoDOC;
    private Map<Integer, String> content;
    private Map<Integer, Integer> classifacaoDoc;
    private Treino treino;
    private Classification cl;

    public InvertedIndex() {
        hash = new LinkedHashMap<>();
        hashTamanhoDOC = new LinkedHashMap<>();
        content = new LinkedHashMap<>();
        classifacaoDoc = new HashMap<Integer, Integer>();
        treino = new Treino();
    }

    public Map<String, ArrayList<Token>> getHash() {
        return hash;
    }

    public Map<Integer, Integer> getClassifacaoDoc() {
        return classifacaoDoc;
    }

    public void putHash(String termo, Token token) {// inserir no hash o termo e um array do objeto Token
        if (!hash.containsKey(termo)) {
            ArrayList<Token> tokens = new ArrayList<>();
            tokens.add(token);
            hash.put(termo, tokens);
        } else {
            hash.get(termo).add(token);
        }
    }

    public void putTamanhoDoc(Integer key, Double valor) { // inserir no hashTamanhoDoc o id do doc e tamanho desse doc
        if (!hashTamanhoDOC.containsKey(key)) {
            hashTamanhoDOC.put(key, valor);
        }
    }

    public void putContent(String content){
        this.content.put(this.content.size()+1,content);
    }
    public void putContent(Integer key, String doc) { // inserir no documento todos os documentos
        if (!content.containsKey(key)) {
            content.put(key, doc);
        }
    }

    public Integer getDbSize(){
        return content.size();
    }

    public Double getTamanho(Integer key) { //retorna o tamanho de um determinado documento
        return hashTamanhoDOC.get(key);
    }

    public ArrayList<Token> getDocuments(String term) {
        if (this.hash.containsKey(term)) {
            return this.hash.get(term);
        } else {
            return null;
        }
    }

    /***
     * Contador de occurences do termo no documento
     * @param term
     * @param doc
     * @return número de occurences
     */
    public int occurences(String term, String doc) {
        int count = 0;
        Pattern pattern = Pattern.compile(term);
        Matcher matcher = pattern.matcher(doc);
        while (matcher.find()) {
            count++;
        }
        return count;
    }

    public int calculoDF(String termo) { // calcula a frequencia do termo no corpus
        return hash.get(termo).size();
    }

    public double docSize(String[] words, String twitt) { // calcula o tamanho do doc
        double tam = 0;
        for (String word : words) {
            tam += Math.pow(this.occurences(word, twitt), 2);
        }
        return Math.sqrt(tam);
    }

    public double termWeight(int tf) {
        return 1 + Math.log(tf);

    }

    public double queryTermWeight(String term, String query) { // calcula o peso da consulta
        int n = hashTamanhoDOC.size();
        double peso = ((1 + Math.log(occurences(term, query))) * Math.log(n / this.calculoDF(term)));
        return peso;
    }
 
    private List<String> extractTokens(int docID) {
        List<String> result = new ArrayList<String>();
        String line = content.get(docID);
        line = line.toLowerCase();
        String textonovo = line.replaceAll("[^a-z]", " ");
        String[] words = textonovo.split(" ");
        for (String x : words) {
            if (x.length() >= 2 && x.length() <= 15) {
                result.add(x);
            }
        }
        return result;
    }

    public List<String> combineWords(List<Classif> a) {
        List<String> result = new ArrayList<String>();
        for (Classif c : a) {
            result.addAll(extractTokens(c.getDocId()));
        }
        return result;
    }

    public Treino multinomialNB(Classification classification) {
        List<String> V = new ArrayList<String>(); //extrai vocabulário de D
        Map<Integer, Double> priori = new HashMap<Integer, Double>();
        Map<String, Double> probcond = new HashMap<String, Double>();
        Map<String, Double> T_c_t; //para salvar o cf de um dado termo em uma dada classe 
        double N = 0.0;

        for (Integer cla : classification.getHash().keySet()) {
            V.addAll(combineWords(classification.getHash().get(cla)));
            N += classification.getHash().get(cla).size();
        }

        //this.log("Valor de N:" + N);
        for (Integer c : classification.getHash().keySet()) { //para cada classe
            //System.out.println("Classe:" + c);/////////////////////
            T_c_t = new HashMap<>();
            double calc = (classification.getHash().get(c).size() / N);
            //System.out.println(classificacao.getHash().get(c).size() + " " + N);
            priori.put(c, calc);
            List<String> words = combineWords(classification.getHash().get(c));
            //System.out.println("Tokens da classe");///////////////////////////
//            for (int i = 0; i < texto.size(); i++) {/////////////////////////////////
//                System.out.println(texto.get(i));
//            }
            //System.out.println("Ocorrencia dos termos");///////////////////////////
            for (String word : V) { //para cada termo em V, conta tokens - CF de cada termo na classe
                //contar quantas vezes o termo t apareceu na classe c
                double frequency = Collections.frequency(words,word);
                //System.out.println("Termo:" + t + " ocorrencia" + q);///////////////////////////////////
                T_c_t.put(word, frequency);
            }
            double soma_cfs = 0.0;
            for (String s : T_c_t.keySet()) { //calculando a soma de todos os cf's
                soma_cfs += T_c_t.get(s);
            }
            for (String t : V) { //para cada termo em D, calcular probcond
                //probcond("termo+classe")= cf do termo na classe + 1 / (cf de todos os termos da classe +1);
                double f = (T_c_t.get(t) + 1.0) / (soma_cfs + 1.0);
                probcond.put(t + c, f);
            }
        }
        Treino treino = new Treino();
        treino.setPriori(priori);
        treino.setProbcond(probcond);
        treino.setV(V);

        this.treino.setPriori(priori);
        this.treino.setProbcond(probcond);
        this.treino.setV(V);

        this.cl = classification;

        return treino;
    }

    public List<String> verSePertence(List<String> procurar) {
        List<String> resposta = new ArrayList<String>();
        for (String s : this.treino.getV()) {
            for (String a : procurar) {
                if (s.equals(a)) {
                    resposta.add(s);
                    break;
                }
            }
        }
        return resposta;
    }

    public int AplicarMultiNomialNB(int docId) { //retorna o ID da classe com maior pontuacao
        //System.out.println("EM APLICARMULTINOMIAL");
        List<String> a = extractTokens(docId);
        List<String> W = verSePertence(a);
        //System.out.println("Palavras em W");///////////////////////////////
//        for (int i = 0; i < W.size(); i++) {////////////////////////////
//            System.out.println(W.get(i));
//        }
        List<Double> pontuacao = new ArrayList<Double>();
        for (Integer c : cl.getHash().keySet()) { //para cada classe c 
            //System.out.println("Classe: " + c);////////////////////////////////
            //System.out.println("Priori: " + treino.getPriori().get(c) + " Log: " + Math.log(treino.getPriori().get(c)));////////////////////////////
            Double valor = new Double(Math.log(treino.getPriori().get(c)));
            for (String t : W) { //para cada termo t em W
                //System.out.println("Termo: "+t);////////////////////////////
                if (treino.getProbcond().containsKey(t+c)) { //percorrendo probcond 
                    valor += Math.log(treino.getProbcond().get(t+c));
                    //System.out.println("Probcond: "+treino.getProbcond().get(t+c)+" Log: "+Math.log(treino.getProbcond().get(t+c)));////////////////
                }
            }
            pontuacao.add(valor);
            //System.out.println("VALOR: "+valor);
        }
        double Maior = pontuacao.get(0);
        int id = 1;
        for (int i = 0; i < pontuacao.size(); i++) {
            //System.out.println("i:"+i+" potuacao:"+pontuacao.get(i));////////////////////////////////
            if (pontuacao.get(i) > Maior) {
                Maior = pontuacao.get(i);
                id = i + 1;//+1 pois as classes começas a ser numeradas com 1, e não com 0
            }
        }
        //System.out.println("Maior: "+Maior + " id:"+id);////////////////////////////////
        return id;
    }

    public void verPriori() {
        for (Integer c : cl.getHash().keySet()) {
            log("Classe ID:" + c + " Priori:" + (treino.getPriori().get(c) * 100) + "%");
        }
    }



    public List<String> cosScore(String[] consulta, int qtd, String consultaS, int classe) {
        Double score,curScore;
        Map<Integer, Double> scores = new LinkedHashMap<>();
        ArrayList<Token> result = new ArrayList<>();
        PriorityQueue<Token> minheap = new PriorityQueue<>();

        List<String> retorno = new ArrayList<String>();
        for (String termo : consulta) {
            ArrayList<Token> tokens = this.getDocuments(termo);
            if (!(tokens == null)) {
                for (Token token : tokens) {
                    curScore = (this.queryTermWeight(termo, consultaS) * this.termWeight(token.getTf()));

                    if ((score = scores.get(token.getDocId())) != null) {
                         curScore += score;
                    }

                    scores.put(token.getDocId(), curScore);
                }
            } else {
                log("Termo: '" +termo + "' não encontrado!");
            }
        }

        for (Integer key : scores.keySet()) { //normalização
            score = scores.get(key) / this.getTamanho(key);
            scores.put(key, score);
        }

        for (Integer key : scores.keySet()) {
            Token t = new Token(key, scores.get(key));
            minheap.add(t);
        }

        if (classe == 0) {
            for (int i = 0; i < qtd && !minheap.isEmpty(); i++) { // qtd seria a quantidade mais relevantes
                result.add(minheap.poll());
            }
        } else {
            for (int i = 0; i < (qtd * 10) && !minheap.isEmpty(); i++) { // qtd seria a quantidade mais relevantes
                result.add(minheap.poll());
            }
        }
        int cont = 0;
        for (Token token : result) {
            if(0 != classe) {
                if (AplicarMultiNomialNB(token.getDocId()) == classe && cont < qtd) {
                    cont++;
                } else if (cont == qtd) {
                    break;
                }
            }
            String s = new String(" DocID:" + token.getDocId() + "," +  " Twitt:" + content.get(token.getDocId()) + "," + " Pontuação:" + token.getScore());
            retorno.add(s);
        }
        return retorno;
    }

    private static void log(String s){
        System.out.println(s);
    }
}
