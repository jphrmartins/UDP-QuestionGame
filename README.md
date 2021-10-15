# UDP-QuestionGame

Sistema criado para realizar a comunicação entre servidor e cliente, através da rede.
Utilizando o protocolo de comunicação UDP.
Como requisitos do trabalho, era necessário realizar os seguintes pontos:

- [x] Realizar a comunicação através do UDP
- [x] Ser capaz de possuir multiplos clientes conectados a um unico servidor
- [x] Relizar mecanismo de retentativas em caso de perda de pacotes

# Só quero rodar isso, pela amor de deus.

execute no seu terminal:
```
$ ./scripts/compile.sh
```

assim o sistema será compilado
logo em seguida execute o comando para subir o servidor
```
$ ./scripts/startServer.sh
```

e então, rode o comando em outro terminal
```
$ ./scripts/runClient.sh 1235
```
Passando a porta como argumento, no exemplo acima a porta é 1235
