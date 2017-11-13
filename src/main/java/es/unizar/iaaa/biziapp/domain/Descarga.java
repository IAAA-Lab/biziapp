package es.unizar.iaaa.biziapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

import es.unizar.iaaa.biziapp.domain.enumeration.Tipo;

import es.unizar.iaaa.biziapp.domain.enumeration.Estado;

/**
 * A Descarga.
 */
@Entity
@Table(name = "descarga")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Descarga implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private Tipo tipo;

    @NotNull
    @Column(name = "fecha_fichero", nullable = false)
    private String fechaFichero;

    @NotNull
    @Column(name = "categoria", nullable = false)
    private String categoria;

    @NotNull
    @Column(name = "subcategoria", nullable = false)
    private String subcategoria;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private Estado estado;


    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP",  insertable=false, updatable=false)
    private Instant createdAt;

    @Column(name = "updated_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP",  insertable=false, updatable=false)
    private Instant updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public Descarga tipo(Tipo tipo) {
        this.tipo = tipo;
        return this;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public String getFechaFichero() {
        return fechaFichero;
    }

    public Descarga fechaFichero(String fechaFichero) {
        this.fechaFichero = fechaFichero;
        return this;
    }

    public void setFechaFichero(String fechaFichero) {
        this.fechaFichero = fechaFichero;
    }

    public String getCategoria() {
        return categoria;
    }

    public Descarga categoria(String categoria) {
        this.categoria = categoria;
        return this;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getSubcategoria() {
        return subcategoria;
    }

    public Descarga subcategoria(String subcategoria) {
        this.subcategoria = subcategoria;
        return this;
    }

    public void setSubcategoria(String subcategoria) {
        this.subcategoria = subcategoria;
    }

    public Estado getEstado() {
        return estado;
    }

    public Descarga estado(Estado estado) {
        this.estado = estado;
        return this;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Descarga createdAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Descarga updatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Descarga descarga = (Descarga) o;
        if (descarga.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), descarga.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Descarga{" +
            "id=" + getId() +
            ", tipo='" + getTipo() + "'" +
            ", fechaFichero='" + getFechaFichero() + "'" +
            ", categoria='" + getCategoria() + "'" +
            ", subcategoria='" + getSubcategoria() + "'" +
            ", estado='" + getEstado() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
