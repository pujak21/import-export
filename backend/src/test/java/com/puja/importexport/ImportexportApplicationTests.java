package com.puja.importexport;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
		"SPRING_DATA_MONGODB_URI=mongodb://localhost:27017/test",
		"SPRING_MAIL_PASSWORD=test-password"
})
class ImportexportApplicationTests {

	@Test
	void contextLoads() {
	}

}
