package utils;

public class SemanticResult {

    public final boolean isValid;
    public final double cosineScore;
    public final String matchedExpected;

    public SemanticResult(boolean isValid, double cosineScore, String matchedExpected) {
        this.isValid = isValid;
        this.cosineScore = cosineScore;
        this.matchedExpected = matchedExpected;
    }
}
