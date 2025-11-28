package com.projeto;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.projeto.models.Cliente;
import com.projeto.models.Pedido;
import com.projeto.models.Produto;

public class Store {

    private static Store instance;
    private Cliente clienteAtual;
    private Pedido pedidoAtual;
    private final List<Produto> produtosDisponiveis;
    private final List<Cliente> clientes;

    // Inicializa as listas de produtos e clientes.
    private Store() {
        produtosDisponiveis = new ArrayList<>();
        carregarFrutasDeImagensComValores();
        clientes = new ArrayList<>();
    }

    private void carregarFrutasDeImagensComValores() {
        Map<String, Double> precos = new HashMap<>();
        Map<String, Integer> estoques = new HashMap<>();
        precos.put("banana", 6.0);   estoques.put("banana", 2);
        precos.put("maca", 8.0);     estoques.put("maca", 5);
        precos.put("laranja", 7.5);  estoques.put("laranja", 8);
        precos.put("pera", 9.0);     estoques.put("pera", 2);
        precos.put("uva", 12.0);     estoques.put("uva", 5);
        precos.put("limao", 11.0);   estoques.put("limao", 1);
        precos.put("abacaxi", 14.0); estoques.put("abacaxi", 8);
        precos.put("kiwi", 13.0);    estoques.put("kiwi", 1);
        precos.put("melancia", 25.0);estoques.put("melancia", 5);
        precos.put("melao", 22.0);   estoques.put("melao", 6);
        try {
            URL dirUrl = Store.class.getResource("/com/projeto/imagens");
            if (dirUrl == null) {
                throw new IllegalStateException("Diretório de imagens não encontrado no classpath");
            }
            Path imagensPath = Paths.get(dirUrl.toURI());
            Files.list(imagensPath)
                .filter(p -> {
                    String n = p.getFileName().toString().toLowerCase();
                    return n.endsWith(".png") || n.endsWith(".jpg") || n.endsWith(".jpeg");
                })
                .sorted()
                .forEach(p -> {
                    String fileName = p.getFileName().toString();
                    String nomeFruta = fileName.substring(0, fileName.lastIndexOf('.'));
                    String imagePath = "/com/projeto/imagens/" + fileName;
                    double valor = precos.getOrDefault(nomeFruta.toLowerCase(), 10.0);
                    int estoque = estoques.getOrDefault(nomeFruta.toLowerCase(), 10);
                    produtosDisponiveis.add(new Produto(nomeFruta, valor, estoque, imagePath));
                });
        } catch (Exception e) {
            // Fallback: quando não é possível listar (ex.: rodando de JAR),
            // tenta carregar pelos nomes conhecidos de frutas em `precos`.
            for (String fruta : precos.keySet()) {
                // tenta png, jpg, jpeg nessa ordem
                String[] exts = new String[] {".png", ".jpg", ".jpeg"};
                URL res = null;
                String imagePath = null;
                for (String ext : exts) {
                    String candidate = "/com/projeto/imagens/" + fruta + ext;
                    res = Store.class.getResource(candidate);
                    if (res != null) { imagePath = candidate; break; }
                }
                if (res != null && imagePath != null) {
                    double valor = precos.getOrDefault(fruta.toLowerCase(), 10.0);
                    int estoque = estoques.getOrDefault(fruta.toLowerCase(), 10);
                    produtosDisponiveis.add(new Produto(fruta, valor, estoque, imagePath));
                }
            }
        }

        // Se ainda estiver vazio (nenhuma imagem encontrada), cria produtos padrão
        if (produtosDisponiveis.isEmpty()) {
            for (String fruta : precos.keySet()) {
                double valor = precos.getOrDefault(fruta.toLowerCase(), 10.0);
                int estoque = estoques.getOrDefault(fruta.toLowerCase(), 10);
                // sem imagem, usa caminho nulo para evitar NPE, controllers lidam com ImageView vazio
                produtosDisponiveis.add(new Produto(fruta, valor, estoque, null));
            }
        }
    }
    
    // Retorna a instância única da classe Store.
    public static Store getInstance() {
        if (instance == null) {
            instance = new Store();
        }
        return instance;
    }

    // Obtém o cliente atualmente ativo na sessão.
    public Cliente getClienteAtual() {
        return clienteAtual;
    }

    // Define o cliente atualmente ativo na sessão.
    public void setClienteAtual(Cliente cliente) {
        this.clienteAtual = cliente;
    }

 
    // Obtém o pedido que está sendo montado atualmente.
    public Pedido getPedidoAtual() {
        return pedidoAtual;
    }

    // Cria um novo pedido para um cliente e o associa a ele.
    public void novoPedido(Cliente cliente) {
        this.pedidoAtual = new Pedido(cliente);
        cliente.adicionarPedido(this.pedidoAtual);
    }

    // Retorna a lista de todos os produtos disponíveis na loja.
    public List<Produto> getProdutosDisponiveis() {
        return produtosDisponiveis;
    }

    // Retorna a lista de todos os clientes cadastrados.
    public List<Cliente> getClientes() {
        return clientes;
    }

    // Adiciona um novo cliente à lista de clientes da loja, se ele ainda não existir.
    public void adicionarCliente(Cliente cliente) {
        if (!clientes.contains(cliente)) {
            clientes.add(cliente);
        }
    }
}