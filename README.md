# IPManagerUtils


## Description：

    Setting your IP dynamically in the project.

## To get a Git project into your build:

- Step 1. Add it in your root build.gradle at the end of repositories:
```
	allprojects {
		repositories {
			...
			maven { url "https://jitpack.io" }
		}
	}
```

- Step 2. Add the dependency：
```
  dependencies {
	   implementation 'com.github.openerteam:IPManagerUtils-master:1.1.2'
	}
```
------
> 分割线----装逼失败

## Version 1.1.2
- 支持添加域名
- 优化IP输入

## 使用方法：
- 项目参考于https://github.com/Rickwan/IPManagerUtils, 感谢作者！

- 使用摇一摇触发IP设置功能：
在需要修改IP的activity的onStart()方法中注册并传出默认IP及名称、在onStop()方法中取消注册。
```
MBShakeUtils shakeUtils;

    @Override
    protected void onStart() {
        super.onStart();
	      shakeUtils = new MBShakeUtils(this);
	      shakeUtils.init(defaultIP, defaultPort, defaultName);
    }

    @Override
    protected void onStop() {
        super.onStop();
        shakeUtils.unRegister();
    }

```
- 使用按钮触发IP设置功能：
在onCreate方法中注册按钮模式并传入默认IP及名称：

        MBFloatWindowUtils floatWindowUtils= new MBFloatWindowUtils();
        floatWindowUtils.init(this, defaultIP, defaultPort, defaultName);

- 设置IP：  
 
 ``` 
Intent intent = new Intent(this, MBIPActivity.class);
startActivityForResult(intent, MBIPContant.REQUEST_CODE);
```

- 获取IP:(返回示例：192.168.1.33:8080)
 
- 方法1：设置成功后，可通过resultCode == MBIPContant.RESULT_CODE在onActivityResult()方法中获取IP,
 ```
  @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == MBIPContant.RESULT_CODE) {

        MBIPInfo info = (MBIPInfo) data.getSerializableExtra(MBIPContant.IP);

            if (!info.getPort().equals("*")) {//当IP设置为域名是端口号为非必传，默认为*
                ipView.setText("新IP地址：" + info.ip + ":" + info.port);
            }else {
                ipView.setText("新IP地址：" + info.ip);
            }

        }
    }
 ```
 
- 方法2：获取已设置的默认IP  
    
    ```
    MBIPUtils.getInstance(context).getIPPort();
    ```
- 方法3：当未设置IP时，可传入默认IP  
    
    ```
     MBIPUtils.getInstance(context).getIPPort(defeaultIP,defeaultPort);
    ```
    
