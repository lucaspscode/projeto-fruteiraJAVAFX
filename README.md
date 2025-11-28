# Demo Loja JavaFX

Aplicação JavaFX de demonstração para fluxo de compras com gerenciamento de pedidos e painel administrativo de status.

## Visão Geral
A aplicação permite que um cliente:
- Informe seu nome
- Selecione produtos e quantidades (respeitando estoque)
- Revise o carrinho
- Confirme o pedido

E permite ao administrador:
- Visualizar todos os clientes que realizaram pedidos
- Ver cada pedido com seus itens e total
- Avançar o status do pedido pelas fases: Em preparo → Pronto → Entregue

## Tecnologias
- Java (JDK 21 ou superior recomendado)
- JavaFX (controls, fxml, graphics)
- Maven (empacotamento e execução)

## Estrutura de Pastas (principal)
```
src/main/java/com/projeto/
  App.java                # Entrada JavaFX
  Store.java              # Singleton de estado da aplicação
  models/                 # Modelos de domínio (Cliente, Produto, ItemPedido, Pedido, StatusPedido)
  controllers/
    cliente/              # Controladores visuais para o fluxo do cliente
      HomeController.java
      ProdutosController.java
      CarrinhoController.java
      ConfirmacaoController.java
    admin/                # Controlador do painel administrativo
      AdminController.java
src/main/resources/com/projeto/
  home.fxml
  produtos.fxml
  carrinho.fxml
  confirmacao.fxml
  admin.fxml
```

## Fluxo de Telas (Cliente)
1. `home.fxml` – captura do nome e criação do pedido
2. `produtos.fxml` – listagem de produtos e seleção de quantidades
3. `carrinho.fxml` – resumo dos itens e ajuste fino
4. `confirmacao.fxml` – confirmação final do pedido
5. `admin.fxml` – painel administrativo (se acessado) mostrando pedidos e status

## Modelo de Domínio
- `Produto`: nome, valor, quantidade em estoque
- `ItemPedido`: produto + quantidade + subtotal
- `Pedido`: número sequencial, lista de itens, cliente, status (`StatusPedido`)
- `Cliente`: id, nome, lista de pedidos
- `StatusPedido`: enum com lógica de avanço (`proximo()`) e uso em `Pedido.avancarStatus()`

## Status do Pedido
| Fase       | Descrição              | Próximo |
|------------|------------------------|---------|
| EM_PREPARO | Pedido em preparação   | PRONTO  |
| PRONTO     | Pronto para entrega    | ENTREGUE|
| ENTREGUE   | Pedido finalizado      | —       |

## Execução
### Pré-requisitos
- JDK instalado (verifique com `java -version`)
- Maven instalado (verifique com `mvn -version`)

### Compilar
```powershell
mvn -f demo/pom.xml clean compile
```

### Executar (JavaFX)
Opção 1 — via plugin JavaFX (se configurado no `pom.xml`):
```powershell
mvn -f demo/pom.xml javafx:run
```
Opção 2 — via exec-maven-plugin (se configurado):
```powershell
mvn -f demo/pom.xml -q exec:java -Dexec.mainClass=com.projeto.App
```
Opção 3 — manualmente, especificando JavaFX no module path:
```powershell
$fx="C:\\javafx-sdk-21\\lib"
java --module-path $fx --add-modules javafx.controls,javafx.fxml -cp target/classes com.projeto.App
```
(Substitua `path\para\javafx\lib` pelo caminho real das libs JavaFX.)

## Decisões de Arquitetura
- Separação cliente vs admin por pacotes para isolar responsabilidades
- `Store` como singleton simplifica compartilhamento de estado (em ambiente real poderia ser serviço injetado)
- Enum de status encapsula lógica de avanço, evitando condicionais dispersas
- FXML mantém declaração de layout; controladores focam em lógica

## Testes
O projeto inclui testes JUnit 5 em `src/test/java` cobrindo:
- `Pedido.calcularTotal()` – soma dos subtotais dos itens
- `StatusPedido.proximo()` – avanço de status (EM_PREPARO → PRONTO → ENTREGUE)
- `Store.novoPedido()` – criação do pedido atual e vínculo ao cliente

Para executar os testes:
```powershell
mvn -f demo/pom.xml test
```

Saída esperada: todos os testes passando. Exemplo:
```
Tests run: 5, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

Caso utilize um IDE, certifique-se de selecionar o perfil JDK 21 e o runner JUnit 5.

