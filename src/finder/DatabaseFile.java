package finder;

/**
 * Created by hilst on 14/11/16.
 */
public interface DatabaseFile {
    InvertedIndex getIndex();
    void loadDB(String dbPath);
}
