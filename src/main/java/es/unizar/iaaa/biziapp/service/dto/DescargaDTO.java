package es.unizar.iaaa.biziapp.service.dto;


import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import es.unizar.iaaa.biziapp.domain.enumeration.Tipo;
import es.unizar.iaaa.biziapp.domain.enumeration.Estado;

/**
 * A DTO for the Descarga entity.
 */
public class DescargaDTO implements Serializable {

    private Long id;

    @NotNull
    private Tipo tipo;

    @NotNull
    private String fechaFichero;

    @NotNull
    private String categoria;

    @NotNull
    private String subcategoria;

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

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getSubcategoria() {
        return subcategoria;
    }

    public void setSubcategoria(String subcategoria) {
        this.subcategoria = subcategoria;
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

        DescargaDTO descargaDTO = (DescargaDTO) o;
        if(descargaDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), descargaDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DescargaDTO{" +
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
