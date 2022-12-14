package com.example.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.Optional;


@Data
@Entity
@Builder
@Table(name = "disease")
@NoArgsConstructor
@AllArgsConstructor
public class Disease extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "curable")
    private Boolean curable;

    public Disease(String name, boolean curable) {
        this.name = name;
        this.curable = curable;
    }

    public static Optional<Disease> findByName(String name) {
        return Disease.find("name", name).firstResultOptional();
    }

    public static List<Disease> listByIdsIn(List<Long> ids) {
        return Disease.list("id in ?1", ids);
    }

    public static List<Disease> listByNameContaining(String name) {
        String searchInput = "%" + name + "%";
        return Disease.list("name like ?1", searchInput);
    }

    public static List<Disease> listByCurable(boolean curable) {
        return Disease.list("curable", curable);
    }

    public static int updateByIdWithNameAndCurable(Long id, String name, Boolean curable) {
        return Disease.update("name = ?1, curable = ?2 where id = ?3", name, curable, id);
    }

    public static int updateByIdWithName(Long id, String name) {
        return Disease.update("name = ?1 where id = ?2", name, id);
    }

    public static int updateByIdWithCurable(Long id, Boolean curable) {
        return Disease.update("curable = ?1 where id = ?2", curable, id);
    }

    public static int updateByNameWithCurable(String name, Boolean curable) {
        return Disease.update("curable = ?1 where name = ?2", curable, name);
    }

    public static int deleteByIds(List<Long> ids) {
        return (int) Disease.delete("id in ?1", ids);
    }

    public static int deleteByName(String name) {
        return (int) Disease.delete("name like ?1", name);
    }

    public static int deleteByCurable(Boolean curable) {
        return (int) Disease.delete("curable", curable);
    }

}
