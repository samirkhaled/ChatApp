package samir.com.chat_fire.Adapters;

public class Chat_msgs {

  private String sender;
  private String massage;
  private String receiver;

    public Chat_msgs(String sender, String massage, String receiver) {
        this.sender = sender;
        this.massage = massage;
        this.receiver = receiver;
    }

    public Chat_msgs() {
    }


    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMassage() {
        return massage;
    }

    public void setMassage(String massage) {
        this.massage = massage;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }
}
