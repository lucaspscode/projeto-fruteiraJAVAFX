package com.projeto.controllers.cliente;

import com.projeto.App;
import com.projeto.Store;
import com.projeto.models.ItemPedido;
import com.projeto.models.Pedido;
import com.projeto.models.Produto;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ConfirmacaoController {

    @FXML
    private FlowPane itensContainer;

    @FXML
    private Label totalLabel;

    @FXML
    public void initialize() {
        Pedido pedido = Store.getInstance().getPedidoAtual();
        if (pedido == null) {
            return;
        }
        itensContainer.getChildren().clear();
        pedido.getItens().forEach(item -> itensContainer.getChildren().add(criarCardItem(item)));
        atualizarTotal(pedido);
    }

    private void atualizarTotal(Pedido pedido) {
        double total = pedido.getItens().stream()
                .mapToDouble(item -> item.getProduto().getValor() * item.getQuantidade())
                .sum();
        totalLabel.setText(String.format("Total: R$ %.2f", total));
    }

    // Removido método não utilizado (formatarItem)

    private HBox criarCardItem(ItemPedido item) {
        VBox card = new VBox(6);
        card.getStyleClass().add("product-card");

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

        Label nameLabel = new Label(item.getProduto().getNome());
        nameLabel.getStyleClass().add("product-name");
        Label qtyPrice = new Label("Qtd: " + item.getQuantidade() + " • Subtotal: R$ " + String.format("%.2f", item.getSubtotal()));
        qtyPrice.getStyleClass().add("product-price");

        card.getChildren().addAll(imageView, nameLabel, qtyPrice);
        HBox wrapper = new HBox();
        wrapper.getChildren().add(card);
        return wrapper;
    }

    @FXML
    public void confirmarPedido() throws java.io.IOException {
        Pedido pedido = Store.getInstance().getPedidoAtual();
        if (pedido == null || pedido.getItens().isEmpty()) {
            App.setRoot("home");
            return;
        }
        for (ItemPedido itemPedido : pedido.getItens()) {
            Produto produtoNoEstoque = itemPedido.getProduto();
            produtoNoEstoque.setQuantidade(produtoNoEstoque.getQuantidade() - itemPedido.getQuantidade());
        }
        Store.getInstance().setClienteAtual(null);
        App.setRoot("home");
    }

    @FXML
    public void voltarParaTerceiro() throws java.io.IOException {
        App.setRoot("carrinho");
    }

    @FXML
    public void navegarParaAdmin() throws java.io.IOException {
        App.navegarParaAdmin("confirmacao");
    }
}