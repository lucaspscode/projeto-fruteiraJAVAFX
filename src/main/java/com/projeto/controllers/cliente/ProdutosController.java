package com.projeto.controllers.cliente;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.projeto.App;
import com.projeto.Store;
import com.projeto.models.Produto;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ProdutosController {

    @FXML
    private javafx.scene.layout.FlowPane listaItens;

    @FXML
    private Label cartCount;

    @FXML
    public void initialize() {
        if (Store.getInstance().getPedidoAtual() == null) {
            return;
        }
        atualizarContadorCarrinho();
        listaItens.getChildren().clear();
        Store.getInstance().getProdutosDisponiveis().stream()
                .filter(produto -> produto.getQuantidade() > 0)
                .map(this::criarLinhaProduto)
                .forEach(linhaProduto -> listaItens.getChildren().add(linhaProduto));
    }

    private void atualizarContadorCarrinho() {
        int count = listaItens.getChildren().stream()
            .map(node -> (HBox) node)
            .map(hbox -> (VBox) hbox.getChildren().get(0))
            .map(vbox -> (HBox) vbox.getChildren().get(3))
            .map(ctrls -> (Label) ctrls.getChildren().get(1))
            .map(Label::getText)
            .mapToInt(Integer::parseInt)
            .sum();
        cartCount.setText(String.valueOf(count));
    }
    
    @FXML
    public void navegarParaTerceiro() throws java.io.IOException {
        consolidarPedido();
        if (Store.getInstance().getPedidoAtual() == null ||
            Store.getInstance().getPedidoAtual().getItens().isEmpty()) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Adicione itens ao carrinho antes de continuar.");
            alert.showAndWait();
            return;
        }
        App.setRoot("carrinho");
    }

    @FXML
    public void navegarParaAdmin() throws java.io.IOException {
        App.navegarParaAdmin("produtos");
    }

    private void consolidarPedido() {
        List<Map.Entry<Produto, Integer>> itensParaAdicionar = new ArrayList<>();
        for (int i = 0; i < listaItens.getChildren().size(); i++) {
            HBox wrapper = (HBox) listaItens.getChildren().get(i);
            VBox card = (VBox) wrapper.getChildren().get(0);
            Label nameLabel = (Label) card.getChildren().get(1);
            HBox controls = (HBox) card.getChildren().get(3);
            Label quantityLabel = (Label) controls.getChildren().get(1);
            int quantity = Integer.parseInt(quantityLabel.getText());
            if (quantity > 0) {
                Produto produtoOriginal = Store.getInstance().getProdutosDisponiveis().stream()
                        .filter(p -> p.getNome().equals(nameLabel.getText()))
                        .findFirst().orElse(null);
                if (produtoOriginal != null) {
                    itensParaAdicionar.add(new AbstractMap.SimpleEntry<>(produtoOriginal, quantity));
                }
            }
        }
        Store.getInstance().getPedidoAtual().getItens().clear();
        for (Map.Entry<Produto, Integer> entry : itensParaAdicionar) {
            Store.getInstance().getPedidoAtual().adicionarItem(entry.getKey(), entry.getValue());
        }
    }

    private HBox criarLinhaProduto(Produto produto) {
        // Card visual vertical para o produto
        VBox card = new VBox(6);
        card.getStyleClass().add("product-card");

        // Imagem
        ImageView imageView;
        try {
            if (produto.getImagePath() != null) {
                java.net.URL url = App.class.getResource(produto.getImagePath());
                if (url == null) {
                    // tenta caminho alternativo sem prefixo de pacote
                    String alt = produto.getImagePath().replace("/com/projeto", "");
                    url = App.class.getResource(alt);
                }
                if (url != null) {
                    Image image = new Image(url.toExternalForm());
                    imageView = new ImageView(image);
                } else {
                    imageView = new ImageView();
                }
            } else {
                imageView = new ImageView();
            }
        } catch (Exception ex) {
            imageView = new ImageView();
        }
        imageView.setFitWidth(100);
        imageView.setFitHeight(100);
        imageView.setPreserveRatio(true);
        imageView.getStyleClass().add("product-image-view");

        // Nome e preço
        Label nameLabel = new Label(produto.getNome());
        nameLabel.getStyleClass().add("product-name");
        Label priceLabel = new Label("R$ " + String.format("%.2f", produto.getValor()));
        priceLabel.getStyleClass().add("product-price");

        // Controles de quantidade
        HBox controls = new HBox(8);
        controls.getStyleClass().add("quantity-controls");
        Label quantityLabel = new Label("0");
        quantityLabel.getStyleClass().add("quantity-label");
        Button removeButton = new Button("-");
        removeButton.getStyleClass().add("quantity-button");
        Button addButton = new Button("+");
        addButton.getStyleClass().add("quantity-button");

        Runnable atualizarEstadoDosBotoes = () -> {
            int quantidadeAtual = Integer.parseInt(quantityLabel.getText());
            removeButton.setDisable(quantidadeAtual == 0);
            addButton.setDisable(quantidadeAtual >= produto.getQuantidade());
        };
        addButton.setOnAction(evento -> {
            alterarQuantidadeProduto(quantityLabel, +1, produto.getQuantidade());
            atualizarEstadoDosBotoes.run();
        });
        removeButton.setOnAction(evento -> {
            alterarQuantidadeProduto(quantityLabel, -1, produto.getQuantidade());
            atualizarEstadoDosBotoes.run();
        });
        removeButton.setDisable(true);
        atualizarEstadoDosBotoes.run();

        controls.getChildren().addAll(removeButton, quantityLabel, addButton);
        card.getChildren().addAll(imageView, nameLabel, priceLabel, controls);

        // Mantém a assinatura retornando HBox para não quebrar o restante do código
        HBox wrapper = new HBox();
        wrapper.getChildren().add(card);
        return wrapper;
    }

    private void alterarQuantidadeProduto(Label quantityLabel, int delta, int max) {
        int current = Integer.parseInt(quantityLabel.getText());
        int next = current + delta;
        if (next < 0 || next > max) {
            return;
        }
        quantityLabel.setText(String.valueOf(next));
        atualizarContadorCarrinho();
    }
}