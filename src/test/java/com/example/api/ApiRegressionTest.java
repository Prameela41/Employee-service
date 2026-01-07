package com.example.api;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ApiRegressionTest {
    private static final Logger log = LoggerFactory.getLogger(ApiRegressionTest.class);

    @Autowired
    private TestRestTemplate restTemplate;

    private static Integer createdUserId;
    private static Integer createdOrderId;

    @BeforeAll
    static void setupAll() {
        // Setup global test data if needed
        System.out.println("[SetupAll] Starting API regression tests...");
    }

    @AfterAll
    static void teardownAll() {
        // Cleanup global test data if needed
        System.out.println("[TeardownAll] Finished API regression tests.");
    }

    // --- USERS ENDPOINTS --- //
    @Test
    @org.junit.jupiter.api.Order(1)
    void testListUsers() {
        ResponseEntity<String> response = restTemplate.getForEntity("/users", String.class);
        log.info("[testListUsers] Status: {} Body: {}", response.getStatusCode(), response.getBody());
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertTrue(response.getBody().contains("id") || response.getBody().equals("[]"));
    }

    @Test
    @org.junit.jupiter.api.Order(2)
    void testCreateUserPositive() {
        Map<String, Object> user = new HashMap<>();
        user.put("name", "Test User");
        user.put("email", "testuser@example.com");
        user.put("role", "USER");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(user, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity("/users", request, Map.class);
        log.info("[testCreateUserPositive] Status: {} Body: {}", response.getStatusCode(), response.getBody());
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        createdUserId = (Integer) response.getBody().get("id");
        Assertions.assertNotNull(createdUserId);
    }

    @Test
    @org.junit.jupiter.api.Order(3)
    void testCreateUserNegative() {
        // Missing required field
        Map<String, Object> user = new HashMap<>();
        user.put("email", "baduser@example.com");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(user, headers);
        ResponseEntity<String> response = restTemplate.postForEntity("/users", request, String.class);
        log.info("[testCreateUserNegative] Status: {} Body: {}", response.getStatusCode(), response.getBody());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @org.junit.jupiter.api.Order(4)
    void testGetUserByIdPositive() {
        Assumptions.assumeTrue(createdUserId != null);
        ResponseEntity<Map> response = restTemplate.getForEntity("/users/" + createdUserId, Map.class);
        log.info("[testGetUserByIdPositive] Status: {} Body: {}", response.getStatusCode(), response.getBody());
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(createdUserId, response.getBody().get("id"));
    }

    @Test
    @org.junit.jupiter.api.Order(5)
    void testGetUserByIdNegative() {
        ResponseEntity<String> response = restTemplate.getForEntity("/users/999999", String.class);
        log.info("[testGetUserByIdNegative] Status: {} Body: {}", response.getStatusCode(), response.getBody());
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @org.junit.jupiter.api.Order(6)
    void testUpdateUserPositive() {
        Assumptions.assumeTrue(createdUserId != null);
        Map<String, Object> update = new HashMap<>();
        update.put("name", "Updated User");
        update.put("email", "updateduser@example.com");
        update.put("role", "ADMIN");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(update, headers);
        ResponseEntity<Map> response = restTemplate.exchange("/users/" + createdUserId, HttpMethod.PUT, request, Map.class);
        log.info("[testUpdateUserPositive] Status: {} Body: {}", response.getStatusCode(), response.getBody());
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals("Updated User", response.getBody().get("name"));
    }

    @Test
    @org.junit.jupiter.api.Order(7)
    void testUpdateUserNegative() {
        // Update non-existent user
        Map<String, Object> update = new HashMap<>();
        update.put("name", "Ghost");
        update.put("email", "ghost@example.com");
        update.put("role", "USER");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(update, headers);
        ResponseEntity<String> response = restTemplate.exchange("/users/999999", HttpMethod.PUT, request, String.class);
        log.info("[testUpdateUserNegative] Status: {} Body: {}", response.getStatusCode(), response.getBody());
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @org.junit.jupiter.api.Order(8)
    void testDeleteUserPositive() {
        Assumptions.assumeTrue(createdUserId != null);
        ResponseEntity<Void> response = restTemplate.exchange("/users/" + createdUserId, HttpMethod.DELETE, null, Void.class);
        log.info("[testDeleteUserPositive] Status: {}", response.getStatusCode());
        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    @org.junit.jupiter.api.Order(9)
    void testDeleteUserNegative() {
        ResponseEntity<String> response = restTemplate.exchange("/users/999999", HttpMethod.DELETE, null, String.class);
        log.info("[testDeleteUserNegative] Status: {} Body: {}", response.getStatusCode(), response.getBody());
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    // --- ORDERS ENDPOINTS --- //
    @Test
    @org.junit.jupiter.api.Order(10)
    void testListOrders() {
        ResponseEntity<String> response = restTemplate.getForEntity("/orders", String.class);
        log.info("[testListOrders] Status: {} Body: {}", response.getStatusCode(), response.getBody());
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertTrue(response.getBody().contains("id") || response.getBody().equals("[]"));
    }

    @Test
    @org.junit.jupiter.api.Order(11)
    void testCreateOrderNegative() {
        // Missing required field
        Map<String, Object> order = new HashMap<>();
        order.put("amount", 100);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(order, headers);
        ResponseEntity<String> response = restTemplate.postForEntity("/orders", request, String.class);
        log.info("[testCreateOrderNegative] Status: {} Body: {}", response.getStatusCode(), response.getBody());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @org.junit.jupiter.api.Order(12)
    void testCreateOrderPositive() {
        // For demo, userId may be required.
        Map<String, Object> order = new HashMap<>();
        order.put("userId", 1);
        order.put("amount", 100);
        order.put("status", "NEW");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(order, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity("/orders", request, Map.class);
        log.info("[testCreateOrderPositive] Status: {} Body: {}", response.getStatusCode(), response.getBody());
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        createdOrderId = (Integer) response.getBody().get("id");
        Assertions.assertNotNull(createdOrderId);
    }

    @Test
    @org.junit.jupiter.api.Order(13)
    void testGetOrderByIdPositive() {
        Assumptions.assumeTrue(createdOrderId != null);
        ResponseEntity<Map> response = restTemplate.getForEntity("/orders/" + createdOrderId, Map.class);
        log.info("[testGetOrderByIdPositive] Status: {} Body: {}", response.getStatusCode(), response.getBody());
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(createdOrderId, response.getBody().get("id"));
    }

    @Test
    @org.junit.jupiter.api.Order(14)
    void testGetOrderByIdNegative() {
        ResponseEntity<String> response = restTemplate.getForEntity("/orders/999999", String.class);
        log.info("[testGetOrderByIdNegative] Status: {} Body: {}", response.getStatusCode(), response.getBody());
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @org.junit.jupiter.api.Order(15)
    void testUpdateOrderNegative() {
        // Update non-existent order
        Map<String, Object> update = new HashMap<>();
        update.put("amount", 999);
        update.put("status", "CANCELLED");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(update, headers);
        ResponseEntity<String> response = restTemplate.exchange("/orders/999999", HttpMethod.PUT, request, String.class);
        log.info("[testUpdateOrderNegative] Status: {} Body: {}", response.getStatusCode(), response.getBody());
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    // Add more edge/positive/negative cases as needed for /orders/{id} PUT, DELETE, etc.

    // --- MONITORING ENDPOINTS (Actuator) --- //
    @Test
    @org.junit.jupiter.api.Order(20)
    void testActuatorHealth() {
        ResponseEntity<String> response = restTemplate.getForEntity("/actuator/health", String.class);
        log.info("[testActuatorHealth] Status: {} Body: {}", response.getStatusCode(), response.getBody());
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertTrue(response.getBody().contains("status"));
    }

    // --- DOCUMENTATION ENDPOINTS --- //
    @Test
    @org.junit.jupiter.api.Order(21)
    void testOpenApiDocs() {
        ResponseEntity<String> response = restTemplate.getForEntity("/v3/api-docs", String.class);
        log.info("[testOpenApiDocs] Status: {} Body: {}", response.getStatusCode(), response.getBody());
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertTrue(response.getBody().contains("openapi"));
    }
}