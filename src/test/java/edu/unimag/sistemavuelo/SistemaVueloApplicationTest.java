package edu.unimag.sistemavuelo;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class SistemaVueloApplicationTest {
    @Test
    void contextLoads() {
    }

}