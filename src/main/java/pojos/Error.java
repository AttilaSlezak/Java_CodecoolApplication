package pojos;

import java.util.HashMap;
import java.util.Map;


public class Error implements Response {
	Map<String, String> error;

	public Error(String errorType,String errorMsg) {
		this.error = new HashMap<String, String>();
		error.put("type",errorType);
		error.put("errormsg", errorMsg);
	}
	public Map<String, String> getError() {
		return error;
	}
	public void setError(Map<String, String> error) {
		this.error = error;
	}
}
