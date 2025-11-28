package com.projeto.models;

/**
 * Representa o status de andamento de um pedido.
 */
public enum StatusPedido {
    EM_PREPARO,
    PRONTO,
    ENTREGUE;

    /**
     * Retorna o próximo status na sequência lógica.
     * Se já estiver em ENTREGUE, retorna ENTREGUE novamente.
     */
    public StatusPedido proximo() {
        return switch (this) {
            case EM_PREPARO -> PRONTO;
            case PRONTO -> ENTREGUE;
            case ENTREGUE -> ENTREGUE;
        };
    }
}
