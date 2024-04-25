package com.backbase.movies.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Year;

@Entity
@Table(name = "academy_awards")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Award {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private Year year;

    @Column(length = 500, nullable = false)
    private String edition;

    @Column(length = 500, nullable = false)
    private String category;

    @Lob
    @Column(columnDefinition="LONGTEXT")
    private String nominee;

    @Lob
    @Column(columnDefinition="LONGTEXT")
    private String additionalInfo;

    @Column(nullable = false)
    private Boolean won;
}