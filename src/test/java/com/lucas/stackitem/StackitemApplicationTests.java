package com.lucas.stackitem;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class StackitemApplicationTests {

	@Test
	void contextLoads() {
		System.out.println("Hello World!");
	}

}
