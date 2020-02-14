package main.DataStructs;

public class Machine {
    private String host;
    private String user;
    private String password;

    public Machine(String host, String user, String password){
        this.host = host;
        this.user = user;
        this.password = password;
    }

    public Machine(){
        this.host = "";
        this.user = "";
        this.password = "";
    }

    public String getHost() {
        return host;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
