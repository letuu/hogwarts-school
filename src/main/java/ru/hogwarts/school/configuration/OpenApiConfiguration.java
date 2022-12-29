package ru.hogwarts.school.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {
    @Bean
    public OpenAPI schoolApi() {
        return new OpenAPI()
                .info(new Info().title("School API").description("API to manage Hogwarts").version("1.0.0"))
                .addTagsItem(new Tag().name("student").description("Student management API"));
    }
}
