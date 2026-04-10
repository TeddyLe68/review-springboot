package com.teddy.youtuberef.entity;

import com.teddy.youtuberef.entity.enums.ERole;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.UuidGenerator;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Table(name = "roles")
public class RoleEntity extends AbstractAuditingEntity<String>{

    @Id
    @UuidGenerator
    String id;

    @Enumerated(EnumType.STRING)
    @Column(name = "name",length = 20)
    ERole name;

}
