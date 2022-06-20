package com.blog.demo.api;

import com.blog.demo.api.dto.category.CategoryDto;
import com.blog.demo.api.dto.category.CreateCategoryRequest;
import com.blog.demo.api.dto.category.CreateCategoryResponse;
import com.blog.demo.api.integration.PreprocessController;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CategoryControllerTest extends PreprocessController {

    @Test
    @DisplayName("category 추가 - 성공")
    void createCategoryTest_Success() throws Exception {
        // given
        willReturn(new CategoryDto(2L, "name"))
                .given(categoryService).createCategory(any(CreateCategoryRequest.class));
        CreateCategoryRequest request = CreateCategoryRequest.builder()
                .parentId(1L)
                .memberId("memberId")
                .name("name")
                .build();

        // when
        String requestBodyStr = objectMapper.writeValueAsString(request);
        ResultActions perform = mockMvc.perform(post("/api/v1/categories")
                .content(requestBodyStr)
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        // then
        CreateCategoryResponse createCategoryResponse = objectMapper.readValue(perform.andExpect(status().isOk())
                .andDo(print())
                .andDo(toDocument("category"))
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8), new TypeReference<>() {
        });

        assertThat(createCategoryResponse.getId()).isEqualTo(2L);
        assertThat(createCategoryResponse.getName()).isEqualTo("name");
    }
}
