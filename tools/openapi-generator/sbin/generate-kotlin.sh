#!/bin/zsh -e

usage() {
    cat 1>&2 <<EOF
Usage: $(basename $0) [OPTIONS] [PRJ_NAME] [PACKAGE_NAME]

This generates Kotlin code from openapi.yml file, to

Parameters:
    PRJ_NAME      Project Name under fluffy
    PACKAGE_NAME  Package Name for generating code

Options:
    -s SERIALIZATION_LIBRARY Specify Serialization Library (jackson[default], moshi, gson, kotlinx-serialization)
    -t GENERATION_TARGETS Specify Generation targets (apis,models[default], apis, models)
EOF
}


## 固定値
GENERATOR_VERSION="v6.6.0"
GENERATOR_IMAGE="openapitools/openapi-generator-cli:${GENERATOR_VERSION}"

## オプション
SERIALIZATION_LIBRARY="jackson"
GENERATION_TARGETS="apis,models"
while getopts 's:t:h:' opt; do
  case "${opt}" in
    s) SERIALIZATION_LIBRARY="${OPTARG}" ;;
    t) GENERATION_TARGETS="${OPTARG}" ;;
    h) usage; exit -1;;
    \?) usage; exit 1;;
  esac
done
shift $(($OPTIND - 1))
## 引数
if [ $# -ne 2 ]; then
  usage; exit 1
fi
PRJ_NAME=$1
PACKAGE_NAME=$2
API_NAME="${PRJ_NAME}-api"

## ディレクトリ名の解決を正しく行うために実行されたファイルのディレクトリに移動
FILE_DIR=$(cd $(dirname $0); pwd)

## dockerコンテナにプロジェクトのルートをマウントするので取得
PRJ_ROOT_DIR=$(cd $(dirname $0); cd ../../../; pwd)

## apiサーバの存在チェック
if [ ! -d "${PRJ_ROOT_DIR}/${API_NAME}" ]; then
  echo "${API_NAME} dir must exist!!!"
  exit 1
fi

## dockerコンテナ内のパスを設定
PRJ_ROOT_ON_DOCKER_DIR="/fluffy"
API_DIR="${PRJ_ROOT_ON_DOCKER_DIR}/${API_NAME}"
SPEC_DIR="${PRJ_ROOT_ON_DOCKER_DIR}/${API_NAME}-spec"
SPEC_FILE="${SPEC_DIR}/${API_NAME}.yml"
TEMPLATE_CONFIG="${PRJ_ROOT_ON_DOCKER_DIR}/tools/openapi-generator/config.yml"

## open-api-generator-cliのコンテナを利用してコード生成
docker run --rm -v "${PRJ_ROOT_DIR}:/${PRJ_ROOT_ON_DOCKER_DIR}" ${GENERATOR_IMAGE} generate \
    -i "${SPEC_FILE}" \
    -g kotlin-spring \
    -o ${API_DIR} \
    -c ${TEMPLATE_CONFIG} \
    --global-property=${GENERATION_TARGETS} \
    --global-property=modelDocs=false \
    --global-property=apiTests=false \
    --global-property=apiDocs=false \
    --additional-properties=serializableModel=false \
    --additional-properties=serializationLibrary=${SERIALIZATION_LIBRARY} \
    --additional-properties=packageName=${PACKAGE_NAME} \
    --additional-properties=useTags=true \
    --additional-properties=enumPropertyNaming="original"
