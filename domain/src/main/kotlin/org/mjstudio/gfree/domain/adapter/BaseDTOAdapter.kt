package org.mjstudio.gfree.domain.adapter

import org.mjstudio.gfree.domain.dto.DTO
import org.mjstudio.gfree.domain.entity.Entity

interface BaseDTOAdapter <E : Entity, D : DTO> {

    fun toStorage(entity: E): D
    fun fromStorage(dto: D): E
}
