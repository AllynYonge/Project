# MVPProject介绍
该项目是MVP结构，依赖了一些常用的开源库

## data
在该模块中有一个pom.xml文件是用来maven构建的，因为用了腾讯的tars框架，而该框架只支持maven构建，所以就加上了它。若不需要使用这个框架的可以删掉pom.xml文件，同时删掉
```gradle
task tars2jar(type:Exec){
    workingDir "E:\\Job\\workApp\\KingReading\\data"
    commandLine  'cmd', '/c', 'mvn', 'qq-cloud-central:tars-maven-plugin:1.0.1:tars2java'
}

dependencies {
    //tars
    compile dataDependencies.tars
}
```
