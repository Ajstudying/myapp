package com.kaj.myapp_backend.auth;

import com.kaj.myapp_backend.auth.entity.Profile;
import com.kaj.myapp_backend.auth.entity.User;
import com.kaj.myapp_backend.auth.entity.ProfileRepository;
import com.kaj.myapp_backend.auth.entity.UserRepository;
import com.kaj.myapp_backend.auth.util.HashUtil;
import com.kaj.myapp_backend.auth.util.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepo;
    @Autowired
    private ProfileRepository profileRepo;
    @Autowired
    private AuthService service;
    @Autowired
    private HashUtil hash;
    @Autowired
    private JwtUtil jwt;

    @PostMapping(value = "/signup")
    public ResponseEntity signUp(@RequestBody SignUpRequest req){
        System.out.println(req);

        if(req.getUserId() == null || req.getUserId().isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if(req.getPassword() == null || req.getPassword().isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if(req.getNickname() == null || req.getNickname().isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        List<Profile> lists = req.getProfilelist();
        System.out.println(lists);
        for(int i = 0; i < lists.size(); i++){
            if(lists.get(i).getPetname() == null || lists.get(i).getPetname().isEmpty()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            if(lists.get(i).getSpecies() == null || lists.get(i).getSpecies().isEmpty()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        }
        service.createIdentity(req);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Auth
    @PostMapping(value = "/signin")
    public ResponseEntity signin(@RequestParam String userid, @RequestParam String password, HttpServletResponse res) {
        System.out.println(userid);
        System.out.println(password);

        Optional<User> user = userRepo.findByUserid(userid);
        if(!user.isPresent()){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        boolean isVerified = hash.verifyHash(password, user.get().getSecret());

        if(!isVerified){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        User u = user.get();
        Optional<List<Profile>> profile = profileRepo.findByUser_Id(u.getId());
        if(!profile.isPresent()){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        String token = jwt.createToken(u.getId(), u.getNickname());
        System.out.println(token);

        Cookie cookie = new Cookie("token", token);
        cookie.setMaxAge((int)(jwt.TOKEN_TIMEOUT/1000));
        cookie.setDomain("localhost");

        res.addCookie(cookie);

        return ResponseEntity
                .status(302)
                .location(ServletUriComponentsBuilder
                        .fromHttpUrl("http://localhost:5500")
                        .build().toUri())
                .build();

    }
}
