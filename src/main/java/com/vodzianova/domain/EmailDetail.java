package com.vodzianova.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Elena Vodzianova
 */
@Entity
@Table(name = "EMAIL_DETAIL")
@Data
@NoArgsConstructor
public class EmailDetail implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "body")
    private String body;
    @Column(name = "subject")
    private String subject;
    @Column(name = "sender")
    private String sender;
    @Column(name = "sentdate")
    private Date sentDate;
    @Column(name = "attachmentdirectory")
    private String attachmentDirectory;

    @ManyToOne
    @JoinColumn(name = "fk_profile_id")
    private Profile profile;

    public EmailDetail(String body, String subject, String sender, Date sentDate, String attachmentDirectory, Profile profile) {
        this.body = body;
        this.subject = subject;
        this.sender = sender;
        this.sentDate = sentDate;
        this.attachmentDirectory = attachmentDirectory;
        this.profile = profile;
    }
}
