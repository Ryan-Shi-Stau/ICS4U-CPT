package cpt;

public class Player {
    private String username;
    private double tr;
    private String rank;
    private double glicko;
    private double rd;
    private double apm;
    private double pps;
    private double vs;

    // Constructor
    public Player(String username, double tr, String rank, double glicko, double rd, double apm, double pps, double vs) {
        this.username = username;
        this.tr = tr;
        this.rank = rank;
        this.glicko = glicko;
        this.rd = rd;
        this.apm = apm;
        this.pps = pps;
        this.vs = vs;
    }

    // Getters
    public String getUsername() {
        return username;
    }

    public double getTr() {
        return tr;
    }

    public String getRank() {
        return rank;
    }

    public double getGlicko() {
        return glicko;
    }

    public double getRd() {
        return rd;
    }

    public double getApm() {
        return apm;
    }

    public double getPps() {
        return pps;
    }

    public double getVs() {
        return vs;
    }
}
