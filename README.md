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

# Principais Componentes

- `App` (entrada da aplicação): inicializa o JavaFX, carrega as primeiras views (`home.fxml`) e configura o contexto necessário.
- `Store` (serviços e estado): atua como uma "fachada" simples que centraliza operações do domínio (catálogo, carrinho, pedidos) e fornece métodos usados pelos controllers.
- Controllers (camada de controle):
  - `HomeController`, `ProdutosController`, `CarrinhoController`, `ConfirmacaoController` (cliente): coordenam interação entre a UI e os modelos, acionando a `Store`.
  - `AdminController` (admin): operações administrativas como cadastro/atualização de produtos.
- Models (camada de domínio):
  - `Produto`, `Cliente`, `Pedido`, `ItemPedido`, `StatusPedido` (enum): representam entidades e regras básicas do negócio.
- Views/Recursos (UI): arquivos FXML (`home.fxml`, `produtos.fxml`, `carrinho.fxml`, `confirmacao.fxml`, `admin.fxml`), estilos (`styles.css`) e imagens dos produtos.
- Testes: classes JUnit em `src/test/java` (`StoreTest`, `PedidoTest`, `StatusPedidoTest`) validam regras essenciais do domínio.

# Decisões de Modelagem

- Padrão MVC simplificado: separação entre Models (lógica e dados), Controllers (orquestração e ligação com UI) e Views (FXML/CSS). Facilita manutenção e testes.
- Enum para estados de pedido: `StatusPedido` garante valores controlados (ex.: PENDENTE, CONFIRMADO), reduzindo erros de strings mágicas e melhorando legibilidade.
- Composição em `Pedido` e `ItemPedido`: um `Pedido` agrega `ItemPedido` (produto + quantidade + preço) para refletir a relação natural do carrinho. Evita acoplamento excessivo entre UI e domínio.
- Imutabilidade seletiva: valores como preço unitário do `Produto` e identificadores tendem a ser estáveis; alterações passam por métodos específicos na `Store` ou em controllers, mantendo invariantes.
- Coleções internas e encapsulamento: uso de listas/mapas para catálogo e carrinho é mediado pela `Store`, evitando exposição direta de estruturas mutáveis e simplificando validação.
- Recursos externos organizados: imagens e arquivos FXML ficam em `src/main/resources/...` para facilitar empacotamento via Maven e compatibilidade com JavaFX.
- Testes de unidade focados no domínio: regras como cálculo de total do pedido e transições de `StatusPedido` são cobertas por testes, incentivando TDD leve e segurança em refatorações.
- Módulos e `module-info.java`: mantém compatibilidade com JPMS, declarando dependências e melhorando segurança/encapsulamento onde possível.

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

