package finder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class CorpusDB implements DatabaseFile {

    @Override
    public InvertedIndex getIndex() {
        return index;
    }

    private InvertedIndex index;

    public CorpusDB() {
        index = new InvertedIndex();
    }

    @Override
    public void loadDB(String dbPath){
        String linha;
        try(BufferedReader arquivo = new BufferedReader(new FileReader(new File(dbPath)))) {
            arquivo.readLine();// ignora header

            while ((linha = arquivo.readLine()) != null /*&& index.getDbSize() < 10000*/) {
                String[] s = linha.split(",");
                int id = Integer.parseInt(s[0]); // DocId
                if(id % 1000 == 0)
                    log(id + " records loaded.");

                String texto = "";
                for (int i = 2; i < s.length; i++) { //
                    texto = texto.concat(s[i]);
                    texto = texto.concat(" ");
                }

                index.putContent(id, texto);//

                texto = texto.toLowerCase();
                String textonovo = texto.replaceAll("[^a-z]", " ");
                String palavras[] = textonovo.split(" ");

                String twittNOVO = "";
                for (String palavra : palavras) {
                    if (palavra.length() >= 2 && palavra.length() <= 15) {
                        int ocorrencias = index.occurences(palavra, textonovo);
                        Token token = new Token(id, ocorrencias);
                        index.putHash(palavra, token); //inserindo os tokens na tabela hash
                        twittNOVO = twittNOVO.concat(palavra + " ");
                    }
                }
                String twittFinal[] = twittNOVO.split(" ");
                index.putTamanhoDoc(id, index.docSize(twittFinal, twittNOVO)); //calcular o tamanho do documento
            }
            arquivo.close();
        } catch (Exception ex){
            log("Erro ao carregar o arquivo.");
            log(ex.getMessage());
        }
    }



    private static void log(String s){
        System.out.println(s);
    }
}
