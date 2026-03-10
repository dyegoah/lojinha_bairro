package br.com.higitech.lojinhaBairro.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "vendas")
public class Venda {
    
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Double valorTotal;
    private Integer quantidadeItens;
    private LocalDateTime dataVenda = LocalDateTime.now();

    // NOVOS CAMPOS PARA CONTROLE DE ESTOQUE
    private String status = "PENDENTE"; // PENDENTE, CONCLUIDA, REJEITADA
    private String nomeCliente;
    
    @Column(columnDefinition = "TEXT")
    private String itensJson; // Vai guardar o Carrinho no formato JSON

    @ManyToOne
    @JoinColumn(name = "lojinha_id")
    @JsonIgnoreProperties({"categorias", "produtos", "clientes"})
    private Lojinha lojinha;

    // Getters e Setters
    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public Double getValorTotal() { return valorTotal; } public void setValorTotal(Double valorTotal) { this.valorTotal = valorTotal; }
    public Integer getQuantidadeItens() { return quantidadeItens; } public void setQuantidadeItens(Integer quantidadeItens) { this.quantidadeItens = quantidadeItens; }
    public LocalDateTime getDataVenda() { return dataVenda; } public void setDataVenda(LocalDateTime dataVenda) { this.dataVenda = dataVenda; }
    public String getStatus() { return status; } public void setStatus(String status) { this.status = status; }
    public String getNomeCliente() { return nomeCliente; } public void setNomeCliente(String nomeCliente) { this.nomeCliente = nomeCliente; }
    public String getItensJson() { return itensJson; } public void setItensJson(String itensJson) { this.itensJson = itensJson; }
    public Lojinha getLojinha() { return lojinha; } public void setLojinha(Lojinha lojinha) { this.lojinha = lojinha; }
}