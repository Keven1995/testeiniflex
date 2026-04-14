package com.iniflex.gestao.funcionarios.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.anyList;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.iniflex.gestao.funcionarios.model.Funcionario;
import com.iniflex.gestao.funcionarios.repository.FuncionarioRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("FuncionarioService Tests")
class FuncionarioServiceTest {

    @Mock
    private FuncionarioRepository funcionarioRepository;

    @InjectMocks
    private FuncionarioService funcionarioService;

    private Funcionario funcionarioTeste1;
    private Funcionario funcionarioTeste2;
    private Funcionario funcionarioTeste3;

    @BeforeEach
    void setUp() {
        funcionarioTeste1 = new Funcionario("João Silva", LocalDate.of(1990, 5, 15), 
                                           new BigDecimal("3000.00"), "Gerente");
        funcionarioTeste1.setId(1L);

        funcionarioTeste2 = new Funcionario("Maria Santos", LocalDate.of(1985, 10, 20), 
                                           new BigDecimal("2500.00"), "Analista");
        funcionarioTeste2.setId(2L);

        funcionarioTeste3 = new Funcionario("Pedro Costa", LocalDate.of(1988, 12, 5), 
                                           new BigDecimal("2800.00"), "Gerente");
        funcionarioTeste3.setId(3L);
    }

    @Test
    @DisplayName("Deve criar um novo funcionário")
    void testCriarFuncionario() {
        when(funcionarioRepository.save(funcionarioTeste1)).thenReturn(funcionarioTeste1);

        Funcionario resultado = funcionarioService.criarFuncionario(funcionarioTeste1);

        assertNotNull(resultado);
        assertEquals(funcionarioTeste1.getNome(), resultado.getNome());
        assertEquals(funcionarioTeste1.getSalario(), resultado.getSalario());
        verify(funcionarioRepository, times(1)).save(funcionarioTeste1);
    }

