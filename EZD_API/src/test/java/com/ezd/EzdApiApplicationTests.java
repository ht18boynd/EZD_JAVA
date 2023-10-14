package com.ezd;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ezd.models.Game;

@SpringBootTest
class EzdApiApplicationTests {
	 @Autowired
	    private TestRestTemplate restTemplate;
	@Test
	void contextLoads() {
ResponseEntity<List> response = restTemplate.getForEntity("/api/games", List.class);
        
        // Kiểm tra xem phản hồi có thành công không
        assertEquals(HttpStatus.OK, response.getStatusCode());
        
        // Kiểm tra dữ liệu trả về
        List<Game> games = response.getBody();
        assertNotNull(games);

	}

}
