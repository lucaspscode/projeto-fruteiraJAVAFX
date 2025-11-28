package com.projeto.models;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class StatusPedidoTest {

    @Test
    void proximo_avancaEmPreparoParaPronto() {
        assertEquals(StatusPedido.PRONTO, StatusPedido.EM_PREPARO.proximo());
    }

    @Test
    void proximo_avancaProntoParaEntregue() {
        assertEquals(StatusPedido.ENTREGUE, StatusPedido.PRONTO.proximo());
    }

    @Test
    void proximo_deEntreguePermaneceEntregue() {
        assertEquals(StatusPedido.ENTREGUE, StatusPedido.ENTREGUE.proximo());
    }
}
