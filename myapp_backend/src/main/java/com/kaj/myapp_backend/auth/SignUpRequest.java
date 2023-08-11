package com.kaj.myapp_backend.auth;

import com.kaj.myapp_backend.auth.entity.Profile;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class SignUpRequest {
    private String userId;
    private String password;
    private String nickname;
    private List<Profile> profilelist;

}
