package org.mjstudio.gfree.domain.adapter

import org.mjstudio.gfree.domain.dto.ClassDataDTO
import org.mjstudio.gfree.domain.entity.ClassData
import org.mjstudio.gfree.domain.entity.HourMin
import org.mjstudio.gfree.domain.entity.TimeSlot

object ClassDataAdapter : BaseDTOAdapter<ClassData, ClassDataDTO> {
    override fun toStorage(entity: ClassData): ClassDataDTO {
        return ClassDataDTO(
                entity.id,
                entity.year,
                entity.semester,
                entity.name,
                entity.code,
                entity.professor,
                entity.place,
                entity.size,
                entity.grade,
                entity.time.getOrNull(0)?.startTime?.toCombination(),
                entity.time.getOrNull(0)?.endTime?.toCombination(),
                entity.time.getOrNull(0)?.week,
                entity.time.getOrNull(1)?.startTime?.toCombination(),
                entity.time.getOrNull(1)?.endTime?.toCombination(),
                entity.time.getOrNull(1)?.week,
                entity.time.getOrNull(2)?.startTime?.toCombination(),
                entity.time.getOrNull(2)?.endTime?.toCombination(),
                entity.time.getOrNull(2)?.week,
                entity.time.getOrNull(3)?.startTime?.toCombination(),
                entity.time.getOrNull(3)?.endTime?.toCombination(),
                entity.time.getOrNull(3)?.week,
                entity.time.getOrNull(4)?.startTime?.toCombination(),
                entity.time.getOrNull(4)?.endTime?.toCombination(),
                entity.time.getOrNull(4)?.week
        )
    }

    override fun fromStorage(dto: ClassDataDTO): ClassData {

        return ClassData(
                dto.id,
                dto.year,
                dto.semester,
                dto.code,
                dto.name,
                dto.professor,
                dto.place,
                dto.size,
                getTimeSlotListWithDTO(dto),
                dto.grade
        )
    }

    private fun getTimeSlotListWithDTO(dto: ClassDataDTO): List<TimeSlot> {
        var result: List<TimeSlot> = listOf()

        dto.start1?.let {
            dto.end1?.let {
                dto.week1?.let {
                    val time = TimeSlot(
                            dto.week1,
                            HourMin(dto.start1, true),
                            HourMin(dto.end1, true)
                    )
                    result += time
                }
            }
        }
        dto.start2?.let {
            dto.end2?.let {
                dto.week2?.let {
                    val time = TimeSlot(
                            dto.week2,
                            HourMin(dto.start2, true),
                            HourMin(dto.end2, true)
                    )
                    result += time
                }
            }
        }
        dto.start3?.let {
            dto.end3?.let {
                dto.week3?.let {
                    val time = TimeSlot(
                            dto.week3,
                            HourMin(dto.start3, true),
                            HourMin(dto.end3, true)
                    )
                    result += time
                }
            }
        }
        dto.start4?.let {
            dto.end4?.let {
                dto.week4?.let {
                    val time = TimeSlot(
                            dto.week4,
                            HourMin(dto.start4, true),
                            HourMin(dto.end4, true)
                    )
                    result += time
                }
            }
        }
        dto.start5?.let {
            dto.end5?.let {
                dto.week5?.let {
                    val time = TimeSlot(
                            dto.week5,
                            HourMin(dto.start5, true),
                            HourMin(dto.end5, true)
                    )
                    result += time
                }
            }
        }

        return result
    }
}

fun ClassData.toDTO(): ClassDataDTO {
    return ClassDataAdapter.toStorage(this)
}
fun ClassDataDTO.toEntity(): ClassData {
    return ClassDataAdapter.fromStorage(this)
}
fun Iterable<ClassData>.toDTO(): Iterable<ClassDataDTO> {
    return this.map {
        it.toDTO()
    }
}
fun Iterable<ClassDataDTO>.toEntity(): Iterable<ClassData> {
    return this.map {
        it.toEntity()
    }
}