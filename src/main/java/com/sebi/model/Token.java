package com.sebi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;
    @Column(name = "is_logged_out")
    private boolean loggedOut;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

}
