package es.unizar.iaaa.biziapp.service.dto;


import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import es.unizar.iaaa.biziapp.domain.enumeration.Tipo;
import es.unizar.iaaa.biziapp.domain.enumeration.Estado;

/**
 * A DTO for the Insercion entity.
 */
public class InsercionDTO implements Serializable {

    private Long id;

    private Long idTarea;

    @NotNull
    private Tipo tipo;

    @NotNull
    private String fechaFichero;

    @NotNull
    private String pathFicheroCSV;

    @NotNull
    private Estado estado;

    private Instant createdAt;

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

    public void setIdTarea(Long idTarea) {
        this.idTarea = idTarea;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public String getFechaFichero() {
        return fechaFichero;
    }

    public void setFechaFichero(String fechaFichero) {
        this.fechaFichero = fechaFichero;
    }

    public String getPathFicheroCSV() {
        return pathFicheroCSV;
    }

    public void setPathFicheroCSV(String pathFicheroCSV) {
        this.pathFicheroCSV = pathFicheroCSV;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
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

        InsercionDTO insercionDTO = (InsercionDTO) o;
        if(insercionDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), insercionDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "InsercionDTO{" +
            "id=" + getId() +
            ", idTarea='" + getIdTarea() + "'" +
            ", tipo='" + getTipo() + "'" +
            ", fechaFichero='" + getFechaFichero() + "'" +
            ", pathFicheroCSV='" + getPathFicheroCSV() + "'" +
            ", estado='" + getEstado() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
