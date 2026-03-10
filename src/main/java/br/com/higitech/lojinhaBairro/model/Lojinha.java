package br.com.higitech.lojinhaBairro.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "lojinhas")
public class Lojinha {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // Dados da Loja
    private String nome; 
    private String slug;
    private String corPrincipal = "#4e73df"; 
    private String telefoneWhatsApp;
    @Column(length = 1000)
    private String bannerMuralUrl; 
    private Integer visualizacoes = 0; 
    private String chavePix;

    // Dados de Autenticação e Sistema
    private String email;
    private String senha;
    private Boolean ativo = true;
    private java.time.LocalDate dataVencimento = java.time.LocalDate.now().plusDays(7); 
    
    // NOVO: DATA DO CADASTRO
    private java.time.LocalDateTime dataCadastro = java.time.LocalDateTime.now();

    // Dados do Proprietário (Primeiro Acesso)
    private String nomeProprietario;
    private String dataNascimento;
    private String cep;
    private String rua;
    private String numero;
    private String complemento;
    private String cidade;
    private String estado;

    @OneToMany(mappedBy = "lojinha", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Categoria> categorias;

    @OneToMany(mappedBy = "lojinha", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Produto> produtos;

    // Getters e Setters
    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; } public void setNome(String nome) { this.nome = nome; }
    public String getSlug() { return slug; } public void setSlug(String slug) { this.slug = slug; }
    public String getCorPrincipal() { return corPrincipal; } public void setCorPrincipal(String corPrincipal) { this.corPrincipal = corPrincipal; }
    public String getTelefoneWhatsApp() { return telefoneWhatsApp; } public void setTelefoneWhatsApp(String telefoneWhatsApp) { this.telefoneWhatsApp = telefoneWhatsApp; }
    public String getBannerMuralUrl() { return bannerMuralUrl; } public void setBannerMuralUrl(String bannerMuralUrl) { this.bannerMuralUrl = bannerMuralUrl; }
    public Integer getVisualizacoes() { return visualizacoes; } public void setVisualizacoes(Integer visualizacoes) { this.visualizacoes = visualizacoes; }
    public String getChavePix() { return chavePix; } public void setChavePix(String chavePix) { this.chavePix = chavePix; }
    public String getEmail() { return email; } public void setEmail(String email) { this.email = email; }
    public String getSenha() { return senha; } public void setSenha(String senha) { this.senha = senha; }
    public Boolean getAtivo() { return ativo; } public void setAtivo(Boolean ativo) { this.ativo = ativo; }
    public java.time.LocalDate getDataVencimento() { return dataVencimento; } public void setDataVencimento(java.time.LocalDate dataVencimento) { this.dataVencimento = dataVencimento; }
    public java.time.LocalDateTime getDataCadastro() { return dataCadastro; } public void setDataCadastro(java.time.LocalDateTime dataCadastro) { this.dataCadastro = dataCadastro; }
    public String getNomeProprietario() { return nomeProprietario; } public void setNomeProprietario(String nomeProprietario) { this.nomeProprietario = nomeProprietario; }
    public String getDataNascimento() { return dataNascimento; } public void setDataNascimento(String dataNascimento) { this.dataNascimento = dataNascimento; }
    public String getCep() { return cep; } public void setCep(String cep) { this.cep = cep; }
    public String getRua() { return rua; } public void setRua(String rua) { this.rua = rua; }
    public String getNumero() { return numero; } public void setNumero(String numero) { this.numero = numero; }
    public String getComplemento() { return complemento; } public void setComplemento(String complemento) { this.complemento = complemento; }
    public String getCidade() { return cidade; } public void setCidade(String cidade) { this.cidade = cidade; }
    public String getEstado() { return estado; } public void setEstado(String estado) { this.estado = estado; }
}