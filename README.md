mikumikustudio-gdx
==================

MikuMikuStudio for libgdx.

![Screenshot1](https://googledrive.com/host/0B_-F-kj1SY0cbGlnQUpMOV90ZDA/mikumikustudio-gdx-screenshot1.png)

[YouTube](http://youtu.be/l26_Wr7PtoU)

## MikuMikuStudio-gdxとは
MikuMikuStudio-gdxはMikuMikuStudioのlibgdx用の実装です。libgdx上にMikuMikuStudioを実装する事により、iOS対応を実現させました。
現在MikuMikuStudio-gdxはMacとWindows, Linux, Android, iOSに対応しています。
https://github.com/chototsu/libgdx-mikumikustudio

## 実行方法
実行にはsbtとRoboVMが必要です。下記のURLからダウンロードしインストールして下さい。

SBT
http://www.scala-sbt.org/release/docs/Getting-Started/Setup.html

RoboVM
http://www.robovm.org/

環境変数

ROBOVM_HOMEにRoboVMをインストールしたディレクトリをセットして下さい。

例

    export ROBOVM_HOME=~/robovm-0.0.9

プロジェクトのディレクトリでsbtを起動してください。

    sbt

### Windows, Mac, Linuxで実行
    desktop/run
### iOSシミュレータで実行
    ios/iphone-sim
### iOS端末（実機）で実行
    ios/device
### ipa作成
    ios/ipa
### Android端末で実行
    android/start
## native bulletライブラリについて
native bulletはlibgdxのgdx-bulletにMikuMikuStudioのnative bulletライブラリをマージした物を使用しています。ソースはこちらです。
https://github.com/chototsu/libgdx-mikumikustudio

## License
BSD
