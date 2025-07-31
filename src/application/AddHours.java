package application;

public class AddHours {
    private double[] hours = new double[7];
    private boolean[] isPTO = new boolean[7];


    public void setHours(int day, double hours) {
        if (day >= 0 && day < 7 && !isPTO[day]) {
            this.hours[day] = hours;
        }
    }


    

 
    public double getHours(int day) {
        if (day >= 0 && day < 7) {
            return hours[day];
        }
        return 0;
    }

    public boolean setPTO(int day, PTO e) {
        if (day >= 0 && day <= 4 && !isPTO[day] && hours[day] == 0) {
            if (e.getRemainingPTOHours() >= 8) {
                isPTO[day] = true;
                hours[day] = 8;
                e.usePTO(8);
                return true;
            }
        }
        return false;
    }
    public boolean isPTO(int day) {
        if (day >= 0 && day < 7) {
            return isPTO[day];
        }
        return false;
    }

    public void clearPTO(int day) {
        if (day >= 0 && day < 7 && isPTO[day]) {
            isPTO[day] = false;
            // Note: PTO refund would need to be handled by the calling method
        }
    }

    public int getNumDaysWorked() {
        int count = 0;
        for (int i = 0; i < hours.length; i++) {
            if (hours[i] > 0 && !isPTO[i]) {
                count++;
            }
        }
        return count;
    }

    public double getTotalHours() {
        double total = 0;
        for (double h : hours) {
            total += h;
        }
        return total;
    }

    public double getWeekdayHours() {
        double total = 0;
        for (int i = 0; i < 5; i++) {
            total += hours[i];
        }
        return total;
    }

    public double getWeekendHours() {
        return hours[5] + hours[6];
    }

    public boolean setOrUpdateHoursForManager(int dayIndex, double newHours, boolean newIsPTO, PTO pto) {
        if (dayIndex < 0 || dayIndex > 6) return false;

        if (isPTO[dayIndex]) {
            pto.refundPTOHours(8);
            isPTO[dayIndex] = false;
        }

        if (newIsPTO) {
            if (newHours != 8) return false;
            if (pto.getRemainingPTOHours() < 8) return false;
            hours[dayIndex] = 8;
            isPTO[dayIndex] = true;
            pto.usePTO(8);
        } else {
            hours[dayIndex] = newHours;
            isPTO[dayIndex] = false;
        }

        return true;
    }
}
    