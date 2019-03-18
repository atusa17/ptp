package edu.msudenver.tsp.services.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
public class Account extends BaseDto implements Serializable {
    @NotBlank(groups = Insert.class, message = "A username must be specified") @Size(max = 50) private String username;
    @NotBlank(groups = Insert.class, message = "A password must be specified") @Size(max = 256) private String password;
    @NotNull @SerializedName("administrator_status") private boolean administratorStatus;
    @SerializedName("last_login") private Date lastLogin;

    private static final long serialVersionUID = 7095627971593953734L;

    public interface Insert {}
}
