package exception.error;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AppErrorObject {

  @JsonIgnore
  private final HttpStatus httpStatus;
  private final AppCode status;
  private final String developerMessage;
  private final String moreInfoUrl;
  private final Throwable throwable;


  private AppErrorObject(Builder builder) {
    if (builder.appCode == null || builder.httpStatus == null) {
      throw new NullPointerException("HttpStatus and app code cannot be null.");
    }
    this.httpStatus = builder.httpStatus;
    this.status = builder.appCode;
    this.developerMessage = builder.developerMessage;
    this.moreInfoUrl = builder.moreInfoUrl;
    this.throwable = builder.throwable;
  }

  public HttpStatus getHttpStatus() {
    return httpStatus;
  }

  public AppCode getStatus() {
    return status;
  }

  public String getDeveloperMessage() {
    return developerMessage;
  }

  public String getMoreInfoUrl() {
    return moreInfoUrl;
  }

  public Throwable getThrowable() {
    return throwable;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;

    AppErrorObject that = (AppErrorObject) o;

    if (httpStatus != that.httpStatus)
      return false;
    return status.equals(that.status);
  }

  @Override
  public int hashCode() {
    int result = httpStatus.hashCode();
    result = 31 * result + status.hashCode();
    return result;
  }


  @Override
  public String toString() {
    return "AppErrorObject{" + "httpStatus=" + httpStatus + ", status=" + status
        + ", developerMessage='" + developerMessage + '\'' + ", moreInfoUrl='" + moreInfoUrl + '\''
        + ", throwable=" + throwable + '}';
  }

  public static class Builder {

    private HttpStatus httpStatus;
    private AppCode appCode;
    private String developerMessage;
    private String moreInfoUrl;
    private Throwable throwable;

    public Builder HttpStatus(HttpStatus httpStatus) {
      this.httpStatus = httpStatus;
      return this;
    }

    public Builder appCode(AppCode appCode) {
      this.appCode = appCode;
      return this;
    }

    public Builder developerMessage(String developerMessage) {
      this.developerMessage = developerMessage;
      return this;
    }

    public Builder moreInfoUrl(String moreInfoUrl) {
      this.moreInfoUrl = moreInfoUrl;
      return this;
    }

    public Builder throwable(Throwable throwable) {
      this.throwable = throwable;
      return this;
    }

    public AppErrorObject build() {
      if (this.httpStatus == null || this.appCode == null) {
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
      }
      return new AppErrorObject(this);
    }
  }
}
