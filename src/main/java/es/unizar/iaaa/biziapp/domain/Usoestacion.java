package es.unizar.iaaa.biziapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Usoestacion.
 */
@Entity
@Table(name = "usoestacion")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Usoestacion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre_completo")
    private String nombreCompleto;

    @Column(name = "id_estacion")
    private Integer idEstacion;

    @Column(name = "nombre_estacion")
    private String nombreEstacion;

    @Column(name = "fecha_de_uso")
    private LocalDate fechaDeUso;

    @Column(name = "intervalo_de_tiempo")
    private String intervaloDeTiempo;

    @Column(name = "devolucion_total")
    private Integer devolucionTotal;

    @Column(name = "devolucion_media")
    private Float devolucionMedia;

    @Column(name = "retiradas_total")
    private Integer retiradasTotal;

    @Column(name = "retiradas_media")
    private Float retiradasMedia;

    @Column(name = "neto")
    private Float neto;

    @Column(name = "total")
    private Float total;

    @Column(name = "fecha_obtencion_datos")
    private LocalDate fechaObtencionDatos;

    @Column(name = "fichero_csv")
    private String ficheroCSV;

    @Column(name = "fichero_xls")
    private String ficheroXLS;

    @Column(name = "hashcode")
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

    public Usoestacion nombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
        return this;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public Integer getIdEstacion() {
        return idEstacion;
    }

    public Usoestacion idEstacion(Integer idEstacion) {
        this.idEstacion = idEstacion;
        return this;
    }

    public void setIdEstacion(Integer idEstacion) {
        this.idEstacion = idEstacion;
    }

    public String getNombreEstacion() {
        return nombreEstacion;
    }

    public Usoestacion nombreEstacion(String nombreEstacion) {
        this.nombreEstacion = nombreEstacion;
        return this;
    }

    public void setNombreEstacion(String nombreEstacion) {
        this.nombreEstacion = nombreEstacion;
    }

    public LocalDate getFechaDeUso() {
        return fechaDeUso;
    }

    public Usoestacion fechaDeUso(LocalDate fechaDeUso) {
        this.fechaDeUso = fechaDeUso;
        return this;
    }

    public void setFechaDeUso(LocalDate fechaDeUso) {
        this.fechaDeUso = fechaDeUso;
    }

    public String getIntervaloDeTiempo() {
        return intervaloDeTiempo;
    }

    public Usoestacion intervaloDeTiempo(String intervaloDeTiempo) {
        this.intervaloDeTiempo = intervaloDeTiempo;
        return this;
    }

    public void setIntervaloDeTiempo(String intervaloDeTiempo) {
        this.intervaloDeTiempo = intervaloDeTiempo;
    }

    public Integer getDevolucionTotal() {
        return devolucionTotal;
    }

    public Usoestacion devolucionTotal(Integer devolucionTotal) {
        this.devolucionTotal = devolucionTotal;
        return this;
    }

    public void setDevolucionTotal(Integer devolucionTotal) {
        this.devolucionTotal = devolucionTotal;
    }

    public Float getDevolucionMedia() {
        return devolucionMedia;
    }

    public Usoestacion devolucionMedia(Float devolucionMedia) {
        this.devolucionMedia = devolucionMedia;
        return this;
    }

    public void setDevolucionMedia(Float devolucionMedia) {
        this.devolucionMedia = devolucionMedia;
    }

    public Integer getRetiradasTotal() {
        return retiradasTotal;
    }

    public Usoestacion retiradasTotal(Integer retiradasTotal) {
        this.retiradasTotal = retiradasTotal;
        return this;
    }

    public void setRetiradasTotal(Integer retiradasTotal) {
        this.retiradasTotal = retiradasTotal;
    }

    public Float getRetiradasMedia() {
        return retiradasMedia;
    }

    public Usoestacion retiradasMedia(Float retiradasMedia) {
        this.retiradasMedia = retiradasMedia;
        return this;
    }

    public void setRetiradasMedia(Float retiradasMedia) {
        this.retiradasMedia = retiradasMedia;
    }

    public Float getNeto() {
        return neto;
    }

    public Usoestacion neto(Float neto) {
        this.neto = neto;
        return this;
    }

    public void setNeto(Float neto) {
        this.neto = neto;
    }

    public Float getTotal() {
        return total;
    }

    public Usoestacion total(Float total) {
        this.total = total;
        return this;
    }

    public void setTotal(Float total) {
        this.total = total;
    }

    public LocalDate getFechaObtencionDatos() {
        return fechaObtencionDatos;
    }

    public Usoestacion fechaObtencionDatos(LocalDate fechaObtencionDatos) {
        this.fechaObtencionDatos = fechaObtencionDatos;
        return this;
    }

    public void setFechaObtencionDatos(LocalDate fechaObtencionDatos) {
        this.fechaObtencionDatos = fechaObtencionDatos;
    }

    public String getFicheroCSV() {
        return ficheroCSV;
    }

    public Usoestacion ficheroCSV(String ficheroCSV) {
        this.ficheroCSV = ficheroCSV;
        return this;
    }

    public void setFicheroCSV(String ficheroCSV) {
        this.ficheroCSV = ficheroCSV;
    }

    public String getFicheroXLS() {
        return ficheroXLS;
    }

    public Usoestacion ficheroXLS(String ficheroXLS) {
        this.ficheroXLS = ficheroXLS;
        return this;
    }

    public void setFicheroXLS(String ficheroXLS) {
        this.ficheroXLS = ficheroXLS;
    }

    public String getHashcode() {
        return hashcode;
    }

    public Usoestacion hashcode(String hashcode) {
        this.hashcode = hashcode;
        return this;
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
        Usoestacion usoestacion = (Usoestacion) o;
        if (usoestacion.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), usoestacion.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Usoestacion{" +
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
