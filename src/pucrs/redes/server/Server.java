package pucrs.redes.server;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pucrs.redes.network.Message;
import pucrs.redes.network.MessageData;
import pucrs.redes.network.NetworkPacketManager;
import pucrs.redes.network.ServerType;
import pucrs.redes.server.game.Game;

public class Server extends NetworkPacketManager {

    private List<MessageHandler> handlers;
     // dict with port of player, the game and current state of game
    private Map<Integer, Game> games;

    public Server() {
        super(NetworkPacketManager.SERVER_PORT, ServerType.SERVER);
        this.games = new HashMap<>();
    }

    @Override
    protected void callWhenStop(Message message) {
        int port = message.getFrom().getPort();
        System.out.println("Removing " + port + " from game " + games.size());
        games.remove(port);
        System.out.println("after removed " + games.size());
    }

    @Override
    protected void handleMessage(Message message) throws IOException {
        String messageData = message.getMessageData().getData();
        System.out.println("Message received from: " + message.getFrom().toString() + " : " + messageData);

        try {
            treatMessage(message);
        } catch (RuntimeException e) {
            e.printStackTrace();
            System.out.println("Error dealing with some kind of message, ending gaming for " + message
                    .getFrom());
            sendMessage(MessageData.buildMessage("System error, stopping the game").toFinish(), message.getFrom());
        }
    }

    public void treatMessage(Message message) throws IOException{
        String data = message.getMessageData().getData().toLowerCase();

        if(data.equals("init")){
            if (initGameToClient(message.getFrom().getPort())){
                sendMessage(MessageData.buildMessage("Game started, please send a difficult level\n\n easy, medium or hard?"),
                        message.getFrom());
            } else{
                sendMessage(MessageData.buildMessage("Error game already started"), message.getFrom());
            }
        }
        else if(data.equals("easy") || data.equals("medium") || data.equals("hard")){
            if(chooseDificultLevel(message)){
                String question = getNewQuestion(message);
                String sendMessage = "Game started with the chosen difficult\n\n" + question;
                sendMessage(MessageData.buildMessage(sendMessage), message.getFrom());
            } else{
                sendMessage(MessageData.buildMessage("Error game already have a difficult level").toFinish(),
                        message.getFrom());
            }
        } else if(data.equals("a") || data.equals("b") || data.equals("c") || data.equals("d") ){
            String msg = "";
            if(checkAnswer(message)){
                msg += "Good Job!\n\n";
            } else{
                String rightAnswer = getRightAnswer(message.getFrom().getPort());
                msg += "Wrong, the right answer was " + rightAnswer + " study harder to be better on the next time!\n\n";
            }
            String question = getNewQuestion(message);
            if(question.equalsIgnoreCase("end game")){
                Game game = games.get(message.getFrom().getPort());
                sendMessage(MessageData.buildMessage(msg + "\n\nGame Over\n\n your accuracy: " + game.getPoints()).toFinish(),
                        message.getFrom());
            } else{
                sendMessage(MessageData.buildMessage(msg + question), message.getFrom());
            }
        }
        else{
            sendMessage(MessageData.buildMessage("Error invalid option").toFinish(), message.getFrom());
        }

    }

    private String getRightAnswer(int port) {
        return games.get(port).getAnswerOfLastQuestion();
    }

    public boolean initGameToClient(int port){
        if(games.containsKey(port)){
            return false;
        }
        games.put(port, new Game());
        return true;
    }

    private boolean portHasGame(int port){
        return games.containsKey(port);
    }

    public boolean chooseDificultLevel(Message message) throws IOException{
        boolean hasGame = portHasGame(message.getFrom().getPort());
        if(!hasGame){
            return false;
        }
        Game game = games.get(message.getFrom().getPort());
        game.chooseDificultLevel(message.getMessageData().getData());
        return true;

    }

    public boolean checkAnswer(Message message){
        if(!portHasGame(message.getFrom().getPort())){
            return false;
        }
        Game game = games.get(message.getFrom().getPort());
        return game.verifyAnswer(message.getMessageData().getData());
    }

    public String getNewQuestion(Message message){
        if(!portHasGame(message.getFrom().getPort())){
            return "Invalid game port, try init a new game or choose a dificult level";
        }
        Game game = games.get(message.getFrom().getPort());
        return game.newQuestion();
    }
}
