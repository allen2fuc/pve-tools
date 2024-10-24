# 简述
这是一个PVE的批量创建小工具，根据模版，可以快速的创建多个PVE虚拟机。

**注意事项**
- CPU显示的是单核数。 
- 内存、标签可在线编辑，按回车保存。
- 重启的话需要是开机状态才能重启。
- 删除的话页面显示deleted，可以点击查询就会删除掉。
- 查询相当于刷新，按回车键即可，新增的窗口也有快捷键，删除的提示按钮Y和N没有做监听。
- 新增虚拟机会按序号递增，如果序号缺失会补缺，序号从01开始，每次最多新增50台虚拟机。
- 新增的虚拟机会自动启动
- 登陆后会加密保存登陆信息，下次登陆不需要输入密码。

**以下是玩法操作步骤，当然也可以不依赖该操作**
1. 创建Windows Server 2019, 创建域环境。
2. 创建Windows10虚拟机，安装qemu-guest-agent, 加入域环境。
3. 将Windows10转成模版，用于快速创建。
4. 安装这个小程序，快速创建多个虚拟机。
5. 客户端操作，下载Virtual-Manager并且克隆https://github.com/joshpatten/PVE-VDIClient进行客户端连接。


## 源码构建

克隆代码到本地, 前提需要安装maven和java环境
```bash
git clone https://github.com/allen2fuc/pve-tools.git

## 或者手动下载zip包代码
https://github.com/allen2fuc/pve-tools/archive/refs/heads/main.zip
```
编译代码
```bash
mvn clean package
```
运行代码
```bash
java -jar target/pve-tools-1.0-SNAPSHOT.jar
```


## Windows下使用说明
### 打包
```bash
mvn clean package
```

### 下载launch4j
下载[launch4j](https://sourceforge.net/projects/launch4j/files/launch4j-3/3.50/)

### 运行launch4j
```bash
# 把JDK也打包进去
java -jar ~/Downloads/launch4j/launch4j.jar launch4j-config.xml
```

### 下载安装graalvm
下载[graalvm](https://www.graalvm.org/downloads/)

### 双击exe文件打开
这里必须得本地安装graalvm23才能使用，不然提示找不到环境。



## Linux或者MacOS下使用
这种方式貌似还有点问题，提示找不到java.awt的包
### 下载安装graalvm
下载[graalvm](https://www.graalvm.org/downloads/)

### 打包
```bash
mvn clean package
```

### 打包成字节包
```bash
native-image -jar target/pve-tools-1.0-SNAPSHOT.jar
```

### 运行
```bash
./pve-tools-1.0-SNAPSHOT
```