# usado para vitualizar o projeto dentro de um container
# ele poderá rodar em outra máquina sem precisar instalar, baixar e rodar.
# Para executar este arquivo: deve ser adicionado no orquestrador de container, ou seja, no arquivo docker-compose.yml

# instalar uma imagem do JDK 21 (versão do java usada para compilar)
FROM openjdk:21

# Criar uma pasta para onde o docker i´ra copiar os arquivos necessários para dentro deste "pacote"
WORKDIR /app

# copiar os arquivos do projeto dentro da pasta acima
COPY . /app

# comando para realizar deploy do projeto, compilar o projeto e implantar (publicado) dentro do container
# Implanta dentro do docker e substitui as variáveis do application pelas informações no docker-compose
RUN ./mvnw -B -e clean package -DskipTests

# identificar a porta em que o projeto será executado (dentro do container)
EXPOSE 8081

# comando para executar o projeto depois do deploy
# a informação de versão fica no pom
CMD ["java","-jar","target/apiPedidos-0.0.1-SNAPSHOT.jar"]
