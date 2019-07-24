package org.mjstudio.gfree.domain.adapter

import org.mjstudio.gfree.domain.dto.EditDTO
import org.mjstudio.gfree.domain.entity.Edit

object EditAdapter : BaseDTOAdapter<Edit,EditDTO> {
    override fun toStorage(entity: Edit): EditDTO {
        return EditDTO(
                entity.id,
                entity.editClass.toDTO(),
                entity.editClass.id,
                entity.year,
                entity.semester,
                entity.writer,
                entity.created,
                entity.type.name,
                entity.value,
                entity.star
        )
    }

    override fun fromStorage(dto: EditDTO): Edit {
        return Edit(
                dto.id,
                dto.editClassData.toEntity(),
                dto.year,
                dto.semester,
                dto.writer,
                dto.created,
                EditDTO.Type.valueOf(dto.type),
                dto.value,
                dto.star
        )
    }
}

fun Edit.toDTO(): EditDTO {
    return EditAdapter.toStorage(this)
}
fun EditDTO.toEntity(): Edit {
    return EditAdapter.fromStorage(this)
}
fun Iterable<Edit>.toDTO(): Iterable<EditDTO> {
    return this.map {
        it.toDTO()
    }
}
fun Iterable<EditDTO>.toEntity(): Iterable<Edit> {
    return this.map {
        it.toEntity()
    }
}