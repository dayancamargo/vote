package com.test.meeting.model.response;

import com.test.meeting.exception.Error;
import com.test.meeting.model.BaseModel;
import lombok.Getter;
import org.springframework.data.domain.Page;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class to make a http body response
 *
 * @param <B> Body object,  it will be a 'data' element with some fields (B) or a list of errors;
 */
@Getter
public class Response<B> extends BaseModel {
    private final List<B> data;
    private final List<Error> errors;
    private final Pagination pagination;

    public Response() {
        data = null;
        errors = null;
        pagination = null;
    }

    public static ContentStep build() {
        return new ContentBuilder();
    }

    private Response(ContentBuilder builder) {
        this.data = builder.data;
        this.errors = builder.errors;
        this.pagination = builder.pagination;
    }

    public interface ContentStep<B> {
        Build withoutContent();

        Build withBody(B body);

        Build withPagination(B body);

        Build withErrors(Error... errors);
    }

    public interface Build {
        Response create();
    }

    private static class ContentBuilder<B> implements ContentStep<B>, Build {
        private List<B> data;
        private List<Error> errors;
        private Pagination pagination;

        public Build withPagination(@NotNull B body) {
            if (!(body instanceof Page)) {
                throw new RuntimeException("Not a paginated object");
            }

            Page page = ((Page) body);

            data = page.getContent();

            pagination = new Pagination(page.getNumber(),
                    page.getSize(),
                    page.getTotalElements(),
                    page.getTotalPages());
            return this;
        }

        public Build withBody(@NotNull B body) {
            data = new ArrayList<>();

            if (body instanceof List) {
                data.addAll((List) body);
            } else {
                data.add(body);
            }

            return this;
        }

        public Build withErrors(@NotNull Error... errors) {
            data = null;
            this.errors = Arrays.asList(errors);
            return this;
        }

        public Build withoutContent() {
            this.data = null;
            this.errors = null;
            this.pagination = null;
            return this;
        }

        public Response create() {
            return new Response(this);
        }
    }
}