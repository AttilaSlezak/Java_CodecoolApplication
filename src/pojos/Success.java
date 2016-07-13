package pojos;

import java.util.HashMap;
import java.util.Map;

public class Success {

	Map<String, String> success;

	public Success(String operation, String message) {
		super();
		this.success = new HashMap<>();
		this.success.put("operation", operation);
		this.success.put("message", message);
	}

	public Map<String, String> getSuccess() {
		return success;
	}

	public void setSuccess(Map<String, String> success) {
		this.success = success;
	}
	
}
