package com.blog.demo.api.unit;

import com.blog.demo.api.CategoryApiController;
import com.blog.demo.restdocs.ApiDocs;
import com.blog.demo.service.*;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@WebMvcTest(controllers = {
        CategoryApiController.class
})
public abstract class PreprocessController extends ApiDocs {
    @MockBean
    protected CategoryService categoryService;
}
