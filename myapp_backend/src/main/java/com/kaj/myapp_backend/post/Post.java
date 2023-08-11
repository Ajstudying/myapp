package com.kaj.myapp_backend.post;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long no;

    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String content;
    private String nickname;
    private String image;
    @Column(length = 1024 * 1024 * 20)
    private long createdTime;
    private String petname;

}
