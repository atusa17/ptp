package edu.msudenver.tsp.persistence.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

@Entity(name = "accounts")
@EntityListeners(AuditingEntityListener.class)
@Data
@EqualsAndHashCode(callSuper = true)
public class Account extends BaseDto implements Serializable {
    @NotBlank(groups = Insert.class, message = "A username must be specified") @Size(max = 50) private String username;
    @NotBlank(groups = Insert.class, message = "A password must be specified") @Size(max = 256) private String password;
    @NotNull private boolean administrator;
    @Temporal(TemporalType.DATE) @Column(name = "last_login") private Date lastLogin;

    private static final long serialVersionUID = 7095627971593953734L;

    @JsonProperty("last_login")
    public Date getLastLogin() {
        return lastLogin;
    }

    @JsonProperty("last_login")
    public void setLastLogin(final Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public interface Insert {}

    @PrePersist
    public void prePersist() {
        lastLogin = new Date();
    }
}
