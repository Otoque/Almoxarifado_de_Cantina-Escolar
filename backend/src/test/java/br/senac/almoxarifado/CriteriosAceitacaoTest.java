package br.senac.almoxarifado;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CriteriosAceitacaoTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper om;

    private String nomeUnico() { return "Produto-" + UUID.randomUUID(); }

    private ResultActions criar(String nome, double minimo) throws Exception {
        String body = om.writeValueAsString(Map.of("nome", nome, "unidade", "kg", "estoqueMinimo", minimo));
        return mvc.perform(post("/api/produtos").contentType(MediaType.APPLICATION_JSON).content(body));
    }

    private long criarId(String nome, double minimo) throws Exception {
        String res = criar(nome, minimo).andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        return om.readTree(res).get("id").asLong();
    }

    private ResultActions mov(long id, String tipo, double qtd) throws Exception {
        String body = om.writeValueAsString(Map.of("tipo", tipo, "quantidade", qtd, "motivo", "teste"));
        return mvc.perform(post("/api/produtos/" + id + "/movimentacoes")
                .contentType(MediaType.APPLICATION_JSON).content(body));
    }

    @Test
    void ca1_entrada10_saida4_saldo6_duasLinhas() throws Exception {
        long id = criarId(nomeUnico(), 0);
        mov(id, "entrada", 10).andExpect(status().isCreated());
        mov(id, "saida", 4).andExpect(status().isCreated());

        mvc.perform(get("/api/produtos/" + id)).andExpect(jsonPath("$.saldo", is(6.0)));
        mvc.perform(get("/api/produtos/" + id + "/movimentacoes")).andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void ca2_saidaMaiorQueSaldo_409_eNadaMuda() throws Exception {
        long id = criarId(nomeUnico(), 0);
        mov(id, "entrada", 10).andExpect(status().isCreated());
        mov(id, "saida", 4).andExpect(status().isCreated());

        mov(id, "saida", 7).andExpect(status().isConflict());

        mvc.perform(get("/api/produtos/" + id)).andExpect(jsonPath("$.saldo", is(6.0)));
        mvc.perform(get("/api/produtos/" + id + "/movimentacoes")).andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void ca3_quantidadeZeroOuNegativa_400() throws Exception {
        long id = criarId(nomeUnico(), 0);
        mov(id, "entrada", 0).andExpect(status().isBadRequest());
        mov(id, "saida", -3).andExpect(status().isBadRequest());
        mvc.perform(get("/api/produtos/" + id + "/movimentacoes")).andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void ca4_abaixoDoMinimo() throws Exception {
        String abaixo = nomeUnico();
        String igual = nomeUnico();
        long idAbaixo = criarId(abaixo, 5);
        long idIgual = criarId(igual, 5);
        mov(idAbaixo, "entrada", 3);
        mov(idIgual, "entrada", 5);

        mvc.perform(get("/api/produtos/abaixo-do-minimo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].nome", hasItem(abaixo)))
                .andExpect(jsonPath("$[*].nome", not(hasItem(igual))));
    }

    @Test
    void ca5_nomeDuplicado_409() throws Exception {
        String nome = nomeUnico();
        criar(nome, 0).andExpect(status().isCreated());
        criar(nome, 0).andExpect(status().isConflict());
    }

    @Test
    void produtoNovoComecaComSaldoZero_eMinimoNegativoRecusado() throws Exception {
        criar(nomeUnico(), 2).andExpect(jsonPath("$.saldo", is(0.0)));
        criar(nomeUnico(), -1).andExpect(status().isBadRequest());
    }

    @Test
    void produtoInexistente_404() throws Exception {
        mvc.perform(get("/api/produtos/999999")).andExpect(status().isNotFound());
        mov(999999, "entrada", 1).andExpect(status().isNotFound());
    }
}
