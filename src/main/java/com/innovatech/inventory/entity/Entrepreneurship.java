package com.innovatech.inventory.entity;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.ArrayList;

import java.util.List;
import org.hibernate.annotations.ManyToAny;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Entrepreneurship")
@Data
@Getter
@Setter
@NoArgsConstructor
public class Entrepreneurship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(unique = true, nullable = false)
    String name;

    String logo;

    String description;

    String names;

    String lastnames;

   

    public Entrepreneurship(String name, String logo, String description, String names, String lastnames) {
        this.name = name;
        this.logo = logo;
        this.description = description;
        this.names = names;
        this.lastnames = lastnames;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Entrepreneurship))
            return false;
        Entrepreneurship that = (Entrepreneurship) o;
        return id != null && id.equals(that.id); // Compara por id
    }

    @Override
    public int hashCode() {
        return 31; // O simplemente puede devolver id.hashCode() si id no es null
    }

}
