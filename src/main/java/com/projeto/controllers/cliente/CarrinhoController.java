package com.projeto.controllers.cliente;

import com.projeto.App;
import com.projeto.Store;
import com.projeto.models.ItemPedido;
import com.projeto.models.Pedido;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class CarrinhoController {

    @FXML
    private javafx.scene.layout.FlowPane itensPedido;

    @FXML
    private Label totalLabel;

    @FXML
    private Button terceiroButton; // Botão "Comprar"

    @FXML
    public void initialize() {
        atualizarLista();
    }

    private void atualizarLista() {
        itensPedido.getChildren().clear();
        Pedido pedido = Store.getInstance().getPedidoAtual();
        if (pedido == null) {
            if (terceiroButton != null) terceiroButton.setDisable(true);
            return;
        }
        for (ItemPedido item : pedido.getItens()) {
            itensPedido.getChildren().add(criarLinhaItem(pedido, item));
        }
        atualizarTotal(pedido);
        boolean vazio = pedido.getItens().isEmpty();
        if (terceiroButton != null) {
            terceiroButton.setDisable(vazio);
        }
    }

        private HBox criarLinhaItem(Pedido pedido, ItemPedido item) {
            // Card visual para o item do pedido
            VBox card = new VBox(6);
            card.getStyleClass().add("product-card");

            // Imagem da fruta
            ImageView imageView;
            try {
                String path = item.getProduto().getImagePath();
                if (path != null) {
                    java.net.URL url = App.class.getResource(path);
                    if (url == null) {
                        String alt = path.replace("/com/projeto", "");
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

            // Nome e subtotal
            Label nameLabel = new Label(item.getProduto().getNome());
            nameLabel.getStyleClass().add("product-name");
            Label subtotalLabel = new Label("Subtotal: R$ " + String.format("%.2f", item.getSubtotal()));
            subtotalLabel.getStyleClass().add("product-price");

            // Controles de quantidade (+/-)
            HBox controls = new HBox(8);
            controls.getStyleClass().add("quantity-controls");
            Button menosButton = new Button("-");
            menosButton.getStyleClass().add("quantity-button");
            Label quantidadeLabel = new Label(String.valueOf(item.getQuantidade()));
            quantidadeLabel.getStyleClass().add("quantity-label");
            Button maisButton = new Button("+");
            maisButton.getStyleClass().add("quantity-button");

            maisButton.setOnAction(e -> {
                if (item.getQuantidade() < item.getProduto().getQuantidade()) {
                    item.setQuantidade(item.getQuantidade() + 1);
                    atualizarLista();
                }
            });
            menosButton.setOnAction(e -> {
                if (item.getQuantidade() > 1) {
                    item.setQuantidade(item.getQuantidade() - 1);
                } else {
                    pedido.removerItem(item);
                }
                atualizarLista();
            });

            controls.getChildren().addAll(menosButton, quantidadeLabel, maisButton);
            card.getChildren().addAll(imageView, nameLabel, subtotalLabel, controls);

            HBox wrapper = new HBox();
            wrapper.getChildren().add(card);
            return wrapper;
    }

    private void atualizarTotal(Pedido pedido) {
        double total = pedido.getItens().stream()
                .mapToDouble(i -> i.getProduto().getValor() * i.getQuantidade())
                .sum();
        totalLabel.setText(String.format("Total: R$ %.2f", total));
    }

    @FXML
    public void navegarParaQuarto() throws java.io.IOException {
        Pedido pedido = Store.getInstance().getPedidoAtual();
        if (pedido == null || pedido.getItens().isEmpty()) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Adicione itens ao carrinho antes de continuar.");
            alert.showAndWait();
            return;
        }
        App.setRoot("confirmacao");
    }

    @FXML
    public void voltarParaSegundo() throws java.io.IOException {
        App.setRoot("produtos");
    }

    @FXML
    public void navegarParaAdmin() throws java.io.IOException {
        App.navegarParaAdmin("carrinho");
    }
}