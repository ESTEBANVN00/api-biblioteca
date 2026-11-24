package biblioteca;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BibliotecaApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void listaLibrosIniciales() throws Exception {
        mockMvc.perform(get("/api/libros"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void creaLibro() throws Exception {
        mockMvc.perform(post("/api/libros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"titulo\":\"Test\",\"autor\":\"Autor\",\"isbn\":\"TEST-001\",\"anioPublicacion\":2020}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.disponible").value(true));
    }

    @Test
    void rechazaLibroInvalido() throws Exception {
        mockMvc.perform(post("/api/libros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"titulo\":\"\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void noPermitePrestarDosVeces() throws Exception {
        mockMvc.perform(patch("/api/libros/3/prestar")).andExpect(status().isOk());
        mockMvc.perform(patch("/api/libros/3/prestar")).andExpect(status().isConflict());
    }
}
