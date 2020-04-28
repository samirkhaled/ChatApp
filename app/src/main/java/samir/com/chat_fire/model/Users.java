package samir.com.chat_fire.model;

public class Users {

   private  String userName;
    private  String Email;
    private  String image;
    private  String userId;

    public Users(String userName, String email, String image, String userId) {
        this.userName = userName;
        Email = email;
        this.image = image;
        this.userId = userId;
    }

    public Users() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
