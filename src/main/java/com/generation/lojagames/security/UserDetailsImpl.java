package com.generation.lojagames.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.generation.lojagames.model.Usuario;

//FAZ A VERIFICAÇÃO DA CONTA DO USUÁRIO
public class UserDetailsImpl implements UserDetails {

	private static final long serialVersionUID = 1L;//TODA VEZ QUE MEXER NESSA CLASSE VOU SUBINDO ESSE NÚMERO

	private String userName;
	private String password;
	private List<GrantedAuthority> authorities;//LISTA DE PERMISSÕES

	public UserDetailsImpl(Usuario user) {
		this.userName = user.getUsuario();
		this.password = user.getSenha();
	}

	public UserDetailsImpl() {	}
	//ASSUSTADOR, MAS É APENAS UM GET
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		return authorities;
	}

	@Override
	public String getPassword() {

		return password;
	}

	@Override
	public String getUsername() {

		return userName;
	}
	//VERIFICA SE A CONTA ESTÁ VÁLIDA 
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
	//VERIFICA SE A CONTA NÃO ESTÁ BLOQUEADA/BANIDA
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}
	//VERIFICA SE A SENHA DA CONTA NÃO ESTÁ EXPIRADA
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	//VERIFICA SE A CONTA DO USUÁRIO ESTÁ ATIVA
	@Override
	public boolean isEnabled() {
		return true;
	}

}