package training.busboard;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PostcodeValidator {
    public Boolean result;

    public Boolean Validated() {return result;}
}

