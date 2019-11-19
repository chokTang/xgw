package com.wbg.xigui.bean

/**
 * Created by :TYK
 * Date: 2019/3/12  17:42
 * Desc:  图片选择body
 */
class FileBody {

    var file: String? = ""
    var id: String? = ""
    var sort: Int = 0

    constructor()
    constructor(file: String,id: String, sort: Int) {
        this.file = file
        this.sort = sort
        this.id = id
    }


}
