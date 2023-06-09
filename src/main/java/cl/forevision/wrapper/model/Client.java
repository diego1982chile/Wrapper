package cl.forevision.wrapper.model;

/**
 * Created by root on 09-08-21.
 */
public class Client {

    /** El identificador único de la entidad, inicialmente fijado en <code>NON_PERSISTED_ID</code>. */
    private long id;

    private String name;

    public Client() {
    }

    public Client(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

        Client client = (Client) o;

        return name.equals(client.name);

    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
