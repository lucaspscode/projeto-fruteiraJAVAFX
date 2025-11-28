package com.projeto.models;

public enum StatusPedido {
    EM_PREPARO,
    PRONTO,
    ENTREGUE;

    public StatusPedido proximo() {
        return switch (this) {
            case EM_PREPARO -> PRONTO;
            case PRONTO -> ENTREGUE;
            case ENTREGUE -> ENTREGUE;
        };
    }
}
