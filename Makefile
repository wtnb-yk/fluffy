APP :=
DO_MIGRATE := true
export DO_MIGRATE
setup:
	docker compose down
# TODO: web-setup に変更する
	docker compose up --build -d api