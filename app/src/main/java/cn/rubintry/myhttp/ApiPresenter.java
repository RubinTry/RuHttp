package cn.rubintry.myhttp;

import android.util.Log;

import com.google.gson.Gson;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import cn.rubintry.ruhttp.IRuHttpRequestListener;
import cn.rubintry.ruhttp.MethodType;
import cn.rubintry.ruhttp.RuHttp;

public class ApiPresenter {

    private ViewInterface view;

    public ApiPresenter(ViewInterface view) {
        this.view = view;
    }

    public void subscribe(final int requestCode){
        List<String> strList = new ArrayList<>();
        strList.add("vc/biz/vehicle/*");
        RuHttp ruHttp = new RuHttp.Builder<>()
                .setMethod(MethodType.POST)
                .addParam("accessId" , "e608d9cabec040d68c1816ca3294747c")
                .addParam("routingKeyPatternList" , new Gson().toJson(strList))
                .addParam("sign" , "7D65F095DA026AE7C823B478B7CEB16F")
                .setUrl("http://101.37.71.102/vc/common/message/subscribe?")
                .setHttpRequestListener(new IRuHttpRequestListener<BaseModel>() {
                    @Override
                    public void onSuccess(BaseModel baseModel) {
                        if(baseModel.getCode() == 0){
                            view.onSucceed(baseModel.getData() , requestCode);
                        }else{
                            view.onFail(new Exception(baseModel.getMsg()) , requestCode);
                        }
                    }

                    @Override
                    public void onFail(Throwable e) {
                        view.onFail(e , requestCode);
                    }
                }).setType(BaseModel.class)
                .build();
        ruHttp.execute();
    }


    public void getMessage(final int requestCode){
        RuHttp ruHttp = new RuHttp.Builder<>()
                .setMethod(MethodType.POST)
                .addParam("accessId" , "e608d9cabec040d68c1816ca3294747c")
                .addParam("sign" , "291078CAF1A9E3ED4B546605D18F8DC2")
                .setUrl("http://101.37.71.102/vc/common/message/get")
                .setConnectTimeOut(30 * 3600)
                .setHttpRequestListener(new IRuHttpRequestListener<BaseModel>() {


                    @Override
                    public void onSuccess(BaseModel baseModel) {
                        if(baseModel.getCode() == 0){
                            view.onSucceed(baseModel.getData() , requestCode);
                        }else{
                            view.onFail(new Exception(baseModel.getMsg()) , requestCode);
                        }

                    }

                    @Override
                    public void onFail(Throwable e) {
                        view.onFail(e , requestCode);
                    }
                }).setType(BaseModel.class)
                .build();

        ruHttp.execute();
    }


    public interface ViewInterface<T>{
        void onSucceed(T data , int requestCode);

        void onFail(Throwable t , int requestCode);
    }
}
