package flashcards;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        String variableImport = "-import";
        String variableExport = "-export";
        String fileImport = "";
        String fileExport = "";
        for (int i = 0; i < args.length; i = i + 2) {
            if(variableImport.equals(args[i])) {
                fileImport = args[i + 1];
            } else if(variableExport.equals(args[i])) {
                fileExport = args[i + 1];
            }
        }
        Logger logger = new Logger(new ArrayList<>());
        logger.add(" ");
        logger.add(" ");
        Scanner scanner = new Scanner(System.in);
        if(fileImport.length()>4) {
            importFile(Card.cards, logger, fileImport);
        }
        while (true) {
            String inputMsg = "Input the action (add, remove, import, export, ask, exit, log, hardest card, reset stats):";
            System.out.println(inputMsg);
            logger.add(inputMsg);
            String input = scanner.nextLine();
            logger.add(input);
            switch (input) {
                case "add":
                    add(Card.cards, logger);
                    break;
                case "remove":
                    remove(Card.cards, logger);
                    break;
                case "import":
                    importFile(Card.cards, logger);
                    break;
                case "export":
                    export(Card.cards, logger);
                    break;
                case "ask":
                    ask(Card.cards, logger);
                    break;
                case "exit":
                    String byeMsg = "Bye bye!";
                    System.out.println(byeMsg);
                    logger.add(byeMsg);
                    if(fileExport.length()>4) {
                        export(Card.cards, logger, fileExport);
                    }
                    System.exit(0);
                case "log":
                    log(logger);
                    break;
                case "hardest card":
                    getHardestCard(Card.cards, logger);
                    break;
                case "reset stats":
                    resetStats(Card.cards, logger);
                    break;
                default:
                    break;
            }
        }
    }


    public static String checkDefinition(List<Card> cards, String search) {
        for (Card card : cards) {
            if (card.getDefinition().equals(search)) {
                return card.getTerm();
            }
        }
        return null;
    }

    public static String checkTerm(List<Card> cards, String search) {
        for (Card card : cards) {
            if (card.getTerm().equals(search)) {
                return card.getDefinition();
            }
        }
        return null;
    }

    public static void replace(List<Card> cards, Card card) {
        for (int i = 0; i < cards.size(); i++) {
            if (cards.get(i).getTerm().equals(card.getTerm())) {
                cards.add(i, card);
                break;
            } else if (cards.get(i).getDefinition().equals(card.getDefinition())) {
                cards.add(i, card);
                break;
            }
        }
    }

    public static void giveCard(List<Card> cards, Card card, Logger logger) {
        Scanner sc = new Scanner(System.in);
        String printMsg = "Print the definition of \"" + card.getTerm() + "\" :";
        System.out.println(printMsg);
        logger.add(printMsg);
        String definitionInput = sc.nextLine();
        logger.add(definitionInput);
        if (definitionInput.equals(card.getDefinition())) {
            String correctMsg = "Correct!";
            System.out.println(correctMsg);
            logger.add(correctMsg);
        } else if (checkDefinition(cards, definitionInput) != null) {
            String correctValue = checkDefinition(cards, definitionInput);
            card.upError();
            String wrongRightMsg = "Wrong. The right answer is \"" + card.getDefinition() + "\", " +
                    "but your definition is correct for \"" + correctValue + "\"";
            System.out.println(wrongRightMsg);
            logger.add(wrongRightMsg);
        } else {
            card.upError();
            String wrongMsg = "Wrong. The right answer is \"" + card.getDefinition() + "\".";
            System.out.println(wrongMsg);
            logger.add(wrongMsg);
        }
    }

    public static void add(List<Card> cards, Logger logger) {
        String term;
        String definition;
        Scanner sc = new Scanner(System.in);
        String theCardMsg = "The card:";
        System.out.println(theCardMsg);
        logger.add(theCardMsg);
        term = sc.nextLine();
        logger.add(term);
        if (checkTerm(cards, term) != null) {
            String termExistMsg = "The card \"" + term + "\" already exists";
            System.out.println(termExistMsg);
            logger.add(termExistMsg);
        } else {
            String definitionCardMsg = "The definition of the card:";
            System.out.println(definitionCardMsg);
            logger.add(definitionCardMsg);
            definition = sc.nextLine();
            logger.add(definition);
            String message;
            if (checkDefinition(cards, definition) != null) {
                message = "The definition \"" + definition + "\" already exists.";
            } else {
                cards.add(new Card(term, definition, 0));
                message = "The pair (\"" + term + "\":\"" + definition + "\") has been added";
            }
            System.out.println(message);
            logger.add(message);
        }
    }

    public static void remove(List<Card> cards, Logger logger) {
        String term;
        Scanner sc = new Scanner(System.in);
        String whichMsg = "Which card?";
        System.out.println(whichMsg);
        logger.add(whichMsg);
        term = sc.nextLine();
        logger.add(term);
        String message = "Can't remove \"" + term + "\": there is no such card.";
        for (int i = 0; i < cards.size(); i++) {
            if (cards.get(i).term.equals(term)) {
                cards.remove(i);
                message = "The card has been removed.";
                break;
            }
        }
        System.out.println(message);
        logger.add(message);
    }

    public static void importFile(List<Card> cards, Logger logger) {
        Scanner sc = new Scanner(System.in);
        List<String> array = new ArrayList<>();
        String term;
        String definition;
        String fileNameMsg = "File name:";
        System.out.println(fileNameMsg);
        logger.add(fileNameMsg);
        String filePath = sc.nextLine();
        logger.add(filePath);
        File file = new File(filePath);
        if (file.exists()) {
            try (BufferedReader in = new BufferedReader(new FileReader(filePath))) {
                array = in.lines().collect(Collectors.toList());
            } catch (IOException | NumberFormatException e) {
                e.printStackTrace();
            }
            if (array.size() == 0) {
                String importFailedMsg = "Import failed. Empty file";
                System.out.println(importFailedMsg);
                logger.add(importFailedMsg);
            } else {
                for (String s : array) {
                    String[] split = s.split(":");
                    term = split[0];
                    definition = split[1];
                    int error = Integer.parseInt(split[2]);
                    Card card = new Card(term, definition, error);
                    if (checkTerm(cards, term) != null || checkDefinition(cards, definition) != null) {
                        replace(cards, card);
                    } else {
                        cards.add(card);
                    }
                }
                String cardsLoadedMsg = array.size() + " cards have been loaded.";
                System.out.println(cardsLoadedMsg);
                logger.add(cardsLoadedMsg);
            }
        } else {
            String fnfMsg = "File not found.";
            System.out.println(fnfMsg);
            logger.add(fnfMsg);
        }
    }

    public static void importFile(List<Card> cards, Logger logger, String filePath) {
        List<String> array = new ArrayList<>();
        String term;
        String definition;
        File file = new File(filePath);
        if (file.exists()) {
            try (BufferedReader in = new BufferedReader(new FileReader(filePath))) {
                array = in.lines().collect(Collectors.toList());
            } catch (IOException | NumberFormatException e) {
                e.printStackTrace();
            }
            if (array.size() == 0) {
                String importFailedMsg = "Import failed. Empty file";
                System.out.println(importFailedMsg);
                logger.add(importFailedMsg);
            } else {
                for (String s : array) {
                    String[] split = s.split(":");
                    term = split[0];
                    definition = split[1];
                    int error = Integer.parseInt(split[2]);
                    Card card = new Card(term, definition, error);
                    if (checkTerm(cards, term) != null || checkDefinition(cards, definition) != null) {
                        replace(cards, card);
                    } else {
                        cards.add(card);
                    }
                }
                String cardsLoadedMsg = array.size() + " cards have been loaded.";
                System.out.println(cardsLoadedMsg);
                logger.add(cardsLoadedMsg);
            }
        } else {
            String fnfMsg = "File not found.";
            System.out.println(fnfMsg);
            logger.add(fnfMsg);
        }
    }

    public static void export(List<Card> cards, Logger logger) {
        Scanner sc = new Scanner(System.in);
        int count = 0;
        String fileNameMsg = "File name:";
        System.out.println(fileNameMsg);
        logger.add(fileNameMsg);
        String filePath = sc.nextLine();
        logger.add(filePath);
        try {
            File file = new File(filePath);
            if (file.createNewFile()) {
                String fileCreatedMsg = "File is created!";
                System.out.println(fileCreatedMsg);
                logger.add(fileCreatedMsg);
            } else {
                String fileExistMsg = "File already exists.";
                System.out.println(fileExistMsg);
                logger.add(fileExistMsg);
            }
            FileWriter writer = new FileWriter(filePath, false);
            for (Card card : cards) {
                writer.write(card.getTerm() + ":" + card.getDefinition() + ":" + card.getError());
                writer.append('\n');
                count++;
            }
            writer.flush();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            logger.add(ex.getMessage());
        }
        String cardsSavedMsg = count + " cards have been saved.";
        System.out.println(cardsSavedMsg);
        logger.add(cardsSavedMsg);
    }

    public static void export(List<Card> cards, Logger logger, String filePath) {
        int count = 0;
        try {
            File file = new File(filePath);
            if (file.createNewFile()) {
                String fileCreatedMsg = "File is created!";
                System.out.println(fileCreatedMsg);
                logger.add(fileCreatedMsg);
            } else {
                String fileExistMsg = "File already exists.";
                System.out.println(fileExistMsg);
                logger.add(fileExistMsg);
            }
            FileWriter writer = new FileWriter(filePath, false);
            for (Card card : cards) {
                writer.write(card.getTerm() + ":" + card.getDefinition() + ":" + card.getError());
                writer.append('\n');
                count++;
            }
            writer.flush();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            logger.add(ex.getMessage());
        }
        String cardsSavedMsg = count + " cards have been saved.";
        System.out.println(cardsSavedMsg);
        logger.add(cardsSavedMsg);
    }

    public static void ask(List<Card> cards, Logger logger) {
        Scanner sc = new Scanner(System.in);
        String howManyAskMsg = "How many times to ask?";
        System.out.println(howManyAskMsg);
        logger.add(howManyAskMsg);
        int count = sc.nextInt();
        logger.add(count + "");
        int i = 0;
        while (i < count) {
            for (Card card : cards) {
                giveCard(cards, card, logger);
                i++;
                if (i >= count) {
                    break;
                }
            }
        }
    }

    public static void log(Logger logger) {
        Scanner sc = new Scanner(System.in);
        String fileNameMsg = "File name:";
        System.out.println(fileNameMsg);
        logger.add(fileNameMsg);
        String fileName = sc.nextLine();
        logger.add(fileName);
        File file = new File(fileName);//"/Users/mkebets/IdeaProjects/Flashcards/Flashcards/task/src/flashcards/" +
        try(FileWriter writer = new FileWriter(file)) {
            for(String str : logger.getLogger()) {
                writer.write(str);
                writer.append('\n');
            }
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String logSavedMsg = "The log has been saved.";
        System.out.println(logSavedMsg);
        logger.add(logSavedMsg);

    }

    public static void getHardestCard(List<Card> cards, Logger logger) {
        int max = Card.getMaxNumError();
        if (cards.size() == 0 || max == 0) {
            String noCardsMsg = "There are no cards with errors";
            System.out.println(noCardsMsg);
            logger.add(noCardsMsg);
        } else {
            ArrayList<String> hardestCard = new ArrayList<>();
            for (Card card : cards) {
                if (card.getError() == max) {
                    hardestCard.add(card.getTerm());
                }
            }
            if (hardestCard.size() == 1) {
                String oneCardMsg = "The hardest card is \"" + hardestCard.get(0) + "\". You have \"" + max + "\" errors answering it.";
                System.out.println(oneCardMsg);
                logger.add(oneCardMsg);
            } else {
                StringBuilder out = new StringBuilder("The hardest cards are ");
                for (String str : hardestCard) {
                    out.append("\"").append(str).append("\"");
                    if (hardestCard.size() != (hardestCard.indexOf(str) + 1)) {
                        out.append(", ");
                    }
                }
                out.append(". You have ").append(max).append(" errors answering them.");
                System.out.println(out);
                logger.add(out.toString());
            }
        }
    }

    public static void resetStats(List<Card> cards, Logger logger) {
        for (Card card : cards) {
            card.setError(0);
        }
        String cardStatMsg = "Card statistics have been reset.";
        System.out.println(cardStatMsg);
        logger.add(cardStatMsg);
    }
}
