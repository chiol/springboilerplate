package kr.ibct.springboilerplate.book;

import kr.ibct.springboilerplate.account.Account;
import kr.ibct.springboilerplate.common.BaseTimeEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
public class Book extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;

    @CreatedBy
    @OneToOne
    private Account author;

}
