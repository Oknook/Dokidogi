package com.ssafy.dokidog2.user.dto;

import com.ssafy.dokidog2.user.entity.Pet;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.json.simple.JSONObject;

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
    private JSONObject grass;
    private List<Pet> pets;
}
