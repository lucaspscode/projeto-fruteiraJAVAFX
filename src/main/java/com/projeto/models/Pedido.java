package com.projeto.models;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Pedido {
    private static final AtomicInteger count = new AtomicInteger(0);
    private int numero;
    private List<ItemPedido> itens;
    private Cliente cliente;
    private StatusPedido status;

    public Pedido(Cliente cliente) {
        this.numero = count.incrementAndGet();
        this.cliente = cliente;
        this.itens = new ArrayList<>();
        this.status = StatusPedido.EM_PREPARO; // status inicial
    }

    public int getNumero() {
        return numero;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public List<ItemPedido> getItens() {
        return itens;
    }

    /** Obtém o status atual do pedido. */
    public StatusPedido getStatus() { return status; }

    /** Define explicitamente o status do pedido. */
    public void setStatus(StatusPedido status) { this.status = status; }

    /** Avança o status para o próximo estado lógico. */
    public StatusPedido avancarStatus() {
        this.status = this.status.proximo();
        return this.status;
    }

    /**
     * Adiciona um item ao pedido. Se o produto já existir no pedido,
     * apenas atualiza a quantidade.
     */
    public void adicionarItem(Produto produto, int quantidade) {
        for (ItemPedido item : itens) {
            if (item.getProduto().equals(produto)) {
                item.setQuantidade(item.getQuantidade() + quantidade);
                return;
            }
        }
        itens.add(new ItemPedido(produto, quantidade));
    }

    // Remove um item do pedido.
    public void removerItem(ItemPedido item) {
        itens.remove(item);
    }
    
    // Calcula o valor total do pedido.
    public double calcularTotal() {
        double totalPedido = 0.0;
        for (ItemPedido item : itens) {
            totalPedido += item.getSubtotal();
        }
        return totalPedido;
    }
}