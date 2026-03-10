package br.com.higitech.lojinhaBairro.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.higitech.lojinhaBairro.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    List<Cliente> findByLojinhaId(Long lojinhaId);
}