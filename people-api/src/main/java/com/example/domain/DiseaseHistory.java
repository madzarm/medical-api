package com.example.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Builder
@Table(name = "disease_history")
@NoArgsConstructor
@AllArgsConstructor
public class DiseaseHistory extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @ManyToOne
    @JoinColumn(name = "person_Id")
    @JsonIgnore
    private Person person;

    @Column(name = "date_discovered", nullable = false)
    private LocalDate dateDiscovered;

    @Column(name = "disease_id", nullable = false)
    private Long diseaseId;

}
