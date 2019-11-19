package com.wbg.xigui.ui.mine

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.wbg.xigui.R
import com.wbg.xigui.XApplication
import com.wbg.xigui.adapter.PicAdapter
import com.wbg.xigui.adapter.PopRefundAdapter
import com.wbg.xigui.base.BaseXActivity
import com.wbg.xigui.bean.FileBody
import com.wbg.xigui.bean.OrderBean
import com.wbg.xigui.bean.RefundSelectBean
import com.wbg.xigui.bean.body.RefundBody
import com.wbg.xigui.rout.RoutUrl
import com.wbg.xigui.ui.comment.AddCommentActivity
import com.wbg.xigui.utils.GlideUtil
import com.wbg.xigui.utils.LubanImg
import com.wbg.xigui.utils.ResourcesUtil
import com.wbg.xigui.utils.UploadUtil
import com.wbg.xigui.viewmodel.RefundViewModel
import com.wbg.xigui.widget.AndroidBug5497Workaround
import kotlinx.android.synthetic.main.apply_refund_activity.*
import kotlinx.android.synthetic.main.pop_refund.*
import toast

/**
 * @author xyx
 * @date :2019/7/10 10:24
 * 申请退款
 */
@Route(path = RoutUrl.Mine.refund)
class RefundActivity : BaseXActivity<RefundViewModel>(), View.OnClickListener {
    var picAdapter: PicAdapter? = null
    var picList: MutableList<LocalMedia> = arrayListOf()//图片列表
    var list: MutableList<FileBody> = arrayListOf()//图片列表
    var picBodyList: MutableList<FileBody> = arrayListOf()//上传文件列表
    var picId = ""
    var typeBean: RefundSelectBean? = null
    var orderBean: OrderBean? = null
    var reason = 0
    var received = 0
    var type1 = 1
    override fun getSmartLayout(): SmartRefreshLayout? {
        return null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidBug5497Workaround.assistActivity(this)
    }

    override fun initView() {
        setTitle("申请退款")
        addView(R.layout.apply_refund_activity)
        orderBean = intent.getBundleExtra(AddCommentActivity.KEY_BUNDLE).get(AddCommentActivity.KEY_OREDR_BEAN) as OrderBean
        setListener()
    }

    fun setListener() {
        submit_btn.setOnClickListener(this)
        type_ll.setOnClickListener(this)
        state_ll.setOnClickListener(this)
        reason_ll.setOnClickListener(this)
    }

    override fun initData() {

        val linearLayoutManager = LinearLayoutManager(this)
        rv_pic.layoutManager = linearLayoutManager
        picAdapter = PicAdapter()
        rv_pic.adapter = picAdapter

        val fileBody = FileBody(PicAdapter.ADD_PIC, "", 0)
        list.add(fileBody)
        picAdapter?.setNewData(list)

        picAdapter?.setOnItemClickListener { adapter, view, position ->
            choosePicture(3)
        }

        GlideUtil.loadImg(orderBean?.list!![0].image!!, img_product, this)
        tv_name.text = orderBean?.list!![0].name
        tv_des.text = orderBean?.list!![0].propertyValue
        tv_money1.text = "¥" + orderBean?.totalAmount
        tv_money.text = "最多¥" + orderBean?.totalAmount
        tv_coupon_money.text = "若退款成功，将退还您¥" + orderBean?.totalAmount

        edt_reason.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()) {
                    tv_num.text = "您还可以输入${170 - s.toString().length}字"
                } else {
                    if (s.length <= 170) {
                        tv_num.text = "您还可以输入${170 - s.toString().length}字"
                    } else {
                        tv_num.text = "您还可以输入${170 - s.toString().length}字"
                        toast("最多只能评论170个字哟")
                        //这里是将超过150字之外的截取掉
                        val a = s.delete(170, s.length)
                        Editable.Factory.getInstance().newEditable(a)
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.type_ll -> { //选择退款种类
                showSelectDialog(0,
                        arrayListOf(RefundSelectBean("退款"),
                                RefundSelectBean("退货"))
                )
            }
            R.id.state_ll -> { //点击选择货物状态
                showSelectDialog(1,
                        arrayListOf(RefundSelectBean("未收到货"),
                                RefundSelectBean("已收到货"))
                )
            }
            R.id.reason_ll -> { //点击选择申请原因
                showSelectDialog(2,
                        arrayListOf(RefundSelectBean("多拍，错拍，不想要"),
                                RefundSelectBean("不喜欢，效果不好"),
                                RefundSelectBean("货物与描述不符"),
                                RefundSelectBean("质量问题"),
                                RefundSelectBean("收到商品少件,破损或污渍"),
                                RefundSelectBean("空包裹"),
                                RefundSelectBean("卖家发错货"),
                                RefundSelectBean("其他")
                        )
                )
            }
            R.id.submit_btn -> {//提交
                val body = RefundBody()
                body.images = picId
                body.memberId = XApplication.instance.getMemberId()
                body.memo = edt_reason.text.toString()
                body.mobile = orderBean?.consigneeMobile
                body.orderId = orderBean?.orderId
                body.reason = reason
                body.received = received
                body.skuId = orderBean?.list!![0].skuId
                body.type = type1 + 1
                mViewModel.refund(body) {
                    if (it?.flag!!) {
                        finish()
                    }
                }
            }
        }
    }


    private fun showSelectDialog(type: Int, list: ArrayList<RefundSelectBean>) {
        val view = LayoutInflater.from(this).inflate(R.layout.pop_refund, null)
        val dialog = Dialog(this)
        when (type) {
            0 -> {
                title_tv.text = "申请类型"
            }
            1 -> {
                title_tv.text = "收货状态"
            }
            2 -> {
                title_tv.text = "申请原因"
            }
        }
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = (object : PopRefundAdapter(typeBean, this, list) {
            override fun onItemClick(bean: RefundSelectBean, position: Int) {
                when (type) {
                    0 -> {
                        type1 = position
                        typeBean = bean
                        type_tv.text = bean.name
                    }
                    1 -> {
                        received = position
                        typeBean = bean
                        tv_status.text = bean.name
                    }
                    2 -> {
                        reason = position
                        typeBean = bean
                        tv_reason.text = bean.name
                    }
                }
                dialog.cancel()
            }

        })
        dialog.setContentView(view)
        dialog.show()
        val window = dialog.window
        window?.setBackgroundDrawableResource(R.color.transparent)
        val lp = window!!.attributes
        lp.gravity = Gravity.BOTTOM
        lp.width = ResourcesUtil.getScreenWidth()
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        window.attributes = lp
    }

    /**
     * 选择图片
     */
    fun choosePicture(num: Int) {
        hideSoftInput()
        var maxNum = num
        if (picAdapter!!.data.size > 0) {
            maxNum = if (picAdapter!!.data[picAdapter!!.data.size - 1].file == PicAdapter.ADD_PIC) {
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
                .compressSavePath(LubanImg.getPath())//压缩图片保存地址
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
                    UploadUtil.uploadFileBody(picBodyList, 2, "正在上传图片……") {
                        val list: MutableList<FileBody> = arrayListOf()
                        picId = ""
                        for (i in 0 until it!!.size) {
                            val body = FileBody()
                            body.file = it[i].imgUrl
                            body.id = it[i].imgId
                            body.sort = i
                            if (i == 0) {
                                picId = it[i].imgId!!
                            } else {
                                picId += it[i].imgId!!
                            }
                            list.add(i, body)
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
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val view = window.peekDecorView()
        if (view != null) {
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }


}