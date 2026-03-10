package br.com.higitech.lojinhaBairro.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.higitech.lojinhaBairro.model.Produto;
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    List<Produto> findByLojinhaId(Long lojinhaId);
}