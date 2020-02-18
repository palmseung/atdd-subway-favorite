package atdd.path.web;

import atdd.path.jwt.JwtTokenProvider;
import atdd.path.jwt.ReadProperties;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

@SpringBootTest
public class LoginInterceptorTest {
    private JwtTokenProvider jwtTokenProvider;
    private ReadProperties readProperties;

    @Autowired
    public LoginInterceptorTest(JwtTokenProvider jwtTokenProvider, ReadProperties readProperties) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.readProperties = readProperties;
    }

    @Mock
    private HttpServletRequest request;

    @Test
    public void 인터셉터에선_토큰을_만들_때_쓴_이메일을_리턴한다() {
        //given
        String email = "abc@email.com";
        String secretKey = readProperties.getSecretKey();
        String token = jwtTokenProvider.createToken(email);
        given(request.getHeader("Authorization")).willReturn(token);

        //when
        String emailFromRequest = jwtTokenProvider.getUserEmail(token);

        //then
        assertEquals(email, emailFromRequest);
    }
}
