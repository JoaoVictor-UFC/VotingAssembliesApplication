# VotingAssembliesApplication

 <p id="sobre" align="center">

📱 Descrição 
  
Projeto de um sistema simples de votação onde conseguimos criar uma sessao de votação
  cadastrar usuários e ve o resultado dessas votações.

Tabela de conteúdos 
================= 
<!--ts-->
 * [Sobre](#sobre) 
  
 *  * [Documentação](#documentacao)
 *  * [Pre Requisitos](#pre-requisitos)
 *  * [Executando a aplicação](#rodando)
 * * [Tecnologias](#tecnologias)
 *  * [Autor](#autor)
 <!--te-->

### Documentação<a id="documentacao"></a>
 
 Documentaçao do projeto foi gerada pelo Swagger-ui
 Então depois de roda a aplicação acesse http://localhost:7001/swagger-ui/
 
### 🛒 Pré-requisitos<a id="pre-requisitos"></a>

  Para roda a aplicação bastar ter o docker instalado na sua maquina:
 [Docker](https://www.docker.com/)
 
 Caso queira gerenciar o banco, recomendo usar o [DBeaver Community](https://dbeaver.io/)
 Todas as variaveis de ambiente são gerenciadas pelo docker-compose na pasta deploy.
 
   ### 📀Rodando a Aplicação<a id="rodando"></a>
   
````bash 
 # Clone este repositório
 git clone [projeto](https://github.com/JoaoVictor-UFC/VotingAssembliesApplication.git)
 
 # Acesse a pasta do projeto no terminal
 cd VotingAssembliesApplication/deploy/
 
 # Rode o comando docker-compose up -d
  Isso ira criar 3 containers com postgres e o da aplicação e o que validar o CPF ou CNPJ.
  
 # Tambem é possivel baixar a imagem do projeto no Docker Hub
  docker push johnnykeys/cpf_validate:1.0
  docker push johnnykeys/voting_assemblies:1.0
 
 # O servidor iniciará na porta:7001
 # Acesse http://localhost:7001
  Utilizando o postman, deixei as colletions no repositorio dentro da pasta deploy,
  Ou usando a tag do Swagger voce consegue criar todas as rotas.
 ````
 
### 🛠 Tecnologias<a id="tecnologias"></a>
 As seguintes ferramentas foram usadas na construção do projeto:
 
  - [Java 17](https://www.oracle.com/br/java/technologies/downloads/#java17)
  - [PostgreSQL](https://www.postgresql.org/)
  - [Spring Boot](https://spring.io/projects/spring-boot)

### 👨‍💻Autor <a id="autor"> </a>

---
<a href="https://github.com/JoaoVictor-UFC" style="text-decoration: none;">
<img style="border-radius: 50%;" src="https://avatars.githubusercontent.com/u/56576465?v=4" width="100px;"  alt="João Victor"/>

<br />
<span> Feito por João Victor 
  Data:12/2022! </span> 
</a> 

