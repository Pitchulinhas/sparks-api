# Sparks API

## Descrição

API para gerenciamento dos recursos do e-commerce "Sparks".

## Configuração

### Variáveis de ambiente

As variáveis de ambiente abaixo devem ser definidas para que a aplicação funcione corretamente:

- PORT: Porta em que a aplicação será executada. Por exemplo: 8081
- KAFKA_BROKERS: Endereço IP dos brokers Kafka separados por vírgula. Por exemplo: localhost:9092,localhost:9093

## Instalação

Para instalar as dependências do projeto é necessário que tenha o Maven instalado em sua máquina ou utilize uma IDE que o tenha. Com isso em mãos, para instalar as dependências, basta executar o comando abaixo na raiz do projeto:

```bash
mvn clean install
```

## Build

Para buildar o projeto basta executar o comando abaixo na raiz do projeto:

```bash
mvn package
```

## Execução

Para executar o arquivo jar gerado basta executar o comando abaixo dentro da pasta **_target_** do projeto:

```bash
java -jar api-<versao>.jar
```
