package jin.h.mun.knutalk.domain.account;

public enum UserLevel {

	UNVERIFIED, VERIFIED;
	
	public static UserLevel convert(String userLevel) {
		for(UserLevel level : UserLevel.values()) {
			if(level.name().equals(userLevel))
				return level;
		}
		return UNVERIFIED;
	}
}
