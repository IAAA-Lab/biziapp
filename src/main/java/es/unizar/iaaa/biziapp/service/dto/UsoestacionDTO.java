package es.unizar.iaaa.biziapp.service.dto;


import java.time.LocalDate;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Usoestacion entity.
 */
public class UsoestacionDTO implements Serializable {

    private Long id;

    private String nombreCompleto;

    private Integer idEstacion;

    private String nombreEstacion;

    private LocalDate fechaDeUso;

    private String intervaloDeTiempo;

    private Integer devolucionTotal;

    private Float devolucionMedia;

    private Integer retiradasTotal;

    private Float retiradasMedia;

    private Float neto;

    private Float total;

    private LocalDate fechaObtencionDatos;

    private String ficheroCSV;

    private String ficheroXLS;

    private String hashcode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public Integer getIdEstacion() {
        return idEstacion;
    }

    public void setIdEstacion(Integer idEstacion) {
        this.idEstacion = idEstacion;
    }

    public String getNombreEstacion() {
        return nombreEstacion;
    }

    public void setNombreEstacion(String nombreEstacion) {
        this.nombreEstacion = nombreEstacion;
    }

    public LocalDate getFechaDeUso() {
        return fechaDeUso;
    }

    public void setFechaDeUso(LocalDate fechaDeUso) {
        this.fechaDeUso = fechaDeUso;
    }

    public String getIntervaloDeTiempo() {
        return intervaloDeTiempo;
    }

    public void setIntervaloDeTiempo(String intervaloDeTiempo) {
        this.intervaloDeTiempo = intervaloDeTiempo;
    }

    public Integer getDevolucionTotal() {
        return devolucionTotal;
    }

    public void setDevolucionTotal(Integer devolucionTotal) {
        this.devolucionTotal = devolucionTotal;
    }

    public Float getDevolucionMedia() {
        return devolucionMedia;
    }

    public void setDevolucionMedia(Float devolucionMedia) {
        this.devolucionMedia = devolucionMedia;
    }

    public Integer getRetiradasTotal() {
        return retiradasTotal;
    }

    public void setRetiradasTotal(Integer retiradasTotal) {
        this.retiradasTotal = retiradasTotal;
    }

    public Float getRetiradasMedia() {
        return retiradasMedia;
    }

    public void setRetiradasMedia(Float retiradasMedia) {
        this.retiradasMedia = retiradasMedia;
    }

    public Float getNeto() {
        return neto;
    }

    public void setNeto(Float neto) {
        this.neto = neto;
    }

    public Float getTotal() {
        return total;
    }

    public void setTotal(Float total) {
        this.total = total;
    }

    public LocalDate getFechaObtencionDatos() {
        return fechaObtencionDatos;
    }

    public void setFechaObtencionDatos(LocalDate fechaObtencionDatos) {
        this.fechaObtencionDatos = fechaObtencionDatos;
    }

    public String getFicheroCSV() {
        return ficheroCSV;
    }

    public void setFicheroCSV(String ficheroCSV) {
        this.ficheroCSV = ficheroCSV;
    }

    public String getFicheroXLS() {
        return ficheroXLS;
    }

    public void setFicheroXLS(String ficheroXLS) {
        this.ficheroXLS = ficheroXLS;
    }

    public String getHashcode() {
        return hashcode;
    }

    public void setHashcode(String hashcode) {
        this.hashcode = hashcode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UsoestacionDTO usoestacionDTO = (UsoestacionDTO) o;
        if(usoestacionDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), usoestacionDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "UsoestacionDTO{" +
            "id=" + getId() +
            ", nombreCompleto='" + getNombreCompleto() + "'" +
            ", idEstacion='" + getIdEstacion() + "'" +
            ", nombreEstacion='" + getNombreEstacion() + "'" +
            ", fechaDeUso='" + getFechaDeUso() + "'" +
            ", intervaloDeTiempo='" + getIntervaloDeTiempo() + "'" +
            ", devolucionTotal='" + getDevolucionTotal() + "'" +
            ", devolucionMedia='" + getDevolucionMedia() + "'" +
            ", retiradasTotal='" + getRetiradasTotal() + "'" +
            ", retiradasMedia='" + getRetiradasMedia() + "'" +
            ", neto='" + getNeto() + "'" +
            ", total='" + getTotal() + "'" +
            ", fechaObtencionDatos='" + getFechaObtencionDatos() + "'" +
            ", ficheroCSV='" + getFicheroCSV() + "'" +
            ", ficheroXLS='" + getFicheroXLS() + "'" +
            ", hashcode='" + getHashcode() + "'" +
            "}";
    }
}
