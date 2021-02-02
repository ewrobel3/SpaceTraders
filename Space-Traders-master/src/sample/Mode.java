package sample;

public enum Mode {
    EASY(17, 14, 7),
    MEDIUM(15, 10, 5),
    HARD(13, 6, 3);

    public int[] getCutoffs() {
        return cutoffs;
    }

    public void setCutoffs(int[] cutoffs) {
        this.cutoffs = cutoffs;
    }

    private int[] cutoffs;
    Mode(int cutoff1, int cutoff2, int cutoff3) {
        cutoffs = new int[] {cutoff1, cutoff2, cutoff3};
    }
}
