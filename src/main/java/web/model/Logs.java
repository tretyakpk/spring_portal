package web.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "logs")
public class Logs {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "log_id")
    private int roleId;

    @Column(name = "type")
    private String type;

    @Column(name = "created_at")
    private Date created_at;

    @Column(name = "parameters")
    private String parameters;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    public Logs() {
    }
}
