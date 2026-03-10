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
@Table(name = "produtos")
public class Produto {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private Double preco;
    private String imageUrl;
    
    // NOVOS CAMPOS PARA O ESTOQUE
    private Integer quantidadeEstoque = 0; 
    private Double precoCusto = 0.0; 

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    @JsonIgnoreProperties("lojinha")
    private Categoria categoria;

    @ManyToOne
    @JoinColumn(name = "lojinha_id")
    @JsonIgnoreProperties({"categorias", "produtos"})
    private Lojinha lojinha;

    // Getters e Setters
    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; } public void setNome(String nome) { this.nome = nome; }
    public Double getPreco() { return preco; } public void setPreco(Double preco) { this.preco = preco; }
    public String getImageUrl() { return imageUrl; } public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public Integer getQuantidadeEstoque() { return quantidadeEstoque; } public void setQuantidadeEstoque(Integer quantidadeEstoque) { this.quantidadeEstoque = quantidadeEstoque; }
    public Double getPrecoCusto() { return precoCusto; } public void setPrecoCusto(Double precoCusto) { this.precoCusto = precoCusto; }
    public Categoria getCategoria() { return categoria; } public void setCategoria(Categoria categoria) { this.categoria = categoria; }
    public Lojinha getLojinha() { return lojinha; } public void setLojinha(Lojinha lojinha) { this.lojinha = lojinha; }
}