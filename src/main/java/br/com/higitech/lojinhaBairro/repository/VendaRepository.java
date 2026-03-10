package br.com.higitech.lojinhaBairro.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.higitech.lojinhaBairro.model.Venda;

public interface VendaRepository extends JpaRepository<Venda, Long> {
    List<Venda> findByLojinhaId(Long lojinhaId);
}