package modele;

public class Score {
    private int seconds;
    private int pas;

    public Score(int seconds, int pas) {
        this.seconds = seconds;
        this.pas = pas;
    }

    public int getSeconds() {
        return seconds;
    }

    public int getPas() {
        return pas;
    }

    public boolean isBetterThan(Score other) {
        // Un score est meilleur si le nombre de secondes est plus petit ou si le nombre de pas est plus petit
        return this.seconds < other.seconds || (this.seconds == other.seconds && this.pas < other.pas);
    }
}
