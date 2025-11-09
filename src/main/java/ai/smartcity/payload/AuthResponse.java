
package ai.smartcity.payload;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;
    public AuthResponse(){}
    public AuthResponse(String token){ this.token = token; }
    public String getToken(){ return token; }
    public void setToken(String token){ this.token = token; }
}
