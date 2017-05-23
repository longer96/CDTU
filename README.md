成都工业学院App
====
大一时针对我校开发的校园客户端，方便学生查课表、成绩、一卡通消费记录、失物招领等等。遵循MD设计原则，数据大多通过抓包获取，现开源（已屏蔽学校相关信息，怕被请去喝茶）
<br>Ps：第一次这么认真写文章，有不合理的地方希望大家提出   --- 谢谢谢谢啦

Introduction
----
功能的实现主要是通过模拟网页登录，通过抓包和爬虫获取数据，底层都是HttpClient写的（当时大一才接触Android，Httpclient已被官方废弃）。正往Mvp + RXjava 转型，在校期间有空就在更新软件，校内好评度很高，现毕业设计之后开源。
<br>Ps：有公司看得起的望给口饭吃（流泪）

实现功能<span id="jump">Hello World</span>  
----
* **App Splash页秒开**（感谢图形图像专业提供的UI设计）
  * 防止APP启动时白屏/黑屏  [参考博客](http://blog.csdn.net/yanzhenjie1003/article/details/52201896)  
  
* **本校学生可通过校园帐号一键登录App**

* 学校附近的商家可以通过手机号或者QQ一键登录 ps:但是不能进行学生相关的查询
  * 短信验证码使用的是：[Bmob短信服务](http://docs.bmob.cn/sms/Android/b_developdoc/doc/index.html) 
  * 同时推荐使用阿里大于的短信验证API：[阿里大于](http://www.alidayu.com/) 
  * [QQ三方登录官方文档](http://wiki.open.qq.com/wiki/QQ%E7%99%BB%E5%BD%95%E5%92%8C%E6%B3%A8%E9%94%80) 
  
  
* **课表功能的实现**（个人感觉非常好看，用户体验棒）
  * 高校大多数都是使用正方系统，课表的数据也是从正方系统爬取解析的，解析的方法大家可以参考utils目录下StreamTools.java getcourse方法。
  * 扁平设计，大大提升内容的占有率  
  * 个性化设计，可使用系统自带背景图以及自定义背景图
  * 用户可编辑课程，查看学期安排以及教学时间表

* **主页轮播图的实现**
  * 预览图使用的是学校的风景图，后台可更换图片url、数量、添加事件。
    * 图片是挂在在  [花瓣网](http://huaban.com/)   上的（自己服务器带宽小，学生党伤不起）
  * 轮播图的实现参考 [RollViewPager](https://github.com/Jude95/RollViewPager)


* **主页布局实现**（主要参考支付宝）
  * 底部导航实现参考  [AHBottomNavigation](https://github.com/aurelhubert/ahbottomnavigation)
  * 布局严格遵守 Material Design 准则。 [Material Design 中文版](http://wiki.jikexueyuan.com/project/material-design/)
  * 实现了信息的整合，将有效的信息提取显示，避免效益冗余。
  * 更多功能菜单的实现,可下载查看布局文件app_bar_main.xml    [参考博客](http://blog.csdn.net/yanzhenjie1003/article/details/51938425)
  * 水平RecyclerView的使用，相关博客：[RecyclerViewSnap](https://github.com/rubensousa/RecyclerViewSnap)
  * 场景过度动画 可百度`Shared Element Transition`

* **表白墙**
  * 点赞动画的实现 [ShineButton](https://github.com/ChadCSong/ShineButton)
  * 文字墙效果的实现 [文字飞去飞出效果](https://github.com/qiushi123/randomlayout)
  
* **图书馆**
  * 抓取我校图书馆相关数据，优雅的显示在手机上
    * 学生可以查询所借图书，历史借阅
    * 可以查询图书信息，检索图书（有时间可以深入完成高级检索功能）
    * 学生可以查询图书馆相关信息
    
* **一卡通消费记录**
  * 下拉刷新，上拉加载  刷新效果参考：[WaveSwipeRefreshLayout](https://github.com/recruit-lifestyle/WaveSwipeRefreshLayout)
  
* **正方系统** 
  * 在校成绩（查成绩很方便，期末app经常挤爆）
  * 课表查询
  * 等级考试、学分统计、考室查询（有时间可以扩展更多功能  如：一键评教、一键选课）

* **学校黄页**
  * 数据不是很齐全，没有官方提供数据。也迫于没有经济支持没能做大
  * 自己写的标签页，之后才发现网上有更好的 
    * 1.[FlowLayout](https://github.com/nex3z/FlowLayout)  
    * 2.[FlowLayout](https://github.com/hongyangAndroid/FlowLayout)
  * 显示数据仿魅族通讯录（上个手机是Mx3，魅族通讯录做的真的不错）

* **同学的店**（还对应一个商户版，用来管理、添加商品，接单等等，学生可以自己开店） 
  > Ps：最近没时间完善了
  * 左边导航栏的实现（仿外卖App以及京东） 开源项目：[VerticalTabLayout](https://github.com/qstumn/VerticalTabLayout)
  * 寝室的选择（友好的交互）  开源项目：[Carousel Picker](https://github.com/GoodieBag/CarouselPicker)
  * 店铺的选择  直接参考Store_Activity.java 中代码
  * 底部购物车的实现 [参考博客](http://blog.csdn.net/qq_17766199/article/details/53726062)
  * 加入购物车动画可参考 开源项目：[ShoppingCartAnimation](https://github.com/captain-miao/ShoppingCartAnimation)
 
* **失物招领**(界面实现还行)
  * 主界面（都是自己辛辛苦苦写出来的界面）
  * 详细界面（消息发布者可以编辑该消息）
  * 发布消息（需登录）
  * 其中也使用到了过渡动画，参考上面“主页布局实现-场景过度动画”
  * EditText的MD实现  可百度：`TextInputLayout`
  * 图片选择框架  开源项目：[ImageSelector](https://github.com/YancyYe/ImageSelector) 
   * 之后发现几个很好的图片选择框架，比如：[GalleryFinal](https://github.com/pengjianbo/GalleryFinal)
   * 同时附上2个图片压缩框架（该项目未使用，是自己写的） 
     * [CompressHelper](https://github.com/nanchen2251/CompressHelper)  
     * [Tiny](https://github.com/Sunzxyong/Tiny)


* **跳蚤市场**
  * 主界面（中规中矩），详细界面和上面的失误招领详细界面差不多就不重复了
  * 右下角FAB 的实现 参考博客：[MaterialSheetFab](https://github.com/gowong/material-sheet-fab),网上还有很多类似的就不详细介绍了
  * [Material Design](https://github.com/wasabeef/awesome-android-ui)
  * 更多福利点击
 

* **校园公告**
  * 抓取我校校园公告数据简单的显示  连接：[校园公告](http://www.cdtu.edu.cn/lanmuliebiao.jsp?urltype=tree.TreeTempUrl&wbtreeid=1012)

* **学生查询、一卡通查询**
  * 主布局（仿QQ个人信息布局）
  * 搜索控件是自己写的，网上有更好的实现，如：
    * [MaterialSearchView](https://github.com/MiguelCatalan/MaterialSearchView) 
    * [MaterialSearchBar](https://github.com/mancj/MaterialSearchBar) 
    * [PersistentSearch](https://github.com/KieronQuinn/PersistentSearch) 

* **校车、校历、学生处**
  * 图片放缩控件 该App使用的是：[PinchImageView](https://github.com/boycy815/PinchImageView),同时附上2个不错的框架
    * [PhotoView](https://github.com/chrisbanes/PhotoView)
    * [FrescoImageViewer](https://github.com/stfalcon-studio/FrescoImageViewer)
  * 网络图片加载框架用的是：[Glide图片加载](http://www.jianshu.com/p/4a3177b57949)，有很多优秀的图片加载框架博文中也有提到
    * Glide加载圆图、白边圆图、高斯模糊见：[更多补充](#jump)

* **关于App**
  * 检查更新（打开App就有自动更新，分为强制和非强制更新，自己写的可以参考）
  * 滑动折叠的实现可以参考：[博客](http://www.jianshu.com/p/f22c0f50ac3f)

* **校园消息通知**（若有消息，主界面显示，一般用于通知学校相关活动，以及学生会相关）

* **换肤框架**（自己写的比较渣，简单略过）

* **一卡通消费记录、改密、挂失**
  * 使用Material-dialogs接受用户的输入





更多补充
----
* 阿狸图标
* 图片加载框架
* 360加固
* 小米统计
* 小米推送
* QQ一键加群
* jsoup解析网页
* json用的自带的解析器
* 接入分享App、跳转到应用商店评论功能

福利
----
  > 自己在开发过程中收集的一些有用的干货现在全部分享出来，个人的开发能力不强，但更愿意借助巨人的肩膀

Apk下载
----

关于我
----
即将毕业的学生，因学历压制，想找个对口的工作都不容易（工作尽心尽责，有上进心）   
<br>★★★★★   **Ps：望收留！（成都）**   ★★★★★
<br>如果看见这篇文章的你也想给自己的学校开发一个App、或是遇到一些问题都可以联系我
<br>QQ：[1114070859](#null)
<br>Email：longer2016@qq.com

[点击跳转](#jump)
