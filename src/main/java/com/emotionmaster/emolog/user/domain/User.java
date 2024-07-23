package com.emotionmaster.emolog.user.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Table(name = "users")
//기본 생성자 없이 생성 가능 + 접근 범위 protected 로 제한 생성 -> 상속을 통해서만 User 엔티티 기본 생성자 호출 인스턴스화 가능
// 상속 없는 User 확장 제한 -> 상속 통한 객체 생성
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
//Spring Security 사용하기위해 Userdetails 상속받기
public class User implements UserDetails {
    @Id
    //기본 키값 자동 생성 + 자동 증가
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    //필드 설정 이후 수정 불ㄱ가
    @Column(name = "id", updatable = false)
    private long id;

    @Column(name = "email" , nullable= false, unique = true)
    private String email;

    @Column(name = "password" )
    private String password;

    @Column(name = "name" )
    private String name;

    @Column(name = "oauthType" )
    private String oauthType;

    @Column(name = "nickname" )
    private String nickname;

    @Column(name = "age" )
    private int age;


    // 유저 - 이메일, 비밀번호, 이름, 타입, 닉네임, 나이, 직업
    @Builder(toBuilder = true)
    public User(String email, String password, String name, String oauthType, String nickname, int age) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.oauthType = oauthType;
        this.nickname = nickname;
        this.age = age;
    }

    @Builder(toBuilder = true)
    public User(String nickname, int age) {
        this.nickname = nickname;
        this.age = age;
    }

    // 리소스서버에서 제공받은 이름으로 값 업데이트
    public User update(String name) {
        this.name = name;

        return this;
    }



    //UserDetails 상속 메소드

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("user"));
    }

    //email - unique 설정 후 변경
    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

}
