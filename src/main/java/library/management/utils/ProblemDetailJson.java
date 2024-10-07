package library.management.utils;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;

@Data
@EqualsAndHashCode(callSuper = true)
public class ProblemDetailJson extends HashMap<String, Object> implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	private ProblemDetailJson() {
	}

	private ProblemDetailJson(HttpStatus status) {
		setStatus(status);
		setTitle(status.getReasonPhrase());
	}

	public static ProblemDetailJson forStatus(HttpStatus status) {
		return new ProblemDetailJson(status);
	}

	public static ProblemDetailJson forStatusAndTitle(HttpStatus status, String title) {
		ProblemDetailJson mapResponse = new ProblemDetailJson(status);
		mapResponse.setTitle(title);
		return mapResponse;
	}

	public static ProblemDetailJson forStatusAndDetail(HttpStatus status, String detail) {
		ProblemDetailJson mapResponse = new ProblemDetailJson(status);
		mapResponse.setDetail(detail);
		return mapResponse;
	}

	public static ProblemDetailJson of(HttpStatus status, String title, String detail) {
		ProblemDetailJson mapResponse = forStatusAndTitle(status, title);
		mapResponse.setDetail(detail);
		return mapResponse;
	}

	public ProblemDetailJson set(String key, Object value) {
		put(key, value);
		return this;
	}

	public static ProblemDetailJson create() {
		return new ProblemDetailJson();
	}

	public ProblemDetailJson setTitle(String title) {
		put("title", title);
		return this;
	}

	public ProblemDetailJson setDetail(String detail) {
		put("detail", detail);
		return this;
	}

	public ProblemDetailJson setStatus(HttpStatus status) {
		put("status", status.value());
		return this;
	}

}

