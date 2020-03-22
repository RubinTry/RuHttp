[![logo](https://github.com/RubinTry/RuHttp/blob/master/repository/img/logo.png)](https://github.com/RubinTry/RuHttp)



# About
**[RuHttp][readme]**:fire: 是一个高性能、高并发、高扩展性的安卓http客户端，它可以使您的内容加载更快并节省带宽。:fire:

## 特性
1.目前支持基于http的GET、POST请求<br/>
2.支持Android x<br/>
3.支持高并发




## 如何引入

### 如何使用
```java
   RuHttp ruHttp = new RuHttp.Builder<>()
                .setMethod(MethodType.POST)//指定请求方式
                .setUrl(url)
                .addParam("key", "value")
                .addParam("key", "value")
                .addParam("key", "value")
                .setType(你的model类.class)
                .setHttpRequestListener(new IRuHttpRequestListener<你的model类>() {
                    @Override
                    public void onSuccess(你的model类 o) {
                        ...
                    }

                    @Override
                    public void onFail(Throwable e) {
                        ...
                    }
                }).build();
    //发起请求
    ruHttp.execute();
```


## License
```text
Copyright 2020 Sunzhong Lu

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```


[readme]: https://github.com/Rubintry/RuHttp
[auc]: https://github.com/Rubintry/RuHttp
[result]: https://android-arsenal.com/result?level=14