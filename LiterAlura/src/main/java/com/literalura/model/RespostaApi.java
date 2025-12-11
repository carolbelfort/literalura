package com.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RespostaApi {

    @JsonAlias("count")
    private Integer totalResultados;

    @JsonAlias("next")
    private String proximaPagina;

    @JsonAlias("previous")
    private String paginaAnterior;

    @JsonAlias("results")
    private List<Livro> resultados;

    // Construtor vazio
    public RespostaApi() {
    }

    // Construtor com parâmetros
    public RespostaApi(Integer totalResultados, String proximaPagina, String paginaAnterior, List<Livro> resultados) {
        this.totalResultados = totalResultados;
        this.proximaPagina = proximaPagina;
        this.paginaAnterior = paginaAnterior;
        this.resultados = resultados;
    }

    // Getters
    public Integer getTotalResultados() {
        return totalResultados;
    }

    public String getProximaPagina() {
        return proximaPagina;
    }

    public String getPaginaAnterior() {
        return paginaAnterior;
    }

    public List<Livro> getResultados() {
        return resultados;
    }

    // Setters
    public void setTotalResultados(Integer totalResultados) {
        this.totalResultados = totalResultados;
    }

    public void setProximaPagina(String proximaPagina) {
        this.proximaPagina = proximaPagina;
    }

    public void setPaginaAnterior(String paginaAnterior) {
        this.paginaAnterior = paginaAnterior;
    }

    public void setResultados(List<Livro> resultados) {
        this.resultados = resultados;
    }

    // toString
    @Override
    public String toString() {
        return "RespostaApi{" +
                "totalResultados=" + totalResultados +
                ", proximaPagina='" + proximaPagina + '\'' +
                ", paginaAnterior='" + paginaAnterior + '\'' +
                ", resultados=" + resultados +
                '}';
    }
}