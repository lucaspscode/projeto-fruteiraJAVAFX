package com.projeto;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.projeto.models.Cliente;
import com.projeto.models.Pedido;

public class StoreTest {

    @Test
    void novoPedido_criaEPersistePedidoAtualNoCliente() {
        Store store = Store.getInstance();
        Cliente c = new Cliente("Alice");
        store.adicionarCliente(c);
        store.setClienteAtual(c);
        store.novoPedido(c);

        Pedido atual = store.getPedidoAtual();
        assertNotNull(atual, "pedidoAtual deve ser criado");
        assertEquals(c, atual.getCliente(), "Pedido deve referenciar o cliente");
        assertTrue(c.getPedidos().contains(atual), "Cliente deve conter o pedido criado");
    }
}
