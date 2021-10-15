package pucrs.redes.server;

public class CurrentPlayerState {
    private int level;
    private int points;

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void increasePoints(int point) {
        this.points += point;
    }

    public int getPoints() {
        return points;
    }
}
