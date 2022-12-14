package com.academy.workSearch.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "ratings")
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "rating_id")
    private UUID ratingId;

    @Min(1)
    @Max(5)
    @Column(name = "rate")
    private Float rate;

    @Column(name = "comment", length = 100)
    private String comment;
}
