package hs.kr.entrydsm.dsmhackerbe.global.security.auth

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.oauth2.core.user.OAuth2User

data class AuthDetails(
    private val id: String,
    private val role: String,
    private val attributes: Map<String, Any> = emptyMap()
) : UserDetails, OAuth2User {

    companion object {
        val EMPTY_ATTRIBUTES = emptyMap<String, Any>()
        private const val ROLE_PREFIX = "ROLE_"
    }

    override fun getAuthorities(): Collection<GrantedAuthority> {
        return listOf(SimpleGrantedAuthority(ROLE_PREFIX + role))
    }

    override fun getUsername(): String = id
    override fun getPassword(): String = ""
    override fun isAccountNonExpired(): Boolean = true
    override fun isAccountNonLocked(): Boolean = true
    override fun isCredentialsNonExpired(): Boolean = true
    override fun isEnabled(): Boolean = true

    override fun getAttributes(): Map<String, Any> = attributes
    override fun getName(): String = id
}
