package com.springboot.blog;

import com.github.slugify.Slugify;
import com.springboot.blog.entity.Role;
import com.springboot.blog.repository.RoleRepository;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@OpenAPIDefinition(
        servers = {@Server(url = "/", description = "Default Server URL")},
        info = @Info(
                title = "Blog REST APIs",
                description = "This is Blog REST APIs Documentation",
                version = "v1.0",
                contact = @Contact(
                        name = "Tran Tuan Anh",
                        email = "anhtran8301@gmail.com",
                        url = "https://www.facebook.com/nhoc.bin.169/"
                )
        ),
        externalDocs = @ExternalDocumentation(
                description = "Blog App Documentation",
                url = "https://github.com/anhtran8301/springboot_blog"
        )
)
public class SpringbootBlogRestapiApplication {
    @Bean
    public RestTemplate restTemplate() { return new RestTemplate(); }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringbootBlogRestapiApplication.class, args);
    }

    private RoleRepository roleRepository;

}
