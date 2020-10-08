package kr.ibct.springboilerplate.account;


import com.fasterxml.jackson.annotation.JsonIgnore;
import kr.ibct.springboilerplate.common.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.Set;


@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id",callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account extends BaseTimeEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    @Email
    private String email;

    @JsonIgnore
    private String password;
    private String phoneNum;
    private String address;

    @Column(unique = true)
    private String username;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<AccountRole> roles;

    private boolean isDeleted;
}
