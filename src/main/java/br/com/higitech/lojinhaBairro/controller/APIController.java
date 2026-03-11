package br.com.higitech.lojinhaBairro.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.com.higitech.lojinhaBairro.model.Categoria;
import br.com.higitech.lojinhaBairro.model.Cliente;
import br.com.higitech.lojinhaBairro.model.Lojinha;
import br.com.higitech.lojinhaBairro.model.Produto;
import br.com.higitech.lojinhaBairro.model.Venda;
import br.com.higitech.lojinhaBairro.repository.CategoriaRepository;
import br.com.higitech.lojinhaBairro.repository.ClienteRepository;
import br.com.higitech.lojinhaBairro.repository.LojinhaRepository;
import br.com.higitech.lojinhaBairro.repository.ProdutoRepository;
import br.com.higitech.lojinhaBairro.repository.VendaRepository;
import br.com.higitech.lojinhaBairro.service.CloudinaryService;
import jakarta.servlet.http.HttpServletRequest;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api")
public class APIController {

    @Autowired private LojinhaRepository lojinhaRepo;
    @Autowired private CategoriaRepository categoriaRepo;
    @Autowired private ProdutoRepository produtoRepo;
    @Autowired private ClienteRepository clienteRepo;
    @Autowired private VendaRepository vendaRepo; 
    @Autowired private CloudinaryService cloudinaryService;
    @Autowired private HttpServletRequest request;

