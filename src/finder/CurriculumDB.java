package finder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;


/**
 * Created by hilst on 14/11/16.
 */
public class CurriculumDB implements DatabaseFile {
    private InvertedIndex invertedIndex;

    public CurriculumDB(){
        invertedIndex = new InvertedIndex();
    }

    @Override
    public InvertedIndex getIndex() {
        return invertedIndex;
    }

    @Override
    public void loadDB(String dbPath) {
        String line;
        try(BufferedReader reader = new BufferedReader(new FileReader(new File("corpus.txt")))){
            String content = "";
            Integer id = 1;
            while ((line = reader.readLine()) != null){
                if (line != ""){
                    content += " " + line;
                }else{
                    invertedIndex.putContent(id,content);
                    String[] words = content.toLowerCase().split(" ");


                    content = "";
                    id++;
                }
            }
        }catch(Exception ex){
            this.log("Erro ao ler arquivo.");
            this.log(ex.getMessage());
        }
    }

    private void log(String s){
        System.out.println(s);
    }
}
