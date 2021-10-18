package pucrs.redes.server.game;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Game {
    private String dificult;
    private Map<String, List<String>> questions;
    private Set<String> solvedQuestions;
    private String lastQuestion;
    private int gameState;
    private int rightQuestions;

    public Game(){
        this.questions = new HashMap<>();
        this.solvedQuestions = new HashSet<>();
        this.gameState = -1;
        this.rightQuestions = 0;
    }

    
    private Map<String, List<String>> loadQuestions(){
        
        try(BufferedReader br = new BufferedReader(new FileReader("questions/" + dificult + ".txt"))) {
            String line = br.readLine();
        
            while (line != null) {
                String[] tokens = line.split(";");
                String question = tokens[0];
                List<String> answers = Arrays.asList(Arrays.copyOfRange(tokens, 1, tokens.length));
                questions.put(question, answers);
                line = br.readLine();
            }

        } catch (Exception e){
            System.out.println(e);
        }
        return questions;
    }

    public void chooseDificultLevel(String dificult){
        this.dificult = dificult;
        this.gameState = 0;
        loadQuestions();
    }

    public String newQuestion(){
        solvedQuestions.add(this.lastQuestion);
        for (String question : questions.keySet()) {
            if(!solvedQuestions.contains(question)){
                List<String> answers = questions.get(question);
                return question + "\n\n" + answers.subList(0, answers.size() - 1).toString();
            }
        }
        return "end game";
    }

    public double getPoints(){
        return rightQuestions * 1.0 / questions.size() * 100.0; 
    }

    public boolean verifyAnswer(String answer){
        List<String> answers = questions.get(this.lastQuestion);
        String rightAnswer = answers.get(answer.length() - 1);
        if(answer.equals(rightAnswer)){
            this.rightQuestions += 1;
            return true;
        }
        return false;
    }

    
}
