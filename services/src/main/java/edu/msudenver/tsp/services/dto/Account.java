package edu.msudenver.tsp.services.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
public class Account extends BaseDto implements Serializable {
    @Size(max = 50) private String username;
    @Size(max = 256) private String password;
    @NotNull @SerializedName("administrator_status") private boolean administratorStatus;
    @Temporal(TemporalType.DATE) @SerializedName("last_login") private Date lastLogin;

    private static final long serialVersionUID = 7095627971593953734L;


}
