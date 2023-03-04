package cl.forevision.wrapper.model;


/**
 * Created by root on 09-08-21.
 */
public class Account {

    /** El identificador único de la entidad, inicialmente fijado en <code>NON_PERSISTED_ID</code>. */
    private long id;

    private String company;

    private String username;

    private String password;

    private Client client;

    private Retailer retailer;

    public Account() {
    }

    public Account(long id, String company, String username, String password, Client client, Retailer retailer) {
        this.id = id;
        this.company = company;
        this.username = username;
        this.password = password;
        this.client = client;
        this.retailer = retailer;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Retailer getRetailer() {
        return retailer;
    }

    public void setRetailer(Retailer retailer) {
        this.retailer = retailer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Account account = (Account) o;

        if (company != null ? !company.equals(account.company) : account.company != null) return false;
        if (!username.equals(account.username)) return false;
        if (!password.equals(account.password)) return false;
        if (!client.equals(account.client)) return false;
        return retailer.equals(account.retailer);

    }

    @Override
    public int hashCode() {
        int result = company != null ? company.hashCode() : 0;
        result = 31 * result + username.hashCode();
        result = 31 * result + password.hashCode();
        result = 31 * result + client.hashCode();
        result = 31 * result + retailer.hashCode();
        return result;
    }
}
