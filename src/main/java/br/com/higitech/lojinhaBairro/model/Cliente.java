package br.com.higitech.lojinhaBairro.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "clientes")
public class Cliente {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String telefoneWhatsApp;
    
    // NOVOS CAMPOS
    private String dataNascimento; 
    private String cep;
    private String endereco;
    private String numero;
    private String cidade;
    private String estado;
    
    private LocalDateTime dataCadastro = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "lojinha_id")
    @JsonIgnoreProperties({"categorias", "produtos"})
    private Lojinha lojinha;

    // Getters e Setters
    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; } public void setNome(String nome) { this.nome = nome; }
    public String getTelefoneWhatsApp() { return telefoneWhatsApp; } public void setTelefoneWhatsApp(String telefoneWhatsApp) { this.telefoneWhatsApp = telefoneWhatsApp; }
    public String getDataNascimento() { return dataNascimento; } public void setDataNascimento(String dataNascimento) { this.dataNascimento = dataNascimento; }
    public String getCep() { return cep; } public void setCep(String cep) { this.cep = cep; }
    public String getEndereco() { return endereco; } public void setEndereco(String endereco) { this.endereco = endereco; }
    public String getNumero() { return numero; } public void setNumero(String numero) { this.numero = numero; }
    public String getCidade() { return cidade; } public void setCidade(String cidade) { this.cidade = cidade; }
    public String getEstado() { return estado; } public void setEstado(String estado) { this.estado = estado; }
    public LocalDateTime getDataCadastro() { return dataCadastro; } public void setDataCadastro(LocalDateTime dataCadastro) { this.dataCadastro = dataCadastro; }
    public Lojinha getLojinha() { return lojinha; } public void setLojinha(Lojinha lojinha) { this.lojinha = lojinha; }
}