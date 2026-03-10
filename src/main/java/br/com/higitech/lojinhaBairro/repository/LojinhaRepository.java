package br.com.higitech.lojinhaBairro.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.higitech.lojinhaBairro.model.Lojinha;

public interface LojinhaRepository extends JpaRepository<Lojinha, Long> {
    Optional<Lojinha> findBySlug(String slug);
}