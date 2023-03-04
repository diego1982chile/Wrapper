package cl.forevision.wrapper.model;

/**
 * Created by root on 22-04-21.
 */
public class Schedule {

    Retailer retailer;
    String schedule;

    public Schedule() {
    }

    public Schedule(Retailer retailer, String schedule) {
        this.retailer = retailer;
        this.schedule = schedule;
    }

    public Retailer getRetailer() {
        return retailer;
    }

    public void setRetailer(Retailer retailer) {
        this.retailer = retailer;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    @Override
    public String toString() {
        return "{" + retailer.getName() + "," + schedule + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Schedule schedule1 = (Schedule) o;

        if (!retailer.equals(schedule1.retailer)) return false;
        return schedule.equals(schedule1.schedule);

    }

    @Override
    public int hashCode() {
        int result = retailer.hashCode();
        result = 31 * result + schedule.hashCode();
        return result;
    }
}
