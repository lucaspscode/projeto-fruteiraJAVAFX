package com.projeto.models;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Cliente {
    private static final AtomicInteger count = new AtomicInteger(0);
    private int id;
    private String nome;
    private List<Pedido> pedidos;

    public Cliente(String nome) {
        this.id = count.incrementAndGet();
        this.nome = nome;
        this.pedidos = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public List<Pedido> getPedidos() {
        return pedidos;
    }

    public void adicionarPedido(Pedido pedido) {
        this.pedidos.add(pedido);
    }
}