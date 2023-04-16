package srw.netty.example.blockingqueue.model;

import java.io.Serializable;

/**
 * @author shangruiwei
 * @date 2023/4/16 19:45
 */
public class Result implements Serializable {

    private Integer code;
    private String content;

    public Result() {

    }

    public Result(Integer code, String content) {
        this.code = code;
        this.content = content;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
