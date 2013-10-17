mikumikustudio-gdx
==================

MikuMikuStudio for libgdx.

## MikuMikuStudio-gdxとは
MikuMikuStudio-gdxはMikuMikuStudioのlibgdx用の実装です。libgdx上にMikuMikuStudioを実装する事により、iOS対応を実現させました。
現在MikuMikuStudio-gdxはMacとiOSに対応しています。その他の環境ではnative bulletライブラリを用意していないため、動作しません。もしその他の環境で動作させたい場合は、以下のリポジトリからgdx-bulletをコンパイルしてご使用下さい。
https://github.com/chototsu/libgdx-mikumikustudio

動作が安定した時点でこのリポジトリはMikuMikuStudioと統合する予定です。
## 実行方法
実行にはsbtが必要です。
まずプロジェクトのディレクトリでsbtを起動してください。
### Macで実行
    desktop/run
### iOSシミュレータで実行
    ios/iphone-sim
## native bulletライブラリについて
native bulletはlibgdxのgdx-bulletにMikuMikuStudioのnative bulletライブラリをマージした物を使用しています。ソースはこちらです。
https://github.com/chototsu/libgdx-mikumikustudio

## 注意
* RoboVMのJNIの実装が不完全なため、メモリリークが発生する可能性があります。
* 音楽再生、Input処理は未実装です。

## TODO
* gdx-bulletをRoboVMで完全に動作するよう修正する。
* Linux,Windows, Android用のgdx-bulletを用意する。
* 音楽再生、Input処理を実装する。

## License
new BSD license
