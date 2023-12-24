package com.github.parkeer.entities.employee;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.parkeer.entities.base.BaseAccountEntity;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.Persistable;

@Entity
@Table(name = "employee", schema = "public")
@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
@ToString
public class EmployeeEntity extends BaseAccountEntity implements Persistable<EmployeeId> {

    @EmbeddedId
    private EmployeeId id;

    @Override
    @JsonIgnore
    public boolean isNew() {
        return getCreatedAt() == null;
    }
}
