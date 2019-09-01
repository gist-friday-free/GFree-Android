package org.mjstudio.gfree.domain.repository

import org.mjstudio.gfree.domain.dto.EditDTO
import org.mjstudio.gfree.domain.dto.UserInfoDTO
import org.mjstudio.gfree.domain.entity.Edit

interface EditRepository {

    suspend fun createEditRequest(edit : EditDTO) : EditDTO

    suspend fun getEditList(year: Int, semester : Int) : List<EditDTO>

    suspend fun getEditListWithCode(code : String,year : Int, semester : Int) : List<EditDTO>

    suspend fun getStarUsersInEdit(edit : EditDTO) : List<UserInfoDTO>

    suspend fun addStar(edit : Edit, uid : String) : EditDTO
    suspend fun removeStar(edit : Edit, uid : String ) : EditDTO

}