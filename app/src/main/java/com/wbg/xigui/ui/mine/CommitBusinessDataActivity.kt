package com.wbg.xigui.ui.mine

import android.app.Activity
import android.content.Intent
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.listener.OnTimeSelectListener
import com.luck.picture.lib.PictureSelector
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.wbg.xigui.R
import com.wbg.xigui.base.BaseXActivity
import com.wbg.xigui.bean.FileBody
import com.wbg.xigui.rout.RoutUrl
import com.wbg.xigui.utils.*
import com.wbg.xigui.viewmodel.CommitIdCardViewModel
import kotlinx.android.synthetic.main.activity_commit_business_data.*
import kotlinx.android.synthetic.main.activity_commit_id_card_data.btn_next
import kotlinx.android.synthetic.main.activity_commit_id_card_data.img_id_card_first
import kotlinx.android.synthetic.main.activity_commit_id_card_data.img_id_card_second
import kotlinx.android.synthetic.main.activity_commit_id_card_data.tv_end_time
import kotlinx.android.synthetic.main.activity_commit_id_card_data.tv_time
import startRout


/**
 * @author tyk
 * @date :2019/9/11 17:45
 * @des : 提交营业执照资料
 */
@Route(path = RoutUrl.Mine.commit_business_data,extras = RoutUrl.Extra.login)
class CommitBusinessDataActivity : BaseXActivity<CommitIdCardViewModel>(), View.OnClickListener {

    companion object {
        const val CODE_FIRST = 11111
        const val CODE_SECOND = 22222
    }

    override fun getSmartLayout(): SmartRefreshLayout? {
        return null
    }

    override fun initView() {
        setTitle("提交身份证资料")
        addView(R.layout.activity_commit_business_data)
    }

    override fun initData() {
        isSzhy(true)
        setListener()
    }

    fun setListener() {
        img_id_card_first.setOnClickListener(this)
        img_id_card_second.setOnClickListener(this)
        tv_time.setOnClickListener(this)
        tv_end_time.setOnClickListener(this)
        btn_next.setOnClickListener(this)
        tv_yes.setOnClickListener(this)
        tv_no.setOnClickListener(this)
    }

    /**
     * 是否是三证合一
     */
    fun isSzhy(isSzhy: Boolean) {
        tv_yes.isSelected = isSzhy
        tv_no.isSelected = !isSzhy
        if (isSzhy){
            tv_number.text = "统一社会信用代码"
            edt_number.hint = "输入统一社会信用码"
        }else{
            tv_number.text = "营业执照编号"
            edt_number.hint = "输入营业执照编号"
        }
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_yes -> {//是
                isSzhy(true)
            }
            R.id.tv_no -> {//否
                isSzhy(false)
            }

            R.id.img_id_card_first -> {//营业执照左边
                PicSelectorUtil.pick(this, 1, CODE_FIRST)
            }
            R.id.img_id_card_second -> {//营业执照右边
                PicSelectorUtil.pick(this, 1, CODE_SECOND)
            }
            R.id.tv_time -> {//选择经营时间
                val list: MutableList<String> = arrayListOf()
                list.add("固定有效期限")
                list.add("长期有效期限")
                PickerViewUtil.showOnePickerView(this, "经营时间", tv_time, list)
            }
            R.id.tv_end_time -> {//选择结束时间
                val pickerTime = TimePickerBuilder(this,
                    OnTimeSelectListener { date, v ->
                        tv_end_time.text = ZZDate.GMTToString(date)
                    }).build()
                pickerTime.show()
            }

            R.id.btn_next -> {//下一步
                startRout(RoutUrl.Mine.commit_success)
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        // 图片、视频、音频选择结果回调
        val selectList = PictureSelector.obtainMultipleResult(data)
        // 例如 LocalMedia 里面返回三种path
        // 1.media.getPath(); 为原图path
        // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true  注意：音视频除外
        // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true  注意：音视频除外
        // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
        val list: MutableList<FileBody> = arrayListOf()
        val body = FileBody()
        when (requestCode) {
            CODE_FIRST -> {//正面
                if (selectList[0].isCompressed) {
                    body.file = selectList[0].compressPath
                    list.add(body)
                    UploadUtil.uploadFileBody(list, 99, "正在上传图片") {
                        GlideUtil.loadImg(it!![0].imgUrl!!, img_id_card_first, this)
                    }
                } else {
                    body.file = selectList[0].path
                    list.add(body)
                    UploadUtil.uploadFileBody(list, 99, "正在上传图片") {
                        GlideUtil.loadImg(it!![0].imgUrl!!, img_id_card_first, this)
                    }
                }
            }
            CODE_SECOND -> {//反面
                if (selectList[0].isCompressed) {
                    body.file = selectList[0].compressPath
                    list.add(body)
                    UploadUtil.uploadFileBody(list, 99, "正在上传图片") {
                        GlideUtil.loadImg(it!![0].imgUrl!!, img_id_card_second, this)
                    }
                } else {
                    body.file = selectList[0].path
                    list.add(body)
                    UploadUtil.uploadFileBody(list, 99, "正在上传图片") {
                        GlideUtil.loadImg(it!![0].imgUrl!!, img_id_card_second, this)
                    }
                }
            }
        }
    }
}