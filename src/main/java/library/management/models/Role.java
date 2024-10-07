package library.management.models;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
	LIBRARIAN, MEMBER;

	@Override
	public String getAuthority() {
		return this.name();
	}
}
