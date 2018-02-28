package web.model;

import javax.persistence.*;

@Entity
@Table(name = "links_to_show")
public class LinkShow {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @Column(name = "merchant")
    private String merchant;

    @Column(name = "service")
    private String service;

    @Column(name = "link", length = 511)
    private String link;

    @Column(name = "carrier")
    private String carrier;

    public LinkShow() {
    }

    public LinkShow(String merchant, String service, String link, String carrier) {

        this.merchant = merchant;
        this.service = service;
        this.link = link;
        this.carrier = carrier;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMerchant() {
        return merchant;
    }

    public void setMerchant(String merchant) {
        this.merchant = merchant;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }
}
