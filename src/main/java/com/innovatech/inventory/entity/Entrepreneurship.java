package com.innovatech.inventory.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
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
