package br.com.higitech.lojinhaBairro.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.higitech.lojinhaBairro.model.Categoria;
import br.com.higitech.lojinhaBairro.model.Lojinha;
import br.com.higitech.lojinhaBairro.model.Produto;
import br.com.higitech.lojinhaBairro.repository.CategoriaRepository;
import br.com.higitech.lojinhaBairro.repository.LojinhaRepository;
import br.com.higitech.lojinhaBairro.repository.ProdutoRepository;

@Configuration
public class DataSetup {

    @Bean
    CommandLineRunner initDatabase(LojinhaRepository lojinhaRepo, CategoriaRepository catRepo, ProdutoRepository prodRepo) {
        return args -> {
            if (lojinhaRepo.count() == 0) {
                // Cria a Lojinha
                Lojinha loja = new Lojinha();
                loja.setNome("Doces da Maria");
                loja.setSlug("doces-da-maria");
                loja.setCorPrincipal("#FF1493");
                loja.setTelefoneWhatsApp("5511999999999");
                lojinhaRepo.save(loja);

                // Cria Categorias
                Categoria c1 = new Categoria(); c1.setNome("Bolos de Pote"); c1.setSlug("bolos"); c1.setLojinha(loja);
                Categoria c2 = new Categoria(); c2.setNome("Docinhos"); c2.setSlug("docinhos"); c2.setLojinha(loja);
                catRepo.save(c1); catRepo.save(c2);

                // Cria Produtos Dinâmicos
                Produto p1 = new Produto(); p1.setNome("Bolo de Pote Ninho"); p1.setPreco(12.00); 
                p1.setImageUrl("https://images.unsplash.com/photo-1578985545062-69928b1d9587?w=500&auto=format&fit=crop");
                p1.setCategoria(c1); p1.setLojinha(loja);
                
                Produto p2 = new Produto(); p2.setNome("Caixa 4 Brigadeiros"); p2.setPreco(15.50); 
                p2.setImageUrl("https://images.unsplash.com/photo-1605807646983-377bc5a76493?w=500&auto=format&fit=crop");
                p2.setCategoria(c2); p2.setLojinha(loja);

                prodRepo.save(p1); prodRepo.save(p2);
                System.out.println("====== BANCO DE DADOS PREENCHIDO COM SUCESSO! ======");
            }
        };
    }
}