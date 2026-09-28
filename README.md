# SSE Project

Exemplo de distribuição de mensagens do Apache Kafka para clientes conectados por Server-Sent Events (SSE).

## Executar localmente

Requisitos: Docker com o comando `docker compose` e uma versão de Java compatível com o projeto.

1. Inicie o Kafka:

   ```bash
   docker compose up -d
   ```

2. Crie o tópico `demo`:

   ```bash
   docker exec sse-kafka /opt/kafka/bin/kafka-topics.sh \
     --create --if-not-exists --topic demo --bootstrap-server localhost:9092
   ```

3. Em outro terminal, inicie a aplicação:

   ```bash
   ./mvnw spring-boot:run
   ```

4. Abra uma conexão SSE para acompanhar eventos:

   ```bash
   curl -N http://localhost:8080/news
   ```

5. Em outro terminal, publique mensagens no Kafka:

   ```bash
   docker exec -i sse-kafka /opt/kafka/bin/kafka-console-producer.sh \
     --topic demo --bootstrap-server localhost:9092
   ```

   Digite uma mensagem por linha e pressione Enter. Ela deverá aparecer no terminal do `curl` como um evento `NEWS`.

Para encerrar o broker, execute `docker compose down`.

## Fluxo

`KafkaConsumer` recebe cada registro do tópico `demo` e chama `NewsBroadcaster.publish`. O serviço envia a mensagem a cada `SseEmitter` ativo. O endpoint `GET /news` cria e registra um emissor por conexão; ao fechar ou falhar a conexão, o emissor é removido.

Esta implementação transmite mensagens novas enquanto o cliente está conectado. Kafka mantém os registros conforme a configuração do broker, mas o endpoint SSE não consulta mensagens anteriores para um assinante novo.
