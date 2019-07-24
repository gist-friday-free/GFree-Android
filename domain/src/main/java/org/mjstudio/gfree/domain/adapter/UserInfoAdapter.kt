package org.mjstudio.gfree.domain.adapter

import org.mjstudio.gfree.domain.dto.UserInfoDTO
import org.mjstudio.gfree.domain.entity.UserInfo

object UserInfoAdapter : BaseDTOAdapter<UserInfo, UserInfoDTO> {
    override fun toStorage(entity: UserInfo): UserInfoDTO {
        return UserInfoDTO(
                entity.uid,
                entity.email,
                entity.major,
                entity.id,
                entity.sex,
                entity.age
        )
    }

    override fun fromStorage(dto: UserInfoDTO): UserInfo {
        return UserInfo(
                dto.uid,
                dto.email,
                dto.majorCode,
                dto.studentId,
                dto.age,
                dto.sex
        )
    }
}

fun UserInfo.toDTO(): UserInfoDTO {
    return UserInfoAdapter.toStorage(this)
}
fun UserInfoDTO.toEntity(): UserInfo {
    return UserInfoAdapter.fromStorage(this)
}
fun Iterable<UserInfo>.toDTO(): Iterable<UserInfoDTO> {
    return this.map {
        it.toDTO()
    }
}
fun Iterable<UserInfoDTO>.toEntity(): Iterable<UserInfo> {
    return this.map {
        it.toEntity()
    }
}