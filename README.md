成都工业学院App
====
大二时针对我校开发的校园客户端，方便学生查课表、成绩、一卡通消费记录、跳蚤市场、失物招领等等。遵循MD开发原则，数据大多通过抓包获取，现开源（已屏蔽学校相关信息，怕被请去喝茶）
<br>Ps：第一次这么认真写文章，希望有不合理的地方帮我提出   -----谢谢啦

Introduction
----
功能的实现主要是通过模拟网页登录，通过抓包和爬虫获取数据，底层都是HttpClient写的（当时大二才接触Android，Httpclient已被官方废弃）。正往MVP转型，在校期间有空就在更新软件，校内好评度很高，现毕业设计之后开源。
<br>Ps：有公司看得起的望给口饭吃（流泪）

实现功能
----
* App Splash页秒开（感谢图形图像专业提供的UI设计）
  * 防止APP启动时白屏/黑屏  [参考博客](http://blog.csdn.net/yanzhenjie1003/article/details/52201896)  

* 本校学生可通过校园帐号一键登录App

* 学校附近的商家可以通过手机号或者QQ一键登录 ps:但是不能进行学生相关的查询
  * 短信验证码使用的是：[Bmob短信服务](http://docs.bmob.cn/sms/Android/b_developdoc/doc/index.html) 
  * 同时推荐使用阿里大于的短信验证API：[阿里大于](http://www.alidayu.com/) 
  * [QQ三方登录官方文档](http://wiki.open.qq.com/wiki/QQ%E7%99%BB%E5%BD%95%E5%92%8C%E6%B3%A8%E9%94%80) 
  
* 课表功能的实现（个人感觉非常好看，用户体验棒）
  * 高校大多数都是使用正方系统，课表的数据也是从正方系统爬取解析的，解析的方法大家可以参考utils目录下StreamTools.java getcourse方法。
  * 扁平设计，大大提升内容的占有率  
  * 个性化设计，可使用系统自带背景图以及自定义背景图
  * 用户可编辑课程，查看学期安排以及教学时间表

* 主页轮播图的实现
  * 预览图使用的是学校的风景图，后台可更换图片url、数量、添加事件。
    * 图片是挂在在 [花瓣网](http://huaban.com/)上的（自己服务器带宽小，学生党伤不起）
  * 轮播图的实现参考 [RollViewPager](https://github.com/Jude95/RollViewPager)


* 主页布局实现（主要参考支付宝）
  * 底部导航实现参考 [AHBottomNavigation](https://github.com/aurelhubert/ahbottomnavigation)
  * 布局严格遵守 Material Design 准则。 [Material Design 中文版](http://wiki.jikexueyuan.com/project/material-design/)
  * 实现了信息的整合，将有效的信息提取显示，避免效益冗余。
  * 更多功能菜单的实现,可下载查看布局文件app_bar_main.xml [参考博客](http://blog.csdn.net/yanzhenjie1003/article/details/51938425)
  * 水平RecyclerView的使用
  * 场景过度动画 可百度`Shared Element Transition`

* 表白墙
  * 点赞动画的实现 [ShineButton](https://github.com/ChadCSong/ShineButton)
  
* 图书馆
  * 抓取我校图书馆相关数据，优雅的显示在手机上
    * 学生可以查询所借图书，历史借阅
    * 可以查询图书信息，检索图书（有时间可以深入完成高级检索功能）
    * 学生可以查询图书馆相关信息
    
* 一卡通消费记录
  * 下拉刷新，上拉加载  刷新效果参考：[WaveSwipeRefreshLayout](https://github.com/recruit-lifestyle/WaveSwipeRefreshLayout)
  
* 正方系统 
  * 在校成绩（查成绩很方便，期末app经常挤爆）
  * 课表查询
  * 等级考试、学分统计、考室查询（有时间可以扩展更多功能  如：一键评教、一键选课）

* 学校黄页
  * 数据不是很齐全，没有官方提供数据。也迫于没有经济支持没能做大
  * 自己写的标签页，之后才发现网上有更好的 [FlowLayout](https://github.com/nex3z/FlowLayout)  还有：[FlowLayout](https://github.com/hongyangAndroid/FlowLayout)
  * 显示数据仿魅族通讯录（上个手机是Mx3，魅族通讯录做的真的不错）


* 同学的店
* 失物招领
* 跳蚤市场

* 自己写的一个换肤框架，比较渣，简单略过


* 接入分享App、跳转到应用商店评论功能

* 可以查询一卡通消费记录、一卡通改密、以及一卡通挂失。





其他补充
----

Apk下载
----

关于我
----
即将毕业的学生，因学历压制，想找个对口的工作都不容易（工作尽心尽责，有上进心）   ★★★★★ Ps：望收留！（成都）
<br>如果看见这篇文章的你也想给自己的学校开发一个App、或是遇到一些问题都可以联系我
<br>QQ：1114070859
<br>邮箱：longer2016@qq.com
