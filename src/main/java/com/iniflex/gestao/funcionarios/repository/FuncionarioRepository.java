package com.iniflex.gestao.funcionarios.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.iniflex.gestao.funcionarios.model.Funcionario;


/**
 * Repository.
 */
@Repository
public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {

    /**
     * Busca funcionários por função.
     * @param funcao a função do funcionário
     * @return lista de funcionários com a função especificada
     */
    List<Funcionario> findByFuncao(String funcao);

    /**
     * Busca funcionários que fazem aniversário em determinado mês.
     * @param mes o mês do aniversário (10-12)
     * @return lista de funcionários
     */
    @Query("SELECT f FROM Funcionario f WHERE MONTH(f.dataNascimento) = :mes ORDER BY f.nome ASC")
    List<Funcionario> findByAniversarioMes(Integer mes);

    /**
     * Busca o funcionário mais velho (maior idade).
     * @return Optional com o funcionário mais velho
     */
    @Query("SELECT f FROM Funcionario f ORDER BY f.dataNascimento ASC LIMIT 1")
    Optional<Funcionario> findFuncionarioMaisVelho();

    /**
     * Retorna todos os funcionários ordenados por nome.
     * @return lista de funcionários ordenados alfabeticamente
     */
    List<Funcionario> findAllByOrderByNomeAsc();
}
