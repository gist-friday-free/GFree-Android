package org.mjstudio.gfree.domain.adapter

import org.mjstudio.gfree.domain.dto.NoticeDTO
import org.mjstudio.gfree.domain.entity.Notice

object NoticeAdapter : BaseDTOAdapter<Notice, NoticeDTO> {
    override fun toStorage(entity: Notice): NoticeDTO {
        return NoticeDTO(entity.id, entity.title, entity.subtitle, entity.body, entity.time, entity.writer)
    }

    override fun fromStorage(dto: NoticeDTO): Notice {
        return Notice(
                dto.id,
                dto.title,
                dto.subtitle,
                dto.body,
                dto.created,
                dto.writer
        )
    }
}

fun Notice.toDTO(): NoticeDTO {
    return NoticeAdapter.toStorage(this)
}
fun NoticeDTO.toEntity(): Notice {
    return NoticeAdapter.fromStorage(this)
}
fun Iterable<Notice>.toDTO(): Iterable<NoticeDTO> {
    return this.map {
        it.toDTO()
    }
}
fun Iterable<NoticeDTO>.toEntity(): Iterable<Notice> {
    return this.map {
        it.toEntity()
    }
}