package org.microservices.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.DefaultExpiringOAuth2RefreshToken;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

/**
 * @author xiangqian
 * @date 21:39 2022/06/30
 */
public class OAuth2AccessTokenSerializerTest {

    @Test
    public void test() throws IOException {
        DefaultOAuth2AccessToken defaultOAuth2AccessToken = new DefaultOAuth2AccessToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsib3JkZXItYml6IiwidXNlci1iaXoiXSwiZXhwIjoxNjU2NjAzMTQxLCJ1c2VyX25hbWUiOiJhZG1pbiIsImp0aSI6IlJDbndybkZ6ZXRIN0x1aUs4NkxyQ2lUeEVUcyIsImNsaWVudF9pZCI6ImNsaWVudF8xIiwic2NvcGUiOlsicmVhZCJdfQ.QpL666SuWnKlxxNc-5r_S7OhBACHhoTO7DWrzIn5Ta0");
        defaultOAuth2AccessToken.setRefreshToken(new DefaultExpiringOAuth2RefreshToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsib3JkZXItYml6IiwidXNlci1iaXoiXSwidXNlcl9uYW1lIjoiYWRtaW4iLCJzY29wZSI6WyJyZWFkIl0sImF0aSI6IlJDbndybkZ6ZXRIN0x1aUs4NkxyQ2lUeEVUcyIsImV4cCI6MTY1Njg1NTE0MSwianRpIjoiLTdQZDhwZWZyU1NlWlUzb2NDa0tuV1IzSmU4IiwiY2xpZW50X2lkIjoiY2xpZW50XzEifQ.lix39Jp2bs8CVqa938DyIGYfqxvARu5aFXXcrA6T6C8", new Date()));
        defaultOAuth2AccessToken.setScope(Sets.newHashSet("read"));
        defaultOAuth2AccessToken.setAdditionalInformation(Map.of("jti", "RCnwrnFzetH7LuiK86LrCiTxETs"));
        System.out.println("defaultOAuth2AccessToken-> " + defaultOAuth2AccessToken);

        ObjectMapper objectMapper = new ObjectMapper();
        byte[] bytes = objectMapper.writeValueAsBytes(defaultOAuth2AccessToken);
        System.out.println("bytes-> " + bytes.length);

    }

}
