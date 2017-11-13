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
 * A Tratamiento.
 */
@Entity
@Table(name = "tratamiento")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Tratamiento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_tarea")
    private Long idTarea;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private Tipo tipo;

    @NotNull
    @Column(name = "fecha_fichero", nullable = false)
    private String fechaFichero;

    @NotNull
    @Column(name = "path_fichero_xls", nullable = false)
    private String pathFicheroXLS;

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

    public Long getIdTarea() {
        return idTarea;
    }

    public Tratamiento idTarea(Long idTarea) {
        this.idTarea = idTarea;
        return this;
    }

    public void setIdTarea(Long idTarea) {
        this.idTarea = idTarea;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public Tratamiento tipo(Tipo tipo) {
        this.tipo = tipo;
        return this;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public String getFechaFichero() {
        return fechaFichero;
    }

    public Tratamiento fechaFichero(String fechaFichero) {
        this.fechaFichero = fechaFichero;
        return this;
    }

    public void setFechaFichero(String fechaFichero) {
        this.fechaFichero = fechaFichero;
    }

    public String getPathFicheroXLS() {
        return pathFicheroXLS;
    }

    public Tratamiento pathFicheroXLS(String pathFicheroXLS) {
        this.pathFicheroXLS = pathFicheroXLS;
        return this;
    }

    public void setPathFicheroXLS(String pathFicheroXLS) {
        this.pathFicheroXLS = pathFicheroXLS;
    }

    public Estado getEstado() {
        return estado;
    }

    public Tratamiento estado(Estado estado) {
        this.estado = estado;
        return this;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Tratamiento createdAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Tratamiento updatedAt(Instant updatedAt) {
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
        Tratamiento tratamiento = (Tratamiento) o;
        if (tratamiento.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), tratamiento.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Tratamiento{" +
            "id=" + getId() +
            ", idTarea='" + getIdTarea() + "'" +
            ", tipo='" + getTipo() + "'" +
            ", fechaFichero='" + getFechaFichero() + "'" +
            ", pathFicheroXLS='" + getPathFicheroXLS() + "'" +
            ", estado='" + getEstado() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
