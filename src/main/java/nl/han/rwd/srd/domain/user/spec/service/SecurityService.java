package nl.han.rwd.srd.domain.user.spec.service;

import nl.han.rwd.srd.database.model.UserEntity;
import nl.han.rwd.srd.domain.user.AuthUser;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Set;

public interface SecurityService
{
	String getAuthUsername();

	void updateCurrentUserSession(HttpSession session);

	Set<String> getUserAuthorities(AuthUser authUser);

	void updateAuthenticationAttributes(HttpSession session, String username);

	AuthUser mapToAuthUser(UserEntity userEntity);

	void clearAuthenticationAttributes(HttpSession request, List<String> attributes);
}

