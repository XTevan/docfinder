package finder;

public class Token implements Comparable<Token> {

    private int docId;
    private int tf;
    private double score;

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public Token(int docId, int tf, double score) {
        this.docId = docId;
        this.tf = tf;
        this.score = score;
    }

    public Token(int docId, int tf) {
        this.docId = docId;
        this.tf = tf;
    }

    public Token(int docId, double score) {
        this.docId = docId;
        this.score = score;
    }

    public int getDocId() {
        return docId;
    }

    public void setDocId(int docId) {
        this.docId = docId;
    }

    public int getTf() {
        return tf;
    }

    public void setTf(int tf) {
        this.tf = tf;
    }

    @Override
    public int compareTo(Token t) {//ORDENAR OS MAIS RELEVANTES
        if (score == t.getScore()) {
            return 0;
        }
        if (score < t.getScore()) {
            return 1;
        } else {
            return -1;
        }
    }

}
