import { ComponentsModule } from './../components/components.module';

import { NgModule, ErrorHandler, NgZone, LOCALE_ID } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { IonicApp, IonicModule, IonicErrorHandler, Platform, LoadingController, Events } from 'ionic-angular';
import { MyApp } from './app.component';
import { Http, HttpModule } from '@angular/http';
import { StatusBar } from '@ionic-native/status-bar';
import { SplashScreen } from '@ionic-native/splash-screen';
import { AppServiceProvider } from '../providers/app-service/app-service';
import { TyNetworkServiceProvider } from '../providers/ty-network-service/ty-network-service';
import { DbServiceProvider } from '../providers/db-service/db-service';
import { WebDbServiceProvider } from '../providers/web-db-service/web-db-service';
import { WebTyNetworkServiceProvider } from '../providers/web-ty-network-service/web-ty-network-service';
import { DeviceIntefaceServiceProvider } from '../providers/device-inteface-service/device-inteface-service';
import { Camera } from '@ionic-native/camera';
import { PhotoLibrary } from '@ionic-native/photo-library';
import { HttpClient, HttpClientModule } from '../../node_modules/@angular/common/http';

export function netFactory(platform:Platform,loadingCtrl:LoadingController,http:Http,zone:NgZone,events?: Events,httpClient?:HttpClient) {
  if (platform.is("mobileweb")) {
    return new WebTyNetworkServiceProvider(http,loadingCtrl,events);
  }else if(platform.is("mobile")){
    return new TyNetworkServiceProvider(loadingCtrl,zone,events,httpClient);
  }else{
    return new WebTyNetworkServiceProvider(http,loadingCtrl,events);
  }
}
export function dbFactory(platform:Platform,zone:NgZone) {
  if (platform.is("mobileweb")) {
    return new WebDbServiceProvider(zone);
  }else if(platform.is("mobile")){
    return new DbServiceProvider(zone);
  }else{
    return new WebDbServiceProvider(zone);
  }
}

@NgModule({
  declarations: [
    MyApp,
  ],
  imports: [
    BrowserModule,
    HttpModule,
    HttpClientModule,
    IonicModule.forRoot(MyApp,{
      backButtonText: '', 
      iconMode: 'ios',//安卓icon强制使用ios的icon以及样式
      mode: 'ios',//样式强制使用ios样式
      // preloadModules: true
    }),
    ComponentsModule,
  ],
  bootstrap: [IonicApp],
  entryComponents: [
    MyApp,
  ],
  providers: [
    StatusBar,
    SplashScreen,
    Camera,
    {provide: ErrorHandler, useClass: IonicErrorHandler},
    AppServiceProvider,
    {provide:TyNetworkServiceProvider,useFactory:netFactory,
      deps:[Platform,LoadingController,Http,NgZone,Events,HttpClient]
    },
    {provide: DbServiceProvider,useFactory:dbFactory,
      deps:[Platform,NgZone]
    },
    {provide: LOCALE_ID, useValue: "zh-CN" },
    DeviceIntefaceServiceProvider,
    PhotoLibrary,
  ]
})
export class AppModule {}
