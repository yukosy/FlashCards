package flashcards;

import java.util.ArrayList;
import java.util.List;

public class Logger {
    List<String> logger = new ArrayList<>();

    public Logger(List<String> logger) {
        this.logger = logger;
    }

    public List<String> getLogger() {
        return logger;
    }

    public void add(String string) {
        this.logger.add(string);

    }
}
