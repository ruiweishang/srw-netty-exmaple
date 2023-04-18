package srw.netty.example.blockingqueue.enums;

/**
 * @author shangruiwei
 * @date 2023/4/18 19:35
 */
public enum CommandEnum {

    OFFER("offer"),

    POLL("poll");

    private String code;

    CommandEnum(String code) {
        this.code = code;
    }

    public static CommandEnum getByCode(String code) {
        for (CommandEnum value : values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
