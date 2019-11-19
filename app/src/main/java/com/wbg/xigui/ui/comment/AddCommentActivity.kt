package com.wbg.xigui.ui.comment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.wbg.xigui.R
import com.wbg.xigui.XApplication
import com.wbg.xigui.adapter.PicAdapter
import com.wbg.xigui.adapter.PicAdapter.Companion.ADD_PIC
import com.wbg.xigui.base.BaseXActivity
import com.wbg.xigui.bean.FileBody
import com.wbg.xigui.bean.OrderBean
import com.wbg.xigui.bean.body.AddCommentBody
import com.wbg.xigui.rout.RoutUrl
import com.wbg.xigui.utils.GlideUtil
import com.wbg.xigui.utils.LubanImg.getPath
import com.wbg.xigui.utils.UploadUtil
import com.wbg.xigui.viewmodel.AddCommentViewModel
import kotlinx.android.synthetic.main.activity_add_comment.*
import toast

/**
 * @author tyk
 * @date :2019/8/26 9:59
 * @des : 添加评价（评价晒单）
 */
@Route(path = RoutUrl.Comment.addComment, extras = RoutUrl.Extra.login)
class AddCommentActivity : BaseXActivity<AddCommentViewModel>(), View.OnClickListener {


    companion object {
        const val KEY_OREDR_BEAN = "order_bean"
        const val KEY_BUNDLE = "bundle"
    }

    var orderBean: OrderBean? = null
    var picAdapter: PicAdapter? = null
    var list: MutableList<FileBody> = arrayListOf()
    var picList: MutableList<LocalMedia> = arrayListOf()//图片列表
    var picBodyList: MutableList<FileBody> = arrayListOf()//上传文件列表
    var score = 0f
    var picId = ""

    override fun getSmartLayout(): SmartRefreshLayout? {
        return null
    }

    override fun initView() {
        orderBean = intent.getBundleExtra(KEY_BUNDLE).get(KEY_OREDR_BEAN) as OrderBean
        addView(R.layout.activity_add_comment)

        val linearLayoutManager = LinearLayoutManager(this)
        rv_pic.layoutManager = linearLayoutManager
        picAdapter = PicAdapter()
        rv_pic.adapter = picAdapter

        setListener()

        val fileBody = FileBody(ADD_PIC,"", 0)
        list.add(fileBody)
        picAdapter?.setNewData(list)
    }

    override fun initData() {
        showProductView(orderBean!!)

        rating_view.setOnRatingChangedListener {
            score = it
        }
    }


    fun setListener() {
        sure_btn.setOnClickListener(this)
        edt_comment.addTextChangedListener(object :TextWatcher{
            @SuppressLint("SetTextI18n")
            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()){
                    tv_text_num.text = "0/150"
                }else{
                    if (s.length<=150){
                        tv_text_num.text = s.length.toString()+"/150"
                    }else{
                        tv_text_num.text = "150/150"
                        toast("最多只能评论150个字哟")
                        //这里是将超过150字之外的截取掉
                        val a = s.delete(150,s.length)
                        Editable.Factory.getInstance().newEditable(a)
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })

        picAdapter?.setOnItemClickListener { adapter, view, position ->
            choosePicture(9)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.sure_btn -> {
                val body = AddCommentBody()
                body.comment = edt_comment.text.toString()
                body.memberId = XApplication.instance.getMemberId()
                body.productId = orderBean!!.list[0].productId
                body.score = score
                body.album = picId
                mViewModel.addComment(body){
                    if (it?.flag!!){
                        toast("评价成功")
                        finish()
                    }
                }
            }
        }
    }

    /**
     * 展示商品信息
     */
    fun showProductView(orderBean: OrderBean) {
        val bean = orderBean.list[0]
        GlideUtil.loadImg(bean.image!!, img_product, this)
        tv_name.text = bean.name
        tv_des.text = bean.propertyValue
        tv_price.text = "¥${bean.price}"
    }


    /**
     * 选择图片
     */
    fun choosePicture(num:Int) {
        var maxNum = num
        hideSoftInput()
        if (picAdapter!!.data.size > 0) {
            maxNum = if (picAdapter!!.data[picAdapter!!.data.size - 1].file == ADD_PIC) {
                maxNum - picAdapter!!.data.size + 1
            } else {
                maxNum - picAdapter!!.data.size
            }
        }
        // 进入相册 以下是例子：用不到的api可以不写
        PictureSelector.create(this)
            .openGallery(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
            .maxSelectNum(maxNum)// 最大图片选择数量 int
            .imageSpanCount(4)// 每行显示个数 int
            .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
            .previewImage(true)// 是否可预览图片 true or false
            .isCamera(true)// 是否显示拍照按钮 true or false
            .imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
            .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
            .sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
            .setOutputCameraPath("/CustomPath")// 自定义拍照保存路径,可不填
            .compress(true)// 是否压缩 true or false
            .withAspectRatio(1, 1)// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
            .hideBottomControls(true)// 是否显示uCrop工具栏，默认不显示 true or false
            .compressSavePath(getPath())//压缩图片保存地址
            .freeStyleCropEnabled(true)// 裁剪框是否可拖拽 true or false
            .circleDimmedLayer(false)// 是否圆形裁剪 true or false
            .showCropFrame(true)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false   true or false
            .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false    true or false
            .selectionMedia(picList)// 是否传入已选图片 List<LocalMedia> list
            .previewEggs(true)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中) true or false
//                .cropCompressQuality()// 裁剪压缩质量 默认90 int
            .minimumCompressSize(100)// 小于100kb的图片不压缩
            .synOrAsy(true)//同步true或异步false 压缩 默认同步
//                .cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效 int
            .rotateEnabled(true) // 裁剪是否可旋转图片 true or false
            .scaleEnabled(true)// 裁剪是否可放大缩小图片 true or false
            .isDragFrame(true)// 是否可拖动裁剪框(固定)
            .forResult(PictureConfig.CHOOSE_REQUEST)//结果回调onActivityResult code
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                PictureConfig.CHOOSE_REQUEST -> {
                    // 图片、视频、音频选择结果回调
                    val selectList = PictureSelector.obtainMultipleResult(data)
//                    list = selectList
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true  注意：音视频除外
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true  注意：音视频除外
                    // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的

                    for (i in 0 until selectList.size) {
                        val fileBody = FileBody()
                        if (selectList[i].isCompressed) {
                            fileBody.file = selectList[i].compressPath
                        } else {
                            fileBody.file = selectList[i].path
                        }
                        fileBody.sort = i
                        picBodyList.add(fileBody)
                    }

                    //上传成功后  适配数据
                    UploadUtil.uploadFileBody(picBodyList,2,"正在上传图片……"){
                        val list :MutableList<FileBody> = arrayListOf()
                        picId = ""
                        for (i in 0 until it!!.size){
                            val body = FileBody()
                            body.file = it[i].imgUrl
                            body.id = it[i].imgId
                            body.sort = i
                            if (i == 0){
                                picId = it[i].imgId!!
                            }else{
                                picId += it[i].imgId!!
                            }
                            list.add(i,body)
                        }
                        picAdapter?.setNewData(list)
                    }
                }
            }
        }
    }

    /**
     * 隐藏输入法
     */
    fun hideSoftInput() {
        val imm =getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val view = window.peekDecorView()
        if (view != null) {
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}