    @Test
    @DisplayName("Deve buscar todos os funcionários")
    void testBuscarTodos() {
        List<Funcionario> funcionarios = Arrays.asList(funcionarioTeste1, funcionarioTeste2, funcionarioTeste3);
        when(funcionarioRepository.findAll()).thenReturn(funcionarios);

        List<Funcionario> resultado = funcionarioService.buscarTodos();

        assertEquals(3, resultado.size());
        assertTrue(resultado.stream().anyMatch(funcionario -> Objects.equals(funcionario.getId(), funcionarioTeste1.getId())));
        assertTrue(resultado.stream().anyMatch(funcionario -> Objects.equals(funcionario.getId(), funcionarioTeste2.getId())));
        verify(funcionarioRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve buscar funcionário por ID existente")
    void testBuscarPorIdExistente() {
        when(funcionarioRepository.findById(1L)).thenReturn(Optional.of(funcionarioTeste1));

        Optional<Funcionario> resultado = funcionarioService.buscarPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals(funcionarioTeste1.getNome(), resultado.get().getNome());
        verify(funcionarioRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve retornar empty ao buscar funcionário por ID inexistente")
    void testBuscarPorIdInexistente() {
        when(funcionarioRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<Funcionario> resultado = funcionarioService.buscarPorId(999L);

        assertFalse(resultado.isPresent());
        verify(funcionarioRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("Deve remover funcionário por ID")
    void testRemoverFuncionario() {
        funcionarioService.removerFuncionario(1L);

        verify(funcionarioRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Deve atualizar funcionário existente")
    void testAtualizarFuncionario() {
        Funcionario funcionarioAtualizado = new Funcionario("João Silva Atualizado", 
                                                            LocalDate.of(1990, 5, 15),
                                                            new BigDecimal("3500.00"), "Gerente Sênior");
        funcionarioAtualizado.setId(1L);
        when(funcionarioRepository.save(funcionarioAtualizado)).thenReturn(funcionarioAtualizado);

        Funcionario resultado = funcionarioService.atualizarFuncionario(funcionarioAtualizado);

        assertEquals(new BigDecimal("3500.00"), resultado.getSalario());
        assertEquals("Gerente Sênior", resultado.getFuncao());
        verify(funcionarioRepository, times(1)).save(funcionarioAtualizado);
    }

    @Test
    @DisplayName("Deve aplicar aumento de 10% em todos os salários")
    void testAplicarAumentoSalarial() {
        List<Funcionario> funcionarios = Arrays.asList(funcionarioTeste1, funcionarioTeste2, funcionarioTeste3);
        when(funcionarioRepository.findAll()).thenReturn(funcionarios);
        when(funcionarioRepository.saveAll(anyList())).thenReturn(funcionarios);

        BigDecimal salarioOriginal1 = funcionarioTeste1.getSalario();
        BigDecimal salarioOriginal2 = funcionarioTeste2.getSalario();

        funcionarioService.aplicarAumentoSalarial();

        verify(funcionarioRepository, times(1)).findAll();
        verify(funcionarioRepository, times(1)).saveAll(anyList());
        assertEquals(salarioOriginal1.multiply(BigDecimal.valueOf(1.10)), funcionarioTeste1.getSalario());
        assertEquals(salarioOriginal2.multiply(BigDecimal.valueOf(1.10)), funcionarioTeste2.getSalario());
    }

    @Test
    @DisplayName("Deve agrupar funcionários por função")
    void testAgruparPorFuncao() {
        List<Funcionario> funcionarios = Arrays.asList(funcionarioTeste1, funcionarioTeste2, funcionarioTeste3);
        when(funcionarioRepository.findAll()).thenReturn(funcionarios);

        Map<String, List<Funcionario>> resultado = funcionarioService.agruparPorFuncao();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals(2, resultado.get("Gerente").size());
        assertEquals(1, resultado.get("Analista").size());
        verify(funcionarioRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve buscar aniversariantes em outubro e dezembro")
    void testBuscarAniversariantes() {
        List<Funcionario> outubristas = Collections.singletonList(funcionarioTeste2);
        List<Funcionario> dezembristas = Collections.singletonList(funcionarioTeste3);

        when(funcionarioRepository.findByAniversarioMes(10)).thenReturn(outubristas);
        when(funcionarioRepository.findByAniversarioMes(12)).thenReturn(dezembristas);

        List<Funcionario> resultado = funcionarioService.buscarAniversariantes(10, 12);

        assertEquals(2, resultado.size());
        assertTrue(resultado.stream().anyMatch(funcionario -> Objects.equals(funcionario.getId(), funcionarioTeste2.getId())));
        assertTrue(resultado.stream().anyMatch(funcionario -> Objects.equals(funcionario.getId(), funcionarioTeste3.getId())));
        verify(funcionarioRepository, times(1)).findByAniversarioMes(10);
        verify(funcionarioRepository, times(1)).findByAniversarioMes(12);
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não há aniversariantes no mês")
    void testBuscarAniversariantes_SemAniversariantes() {
        when(funcionarioRepository.findByAniversarioMes(1)).thenReturn(Collections.emptyList());

        List<Funcionario> resultado = funcionarioService.buscarAniversariantes(1);

        assertTrue(resultado.isEmpty());
        verify(funcionarioRepository, times(1)).findByAniversarioMes(1);
    }

    @Test
    @DisplayName("Deve buscar funcionário mais velho")
    void testBuscarFuncionarioMaisVelho() {
        when(funcionarioRepository.findFuncionarioMaisVelho()).thenReturn(Optional.of(funcionarioTeste2));

        Optional<Funcionario> resultado = funcionarioService.buscarFuncionarioMaisVelho();

        assertTrue(resultado.isPresent());
        assertEquals(funcionarioTeste2.getNome(), resultado.get().getNome());
        verify(funcionarioRepository, times(1)).findFuncionarioMaisVelho();
    }

    @Test
    @DisplayName("Deve retornar empty quando não há funcionários (buscar mais velho)")
    void testBuscarFuncionarioMaisVelho_Vazio() {
        when(funcionarioRepository.findFuncionarioMaisVelho()).thenReturn(Optional.empty());

        Optional<Funcionario> resultado = funcionarioService.buscarFuncionarioMaisVelho();

        assertFalse(resultado.isPresent());
        verify(funcionarioRepository, times(1)).findFuncionarioMaisVelho();
    }

    @Test
    @DisplayName("Deve buscar todos os funcionários ordenados por nome")
    void testBuscarTodosOrdenados() {
        List<Funcionario> funcionariosOrdenados = Arrays.asList(funcionarioTeste1, funcionarioTeste2, funcionarioTeste3);
        when(funcionarioRepository.findAllByOrderByNomeAsc()).thenReturn(funcionariosOrdenados);

        List<Funcionario> resultado = funcionarioService.buscarTodosOrdenados();

        assertEquals(3, resultado.size());
        verify(funcionarioRepository, times(1)).findAllByOrderByNomeAsc();
    }

    @Test
    @DisplayName("Deve calcular total de salários corretamente")
    void testCalcularTotalSalarios() {
        List<Funcionario> funcionarios = Arrays.asList(funcionarioTeste1, funcionarioTeste2, funcionarioTeste3);
        when(funcionarioRepository.findAll()).thenReturn(funcionarios);

        BigDecimal totalEsperado = new BigDecimal("3000.00")
                .add(new BigDecimal("2500.00"))
                .add(new BigDecimal("2800.00"));

        BigDecimal resultado = funcionarioService.calcularTotalSalarios();

        assertEquals(totalEsperado, resultado);
        verify(funcionarioRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve retornar zero ao calcular total de salários com lista vazia")
    void testCalcularTotalSalarios_ListaVazia() {
        when(funcionarioRepository.findAll()).thenReturn(Collections.emptyList());

        BigDecimal resultado = funcionarioService.calcularTotalSalarios();

        assertEquals(BigDecimal.ZERO, resultado);
        verify(funcionarioRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve retornar zero ao calcular total quando salário é nulo")
    void testCalcularTotalSalarios_ComSalariosNulos() {
        Funcionario funcionarioComSalariosNulo = new Funcionario("Ana Silva", LocalDate.of(1992, 3, 10), null, "Estagiária");
        List<Funcionario> funcionarios = Arrays.asList(funcionarioTeste1, funcionarioComSalariosNulo);
        when(funcionarioRepository.findAll()).thenReturn(funcionarios);

        BigDecimal resultado = funcionarioService.calcularTotalSalarios();

        assertEquals(new BigDecimal("3000.00"), resultado);
        verify(funcionarioRepository, times(1)).findAll();
    }
}
