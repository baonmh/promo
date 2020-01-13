package promo_creation.objects;

import org.apache.commons.lang3.StringUtils;
import promo_creation.utils.OtpUtil;

public class Credential {
    String username;
    String key;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Credential(String csvLine) {
        String[] splitter = StringUtils.split(csvLine, ",");
        if (splitter.length != 2) throw new RuntimeException();
        this.username = splitter[0];
        this.key = splitter[1];
    }

}
