package finder;

import java.util.List;
import java.util.Scanner;

/**
 * Created by hilst on 08/11/16.
 */
public class NBClassifier implements Classifier {
    private String dbFilePath;
    private DatabaseFile termSet;
    private Class classes;
    private Query queries;
    private Classification classification;


    public NBClassifier(String dbPath){
        this.dbFilePath = dbPath;
    }

    @Override
    public void initialize(){
        termSet = new CorpusDB();
        classes = new Class();
        queries = new Query();
        classification = new Classification();


        termSet.loadDB(dbFilePath);
    }

    @Override
    public void train(){

        Scanner scanner = new Scanner(System.in);
        log("Modo Treino");
        log("Digite a quantidade de classes:");
        Integer numClasses = scanner.nextInt();
        scanner.nextLine();//skips \n from previous statement
        for (int c = 1; c <= numClasses; c++) {
            log("Digite o nome da classe nº:" +  c);
            classes.addClass(scanner.nextLine());
        }
        log("---Digite 0 para sair!---");

        String term;
        do {
            log("Digite a consulta:(ou 0 para sair)");
            term = scanner.nextLine();
            if (!term.equals("0")) {
                Integer queryId = queries.addQuery(term);
                String[] words = term.split(" ");
                List<String> result = termSet.getIndex().cosScore(words, 10, term, 0);

                for (String s : result) {
                    log(s);
                    log("Informe a classificação:");
                    String c = scanner.nextLine();
                    int classId = classes.getClassId(c);
                    String[] x = s.split(",");
                    Integer idDocumento = Integer.parseInt(x[0].split(":")[1]);
                    Classif classif = new Classif(idDocumento, queryId);
                    classification.put(classId, classif);
                }
            }
        } while (!term.equals("0"));

    }

    @Override
    public void test(){
        Scanner scanner = new Scanner(System.in);
        log("Modo Teste");
        termSet.getIndex().multinomialNB(classification);
        log("Priori");
        termSet.getIndex().verPriori();
        String classe;

        do {
            log("\nClasse:");
            for (Integer i : classes.getHash().keySet()) {
                System.out.println("ID:" + i + " Nome:" + classes.getHash().get(i));
            }
            log("Digite zero(0) para sair");
            log("Digite o ID da classe:");
            classe = scanner.nextLine();
            try {
                if (!classe.equals("0")) {
                    int idClasse = Integer.parseInt(classe);
                    if (!classes.getClassName(idClasse).equals("-1")) {
                        log("Classe: " + classes.getClassName(idClasse));
                        log("Digite a busca");
                        String busca = scanner.nextLine();
                        String[] pesquisa = busca.split(" ");
                        List<String> resultado = termSet.getIndex().cosScore(pesquisa, 10, busca, idClasse);
                        int contador = 0;
                        for (String s : resultado) {
                            log(s);
                            String[] x = s.split(" ");
                            String[] z = x[0].split(":");
                            int id = Integer.parseInt(z[1]);
                            if (idClasse == 1 && id == 1) {
                                contador++;
                            } else if (idClasse == 2 && id == 0) {
                                contador++;
                            }
                        }
                        log("Percentual de acerto: " + (contador * 100) / resultado.size() + "%");
                    } else {
                        log("Classe não encontrada");
                    }
                }
            } catch (Exception e) {
                log(e.getMessage());
            }
        } while (!classe.equals("0"));
    }

    private static void log(String s){
        System.out.println(s);
    }
}
