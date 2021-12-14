package be.intec.models;


import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.List;


@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "message")
public class Message {

    @Id
    @GeneratedValue
    Long id;

    String subject;

    String content;

    @ManyToOne
    @JoinColumn(name = "from_user_id")
    User fromUserId;

    @ManyToOne
    @JoinColumn(name = "to_user_id")
    User toUserId;

}
