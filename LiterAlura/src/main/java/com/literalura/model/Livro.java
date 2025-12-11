package com.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "livros")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @JsonAlias("title")
    private String titulo;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "autor_id")
    @JsonAlias("authors")
    private Autor autor;

    @JsonAlias("languages")
    private String idioma;

    @JsonAlias("download_count")
    private Integer numeroDownloads;

    // Construtor vazio
    public Livro() {
    }

    // Construtor com parâmetros
    public Livro(String titulo, Autor autor, String idioma, Integer numeroDownloads) {
        this.titulo = titulo;
        this.autor = autor;
        this.idioma = idioma;
        this.numeroDownloads = numeroDownloads;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public Autor getAutor() {
        return autor;
    }

    public String getIdioma() {
        return idioma;
    }

    public Integer getNumeroDownloads() {
        return numeroDownloads;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public void setNumeroDownloads(Integer numeroDownloads) {
        this.numeroDownloads = numeroDownloads;
    }

    // Método auxiliar para configurar da API
    public void setAutoresFromApi(List<Autor> autores) {
        if (autores != null && !autores.isEmpty()) {
            this.autor = autores.get(0); // Pega apenas o primeiro autor
        }
    }

    // Método auxiliar para configurar idioma da API
    public void setIdiomasFromApi(List<String> idiomas) {
        if (idiomas != null && !idiomas.isEmpty()) {
            this.idioma = idiomas.get(0); // Pega apenas o primeiro idioma
        }
    }

    // toString
    @Override
    public String toString() {
        return "Livro{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", autor=" + (autor != null ? autor.getNome() : "N/A") +
                ", idioma='" + idioma + '\'' +
                ", numeroDownloads=" + numeroDownloads +
                '}';
    }
}