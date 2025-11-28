package com.projeto.controllers.admin;

import java.util.stream.Collectors;

import com.projeto.App;
import com.projeto.Store;
import com.projeto.models.ItemPedido;
import com.projeto.models.Pedido;
import com.projeto.models.Produto;
import com.projeto.models.StatusPedido;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class AdminController {

    @FXML
    private VBox listaClientes; // Container principal

    @FXML
    public void initialize() {
        listaClientes.getChildren().clear();
        Store store = Store.getInstance();
        store.getClientes().stream()
            .filter(cliente -> cliente != store.getClienteAtual()) // não mostra cliente atual enquanto pedido em andamento
            .forEach(cliente -> {
            VBox clienteBox = new VBox(5);
            Label nomeClienteLabel = new Label("Cliente: " + cliente.getNome());
            clienteBox.getChildren().add(nomeClienteLabel);
            cliente.getPedidos().forEach(pedido -> clienteBox.getChildren().add(criarBoxPedido(pedido)));
            listaClientes.getChildren().add(clienteBox);
        });
    }

    private HBox criarBoxPedido(Pedido pedido) {
        // Mantém assinatura retornando HBox, mas organiza conteúdo em duas linhas (labels em cima, botões em baixo)
        HBox wrapper = new HBox();

        VBox pedidoBox = new VBox(8);

        Label pedidoLabel = new Label("Pedido #" + pedido.getNumero());
        Label statusLabel = new Label(formatarStatus(pedido.getStatus()));
        aplicarClasseStatus(statusLabel, pedido.getStatus());
        pedidoLabel.setWrapText(true);
        statusLabel.setWrapText(true);

        HBox infoRow = new HBox(10);
        infoRow.getChildren().addAll(pedidoLabel, statusLabel);

        Button detalhesButton = new Button("Detalhes");
        Button avancarButton = new Button("Avançar Status");
        detalhesButton.getStyleClass().addAll("btn", "btn-secondary");
        avancarButton.getStyleClass().addAll("btn", "btn-primary");

        detalhesButton.setOnAction(e -> mostrarDetalhesPedido(pedido));
        avancarButton.setOnAction(e -> {
            pedido.avancarStatus();
            statusLabel.setText(formatarStatus(pedido.getStatus()));
            aplicarClasseStatus(statusLabel, pedido.getStatus());
            avancarButton.setDisable(pedido.getStatus() == StatusPedido.ENTREGUE);
        });
        avancarButton.setDisable(pedido.getStatus() == StatusPedido.ENTREGUE);

        HBox actionsRow = new HBox(10);
        actionsRow.getChildren().addAll(detalhesButton, avancarButton);

        pedidoBox.getChildren().addAll(infoRow, actionsRow);
        wrapper.getChildren().add(pedidoBox);
        return wrapper;
    }

    private void aplicarClasseStatus(Label statusLabel, StatusPedido status) {
        statusLabel.getStyleClass().removeAll("badge", "badge-preparo", "badge-pronto", "badge-entregue");
        statusLabel.getStyleClass().add("badge");
        switch (status) {
            case EM_PREPARO:
                statusLabel.getStyleClass().add("badge-preparo");
                break;
            case PRONTO:
                statusLabel.getStyleClass().add("badge-pronto");
                break;
            case ENTREGUE:
                statusLabel.getStyleClass().add("badge-entregue");
                break;
            default:
                break;
        }
    }

    private String formatarStatus(StatusPedido status) {
        switch (status) {
            case EM_PREPARO: return "Em preparo";
            case PRONTO: return "Pronto";
            case ENTREGUE: return "Entregue";
            default: return status.name();
        }
    }

    @FXML
    private void voltarParaOrigem() throws java.io.IOException {
        String origem = App.getOrigemAdmin();
        if (origem == null) {
            origem = "home";
        }
        App.setRoot(origem);
    }

    private void mostrarDetalhesPedido(Pedido pedido) {
        String detalhes = construirDetalhesPedido(pedido);
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Detalhes do Pedido");
        alert.setHeaderText(null);
        alert.setContentText(detalhes);
        alert.showAndWait();
    }

    private String construirDetalhesPedido(Pedido pedido) {
        String itens = pedido.getItens().stream()
            .map(this::formatarItem)
            .collect(Collectors.joining("\n"));
        double total = pedido.getItens().stream()
            .mapToDouble(item -> item.getProduto().getValor() * item.getQuantidade())
            .sum();
        return "Pedido #" + pedido.getNumero() + "\nStatus: " + formatarStatus(pedido.getStatus()) + "\n" + itens + "\nTotal: R$ " + String.format("%.2f", total);
    }

    private String formatarItem(ItemPedido item) {
        Produto produto = item.getProduto();
        return produto.getNome() + " x" + item.getQuantidade() + " - R$ " + String.format("%.2f", produto.getValor() * item.getQuantidade());
    }
}
