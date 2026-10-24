package com.interview.exception;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

class GlobalExceptionHandlerTest {

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new ThrowingController())
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void notFoundMapsTo404() throws Exception {
        mockMvc.perform(get("/not-found"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("thing 1 not found"));
    }

    @Test
    void conflictMapsTo409() throws Exception {
        mockMvc.perform(get("/conflict")).andExpect(status().isConflict());
    }

    @Test
    void badRequestMapsTo400() throws Exception {
        mockMvc.perform(get("/bad-request")).andExpect(status().isBadRequest());
    }

    @Test
    void validationErrorMapsTo400WithFieldName() throws Exception {
        mockMvc.perform(post("/validated").contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value(org.hamcrest.Matchers.containsString("name")));
    }

    @Test
    void malformedJsonMapsTo400() throws Exception {
        mockMvc.perform(post("/validated").contentType(MediaType.APPLICATION_JSON).content("{not json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void missingHeaderMapsTo400() throws Exception {
        mockMvc.perform(get("/needs-header")).andExpect(status().isBadRequest());
    }

    record NameRequest(@NotBlank String name) {}

    @RestController
    static class ThrowingController {
        @GetMapping("/not-found")
        void notFound() { throw new NotFoundException("thing 1 not found"); }

        @GetMapping("/conflict")
        void conflict() { throw new ConflictException("conflict"); }

        @GetMapping("/bad-request")
        void badRequest() { throw new BadRequestException("bad"); }

        @PostMapping("/validated")
        String validated(@Valid @RequestBody NameRequest request) { return request.name(); }

        @GetMapping("/needs-header")
        String needsHeader(@RequestHeader("X-User-Id") String userId) { return userId; }
    }
}
