package com.vodzianova.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * @author Elena Vodzianova 11/08/2016
 */
@Entity
@Table(name = "PROFILE")
@Data
@ToString(exclude = "emailDetails")
@NoArgsConstructor
public class Profile implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;
    @Column(name = "email")
    private String email;
    @Column(name = "password")
    private String password;
    @Column(name = "port")
    private String port;
    @Column(name = "directory")
    private String directory;

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL)
    private List<EmailDetail> emailDetails;

    public Profile(String name, String email, String password, String port, String directory) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.port = port;
        this.directory = directory;
    }
}
