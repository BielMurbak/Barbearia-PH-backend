package com.barbearia.ph;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BarbeariaApiApplicationTest {

    @Test
    @DisplayName("Deve carregar o contexto da aplicação")
    void deveCarregarContextoAplicacao() {
        // Este teste verifica se o contexto Spring Boot carrega corretamente
        // O próprio @SpringBootTest já executa o método main() internamente
    }

    @Test
    @DisplayName("Deve executar o método main")
    void deveExecutarMetodoMain() {
        // Testa diretamente o método main
        BarbeariaApiApplication.main(new String[]{});
    }
}