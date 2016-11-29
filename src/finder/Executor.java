package finder;

import java.util.Scanner;

public class Executor {

    public static void main(String[] args) throws Exception {

        Scanner scanner = new Scanner(System.in);
        Classifier classifier = new NBClassifier("corpus.csv");
        classifier.initialize();
        String opcao;

        do {
            log("Escolha uma das opcões:");
            log("(1) Modo Treino");
            log("(2) Modo Teste");
            log("(0) Para Sair");
            opcao = scanner.nextLine();

            switch (opcao){
                case "0":
                    break;
                case "1":
                    classifier.train();
                    break;
                case "2":
                    classifier.test();
                    break;
                default:
                    break;
            }
        } while (!opcao.equals("0"));
    }



    private static void log(String s){
        System.out.println(s);
    }


}
