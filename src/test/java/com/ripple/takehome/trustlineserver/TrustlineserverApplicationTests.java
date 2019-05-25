package com.ripple.takehome.trustlineserver;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TrustlineserverApplicationTests {

	@BeforeClass
	public static void setupEnvironment() {
		System.setProperty("node.name", "Alice");
	}

	@Test
	public void contextLoads() {
	}

}
