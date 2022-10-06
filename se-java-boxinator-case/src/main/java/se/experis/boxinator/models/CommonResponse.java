package se.experis.boxinator.models;

import com.fasterxml.jackson.annotation.JsonInclude;

//common response for requests
public class CommonResponse<T> {
    public class ErrorDetails {
        private long status;
        private String message;

        public long getStatus() {
            return status;
        }

        public String getMessage() {
            return message;
        }

        public ErrorDetails(long status, String message) {
            this.status = status;
            this.message = message;
        }
    }

    private Boolean success;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T payload;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ErrorDetails error;

    public CommonResponse(T payload) {
        this.payload = payload;
        this.success = true;
        this.error = null;
    }

    public CommonResponse(long status, String message) {
        this.payload = null;
        this.success = false;
        this.error = new ErrorDetails(status, message);
    }

    public Boolean getSuccess() {
        return success;
    }

    public T getPayload() {
        return payload;
    }

    public ErrorDetails getError() {
        return error;
    }
}
