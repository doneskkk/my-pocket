package com.donesk.moneytracker;

 import static org.junit.jupiter.api.Assertions.*;
 import static org.mockito.Mockito.*;
 import java.util.Date;
 import org.springframework.test.util.ReflectionTestUtils;
 import com.donesk.moneytracker.jwt.JwtUtils;
 import com.donesk.moneytracker.service.UserDetailsImpl;
import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

class JwtUtilsTest {

    private JwtUtils jwtUtils;

    @BeforeEach
    void setUp() {
        jwtUtils = new JwtUtils();
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", "V0hBMTIzNDU2Nzg5MGFiY2RlZmdoaWprbG1ub3BxcnN0dnd4eg==");
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", 3600000);
    }

    @Test
    void testGenerateAndParseJwtToken() {
        UserDetailsImpl userDetails = new UserDetailsImpl("testuser", "password", /* authorities */ null);
        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        String token = jwtUtils.generateJwtToken(auth);
        assertNotNull(token);

        String username = jwtUtils.getUserNameFromJwtToken(token);
        assertEquals("testuser", username);
    }

    @Test
    void testValidateJwtToken_validToken() {
        UserDetailsImpl userDetails = new UserDetailsImpl("testuser", "password", null);
        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        String token = jwtUtils.generateJwtToken(auth);
        assertTrue(jwtUtils.validateJwtToken(token));
    }

    @Test
    void testValidateJwtToken_invalidToken() {
        String invalidToken = "invalid.token.example";
        assertFalse(jwtUtils.validateJwtToken(invalidToken));
    }

    @Test
    void testValidateJwtToken_expiredToken() throws InterruptedException {
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", -1000);

        UserDetailsImpl userDetails = new UserDetailsImpl("testuser", "password", null);
        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        String token = jwtUtils.generateJwtToken(auth);
        assertFalse(jwtUtils.validateJwtToken(token));
    }
}

