package pucrs.redes.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pucrs.redes.network.Message;
import pucrs.redes.network.MessageData;
import pucrs.redes.network.NetworkPacketManager;
import pucrs.redes.network.ServerType;
import pucrs.redes.server.game.Game;

public class Server extends NetworkPacketManager {

    public Map<InetSocketAddress, CurrentPlayerState> states;
    private List<MessageHandler> handlers;
     // dict with port of player, the game and current state of game
    private Map<Integer, Game> games;

    public Server() {
        super(NetworkPacketManager.SERVER_PORT, ServerType.SERVER);
        states = new HashMap<>();
    }

    @Override
    protected void handleMessage(Message message) throws IOException {
        String messageData = message.getMessageData().getData();
        System.out.println("Message received from: " + message.getFrom().toString() + " : " + messageData);
        sendMessage(MessageData.buildMessage("Received"), message.getFrom());

        treatMessage(message);
    }

    public void treatMessage(Message message) throws IOException{
        String data = message.getMessageData().getData().toLowerCase();
        
        if(data.equals("init")){
            if (initGameToClient(message.getFrom().getPort())){
                sendMessage(MessageData.buildMessage("Game started, please send a dificult level\n\n easy, medium or hard?"),
                            message.getFrom());
            } else{
                sendMessage(MessageData.buildMessage("Error game already started"), message.getFrom());
            }
        }
        else if(data.equals("easy") || data.equals("medium") || data.equals("hard")){
            if(chooseDificultLevel(message)){
                sendMessage(MessageData.buildMessage("Game started with the choosed dificult"), message.getFrom());
                String question = getNewQuestion(message);
                sendMessage(MessageData.buildMessage(question), message.getFrom());
            } else{
                sendMessage(MessageData.buildMessage("Error game already have a dificult level"), message.getFrom());
            }
        } else if(data.equals("a") || data.equals("b") || data.equals("c") || data.equals("d") ){
            if(checkAnswer(message)){
                sendMessage(MessageData.buildMessage("Good job!"), message.getFrom());
            } else{
                sendMessage(MessageData.buildMessage("better next time!"), message.getFrom());
            }
            String question = getNewQuestion(message);
            if(question.equalsIgnoreCase("end game")){
                Game game = games.get(message.getFrom().getPort());
                sendMessage(MessageData.buildMessage("\n\nGame Over\n\n your accuracy: " + game.getPoints()), message.getFrom());
            } else{
                sendMessage(MessageData.buildMessage(question), message.getFrom());
            }
        }
        else{
            sendMessage(MessageData.buildMessage("Error invalid option"), message.getFrom());
        }
                
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

    @Override
    protected void callWhenStop() throws IOException {

    }
}
