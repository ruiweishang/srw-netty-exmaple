package srw.netty.example.blockingqueue.model;

import java.io.Serializable;

/**
 * @author shangruiwei
 * @date 2023/4/16 19:35
 */
public class Command implements Serializable {

    private String command;
    private String content;

    public Command(String command, String content) {
        this.command = command;
        this.content = content;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