    // ================= ROTAS DE AUTENTICAÇÃO E REGISTRO =================
    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> creds) {
        String email = creds.get("email"); String senha = creds.get("senha");
        if("admin@lojinha.com".equals(email) && "admin123".equals(senha)) { return ResponseEntity.ok(Map.of("role", "SUPER_ADMIN")); }
        
        Optional<Lojinha> lojaOpt = lojinhaRepo.findAll().stream().filter(l -> email.equals(l.getEmail()) && senha.equals(l.getSenha())).findFirst();
        if(lojaOpt.isPresent()) {
            Lojinha loja = lojaOpt.get();
            if(loja.getAtivo() != null && !loja.getAtivo()) return ResponseEntity.status(403).body(Map.of("erro", "Loja bloqueada. Contate o suporte."));
            if(loja.getDataVencimento() != null && loja.getDataVencimento().isBefore(java.time.LocalDate.now())) return ResponseEntity.status(402).body(Map.of("erro", "Sua assinatura venceu. Renove o plano."));
            return ResponseEntity.ok(Map.of("role", "LOJA", "lojaId", loja.getId(), "lojaNome", loja.getNome()));
        }
        return ResponseEntity.status(401).body(Map.of("erro", "Email ou senha incorretos."));
    }

    @PostMapping("/auth/register")
    public ResponseEntity<?> register(@RequestBody Lojinha novaLoja) {
        if(lojinhaRepo.findAll().stream().anyMatch(l -> novaLoja.getEmail().equals(l.getEmail()))) {
            return ResponseEntity.badRequest().body(Map.of("erro", "E-mail já cadastrado no sistema."));
        }
        String baseSlug = novaLoja.getNome().toLowerCase().replaceAll("[^a-z0-9]+", "-");
        novaLoja.setSlug(baseSlug + "-" + (System.currentTimeMillis() % 1000));
        novaLoja.setAtivo(true);
        novaLoja.setDataVencimento(java.time.LocalDate.now().plusDays(7));
        return ResponseEntity.ok(lojinhaRepo.save(novaLoja));
    }

    private Lojinha getMinhaLoja() {
        String lojaIdStr = request.getHeader("X-Loja-ID");
        if (lojaIdStr != null && !lojaIdStr.isEmpty()) return lojinhaRepo.findById(Long.parseLong(lojaIdStr)).orElse(null);
        return lojinhaRepo.findAll().stream().findFirst().orElse(null);
    }

    // ================= ROTAS DO SUPER ADMIN =================
    @GetMapping("/superadmin/lojas")
    public ResponseEntity<List<Lojinha>> getTodasLojasAdmin() { return ResponseEntity.ok(lojinhaRepo.findAll()); }

    @PutMapping("/superadmin/lojas/{id}/dias")
    public ResponseEntity<?> addDiasLoja(@PathVariable Long id, @RequestBody Map<String, Integer> payload) {
        Lojinha loja = lojinhaRepo.findById(id).orElseThrow();
        int dias = payload.get("dias");
        
        if (dias == 99999) { 
            loja.setDataVencimento(java.time.LocalDate.now().plusYears(100));
        } else {
            if(loja.getDataVencimento() == null || loja.getDataVencimento().isBefore(java.time.LocalDate.now())) {
                loja.setDataVencimento(java.time.LocalDate.now().plusDays(dias));
            } else {
                loja.setDataVencimento(loja.getDataVencimento().plusDays(dias));
            }
        }
        return ResponseEntity.ok(lojinhaRepo.save(loja));
    }

    @PutMapping("/superadmin/lojas/{id}/status")
    public ResponseEntity<?> toggleStatusLoja(@PathVariable Long id) {
        Lojinha loja = lojinhaRepo.findById(id).orElseThrow();
        loja.setAtivo(loja.getAtivo() == null ? false : !loja.getAtivo()); 
        return ResponseEntity.ok(lojinhaRepo.save(loja));
    }

    // ================= ROTAS DO CATÁLOGO (PÚBLICO) =================
    @GetMapping("/catalogo/{slugLoja}")
    public ResponseEntity<?> getCatalogo(@PathVariable String slugLoja) {
        Optional<Lojinha> lojinhaOpt = lojinhaRepo.findBySlug(slugLoja);
        if (lojinhaOpt.isEmpty()) return ResponseEntity.notFound().build();
        Lojinha loja = lojinhaOpt.get();
        if (loja.getVisualizacoes() == null) loja.setVisualizacoes(0);
        loja.setVisualizacoes(loja.getVisualizacoes() + 1);
        lojinhaRepo.save(loja);
        return ResponseEntity.ok(Map.of("loja", loja, "categorias", categoriaRepo.findByLojinhaId(loja.getId()), "produtos", produtoRepo.findByLojinhaId(loja.getId())));
    }

    @PostMapping("/catalogo/{slugLoja}/cliente")
    public ResponseEntity<?> salvarClienteCatalogo(@PathVariable String slugLoja, @RequestBody Cliente cliente) {
        Optional<Lojinha> lojinhaOpt = lojinhaRepo.findBySlug(slugLoja);
        if (lojinhaOpt.isEmpty()) return ResponseEntity.notFound().build();
        cliente.setLojinha(lojinhaOpt.get()); return ResponseEntity.ok(clienteRepo.save(cliente));
    }

    @PostMapping("/catalogo/{slugLoja}/venda")
    public ResponseEntity<?> registrarVenda(@PathVariable String slugLoja, @RequestBody Map<String, Object> payload) {
        Optional<Lojinha> lojinhaOpt = lojinhaRepo.findBySlug(slugLoja);
        if (lojinhaOpt.isEmpty()) return ResponseEntity.notFound().build();
        Venda venda = new Venda(); venda.setLojinha(lojinhaOpt.get());
        venda.setValorTotal(Double.valueOf(payload.get("valorTotal").toString()));
        venda.setQuantidadeItens((Integer) payload.get("quantidadeItens"));
        venda.setNomeCliente((String) payload.get("nomeCliente")); venda.setStatus("PENDENTE");
        try { venda.setItensJson(new ObjectMapper().writeValueAsString(payload.get("itens"))); } catch(Exception e) {}
        return ResponseEntity.ok(vendaRepo.save(venda));
    }

    // ================= ROTAS DO DASHBOARD LOJISTA =================
    @GetMapping("/admin/loja") public ResponseEntity<Lojinha> getLojaAdmin() { return ResponseEntity.ok(getMinhaLoja()); }
    @GetMapping("/admin/produtos") public ResponseEntity<List<Produto>> getProdutosAdmin() { return ResponseEntity.ok(produtoRepo.findByLojinhaId(getMinhaLoja().getId())); }
    @GetMapping("/admin/categorias") public ResponseEntity<List<Categoria>> getCategoriasDashboard() { return ResponseEntity.ok(categoriaRepo.findByLojinhaId(getMinhaLoja().getId())); }
    @GetMapping("/admin/clientes") public ResponseEntity<List<Cliente>> getClientesAdmin() { return ResponseEntity.ok(clienteRepo.findByLojinhaId(getMinhaLoja().getId())); }
    @GetMapping("/admin/vendas") public ResponseEntity<List<Venda>> getVendasAdmin() { return ResponseEntity.ok(vendaRepo.findByLojinhaId(getMinhaLoja().getId())); }
    
    @PostMapping("/admin/produto")
    public ResponseEntity<?> salvarProduto(@RequestBody Produto produto) {
        produto.setLojinha(getMinhaLoja()); 
        if(produto.getCategoria() != null && produto.getCategoria().getId() != null) { produto.setCategoria(categoriaRepo.findById(produto.getCategoria().getId()).get()); }
        return ResponseEntity.ok(produtoRepo.save(produto));
    }
    
    @PutMapping("/admin/loja")
    public ResponseEntity<?> atualizarLoja(@RequestBody Lojinha lojaAtualizada) {
        Lojinha loja = getMinhaLoja();
        if(lojaAtualizada.getNome() != null) loja.setNome(lojaAtualizada.getNome());
        if(lojaAtualizada.getTelefoneWhatsApp() != null) loja.setTelefoneWhatsApp(lojaAtualizada.getTelefoneWhatsApp());
        if(lojaAtualizada.getCorPrincipal() != null) loja.setCorPrincipal(lojaAtualizada.getCorPrincipal());
        if(lojaAtualizada.getBannerMuralUrl() != null) loja.setBannerMuralUrl(lojaAtualizada.getBannerMuralUrl());
        if(lojaAtualizada.getChavePix() != null) loja.setChavePix(lojaAtualizada.getChavePix());
        return ResponseEntity.ok(lojinhaRepo.save(loja));
    }

    @PutMapping("/admin/estoque/{id}")
    public ResponseEntity<?> atualizarEstoque(@PathVariable Long id, @RequestBody Map<String, Integer> payload) {
        Produto produto = produtoRepo.findById(id).get(); produto.setQuantidadeEstoque(payload.get("quantidade")); return ResponseEntity.ok(produtoRepo.save(produto));
    }

    @DeleteMapping("/admin/clientes/{id}")
    public ResponseEntity<?> deletarCliente(@PathVariable Long id) { clienteRepo.deleteById(id); return ResponseEntity.ok().build(); }

    @PutMapping("/admin/clientes/{id}")
    public ResponseEntity<?> atualizarCliente(@PathVariable Long id, @RequestBody Cliente clienteAtualizado) {
        Cliente c = clienteRepo.findById(id).get();
        c.setNome(clienteAtualizado.getNome()); c.setTelefoneWhatsApp(clienteAtualizado.getTelefoneWhatsApp());
        c.setDataNascimento(clienteAtualizado.getDataNascimento()); c.setCep(clienteAtualizado.getCep()); 
        c.setEndereco(clienteAtualizado.getEndereco()); c.setNumero(clienteAtualizado.getNumero());
        c.setCidade(clienteAtualizado.getCidade()); c.setEstado(clienteAtualizado.getEstado());
        return ResponseEntity.ok(clienteRepo.save(c));
    }

    @PutMapping("/admin/vendas/{id}/status")
    public ResponseEntity<?> atualizarStatusVenda(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        Venda venda = vendaRepo.findById(id).get(); String novoStatus = payload.get("status");
        if ("CONCLUIDA".equals(novoStatus) && "PENDENTE".equals(venda.getStatus())) {
            try {
                List<Map<String, Object>> itens = new ObjectMapper().readValue(venda.getItensJson(), new TypeReference<List<Map<String, Object>>>(){});
                for (Map<String, Object> item : itens) {
                    if(item.get("id") != null) {
                        Produto p = produtoRepo.findById(Long.valueOf(item.get("id").toString())).orElse(null);
                        if (p != null) { p.setQuantidadeEstoque(Math.max(0, (p.getQuantidadeEstoque() == null ? 0 : p.getQuantidadeEstoque()) - 1)); produtoRepo.save(p); }
                    }
                }
            } catch(Exception e) {}
        }
        venda.setStatus(novoStatus); return ResponseEntity.ok(vendaRepo.save(venda));
    }

    @PostMapping("/admin/upload")
    public ResponseEntity<Map<String, String>> uploadArquivo(@RequestParam("file") MultipartFile file) {
        try { return ResponseEntity.ok(Map.of("url", cloudinaryService.uploadImagem(file))); } 
        catch (Exception e) { return ResponseEntity.internalServerError().build(); }
    }
}