package com.puja.importexport.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.puja.importexport.model.Contact;
import com.puja.importexport.service.ContactService;

@WebMvcTest(controllers = ContactController.class, properties = {
        "app.rate-limit.contact.max-requests=2",
        "app.rate-limit.contact.window-seconds=600"
})
class ContactControllerTest {

    private static final String VALID_BODY = """
            {"name":"Jane Buyer","email":"jane@example.com","country":"Germany",
             "serviceNeeded":"Turmeric","message":"Need 500kg"}
            """;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ContactService contactService;

    @Test
    void validInquiryReturnsCreated() throws Exception {
        Contact saved = new Contact();
        saved.setId("abc123");
        when(contactService.submitInquiry(any())).thenReturn(saved);

        mockMvc.perform(post("/api/contact").contentType(MediaType.APPLICATION_JSON).content(VALID_BODY)
                        .header("X-Forwarded-For", "10.0.0.1"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("abc123"))
                .andExpect(jsonPath("$.message").value("Inquiry received"));
    }

    @Test
    void invalidInquiryReturnsFieldErrors() throws Exception {
        mockMvc.perform(post("/api/contact").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"\",\"email\":\"not-an-email\"}")
                        .header("X-Forwarded-For", "10.0.0.2"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors.name").exists())
                .andExpect(jsonPath("$.fieldErrors.email").exists());

        verify(contactService, never()).submitInquiry(any());
    }

    @Test
    void malformedJsonReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/api/contact").contentType(MediaType.APPLICATION_JSON).content("{not json")
                        .header("X-Forwarded-For", "10.0.0.3"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("Malformed JSON request"));
    }

    @Test
    void excessiveRequestsAreRateLimited() throws Exception {
        when(contactService.submitInquiry(any())).thenReturn(new Contact());

        for (int i = 0; i < 2; i++) {
            mockMvc.perform(post("/api/contact").contentType(MediaType.APPLICATION_JSON).content(VALID_BODY)
                            .header("X-Forwarded-For", "10.0.0.4"))
                    .andExpect(status().isCreated());
        }

        mockMvc.perform(post("/api/contact").contentType(MediaType.APPLICATION_JSON).content(VALID_BODY)
                        .header("X-Forwarded-For", "10.0.0.4"))
                .andExpect(status().isTooManyRequests())
                .andExpect(header().exists("Retry-After"));
    }

    @Test
    void corsAllowsConfiguredOriginOnly() throws Exception {
        mockMvc.perform(options("/api/contact")
                        .header("Origin", "https://import-export-ruddy.vercel.app")
                        .header("Access-Control-Request-Method", "POST"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", "https://import-export-ruddy.vercel.app"));

        mockMvc.perform(options("/api/contact")
                        .header("Origin", "https://evil.example")
                        .header("Access-Control-Request-Method", "POST"))
                .andExpect(status().isForbidden());
    }
}
