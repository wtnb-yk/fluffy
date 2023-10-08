#!/bin/zsh

## このファイルのあるディレクトリをカレントディレクトリにする
THIS_FILE_PATH=$(cd $(dirname $0); pwd)
cd $THIS_FILE_PATH

## fluffy-frontのディレクトリに移動する
FRONTEND_DIR="$THIS_FILE_PATH/../"
cd $FRONTEND_DIR
pnpm install && pnpm dev --host
