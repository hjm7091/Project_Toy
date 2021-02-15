package jin.h.mun.knutalk.domain.account;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import jin.h.mun.knutalk.domain.board.Post;
import jin.h.mun.knutalk.domain.common.BaseField;
import jin.h.mun.knutalk.dto.account.UserRegisterDto;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter(value = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"id", "email"}, callSuper = false)
@ToString(of = {"id", "email", "nickName"})
@Entity
@Table(name = "tbUser")
public class User extends BaseField {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "userId")
	private Long id;
	
	@Column(name = "userEmail", nullable = false)
	private String email;
	
	@Column(name = "userPassword", nullable = false)
	private String password;
	
	@Column(name = "userNickName", nullable = false)
	private String nickName;
	
	@Column(name = "userStatus", nullable = false)
	@Enumerated(EnumType.STRING)
	private UserLevel userLevel = UserLevel.UNVERIFIED;
	
	@OneToMany(mappedBy = "owner", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
	private List<Post> posts = new ArrayList<>();
		
	public static User createUser(UserRegisterDto userRegisterDto) {
		User user = new User();
		
		user.setEmail(userRegisterDto.getEmail());
		user.setPassword(userRegisterDto.getPassword());
		user.setNickName(userRegisterDto.getNickName());
		user.setCreatedAt(LocalDateTime.now());
		user.setUpdatedAt(LocalDateTime.now());
		
		return user;
	}
	
	public void changePassword(String password) {
		this.setUpdatedAt(LocalDateTime.now());
		this.password = password;
	}
	
	public void changeNickName(String nickName) {
		this.setUpdatedAt(LocalDateTime.now());
		this.nickName = nickName;
	}
	
	public void changeUserLevel(String userLevel) {
		this.setUpdatedAt(LocalDateTime.now());
		this.userLevel = UserLevel.convert(userLevel);
	}
}
