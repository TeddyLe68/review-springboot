package com.teddy.youtuberef.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

@Getter
@Setter
// anotation này dùng để đánh dấu một lớp là một lớp cha (superclass) mà các lớp con (subclass) có thể kế thừa từ nó. Lớp được đánh dấu bằng
// @MappedSuperclass sẽ không được ánh xạ trực tiếp đến một bảng trong cơ sở dữ liệu, nhưng các thuộc tính của nó sẽ được kế thừa bởi các lớp con và ánh xạ vào bảng tương ứng của chúng.
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"createdDate", "lastModifiedDate","CreatedBy","LastModifiedBy"}, allowGetters = true)
public abstract class AbstractAuditingEntity<ID> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
//
//    @CreatedBy
//    @Column(name = "created_by", nullable = false, length = 50, updatable = false)
//    private String createdBy;

    @CreatedDate
    @Column(name = "created_date", updatable = false)
    private Instant createdDate = Instant.now();

//    @LastModifiedBy
//    @Column(name = "last_modified_by", length = 50)
//    private String lastModifiedBy;

    @LastModifiedDate
    @Column(name = "last_modified_date")
    private Instant lastModifiedDate = Instant.now();

    public abstract ID getId();
}
