package br.com.higitech.lojinhaBairro.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "categorias")
public class Categoria {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String slug;

    @ManyToOne
    @JoinColumn(name = "lojinha_id")
    @JsonIgnoreProperties({"categorias", "produtos"})
    private Lojinha lojinha;

    // Getters e Setters
    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; } public void setNome(String nome) { this.nome = nome; }
    public String getSlug() { return slug; } public void setSlug(String slug) { this.slug = slug; }
    public Lojinha getLojinha() { return lojinha; } public void setLojinha(Lojinha lojinha) { this.lojinha = lojinha; }
}