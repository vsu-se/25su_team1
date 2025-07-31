package application;

public class PTO {
    private int remainingPTOHours;

    public PTO(int startingPTOHours) {
        this.remainingPTOHours = startingPTOHours;
    }

    public int getRemainingPTOHours() {
        return remainingPTOHours;
    }

    public void usePTO(int hours) {
        if (hours > 0 && remainingPTOHours >= hours) {
            remainingPTOHours -= hours;
        }
    }

    public void refundPTOHours(int hours) {
        if (hours > 0) {
            remainingPTOHours += hours;
        }
    }
}