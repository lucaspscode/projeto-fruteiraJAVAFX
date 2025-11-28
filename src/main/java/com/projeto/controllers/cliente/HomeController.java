package com.projeto.controllers.cliente;

import java.io.IOException;

import com.projeto.App;
import com.projeto.Store;
import com.projeto.models.Cliente;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;

public class HomeController {

    @FXML
    private TextField nomeCliente;

    @FXML
    private void navegarParaSegundo() throws IOException {
        String nome = nomeCliente.getText();
        if (estaEmBranco(nome)) {
            mostrarMensagem("Digite seu nome para iniciar");
            return;
        }

        Store store = Store.getInstance();
        Cliente cliente = new Cliente(nome.trim());
        store.adicionarCliente(cliente);
        store.setClienteAtual(cliente);
        store.novoPedido(cliente);
        App.setRoot("produtos");
    }

    private static boolean estaEmBranco(String texto) {
        return texto == null || texto.trim().isEmpty();
    }

    private static void mostrarMensagem(String mensagem) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
    
    @FXML
    private void navegarParaAdmin() throws IOException {
        App.navegarParaAdmin("home");
    }
}