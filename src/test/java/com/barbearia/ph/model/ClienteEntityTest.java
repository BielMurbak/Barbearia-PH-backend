package com.barbearia.ph.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClienteEntityTest {

    @Test
    @DisplayName("TESTE DE UNIDADE – Deve retornar nome completo corretamente")
    void deveRetornarNomeCompletoCorretamente() {
        ClienteEntity cliente = new ClienteEntity();
        cliente.setNome("João");
        cliente.setSobrenome("Silva");

        String nomeCompleto = cliente.getNomeCompleto();

        assertEquals("João Silva", nomeCompleto);
    }

    @Test
    @DisplayName("TESTE DE UNIDADE – Deve retornar string vazia quando nome for nulo")
    void deveRetornarVazioQuandoNomeNulo() {
        ClienteEntity cliente = new ClienteEntity();
        cliente.setNome(null);
        cliente.setSobrenome("Silva");

        String nomeCompleto = cliente.getNomeCompleto();

        assertEquals("", nomeCompleto);
    }

    @Test
    @DisplayName("TESTE DE UNIDADE – Deve retornar string vazia quando sobrenome for nulo")
    void deveRetornarVazioQuandoSobrenomeNulo() {
        ClienteEntity cliente = new ClienteEntity();
        cliente.setNome("João");
        cliente.setSobrenome(null);

        String nomeCompleto = cliente.getNomeCompleto();

        assertEquals("", nomeCompleto);
    }

    @Test
    @DisplayName("TESTE DE UNIDADE – Deve validar celular válido corretamente")
    void deveValidarCelularValido() {
        ClienteEntity cliente = new ClienteEntity();
        cliente.setCelular("(11) 99999-9999");

        boolean isValid = cliente.isValidCelular();

        assertTrue(isValid);
    }

    @Test
    @DisplayName("TESTE DE UNIDADE – Deve invalidar celular com poucos dígitos")
    void deveInvalidarCelularComPoucosDigitos() {
        ClienteEntity cliente = new ClienteEntity();
        cliente.setCelular("123456789");

        boolean isValid = cliente.isValidCelular();

        assertFalse(isValid);
    }

    @Test
    @DisplayName("TESTE DE UNIDADE – Deve invalidar celular nulo")
    void deveInvalidarCelularNulo() {
        ClienteEntity cliente = new ClienteEntity();
        cliente.setCelular(null);

        boolean isValid = cliente.isValidCelular();

        assertFalse(isValid);
    }

    @Test
    @DisplayName("TESTE DE UNIDADE – Deve invalidar celular vazio")
    void deveInvalidarCelularVazio() {
        ClienteEntity cliente = new ClienteEntity();
        cliente.setCelular("");

        boolean isValid = cliente.isValidCelular();

        assertFalse(isValid);
    }

    @Test
    @DisplayName("TESTE DE UNIDADE – Deve remover espaços do nome completo")
    void deveRemoverEspacosDoNomeCompleto() {
        ClienteEntity cliente = new ClienteEntity();
        cliente.setNome("  João  ");
        cliente.setSobrenome("  Silva  ");

        String nomeCompleto = cliente.getNomeCompleto();

        assertEquals("João Silva", nomeCompleto);
    }
}