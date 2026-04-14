package com.iniflex.gestao.funcionarios.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI gestaoFuncionariosOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("API de Gestao de Funcionarios")
                .description("Documentacao da API para cadastro, atualizacao, exclusao e consulta de funcionarios.")
                .version("v1"));
    }
}
