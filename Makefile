APP :=
DO_MIGRATE := true
export DO_MIGRATE
setup:
	docker compose down
	docker compose up --build -d web-setup

codegen:
	./tools/openapi-generator/sbin/generate-kotlin.sh fluffy com.fluffy.fluffyapi.controller.gen