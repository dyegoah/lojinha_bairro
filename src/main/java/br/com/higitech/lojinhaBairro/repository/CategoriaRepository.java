package br.com.higitech.lojinhaBairro.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.higitech.lojinhaBairro.model.Categoria;
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    List<Categoria> findByLojinhaId(Long lojinhaId);
}