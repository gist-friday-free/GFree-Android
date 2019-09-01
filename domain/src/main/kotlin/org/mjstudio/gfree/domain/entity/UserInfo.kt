package org.mjstudio.gfree.domain.entity

data class UserInfo(
    var uid: String,
    var email: String,
    var major: String,
    var id: Int,
    var age: Int,
    var sex: Int /* -1 : 비공개 0 : 남자 1 : 여자 */
) : Entity