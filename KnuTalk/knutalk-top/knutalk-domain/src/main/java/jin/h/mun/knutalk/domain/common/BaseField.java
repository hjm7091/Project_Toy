package jin.h.mun.knutalk.domain.common;

import java.time.LocalDateTime;

import javax.persistence.MappedSuperclass;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter(value = AccessLevel.PROTECTED)
@MappedSuperclass
public abstract class BaseField {

	LocalDateTime createdAt;
	
	LocalDateTime updatedAt;
	
}
