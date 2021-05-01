package jin.h.mun.knutalk.domain.common;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass
public abstract class BaseTimeField {

	@Column( updatable = false )
	LocalDateTime createdDate;

	LocalDateTime updatedDate;

	@PrePersist
	public void prePersist() {
		LocalDateTime now = LocalDateTime.now();
		this.createdDate = now;
		this.updatedDate = now;
	}

	@PreUpdate
	public void preUpdate() {
		this.updatedDate = LocalDateTime.now();
	}
}
