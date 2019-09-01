package org.mjstudio.gfree.domain.repository

import io.reactivex.Single
import org.mjstudio.gfree.domain.dto.EditDTO
import org.mjstudio.gfree.domain.dto.UserInfoDTO
import org.mjstudio.gfree.domain.entity.Edit

interface EditRepository {

    fun createEditRequest(edit : EditDTO) : Single<EditDTO>

    fun getEditList(year: Int, semester : Int) : Single<List<EditDTO>>

    fun getEditListWithCode(code : String,year : Int, semester : Int) : Single<List<EditDTO>>

    fun getStarUsersInEdit(edit : EditDTO) : Single<List<UserInfoDTO>>

    fun addStar(edit : Edit, uid : String) : Single<EditDTO>
    fun removeStar(edit : Edit, uid : String ) : Single<EditDTO>

}