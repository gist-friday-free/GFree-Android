package org.mjstudio.gfree.data.repository

import org.mjstudio.gfree.data.api.EditAPI
import org.mjstudio.gfree.domain.dto.EditDTO
import org.mjstudio.gfree.domain.dto.UserInfoDTO
import org.mjstudio.gfree.domain.entity.Edit
import org.mjstudio.gfree.domain.repository.EditRepository
import javax.inject.Inject

class EditRepositoryImpl @Inject constructor(
        private val editAPI : EditAPI
) : EditRepository {

    override suspend fun createEditRequest(edit: EditDTO) : EditDTO {
        return editAPI.createEditRequest(edit)
    }

    override suspend fun getEditList(year: Int, semester: Int): List<EditDTO> {
        return editAPI.getEditList(year,semester)
    }

    override suspend fun getEditListWithCode(code: String, year: Int, semester: Int): List<EditDTO> {
        return editAPI.getEditListWithCode(code,year,semester)
    }

    override suspend fun getStarUsersInEdit(edit: EditDTO): List<UserInfoDTO> {
        return editAPI.getHeartUsersInEdit(edit.id)
    }

    override suspend fun addStar(edit: Edit, uid: String): EditDTO {
        return editAPI.updateStar(edit.id,1,uid)
    }

    override suspend fun removeStar(edit: Edit, uid: String): EditDTO {
        return editAPI.updateStar(edit.id,0,uid)
    }
}