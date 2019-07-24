package org.mjstudio.gfree.data.repository

import io.reactivex.Single
import org.mjstudio.gfree.data.api.EditAPI
import org.mjstudio.gfree.domain.dto.EditDTO
import org.mjstudio.gfree.domain.dto.UserInfoDTO
import org.mjstudio.gfree.domain.entity.Edit
import org.mjstudio.gfree.domain.repository.EditRepository
import javax.inject.Inject

class EditRepositoryImpl @Inject constructor(
        private val editAPI : EditAPI
) : EditRepository {

    override fun createEditRequest(edit: EditDTO) : Single<EditDTO> {
        return editAPI.createEditRequest(edit)
    }

    override fun getEditList(year: Int, semester: Int): Single<List<EditDTO>> {
        return editAPI.getEditList(year,semester)
    }

    override fun getEditListWithCode(code: String, year: Int, semester: Int): Single<List<EditDTO>> {
        return editAPI.getEditListWithCode(code,year,semester)
    }

    override fun getStarUsersInEdit(edit: EditDTO): Single<List<UserInfoDTO>> {
        return editAPI.getHeartUsersInEdit(edit.id)
    }

    override fun addStar(edit: Edit, uid: String): Single<EditDTO> {
        return editAPI.updateStar(edit.id,1,uid)
    }

    override fun removeStar(edit: Edit, uid: String): Single<EditDTO> {
        return editAPI.updateStar(edit.id,0,uid)
    }
}