package ghops.gprint.models;

public class Customer {

    private int id = 0;
    private String name = "";
    private String email = "";
    private String web = "";
    private String city = "";
    private String fax = "";
    private String address = "";
    private String telephone = "";
    private boolean status = true;

    public Customer() {
    }

    public Customer(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public boolean isStatus() {
        return status;
    }

    public boolean getStatus() {
        return this.status;
    }

    public int getStatusValue() {
        return (this.status) ? 1 : 0;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setStatus(int st) {
        if (st == 1) {
            this.status = true;

        } else {
            this.status = false;

        }
    }

    @Override
    public String toString() {
        return this.getName();
    }

}
