package jin.h.mun.rowdystory.web.resolver.session;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target( ElementType.PARAMETER )
@Retention( RetentionPolicy.RUNTIME )
@Session( sessionDefine = SessionDefine.USER )
public @interface SessionUser {
}
