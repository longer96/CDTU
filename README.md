成都工业学院App
====
![Logo](https://github.com/longer96/CDTU/blob/master/images/logo.png)
<img width="80" height="80" src="https://github.com/longer96/CDTU/blob/master/images/logo.png"/>
<br>大一时针对我校开发的校园客户端，方便学生查课表、成绩、一卡通消费记录、失物招领等等。遵循MD设计原则，数据大多通过抓包获取，现开源（已屏蔽学校相关信息，怕被请回去喝茶）
<br>Ps：第一次这么认真写文章，有不合理的地方希望大家提出   --- 谢谢谢谢啦
<br>
<br>

----
Introduction
----
功能的实现主要是通过模拟网页登录，通过抓包和爬虫获取数据，底层都是HttpClient写的（当时大一才接触Android，Httpclient已被官方废弃）。正往Mvp + RXjava 转型，在校期间有空就在更新软件，校内好评度很高，现毕业设计之后开源。
<br>Ps：有公司看得起的望给口饭吃（流泪）



 <br><br>![img]()
实现功能
----
* **App Splash页秒开**（感谢图形图像专业提供的UI设计）
    > 涉及代码：AppSplashActivity.java
  * 防止APP启动时白屏/黑屏  [参考博客](http://blog.csdn.net/yanzhenjie1003/article/details/52201896)  
  <br><br>![秒开](https://github.com/longer96/CDTU/blob/master/images/%E7%A7%92%E5%BC%80.gif)
  <img width="200" height="200" src="https://github.com/longer96/CDTU/blob/master/images/%E7%A7%92%E5%BC%80.gif"/>
  
<br><br>
* **本校学生通过校园帐号登录**
    > 涉及代码：LoginSchool_Activity.java
  * 若作为刚接触的同学可以参考后面补充，写的很详细（大佬绕道）：**更多补充_模拟网页登录**
     <br><br>![img](https://github.com/longer96/CDTU/blob/master/images/%E6%A0%A1%E5%9B%AD%E5%8F%B7%E7%99%BB%E5%BD%95.gif)

<br><br>
* **其他用户可以通过手机号或QQ一键注册、登录** 
  > ps：但是不能进行学生相关的查询。涉及代码：Register_Activity.java , LoginPhone_Activity.java
  * 短信验证码使用的是：[Bmob短信服务](http://docs.bmob.cn/sms/Android/b_developdoc/doc/index.html) 
  * 同时推荐使用阿里大于的短信验证API：[阿里大于](http://www.alidayu.com/) 
  * 中间还涉及到手机号码的验证，介绍一个好用的工具集合：[AndroidUtilCode](http://www.jianshu.com/p/72494773aace)
  * [QQ三方登录官方文档](http://wiki.open.qq.com/wiki/QQ%E7%99%BB%E5%BD%95%E5%92%8C%E6%B3%A8%E9%94%80) 
   <br><br>![注册](https://github.com/longer96/CDTU/blob/master/images/%E6%B3%A8%E5%86%8C.gif)   ![QQ登录](https://github.com/longer96/CDTU/blob/master/images/QQ%E7%99%BB%E5%BD%95.gif)
  
<br><br>
* **课表功能的实现**（很好看，用户体验棒）
  > 涉及代码：CourseActivity.java , Course_addActivity.java , Course_editActivity.java
  * 高校大多数都是使用正方系统，课表的数据也是从正方系统爬取解析的，解析的方法大家可以参考utils目录下StreamTools.java getcourse方法。
  * 扁平设计，大大提升内容的占有率  
  * 个性化设计，可使用系统自带背景图以及自定义背景图
  * 用户可编辑课程，查看学期安排以及教学时间表
  <br><br>![课表](https://github.com/longer96/CDTU/blob/master/images/%E8%AF%BE%E8%A1%A8%E5%B1%95%E7%A4%BA.gif) ![课表](https://github.com/longer96/CDTU/blob/master/images/%E6%8D%A2%E8%83%8C%E6%99%AF.gif)  ![img](https://github.com/longer96/CDTU/blob/master/images/课表添加课.gif)
  <br>![11](https://github.com/longer96/CDTU/blob/master/images/%E8%AF%BE%E8%A1%A8.png)
 
 
<br><br>
* **主页轮播图的实现**
  > 涉及代码：MainActivity.java
  * 预览图使用的是学校的风景图，后台可更换图片url、数量、添加事件。
    * 图片是挂在在  [花瓣网](http://huaban.com/)   上的（自己服务器带宽小，学生党伤不起）
  * 轮播图的实现参考 [RollViewPager](https://github.com/Jude95/RollViewPager)


<br><br>
* **主页布局实现**（主要参考支付宝）
  > 涉及代码：MainActivity.java , Fragment_Menu.java
  * 底部导航实现参考  [AHBottomNavigation](https://github.com/aurelhubert/ahbottomnavigation)
  * 布局严格遵守 Material Design 准则。 [Material Design 中文版](http://wiki.jikexueyuan.com/project/material-design/)
  * 实现了信息的整合，将有效的信息提取显示，避免效益冗余。
  * 更多功能菜单的实现,可下载查看布局文件app_bar_main.xml    [参考博客](http://blog.csdn.net/yanzhenjie1003/article/details/51938425)
  * 水平RecyclerView的使用，相关博客：[RecyclerViewSnap](https://github.com/rubensousa/RecyclerViewSnap)
  * 场景过度动画 可百度`Shared Element Transition`


<br><br>
* **表白墙**
  > 涉及代码：LoveActivity.java , LoveOne_Activity.java
  * 点赞动画的实现 [ShineButton](https://github.com/ChadCSong/ShineButton)
  * 文字墙效果的实现 [文字飞入、飞出效果](https://github.com/qiushi123/randomlayout)
  
 
<br><br> 
* **图书馆**
  > 涉及代码：library 文件下所有 
  * 抓取我校图书馆相关数据，优雅的显示在手机上
    * 学生可以查询所借图书，历史借阅
    * 可以查询图书信息，检索图书（有时间可以深入完成高级检索功能）
    * 学生可以查询图书馆相关信息
    
    
<br><br>    
* **一卡通消费记录**
  > 涉及代码：Card_Activity.java 
  * 下拉刷新，上拉加载  刷新效果参考：[WaveSwipeRefreshLayout](https://github.com/recruit-lifestyle/WaveSwipeRefreshLayout)
  
  
<br><br>  
* **正方系统** 
  > 涉及代码：zfxt 文件下所有 
  * 在校成绩（查成绩很方便，期末app经常挤爆）
  * 课表查询
  * 等级考试、学分统计、考室查询（有时间可以扩展更多功能  如：一键评教、一键选课）


<br><br>
* **学校黄页**
  > 涉及代码：Yellow_Activity.java , Sqlite_selectActivity.java
  * 数据不是很齐全，没有官方提供数据。也迫于没有经济支持没能做大
  * 自己写的标签页，之后才发现网上有更好的 
    * [FlowLayout](https://github.com/nex3z/FlowLayout)  
    * [FlowLayout2](https://github.com/hongyangAndroid/FlowLayout)
  * 显示数据仿魅族通讯录（上个手机是Mx3，魅族通讯录做的真的不错）


<br><br>
* **同学的店**（还对应一个商户版，用来管理、添加商品，接单等等，学生可以自己开店） 
  > Ps：最近没时间完善了,涉及代码：Store_Activity.java
  * 左边导航栏的实现（仿外卖App以及京东） 开源项目：[VerticalTabLayout](https://github.com/qstumn/VerticalTabLayout)
  * 寝室的选择（友好的交互）  开源项目：[Carousel Picker](https://github.com/GoodieBag/CarouselPicker)
  * 店铺的选择  直接参考Store_Activity.java 中代码
  * 底部购物车的实现 [参考博客](http://blog.csdn.net/qq_17766199/article/details/53726062)
  * 加入购物车动画可参考 开源项目：[ShoppingCartAnimation](https://github.com/captain-miao/ShoppingCartAnimation)
 
 
<br><br> 
* **失物招领**(界面实现还行)
  > 涉及代码：MainActivity.java , Fragment_Menu.java , Fragment_Lost.java
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
   <br><br>![投票选择](https://raw.githubusercontent.com/YancyYe/ImageSelector/master/resource/gif_1.gif)

<br><br>
* **跳蚤市场**
  > 涉及代码：MainActivity.java , Fragment_Menu.java , Fragment_Goods.java
  * 主界面（中规中矩），详细界面和上面的失误招领详细界面差不多就不重复了
  * 右下角FAB 的实现 参考博客：[MaterialSheetFab](https://github.com/gowong/material-sheet-fab),网上还有很多类似的就不详细介绍了
  * [Material Design](https://github.com/wasabeef/awesome-android-ui)
  * 更多福利点击
 
<br><br>
* **校园公告**
  > 涉及代码：NewsActivity.java , News_bodyActivity.java
  * 抓取我校校园公告数据简单的显示  连接：[校园公告](http://www.cdtu.edu.cn/lanmuliebiao.jsp?urltype=tree.TreeTempUrl&wbtreeid=1012)


<br><br>
* **学生查询、一卡通查询**
  > 涉及代码：QueryActivity.java , Query_cardActivity.java
  * 主布局（仿QQ个人信息布局）
  * 搜索控件是自己写的，网上有更好的实现，如：
    * [MaterialSearchView](https://github.com/MiguelCatalan/MaterialSearchView) 
    * [MaterialSearchBar](https://github.com/mancj/MaterialSearchBar) 
    * [PersistentSearch](https://github.com/KieronQuinn/PersistentSearch) 


<br><br>
* **校车、校历、学生处**
  > 涉及代码：ImageActivity.java , Calan_Activity.java , StudentActivity.java
  * 图片放缩控件 该App使用的是：[PinchImageView](https://github.com/boycy815/PinchImageView),同时附上2个不错的框架
    * [PhotoView](https://github.com/chrisbanes/PhotoView)
    * [FrescoImageViewer](https://github.com/stfalcon-studio/FrescoImageViewer)
  * 网络图片加载框架用的是：[Glide图片加载](http://www.jianshu.com/p/4a3177b57949)，有很多优秀的图片加载框架博文中也有提到
    * Glide加载圆图、白边圆图、高斯模糊见：[更多补充](#jump)


<br><br>
* **关于App**
  > 涉及代码：Info_Activity.java
  * 检查更新（打开App就有自动更新，分为强制和非强制更新，自己写的可以参考）
  * 滑动折叠的实现可以参考：[博客](http://www.jianshu.com/p/f22c0f50ac3f)

<br><br>
* **校园消息通知**（若有消息，主界面显示，一般用于通知学校相关活动，以及学生会相关）


<br><br>
* **换肤框架**（自己写的比较渣，简单略过）
  > 涉及代码：SkinActivity.java

<br><br>
* **一卡通改密、挂失**
  > 涉及代码：CardToolsActivity.java
  * 使用Material-dialogs接受用户的输入





更多补充
----
<br><br>
* **模拟网页登录**（初学者）
  > 能把以下3篇内容吸收就没有问题了
  * [毕业设计想把学校教务系统的功能模块做成手机APP?](https://www.zhihu.com/question/31566835)（灰常灰常详细）
  * [打造超级课程表一键提取课表功能](http://blog.csdn.net/sbsujjbcy/article/details/44004595)
  * [如何做一个Android APP来访问正方教务管理系统查成绩？](https://www.zhihu.com/question/37111747)
  * 补充：[模拟登陆带验证码的网站客户端](http://blog.csdn.net/mypanlong/article/details/44734613)

<br><br>
* **图标素材网**
  * [阿里巴巴图标库](http://www.iconfont.cn/) 
  * [easyicon.net](http://www.easyicon.net/)
  * [不容错过的10个图标素材网站](http://www.jianshu.com/p/883ae50a846e)
  
<br><br>  
* **图片加载框架**
  * [Gilde](https://github.com/bumptech/glide)
  * [使用方法及简介](https://mrfu.me/2016/02/27/Glide_Getting_Started/)
  * [Glide加载圆形图片，设置白色边框](http://blog.csdn.net/u010694658/article/details/53539486)
  * [Glide的拓展(高斯模糊、加载监听、圆角图片)](http://www.jianshu.com/p/4107565955e4?open_source=weibo_search)
  
<br><br>  
* **应用加固**(第三方加固平台很多，仁者见仁)
  * [5大移动应用加固平台评测](http://www.cnblogs.com/redbricks/p/5666202.html)
  * 该App使用的是：[360加固宝](http://jiagu.360.cn/)

<br><br>
* **小米统计**
  * 国内做应用统计的也有很多，大家可以自行了解后选择
  * [小米统计服务使用指南](https://dev.mi.com/doc/p=3995/index.html)
  
  
<br><br>  
* **小米推送**
  * 推送了解：[安卓推送这件小事](https://zhuanlan.zhihu.com/p/26053061)
  * 国内做第三方推送的也有很多：[Android推送SDK哪家好？](https://www.zhihu.com/question/22354498)
  * 大家自行选择，该App使用的是：[小米推送](https://dev.mi.com/doc/p=6421/index.html)


<br><br>
* **QQ一键加群**
  * 原来以为需要接入QQ Sdk才能实现，结果却是很简单：[QQ一键加群](http://qun.qq.com/join.html)



<br><br>
* **jsoup解析网页**
  * [利用Jsoup抓取数据](http://www.jianshu.com/p/b7ee086e6eae)



<br><br>
* **接入分享App、跳转到应用商店评论功能**
  * 这块网上教程很多，也不复杂，博主就不啰嗦了




福利
----
  > 自己在开发过程中收集的一些有用的干货现在全部分享出来，总的来说：个人的开发能力不强，但更愿意借助巨人的肩膀
  * [最全面的 Material Design 学习资料](https://github.com/Luosunce/material-design-data)
  * 博主每天有时间就在看的干货App：[Gank.Io非官方客户端应用](https://github.com/HotBitmapGG/gank.io-unofficial-android-client)
  * [安卓书签网](https://github.com/ColorfulCat/AndroidGuide)
  * [Android开发技术周报](http://www.androidweekly.cn/)
  * 有很多不错的Demo：[开源代码](http://jcodecraeer.com/plus/list.php?tid=31&TotalResult=1359&PageNo=1)
  * [Android 资源大全中文版](https://github.com/jobbole/awesome-android-cn)
  
  * **UE、UI 相关的网站**
     * [App整体布局参考](https://material.uplabs.com/)
     * [RGB对照表](http://tool.oschina.net/commons?type=3)
     * [图片在线压缩](http://zhitu.isux.us/)
     
   

  * **RxJava 的学习**
    * [给 Android 开发者的 RxJava 详解](http://gank.io/post/560e15be2dca930e00da1083)
    * [RxJava是什么](http://wuxiaolong.me/2016/01/18/rxjava/)
    * [给初学者的RxJava2.0教程](http://www.jianshu.com/p/464fa025229e)
    * [ MVP+Retrofit+RxJava实践小结](https://zhuanlan.zhihu.com/p/22042786?utm_source=qq&utm_medium=social)
  * [Android之App瘦身攻略](http://www.jianshu.com/p/99f3c09982d4)



Apk下载
----
1. Pre.im http://pre.im/cdgyxy
2. 应用商店搜索 “**成都工业学院**” 
3. [下载Demo](https://github.com/longer96/CDTU/blob/master/apk/cdtu_3.3.apk)  


关于我
----
即将毕业的学生，因学历压制，想找个对口的工作都不容易（工作尽心尽责，有上进心）   
<br>★★★★★   **Ps：望收留！（成都）**   ★★★★★
<br>如果看见这篇文章的你也想给自己的学校开发一个App、或是遇到一些问题都可以联系我
<br><br>QQ：[1114070859](#null)
<br>Email：longer2016@qq.com