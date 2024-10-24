# 这是一个PVE的批量创建小工具，可以快速的创建多个PVE虚拟机。

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
java -jar /path/to/launch4j/launch4j.jar -jar target/pve-tools-1.0-SNAPSHOT.jar -outfile pve-tools.exe -icon docs/images/icon.ico
```

### 下载安装graalvm
下载[graalvm](https://www.graalvm.org/downloads/)

### 双击exe文件打开

## Linux或者MacOS下使用
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