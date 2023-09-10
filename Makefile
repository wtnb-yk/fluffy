APP :=
DO_MIGRATE := true
export DO_MIGRATE
setup:
	docker compose down
# TODO: web-setup に変更する
	docker compose up -d --build

codegen:
	./tools/openapi-generator/sbin/generate-kotlin.sh fluffy com.fluffy.fluffyapi.controller.gen