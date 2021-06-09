package jin.h.mun.rowdystory.exception.account;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ErrorMessage {

    EMAIL_NOT_EXIST( "해당 이메일에 대한 정보가 없습니다. 이메일 주소를 확인해주세요." ),
    EMAIL_ALREADY_EXIST( "해당 이메일로 이미 등록한 사용자가 존재합니다. 이메일 주소를 확인해주세요." ),
    EMAIL_AlREADY_REGISTERED_SOCIAL_ACCOUNT( "해당 이메일은 소셜 로그인으로 등록된 계정입니다. 소셜 로그인을 통해 로그인 해주세요." ),
    PASSWORD_NOT_MATCH( "비밀번호가 일치하지 않습니다. 비밀번호를 확인해주세요." )

    ;

    @Getter
    private final String message;
}
