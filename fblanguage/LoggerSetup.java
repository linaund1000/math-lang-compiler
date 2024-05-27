package hellyeah;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;


public class LoggerSetup {
    public static Logger getLogger() {
        Logger logger = Logger.getLogger("ASTLogger");
        try {
            FileHandler fileHandler = new FileHandler("ast.log", true);
            fileHandler.setFormatter(new SimpleMessageFormatter());
            logger.addHandler(fileHandler);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return logger;
    }
}


class SimpleMessageFormatter extends Formatter {
    @Override
    public String format(LogRecord record) {
        return record.getMessage() + "\n";
    }
}

//logger kullanilmayabilir gelistirme surecinde lazum oluyodu sadece