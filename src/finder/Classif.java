
package finder;

public class Classif {
    private int docId, queryId;

    public Classif(int docId, int queryId) {
        this.docId = docId;
        this.queryId = queryId;
    }

    public Classif() {
    }

    public int getDocId() {
        return docId;
    }

    public void setDocId(int docId) {
        this.docId = docId;
    }

    public int getQueryId() {
        return queryId;
    }

    public void setQueryId(int queryId) {
        this.queryId = queryId;
    }
    
    
}
