package com.projeto.models;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class PedidoTest {

    @Test
    void calcularTotal_somaSubtotaisDosItens() {
        Cliente cliente = new Cliente("Teste");
        Pedido pedido = new Pedido(cliente);
        Produto banana = new Produto("banana", 6.0, 10);
        Produto maca = new Produto("maca", 8.0, 10);
        pedido.adicionarItem(banana, 2); // 12.0
        pedido.adicionarItem(maca, 3);   // 24.0
        double total = pedido.calcularTotal();
        assertEquals(36.0, total, 0.0001);
    }
}
