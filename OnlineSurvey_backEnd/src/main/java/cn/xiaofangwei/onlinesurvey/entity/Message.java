package cn.xiaofangwei.onlinesurvey.entity;

import lombok.Data;

@Data
public class Message {

    /**
     * @description 返回编码：错误：0；正常：1
     */
    private Integer code;

    private Object data;
    private String description;

    void message() {

    }

    public static Message error() {
        Message message = new Message();
        message.setCode(0);
        message.setDescription("请求错误，请重试！");
        return message;
    }

    public static Message error(String description) {
        Message message = new Message();
        message.setCode(0);
        message.setDescription(description);
        return message;
    }

    public static Message info() {
        Message message = new Message();
        message.setCode(1);
        message.setDescription("请求成功！");
        return message;
    }

    public static Message info(String description) {
        Message message = new Message();
        message.setCode(1);
        message.setDescription(description);
        return message;
    }

    public static Message info(String description, Object data) {
        Message message = new Message();
        message.setCode(1);
        message.setDescription(description);
        message.setData(data);
        return message;
    }


}
