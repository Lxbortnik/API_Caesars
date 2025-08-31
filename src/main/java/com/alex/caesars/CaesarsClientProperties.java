package com.alex.caesars;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "caesars")
public class CaesarsClientProperties {
    private String host;
    private Paths paths = new Paths();

    public static class Paths{
        private String auth;
        private String wallet;
        private String playerInfo;
        private String session;


        public String getAuth() { return auth; }
        public void setAuth(String auth) { this.auth = auth; }
        public String getWallet() { return wallet; }
        public void setWallet(String wallet) { this.wallet = wallet; }
        public String getPlayerInfo() { return playerInfo; }
        public void setPlayerInfo(String playerInfo) { this.playerInfo = playerInfo; }
        public String getSession() { return session; }
        public void setSession(String session) { this.session = session; }
    }

}
