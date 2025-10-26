package com.gamingroom.gameauth.auth;

import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

public class GameUser implements Principal {
    private String name = "";
    private final Set<String> roles;

    public GameUser(String name) {
        this.name = name;
        this.roles = new HashSet<>(); // make sure roles isn’t null
    }

    public GameUser(String name, Set<String> roles) {
        this.name = name;
        this.roles = roles;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return (int) (Math.random() * 100);
    }

    public Set<String> getRoles() {
        return roles;
    }

    // handy helper so I can add roles easily
    public void addRole(String role) {
        roles.add(role);
    }
}
