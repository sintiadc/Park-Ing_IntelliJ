package com.github.parkeer.entities.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.time.ZonedDateTime;

@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
@MappedSuperclass
public class BaseEntity implements Serializable {

    @Column(nullable = false, updatable = false)
    protected ZonedDateTime createdAt;

    @Column(nullable = false)
    protected ZonedDateTime updatedAt;

    @Column
    protected ZonedDateTime deletedAt;

    @PrePersist
    @JsonIgnore
    public void prePersist() {
        if (getCreatedAt() == null) {
            setCreatedAt(ZonedDateTime.now());
            setUpdatedAt(ZonedDateTime.now());
        }
    }

    @PreUpdate
    @JsonIgnore
    public void preUpdate() {
        setUpdatedAt(ZonedDateTime.now());
    }

    @PreRemove
    @JsonIgnore
    public void preRemove() {
        if (getDeletedAt() == null) {
            setDeletedAt(ZonedDateTime.now());
        }
    }
}
