package com.ssafy.dokidog2.user.dto;

import com.ssafy.dokidog2.user.entity.Grass;
import com.ssafy.dokidog2.user.entity.Relation;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@NoArgsConstructor
public class UserDTO {
    private Long id;
    private String nickname;
    private String email;
    private Boolean sex;
    private Integer birth;
    private String address;
    private Long imageId;
    private Integer point;
}
