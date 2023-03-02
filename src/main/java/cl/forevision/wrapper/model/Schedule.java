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
}
