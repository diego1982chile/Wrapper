package cl.forevision.wrapper.model;


/**
 * Created by root on 09-08-21.
 */
public class Retailer {

    private Long id;

    private String name;

    //@OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, orphanRemoval = true)
    //private List<Schedule> schedules = new ArrayList<>();


    public Retailer() {
    }

    public Retailer(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Retailer retailer = (Retailer) o;

        return name.equals(retailer.name);

    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
